package com.zjzy.morebit.pojo;

import java.io.Serializable;
import java.util.List;

public class DoorGodCategoryBean implements Serializable {

        private List<ResultListBean> resultList;

        public List<ResultListBean> getResultList() {
            return resultList;
        }

        public void setResultList(List<ResultListBean> resultList) {
            this.resultList = resultList;
        }

        public static class ResultListBean {
            /**
             * categoryId : 1
             * categoryName : 热门
             * wheelChartDisplayVo : [{"id":1525,"title":"高佣精品","picture":"http://morebit-static.oss-cn-hangzhou.aliyuncs.com/morebit-img/1584518423495.png","mediaType":0,"url":"14624","type":21,"sort":8,"open":11,"desc":"1","classId":null,"os":null,"width":0,"height":0,"couponUrl":null,"itemTitle":null,"displayStyle":0,"colorValue":"","backgroundImage":"","permission":"","splicePid":"0","subTitle":"","mark":0,"popupType":0,"categoryId":1,"categoryName":null}]
             */

            private int categoryId;
            private String categoryName;
            private List<WheelChartDisplayVoBean> wheelChartDisplayVo;

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public List<WheelChartDisplayVoBean> getWheelChartDisplayVo() {
                return wheelChartDisplayVo;
            }

            public void setWheelChartDisplayVo(List<WheelChartDisplayVoBean> wheelChartDisplayVo) {
                this.wheelChartDisplayVo = wheelChartDisplayVo;
            }

            public static class WheelChartDisplayVoBean {
                /**
                 * id : 1525
                 * title : 高佣精品
                 * picture : http://morebit-static.oss-cn-hangzhou.aliyuncs.com/morebit-img/1584518423495.png
                 * mediaType : 0
                 * url : 14624
                 * type : 21
                 * sort : 8
                 * open : 11
                 * desc : 1
                 * classId : null
                 * os : null
                 * width : 0
                 * height : 0
                 * couponUrl : null
                 * itemTitle : null
                 * displayStyle : 0
                 * colorValue :
                 * backgroundImage :
                 * permission :
                 * splicePid : 0
                 * subTitle :
                 * mark : 0
                 * popupType : 0
                 * categoryId : 1
                 * categoryName : null
                 */

                private int id;
                private String title;
                private String picture;
                private int mediaType;
                private String url;
                private int type;
                private int sort;
                private int open;
                private String desc;
                private int classId;
                private int os;
                private int width;
                private int height;
                private String couponUrl;
                private String itemTitle;
                private int displayStyle;
                private String colorValue;
                private String backgroundImage;
                private String permission;
                private String splicePid;
                private String subTitle;
                private int mark;
                private int popupType;
                private int categoryId;
                private String categoryName;

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

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
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




                public int getDisplayStyle() {
                    return displayStyle;
                }

                public void setDisplayStyle(int displayStyle) {
                    this.displayStyle = displayStyle;
                }

                public String getColorValue() {
                    return colorValue;
                }

                public void setColorValue(String colorValue) {
                    this.colorValue = colorValue;
                }

                public String getBackgroundImage() {
                    return backgroundImage;
                }

                public void setBackgroundImage(String backgroundImage) {
                    this.backgroundImage = backgroundImage;
                }

                public String getPermission() {
                    return permission;
                }

                public void setPermission(String permission) {
                    this.permission = permission;
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

                public int getMark() {
                    return mark;
                }

                public void setMark(int mark) {
                    this.mark = mark;
                }

                public int getPopupType() {
                    return popupType;
                }

                public void setPopupType(int popupType) {
                    this.popupType = popupType;
                }

                public int getCategoryId() {
                    return categoryId;
                }

                public void setCategoryId(int categoryId) {
                    this.categoryId = categoryId;
                }

                public int getClassId() {
                    return classId;
                }

                public void setClassId(int classId) {
                    this.classId = classId;
                }

                public int getOs() {
                    return os;
                }

                public void setOs(int os) {
                    this.os = os;
                }

                public String getCouponUrl() {
                    return couponUrl;
                }

                public void setCouponUrl(String couponUrl) {
                    this.couponUrl = couponUrl;
                }

                public String getItemTitle() {
                    return itemTitle;
                }

                public void setItemTitle(String itemTitle) {
                    this.itemTitle = itemTitle;
                }

                public String getCategoryName() {
                    return categoryName;
                }

                public void setCategoryName(String categoryName) {
                    this.categoryName = categoryName;
                }
            }
        }
    }


