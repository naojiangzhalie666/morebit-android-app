package com.zjzy.morebit.pojo;

import java.io.Serializable;
import java.util.List;

public class PushMessage implements Serializable {


    /**
     * content : 恭喜您已经攒够成长值，快去升级VIP畅享海量优惠吧~
     * contentJson : {"msg":"恭喜您已经攒够成长值，快去升级VIP畅享海量优惠吧~","level":"0","growthFlag":"1","growth":"360"}
     * push_type : 19
     */

    private String content;
    private String contentJson;
    private String push_type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentJson() {
        return contentJson;
    }

    public void setContentJson(String contentJson) {
        this.contentJson = contentJson;
    }

    public String getPush_type() {
        return push_type;
    }

    public void setPush_type(String push_type) {
        this.push_type = push_type;
    }
}
