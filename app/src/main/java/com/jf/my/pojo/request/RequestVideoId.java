package com.jf.my.pojo.request;

/**
 * Created by JerryHo on 2019/3/18
 * Description:
 */
public class RequestVideoId {

    /**
     * id : 82
     * twoLevelName : mock
     * sort : 98
     */

    private int id;
    private  int type;
    public RequestVideoId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
