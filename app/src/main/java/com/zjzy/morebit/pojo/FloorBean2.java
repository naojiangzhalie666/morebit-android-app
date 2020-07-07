package com.zjzy.morebit.pojo;

import java.io.Serializable;
import java.util.List;

public class FloorBean2 implements Serializable {



        /**
         * listData : [{"id":106,"showType":7,"mainTitleShow":0,"mainTitle":"7","subTitle":"","sort":1,"status":1,"childNum":0,"child":[{"id":28781,"mainTitle":"","subTitle":"","picture":"https://img.morebit.com.cn/morebit-img/1593588769678.gif","backgroundImage":"","sort":1,"open":0,"graphicInfoId":"28781","url":"1","showType":7,"splicePid":"0"}]},{"id":96,"showType":5,"mainTitleShow":0,"mainTitle":"测试罗岑","subTitle":"","sort":12,"status":1,"childNum":0,"child":[{"id":28758,"mainTitle":"","subTitle":"","picture":"https://img.morebit.com.cn/morebit-img/1593325563284.png","backgroundImage":"","sort":1,"open":0,"graphicInfoId":"28758","classId":1,"showType":5,"splicePid":"0","desc":""},{"id":28759,"mainTitle":"","subTitle":"","picture":"https://img.morebit.com.cn/morebit-img/1593325590787.jpeg","backgroundImage":"","sort":2,"open":0,"graphicInfoId":"28759","classId":1,"showType":5,"splicePid":"0","desc":""},{"id":28760,"mainTitle":"","subTitle":"","picture":"https://img.morebit.com.cn/morebit-img/1593325610489.png","backgroundImage":"","sort":3,"open":0,"graphicInfoId":"28760","classId":1,"showType":5,"splicePid":"0","desc":""},{"id":28761,"mainTitle":"","subTitle":"","picture":"https://img.morebit.com.cn/morebit-img/1593325628991.jpg","backgroundImage":"","sort":4,"open":0,"graphicInfoId":"28761","classId":1,"showType":5,"splicePid":"0","desc":""}]},{"id":101,"showType":6,"mainTitleShow":0,"mainTitle":"楼层666","subTitle":"666","sort":23,"status":1,"childNum":0,"child":[{"id":28764,"mainTitle":"","subTitle":"","picture":"https://img.morebit.com.cn/morebit-img/1593335040718.png","backgroundImage":"","sort":1,"open":0,"graphicInfoId":"28764","classId":1,"showType":6,"splicePid":"0","desc":""},{"id":28765,"mainTitle":"","subTitle":"","picture":"https://img.morebit.com.cn/morebit-img/1593335061811.png","backgroundImage":"","sort":2,"open":0,"graphicInfoId":"28765","classId":1,"showType":6,"splicePid":"0","desc":""},{"id":28766,"mainTitle":"","subTitle":"","picture":"https://img.morebit.com.cn/morebit-img/1593335071665.jpeg","backgroundImage":"","sort":3,"open":0,"graphicInfoId":"28766","classId":1,"showType":6,"splicePid":"0","desc":""},{"id":28767,"mainTitle":"","subTitle":"","picture":"https://img.morebit.com.cn/morebit-img/1593335081163.png","backgroundImage":"","sort":4,"open":0,"graphicInfoId":"28767","classId":1,"showType":6,"splicePid":"0","desc":""}]}]
         * ext : #ADD8E6
         */

        private String ext;
        private List<ListDataBean> listData;

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        public List<ListDataBean> getListData() {
            return listData;
        }

        public void setListData(List<ListDataBean> listData) {
            this.listData = listData;
        }

        public static class ListDataBean {
            /**
             * id : 106
             * showType : 7
             * mainTitleShow : 0
             * mainTitle : 7
             * subTitle :
             * sort : 1
             * status : 1
             * childNum : 0
             * child : [{"id":28781,"mainTitle":"","subTitle":"","picture":"https://img.morebit.com.cn/morebit-img/1593588769678.gif","backgroundImage":"","sort":1,"open":0,"graphicInfoId":"28781","url":"1","showType":7,"splicePid":"0"}]
             */

            private int id;
            private int showType;
            private int mainTitleShow;
            private String mainTitle;
            private String subTitle;
            private int sort;
            private int status;
            private int childNum;
            private List<ChildBean> child;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getShowType() {
                return showType;
            }

            public void setShowType(int showType) {
                this.showType = showType;
            }

            public int getMainTitleShow() {
                return mainTitleShow;
            }

            public void setMainTitleShow(int mainTitleShow) {
                this.mainTitleShow = mainTitleShow;
            }

            public String getMainTitle() {
                return mainTitle;
            }

            public void setMainTitle(String mainTitle) {
                this.mainTitle = mainTitle;
            }

            public String getSubTitle() {
                return subTitle;
            }

            public void setSubTitle(String subTitle) {
                this.subTitle = subTitle;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getChildNum() {
                return childNum;
            }

            public void setChildNum(int childNum) {
                this.childNum = childNum;
            }

            public List<ChildBean> getChild() {
                return child;
            }

            public void setChild(List<ChildBean> child) {
                this.child = child;
            }

            public static class ChildBean {
                /**
                 * id : 28781
                 * mainTitle :
                 * subTitle :
                 * picture : https://img.morebit.com.cn/morebit-img/1593588769678.gif
                 * backgroundImage :
                 * sort : 1
                 * open : 0
                 * graphicInfoId : 28781
                 * url : 1
                 * showType : 7
                 * splicePid : 0
                 */

                private int id;
                private String mainTitle;
                private String subTitle;
                private String picture;
                private String backgroundImage;
                private int sort;
                private int open;
                private String graphicInfoId;
                private String url;
                private int showType;
                private String splicePid;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getMainTitle() {
                    return mainTitle;
                }

                public void setMainTitle(String mainTitle) {
                    this.mainTitle = mainTitle;
                }

                public String getSubTitle() {
                    return subTitle;
                }

                public void setSubTitle(String subTitle) {
                    this.subTitle = subTitle;
                }

                public String getPicture() {
                    return picture;
                }

                public void setPicture(String picture) {
                    this.picture = picture;
                }

                public String getBackgroundImage() {
                    return backgroundImage;
                }

                public void setBackgroundImage(String backgroundImage) {
                    this.backgroundImage = backgroundImage;
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

                public String getGraphicInfoId() {
                    return graphicInfoId;
                }

                public void setGraphicInfoId(String graphicInfoId) {
                    this.graphicInfoId = graphicInfoId;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public int getShowType() {
                    return showType;
                }

                public void setShowType(int showType) {
                    this.showType = showType;
                }

                public String getSplicePid() {
                    return splicePid;
                }

                public void setSplicePid(String splicePid) {
                    this.splicePid = splicePid;
                }
            }
        }
    }

