package com.markermall.cat.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fengrs on 2018/6/4.
 */

public class ShopGoodBean  implements Serializable {


    private String minId;
    private String page;
    private int searchType;//1库内，0全网
    private int isPageEnd;// 1结束，0没结束
    private List<ShopGoodInfo> data;

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public String getMinId() {
        return minId;
    }

    public void setMinId(String minId) {
        this.minId = minId;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<ShopGoodInfo> getData() {
        return data;
    }

    public void setData(List<ShopGoodInfo> data) {
        this.data = data;
    }

    public int getIsPageEnd() {
        return isPageEnd;
    }

    public void setIsPageEnd(int isPageEnd) {
        this.isPageEnd = isPageEnd;
    }
}
