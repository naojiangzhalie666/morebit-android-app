package com.zjzy.morebit.pojo;

import java.io.Serializable;

public class CommonShareTemplateBean implements Serializable {


        /**
         * shareContent : 【四合院儿卷纸无芯家用可湿水抽纸巾母婴实惠装酒店卫生纸原生木浆】
         【在售价】44.99
         【券后价】29.99
         【免费下载多点优选下单】再返还￥5.39
         【下载链接】https://a.app.qq.com/o/simple.jsp?pkgname=com.zjzy.morebit
         * tklVo : {"tkl":"(UIKV1xqiv04)","couponUrl":"https://w.url.cn/s/AN0FGBC","template":"\n【在售价】44.99元\n【券后价】29.99元","temp":"【{标题}】\n【在售价】{商品原价}元\n【券后价】{券后价}元","isShortLink":2}
         */

        private String shareContent;
        private TklVoBean tklVo;

        public String getShareContent() {
            return shareContent;
        }

        public void setShareContent(String shareContent) {
            this.shareContent = shareContent;
        }

        public TklVoBean getTklVo() {
            return tklVo;
        }

        public void setTklVo(TklVoBean tklVo) {
            this.tklVo = tklVo;
        }

        public static class TklVoBean {
            /**
             * tkl : (UIKV1xqiv04)
             * couponUrl : https://w.url.cn/s/AN0FGBC
             * template :
             【在售价】44.99元
             【券后价】29.99元
             * temp : 【{标题}】
             【在售价】{商品原价}元
             【券后价】{券后价}元
             * isShortLink : 2
             */

            private String tkl;
            private String couponUrl;
            private String template;
            private String temp;
            private int isShortLink;

            public String getTkl() {
                return tkl;
            }

            public void setTkl(String tkl) {
                this.tkl = tkl;
            }

            public String getCouponUrl() {
                return couponUrl;
            }

            public void setCouponUrl(String couponUrl) {
                this.couponUrl = couponUrl;
            }

            public String getTemplate() {
                return template;
            }

            public void setTemplate(String template) {
                this.template = template;
            }

            public String getTemp() {
                return temp;
            }

            public void setTemp(String temp) {
                this.temp = temp;
            }

            public int getIsShortLink() {
                return isShortLink;
            }

            public void setIsShortLink(int isShortLink) {
                this.isShortLink = isShortLink;
            }
        }
    }

