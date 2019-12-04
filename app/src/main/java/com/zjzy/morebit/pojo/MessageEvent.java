package com.zjzy.morebit.pojo;

/**
 * Created by fengrs on 2019/3/22.
 * 发送事件需要重新创建类
 */
@Deprecated
public class MessageEvent {

    private String action="";
    private String msg="";
    private int id = 0;

    public MessageEvent(String action) {
        this.action = action;
    }

    public MessageEvent() {
    }

    public int getId() {
        return id;
    }

    public MessageEvent setId(int id) {
        this.id = id;
        return this;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
