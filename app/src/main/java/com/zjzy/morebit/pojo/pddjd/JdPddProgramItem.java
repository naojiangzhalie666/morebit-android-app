package com.zjzy.morebit.pojo.pddjd;

/**
 * @author CSR
 */

public class JdPddProgramItem extends ProgramItem {

    /**
     * 是否 拼购   苏宁专用  优先跳 拼购小程序  1是 0 否
     */
    private Integer isPin;

    /**
     * 店家 编码
     */
    private String supplierCode;
    /**
     * 优惠券活动ID
     */
    private String couponActivityId;

    /**
     * 拼购ID
     */
    private String pgActionId;

    public Integer getIsPin() {
        return isPin;
    }

    public void setIsPin(Integer isPin) {
        this.isPin = isPin;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getCouponActivityId() {
        return couponActivityId;
    }

    public void setCouponActivityId(String couponActivityId) {
        this.couponActivityId = couponActivityId;
    }

    public String getPgActionId() {
        return pgActionId;
    }

    public void setPgActionId(String pgActionId) {
        this.pgActionId = pgActionId;
    }
}
