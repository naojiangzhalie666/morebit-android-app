package com.zjzy.morebit.pojo.requestbodybean;

import com.zjzy.morebit.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/7
 * Description: 检测是否存在提现记录
 */
public class RequestCheckWithdrawBean extends RequestBaseBean {

    private int type=1;

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
