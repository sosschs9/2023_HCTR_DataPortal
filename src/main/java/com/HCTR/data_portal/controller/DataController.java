package com.HCTR.data_portal.controller;

import com.HCTR.data_portal.dto.*;
import com.HCTR.data_portal.service.DataService;
import com.HCTR.data_portal.service.HdfsService;
import com.HCTR.data_portal.vo.Request.EarthQuakeVO;
import com.HCTR.data_portal.vo.Request.NormalVO;
import com.HCTR.data_portal.vo.Response.DataList;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DataController {
    private final HttpSession httpSession;
    private final DataService dataService;
    private final HdfsService hdfsService;

    // 지진 발생 시 데이터 저장하기
    @PostMapping("/data-earthquake")
    public ResponseEntity<?> uploadEarthQuake(
            @RequestPart("text") EarthQuakeVO earthQuakeVO,
            @RequestPart("ZipFile") MultipartFile zipFile,
            @RequestPart("MapImage") MultipartFile mapImage,
            @RequestPart("TimeSeries") MultipartFile timeSeries,
            @RequestPart("SensorInfo") MultipartFile sensorInfo) {
        // 실행 시간 확인
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        System.out.println("Upload EarthQuake Data");
        Map<String, Object> msg = new HashMap<>();

        // 파일이 저장될 디렉토리 경로 설정 Ex. /EQ/2021/(데이터번호)_(지역이름)
        String filePath = "/EQ/" + earthQuakeVO.getYear() + "/" + hdfsService.countHdfsFile("/EQ/" + earthQuakeVO.getYear()) + "_" + earthQuakeVO.getLocation();
        // HDFS에 ACL 설정
        //hdfsService.setHdfsACL("/EQ", "user");

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
                msg.put("Error", "Hdfs Upload Failure.");
            }
        }

        // MariaDB에 저장하기 위한 멤버 변수 설정
        earthQuakeVO.setHdfsFilePath(filePath);
        earthQuakeVO.setMapImage(filePath + "/" + mapImage.getOriginalFilename());
        earthQuakeVO.setTimeSeries(filePath + "/" + timeSeries.getOriginalFilename());
        earthQuakeVO.setSensorInfo(filePath + "/" + sensorInfo.getOriginalFilename());

        int result = dataService.uploadEarthQuakeVO(earthQuakeVO);
        if (result > 0) {
            // result => DataId를 가리킴
            msg.put("DataId", result);
            return ResponseEntity.status(HttpStatus.CREATED).body(msg);
        } else if (result == -1){
            // Data DB 저장 오류
            msg.put("Error", "Data Table Insert Failure.");
        } else if (result == -2) {
            // Normal DB 저장 오류
            msg.put("Error", "EarthQuake_Data Table Insert Failure.");
        } else {
            msg.put("Error", "File Upload Fail. Check your data file");
        }

        // 실행 시간 확인
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }

    // 평시 데이터 저장하기
    @PostMapping("/data-normal")
    public ResponseEntity<?> uploadNormal(
            @RequestPart("text") NormalVO normalVO,
            @RequestPart("file") MultipartFile[] fileDataArr) {
        System.out.println("Upload Normal Data");
        Map<String, Object> msg = new HashMap<>();

        normalVO.setDescriptionImage(fileDataArr[0].getOriginalFilename());
        normalVO.setChart(fileDataArr[1].getOriginalFilename());

        System.out.println(normalVO);
        int res = dataService.uploadNormalVO(normalVO);


        if (res > 0) {
            // result => DataId를 가리킴
            msg.put("DataId", res);
            return ResponseEntity.status(HttpStatus.CREATED).body(msg);
        } else if (res == -1){
            // Data DB 저장 오류
            msg.put("Error", "Data Table Insert Failure.");
        } else if (res == -2) {
            // Normal DB 저장 오류
            msg.put("Error", "Normal_Data Table Insert Failure.");
        } else {
            msg.put("Error", "File Upload Fail. Check your data file");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }

    // 데이터 목록 불러오기
    @GetMapping("/data")
    public ResponseEntity<?> findAllData(){
        System.out.println("Find Data List");
        List<DataDTO> dataList = dataService.findAllData();

        if(httpSession.getAttribute("MANAGER") == null){
            new Exception();
        }

        return ResponseEntity.status(HttpStatus.OK).body(new DataList<>(dataList.size(), dataList));
    }

    // 데이터 상세 조회
    @GetMapping("/data/{DataId}")
    public ResponseEntity<?> findDataById(@PathVariable("DataId") int dataId){
        System.out.println("About Data Id: " + dataId);
        // 제목, 위치, 상세 위치, 위도, 경도, 규모
        // 맵, 시계열 이미지 파일 가져오기
        // view 횟수 증가

        // 아이디로 DataDTO 불러오기
        // Data type 지진이면 지진DTO 불러오기, 평상시면 평시DTO 불러오기
        DataDTO dataDTO = dataService.findData(dataId);
        System.out.println(dataDTO);

        if (dataDTO.getDataType() == 0){
            EarthQuakeDTO earthQuakeDTO = dataService.findEarthQuakeData(dataId);
            byte imageBytes[] = hdfsService.readImageFromHdfs(earthQuakeDTO.getMapImage());
            Base64.Encoder encoder = Base64.getEncoder();
            String base64Encode = "data:image/gif;base64," + encoder.encodeToString(imageBytes);
            System.out.println(base64Encode);

            if (imageBytes != null){
                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.IMAGE_GIF);
//                headers.setContentLength(imageBytes.length);

                return new ResponseEntity<>(base64Encode, headers, HttpStatus.OK);
            } else return ResponseEntity.notFound().build();
        } else {
            NormalDTO normalDTO = dataService.findNormalData(dataId);
        }
        return null;
    }


    // 데이터 다운로드
    // PUT /dataportal/mypage/data
}
