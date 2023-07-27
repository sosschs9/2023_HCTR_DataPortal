package com.HCTR.data_portal.service;

import com.HCTR.data_portal.dto.DataDTO;
import com.HCTR.data_portal.dto.RequestDTO;
import com.HCTR.data_portal.repository.RequestRepository;
import com.HCTR.data_portal.repository.UserRepository;
import com.HCTR.data_portal.vo.Response.RequestItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    // RequestDTO 인스턴스 생성
    public RequestDTO buildRequest(String userId, int dataId){
        // 해당 유저가 이미 요청한(waiting) 데이터인지 확인
        if (requestRepository.isAlreadyRequest(userId, dataId)) return null;
        return new RequestDTO(
                requestRepository.findLastIndex() + 1, new Date(), "Waiting",
                false, userId, dataId, "manager");
    }
    public int insertRequest(RequestDTO requestDTO){
        if (requestDTO == null) return -1;  // 예외 확인
        return requestRepository.insertRequest(requestDTO);
    }

    // 요청 승인
    public int acceptRequest(int requestId) {
        RequestDTO requestDTO = requestRepository.findRequestById(requestId);
        // 현재 요청 상태 검사
        String nowStatus = requestDTO.getReqStatus();
        if (nowStatus.equals("Complete"))
            return -1;  // 이미 승인 완료된 데이터
        else if (nowStatus.equals("Expired"))
            return -2;  // 만료된 데이터
        else return requestRepository.acceptRequest(requestDTO);    // Waiting -> Complete
    }

    // 요청 목록 불러오기
    public List<RequestItem> findAllRequestById(String userId) {
        // 유저 역할 검사
        // -> 0 이면 매니저: 요청 목록 전부 받아오기 , -> 1 이면 userId가 요청한 목록 전부 받아오기
        int checkRole = userRepository.checkRole(userId);
        if (checkRole == 0) return requestRepository.findAllRequest();
        else if (checkRole == 1) return requestRepository.findAllRequestByUser(userId);
        else return null;
    }
}
