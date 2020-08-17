package com.zjzy.morebit.order;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.Activity.MyOrderActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.ShopCarOrderAdapter;
import com.zjzy.morebit.address.ui.ManageGoodsAddressActivity;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.info.ui.fragment.OrderDetailFragment;
import com.zjzy.morebit.main.ui.CollectFragment2;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpActivity;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.order.contract.ConfirmOrderContract;
import com.zjzy.morebit.order.presenter.ConfirmOrderPresenter;
import com.zjzy.morebit.order.ui.NumberOrderDetailActivity;
import com.zjzy.morebit.order.ui.PayOrderSuccessActivity;
import com.zjzy.morebit.payment.PayResult;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.RequestIscheckShopcarBean;
import com.zjzy.morebit.pojo.RequestOrderShopcarBean;
import com.zjzy.morebit.pojo.ShopCarGoodsBean;
import com.zjzy.morebit.pojo.ShopCarOrderBean;
import com.zjzy.morebit.pojo.address.AddressInfo;
import com.zjzy.morebit.pojo.number.GoodsOrderInfo;
import com.zjzy.morebit.pojo.order.OrderSyncResult;
import com.zjzy.morebit.pojo.order.ResponseOrderInfo;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.ViewShowUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

/**
 * 确认订单
 * Created by haiping.liu on 2019-12-10.
 */
public class ConfirmOrderActivity2 extends MvpActivity<ConfirmOrderPresenter> implements View.OnClickListener, ConfirmOrderContract.View  {

    private  final String TAG = ConfirmOrderActivity2.class.getSimpleName();

    @BindView(R.id.txt_head_title)
    TextView headTitle;
    /**
     * 没有收货地址的情况展示
     */
    @BindView(R.id.goods_confirm_order_add_address)
    RelativeLayout rlAddAddress;
    /**
     * 添加地址
     */
    @BindView(R.id.img_add_address)
    ImageView addAddress;



    /**
     * 收货地址
     */
    @BindView(R.id.goods_confirm_order_address)
    CardView GoodsAddress;

    @BindView(R.id.txt_confirm_order_name)
    TextView nameView;

    /**
     * 是否是默认地址
     */
    @BindView(R.id.lb_confirm_order_default)
    TextView defaultFlagView;

    /**
     * 电话
     */
    @BindView(R.id.txt_confirm_order_phone)
    TextView phoneView;

    /**
     * 地址详情
     */
    @BindView(R.id.txt_address_detail)
    TextView addressDetailView;

    @BindView(R.id.ll_address_manage)
    RelativeLayout llAddressManage;
    /**
     * 商品图片
     */
    @BindView(R.id.img_confirm_order_goods)
    ImageView goodsImage;
    /**
     * 商品题目
     */
    @BindView(R.id.txt_confirm_order_goods_title)
    TextView title;
    /**
     * 商品规格
     */
    @BindView(R.id.txt_confirm_order_goods_spec)
    TextView goodsSpec;
    /**
     * 商品价格
     */
    @BindView(R.id.txt_confirm_order_goods_price)
    TextView price;

    /**
     * 商品数量
     */
    @BindView(R.id.txt_confirm_order_goods_count)
    TextView goodsCount;
    /**
     * 商品总价
     */
    @BindView(R.id.txt_confirm_order_pay_price)
    TextView goodsTotalPrice;

    /**
     * 实付金额
     */
    @BindView(R.id.txt_confirm_order_goods_real_pay)
    TextView realPrice;//
    /**
     * 立即支付
     */
    @BindView(R.id.txt_confirm_order_goods_real_pay_action)
    TextView payAction;
    /**
     * 收货地址信息
     */
    private AddressInfo mAddressInfo;
    @BindView(R.id.rcy_goods)
    RecyclerView rcy_goods;
    /**
     * 商品订单信息
     */
    private GoodsOrderInfo mGoodsOrderInfo;
    /**
     * 支付
     */
    private static final int SDK_PAY_FLAG = 1;

    private static final int REQUEST_ADDRESS_CODE =100;

    private String mOrderId;
    private int payStatus;
    private String allPrice;

