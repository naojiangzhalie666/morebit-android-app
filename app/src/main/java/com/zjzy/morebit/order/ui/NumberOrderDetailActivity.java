package com.zjzy.morebit.order.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.ToastUtils;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.Activity.NumberGoodsDetailsActivity;
import com.zjzy.morebit.LocalData.CommonLocalData;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.fragment.BindZhiFuBaoFragment;
import com.zjzy.morebit.info.ui.fragment.OrderListFragment;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpActivity;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.order.OrderDetailInfo;
import com.zjzy.morebit.pojo.order.OrderSyncResult;
import com.zjzy.morebit.pojo.order.ResponseOrderInfo;
import com.zjzy.morebit.order.contract.OrderDetailContract;
import com.zjzy.morebit.order.presenter.OrderDetailPresenter;

import com.zjzy.morebit.R;
import com.zjzy.morebit.payment.PayResult;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ViewShowUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.BindView;

/**
 * 订单详情页面
 * Created by haiping.liu on 2019-12-13.
 */
public class NumberOrderDetailActivity extends MvpActivity<OrderDetailPresenter> implements OrderDetailContract.View {

    private static final String TAG = NumberOrderDetailActivity.class.getSimpleName();

    @BindView(R.id.btn_back)
    LinearLayout btnBack;

    @BindView(R.id.txt_head_title)
    TextView headTitle;
    /**
     * 订单状态-图片
     */
    @BindView(R.id.img_order_detail)
    ImageView orderDetailView;

    /**
     * 订单状态-文字
     */
    @BindView(R.id.number_order_detail_status)
    TextView orderDetailTxtView;

    @BindView(R.id.tv_daojishi_time)
    TextView tvDaojishiTime;
    /**
     * 物流信息
     */
    @BindView(R.id.order_success_wuliu)
    RelativeLayout orderSuccessWuluView;
    /**
     * 收货地址
     */
    @BindView(R.id.address_user_name)
    TextView addressUserNameView;

    @BindView(R.id.address_phone)
    TextView addressPhoneView;

    @BindView(R.id.address_detail)
    TextView addressDetailView;

    /**
     * 商品信息
     * @return
     */
    @BindView(R.id.img_goods)
    ImageView imgGoodsView;

    @BindView(R.id.goods_title)
    TextView goodsNameView;

    @BindView(R.id.gogds_price)
    TextView goodsPriceView;

    @BindView(R.id.goods_count)
    TextView goodsCountView;

    @BindView(R.id.goods_yugushouyi)
    TextView goodsYugushouyi;

    @BindView(R.id.goods_total_price)
    TextView goodsTotalPriceView;

    //实付金额是否展示
    @BindView(R.id.ll_real_pay_info)
    LinearLayout llRealPayInfo;

    @BindView(R.id.goods_real_price)
    TextView goodsRealPriceView;

    /**
     * 订单信息
     * @return
     */
    /**
     * 订单未支付或者已经关闭
     *
     */
    @BindView(R.id.card_order_close_wait_pay)
    CardView  cardOrderCloseWaitPay;

    @BindView(R.id.order_info_ordersn)
    TextView orderInfoOrderSnView;
    /**
     * 订单号的copy
     */
    @BindView(R.id.order_info_copy)
    TextView orderInfoCopyTv;

    @BindView(R.id.order_create_time)
    TextView orderCreateTimeView;

    @BindView(R.id.order_success_alipay_id)
    TextView orderAlipayId;

    /**
     * 订单完成的布局
     * @return
     */
    @BindView(R.id.card_order_success)
    CardView  cardOrderSuccess;
    /**
     * 订单号
     */
    @BindView(R.id.order_success_info_ordersn)
    TextView orderSuccessInfoOrderSn;
    /**
     * 订单成功的订单号copy
     */
    @BindView(R.id.order_success_info_copy)
    TextView orderSuccessInfoCopyTv;

