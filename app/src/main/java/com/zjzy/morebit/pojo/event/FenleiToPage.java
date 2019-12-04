package com.zjzy.morebit.pojo.event;

import com.zjzy.morebit.pojo.goods.GoodCategoryInfo;

import java.util.List;

/**
 * Created by fengrs on 2018/5/17.
 * 发送超级列表数据homepage页
 */

public class FenleiToPage {
    private List<GoodCategoryInfo> getSuperInfo;

    public FenleiToPage(List<GoodCategoryInfo> getSuperInfo) {
        this.getSuperInfo = getSuperInfo;
    }

    public List<GoodCategoryInfo> getGetSuperInfo() {
        return getSuperInfo;
    }

    public void setGetSuperInfo(List<GoodCategoryInfo> getSuperInfo) {
        this.getSuperInfo = getSuperInfo;
    }
}

