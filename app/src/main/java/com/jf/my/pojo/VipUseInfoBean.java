package com.jf.my.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liys on 2019/01/14.
 */
public class VipUseInfoBean implements Serializable {


    /**
     * type : 1
     * isAutoUpgrade : 0
     * conditionOne : [{"message":"直接邀请用户 0 人以上","oneLevelCount":0,"finishOneLevelCount":3},{"message":"连续0天累计结算佣金0元","oneLevelCount":0,"finishOneLevelCount":0}]
     * conditionTwo : [{"message":"直接邀请用户 0 人以上","oneLevelCount":0,"finishOneLevelCount":3},{"message":"间接邀请用户 0 人以上","oneLevelCount":0,"finishOneLevelCount":1},{"message":"连续0天累计结算佣金0元","oneLevelCount":0,"finishOneLevelCount":0}]
     */

    private int type;
    private int isAutoUpgrade;
    private List<ConditionOneBean> conditionOne;
    private List<ConditionOneBean> conditionTwo;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsAutoUpgrade() {
        return isAutoUpgrade;
    }

    public void setIsAutoUpgrade(int isAutoUpgrade) {
        this.isAutoUpgrade = isAutoUpgrade;
    }

    public List<ConditionOneBean> getConditionOne() {
        return conditionOne;
    }

    public void setConditionOne(List<ConditionOneBean> conditionOne) {
        this.conditionOne = conditionOne;
    }

    public List<ConditionOneBean> getConditionTwo() {
        return conditionTwo;
    }

    public void setConditionTwo(List<ConditionOneBean> conditionTwo) {
        this.conditionTwo = conditionTwo;
    }

    public static class ConditionOneBean {
        /**
         * message : 直接邀请用户 0 人以上
         * oneLevelCount : 0
         * finishOneLevelCount : 3
         */

        private String message;
        private int oneLevelCount;
        private double finishOneLevelCount;
        private String schedule;

        public String getSchedule() {
            return schedule;
        }

        public void setSchedule(String schedule) {
            this.schedule = schedule;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getOneLevelCount() {
            return oneLevelCount;
        }

        public void setOneLevelCount(int oneLevelCount) {
            this.oneLevelCount = oneLevelCount;
        }

        public double getFinishOneLevelCount() {
            return finishOneLevelCount;
        }

        public void setFinishOneLevelCount(double finishOneLevelCount) {
            this.finishOneLevelCount = finishOneLevelCount;
        }
    }

}
