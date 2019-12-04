package com.zjzy.morebit.pojo.goods;

import com.zjzy.morebit.pojo.ShopGoodInfo;

import java.util.List;

/**
 * Created by fengrs on 2018/6/29.
 */

public class RecommendBean {
    private String extra="";
    private List<ShopGoodInfo> list;

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public List<ShopGoodInfo> getList() {
        return list;
    }

    public void setList(List<ShopGoodInfo> list) {
        this.list = list;
    }


}
