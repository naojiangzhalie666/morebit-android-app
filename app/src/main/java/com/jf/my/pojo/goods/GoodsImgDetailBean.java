package com.jf.my.pojo.goods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengrs on 2018/12/24.
 * 备注:
 */

public class GoodsImgDetailBean implements Serializable {

    /**
     * a : {"content":"https://mdetail.tmall.com/templates/pages/desc?id=584171767246","type":2}
     * b : {"content":"https://hws.m.taobao.com/cache/desc/5.0?id=584171767246","type":1}
     * c : {"content":"http://img.alicdn.com/imgextra/i4/100503319/O1CN01791j281aO91ka5Pms_!!0-item_pic.jpg,http://img.alicdn.com/imgextra/i8/TB19rGqJVXXXXbiXVXXYXGcGpXX_M2.SS2,http://img.alicdn.com/imgextra/i3/100503319/TB2j9e_lhtnkeRjSZSgXXXAuXXa_!!100503319.jpg,http://img.alicdn.com/imgextra/i1/100503319/O1CN011aO912ZW56T553X_!!100503319.jpg,http://img.alicdn.com/imgextra/i3/100503319/TB2jXKPjUdnpuFjSZPhXXbChpXa_!!100503319.jpg","type":0}
     */

    private Bean a;
    private Bean b;
    private Bean c;

    public Bean getA() {
        return a;
    }

    public void setA(Bean a) {
        this.a = a;
    }

    public Bean getB() {
        return b;
    }

    public void setB(Bean b) {
        this.b = b;
    }

    public Bean getC() {
        return c;
    }

    public void setC(Bean c) {
        this.c = c;
    }

    public static class Bean implements Serializable {
        /**
         * content : https://mdetail.tmall.com/templates/pages/desc?id=584171767246
         * type : 2
         */

        private List<String> content;//(商品详情图片,淘宝json地址,h5链接地址)
        private int type;//0 商品详情图片,1 花生json,2 h5地址

        public Bean(ArrayList<String> arrayList) {
            content =arrayList;
        }

        public List<String> getContent() {
            return content;
        }

        public void setContent(List<String> content) {
            this.content = content;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

    }
}
