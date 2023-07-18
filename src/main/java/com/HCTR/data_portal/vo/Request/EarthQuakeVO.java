package com.HCTR.data_portal.vo.Request;

import com.HCTR.data_portal.dto.DataDTO;
import com.HCTR.data_portal.dto.EarthQuakeDTO;
import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonPropertyOrder({
        "Title", "HdfsFilePath", "Location", "DetailLocation", "EventDate", "Latitude",
        "Longtitude", "Scale", "MapImage", "TimeSeries", "SensorInfo", "AdditionalData"})
public class EarthQuakeVO {
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
    @JsonSetter("Latitude")
    private float Latitude;
    @JsonSetter("Longtitude")
    private float Longtitude;
    @JsonSetter("Scale")
    private float Scale;
    @JsonSetter("MapImage")
    private String MapImage;
    @JsonSetter("TimeSeries")
    private String TimeSeries;
    @JsonSetter("AdditionalData")
    private String AdditionalData;
    @JsonSetter("SensorInfo")
    private String SensorInfo;

    public EarthQuakeVO(
            String title, String hdfsFilePath, String location, String detailLocation,
            Date eventDate, float latitude, float longtitude, float scale,
            String mapImage, String timeSeries, String additionalData, String sensorInfo){
        this.Title = title;
        this.HdfsFilePath = hdfsFilePath;
        this.Location = location;
        this.DetailLocation = detailLocation;
        this.EventDate = eventDate;
        this.Latitude = latitude;
        this.Longtitude = longtitude;
        this.Scale = scale;
        this.MapImage = mapImage;
        this.TimeSeries = timeSeries;
        this.AdditionalData = additionalData;
        this.SensorInfo = sensorInfo;
    }

    public DataDTO buildData(EarthQuakeVO earthQuakeVO) {
        DataDTO dataDTO = new DataDTO();

        dataDTO.setHdfsFilePath(earthQuakeVO.getHdfsFilePath());
        dataDTO.setTitle(earthQuakeVO.getTitle());
        dataDTO.setEventDate(earthQuakeVO.getEventDate());
        dataDTO.setDataType(0);
        dataDTO.setViews(0);
        dataDTO.setLocation(earthQuakeVO.getLocation());
        dataDTO.setDetailLocation(earthQuakeVO.getDetailLocation());
        dataDTO.setManagerId("manager");

        return dataDTO;
    }

    public EarthQuakeDTO buildEarthQuake(EarthQuakeVO earthQuakeVO) {
        EarthQuakeDTO earthQuakeDTO = new EarthQuakeDTO();

        earthQuakeDTO.setLatitude(earthQuakeVO.getLatitude());
        earthQuakeDTO.setLongtitude(earthQuakeVO.getLongtitude());
        earthQuakeDTO.setScale(earthQuakeVO.getScale());
        earthQuakeDTO.setMapImage(earthQuakeVO.MapImage);
        earthQuakeDTO.setTimeSeries(earthQuakeVO.getTimeSeries());
        earthQuakeDTO.setAdditionalData(earthQuakeVO.getAdditionalData());
        earthQuakeDTO.setSensorInfo(earthQuakeVO.getSensorInfo());

        return earthQuakeDTO;
    }

}
