package com.zjzy.morebit.pojo;


import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/3.
 */

public class TeamInfo implements Serializable {

    /**
     * username : AutumnWind
     * userhead : /userhead.png
     * phone : 17620179915
     * id : 2
     * partner : 1
     * ref_num : 1
     */
    private int id   ;//用户ID
    private String phone = "";//电话号码
    private String headImg = "";//用户头像
    private String inviteCode = "";//邀请码
    private String createTime = "";//注册时间
    private String grade = "";//级别(会员、VIP会员、运营专员)

    private String nickname = "";
    private String remark;  //备注
     private String  lastActiveTime;  //最后登录时间
     private String  sevenCommission;   //近7天佣金
     private String  sevenNewUsers;     //近7天拉新
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    private String nickName = "";
   private String wxNumber= "";  //微信号
    private String wxQrCode=""; //微信二维码
    private int userType; //用户类型
    private int customerServiceType; //客服类型：0运营专员，1公司

    private String specialId;//是否授权

    private String invitationUserName;
    private String fsType;

    public String getFsType() {
        return fsType;
    }

    public void setFsType(String fsType) {
        this.fsType = fsType;
    }

    public String getInvitationUserName() {
        return invitationUserName;
    }

    public void setInvitationUserName(String invitationUserName) {
        this.invitationUserName = invitationUserName;
    }

    public String getSpecialId() {
        return specialId;
    }

    public void setSpecialId(String specialId) {
        this.specialId = specialId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getCustomerServiceType() {
        return customerServiceType;
    }

    public void setCustomerServiceType(int customerServiceType) {
        this.customerServiceType = customerServiceType;
    }

    private int childCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickName) {
        this.nickname = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getWxNumber() {
        return wxNumber;
    }

    public void setWxNumber(String wxNumber) {
        this.wxNumber = wxNumber;
    }

    public String getWxQrCode() {
        return wxQrCode;
    }

    public void setWxQrCode(String wxQrCode) {
        this.wxQrCode = wxQrCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(String lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public String getSevenCommission() {
        return sevenCommission;
    }

    public void setSevenCommission(String sevenCommission) {
        this.sevenCommission = sevenCommission;
    }

    public String getSevenNewUsers() {
        return sevenNewUsers;
    }

    public void setSevenNewUsers(String sevenNewUsers) {
        this.sevenNewUsers = sevenNewUsers;
    }
}
