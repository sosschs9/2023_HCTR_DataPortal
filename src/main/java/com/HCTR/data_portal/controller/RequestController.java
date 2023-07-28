package com.HCTR.data_portal.controller;

import com.HCTR.data_portal.dto.RequestDTO;
import com.HCTR.data_portal.service.RequestService;
import com.HCTR.data_portal.vo.Response.DataList;
import com.HCTR.data_portal.vo.Response.RequestItem;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RequestController {
    private final RequestService requestService;

    // 승인 요청하기 (유저)
    @PostMapping("/request")
    public ResponseEntity<?> requestData(
            @RequestHeader("userId") String userId,
            @RequestParam int dataId){
        System.out.println("Request Data");
        Map<String, Object> msg = new HashMap<>();

        // DB에 저장할 requestDTO 인스턴스 생성
        RequestDTO requestDTO = requestService.buildRequest(userId, dataId);
        if (requestDTO == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Already Requested");

        int res = requestService.insertRequest(requestDTO);
        if (res > 0) {
            msg.put("RequestId", requestDTO.getId());
            return ResponseEntity.status(HttpStatus.OK).body(msg);
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Data Request Failure");
    }
    
    // 요청 승인하기 (관리자)
    @PutMapping("/request")
    public ResponseEntity<?> acceptRequest(@RequestParam int requestId){
        System.out.println("Accept Request");
        String msg;

        // RequestId에 해당하는 요청 상태 Complete로 변경
        int res = requestService.acceptRequest(requestId);

        if (res > 0) return ResponseEntity.status(HttpStatus.OK).body("Success");
        else if (res == -1) msg = "Error: This Request Has Already Complete.";
        else if (res == -2) msg = "Error: This Request Has Been Expired.";
        else msg = "Error: Accept Request Failure";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }

    // 요청 목록 불러오기
    // 관리자 -> 모든 유저의 요청 목록
    @GetMapping("/request")
    public ResponseEntity<?> findAllRequest(@RequestHeader("userId") String userId) {
        System.out.println("Find All Request List");
        List<RequestItem> requestList = requestService.findAllRequestByUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(new DataList<>(requestList.size(), requestList));
    }
}
