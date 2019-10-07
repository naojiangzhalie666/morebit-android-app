package com.markermall.cat.pojo.requestbodybean;

import com.markermall.cat.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/8
 * Description: 确认升级
 */
public class RequestConfirmUpgradeData extends RequestBaseBean {

    private String isApply;
    private String sign;

    public String getIsApply() {
        return isApply;
    }

    public RequestConfirmUpgradeData setIsApply(String isApply) {
        this.isApply = isApply;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public RequestConfirmUpgradeData setSign(String sign) {
        this.sign = sign;
        return this;
    }
}
