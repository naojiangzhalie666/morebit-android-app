package com.jf.my.pojo.myInfo;

/**
 * Created by fengrs on 2018/12/27.
 * 备注: oss 回调解析
 */

public class OssCallbackBean {

    /**
     * callbackUrl : http://127.0.0.1:8911/oss/callBack
     * callbackBody : {"bucket":${bucket},"x:realname":${x:realname},"x:uploadtype":${x:uploadtype},"x:uploadid":${x:uploadid},"object":${object},"mimeType":${mimeType},"size":${size}}
     * callbackBodyType : application/json
     */

    private String callbackUrl;
    private String callbackBody;
    private String callbackBodyType;

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getCallbackBody() {
        return callbackBody;
    }

    public void setCallbackBody(String callbackBody) {
        this.callbackBody = callbackBody;
    }

    public String getCallbackBodyType() {
        return callbackBodyType;
    }

    public void setCallbackBodyType(String callbackBodyType) {
        this.callbackBodyType = callbackBodyType;
    }
}
