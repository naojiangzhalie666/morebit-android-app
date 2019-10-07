package com.jf.my.pojo.requestbodybean;

import com.jf.my.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/3/18
 * Description:
 */
public class RequestModelIdData extends RequestBaseBean {

    /**
     * id : 82
     * twoLevelName : mock
     * sort : 98
     */

    private int id;
    private String twoLevelName;
    private int sort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTwoLevelName() {
        return twoLevelName;
    }

    public void setTwoLevelName(String twoLevelName) {
        this.twoLevelName = twoLevelName;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
