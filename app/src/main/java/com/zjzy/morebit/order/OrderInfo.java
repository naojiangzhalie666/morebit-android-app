package com.zjzy.morebit.order;

import java.io.Serializable;

/**
 * 订单信息
 * Created by haiping.liu on 2019-12-13.
 */
public class OrderInfo implements Serializable {
    private String id;
    private String orderSn;
    private String addTime;
    private String consignee;
    private String mobile;
    private String address;
    private String goodsPrice;
    private String actualPrice;
    private String orderStatusText;
    private String handleOption;
    private String expCode;
    private String expNo;


}
