package com.markermall.cat.pojo.request;

import com.markermall.cat.pojo.Records;
import com.markermall.cat.utils.C;

import java.util.List;

public class RequestPopupBean extends RequestBaseBean {

     int type;
     List<Records> records;
     int os = C.Setting.os;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Records> getRecords() {
        return records;
    }

    public void setRecords(List<Records> records) {
        this.records = records;
    }

}
