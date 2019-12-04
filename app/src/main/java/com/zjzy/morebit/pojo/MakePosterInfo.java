package com.zjzy.morebit.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/27.
 */

public class MakePosterInfo implements Serializable{
    /**
     * id : 32
     * group : extension
     * title : 3
     * details : 3
     * thumb : /20171026/150902216669401.jpg
     * sort : 3
     * status : 1
     * url : #
     */

    private String picture="";



    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
