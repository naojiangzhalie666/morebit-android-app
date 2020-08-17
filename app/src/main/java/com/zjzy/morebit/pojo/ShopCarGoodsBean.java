package com.zjzy.morebit.pojo;

import java.io.Serializable;
import java.util.List;

public class ShopCarGoodsBean implements Serializable {


        /**
         * cartTotal : {"goodsCount":8,"checkedGoodsCount":8,"goodsAmount":588.02,"checkedGoodsAmount":588.02}
         * cartList : [{"productId":275,"addTime":"2020-08-13 10:25:32","goodsId":1181005,"goodsSn":"15209035","updateTime":"2020-08-13 10:26:50","userId":246322,"specifications":["标准"],"number":6,"picUrl":"https://file.morebit.com.cn/ui4qcftyplisy80cdbcg.jpeg","deleted":false,"price":98,"checked":true,"id":8,"goodsName":"iPhone"},{"productId":281,"addTime":"2020-08-13 11:22:36","goodsId":1181006,"goodsSn":"001","updateTime":"2020-08-13 11:22:36","userId":246322,"specifications":["白色"],"number":2,"picUrl":"https://file.morebit.com.cn/crvugteda6h5c3q08jsw.jpg","deleted":false,"price":0.01,"checked":true,"id":9,"goodsName":"测试"}]
         */

        private CartTotalBean cartTotal;
        private List<CartListBean> cartList;

        public CartTotalBean getCartTotal() {
            return cartTotal;
        }

        public void setCartTotal(CartTotalBean cartTotal) {
            this.cartTotal = cartTotal;
        }

        public List<CartListBean> getCartList() {
            return cartList;
        }

        public void setCartList(List<CartListBean> cartList) {
            this.cartList = cartList;
        }

        public static class CartTotalBean {
            /**
             * goodsCount : 8
             * checkedGoodsCount : 8
             * goodsAmount : 588.02
             * checkedGoodsAmount : 588.02
             */

            private int goodsCount;//购物车商品数量
            private int checkedGoodsCount;//已勾选商品数量
            private String goodsAmount;//商品总价
            private String checkedGoodsAmount;//勾选商品总价

            public int getGoodsCount() {
                return goodsCount;
            }

            public void setGoodsCount(int goodsCount) {
                this.goodsCount = goodsCount;
            }

            public int getCheckedGoodsCount() {
                return checkedGoodsCount;
            }

            public void setCheckedGoodsCount(int checkedGoodsCount) {
                this.checkedGoodsCount = checkedGoodsCount;
            }

            public String getGoodsAmount() {
                return goodsAmount;
            }

            public void setGoodsAmount(String goodsAmount) {
                this.goodsAmount = goodsAmount;
            }

            public String getCheckedGoodsAmount() {
                return checkedGoodsAmount;
            }

            public void setCheckedGoodsAmount(String checkedGoodsAmount) {
                this.checkedGoodsAmount = checkedGoodsAmount;
            }
        }

        public static class CartListBean implements Serializable {
            /**
             * productId : 275
             * addTime : 2020-08-13 10:25:32
             * goodsId : 1181005
             * goodsSn : 15209035
             * updateTime : 2020-08-13 10:26:50
             * userId : 246322
             * specifications : ["标准"]
             * number : 6
             * picUrl : https://file.morebit.com.cn/ui4qcftyplisy80cdbcg.jpeg
             * deleted : false
             * price : 98.0
             * checked : true
             * id : 8
             * goodsName : iPhone
             */

            private String productId;//商品货品表id
            private String addTime;//添加时间
            private String goodsId;//商品id
            private String goodsSn;//商品编号
            private String updateTime;//修改时间
            private int userId;//用户id
            private String number;//商品数量
            private String picUrl;//商品图片
            private boolean deleted;//是否删除 ture 删除
            private String price;//商品价格
            private boolean checked;//是否勾选 ture 勾选
            private String id;//购物车id
            private String goodsName;//商品名
            private List<String> specifications;//购物车
            private boolean isOnSale;//是否下架   false 已下架

            public boolean isOnSale() {
                return isOnSale;
            }

            public void setOnSale(boolean onSale) {
                isOnSale = onSale;
            }

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public String getAddTime() {
                return addTime;
            }

            public void setAddTime(String addTime) {
                this.addTime = addTime;
            }

            public String getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(String goodsId) {
                this.goodsId = goodsId;
            }

            public String getGoodsSn() {
                return goodsSn;
            }

            public void setGoodsSn(String goodsSn) {
                this.goodsSn = goodsSn;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public boolean isDeleted() {
                return deleted;
            }

            public void setDeleted(boolean deleted) {
                this.deleted = deleted;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public boolean isChecked() {
                return checked;
            }

            public void setChecked(boolean checked) {
                this.checked = checked;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public List<String> getSpecifications() {
                return specifications;
            }

            public void setSpecifications(List<String> specifications) {
                this.specifications = specifications;
            }
        }
}
