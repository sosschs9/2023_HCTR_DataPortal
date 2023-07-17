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
public class RequestDTO {
    private int Id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ReqDate;
    private String ReqStatus;
    private boolean IsDownload;
    private String UserId;
    private int DataId;
    private String ManagerId;

    public RequestDTO(
            int id, Date reqDate, String reqStatus, boolean isDownload,
            String userId, int dataId, String managerId){
        this.Id = id;
        this.ReqDate = reqDate;
        this.ReqStatus = reqStatus;
        this.IsDownload = isDownload;
        this.UserId = userId;
        this.DataId = dataId;
        this.ManagerId = managerId;
    }
}
