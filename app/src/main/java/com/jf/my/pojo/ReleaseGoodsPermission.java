package com.jf.my.pojo;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2019/6/18.
 */

public class ReleaseGoodsPermission implements Serializable {
    private int status;   //   用户是否有权限添加：0: 否 1：是
    private String itemStatus;   //用户请求的商品是否有可添加：0: 否 1：是
   private String hint;
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getItemStatus() {
        return itemStatus;
    } 

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
