package com.zjzy.morebit.pojo.requestbodybean;

import com.zjzy.morebit.pojo.request.RequestBaseBean;
import com.zjzy.morebit.pojo.request.RequestShareGoodsMoreUrlBean;

import java.util.List;

/**
 * Created by JerryHo on 2019/1/7
 * Description: 请求分享物品链接,二维码bean
 */
public class RequestShareMoreGoods extends RequestBaseBean {

    private List<RequestShareGoodsMoreUrlBean> itemShareVo;

    public List<RequestShareGoodsMoreUrlBean> getItemShareVo() {
        return itemShareVo;
    }

    public void setItemShareVo(List<RequestShareGoodsMoreUrlBean> itemShareVo) {
        this.itemShareVo = itemShareVo;
    }
}
