package com.HCTR.data_portal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
public class DataDTO {
    @JsonProperty("Id")
    private int Id;
    @JsonProperty("Title")
    private String Title;
    @JsonProperty("HdfsFilePath")
    private String HdfsFilePath;
    @JsonProperty("EventDate")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private Date EventDate;
    @JsonProperty("DataType")
    private int DataType;
    @JsonProperty("Views")
    private int Views;
    @JsonProperty("Location")
    private String Location;
    @JsonProperty("DetailLocation")
    private String DetailLocation;
    @JsonProperty("ManagerId")
    private String ManagerId;

    public DataDTO(
            int id, String title, String hdfsFilePath, Date eventDate, int dataType,
            int views, String location, String detailLocation, String managerId) {
        this.Id = id;
        this.Title = title;
        this.HdfsFilePath = hdfsFilePath;
        this.EventDate = eventDate;
        this.DataType = dataType;
        this.Views = views;
        this.Location = location;
        this.DetailLocation = detailLocation;
        this.ManagerId = managerId;
    }

    public DataDTO() {

    }
}
