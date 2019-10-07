package com.jf.my.pojo.requestbodybean;

import com.jf.my.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/7
 * Description: 请求分享物品链接,二维码bean
 */
public class RequestShareGoods extends RequestBaseBean {

    private String type;
    private String itemSourceId;
    private String tkl;
    private String  itemsJson;

    public String getTkl() {
        return tkl;
    }

    public void setTkl(String tkl) {
        this.tkl = tkl;
    }

    public String getItemsJson() {
        return itemsJson;
    }

    public void setItemsJson(String itemsJson) {
        this.itemsJson = itemsJson;
    }

    public String getType() {
        return type;
    }

    public RequestShareGoods setType(String type) {
        this.type = type;
        return this;
    }

    public String getItemSourceId() {
        return itemSourceId;
    }

    public RequestShareGoods setItemSourceId(String itemSourceId) {
        this.itemSourceId = itemSourceId;
        return this;
    }

}
