package com.markermall.cat.pojo;

import com.markermall.cat.pojo.goods.ConsumerProtectionBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by feng on 2018/11/17.
 */

public class ShopEvaluateGood extends ShopGoodInfo implements Serializable {


    private String taobaoDescUrl ="";
    private String tmallDescUrl="";

    private String from="";

    private String fans="";
    private List<ConsumerProtectionBean> items;
    private List<EvaluatesBean> evaluates;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

        public String getTaobaoDescUrl() {
            return taobaoDescUrl;
    }

    public void setTaobaoDescUrl(String taobaoDescUrl) {
        this.taobaoDescUrl = taobaoDescUrl;
    }

    public String getTmallDescUrl() {
        return tmallDescUrl;
    }

    public void setTmallDescUrl(String tmallDescUrl) {
        this.tmallDescUrl = tmallDescUrl;
    }

    public List<ConsumerProtectionBean> getItems() {
        return items;
    }

    public void setItems(List<ConsumerProtectionBean> items) {
        this.items = items;
    }

    public List<EvaluatesBean> getEvaluates1() {
        return evaluates;
    }



    public static class EvaluatesBean {
        /**
         * title : 宝贝描述
         * score : 4.8
         * type : desc
         * level : 0
         * levelText : 平
         * levelTextColor : #999999
         * levelBackgroundColor : #EEEEEE
         */

        private String title="";
        private String score="";
        private String type="";
        private String level="";
        private String levelText="";
        private String levelTextColor="";
        private String levelBackgroundColor="";

        public String getTitle() {
            return title;
        }

        public void setTitle(String titleX) {
            this.title = titleX;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getLevelText() {
            return levelText;
        }

        public void setLevelText(String levelText) {
            this.levelText = levelText;
        }

        public String getLevelTextColor() {
            return levelTextColor;
        }

        public void setLevelTextColor(String levelTextColor) {
            this.levelTextColor = levelTextColor;
        }

        public String getLevelBackgroundColor() {
            return levelBackgroundColor;
        }

        public void setLevelBackgroundColor(String levelBackgroundColor) {
            this.levelBackgroundColor = levelBackgroundColor;
        }
    }

}
