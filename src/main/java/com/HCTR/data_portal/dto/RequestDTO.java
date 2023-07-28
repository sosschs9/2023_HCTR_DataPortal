package com.HCTR.data_portal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RequestDTO {
    private int Id;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
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
