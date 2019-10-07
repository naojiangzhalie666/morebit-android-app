package com.markermall.cat.pojo.requestbodybean;

import com.markermall.cat.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/8
 * Description:商家同店商品
 */
public class RequestShopId  extends RequestBaseBean {

    private String shopId;
    private int page =1;

    public int getPage() {
        return page;
    }

    public RequestShopId setPage(int page) {
        this.page = page;
        return this;
    }

    public String getShopId() {
        return shopId;
    }

    public RequestShopId setShopId(String shopId) {
        this.shopId = shopId;
        return this;
    }
}
