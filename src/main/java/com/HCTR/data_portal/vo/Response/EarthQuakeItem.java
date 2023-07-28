package com.HCTR.data_portal.vo.Response;

import com.HCTR.data_portal.dto.DataDTO;
import com.HCTR.data_portal.dto.EarthQuakeDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EarthQuakeItem {
    // 제목, 위치, 상세 위치, 발생일자, 위도, 경도, 규모
    // 맵, 시계열 이미지 파일 가져오기
    @JsonProperty("Title")
    private String Title;
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

    public EarthQuakeItem(
            String title, String location, String detailLocation,
            Date eventDate, float latitude, float longtitude,
            float scale, String mapImage, String timeSeries) {
        this.Title = title;
        this.Location = location;
        this.DetailLocation = detailLocation;
        this.EventDate = eventDate;
        this.Latitude = latitude;
        this.Longtitude = longtitude;
        this.Scale = scale;
        this.MapImage = mapImage;
        this.TimeSeries = timeSeries;
    }

    public void setUrl(String rootUrl) {
        MapImage = rootUrl + MapImage;
        TimeSeries = rootUrl + TimeSeries;
    }
}
