package com.jf.my.pojo.home;

import com.jf.my.pojo.ImageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fengrs on 2018/8/29.
 */

public class HomeRedPagckageBean implements Serializable {
    private List<ImageInfo> list;
    private String record;

    public List<ImageInfo> getList() {
        return list;
    }

    public void setList(List<ImageInfo> list) {
        this.list = list;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }
}
