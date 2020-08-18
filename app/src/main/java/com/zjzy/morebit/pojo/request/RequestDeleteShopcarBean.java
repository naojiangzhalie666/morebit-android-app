package com.zjzy.morebit.pojo.request;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YangBoTian on 2018/12/24.
 */

public class RequestDeleteShopcarBean implements Serializable {
    List<String> productIds;//商品货品表id

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }
}
