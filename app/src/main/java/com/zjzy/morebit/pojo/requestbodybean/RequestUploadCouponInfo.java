package com.zjzy.morebit.pojo.requestbodybean;

import com.zjzy.morebit.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/8
 * Description: 上传优惠券json bean
 */
public class RequestUploadCouponInfo extends RequestBaseBean {

    private String itemSourceId;
    private String couponJson;
    private int count;
    private int beParsed;

    public String getItemSourceId() {
        return itemSourceId;
    }

    public RequestUploadCouponInfo setItemSourceId(String itemSourceId) {
        this.itemSourceId = itemSourceId;
        return this;
    }

    public String getCouponJson() {
        return couponJson;
    }

    public RequestUploadCouponInfo setCouponJson(String couponJson) {
        this.couponJson = couponJson;
        return this;
    }

    public int getCount() {
        return count;
    }

    public RequestUploadCouponInfo setCount(int count) {
        this.count = count;
        return this;
    }

    public int getBeParsed() {
        return beParsed;
    }

    public RequestUploadCouponInfo setBeParsed(int beParsed) {
        this.beParsed = beParsed;
        return this;
    }
}
