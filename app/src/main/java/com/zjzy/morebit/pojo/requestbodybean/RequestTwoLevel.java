package com.zjzy.morebit.pojo.requestbodybean;

import com.zjzy.morebit.pojo.request.RequestListBody;

/**
 * Created by JerryHo on 2019/3/18
 * Description:
 */
public class RequestTwoLevel extends RequestListBody {

    private int twoLevel;
    private  String modelId;

    public String getModelId() {
        return modelId;
    }

    public int getTwoLevel() {
        return twoLevel;
    }

    public RequestTwoLevel setTwoLevel(int twoLevel) {
        this.twoLevel = twoLevel;
        return this;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
}
