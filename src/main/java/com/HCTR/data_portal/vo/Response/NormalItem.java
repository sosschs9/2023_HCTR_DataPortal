package com.HCTR.data_portal.vo.Response;

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
public class NormalItem {
    // 제목, 위치, 상세 위치, 발생일자
    // 설명, 설명이미지, 차트

    @JsonProperty("Title")
    private String Title;
    @JsonProperty("Location")
    private String Location;
    @JsonProperty("DetailLocation")
    private String DetailLocation;
    @JsonProperty("EventDate")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    private Date EventDate;
    @JsonProperty("Description")
    private String Description;
    @JsonProperty("DescriptionImage")
    private String DescriptionImage;
    @JsonProperty("Chart")
    private String Chart;

    public NormalItem(String title, String location, String detailLocation,
                      Date eventDate, String description, String descriptionImage, String chart){
        this.Title = title;
        this.Location = location;
        this.DetailLocation = detailLocation;
        this.EventDate = eventDate;
        this.Description = description;
        this.DescriptionImage = descriptionImage;
        this.Chart = chart;
    }

    public void setUrl(String rootUrl) {
        DescriptionImage = rootUrl + DescriptionImage;
        Chart = rootUrl + Chart;
    }
}
