package com.zjzy.morebit.pojo.requestbodybean;

import com.zjzy.morebit.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/7
 * Description:获取专属客服请求bean
 */
public class RequestInviteCodeBean  extends RequestBaseBean {
    private int wxShowType;

    public int getWxShowType() {
        return wxShowType;
    }

    public RequestInviteCodeBean setWxShowType(int wxShowType) {
        this.wxShowType = wxShowType;
        return this;
    }

    }
