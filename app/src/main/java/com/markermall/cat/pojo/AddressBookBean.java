package com.markermall.cat.pojo;

import java.io.Serializable;

/**
 * Created by fengrs on 2018/6/23.
 */

public class AddressBookBean implements Serializable {
    private String name;
    private String phome;
    public boolean isClick;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhome() {
        return phome;
    }

    public void setPhome(String phome) {
        this.phome = phome;
    }

    public boolean getIsClick() {
        return isClick;
    }

    public void setIsClick(boolean isClick) {
        this.isClick = isClick;
    }
}
