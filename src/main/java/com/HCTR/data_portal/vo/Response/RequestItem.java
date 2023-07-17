package com.HCTR.data_portal.vo.Response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class RequestItem {
    private String ReqStatus;
    private int DataId;
    private int DataType;
    private String Title;
    private String UserId;
    private String ReqDate;

    public RequestItem(
            String reqStatus, int dataId, int dataType,
            String title, String userId, String reqDate) {
        this.ReqStatus = reqStatus;
        this.DataId = dataId;
        this.DataType = dataType;
        this.Title = title;
        this.UserId = userId;
        this.ReqDate = reqDate;
    }

}
