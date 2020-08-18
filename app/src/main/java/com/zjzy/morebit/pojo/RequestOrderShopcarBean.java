package com.zjzy.morebit.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YangBoTian on 2018/12/24.
 */

public class RequestOrderShopcarBean implements Serializable {
   private List<String> cartIdList;
   private String addressId;

    public List<String> getCartIdList() {
        return cartIdList;
    }

    public void setCartIdList(List<String> cartIdList) {
        this.cartIdList = cartIdList;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
}
