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

    public int uploadNormalVO(NormalVO normalVO){
        DataDTO dataDTO = normalVO.buildData();
        NormalDTO normalDTO = normalVO.buildNormal();

        int DataId = dataRepository.findLastIndex() + 1;
        dataDTO.setId(DataId);
        normalDTO.setDataId(DataId);

        // Data Table 저장
        int dataInsert = dataRepository.insertData(dataDTO);
        if (dataInsert < 0) return -1;  // Data Table 업로드 실패

        // Normal_Data Table 저장
        int normalInsert = dataRepository.insertNormal(normalDTO);
        if (normalInsert < 0) return -2;    // Normal_Data Table 업로드 실패

        // 업로드 성공
        return DataId;
    }
    public int uploadEarthQuakeVO(EarthQuakeVO earthQuakeVO){
        DataDTO dataDTO = earthQuakeVO.buildData();
        EarthQuakeDTO earthQuakeDTO = earthQuakeVO.buildEarthQuake();

        int DataId = dataRepository.findLastIndex() + 1;
        dataDTO.setId(DataId);
        earthQuakeDTO.setDataId(DataId);

        // Data Table 저장
        int dataInsert = dataRepository.insertData(dataDTO);
        if (dataInsert < 0) return -1;  // Data Table 업로드 실패

        // EarthQuake_Data Table 저장
        int earthQuakeInsert = dataRepository.insertEarthQuake(earthQuakeDTO);
        if (earthQuakeInsert < 0) return -2;    // Normal_Data Table 업로드 실패

        // 업로드 성공
        return DataId;
    }

    public List<DataDTO> findAllData(){
        return dataRepository.findAllData();
    }
    public DataDTO findData(int dataId) {
        return dataRepository.findData(dataId);
    }
    public EarthQuakeDTO findEarthQuakeData(int dataId) {
        return dataRepository.findEarthQuakeData(dataId);
    }
    public NormalDTO findNormalData(int dataId) {
        return dataRepository.findNormalData(dataId);
    }

}
