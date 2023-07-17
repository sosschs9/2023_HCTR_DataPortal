package com.HCTR.data_portal.controller;

import com.HCTR.data_portal.dto.DataDTO;
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
@RequestMapping("/dataportal")
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/request")
    public ResponseEntity<?> requestData(
            @RequestHeader("userId") String userId,
            @RequestBody DataDTO dataDTO){
        System.out.println("Request Data");
        Map<String, Object> msg = new HashMap<>();

        RequestDTO requestDTO = requestService.buildRequest(userId, dataDTO);
        int res = requestService.insertRequest(requestDTO);

        if (res > 0) {
            msg.put("RequestId", requestDTO.getId());
            return ResponseEntity.status(HttpStatus.OK).body(msg);
        } else {
            msg.put("Error", "Data Request Failure");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }

    }

    @PutMapping("/request")
    public ResponseEntity<?> acceptRequest(@RequestBody RequestDTO requestDTO){
        // RequestId에 해당하는 요청 상태 Complete으로 변경
        System.out.println("Accept Request");
        Map<String, Object> msg = new HashMap<>();

        int res = requestService.acceptRequest(requestDTO);

        if (res > 0) {
            msg.put("RequestId", requestDTO.getId());
            return ResponseEntity.status(HttpStatus.OK).body(msg);
        } else if (res == -1){
            msg.put("Error", "This Request Has Already Complete.");
        } else if (res == -2) {
            msg.put("Error", "This Request Has Been Expired.");
        } else {
            msg.put("Error", "Accept Request Failure");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }

    @GetMapping("/request")
    public ResponseEntity<?> findAllRequest(@RequestHeader("userId") String userId) {
        System.out.println("Find All Request List");
        Map<String, Object> msg = new HashMap<>();

        List<RequestItem> requestList = requestService.findAllRequestById(userId);
        System.out.println(requestList);

        return ResponseEntity.status(HttpStatus.OK).body(new DataList<>(requestList.size(), requestList));
    }
}
