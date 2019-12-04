package com.zjzy.morebit.pojo.requestbodybean;

import com.zjzy.morebit.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/7
 * Description: 提现请求bean
 */
public class RequestWithdrawBean  extends RequestBaseBean {

    private String amount;
    private String sign;

    public String getAmount() {
        return amount;
    }

    public RequestWithdrawBean setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public RequestWithdrawBean setSign(String sign) {
        this.sign = sign;
        return this;
    }
}
