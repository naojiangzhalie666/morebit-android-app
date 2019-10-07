package com.jf.my.pojo;

/**
 * Created by Administrator on 2017/10/20.
 */

public class MsgInfo {


        /**
         * userhead : /userhead.png
         * record : 记录备注
         * record_time : 2018-01-16 13:18:57
         */
        private String title="";
        private String digest="";
        private String pictrue="";
        private String createTime ="";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPictrue() {
        return pictrue;
    }

    public void setPictrue(String pictrue) {
        this.pictrue = pictrue;
    }
}
