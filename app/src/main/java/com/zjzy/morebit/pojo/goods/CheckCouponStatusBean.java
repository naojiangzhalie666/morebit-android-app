package com.zjzy.morebit.pojo.goods;

public   class CheckCouponStatusBean {
        /**
         * cookie : cookie1
         * url : www.coupon.com
         */

        private String cookie;//请求cookie值
        private String url;//请求路径
        private int valideCode;

    public int getValideCode() {
        return valideCode;
    }

    public void setValideCode(int valideCode) {
        this.valideCode = valideCode;
    }

    public String getCookie() {
            return cookie;
        }

        public void setCookie(String cookie) {
            this.cookie = cookie;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }