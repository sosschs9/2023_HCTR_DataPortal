package com.HCTR.data_portal.service;

import com.HCTR.data_portal.dto.DataDTO;
import com.HCTR.data_portal.dto.EarthQuakeDTO;
import com.HCTR.data_portal.dto.NormalDTO;
import com.HCTR.data_portal.repository.DataRepository;
import com.HCTR.data_portal.vo.Request.EarthQuakeVO;
import com.HCTR.data_portal.vo.Request.NormalVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DataService {
    private final DataRepository dataRepository;
    public int requestNormalVO(NormalVO normalVO){
        DataDTO dataDTO = normalVO.buildData(normalVO);
        NormalDTO normalDTO = normalVO.buildNormal(normalVO);

        // Data Table 저장
        int dataInsert = dataRepository.insertData(dataDTO);
        if (dataInsert < 0) return -1;

        int DataId = dataRepository.findLastIndex();
        normalDTO.setDataId(DataId);

        // Normal_Data Table 저장
        int normalInsert = dataRepository.insertNormal(normalDTO);
        if (normalInsert < 0) return -2;

        // 업로드 성공
        return DataId;
    }
    public int requestEarthQuakeVO(EarthQuakeVO earthQuakeVO){
        DataDTO dataDTO = earthQuakeVO.buildData(earthQuakeVO);
        EarthQuakeDTO earthQuakeDTO = earthQuakeVO.buildEarthQuake(earthQuakeVO);

        // Data Table 저장
        int dataInsert = dataRepository.insertData(dataDTO);
        if (dataInsert < 0) return -1;

        int DataId = dataRepository.findLastIndex();
        earthQuakeDTO.setDataId(DataId);

        // EarthQuake_Data Table 저장
        int earthQuakeInsert = dataRepository.insertEarthQuake(earthQuakeDTO);
        if (earthQuakeInsert < 0) return -2;

        // 업로드 성공
        return DataId;
    }

    public List<DataDTO> findAllData(){
        return dataRepository.findAllData();
    }

}
