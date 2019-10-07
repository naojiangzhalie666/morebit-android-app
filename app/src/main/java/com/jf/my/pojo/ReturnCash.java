package com.jf.my.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YangBoTian on 2018/8/20.
 */

public class ReturnCash  implements Serializable {

    /**
     * taobao : 537883173612
     * title  : 手工巧克力原料千言万语diy巧克力块砖 可可脂12种口味
     * price : 8.5
     * picture : https: //img.alicdn.com/bao/uploaded/i2/115330117/TB2_NGTiuuSBuNjSsplXXbe8pXa_!!115330117.jpg
     * showdesc : 大范德萨发大水发捡垃圾
     * banner : ["https: //img.alicdn.com/i2/115330117/TB2pSi1ixSYBuNjSsphXXbGvVXa_!!115330117.jpg","https: //img.alicdn.com/i3/115330117/TB2X.MziDlYBeNjSszcXXbwhFXa_!!115330117.jpg","https: //img.alicdn.com/i1/115330117/TB2k9y6iruWBuNjSszgXXb8jVXa_!!115330117.jpg","https: //img.alicdn.com/i4/115330117/TB2ovzMirSYBuNjSspfXXcZCpXa_!!115330117.jpg"]
     * shoptype : 1
     * rebate_money : 10.00
     * invite_num : 12
     * stock : 20
     * sold_stock : 10
     * ad_banner : https: //img.gzzhitu.com/picture/20180817/153447000896172.jpeg
     * create_time : 2018-08-06 18:45:24
     * start_time : 1533867118
     * end_time : 1535644800
     * taobao_url : https: //item.taobao.com/item.htm?id=537883173612
     * status : 0--> 已经开抢,1-->已经抢光,2-->倒计时,3-->即将开抢
     */

    private long taobao;
    private String title ="";
    private double price;
    private String picture="";
    private String showdesc="";
    private int shoptype;
    private String rebate_money="";
    private int invite_num; // 邀请总数
    private int already_invited_num; //已经邀请的人数
    private int stock;
    private int sold_stock;
    private String ad_banner="";
    private String create_time="";
    private int start_time;
    private int end_time;
    private String taobao_url="";
    private List<String> banner;
    private int status;
    private int investment_id;
    private int store_id;

    public int getInvestment_id() {
        return investment_id;
    }

    public void setInvestment_id(int investment_id) {
        this.investment_id = investment_id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public long getTaobao() {
        return taobao;
    }

    public void setTaobao(long taobao) {
        this.taobao = taobao;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getShowdesc() {
        return showdesc;
    }

    public void setShowdesc(String showdesc) {
        this.showdesc = showdesc;
    }

    public int getShoptype() {
        return shoptype;
    }

    public void setShoptype(int shoptype) {
        this.shoptype = shoptype;
    }

    public String getRebate_money() {
        return rebate_money;
    }

    public void setRebate_money(String rebate_money) {
        this.rebate_money = rebate_money;
    }

    public int getInvite_num() {
        return invite_num;
    }

    public void setInvite_num(int invite_num) {
        this.invite_num = invite_num;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getSold_stock() {
        return sold_stock;
    }

    public void setSold_stock(int sold_stock) {
        this.sold_stock = sold_stock;
    }

    public String getAd_banner() {
        return ad_banner;
    }

    public void setAd_banner(String ad_banner) {
        this.ad_banner = ad_banner;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public String getTaobao_url() {
        return taobao_url;
    }

    public void setTaobao_url(String taobao_url) {
        this.taobao_url = taobao_url;
    }

    public List<String> getBanner() {
        return banner;
    }

    public void setBanner(List<String> banner) {
        this.banner = banner;
    }

    public int getAlready_invited_num() {
        return already_invited_num;
    }

    public void setAlready_invited_num(int already_invited_num) {
        this.already_invited_num = already_invited_num;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
