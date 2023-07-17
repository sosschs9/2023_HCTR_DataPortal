package com.HCTR.data_portal.vo.Request;

import com.HCTR.data_portal.dto.DataDTO;
import com.HCTR.data_portal.dto.NormalDTO;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@ToString
public class NormalVO {
    private final String Title;
    private final String HdfsFilePath;
    private final String Location;
    private final String DetailLocation;
    private final String Description;
    private final String DescriptionImage;
    private final String Chart;
    private final Date EventData;

    public NormalVO(
            String title, String hdfsFilePath, String location, String detailLocation,
            String description, String descriptionImage, String chart, Date eventData){
        this.Title = title;
        this.HdfsFilePath = hdfsFilePath;
        this.Location = location;
        this.DetailLocation = detailLocation;
        this.Description = description;
        this.DescriptionImage = descriptionImage;
        this.Chart = chart;
        this.EventData = eventData;
    }

    public DataDTO buildData(NormalVO normalVO){
        DataDTO dataDTO = new DataDTO();

        dataDTO.setHdfsFilePath(normalVO.getHdfsFilePath());
        dataDTO.setTitle(normalVO.getTitle());
        dataDTO.setEventDate(normalVO.getEventData());
        dataDTO.setDataType(1);
        dataDTO.setViews(0);
        dataDTO.setLocation(normalVO.getLocation());
        dataDTO.setDetailLocation(normalVO.getDetailLocation());
        dataDTO.setManagerId("manager");

        return dataDTO;
    }

    public NormalDTO buildNormal(NormalVO normalVO){
        NormalDTO normalDTO = new NormalDTO();

        normalDTO.setDescription(normalVO.getDescription());
        normalDTO.setDescriptionImage(normalVO.getDescriptionImage());
        normalDTO.setChart(normalVO.getChart());

        return normalDTO;
    }
}
