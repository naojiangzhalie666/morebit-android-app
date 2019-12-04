package com.zjzy.morebit.pojo.requestbodybean;

import com.zjzy.morebit.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/8
 * Description: 解析淘口令
 */
public class RequestAnalysisTKL  extends RequestBaseBean {
    private String tkl;

    public String getTkl() {
        return tkl;
    }

    public RequestAnalysisTKL setTkl(String tkl) {
        this.tkl = tkl;
        return this;
    }
}
