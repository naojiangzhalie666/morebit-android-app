package com.jf.my.pojo.request;

public class RequestFeedBackBean extends RequestBaseBean{

    private String action;
    private String base64;
    private String os;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
}
