package com.zjzy.morebit.pojo.request;

import com.zjzy.morebit.address.AddressInfo;

/**
 * Created by haiping.liu on 2019-12-12.
 */
public class RequestAddAddressBean extends RequestBaseBean {
    private AddressInfo addressInfo;

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }
}
