package com.HCTR.data_portal.service;

import com.HCTR.data_portal.dto.DataDTO;
import com.HCTR.data_portal.dto.EarthQuakeDTO;
import com.HCTR.data_portal.dto.NormalDTO;
import com.HCTR.data_portal.repository.DataRepository;
import com.HCTR.data_portal.vo.Request.EarthQuakeVO;
import com.HCTR.data_portal.vo.Request.NormalVO;
import com.HCTR.data_portal.vo.Response.EarthQuakeItem;
import com.HCTR.data_portal.vo.Response.NormalItem;
import lombok.Data;
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

    public Object findData(int dataId) {
        // dataId로 dataDto 가져오기
        DataDTO dataDTO = dataRepository.findData(dataId);
        if (dataDTO == null) return null;
        else dataRepository.countView(dataId);  // 불러온 객체가 있으면 view count + 1

        String imgUrlRoot = "http://155.230.118.227:9000/dataportal";  // minIO 스토리지 서버 주소
        if (dataDTO.getDataType() == 0) {
            EarthQuakeDTO earthQuakeDTO = dataRepository.findEarthQuakeData(dataId);
            // 클라이언트에게 전송할 객체 생성
            EarthQuakeItem resItem =
                    new EarthQuakeItem(
                            dataDTO.getTitle(), dataDTO.getLocation(), dataDTO.getDetailLocation(),
                            dataDTO.getEventDate(), earthQuakeDTO.getLatitude(), earthQuakeDTO.getLongtitude(),
                            earthQuakeDTO.getScale(), earthQuakeDTO.getMapImage(), earthQuakeDTO.getTimeSeries()
                    );
            resItem.setUrl(imgUrlRoot);
            return resItem;
        }
        else if(dataDTO.getDataType() == 1) {
            NormalDTO normalDTO = dataRepository.findNormalData(dataId);
            // 클라이언트에게 전송할 객체 생성
            NormalItem resItem =
                    new NormalItem(
                            dataDTO.getTitle(), dataDTO.getLocation(), dataDTO.getDetailLocation(),
                            dataDTO.getEventDate(), normalDTO.getDescription(),
                            normalDTO.getDescriptionImage(), normalDTO.getChart());
            resItem.setUrl(imgUrlRoot);
            return resItem;
        }
        return null;
    }
}
