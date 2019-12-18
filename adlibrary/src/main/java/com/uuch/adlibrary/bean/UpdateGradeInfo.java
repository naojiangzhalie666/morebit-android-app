package com.uuch.adlibrary.bean;

/**
 * Created by haiping.liu on 2019-12-17.
 */
public class UpdateGradeInfo {
    /**
     * vip升级用的多豆
     */
    private String vipMoreCoin;
    /**
     * 团队长的多豆
     */
    private String leaderMoreCoin;

    public String getVipMoreCoin() {
        return vipMoreCoin;
    }

    public void setVipMoreCoin(String vipMoreCoin) {
        this.vipMoreCoin = vipMoreCoin;
    }

    public String getLeaderMoreCoin() {
        return leaderMoreCoin;
    }

    public void setLeaderMoreCoin(String leaderMoreCoin) {
        this.leaderMoreCoin = leaderMoreCoin;
    }
}
