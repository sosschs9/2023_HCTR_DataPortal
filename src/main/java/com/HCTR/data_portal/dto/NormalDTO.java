package com.HCTR.data_portal.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NormalDTO{
    private int Id;
    private String Description;
    private String DescriptionImage;
    private String Chart;
    private int DataId;

    public NormalDTO(
            int id, String description, String descriptionImage,
            String chart, int dataId){
        this.Id = id;
        this.Description = description;
        this.DescriptionImage = descriptionImage;
        this.Chart = chart;
        this.DataId = dataId;
    }
}
