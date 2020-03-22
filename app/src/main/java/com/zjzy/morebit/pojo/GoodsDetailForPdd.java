package com.zjzy.morebit.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 拼多多商品详情
 * Created by haiping.liu on 2020-03-21.
 */

public class GoodsDetailForPdd implements Serializable {
    private List<String> goodsDetails;

    public List<String> getGoodsDetails() {
        return goodsDetails;
    }

    public void setGoodsDetails(List<String> goodsDetails) {
        this.goodsDetails = goodsDetails;
    }
}
