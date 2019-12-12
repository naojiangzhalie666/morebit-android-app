package com.zjzy.morebit.pojo.number;

import java.io.Serializable;

/**
 * 会员商品item
 * Created by haiping.liu on 2019-12-11.
 */

public class NumberGoods implements Serializable {
    /**
     * 商品Id
     */
    private int id;
    /**
     * 商品图片
     */
    private String img;
    /**
     * 商品描述
     */
    private String desc;
    /**
     * 商品价格
     */
    private String price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
