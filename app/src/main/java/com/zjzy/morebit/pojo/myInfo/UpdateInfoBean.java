package com.zjzy.morebit.pojo.myInfo;

import com.zjzy.morebit.utils.LoadImgUtils;

import java.io.Serializable;

/**
 * 升级后的用户信息
 * Created by haiping.liu on 2019-12-15.
 */
public class UpdateInfoBean implements Serializable {
    /**
     * 等级类型
     */
     private Integer userType;
    /**
     * 当前用户的剩余多豆
     */
    private Long moreCoin;

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Long getMoreCoin() {
        return moreCoin;
    }

    public void setMoreCoin(Long moreCoin) {
        this.moreCoin = moreCoin;
    }
}
