package com.markermall.cat.pojo.requestbodybean;

import com.markermall.cat.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/3/18
 * Description: 商学院模块id
 */
public class RequestModelId  extends RequestBaseBean {

    private int modelId;

    public int getModelId() {
        return modelId;
    }

    public RequestModelId setModelId(int modelId) {
        this.modelId = modelId;
        return this;
    }
}
