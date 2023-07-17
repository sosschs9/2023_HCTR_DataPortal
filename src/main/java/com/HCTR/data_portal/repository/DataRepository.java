package com.HCTR.data_portal.repository;

import com.HCTR.data_portal.dto.DataDTO;
import com.HCTR.data_portal.dto.EarthQuakeDTO;
import com.HCTR.data_portal.dto.NormalDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class DataRepository {
    private final SqlSessionTemplate sql;
    public int findLastIndex(){
        Integer res = sql.selectOne("data.findLastReqIndex");
        if (res == null) return 0;
        else return res;
    }
    public int insertData(DataDTO dataDTO){
        return sql.insert("data.insertData", dataDTO);
    }
    public int insertNormal(NormalDTO normalDTO){
        return sql.insert("data.insertNormal",normalDTO);
    }
    public int insertEarthQuake(EarthQuakeDTO earthQuakeDTO) { return sql.insert("data.insertEarthQuake", earthQuakeDTO);}
    public List<DataDTO> findAllData() {
        return sql.selectList("data.findAllData");
    }
}
