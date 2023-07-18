package com.HCTR.data_portal.vo.Request;

import com.HCTR.data_portal.dto.DataDTO;
import com.HCTR.data_portal.dto.NormalDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonPropertyOrder({
        "Title", "HdfsFilePath", "Location", "DetailLocation",
        "EventDate", "Description", "DescriptionImage", "Chart" })
public class NormalVO {
    @JsonSetter("Title")
    private String Title;
    @JsonSetter("HdfsFilePath")
    private String HdfsFilePath;
    @JsonSetter("Location")
    private String Location;
    @JsonSetter("DetailLocation")
    private String DetailLocation;
    @JsonSetter("EventDate")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private Date EventDate;
    @JsonSetter("Description")
    private String Description;
    @JsonSetter("DescriptionImage")
    private String DescriptionImage;
    @JsonSetter("Chart")
    private String Chart;


    public NormalVO(
            String title, String hdfsFilePath, String location, String detailLocation,
            String description, String descriptionImage, String chart, Date eventDate){
        this.Title = title;
        this.HdfsFilePath = hdfsFilePath;
        this.Location = location;
        this.DetailLocation = detailLocation;
        this.Description = description;
        this.DescriptionImage = descriptionImage;
        this.Chart = chart;
        this.EventDate = eventDate;
    }

    public DataDTO buildData(NormalVO normalVO){
        DataDTO dataDTO = new DataDTO();

        dataDTO.setHdfsFilePath(normalVO.getHdfsFilePath());
        dataDTO.setTitle(normalVO.getTitle());
        dataDTO.setEventDate(normalVO.getEventDate());
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
