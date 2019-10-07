package com.markermall.cat.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YangBoTian on 2018/6/20 20:55
 */

public class OfficialRecom implements Serializable {
    private int lm;
    List<ShopGoodInfo> data;

    public int getLm() {
        return lm;
    }

    public void setLm(int lm) {
        this.lm = lm;
    }

    public List<ShopGoodInfo> getData() {
        return data;
    }

    public void setData(List<ShopGoodInfo> data) {
        this.data = data;
    }
}
