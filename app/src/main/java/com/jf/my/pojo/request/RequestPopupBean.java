package com.jf.my.pojo.request;

import com.jf.my.pojo.Records;
import com.jf.my.utils.C;

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
