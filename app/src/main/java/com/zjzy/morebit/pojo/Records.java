package com.zjzy.morebit.pojo;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2019/1/26.
 */

public class Records implements Serializable {
    private int id;
    private int time;
    private int num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
