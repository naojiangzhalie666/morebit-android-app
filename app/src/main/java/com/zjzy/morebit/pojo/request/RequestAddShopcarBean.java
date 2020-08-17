package com.zjzy.morebit.pojo.request;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2018/12/24.
 */

public class RequestAddShopcarBean implements Serializable {
    private String  goodsId;//商品id
    private String productId;//商品货品id
    private String number;//商品数量
    private String cartId;//购物车id

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
