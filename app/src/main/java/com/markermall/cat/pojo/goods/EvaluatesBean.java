package com.markermall.cat.pojo.goods;

public   class EvaluatesBean {
        /**
         * title : 宝贝描述
         * score : 4.8
         * type : desc
         * level : 1
         * levelText : 高
         * levelTextColor : #FF5000
         * levelBackgroundColor : #FFF1EB
         * tmallLevelTextColor : #FF0036
         * tmallLevelBackgroundColor : #FFF1EB
         */

        private String title="";
        private String score="";
        private String type="";
        private String level="";
        private String levelText="";

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

    }