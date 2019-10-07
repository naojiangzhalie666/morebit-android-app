package com.markermall.cat.pojo.request;

/**
 * Created by fengrs on 2019/6/14.
 */

public class RequestMaterialLink extends RequestBaseBean {
    private String itemId;
    private String invedeCode;
    private String material;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getInvedeCode() {
        return invedeCode;
    }

    public void setInvedeCode(String invedeCode) {
        this.invedeCode = invedeCode;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