    @BindView(R.id.order_success_create_time)
    TextView  orderSuccessCreateTime;

    @BindView(R.id.order_success_pay_time)
    TextView orderSuccessPayTime;

    @BindView(R.id.order_success_fahuo_time)
    TextView orderSuccessDeliveryTime;

    @BindView(R.id.order_success_chengjiao_time)
    TextView orderSuccessDealTimeView;

    /**
     * 去支付。底部取消、去支付
     * @return
     */
    @BindView(R.id.order_cancel_repay)
    LinearLayout llOrderCancelRePay;

    @BindView(R.id.total_pay_price_topay)
    TextView totalPriceToPayView;

    @BindView(R.id.btn_cancel_order)
    TextView btnCancelOrder;

    @BindView(R.id.btn_to_pay)
    TextView btnToPay;

    /**
     * 已关闭订单，完成
     */
    @BindView(R.id.order_finish_rebuy)
    LinearLayout orderFinishRebuyLlView;

    /**
     * 重新购买
     * @return
     */
    @BindView(R.id.btn_re_buy)
    TextView btnReBuyView;
    /**
     * 支付合计
     */
    @BindView(R.id.total_pay_price)
    TextView btnTotalPayPrice;

    @BindView(R.id.btn_confirm_goods)
    TextView btnConfirmGoods;

    private String mOrderId;

    /**
     * 支付
     */
    private static final int SDK_PAY_FLAG = 1;

    private int payStatus =0;

    //private Counter mTimer;
    private String isOnSale;


