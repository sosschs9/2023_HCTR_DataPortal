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

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dataportal")
public class DataController {
    private final DataService dataService;

    @PostMapping("/data-earthquake")
    public ResponseEntity<?> uploadEarthQuake(@RequestBody EarthQuakeVO earthQuakeVO) {
        System.out.println("Upload EarthQuake Data");
        Map<String, Object> msg = new HashMap<>();

        // base64인코딩 된 이미지 파일을 디코딩하여 hdfs에 저장하는 과정 필요함.
        // 이를 서비스에서 할지 컨트롤러에서 할지

        int result = dataService.requestEarthQuakeVO(earthQuakeVO);

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

    @PostMapping("/data-normal")
    public ResponseEntity<?> uploadNormal(@RequestBody NormalVO normalVO) {
        System.out.println("Upload Normal Data");
        Map<String, Object> msg = new HashMap<>();

        // base64인코딩 된 이미지 파일을 디코딩하여 hdfs에 저장하는 과정 필요함.
        // 이를 서비스에서 할지 컨트롤러에서 할지

        int res = dataService.requestNormalVO(normalVO);

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

    @GetMapping("/data")
    public ResponseEntity<?> findAllData(){
        List<DataDTO> dataList = dataService.findAllData();

        System.out.println(dataList.get(0).getEventDate());

        return ResponseEntity.status(HttpStatus.OK).body(new DataList<>(dataList.size(), dataList));
    }


    @GetMapping("/data/{DataId}")
    public ResponseEntity<?> findDataById(@PathVariable("DataId") int DataId){
        // 제목, 위치, 상세 위치, 위도, 경도, 규모
        // 맵, 시계열 이미지 파일 가져오기
        // view 횟수 증가
        return null;
    }

    // 파일 다운로드 메소드 작성 필요
    // PUT /dataportal/mypage/data
}
