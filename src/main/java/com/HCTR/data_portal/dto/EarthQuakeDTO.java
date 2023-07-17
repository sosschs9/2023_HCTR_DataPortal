package com.HCTR.data_portal.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class EarthQuakeDTO{
    private int Id;
    private float Latitude;
    private float Longtitude;
    private float Scale;
    private String MapImage;
    private String TimeSeries;
    private String AdditionalData;
    private String SensorInfo;
    private int DataId;

    public EarthQuakeDTO(
            float latitude, float longtitude,
            float scale, String mapImage, String timeSeries,
            String additionalData, String sensorInfo, int dataId){
        this.Latitude = latitude;
        this.Longtitude = longtitude;
        this.Scale = scale;
        this.MapImage = mapImage;
        this.TimeSeries = timeSeries;
        this.AdditionalData = additionalData;
        this.SensorInfo = sensorInfo;
        this.DataId = dataId;
    }

    public EarthQuakeDTO() {}
}
