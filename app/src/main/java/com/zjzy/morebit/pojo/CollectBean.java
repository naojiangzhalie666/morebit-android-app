package com.zjzy.morebit.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fengrs on 2018/8/29.
 */

public class CollectBean implements Serializable {
    List<ShopGoodInfo> data;

    int page;
    int type;

    public List<ShopGoodInfo> getData() {
        return data;
    }

    public void setData(List<ShopGoodInfo> data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
