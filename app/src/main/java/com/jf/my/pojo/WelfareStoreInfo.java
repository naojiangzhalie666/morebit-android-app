package com.jf.my.pojo;

import java.io.Serializable;

/**
 * Created by fengrs on 2018/5/28.
 */

public class WelfareStoreInfo  implements Serializable {

    /**
     * id : 26
     * taobao : 568511735487
     * start_time : 2018-05-22 14:20:42
     * end_time : 2018-05-24 14:20:42
     * picture : https://img.alicdn.com/tfscom/i2/114419352/TB23RoBnL5TBuNjSspcXXbnGFXa_!!114419352.jpg_120x120.jpg
     * price : 100.00
     * tb_reserve_price : 289.00
     * user_type : 0
     * zk_final_price : null
     * title : 垂感阔腿裤女夏季南瓜谷2018新款复古温柔风chic高腰九分休闲裤子
     * pickup_quantity : 0
     */

    private int id;
    private String taobao="";
    private String start_time="";
    private String end_time="";
    private String picture="";
    private String price="";
    private String tb_reserve_price="";
    private int user_type;
    private Object zk_final_price;
    private String title="";
    private String coupon_url="";
    private int pickup_quantity;  //领取的数量
    private int free_quantity ;//免单的数量
    private String qualifications_tips="" ;//抢购状态

    public String getQualifications_tips() {
        return qualifications_tips;
    }

    public void setQualifications_tips(String qualifications_tips) {
        this.qualifications_tips = qualifications_tips;
    }

    public int getFree_quantity() {
        return free_quantity;
    }

    public void setFree_quantity(int free_quantity) {
        this.free_quantity = free_quantity;
    }

    public String getCoupon_url() {
        return coupon_url;
    }

    public void setCoupon_url(String coupon_url) {
        this.coupon_url = coupon_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaobao() {
        return taobao;
    }

    public void setTaobao(String taobao) {
        this.taobao = taobao;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTb_reserve_price() {
        return tb_reserve_price;
    }

    public void setTb_reserve_price(String tb_reserve_price) {
        this.tb_reserve_price = tb_reserve_price;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public Object getZk_final_price() {
        return zk_final_price;
    }

    public void setZk_final_price(Object zk_final_price) {
        this.zk_final_price = zk_final_price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPickup_quantity() {
        return pickup_quantity;
    }

    public void setPickup_quantity(int pickup_quantity) {
        this.pickup_quantity = pickup_quantity;
    }
}
