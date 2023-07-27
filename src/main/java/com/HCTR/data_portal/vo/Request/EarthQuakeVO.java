package com.HCTR.data_portal.vo.Request;

import com.HCTR.data_portal.dto.DataDTO;
import com.HCTR.data_portal.dto.EarthQuakeDTO;
import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonPropertyOrder({
        "Title", "HdfsFilePath", "Location", "DetailLocation", "EventDate", "Latitude",
        "Longtitude", "Scale", "MapImage", "TimeSeries", "SensorInfo", "AdditionalData"})
public class EarthQuakeVO {
    @JsonProperty("Title")
    private String Title;
    @JsonProperty("HdfsFilePath")
    private String HdfsFilePath;
    @JsonProperty("Location")
    private String Location;
    @JsonProperty("DetailLocation")
    private String DetailLocation;
    @JsonProperty("EventDate")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private Date EventDate;
    @JsonProperty("Latitude")
    private float Latitude;
    @JsonProperty("Longtitude")
    private float Longtitude;
    @JsonProperty("Scale")
    private float Scale;
    @JsonProperty("MapImage")
    private String MapImage;
    @JsonProperty("TimeSeries")
    private String TimeSeries;
    @JsonProperty("AdditionalData")
    private String AdditionalData;
    @JsonProperty("SensorInfo")
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

    public DataDTO buildData() {
        DataDTO dataDTO = new DataDTO();

        dataDTO.setHdfsFilePath(HdfsFilePath);
        dataDTO.setTitle(Title);
        dataDTO.setEventDate(EventDate);
        dataDTO.setDataType(0);
        dataDTO.setViews(0);
        dataDTO.setLocation(Location);
        dataDTO.setDetailLocation(DetailLocation);
        dataDTO.setManagerId("manager");

        return dataDTO;
    }

    public EarthQuakeDTO buildEarthQuake() {
        EarthQuakeDTO earthQuakeDTO = new EarthQuakeDTO();

        earthQuakeDTO.setLatitude(Latitude);
        earthQuakeDTO.setLongtitude(Longtitude);
        earthQuakeDTO.setScale(Scale);
        earthQuakeDTO.setMapImage(MapImage);
        earthQuakeDTO.setTimeSeries(TimeSeries);
        earthQuakeDTO.setAdditionalData(AdditionalData);
        earthQuakeDTO.setSensorInfo(SensorInfo);

        return earthQuakeDTO;
    }

    public int getYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(EventDate);

        return calendar.get(Calendar.YEAR);
    }

    public void setHdfsPath(String rootPath, String mapImagePath, String timeSeriesPath, String sensorInfoPath) {
        HdfsFilePath = rootPath;
        MapImage = rootPath + '/' + mapImagePath;
        TimeSeries = rootPath + '/' + timeSeriesPath;
        SensorInfo = rootPath + '/' + sensorInfoPath;
    }

}
