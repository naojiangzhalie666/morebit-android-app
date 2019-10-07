package com.markermall.cat.pojo.myInfo;

/**
 * Created by fengrs on 2018/9/8.
 * 备注:
 */

public class MakeMoenyBean {

    /**
     * picture : https://img.gzzhitu.com/picture/20180807/153360982656403.jpg
     * intro : {"left":{"title":"直属下家","subtitle":"每单赚60%佣金"},"right":{"title":"非直属下家","subtitle":"每单赚20%佣金"},"content":"邀请好友注册会员成为你的下家，下家在马克猫上购买每笔订单，你都可以获得该商品显示佣金的60%作为奖励，下家在推广邀请注册得到下家，你都可获得显示佣金的20%作为奖励。邀请的下家满足条件还可升级"}
     * link : https://img.gzzhitu.com/apphtml/97.html
     * income : : 85.24
     * e_month  : 0
     * grade  : 代理商
     * fan_count  : 31
     */

    private String picture = "";
    private IntroBean intro;
    private String link= "";
    private  String  eToday=""; // 一共收益
    private  String eMonth ="";  // 一个月收益
    private String grade = ""; // 角色
    private int fanCount  ; //粉丝数
    private String  operator;
    private String inviteCode;

    public String getEToday() {
        return eToday;
    }

    public void setEToday(String eToday) {
        this.eToday = eToday;
    }

    public String getEMonth() {
        return eMonth;
    }

    public void setEMonth(String eMonth) {
        this.eMonth = eMonth;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getFanCount() {
        return fanCount;
    }

    public void setFanCount(int fanCount) {
        this.fanCount = fanCount;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public IntroBean getIntro() {
        return intro;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public void setIntro(IntroBean intro) {
        this.intro = intro;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return operator;
    }


    public static class IntroBean {
        /**
         * left : {"title":"直属下家","subtitle":"每单赚60%佣金"}
         * right : {"title":"非直属下家","subtitle":"每单赚20%佣金"}
         * content : 邀请好友注册会员成为你的下家，下家在马克猫上购买每笔订单，你都可以获得该商品显示佣金的60%作为奖励，下家在推广邀请注册得到下家，你都可获得显示佣金的20%作为奖励。邀请的下家满足条件还可升级
         */

        private ItmeBean left ;
        private ItmeBean right;
        private String content= "";

        public ItmeBean getLeft() {
            return left;
        }

        public void setLeft(ItmeBean left) {
            this.left = left;
        }

        public ItmeBean getRight() {
            return right;
        }

        public void setRight(ItmeBean right) {
            this.right = right;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }


        public static class ItmeBean {
            /**
             * title : 非直属下家
             * subtitle : 每单赚20%佣金
             */

            private String title= "";
            private String subtitle= "";

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSubtitle() {
                return subtitle;
            }

            public void setSubtitle(String subtitle) {
                this.subtitle = subtitle;
            }
        }
    }
}
