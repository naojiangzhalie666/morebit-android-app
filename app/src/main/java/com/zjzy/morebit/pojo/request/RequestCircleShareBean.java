package com.zjzy.morebit.pojo.request;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2018/12/24.
 */

public class RequestCircleShareBean implements Serializable {
    private int  type;
    private String itemId;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
