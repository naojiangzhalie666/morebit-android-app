package com.zjzy.morebit.pojo.goods;

/**
 * Created by fengrs on 2018/12/24.
 * 备注:
 */

public class CouponUrlBean {


    /**
     * checkCouponStatus : {"cookie":"cookie1","url":"www.coupon.com"}
     * couponUrl : www.1111.com
     */

    private CheckCouponStatusBean checkCouponStatus;
    private String couponUrl;

    public CheckCouponStatusBean getCheckCouponStatus() {
        return checkCouponStatus;
    }

    public void setCheckCouponStatus(CheckCouponStatusBean checkCouponStatus) {
        this.checkCouponStatus = checkCouponStatus;
    }

    public String getCouponUrl() {
        return couponUrl;
    }

    public void setCouponUrl(String couponUrl) {
        this.couponUrl = couponUrl;
    }


}
