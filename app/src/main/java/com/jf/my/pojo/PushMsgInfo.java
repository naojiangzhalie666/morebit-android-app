package com.jf.my.pojo;

/**
 * Created by Administrator on 2018/2/6.
 */

public class PushMsgInfo {

    /**
     * phone :
     * content : 呵呵呵呵呵
     * push_type : 4
     * title : 测试
     */

    private String phone="";
    private String content="";
    private String push_type="";
    private String title="";
    private String msgId="";
    private String createTime="";
    private String goods_id="";
    private String schedule_id;    //h5跳转地址
    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPush_type() {
        return push_type;
    }

    public void setPush_type(String push_type) {
        this.push_type = push_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }
}
