package com.jf.my.pojo;

import java.io.Serializable;

/**
 * Created by fengrs on 2019/7/15.
 * 商品itme
 */

public class RankingTitleBean implements Serializable {
    int cid;
    String cname;
    int page;
    int type;//1、实时排行/2、今日排行
    String order;
    String sort;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
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

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}

