package com.markermall.cat.pojo;

import java.io.Serializable;

/**
 * Created by fengrs on 2018/5/28.
 */

public class WelfareStoreDetailsInfo extends ShopGoodInfo implements Serializable {
    private int free_quantity;
    private int pickup_quantity; // 領取數量
    private int user_type;
    private int person_num;  // 需要邀请的人
    private int invite_number;// 自己邀请的人
    private String tb_reserve_price="";
    private String lb_picture="";
    private String qualifications="";//  领取过人的Id
    private String cupon_url="";//  优惠地址 获取淘口令
    private int remaining_quantity;//  剩余免单份数
    private String qualifications_tips="";//    提示语句
    private int qualifications_type;//    1 立即领取  2邀请好友支持  3已经领取  4名额已满

    public int getRemaining_quantity() {
        return remaining_quantity;
    }

    public void setRemaining_quantity(int remaining_quantity) {
        this.remaining_quantity = remaining_quantity;
    }

    public String getQualifications_tips() {
        return qualifications_tips;
    }

    public void setQualifications_tips(String qualifications_tips) {
        this.qualifications_tips = qualifications_tips;
    }

    public int getQualifications_type() {
        return qualifications_type;
    }

    public void setQualifications_type(int qualifications_type) {
        this.qualifications_type = qualifications_type;
    }

    public String getCupon_url() {
        return cupon_url;
    }

    public void setCupon_url(String cupon_url) {
        this.cupon_url = cupon_url;
    }

    public int getPerson_num() {
        return person_num;
    }

    public void setPerson_num(int person_num) {
        this.person_num = person_num;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getLb_picture() {
        return lb_picture;
    }

    public void setLb_picture(String lb_picture) {
        this.lb_picture = lb_picture;
    }

    public int getFree_quantity() {
        return free_quantity;
    }

    public void setFree_quantity(int free_quantity) {
        this.free_quantity = free_quantity;
    }

    public int getPickup_quantity() {
        return pickup_quantity;
    }

    public void setPickup_quantity(int pickup_quantity) {
        this.pickup_quantity = pickup_quantity;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getInvite_number() {
        return invite_number;
    }

    public void setInvite_number(int invite_number) {
        this.invite_number = invite_number;
    }

    public String getTb_reserve_price() {
        return tb_reserve_price;
    }

    public void setTb_reserve_price(String tb_reserve_price) {
        this.tb_reserve_price = tb_reserve_price;
    }
}
