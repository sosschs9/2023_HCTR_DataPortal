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

    public RequestDTO buildRequest(String userId, DataDTO dataDTO){
        // Id, ReqDate, ReqStatus, IsDownload, UserId, DataId, ManagerId
        RequestDTO requestDTO = new RequestDTO(
                requestRepository.findLastIndex() + 1, new Date(), "Waiting",
                false, userId, dataDTO.getId(), dataDTO.getManagerId());
        return requestDTO;
    }
    public int insertRequest(RequestDTO requestDTO){
        return requestRepository.insertRequest(requestDTO);
    }

    public int acceptRequest(RequestDTO requestDTO) {
        String nowStatus = requestDTO.getReqStatus();
        if (nowStatus.equals("Complete"))
            return -1;
        else if (nowStatus.equals("Expired"))
            return -2;
        else return requestRepository.acceptRequest(requestDTO);
    }

    public List<RequestItem> findAllRequestById(String userId) {
        // 유저 아이디 존재하는지
        // 유저 역할 -> 0이면 매니저: 요청 목록 전부 받아오기 , -> 1 이면 본인이 요청한 목록 전부 받아오기
        int checkRole = userRepository.checkRole(userId);
        if (checkRole == 0) return requestRepository.findAllRequest();
        else if (checkRole == 1) return requestRepository.findAllRequestById(userId);
        else return null;
    }
}
