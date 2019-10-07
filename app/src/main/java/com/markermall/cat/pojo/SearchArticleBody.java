package com.markermall.cat.pojo;

import com.markermall.cat.pojo.request.RequestListBody;

/**
 * Created by YangBoTian on 2019/1/7.
 */

public class SearchArticleBody extends RequestListBody {
  private  String search;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
