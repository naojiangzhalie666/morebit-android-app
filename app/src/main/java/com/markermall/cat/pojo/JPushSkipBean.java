package com.markermall.cat.pojo;

import java.io.Serializable;

/**
 * Created by fengrs on 2018/11/28.
 */

public class JPushSkipBean implements Serializable {

    /**
     * n_extras : {}
     * msg_id : 38280599675084505
     * rom_type : 2
     * n_content : 123456
     * n_title : zyhzyh
     */

    private long msg_id;
    private int rom_type;
    private String n_content;
    private String n_title;
    private PushMsgInfo n_extras;

    public long getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(long msg_id) {
        this.msg_id = msg_id;
    }

    public int getRom_type() {
        return rom_type;
    }

    public void setRom_type(int rom_type) {
        this.rom_type = rom_type;
    }

    public String getN_content() {
        return n_content;
    }

    public void setN_content(String n_content) {
        this.n_content = n_content;
    }

    public String getN_title() {
        return n_title;
    }

    public void setN_title(String n_title) {
        this.n_title = n_title;
    }

    public static class NExtrasBean {
    }

    public PushMsgInfo getN_extras() {
        return n_extras;
    }

    public void setN_extras(PushMsgInfo n_extras) {
        this.n_extras = n_extras;
    }
}
