package com.zjzy.morebit.pojo.goods;

import java.util.List;

/**
 * Created by fengrs on 2018/12/4.
 * 备注:
 */

public class TaobaoSellerBean {

    /**
     * delivery : {"from":"上海"}
     * consumerProtection : {"items":[{"title":"1次破损补寄","desc":"商品在运输途中出现破损的，消费者可向卖家提出补寄申请，可补寄1次，补寄邮费由卖家承担"},{"title":"7天无理由","desc":"满足7天无理由退换货申请的前提下，包邮商品需要买家承担退货邮费，非包邮商品需要买家承担发货和退货邮费。"},{"title":"蚂蚁花呗"},{"title":"信用卡支付"},{"title":"集分宝"}],"passValue":"all"}
     */

    private DeliveryBean delivery;
    private ConsumerProtectionBean consumerProtection;

    public DeliveryBean getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryBean delivery) {
        this.delivery = delivery;
    }

    public ConsumerProtectionBean getConsumerProtection() {
        return consumerProtection;
    }

    public void setConsumerProtection(ConsumerProtectionBean consumerProtection) {
        this.consumerProtection = consumerProtection;
    }

    public static class DeliveryBean {
        /**
         * from : 上海
         */

        private String from="";

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }
    }

    public static class ConsumerProtectionBean {
        /**
         * items : [{"title":"1次破损补寄","desc":"商品在运输途中出现破损的，消费者可向卖家提出补寄申请，可补寄1次，补寄邮费由卖家承担"},{"title":"7天无理由","desc":"满足7天无理由退换货申请的前提下，包邮商品需要买家承担退货邮费，非包邮商品需要买家承担发货和退货邮费。"},{"title":"蚂蚁花呗"},{"title":"信用卡支付"},{"title":"集分宝"}]
         * passValue : all
         */

        private List<ItemsBean> items;


        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            /**
             * title : 1次破损补寄
             * desc : 商品在运输途中出现破损的，消费者可向卖家提出补寄申请，可补寄1次，补寄邮费由卖家承担
             */

            private String title="";
            private String desc="";

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
        }
    }
}