    private OrderDetailInfo mInfo;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        payStatus = 0;
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        Toast.makeText(ConfirmOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();


                    } else {
                        payStatus = 1;
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        Toast.makeText(ConfirmOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    mPresenter.syncPayResult(NumberOrderDetailActivity.this,mOrderId,payStatus);
                    break;
                }
            }

        }
    };


    public static void  startOrderDetailActivity(Activity activity,String isOnSale,String orderId){
        //跳转到确认订单页面
        Intent it = new Intent(activity, NumberOrderDetailActivity.class);
        it.putExtra("orderId",orderId);
        it.putExtra("isOnSale",isOnSale);
        activity.startActivity(it);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        //todo 隐藏倒计时功能
        /**if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }*/
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_number_order_detail;
    }

    @Override
    public void onCancelOrderSuccess() {
        ViewShowUtils.showShortToast(NumberOrderDetailActivity.this,"取消订单成功！");
        finish();
    }

    @Override
    public void onCancelOrderError() {
        ViewShowUtils.showShortToast(NumberOrderDetailActivity.this,"取消订单失败！");
    }

    @Override
    public void onRePaySuccess(ResponseOrderInfo orderInfo) {
        final String orderInfoBody =  orderInfo.getBody();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(NumberOrderDetailActivity.this);
                Map<String, String> result = alipay.payV2(orderInfoBody, true);
                MyLog.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    @Override
    public void onRePayError() {
        ViewShowUtils.showShortToast(NumberOrderDetailActivity.this,"支付失败！");
    }

    @Override
    public void onOrderDetailSuccess(OrderDetailInfo info) {
        if (info == null) {
            MyLog.e(TAG,"订单详情信息为空");
            return ;
        }
        mInfo = info;
            int status = info.getOrderStatus();
            switch (status){
                case C.OrderStatus.WAIT_PAY_ORDER_STATUS:
                    initOrderStatusUnPay(info);
                    break;
                case C.OrderStatus.SUCCESS_ORDER_STATUS:
                    initOrderStatusSuccess(info);
                    break;
                case C.OrderStatus.SETTLE_ORDER_STATUS:
                    initOrderStatusSettle(info);
                    break;

                case C.OrderStatus.CLOSE_ORDER_STATUS:
                    initOrderStatusClose(info);
                    break;
                case C.OrderStatus.RECEIVE_GOODS_ORDER_STATUS:
                    initOrderStatusReceiveGoods(info);
                default:
                    break;

            }
            //公共部分
        //收货地址
        addressUserNameView.setText(info.getName());
        addressPhoneView.setText(info.getMobile());
        addressDetailView.setText(info.getAddressDetail());


        //商品信息
        LoadImgUtils.setImg(NumberOrderDetailActivity.this,imgGoodsView,info.getGoodsUrl());
        goodsNameView.setText(info.getDetail());
        double goodsPrice = info.getGoodsPrice();
        int number = info.getNumber();
        goodsPriceView.setText(getResources().getString(R.string.number_goods_price,
                String.valueOf(goodsPrice)));
        goodsCountView.setText(getResources().getString(R.string.number_goods_count,
                String.valueOf(number)));

        double totalPrice = goodsPrice*number;
        goodsTotalPriceView.setText(getResources().getString(R.string.number_goods_price,
                String.valueOf(totalPrice)));



    }

    /**
     * 待收货状态
     * @param info
     */
    private void initOrderStatusReceiveGoods(OrderDetailInfo info){
        tvDaojishiTime.setVisibility(View.GONE);
        orderDetailView.setImageResource(R.mipmap.order_success);

        orderDetailTxtView.setText("待收货");


        llRealPayInfo.setVisibility(View.VISIBLE);
        goodsRealPriceView.setText(getResources().getString(R.string.number_goods_price,
                String.valueOf(info.getActualPrice())));

        //物流信息
        orderSuccessWuluView.setVisibility(View.VISIBLE);
        String shipUrl = info.getShipUrl();
        final String shipUrlWithParm = shipUrl+"?orderId="+info.getOrderId();
        orderSuccessWuluView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(shipUrlWithParm)){
                    if (TextUtils.isEmpty(shipUrlWithParm)) return;
                    ShowWebActivity.start(NumberOrderDetailActivity.this,shipUrlWithParm,"物流信息");
                }
            }
        });

        //订单信息
        cardOrderCloseWaitPay.setVisibility(View.GONE);
        cardOrderSuccess.setVisibility(View.VISIBLE);
        orderSuccessInfoOrderSn.setText(getResources().getString(R.string.number_order_number,
                info.getOrderId()));
        final String orderId = info.getOrderId();

        orderSuccessInfoCopyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.coayText(NumberOrderDetailActivity.this,orderId);
                ViewShowUtils.showShortToast(NumberOrderDetailActivity.this,getResources().getString(R.string.coayTextSucceed));
            }
        });

        //支付宝交易号
        orderAlipayId.setText(getResources().getString(R.string.number_alipay_order_number,info.getPayId()));
        //创建时间
        orderSuccessCreateTime.setText(getResources().getString(R.string.number_order_create_time,
                info.getCreateTime()));
        //支付时间
        String payTime = info.getPayTime();
        if (TextUtils.isEmpty(payTime)){
            orderSuccessPayTime.setText("");
        }else{
            orderSuccessPayTime.setText(payTime);
        }

        //发货时间
        String deliveryTime = info.getShipTime();
        if (TextUtils.isEmpty(deliveryTime)){
            orderSuccessDeliveryTime.setText("");
        }else{
            orderSuccessDeliveryTime.setText(deliveryTime);
        }
        //确认收货：
        btnConfirmGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确认收货
                mPresenter.ConfirmReceiveGoods(NumberOrderDetailActivity.this,orderId);
            }
        });
        orderSuccessDealTimeView.setVisibility(View.GONE);

        llOrderCancelRePay.setVisibility(View.GONE);
        orderFinishRebuyLlView.setVisibility(View.GONE);

    }

    /**
     * 已结算
     * @param info
     */
    private void initOrderStatusSettle(OrderDetailInfo info){
        tvDaojishiTime.setVisibility(View.GONE);
        orderDetailView.setImageResource(R.mipmap.order_success);

        orderDetailTxtView.setText("已结算");


        llRealPayInfo.setVisibility(View.VISIBLE);
        goodsRealPriceView.setText(getResources().getString(R.string.number_goods_price,
                String.valueOf(info.getActualPrice())));

        //物流信息
        orderSuccessWuluView.setVisibility(View.VISIBLE);
        String shipUrl = info.getShipUrl();
        final String shipUrlWithParm = shipUrl+"?orderId="+info.getOrderId();
        orderSuccessWuluView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(shipUrlWithParm)){
                    if (TextUtils.isEmpty(shipUrlWithParm)) return;
                    ShowWebActivity.start(NumberOrderDetailActivity.this,shipUrlWithParm,"物流信息");
                }
            }
        });

        //订单信息
        cardOrderCloseWaitPay.setVisibility(View.GONE);
        cardOrderSuccess.setVisibility(View.VISIBLE);
        orderSuccessInfoOrderSn.setText(getResources().getString(R.string.number_order_number,
                info.getOrderId()));
        final String orderId = info.getOrderId();

        orderSuccessInfoCopyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.coayText(NumberOrderDetailActivity.this,orderId);
                ViewShowUtils.showShortToast(NumberOrderDetailActivity.this,getResources().getString(R.string.coayTextSucceed));
            }
        });


        //支付宝交易号
        orderAlipayId.setText(getResources().getString(R.string.number_alipay_order_number,info.getPayId()));

        //创建时间
        orderSuccessCreateTime.setText(getResources().getString(R.string.number_order_create_time,
                info.getCreateTime()));

        //支付时间
        String payTime = info.getPayTime();
        if (TextUtils.isEmpty(payTime)){
            orderSuccessPayTime.setText("");
        }else{
            orderSuccessPayTime.setText(getResources().getString(R.string.number_order_pay_time,payTime));
        }

        //发货时间
        String deliveryTime = info.getShipTime();
        if (TextUtils.isEmpty(deliveryTime)){
            orderSuccessDeliveryTime.setText("");
        }else{
            orderSuccessDeliveryTime.setText(deliveryTime);
        }

        //成交时间：

        orderSuccessDealTimeView.setVisibility(View.VISIBLE);
        String dealTime = info.getFinalTime();
        if (TextUtils.isEmpty(dealTime)){
            orderSuccessDealTimeView.setText("");
        }else{
            orderSuccessDealTimeView.setText(dealTime);
        }


        llOrderCancelRePay.setVisibility(View.GONE);
        orderFinishRebuyLlView.setVisibility(View.GONE);

    }

    private void initOrderStatusSuccess(OrderDetailInfo info){
        tvDaojishiTime.setVisibility(View.GONE);
        orderDetailView.setImageResource(R.mipmap.order_success);

        orderDetailTxtView.setText("已支付");


        llRealPayInfo.setVisibility(View.VISIBLE);
        goodsRealPriceView.setText(getResources().getString(R.string.number_goods_price,
                String.valueOf(info.getActualPrice())));

        //物流信息
        orderSuccessWuluView.setVisibility(View.VISIBLE);
         String shipUrl = info.getShipUrl();
        final String shipUrlWithParm = shipUrl+"?orderId="+info.getOrderId();
        orderSuccessWuluView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(shipUrlWithParm)){
                    if (TextUtils.isEmpty(shipUrlWithParm)) return;
                    ShowWebActivity.start(NumberOrderDetailActivity.this,shipUrlWithParm,"物流信息");
                }
            }
        });

        //订单信息
        cardOrderCloseWaitPay.setVisibility(View.GONE);
        cardOrderSuccess.setVisibility(View.VISIBLE);
        orderSuccessInfoOrderSn.setText(getResources().getString(R.string.number_order_number,
                info.getOrderId()));
        final String orderId = info.getOrderId();

        orderSuccessInfoCopyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.coayText(NumberOrderDetailActivity.this,orderId);
                ViewShowUtils.showShortToast(NumberOrderDetailActivity.this,getResources().getString(R.string.coayTextSucceed));
            }
        });
        //支付宝交易号
        orderAlipayId.setText(getResources().getString(R.string.number_alipay_order_number,info.getPayId()));
        //创建时间
        orderSuccessCreateTime.setText(getResources().getString(R.string.number_order_create_time,
                info.getCreateTime()));

        String payTime = info.getPayTime();
        if (TextUtils.isEmpty(payTime)){
            orderSuccessPayTime.setText("");
        }else{
            orderSuccessPayTime.setText(getResources().getString(R.string.number_order_pay_time,payTime));
        }

        orderSuccessDeliveryTime.setVisibility(View.GONE);
        orderSuccessDealTimeView.setVisibility(View.GONE);
