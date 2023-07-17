package com.HCTR.data_portal.repository;

import com.HCTR.data_portal.dto.RequestDTO;
import com.HCTR.data_portal.vo.Response.RequestItem;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class RequestRepository {
    private final SqlSessionTemplate sql;
    public int findLastIndex(){
        Integer res = sql.selectOne("request.findLastReqIndex");
        if (res == null) return 0;
        else return res;
    }
    public int insertRequest(RequestDTO requestDTO) {
        return sql.insert("request.insertRequest", requestDTO);
    }
    public int acceptRequest(RequestDTO requestDTO) {
        return sql.update("request.acceptRequest", requestDTO);
    }
    public List<RequestItem> findAllRequest(){
        return sql.selectList("request.findAllRequest");
    }
    public List<RequestItem> findAllRequestById(String userId) {
        return sql.selectList("request.findAllRequestById", userId);
    }
}
