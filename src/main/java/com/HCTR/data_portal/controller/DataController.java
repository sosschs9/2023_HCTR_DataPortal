package com.HCTR.data_portal.controller;

import com.HCTR.data_portal.dto.DataDTO;
import com.HCTR.data_portal.service.DataService;
import com.HCTR.data_portal.vo.Request.EarthQuakeVO;
import com.HCTR.data_portal.vo.Request.NormalVO;
import com.HCTR.data_portal.vo.Response.DataList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dataportal")
public class DataController {
    private final DataService dataService;

    // 지진 발생 시 데이터 저장하기
    @PostMapping("/data-earthquake")
    public ResponseEntity<?> uploadEarthQuake(
            @RequestPart("text") EarthQuakeVO earthQuakeVO,
            @RequestPart("file") MultipartFile[] fileDataArr) throws IOException {
        System.out.println("Upload EarthQuake Data");
        Map<String, Object> msg = new HashMap<>();

        // 이거는 컨트롤러에 냅둘지 ?
        earthQuakeVO.setMapImage(fileDataArr[0].getOriginalFilename());
        earthQuakeVO.setTimeSeries(fileDataArr[1].getOriginalFilename());
        earthQuakeVO.setSensorInfo(fileDataArr[2].getOriginalFilename());

        // 한글 파일일 경우 인코딩 한 후에 디코딩한 파일명을 받아와야 함.
        String encodedFileName = URLEncoder.encode(fileDataArr[0].getOriginalFilename(), StandardCharsets.UTF_8.toString());
        String decodedFileName = URLDecoder.decode(encodedFileName, StandardCharsets.UTF_8.toString());

        // 원하는 파일 위치 + 파일명
        // pk : 데이터의 총 갯수 +1
        //String filePath = "/EQ/2021/" + pk + "text에 있는 location"
        // String filePath = "C:\\test\\" + decodedFileName;
        // fileDataArr[0].transferTo(new File(filePath));
        // File file1 = New File([syj);


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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }

    // 평시 데이터 저장하기
    @PostMapping("/data-normal")
    public ResponseEntity<?> uploadNormal(
            @RequestPart("text") NormalVO normalVO,
            @RequestPart("file") MultipartFile[] fileDataArr) throws IOException {
        System.out.println("Upload Normal Data");
        Map<String, Object> msg = new HashMap<>();

        // base64인코딩 된 이미지 파일을 디코딩하여 hdfs에 저장하는 과정 필요함.
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

        System.out.println(dataList.get(0).getEventDate());

        return ResponseEntity.status(HttpStatus.OK).body(new DataList<>(dataList.size(), dataList));
    }

    // 데이터 상세 조회
    @GetMapping("/data/{DataId}")
    public ResponseEntity<?> findDataById(@PathVariable("DataId") int DataId){
        System.out.println("About Data Id: " + DataId);
        // 제목, 위치, 상세 위치, 위도, 경도, 규모
        // 맵, 시계열 이미지 파일 가져오기
        // view 횟수 증가
        return null;
    }


    // 데이터 다운로드
    // PUT /dataportal/mypage/data
}