//        String deliveryTime = info.getShipTime();
//        if (TextUtils.isEmpty(deliveryTime)){
//            orderSuccessDeliveryTime.setText("");
//        }else{
//            orderSuccessDeliveryTime.setText(deliveryTime);
//        }
//
//        orderSuccessDealTimeView.setVisibility(View.VISIBLE);
//        String dealTime = info.getFinalTime();
//        if (TextUtils.isEmpty(dealTime)){
//            orderSuccessDealTimeView.setText("");
//        }else{
//            orderSuccessDealTimeView.setText(dealTime);
//        }


        llOrderCancelRePay.setVisibility(View.GONE);
        orderFinishRebuyLlView.setVisibility(View.GONE);

    }
    private void initOrderStatusClose(final OrderDetailInfo info){
        tvDaojishiTime.setVisibility(View.GONE);
        orderDetailView.setImageResource(R.mipmap.order_finish);
        orderDetailTxtView.setText("已关闭");
        //物流信息
        orderSuccessWuluView.setVisibility(View.GONE);
        //商品信息的实付信息
        llRealPayInfo.setVisibility(View.GONE);

        cardOrderCloseWaitPay.setVisibility(View.VISIBLE);
        cardOrderSuccess.setVisibility(View.GONE);
        orderInfoOrderSnView.setText(getResources().getString(R.string.number_order_number,info.getOrderId()));
        orderCreateTimeView.setText(getResources().getString(R.string.number_order_create_time,info.getCreateTime()));

        llOrderCancelRePay.setVisibility(View.GONE);
        orderFinishRebuyLlView.setVisibility(View.VISIBLE);
        btnTotalPayPrice.setText(getResources().getString(R.string.number_order_total_price,
                String.valueOf(info.getActualPrice())));
        final String goodsId = info.getGoodsId();
        btnReBuyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo:跳转到商品详情页面
                if (isOnSale.equals("true")){
                    if (!TextUtils.isEmpty(goodsId)){
                        NumberGoodsDetailsActivity.start(NumberOrderDetailActivity.this,goodsId);
                    }else{
                        MyLog.d(TAG,"商品Id为空");
                    }
                }else{
                    ToastUtils.showLong("商品已下架");
                }



            }
        });

    }
    private void initOrderStatusUnPay(OrderDetailInfo info){
        tvDaojishiTime.setVisibility(View.GONE);
        orderDetailView.setImageResource(R.mipmap.order_wait_pay);
        orderDetailTxtView.setText("待付款");
        //物流信息
        orderSuccessWuluView.setVisibility(View.GONE);
        //商品信息的实付信息
        llRealPayInfo.setVisibility(View.GONE);

        //订单信息
        cardOrderCloseWaitPay.setVisibility(View.VISIBLE);
        cardOrderSuccess.setVisibility(View.GONE);
        final String orderId = info.getOrderId();
        orderInfoOrderSnView.setText(getResources().getString(R.string.number_order_number,orderId));

        orderInfoCopyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.coayText(NumberOrderDetailActivity.this,orderId);
                ViewShowUtils.showShortToast(NumberOrderDetailActivity.this,getResources().getString(R.string.coayTextSucceed));
            }
        });
        orderCreateTimeView.setText(getResources().getString(R.string.number_order_create_time,
                DateTimeUtils.getYmdhhmmss(info.getCreateTime())));

        llOrderCancelRePay.setVisibility(View.VISIBLE);
        orderFinishRebuyLlView.setVisibility(View.GONE);

        totalPriceToPayView.setText(getResources().getString(R.string.number_order_total_price,
                String.valueOf(info.getActualPrice())));

        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //todo:取消订单
                mPresenter.cancelOrder(NumberOrderDetailActivity.this,mOrderId);
            }
        });

        btnToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo:再次支付
                mPresenter.payForOrder(NumberOrderDetailActivity.this,mOrderId);
            }
        });
        //todo： 隐藏倒计时功能
        tvDaojishiTime.setVisibility(View.GONE);
      /**  String createTime = info.getCreateTime();
        if (isNumeric(createTime)){


            long orderTime = Long.parseLong(createTime);
            MyLog.d("test","订单创建时间:"+DateTimeUtils.getYmdhhmmss(String.valueOf(orderTime)));
            long time = CommonLocalData.syncServiceTime() - orderTime;
            if (time/1000 < 1800){
                tvDaojishiTime.setVisibility(View.VISIBLE);
                Counter counter = new Counter(1800*1000-time, 1000);
                counter.start();
            }
            else{
                tvDaojishiTime.setVisibility(View.GONE);
                MyLog.d("test","订单与客户端本地时间差异:"+time+"毫秒");
            }
        }*/

