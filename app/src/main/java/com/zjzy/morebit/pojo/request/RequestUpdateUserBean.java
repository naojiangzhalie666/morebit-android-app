package com.zjzy.morebit.pojo.request;

/**
 * 请求升级
 * Created by haiping.liu on 2019-12-15.
 */
public class RequestUpdateUserBean extends RequestBaseBean{

    private  Integer userType;

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}
