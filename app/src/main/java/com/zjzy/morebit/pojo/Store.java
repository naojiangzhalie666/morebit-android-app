package com.zjzy.morebit.pojo;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2018/8/20.
 */

public class Store implements Serializable {

    /**
     * id : 44
     * investment_id : 19
     * seller_id : ajilchiban
     * seller_name : TOP家 TOPCLOSET l頂l級l私l櫥l
     * seller_picture :
     * seller_url : https://t-closet.taobao.com/index.htm?spm=2013.1.w5002-15245075711.2.182c4db9ViXt0i
     * shoptype : 1
     * create_time : 2018-08-20 15:53:45
     */

    private int id;
    private int investment_id;
    private String seller_id;
    private String seller_name;
    private String seller_picture;
    private String seller_url;
    private int shoptype;
    private String create_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInvestment_id() {
        return investment_id;
    }

    public void setInvestment_id(int investment_id) {
        this.investment_id = investment_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getSeller_picture() {
        return seller_picture;
    }

    public void setSeller_picture(String seller_picture) {
        this.seller_picture = seller_picture;
    }

    public String getSeller_url() {
        return seller_url;
    }

    public void setSeller_url(String seller_url) {
        this.seller_url = seller_url;
    }

    public int getShoptype() {
        return shoptype;
    }

    public void setShoptype(int shoptype) {
        this.shoptype = shoptype;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