//        Counter counter = new Counter(60 * 1000, 1000); // 第一个参数是倒计时时间，后者为计时间隔，单位毫秒，这里是倒计时
//        counter.start();

    }

    @Override
    public void onOrderDetailError() {
        MyLog.e(MyLog.TAG,"获取订单详情失败");
    }

    @Override
    public void onSyncPayResultSuccess(OrderSyncResult result) {
        if (result == null){
            MyLog.e(TAG,"支付同步成功，结果为空");
        }
        if (result.getPayStatus() == 0){
            ViewShowUtils.showShortToast(NumberOrderDetailActivity.this,"支付成功");
            PayOrderSuccessActivity.start(NumberOrderDetailActivity.this,mOrderId);
            finish();
        }else{

            ViewShowUtils.showShortToast(NumberOrderDetailActivity.this,"支付失败");
        }
    }

    @Override
    public void onSyncPayResultError() {
        ViewShowUtils.showShortToast(NumberOrderDetailActivity.this,"支付同步结果失败");
    }

    @Override
    public void onReceiveGoodsSuccessFul(Boolean data) {
        ViewShowUtils.showShortToast(this,"确认收货成功！");
    }

    private void initView(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
        } else {
            ActivityStyleUtil.initSystemBar(this, R.color.color_F8F8F8); //设置标题栏颜色值
        }
        mOrderId = getIntent().getStringExtra("orderId");
          isOnSale = getIntent().getStringExtra("isOnSale");
        headTitle.setText("订单详情");
        //返回按钮
        btnBack.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
                EventBus.getDefault().post(new MessageEvent(EventBusAction.SHOPCAR_REFRSH));
            }
        });


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            EventBus.getDefault().post(new MessageEvent(EventBusAction.SHOPCAR_REFRSH));
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

    private void initData(){
        mPresenter.getOrderDetail(NumberOrderDetailActivity.this,mOrderId);
    }
    //todo 隐藏倒计时功能
    /**class Counter extends CountDownTimer {

        public Counter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            try {
                initOrderStatusClose(mInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if(!NumberOrderDetailActivity.this.isFinishing()){
                // TODO
                String dataTime = DateTimeUtils.getHHmmss(millisUntilFinished);
                tvDaojishiTime.setText("剩余 "+dataTime);
            }


        }

    }*/
}
