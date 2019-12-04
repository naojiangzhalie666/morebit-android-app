package com.zjzy.morebit.pojo.request;

public class RequestAppFeedBackBean extends RequestBaseBean {

    private String picture;
    private String message;
    private String goodsId;
    private String type;
    private String os;
    private String sign;
    private String accountName;
    private String phone;
    private String title;

    public String getPictrue() {
        return picture;
    }

    public void setPictrue(String pictrue) {
        this.picture = pictrue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
