package com.zjzy.morebit.pojo.goods;

import com.zjzy.morebit.pojo.ShopGoodInfo;

import java.io.Serializable;

/**
 * Created by fengrs on 2018/12/17.
 */

public class TaobaoGoodBean extends ShopGoodInfo implements Serializable {



    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean {
//        private SellerBean seller; // 店铺评价
//        private List<ApiStackBean> apiStack;
        private BannerBean item;


        public BannerBean getItem() {
            return item;
        }
//
//        public void setItem(BannerBean item) {
//            this.item = item;
//        }
//
//        public SellerBean getSeller() {
//            return seller;
//        }
//
//        public void setSeller(SellerBean seller) {
//            this.seller = seller;
//        }
//
//
//        public List<ApiStackBean> getApiStack() {
//            return apiStack;
//        }
//
//        public void setApiStack(List<ApiStackBean> apiStack) {
//            this.apiStack = apiStack;
//        }
//
//
//
//
//
//        public static class ApiStackBean {
//
//            private String name="";
//            private String value="";
//
//            public String getName() {
//                return name;
//            }
//
//            public void setName(String name) {
//                this.name = name;
//            }
//
//            public String getValue() {
//                return value;
//            }
//
//            public void setValue(String value) {
//                this.value = value;
//            }
//        }
//
        public static class BannerBean {

//            public List<String> images;
            private String moduleDescUrl ="";// 详情图片接口
//            private String tmallDescUrl ="";

            public String getModuleDescUrl() {
                return moduleDescUrl;
            }

            public void setModuleDescUrl(String moduleDescUrl) {
                this.moduleDescUrl = moduleDescUrl;
            }
//
//            public String getTmallDescUrl() {
//                return tmallDescUrl;
//            }
//
//            public void setTmallDescUrl(String tmallDescUrl) {
//                this.tmallDescUrl = tmallDescUrl;
//            }
//            public List<String> getImages() {
//                return images;
//            }
//
//            public void setImages(List<String> images) {
//                this.images = images;
//            }
        }
    }
}
