package com.zjzy.morebit.pojo;

import java.io.Serializable;
import java.util.List;

public class VipBean implements Serializable {


    /**
     * msg : 请求成功
     * data : [{"categoryTitle":"生活服务","vo":[{"id":28791,"title":"饿了么","picture":"https://img.morebit.com.cn/morebit-img/1595297749526.gif","mediaType":0,"url":"www.baidu.com","type":28,"sort":1,"open":3,"classId":null,"os":null,"width":0,"height":0,"couponUrl":null,"itemTitle":null,"backgroundImage":"","splicePid":"0","subTitle":"饿了么红包","categoryTitle":"生活服务"},{"id":28792,"title":"口碑","picture":"https://img.morebit.com.cn/morebit-img/1595297791163.gif","mediaType":0,"url":null,"type":28,"sort":2,"open":0,"classId":1,"os":null,"width":0,"height":0,"couponUrl":null,"itemTitle":null,"backgroundImage":"","splicePid":"0","subTitle":"特价美食","categoryTitle":"生活服务"}]},{"categoryTitle":"优惠特权","vo":[{"id":28793,"title":"限时秒杀","picture":"https://img.morebit.com.cn/morebit-img/1595297836779.png","mediaType":0,"url":"1","type":28,"sort":1,"open":0,"classId":null,"os":null,"width":0,"height":0,"couponUrl":null,"itemTitle":null,"backgroundImage":"","splicePid":"0","subTitle":"限时秒杀1","categoryTitle":"优惠特权"}]},{"categoryTitle":"购物特权","vo":[{"id":28789,"title":"淘宝","picture":"https://img.morebit.com.cn/morebit-img/1595297617010.png","mediaType":0,"url":null,"type":28,"sort":1,"open":0,"classId":1,"os":null,"width":0,"height":0,"couponUrl":null,"itemTitle":null,"backgroundImage":"","splicePid":"0","subTitle":"最高返佣87%","categoryTitle":"购物特权"},{"id":28790,"title":"京东","picture":"https://img.morebit.com.cn/morebit-img/1595297688255.jpg","mediaType":0,"url":null,"type":28,"sort":2,"open":0,"classId":1,"os":null,"width":0,"height":0,"couponUrl":null,"itemTitle":null,"backgroundImage":"","splicePid":"0","subTitle":"最高返佣87%","categoryTitle":"购物特权"}]}]
     * code : B00000
     */



        /**
         * categoryTitle : 生活服务
         * vo : [{"id":28791,"title":"饿了么","picture":"https://img.morebit.com.cn/morebit-img/1595297749526.gif","mediaType":0,"url":"www.baidu.com","type":28,"sort":1,"open":3,"classId":null,"os":null,"width":0,"height":0,"couponUrl":null,"itemTitle":null,"backgroundImage":"","splicePid":"0","subTitle":"饿了么红包","categoryTitle":"生活服务"},{"id":28792,"title":"口碑","picture":"https://img.morebit.com.cn/morebit-img/1595297791163.gif","mediaType":0,"url":null,"type":28,"sort":2,"open":0,"classId":1,"os":null,"width":0,"height":0,"couponUrl":null,"itemTitle":null,"backgroundImage":"","splicePid":"0","subTitle":"特价美食","categoryTitle":"生活服务"}]
         */

        private String categoryTitle;
        private List<VoBean> vo;

        public String getCategoryTitle() {
            return categoryTitle;
        }

        public void setCategoryTitle(String categoryTitle) {
            this.categoryTitle = categoryTitle;
        }

        public List<VoBean> getVo() {
            return vo;
        }

        public void setVo(List<VoBean> vo) {
            this.vo = vo;
        }

        public static class VoBean {
            /**
             * id : 28791
             * title : 饿了么
             * picture : https://img.morebit.com.cn/morebit-img/1595297749526.gif
             * mediaType : 0
             * url : www.baidu.com
             * type : 28
             * sort : 1
             * open : 3
             * classId : null
             * os : null
             * width : 0
             * height : 0
             * couponUrl : null
             * itemTitle : null
             * backgroundImage :
             * splicePid : 0
             * subTitle : 饿了么红包
             * categoryTitle : 生活服务
             */

            private int id;
            private String title;
            private String picture;
            private int mediaType;
            private String url;
            private int type;
            private int sort;
            private int open;
            private Object classId;
            private Object os;
            private int width;
            private int height;
            private Object couponUrl;
            private Object itemTitle;
            private String backgroundImage;
            private String splicePid;
            private String subTitle;
            private String categoryTitle;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getPicture() {
                return picture;
            }

            public void setPicture(String picture) {
                this.picture = picture;
            }

            public int getMediaType() {
                return mediaType;
            }

            public void setMediaType(int mediaType) {
                this.mediaType = mediaType;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getOpen() {
                return open;
            }

            public void setOpen(int open) {
                this.open = open;
            }

            public Object getClassId() {
                return classId;
            }

            public void setClassId(Object classId) {
                this.classId = classId;
            }

            public Object getOs() {
                return os;
            }

            public void setOs(Object os) {
                this.os = os;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public Object getCouponUrl() {
                return couponUrl;
            }

            public void setCouponUrl(Object couponUrl) {
                this.couponUrl = couponUrl;
            }

            public Object getItemTitle() {
                return itemTitle;
            }

            public void setItemTitle(Object itemTitle) {
                this.itemTitle = itemTitle;
            }

            public String getBackgroundImage() {
                return backgroundImage;
            }

            public void setBackgroundImage(String backgroundImage) {
                this.backgroundImage = backgroundImage;
            }

            public String getSplicePid() {
                return splicePid;
            }

            public void setSplicePid(String splicePid) {
                this.splicePid = splicePid;
            }

            public String getSubTitle() {
                return subTitle;
            }

            public void setSubTitle(String subTitle) {
                this.subTitle = subTitle;
            }

            public String getCategoryTitle() {
                return categoryTitle;
            }

            public void setCategoryTitle(String categoryTitle) {
                this.categoryTitle = categoryTitle;
            }
        }
    }




