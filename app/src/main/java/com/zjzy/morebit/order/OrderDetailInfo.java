package com.zjzy.morebit.order;

import java.io.Serializable;

/**
 * 订单详情
 * Created by haiping.liu on 2019-12-13.
 */
public class OrderDetailInfo implements Serializable {
    /**
     * 订单信息
     */
    private OrderInfo orderInfo;
    /**
     * 订单货物
     */
    private OrderGoods orderGoods;

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public OrderGoods getOrderGoods() {
        return orderGoods;
    }

    public void setOrderGoods(OrderGoods orderGoods) {
        this.orderGoods = orderGoods;
    }
}
