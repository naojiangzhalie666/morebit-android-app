package com.zjzy.morebit.order.model;


import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.order.OrderDetailInfo;
import com.zjzy.morebit.pojo.order.OrderSyncResult;
import com.zjzy.morebit.pojo.order.ResponseOrderInfo;
import com.zjzy.morebit.pojo.request.RequestOrderDetailBean;
import com.zjzy.morebit.pojo.request.RequestSyncPayResultResultBean;

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
    public Observable<BaseResponse<Boolean>> cancelOrder(BaseActivity rxActivity,
                                                                         String orderId){
        RequestOrderDetailBean bean = new RequestOrderDetailBean();
        bean.setOrderId(orderId);
        return RxHttp.getInstance().getCommonService().cancelOrder(bean)
                .compose(RxUtils.<BaseResponse<Boolean>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<Boolean>>bindToLifecycle());
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


    /**
     * 订单详情
     * @param rxActivity
     * @param orderId
     * @return
     */
    public Observable<BaseResponse<OrderDetailInfo>> getOrderDetail(BaseActivity rxActivity,
                                                                    String orderId
    ) {
        RequestOrderDetailBean bean = new RequestOrderDetailBean();
        bean.setOrderId(orderId);

        return RxHttp.getInstance().getCommonService().getOrderDetail(bean)
                .compose(RxUtils.<BaseResponse<OrderDetailInfo>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<OrderDetailInfo>>bindToLifecycle());
    }

    /**
     * 同步支付结果
     * @param rxActivity
     * @param orderId
     * @param payStatus
     * @return
     */
    public Observable<BaseResponse<OrderSyncResult>> syncPayStatus(BaseActivity rxActivity, String orderId,
                                                                   int payStatus

    ) {
        RequestSyncPayResultResultBean requestBean = new RequestSyncPayResultResultBean();
        requestBean.setOrderId(orderId);
        requestBean.setPayStatus(payStatus);
        return RxHttp.getInstance().getCommonService().syncPayResult(requestBean)
                .compose(RxUtils.<BaseResponse<OrderSyncResult>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<OrderSyncResult>>bindToLifecycle());
    }

    /**
     * 确认收货
     * @param rxFragment
     * @param OrderId
     * @return
     */
    public Observable<BaseResponse<Boolean>> confirmOrder(BaseActivity rxFragment, String OrderId) {
        RequestOrderDetailBean bean = new RequestOrderDetailBean();
        bean.setOrderId(OrderId);
        return RxHttp.getInstance().getCommonService().confirmOrder(bean)
                .compose(RxUtils.<BaseResponse<Boolean>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<Boolean>>bindToLifecycle());
    }



}