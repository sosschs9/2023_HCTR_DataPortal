package com.HCTR.data_portal.controller;

import com.HCTR.data_portal.dto.*;
import com.HCTR.data_portal.service.DataService;
import com.HCTR.data_portal.service.HdfsService;
import com.HCTR.data_portal.vo.Request.EarthQuakeVO;
import com.HCTR.data_portal.vo.Request.NormalVO;
import com.HCTR.data_portal.vo.Response.DataList;

import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api", produces = "application/json; charset=utf8")
public class DataController {
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
        String errMsg;

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
                errMsg = "Error: Hdfs Upload Failure.";
            }
        }

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

        return ResponseEntity.status(HttpStatus.OK).body(new DataList<>(dataList.size(), dataList));
    }

    // 데이터 상세 조회
    @GetMapping("/data/{DataId}")
    public ResponseEntity<?> findDataById(@PathVariable("DataId") int dataId){
        System.out.println("About Data Id: " + dataId);

        Object res = dataService.findData(dataId);
        if (res == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("올바르지 않은 접근입니다. 정상적인 경로로 다시 접속해 주세요");
        else return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping("/minio")
    public ResponseEntity<?> minioTest(@RequestPart("file") MultipartFile testFile) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("http://155.230.118.227:9000")
                            .credentials("minioadmin", "minioadmin")
                            .build();
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket("dataportal").build());
            if (!found) {
                // Make a new bucket called 'dataportal'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("dataportal").build());
            } else {
                System.out.println("Bucket 'dataportal' already exists.");
            }

            String dataPath = "/EQ/2021/test/" + testFile.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder().bucket("dataportal").object(dataPath).stream(
                                    testFile.getInputStream(), testFile.getSize(), -1)
                            .contentType(testFile.getContentType())
                            .build());
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }

        return ResponseEntity.status(HttpStatus.OK).body("test Ok");
    }


    // 데이터 다운로드
    // PUT /api/data
}
