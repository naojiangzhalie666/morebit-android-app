package com.jf.my.pojo.request;

/**
 * Created by YangBoTian on 2019/5/16.
 */

public class RequestFansInfoBean  extends RequestBaseBean {
    private String id;
   private String phoneOrActivationCode;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneOrActivationCode() {
        return phoneOrActivationCode;
    }

    public void setPhoneOrActivationCode(String phoneOrActivationCode) {
        this.phoneOrActivationCode = phoneOrActivationCode;
    }
}
