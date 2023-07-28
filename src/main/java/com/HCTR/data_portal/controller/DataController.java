package com.HCTR.data_portal.controller;

import com.HCTR.data_portal.dto.*;
import com.HCTR.data_portal.service.DataService;
import com.HCTR.data_portal.service.HdfsService;
import com.HCTR.data_portal.service.MinIOService;
import com.HCTR.data_portal.service.RequestService;
import com.HCTR.data_portal.vo.Request.EarthQuakeVO;
import com.HCTR.data_portal.vo.Request.NormalVO;
import com.HCTR.data_portal.vo.Response.DataList;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api", produces = "application/json; charset=utf8")
public class DataController {
    private final DataService dataService;
    private final HdfsService hdfsService;
    private final MinIOService minIOService;
    private final RequestService requestService;

    // 지진 발생 시 데이터 저장하기
    @PostMapping("/data-earthquake")
    public ResponseEntity<?> uploadEarthQuake(
            @RequestPart("text") EarthQuakeVO earthQuakeVO,
            @RequestPart("ZipFile") MultipartFile zipFile,
            @RequestPart("MapImage") MultipartFile mapImage,
            @RequestPart("TimeSeries") MultipartFile timeSeries,
            @RequestPart("SensorInfo") MultipartFile sensorInfo) throws Exception {
        // 실행 시간 확인
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        System.out.println("Upload EarthQuake Data");
        Map<String, Object> msg = new HashMap<>();
        String errMsg;

        // 파일이 저장될 디렉토리 경로 설정 Ex. /EQ/2021/(데이터번호)_(지역이름)
        String filePath = "/EQ/" + earthQuakeVO.getYear() + "/" + hdfsService.countHdfsFile("/EQ/" + earthQuakeVO.getYear()) + "_" + earthQuakeVO.getLocation();
        // HDFS에 ACL 설정
        //hdfsService.setHdfsACL("/EQ/2021/9_전라남도", "user");

        // 저장할 파일 리스트 객체 생성
        MultipartFile[] fileList = new MultipartFile[]{zipFile, mapImage, timeSeries, sensorInfo};
        List<Future<String>> futureResults = new ArrayList<>();
        for (MultipartFile file : fileList){
            // 하둡에 파일 저장 (비동기)
            Future<String> futureResult = hdfsService.uploadHdfs(file, filePath);
            futureResults.add(futureResult);
        }
        // 모든 스레드 작업의 결과를 기다림
        for (Future<String> future : futureResults) {
            try {
                String result = future.get();
                System.out.println(result); // File upload completed
            } catch (InterruptedException | ExecutionException e) {
                errMsg = "Error: Hdfs Upload Failure.";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errMsg);
            }
        }
        minIOService.uploadMinIO(mapImage, timeSeries, filePath);

        // MariaDB에 저장하기 위한 멤버 변수 설정
        earthQuakeVO.setHdfsPath(filePath, mapImage.getOriginalFilename(), timeSeries.getOriginalFilename(), sensorInfo.getOriginalFilename());

        int result = dataService.uploadEarthQuakeVO(earthQuakeVO);
        if (result > 0) {
            // result => DataId를 가리킴
            msg.put("DataId", result);
            // 실행 시간 확인
            stopWatch.stop();
            System.out.println(stopWatch.prettyPrint());

            return ResponseEntity.status(HttpStatus.CREATED).body(msg);
        }
        else if (result == -1) errMsg = "Error: Data Table Insert Failure.";
        else if (result == -2) errMsg = "Error: EarthQuake_Data Table Insert Failure.";
        else errMsg = "Error: File Upload Fail. Check your data file";

