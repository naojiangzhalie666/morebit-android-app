package com.markermall.cat.pojo.goods;

import com.markermall.cat.pojo.ShopGoodInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fengrs on 2018/12/17.
 */

public class TaobaoGoodImgBean extends ShopGoodInfo implements Serializable {


    /**
     * sellerId : 2765120816
     * wdescContent : {"pages":["<img size=790x1937>https://img.alicdn.com/imgextra/i1/2765120816/O1CN011HtledE7c3bDKhz_!!2765120816.jpg<\/img>","<img size=790x1936>https://img.alicdn.com/imgextra/i1/2765120816/O1CN011HtleeOQWz1DQGo_!!2765120816.jpg<\/img>","<img size=790x1937>https://img.alicdn.com/imgextra/i2/2765120816/O1CN011HtlebgJnwLPKZc_!!2765120816.jpg<\/img>","<img size=790x1937>https://img.alicdn.com/imgextra/i4/2765120816/O1CN011HtledS1w5PATru_!!2765120816.jpg<\/img>","<img size=790x1936>https://img.alicdn.com/imgextra/i2/2765120816/O1CN011Htlec7xx4Eqmw6_!!2765120816.jpg<\/img>","<img size=790x1937>https://img.alicdn.com/imgextra/i1/2765120816/O1CN011Htledk0j3UrfMd_!!2765120816.jpg<\/img>"]}
     */

    private WdescContentBean wdescContent;



    public WdescContentBean getWdescContent() {
        return wdescContent;
    }

    public void setWdescContent(WdescContentBean wdescContent) {
        this.wdescContent = wdescContent;
    }

    public static class WdescContentBean {
        private List<String> pages;

        public List<String> getPages() {
            return pages;
        }

        public void setPages(List<String> pages) {
            this.pages = pages;
        }
    }
}
