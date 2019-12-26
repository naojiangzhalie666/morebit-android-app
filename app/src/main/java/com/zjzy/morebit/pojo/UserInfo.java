package com.zjzy.morebit.pojo;

import java.io.Serializable;

/**
 * 用户信息实体
 */

public class UserInfo implements Serializable {

    /**
     * id : 3
     * username : 0
     * truename :
     * sex : 0
     * alipay : null
     * phone : 13202010629
     * pid : null
     * email : null
     * login_at : 2018-01-17 18:06:35
     * loginip : null
     * regist_time : 2018-01-17 17:58:19
     * status : 1
     * publish : 0
     * userhead : /userhead.png
     * do_money : 0.000
     * partner : 0
     * agent_money : 0.000
     * invite_code : ylydqm
     * vip : 0
     * money : 0.1
     */


    private String phone = "";
    private String nickname = "";
    private String headImg = "";  // 頭像
    private String realName = "";//真实姓名
    private String alipayNumber = "";//支付宝
    private String pid = "";
    private String oauthWx = "";//微信openId
    private String qrCode = "";// 微信二维码
    private String wxNumber;// 微信号
    private String inviteCode = "";//邀请码
    private String userType; // partner  是判断角色字段  0 会员 1：vip会员 2团队长
    private String calculationRate = "";// 是佣金计算比例
    private String gradename = "";// 用户等级名称
    private  int releasePermission;    //发圈发布管理权限 0 :否  1:有
    private int qr_code_show  ;//  微信二维码显示位置 全部 1，我的团队2，客服3
    private String relationIdName;//渠道id名称
    private String pidName;//pid名称
    private String relationId; // 渠道id
    private String spliceIdsName; //relationId名称和渠道id拼接
    private String label= ""; //发圈标签
    private String labelPicture= ""; //发圈标签图片
    private String id = "";

    private String birthDate;// 生日
    private int sex;//0未知，1男，2女
    private Long moreCoin;//多豆

    public Long getMoreCoin() {
        return moreCoin;
    }

    public void setMoreCoin(Long moreCoin) {
        this.moreCoin = moreCoin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String do_money = "";

    private String agent_money = "";


    private String grade = "";//  等级名称

    private String token = "";

    private String encryption = "";
    private boolean isNeedAuth;    //是否需要授权：ture需要，false不需要


    public String getRelationIdName() {
        return relationIdName;
    }

    public void setRelationIdName(String relationIdName) {
        this.relationIdName = relationIdName;
    }

    public String getPidName() {
        return pidName;
    }

    public void setPidName(String pidName) {
        this.pidName = pidName;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getTealName() {
        return realName;
    }

    public void setTealName(String realName) {
        this.realName = realName;
    }


    public String getAliPayNumber() {
        return alipayNumber;
    }

    public void setAliPayNumber(String alipayNumber) {
        this.alipayNumber = alipayNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }


    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getDo_money() {
        return do_money;
    }

    public void setDo_money(String do_money) {
        this.do_money = do_money;
    }

    public String getPartner() {
        return userType;// partner  是判断角色字段  0 会员 1vip会员  2团队长
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    public String getAgent_money() {
        return agent_money;
    }

    public void setAgent_money(String agent_money) {
        this.agent_money = agent_money;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInvite_code(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getOauthWx() {
        return oauthWx;
    }

    public void setOauthWx(String oauthWx) {
        this.oauthWx = oauthWx;
    }

    public String getNickName() {
        return nickname;
    }

    public void setNickName(String nickName) {
        this.nickname = nickName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


    public String getCalculationRate() {
        return calculationRate;
    }

    public void setCalculation(String calculationRate) {
        this.calculationRate = calculationRate;
    }



    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }



    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGradename() {
        return gradename;
    }

    public void setGradename(String gradename) {
        this.gradename = gradename;
    }

    public int getQr_code_show() {
        return qr_code_show;
    }

    public void setQr_code_show(int qr_code_show) {
        this.qr_code_show = qr_code_show;
    }

    public boolean isNeedAuth() {
        return isNeedAuth;
    }

    public void setNeedAuth(boolean needAuth) {
        isNeedAuth = needAuth;
    }

    public String getWxNumber() {
        return wxNumber;
    }

    public void setWxNumber(String wxNumber) {
        this.wxNumber = wxNumber;
    }

    public int getReleasePermission() {
        return releasePermission;
    }

    public void setReleasePermission(int releasePermission) {
        this.releasePermission = releasePermission;
    }

    public String getSpliceIdsName() {
        return spliceIdsName;
    }

    public void setSpliceIdsName(String spliceIdsName) {
        this.spliceIdsName = spliceIdsName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabelPicture() {
        return labelPicture;
    }

    public void setLabelPicture(String labelPicture) {
        this.labelPicture = labelPicture;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}