        // 실행 시간 확인
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errMsg);
    }

    // 평시 데이터 저장하기
    @PostMapping("/data-normal")
    public ResponseEntity<?> uploadNormal(
            @RequestPart("text") NormalVO normalVO,
            @RequestPart("DescriptionImage") MultipartFile descriptionImage,
            @RequestPart("Chart") MultipartFile chart)  throws Exception {
        System.out.println("Upload Normal Data");
        Map<String, Object> msg = new HashMap<>();
        String errMsg;

        // 파일이 저장될 디렉토리 경로 설정 Ex. /EQ/2021/(데이터번호)_(지역이름)
        String filePath = "/NM/" + normalVO.getYear() + "/" + hdfsService.countHdfsFile("/NM/" + normalVO.getYear()) + "_" + normalVO.getLocation();

        // 저장할 파일 리스트 객체 생성
        MultipartFile[] fileList = new MultipartFile[]{descriptionImage, chart};
        List<Future<String>> futureResults = new ArrayList<>();
        for (MultipartFile file : fileList){
            // 하둡에 파일 저장 (비동기)
            Future<String> futureResult = hdfsService.uploadHdfs(file, filePath);
            futureResults.add(futureResult);
        }
        // 모든 스레드 작업의 결과를 기다림
        for (Future<String> future : futureResults) {
            try {
                String result = future.get();
                System.out.println(result); // File upload completed
            } catch (InterruptedException | ExecutionException e) {
                errMsg = "Error: Hdfs Upload Failure.";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errMsg);
            }
        }

        minIOService.uploadMinIO(descriptionImage, chart, filePath);

        // MariaDB에 저장하기 위한 멤버 변수 설정
        normalVO.setHdfsPath(filePath, descriptionImage.getOriginalFilename(), chart.getOriginalFilename());

        int result = dataService.uploadNormalVO(normalVO);
        if (result > 0) {
            // result => DataId를 가리킴
            msg.put("DataId", result);
            return ResponseEntity.status(HttpStatus.CREATED).body(msg);
        }
        else if (result == -1) errMsg = "Error: Data Table Insert Failure.";
        else if (result == -2) errMsg = "Error: Normal_Data Table Insert Failure.";
        else errMsg = "Error: File Upload Fail. Check your data file";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errMsg);
    }

    // 데이터 목록 불러오기
    @GetMapping("/data")
    public ResponseEntity<?> findAllData(){
        System.out.println("Find Data List");
        List<DataDTO> dataList = dataService.findAllData();

        return ResponseEntity.status(HttpStatus.OK).body(new DataList<>(dataList.size(), dataList));
    }

    // 데이터 상세 조회
    @GetMapping("/data/{DataId}")
    public ResponseEntity<?> findDataById(@PathVariable("DataId") int dataId){
        System.out.println("About Data Id: " + dataId);

        Object res = dataService.viewDetailData(dataId);
        if (res == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("올바르지 않은 접근입니다. 정상적인 경로로 다시 접속해 주세요");
        else return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // 데이터 다운로드
    @GetMapping(value = "/download/{RequestId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> downloadData(
            @PathVariable("RequestId") int requestId,
            HttpServletResponse response, HttpServletRequest request) {
        System.out.println("Download Data");

        // requestId를 통해 requestDTO 가져오기
        RequestDTO requestDTO = requestService.findRequestById(requestId);
        if (requestDTO == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("올바르지 않은 접근입니다. 정상적인 경로로 다시 접속해 주세요");

        // 요청 상태 확인 및 현재 사용자가 요청한 데이터가 맞는지 확인
        if (requestDTO.getReqStatus().equals("Complete")){
            // 맞으면 DataId를 통해 hdfsFilePath 가져오기
            String hdfsFilePath = dataService.findDataById(requestDTO.getDataId()).getHdfsFilePath();
            String onlyFileName = hdfsFilePath.replace("/", "_").substring(1) + ".zip";

            // hdfsFilePath에서 하둡에 있는 데이터 가져와 클라이언트에게 전송
            String agent = request.getHeader("User-Agent");
            if (agent.contains("Trident"))//Internet Explore
                onlyFileName = URLEncoder.encode(onlyFileName, StandardCharsets.UTF_8).replaceAll("\\+", " ");
            else if (agent.contains("Edge")) //Micro Edge
                onlyFileName = URLEncoder.encode(onlyFileName, StandardCharsets.UTF_8);
            else //Chrome
                onlyFileName = new String(onlyFileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

            response.setHeader("Content-Type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + onlyFileName);
            hdfsService.readHdfsFile(hdfsFilePath, response);

            // 다운로드 로직 성공 시 Request IsDownload 필드 true로 업데이트 하기
            requestService.downloadData(requestId);
        }
        return null;
    }
}
