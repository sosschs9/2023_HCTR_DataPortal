package com.HCTR.data_portal.vo.Request;

import com.HCTR.data_portal.dto.DataDTO;
import com.HCTR.data_portal.dto.NormalDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonPropertyOrder({
        "Title", "HdfsFilePath", "Location", "DetailLocation",
        "EventDate", "Description", "DescriptionImage", "Chart" })
public class NormalVO {
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
    @JsonProperty("Description")
    private String Description;
    @JsonProperty("DescriptionImage")
    private String DescriptionImage;
    @JsonProperty("Chart")
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

    public DataDTO buildData(){
        DataDTO dataDTO = new DataDTO();

        dataDTO.setHdfsFilePath(HdfsFilePath);
        dataDTO.setTitle(Title);
        dataDTO.setEventDate(EventDate);
        dataDTO.setDataType(1);
        dataDTO.setViews(0);
        dataDTO.setLocation(Location);
        dataDTO.setDetailLocation(DetailLocation);
        dataDTO.setManagerId("manager");

        return dataDTO;
    }

    public NormalDTO buildNormal(){
        NormalDTO normalDTO = new NormalDTO();

        normalDTO.setDescription(Description);
        normalDTO.setDescriptionImage(DescriptionImage);
        normalDTO.setChart(Chart);

        return normalDTO;
    }

    public int getYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(EventDate);

        return calendar.get(Calendar.YEAR);
    }

    public void setHdfsPath(String rootPath, String descriptionImagePath, String chartPath) {
        HdfsFilePath = rootPath;
        DescriptionImage = rootPath + '/' + descriptionImagePath;
        Chart = rootPath + '/' + chartPath;
    }
}
