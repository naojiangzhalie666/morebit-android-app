package com.jf.my.pojo.requestbodybean;

import com.jf.my.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/7
 * Description: 检测是否存在提现记录
 */
public class RequestCheckWithdrawBean extends RequestBaseBean {

    private int type;

    public RequestCheckWithdrawBean(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
