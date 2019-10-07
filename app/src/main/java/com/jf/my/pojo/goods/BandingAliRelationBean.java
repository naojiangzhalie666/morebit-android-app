package com.jf.my.pojo.goods;

/**
 * Created by fengrs on 2019/2/21.
 * 备注: 上传渠道id
 */

public class BandingAliRelationBean {
    private int resultType;  // 请求状态   0 默认 ,1 重试  ,2 关闭   , 3 成功 ,
    private String errMsg;  // 错误信息

    public int getResultType() {
        return resultType;
    }

    public void setResultType(int resultType) {
        this.resultType = resultType;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
