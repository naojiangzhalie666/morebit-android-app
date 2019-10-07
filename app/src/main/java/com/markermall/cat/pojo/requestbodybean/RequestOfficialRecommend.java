package com.markermall.cat.pojo.requestbodybean;

import com.markermall.cat.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/7
 * Description: 官方推荐请求bean
 */
public class RequestOfficialRecommend  extends RequestBaseBean {

    private int type;
    private int page;
    private String keyword;

    public int getType() {
        return type;
    }

    public RequestOfficialRecommend setType(int type) {
        this.type = type;
        return this;
    }

    public int getPageNum() {
        return page;
    }

    public RequestOfficialRecommend setPageNum(int pageNum) {
        this.page = pageNum;
        return this;
    }

    public String getKeyword() {
        return keyword;
    }

    public RequestOfficialRecommend setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }
}
