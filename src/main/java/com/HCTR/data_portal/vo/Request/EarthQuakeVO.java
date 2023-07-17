package com.HCTR.data_portal.vo.Request;

import com.HCTR.data_portal.dto.DataDTO;
import com.HCTR.data_portal.dto.EarthQuakeDTO;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@ToString
public class EarthQuakeVO {
    private final String Title;
    private final String HdfsFilePath;
    private final String Location;
    private final String DetailLocation;
    private final Date EventDate;
    private final float Latitude;
    private final float Longtitude;
    private final float Scale;
    private final String MapImage;
    private final String TimeSeries;
    private final String AdditionalData;
    private final String SensorInfo;

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
