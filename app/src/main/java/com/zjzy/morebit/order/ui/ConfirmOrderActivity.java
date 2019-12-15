package com.zjzy.morebit.order.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.zjzy.morebit.address.ui.ManageGoodsAddressActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.address.AddressInfo;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpActivity;
import com.zjzy.morebit.order.ResponseOrderInfo;
import com.zjzy.morebit.order.contract.ConfirmOrderContract;
import com.zjzy.morebit.order.presenter.ConfirmOrderPresenter;
import com.zjzy.morebit.payment.PayResult;
import com.zjzy.morebit.pojo.number.GoodsOrderInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ViewShowUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 确认订单
 * Created by haiping.liu on 2019-12-10.
 */
public class ConfirmOrderActivity extends MvpActivity<ConfirmOrderPresenter> implements View.OnClickListener, ConfirmOrderContract.View  {

    private  final String TAG = ConfirmOrderActivity.class.getSimpleName();

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
    LinearLayout llAddressManage;
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
    TextView realPrice;
    /**
     * 立即支付
     */
    @BindView(R.id.txt_confirm_order_goods_real_pay_action)
    TextView payAction;
    /**
     * 收货地址信息
     */
    private AddressInfo mAddressInfo;
    /**
     * 商品订单信息
     */
    private GoodsOrderInfo mGoodsOrderInfo;
    /**
     * 支付
     */
    private static final int SDK_PAY_FLAG = 1;

    private static final int REQUEST_ADDRESS_CODE =100;

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
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(ConfirmOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(ConfirmOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }

        }
    };


    public static void start(Activity activity, GoodsOrderInfo info) {
        if (info == null) {
            return;
        }
        //跳转到确认订单页面
        Intent it = new Intent(activity, ConfirmOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(C.Extras.GOODS_ORDER_INFO,info);
        it.putExtras(bundle);
        activity.startActivity(it);
    }

    /**
     * 收货地址管理页面
     * @param context
     */
    public static void addressStart(Activity context){
        Intent intent = new Intent(context, ManageGoodsAddressActivity.class);
        context.startActivityForResult(intent, REQUEST_ADDRESS_CODE);
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
    protected int getViewLayout() {
        return R.layout.activity_comfirm_order;
    }



    private void initView(){
        headTitle.setText("确认订单");
        mGoodsOrderInfo = (GoodsOrderInfo) getIntent().getSerializableExtra(C.Extras.GOODS_ORDER_INFO);
        if (mGoodsOrderInfo == null){
            MyLog.e(TAG,"跳转页面的参数为空");
            return;
        }
        if (GoodsAddress.getVisibility() == View.VISIBLE){
            llAddressManage.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    ManageGoodsAddressActivity.start(ConfirmOrderActivity.this);
                }
            });
        }
        LoadImgUtils.setImg(ConfirmOrderActivity.this, goodsImage, mGoodsOrderInfo.getImage());
        title.setText(mGoodsOrderInfo.getTitle());
        goodsSpec.setText(mGoodsOrderInfo.getSpec());
        goodsCount.setText(getResources().getString(R.string.number_goods_count,String.valueOf(mGoodsOrderInfo.getCount())));
        goodsTotalPrice.setText(getResources().getString(R.string.number_goods_price,mGoodsOrderInfo.getGoodsTotalPrice()));
        realPrice.setText(getResources().getString(R.string.number_goods_price,mGoodsOrderInfo.getPayPrice()));
        price.setText(getResources().getString(R.string.number_goods_price,mGoodsOrderInfo.getPrice()));

    }

    /**
     * 初始化数据
     */
    private void initData(){
        mPresenter.getDefaultAddress(ConfirmOrderActivity.this);
    }

    @OnClick({R.id.txt_confirm_order_goods_real_pay_action,R.id.img_add_address})
    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.txt_confirm_order_goods_real_pay_action:

                    if (checkAddress(mAddressInfo) && checkGoodsOrder(mGoodsOrderInfo)){
                        //创建订单
                        mPresenter.createOrderForVip(ConfirmOrderActivity.this,
                                mAddressInfo.getId(),mGoodsOrderInfo.getGoodsId(),
                                mGoodsOrderInfo.getCount(),mGoodsOrderInfo.getGoodsTotalPrice(),
                                mGoodsOrderInfo.getPayPrice()
                                );
                    }

                    break;
                case R.id.img_add_address:
                    //跳转到收货管理地址页面
                    addressStart(ConfirmOrderActivity.this);
                    break;

                default:
                    break;
            }
    }

    /**
     * 检查地址
     */
    private boolean checkAddress(AddressInfo info){
        boolean isCheck = true;
        if (mAddressInfo == null){
            isCheck = false;
            ViewShowUtils.showShortToast(ConfirmOrderActivity.this, "请确认收货地址信息");
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
            ViewShowUtils.showShortToast(ConfirmOrderActivity.this, "请确认商品信息");
        }

        return isCheck;
    }


    @Override
    public void onCreateOrderSuccess(ResponseOrderInfo orderInfo) {
       final String orderInfoBody =  orderInfo.getBody();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ConfirmOrderActivity.this);
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
    public void onCreateError() {
        ViewShowUtils.showShortToast(ConfirmOrderActivity.this, "支付失败，请稍后重试");
    }

    @Override
    public void onDefaultAddressSuccess(AddressInfo addressInfo) {

        fillDataForAddress(addressInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADDRESS_CODE ){
            AddressInfo info = (AddressInfo) getIntent().getSerializableExtra(C.Extras.GOODS_ADDRESS_INFO);
            if (info == null){
                MyLog.d(TAG,"从管理收货地址页面返回为空");
            }else{
                fillDataForAddress(info);
            }

        }
    }

    /**
     * 填充地址信息
     * @param addressInfo
     */
    private void fillDataForAddress(AddressInfo addressInfo){
        mAddressInfo = addressInfo;
        if (mAddressInfo == null ){
            rlAddAddress.setVisibility(View.VISIBLE);
            GoodsAddress.setVisibility(View.GONE);
        }else{
            rlAddAddress.setVisibility(View.GONE);
            GoodsAddress.setVisibility(View.VISIBLE);
        }
        //是否默认地址
        if (mAddressInfo.isDefault()){
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


    @Override
    public void onDefaultAddressError() {
        ViewShowUtils.showShortToast(ConfirmOrderActivity.this, "获取收货地址失败，请稍后重试");
    }

}
