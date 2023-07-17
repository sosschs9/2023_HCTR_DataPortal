package com.HCTR.data_portal.vo.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataList<T> {
    private int TotalData;
    private T Items;

    public DataList(int totalData, T items) {
        this.TotalData = totalData;
        this.Items = items;
    }
}