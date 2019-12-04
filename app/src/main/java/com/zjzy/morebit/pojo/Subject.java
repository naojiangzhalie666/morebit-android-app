package com.zjzy.morebit.pojo;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2019/6/16.
 */

public class Subject implements Serializable {

    private int twoLevelId;  // 二级专题id
    private int id ;//1级专题id
    private String name; //一级专题名称;
    private String twoLevelName ; // 二级专题名称

    public int getTwoLevelId() {
        return twoLevelId;
    }

    public void setTwoLevelId(int twoLevelId) {
        this.twoLevelId = twoLevelId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTwoLevelName() {
        return twoLevelName;
    }

    public void setTwoLevelName(String twoLevelName) {
        this.twoLevelName = twoLevelName;
    }
}
