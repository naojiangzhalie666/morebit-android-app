package com.jf.my.pojo.request;

public class RequestMaterial extends RequestBaseBean {
    int page;
    String material;
    String invedeCode;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getInvedeCode() {
        return invedeCode;
    }

    public void setInvedeCode(String invedeCode) {
        this.invedeCode = invedeCode;
    }
}
