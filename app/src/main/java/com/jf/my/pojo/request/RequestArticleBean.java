package com.jf.my.pojo.request;

import java.io.Serializable;

public class RequestArticleBean extends RequestBaseBean {
    private int modelId;
    private int type;

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
