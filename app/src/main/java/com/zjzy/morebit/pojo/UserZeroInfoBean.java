package com.zjzy.morebit.pojo;

import java.io.Serializable;
import java.util.List;

public class UserZeroInfoBean implements Serializable {
        /**
         * isNewUser : false
         * time : 641687222
         * itemList : [{"slogan":"福利价","itemPicture":"https://img.alicdn.com/bao/uploaded/i3/2200807493391/O1CN01RDh3r51av7fBBidYJ_!!2200807493391-0-lubanu-s.jpg","itemVoucherPrice":"0.00"},{"slogan":"福利价","itemPicture":"https://img.alicdn.com/bao/uploaded/i2/2966768356/O1CN01PUmfl32Bb68MUEGZZ_!!2966768356-0-lubanu-s.jpg","itemVoucherPrice":"0.00"},{"slogan":"福利价","itemPicture":"https://img.alicdn.com/bao/uploaded/i1/3058711528/O1CN01NCXfOG1N9rmkDutoP_!!0-item_pic.jpg","itemVoucherPrice":"0.00"}]
         * linkUrl :
         */

        private boolean isNewUser;//true 新人   false  不是
        private String time;//剩余时间
        private String linkUrl;//跳转地址
        private List<ItemListBean> itemList;//商品集合

        public boolean isIsNewUser() {
            return isNewUser;
        }

        public void setIsNewUser(boolean isNewUser) {
            this.isNewUser = isNewUser;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }

        public List<ItemListBean> getItemList() {
            return itemList;
        }

        public void setItemList(List<ItemListBean> itemList) {
            this.itemList = itemList;
        }

        public static class ItemListBean {
            /**
             * slogan : 福利价
             * itemPicture : https://img.alicdn.com/bao/uploaded/i3/2200807493391/O1CN01RDh3r51av7fBBidYJ_!!2200807493391-0-lubanu-s.jpg
             * itemVoucherPrice : 0.00
             */

            private String slogan;
            private String itemPicture;
            private String itemVoucherPrice;
            private String title;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSlogan() {
                return slogan;
            }

            public void setSlogan(String slogan) {
                this.slogan = slogan;
            }

            public String getItemPicture() {
                return itemPicture;
            }

            public void setItemPicture(String itemPicture) {
                this.itemPicture = itemPicture;
            }

            public String getItemVoucherPrice() {
                return itemVoucherPrice;
            }

            public void setItemVoucherPrice(String itemVoucherPrice) {
                this.itemVoucherPrice = itemVoucherPrice;
            }
        }
    }


