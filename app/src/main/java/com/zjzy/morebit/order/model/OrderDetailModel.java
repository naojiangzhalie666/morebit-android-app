package com.zjzy.morebit.order.model;


import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.address.AddressInfo;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.order.ResponseOrderInfo;
import com.zjzy.morebit.pojo.request.RequestCreateOrderBean;
import com.zjzy.morebit.pojo.request.RequestOrderDetailBean;

import io.reactivex.Observable;

/**
 * 订单详情
 * Data: 2018/12/15.
 * 会员商品详情 model
 */
public class OrderDetailModel extends MvpModel {
    /**
     * 取消订单
     * @param rxActivity
     * @param orderId
     * @return
     */
    public Observable<BaseResponse<Integer>> cancelOrder(BaseActivity rxActivity,
                                                                         String orderId){
        RequestOrderDetailBean bean = new RequestOrderDetailBean();
        bean.setOrderId(orderId);
        return RxHttp.getInstance().getCommonService().cancelOrder(bean)
                .compose(RxUtils.<BaseResponse<Integer>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<Integer>>bindToLifecycle());
    }

    /**
     * 去支付商品订单
     * @param rxActivity
     * @param orderId
     * @return
     */
    public Observable<BaseResponse<ResponseOrderInfo>> rePayByOrderId(BaseActivity rxActivity,
                                                                      String orderId
    ) {
        RequestOrderDetailBean bean = new RequestOrderDetailBean();
        bean.setOrderId(orderId);

        return RxHttp.getInstance().getCommonService().rePay(bean)
                .compose(RxUtils.<BaseResponse<ResponseOrderInfo>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<ResponseOrderInfo>>bindToLifecycle());
    }




}