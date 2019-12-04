package com.zjzy.morebit.pojo.goods;

import java.io.Serializable;
import java.util.List;

public class Child2 implements Serializable {
    /**
     * id : 2
     * category_id : 1
     * pid : 1
     * name : 裙装
     * pictrue :
     * child : [{"id":3,"category_id":1,"pid":2,"name":"连衣裙","pictrue":"http://img.haodanku.com/89937f347f81f5c5539f9da9b35b7a62-600"},{"id":4,"category_id":1,"pid":2,"name":"雪纺裙","pictrue":"http://img.haodanku.com/3deb054da8cb2f4b1b5a07ab530e7e41-600"},{"id":5,"category_id":1,"pid":2,"name":"半身裙","pictrue":"http://img.haodanku.com/b68bc66ab1a81db336110b7c1196b5a9-600"},{"id":6,"category_id":1,"pid":2,"name":"印花裙","pictrue":"http://img.haodanku.com/3ce7249ba847286308c82bed97f7817d-600"},{"id":7,"category_id":1,"pid":2,"name":"吊带裙","pictrue":"http://img.haodanku.com/0716abc13652355b130dc3c83d39a7dc-600"},{"id":8,"category_id":1,"pid":2,"name":"纯色裙","pictrue":"http://img.haodanku.com/de464503dab5d20a5d6505573f1624bd-600"}]
     */

    private int id;
    private int parentId;
    private int pid;
    private String name="";
    private String picture="";
    private List<Child3> child;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int category_id) {
        this.parentId = category_id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictrue() {
        return picture;
    }

    public void setPictrue(String pictrue) {
        this.picture = pictrue;
    }

    public List<Child3> getChild3() {
        return child;
    }

    public void setChild3(List<Child3> child) {
        this.child = child;
    }

}