    private List<ShopCarGoodsBean.CartListBean> cartListBean=new ArrayList<>();
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
                        payStatus =0;
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        Toast.makeText(ConfirmOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        ViewShowUtils.showShortToast(ConfirmOrderActivity2.this,"支付成功");
                    } else {
                        payStatus =1;
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ViewShowUtils.showShortToast(ConfirmOrderActivity2.this,"支付失败");

//                        Toast.makeText(ConfirmOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                    }
                    mPresenter.syncPayResult(ConfirmOrderActivity2.this,mOrderId,payStatus);
                    payAction.setEnabled(true);
                    payAction.setTextColor(Color.parseColor("#FFFFFF"));
                    payAction.setBackgroundResource(R.drawable.bg_confirm_order_buy_corner);
                    Bundle bundle = new Bundle();
                    bundle.putInt(C.Extras.order_type, C.OrderType.YUXUAN);
                    bundle.putInt(C.Extras.YXTYPE,0);
                    OpenFragmentUtils.goToSimpleFragment(ConfirmOrderActivity2.this, OrderDetailFragment.class.getName(), bundle);
                    EventBus.getDefault().post(new MessageEvent(EventBusAction.SHOPCAR_REFRSH));
                    break;
                }
            }

        }
    };





    public static void start(Activity activity, String zong, List<ShopCarGoodsBean.CartListBean> cartListBean) {

        //跳转到确认订单页面
        Intent it = new Intent(activity, ConfirmOrderActivity2.class);
        Bundle bundle = new Bundle();
        bundle.putString(C.Extras.GOODS_ORDER_ZONG,zong);
        bundle.putSerializable(C.Extras.GOODS_ORDER_GOODS, (Serializable) cartListBean);
        it.putExtras(bundle);
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
        //移除所有的handler消息
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_comfirm_order2;
    }



    private void initView(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
        } else {
            ActivityStyleUtil.initSystemBar(this, R.color.color_F8F8F8); //设置标题栏颜色值
        }
        headTitle.setText("确认订单");
        cartListBean = (List<ShopCarGoodsBean.CartListBean>) getIntent().getSerializableExtra(C.Extras.GOODS_ORDER_GOODS);
        allPrice=getIntent().getStringExtra(C.Extras.GOODS_ORDER_ZONG);
        if (cartListBean == null){
            MyLog.e(TAG,"跳转页面的参数为空");
            return;
        }
        if (GoodsAddress.getVisibility() == View.VISIBLE){
            llAddressManage.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    ManageGoodsAddressActivity.addressStart(ConfirmOrderActivity2.this);
                }
            });
        }

        LinearLayoutManager manager=new LinearLayoutManager(this);
        rcy_goods.setLayoutManager(manager);
        ShopCarOrderAdapter shopCarOrderAdapter=new ShopCarOrderAdapter(this);
        shopCarOrderAdapter.addData(cartListBean);
        rcy_goods.setAdapter(shopCarOrderAdapter);
        Log.e("sssss",allPrice+"");
        if (!TextUtils.isEmpty(allPrice)){
            goodsTotalPrice.setText(allPrice+"");
            realPrice.setText(allPrice+"");
        }





    }

    /**
     * 初始化数据
     */
    private void initData(){
        mPresenter.getDefaultAddress(ConfirmOrderActivity2.this);
    }

    @OnClick({R.id.btn_back,R.id.txt_confirm_order_goods_real_pay_action,R.id.goods_confirm_order_address,R.id.goods_confirm_order_add_address})
    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.txt_confirm_order_goods_real_pay_action:

                    if (checkAddress(mAddressInfo) && cartListBean!=null){
                        //创建订单

                        getSubmit();
//                        mPresenter.createOrderForVip(ConfirmOrderActivity2.this,
//                                mAddressInfo.getId(),mGoodsOrderInfo.getGoodsId(),
//                                mGoodsOrderInfo.getCount(),mGoodsOrderInfo.getPrice(),
//                                mGoodsOrderInfo.getPayPrice()
//                                );
                    }

                    break;
                case R.id.goods_confirm_order_address:
                case R.id.goods_confirm_order_add_address:
                    //跳转到收货管理地址页面
                    ManageGoodsAddressActivity.addressStart(ConfirmOrderActivity2.this);
                    break;
                case R.id.btn_back:
                    finish();
                    break;
                default:
                    break;
            }
    }

    private void getSubmit() {//获取订单
        shopCarSubmit(this)
                .subscribe(new DataObserver<ShopCarOrderBean>(false) {
                    @Override
                    protected void onDataListEmpty() {

                    }

                    @Override
                    protected void onDataNull() {


                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {

                    }

                    @Override
                    protected void onSuccess(ShopCarOrderBean data) {
                        if (data!=null){
                            mOrderId=data.getOrderId();
                            final String payMetaData = data.getPayMetaData();
                            Runnable payRunnable = new Runnable() {

                                @Override
                                public void run() {
                                    PayTask alipay = new PayTask(ConfirmOrderActivity2.this);
                                    Map<String, String> result = alipay.payV2(payMetaData, true);
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

                    }
                });
    }


    /**
     * 检查地址
     */
    private boolean checkAddress(AddressInfo info){
        boolean isCheck = true;
        if (mAddressInfo == null
                || (mAddressInfo != null && mAddressInfo.getId() == null)){
            isCheck = false;
            ViewShowUtils.showShortToast(ConfirmOrderActivity2.this, "请确认收货地址信息");
        }

        return isCheck;
    }

    /**
     * 检查商品订单
     */
    private boolean checkGoodsOrder(GoodsOrderInfo info){
        boolean isCheck = true;
        if (info == null){
            isCheck = false;
            ViewShowUtils.showShortToast(ConfirmOrderActivity2.this, "请确认商品信息");
        }

        return isCheck;
    }


    @Override
    public void onCreateOrderSuccess(ResponseOrderInfo orderInfo) {
       final String orderInfoBody =  orderInfo.getBody();
       mOrderId = orderInfo.getOrderId();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ConfirmOrderActivity2.this);
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
    public void onCreateError(String msgError) {
        ViewShowUtils.showShortToast(ConfirmOrderActivity2.this, msgError);
        payAction.setEnabled(true);
        payAction.setTextColor(Color.parseColor("#FFFFFF"));
        payAction.setBackgroundResource(R.drawable.bg_confirm_order_buy_corner);
    }

    @Override
    public void onDefaultAddressSuccess(AddressInfo addressInfo) {

        fillDataForAddress(addressInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADDRESS_CODE ){
            if (data != null){
                Bundle bundle = data.getExtras();
                AddressInfo info = null;
                if (bundle != null){
                    info= (AddressInfo) bundle.get(C.Extras.GOODS_ADDRESS_INFO);
                }
                if (info == null){
                    MyLog.d(TAG,"从管理收货地址页面返回为空");
                }else{
                    fillDataForAddress(info);
                }
            }else{
                mAddressInfo = null;
            }

        }
    }

    /**
     * 填充地址信息
     * @param addressInfo
     */
    private void fillDataForAddress(AddressInfo addressInfo){
        mAddressInfo = addressInfo;
        if (mAddressInfo == null ||
                (mAddressInfo != null && mAddressInfo.getId() == null) ){
            rlAddAddress.setVisibility(View.VISIBLE);
            GoodsAddress.setVisibility(View.GONE);
        }else{
            rlAddAddress.setVisibility(View.GONE);
            GoodsAddress.setVisibility(View.VISIBLE);
            //是否默认地址
            if (mAddressInfo.getIsDefault()==1){
                defaultFlagView.setVisibility(View.VISIBLE);
            }else{
                defaultFlagView.setVisibility(View.GONE);
            }
            //姓名
            nameView.setText(mAddressInfo.getName());
            //
            phoneView.setText(mAddressInfo.getTel());
            String address = mAddressInfo.getProvince()+mAddressInfo.getCity()
                    +mAddressInfo.getDistrict()+mAddressInfo.getDetailAddress();
            addressDetailView.setText(address);
        }

    }


    @Override
    public void onDefaultAddressError() {
        rlAddAddress.setVisibility(View.VISIBLE);
        GoodsAddress.setVisibility(View.GONE);
//        ViewShowUtils.showShortToast(ConfirmOrderActivity.this, "获取收货地址失败，请稍后重试");
    }



    @Override
    public void onSyncPayResultSuccess(OrderSyncResult result) {
        if (result == null){
            MyLog.e(TAG,"同步结果成功，返回结果为空");
            return;
        }
//        if (result.getPayStatus() == 0){
//            PayOrderSuccessActivity.start(ConfirmOrderActivity2.this,result.getOrderId());
//        }else{
//            NumberOrderDetailActivity.startOrderDetailActivity(ConfirmOrderActivity2.this,"",result.getOrderId());
//        }
        finish();
    }

    @Override
    public void onSyncPayResultError() {
        ViewShowUtils.showShortToast(ConfirmOrderActivity2.this,"同步结果失败");
    }


    //勾选购物车
    public Observable<BaseResponse<ShopCarOrderBean>> shopCarSubmit(RxAppCompatActivity fragment) {
        RequestOrderShopcarBean ischeckShopcarBean = new RequestOrderShopcarBean();
        List<String> list=new ArrayList<>();
        for (int i=0;i<cartListBean.size();i++){
            list.add(cartListBean.get(i).getId());
        }
        ischeckShopcarBean.setCartIdList(list);
        if (!TextUtils.isEmpty(String.valueOf(mAddressInfo.getId()))){
            ischeckShopcarBean.setAddressId(String.valueOf(mAddressInfo.getId()));
        }
        return RxHttp.getInstance().getSysteService().getShopCarSubmit(ischeckShopcarBean)
                .compose(RxUtils.<BaseResponse<ShopCarOrderBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<ShopCarOrderBean>>bindToLifecycle());
    }

}
