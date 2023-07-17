package com.HCTR.data_portal.dto;

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
    private int Id;
    private String Title;
    private String HdfsFilePath;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date EventDate;
    private int DataType;
    private int Views;
    private String Location;
    private String DetailLocation;
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
