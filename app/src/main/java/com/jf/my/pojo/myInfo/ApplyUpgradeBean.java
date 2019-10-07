package com.jf.my.pojo.myInfo;

/**
 * Created by fengrs on 2018/9/8.
 * 备注:
 */

public class ApplyUpgradeBean {
    private String message = "";
    private  String type  ;//type: 判断1 ,判断2 判断3  //(1:满足升级条件，2：不满足升级条件，3：需要联系客服咨询)

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
