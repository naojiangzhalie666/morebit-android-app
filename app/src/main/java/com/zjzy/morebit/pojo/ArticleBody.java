package com.zjzy.morebit.pojo;

import com.zjzy.morebit.pojo.request.RequestListBody;

/**
 * Created by YangBoTian on 2019/1/7.
 */

public class ArticleBody extends RequestListBody{
  private  String modelId;

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
}
