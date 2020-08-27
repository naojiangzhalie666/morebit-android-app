package com.zjzy.morebit.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YangBoTian on 2018/12/24.
 */

public class RequestWithdrawBean2 implements Serializable {

   private String amount;//提现金额
    private String type;//1-自购返利 2-优选商品积分 3-活动奖励

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
