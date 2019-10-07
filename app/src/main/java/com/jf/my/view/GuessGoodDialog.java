package com.jf.my.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jf.my.Activity.GoodsDetailActivity;
import com.jf.my.App;
import com.jf.my.LocalData.UserLocalData;
import com.jf.my.Module.common.Activity.BaseActivity;
import com.jf.my.Module.common.Dialog.ClearSDdataDialog;
import com.jf.my.Module.common.Utils.LoadingView;
import com.jf.my.R;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.ImageInfo;
import com.jf.my.pojo.ShopGoodInfo;
import com.jf.my.pojo.UserInfo;
import com.jf.my.pojo.request.RequestMaterialLink;
import com.jf.my.utils.AppUtil;
import com.jf.my.utils.C;
import com.jf.my.utils.GoodsUtil;
import com.jf.my.utils.LoadImgUtils;
import com.jf.my.utils.LoginUtil;
import com.jf.my.utils.MathUtils;
import com.jf.my.utils.SensorsDataUtil;
import com.jf.my.utils.StringsUtils;
import com.jf.my.utils.TaobaoUtil;
import com.jf.my.utils.ViewShowUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 猜你喜欢弹窗
 */

public class GuessGoodDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private OnComissionListener listener;
    private ShopGoodInfo mData;
    private ImageView imageView;
    private TextView title;
    private TextView coupon;
    private TextView commission;
    private TextView discount_price;
    private TextView commissionTv;
    private TextView buyTv;
    private LinearLayout closeLay;
    private RelativeLayout shopLayout;
    List<ImageInfo> indexbannerdataArray = new ArrayList<>();

    public GuessGoodDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public GuessGoodDialog(Context context, int themeResId, ShopGoodInfo data) {
        super(context, themeResId);
        this.mContext = context;
        this.mData = data;
    }

    public GuessGoodDialog(Context context, int themeResId, ShopGoodInfo data, OnComissionListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.mData = data;
        this.listener = listener;
    }


    protected GuessGoodDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_guessgood);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public void setData(ShopGoodInfo data) {
        if (null != data) {
            this.mData = data;
            initView();
        }
    }


    public void setTitle(String text) {

    }

    public void setContent(String text) {
    }

    private void initView() {
        shopLayout = findViewById(R.id.shopLayout);
        shopLayout.setOnClickListener(this);
        closeLay = findViewById(R.id.closeLay);
        closeLay.setOnClickListener(this);
        commissionTv = findViewById(R.id.commissionTv);
        imageView = findViewById(R.id.imageView);
        title = findViewById(R.id.title);
        coupon = findViewById(R.id.coupon);
        commission = findViewById(R.id.commission);
        discount_price = findViewById(R.id.discount_price);
        buyTv = findViewById(R.id.buyTv);
        commissionTv.setText("分享");
        buyTv.setText("购买");
        if (null != mData) {
            setGoodsAdImg(mData);
            if (!TextUtils.isEmpty(mData.getItemPicture())) {
                LoadImgUtils.setImg(mContext, imageView, mData.getItemPicture());
            }
            if (!TextUtils.isEmpty(mData.getItemTitle())) {
                title.setVisibility(View.VISIBLE);
                title.setText(mData.getItemTitle());
            } else {
                title.setVisibility(View.GONE);
            }

            if (!StringsUtils.isEmpty(mData.getCouponPrice())) {
                coupon.setVisibility(View.VISIBLE);
                coupon.setText(mData.getCouponPrice() + "元券");
                setBuyText(mData.getCommission(), mData.getCouponPrice());
            } else {
                coupon.setVisibility(View.GONE);
            }

            if (C.UserType.operator.equals(UserLocalData.getUser(mContext).getPartner()) || C.UserType.vipMember.equals(UserLocalData.getUser(mContext).getPartner())) {
                commission.setText(mContext.getString(R.string.commission, MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), mData.getCommission())));
            } else {
                commission.setText(mContext.getString(R.string.upgrade_commission));
            }

            if (!StringsUtils.isEmpty(mData.getItemVoucherPrice())) {
                discount_price.setText(" ¥" + MathUtils.getPrice(mData.getItemVoucherPrice()));
            }


            if (TextUtils.isEmpty(UserLocalData.getUser(mContext).getPartner()) || C.UserType.member.equals(UserLocalData.getUser(mContext).getPartner())) {
                commissionTv.setText("分享");
            } else {
                if (!StringsUtils.isEmpty(mData.getCommission())) {
                    if ("分享".equals(commissionTv.getText().toString())) {
                        String muRatioComPrice = MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), mData.getCommission());
                        setBuyText(mData.getCommission(), mData.getCouponPrice());
                        if (!TextUtils.isEmpty(muRatioComPrice)) {
                            commissionTv.setText("收益 " + muRatioComPrice + "元");
                        }
                    }
                }
            }

        }
        commissionTv.setOnClickListener(this);
        buyTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shopLayout:
                if (null != mData) {
                    GoodsDetailActivity.start(mContext, mData);
                    App.mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            GuessGoodDialog.this.dismiss();
                        }
                    }, 200);

                }
                break;
            case R.id.closeLay:
                this.dismiss();
                break;
            case R.id.commissionTv:
                if (!LoginUtil.checkIsLogin((Activity) mContext)) {
                    return;
                }
                if (TaobaoUtil.isAuth()) {//淘宝授权
                    TaobaoUtil.getAllianceAppKey((BaseActivity) mContext);
                    return;
                } else {
                    if (isGoodsLose()) return;
                    if (mData != null) {
                        Log.e("解析", indexbannerdataArray.size() + "");
                        mData.setAdImgUrl(indexbannerdataArray);
                    }
                    LoadingView.showDialog(mContext, "");
                    GoodsUtil.getTaoKouLing((RxAppCompatActivity) mContext, mData, null);
                }
                if (listener != null) {
                    listener.onClick(this, "");
                }
                this.dismiss();
                break;
            case R.id.buyTv:
                if (!LoginUtil.checkIsLogin((Activity) mContext)) {
                    return;
                }
                if (TaobaoUtil.isAuth()) {//淘宝授权
                    TaobaoUtil.getAllianceAppKey((BaseActivity) mContext);
                    return;
                } else {
                    if (isGoodsLose()) return;

                    if (mData.getGrab_type() == 1) {
                        showGotoNotification(mData);
                        return;
                    }
                    if (!TextUtils.isEmpty(mData.material) || "11".equals(mData.getItemSource()) && !TextUtils.isEmpty(mData.getComeFrom())) { // 物料商品跳转
                        if ("11".equals(mData.getItemSource()) && !TextUtils.isEmpty(mData.getComeFrom())) {
                            mData.material = mData.getComeFrom();
                        }
                        materialLinkList((RxAppCompatActivity) mContext, mData.getItemSourceId(), mData.material);
                    } else {
                        GoodsUtil.getCouponInfo((RxAppCompatActivity) mContext, mData);
                    }
                }
                SensorsDataUtil.getInstance().buy("", "", mData.getItemSourceId(), mData.getTitle(), mData.getPrice());
                this.dismiss();
                break;

        }
    }


    public static void materialLinkList(final RxAppCompatActivity rxActivity, String itemSourceId, String material) {
        RequestMaterialLink link = new RequestMaterialLink();
        link.setInvedeCode(UserLocalData.getUser().getInviteCode());
        link.setItemId(itemSourceId);
        link.setMaterial(material);
        RxHttp.getInstance().getGoodsService().materialLinkList(link)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<String>>bindToLifecycle())
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        if (!TextUtils.isEmpty(data)) {
                            TaobaoUtil.showUrl(rxActivity, data);
                        }
                    }
                });
    }

    private void showGotoNotification(final ShopGoodInfo goodsInfo) {
        ClearSDdataDialog mDialog = new ClearSDdataDialog(mContext, R.style.dialog, mContext.getString(R.string.hint), "活动马上开始，敬请期待喔~~~", new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View view, String text) {
                GoodsUtil.getCouponInfo((RxAppCompatActivity) mContext, goodsInfo);
            }
        });
        mDialog.setOkTextAndColor(R.color.color_FF4848, "确定");
        mDialog.setCancelTextAndColor(R.color.color_333333, "取消");
        mDialog.show();

    }

    public interface OnComissionListener {
        void onClick(Dialog dialog, String text);
    }


    /**
     * 商品是否过期
     *
     * @return
     */
    private boolean isGoodsLose() {
        if (!LoginUtil.checkIsLogin((Activity) mContext)) {
            return true;
        }
        if (AppUtil.isFastClick(200)) {
            return true;
        }
        if (mData == null) {
            return true;
        }
        if (TextUtils.isEmpty(mData.getItemVoucherPrice())) {
            ViewShowUtils.showLongToast(mContext, "商品已经过期，请联系管理员哦");
            return true;
        }
        return false;
    }


    private void setGoodsAdImg(final ShopGoodInfo mGoodsInfo) {
        //如果没有轮播图,初始化默认商品logo图
        //头顶图片填充数据
        if (mGoodsInfo == null) {
            return;
        }
        indexbannerdataArray.clear();
        if (null != mGoodsInfo.getItemBanner() && mGoodsInfo.getItemBanner().size() <= 1) {
            List<String> getBanner = mGoodsInfo.getItemBanner();

            if (getBanner == null || getBanner.size() == 0) {
                if (!TextUtils.isEmpty(mGoodsInfo.getPicture())) {
                    getBanner = new ArrayList<>();
                    getBanner.add(StringsUtils.checkHttp(mGoodsInfo.getPicture()));
                } else {
                    return;
                }
            } else {
                if (!TextUtils.isEmpty(mGoodsInfo.getPicture())) {
                    getBanner.add(0, StringsUtils.checkHttp(mGoodsInfo.getPicture()));
                }
                mGoodsInfo.setBanner(getBanner);
                mGoodsInfo.setItemBanner(getBanner);
            }

        }
        if (null != mGoodsInfo.getItemBanner()) {
            for (int i = 0; i < mGoodsInfo.getItemBanner().size(); i++) {
                String s = StringsUtils.checkHttp(mGoodsInfo.getItemBanner().get(i));
                if (TextUtils.isEmpty(s)) return;
                if (LoadImgUtils.isPicture(s)) {
                    ImageInfo imageInfo = new ImageInfo();
                    imageInfo.setThumb(mGoodsInfo.getItemBanner().get(i));
                    indexbannerdataArray.add(imageInfo);
                }
            }
        }

    }

    /**
     * @param commission  总佣金
     * @param couponPrice 优惠券金额
     */
    private void setBuyText(String commission, String couponPrice) {
        UserInfo user = UserLocalData.getUser();
        boolean isLogin = LoginUtil.checkIsLogin((Activity) mContext, false);
        boolean invalidMoney = MathUtils.checkoutInvalidMoney(couponPrice);// 是否有券
        if (!isLogin && !invalidMoney) {//（1）未登录，商品没券：立即购买
            buyTv.setText("购买");
        } else if (isLogin && !invalidMoney && C.UserType.member.equals(user.getPartner())) {//（6）已登录，普通会员，商品没券：立即购买
            buyTv.setText("购买");
        } else {
            double allDiscountsMoney = MathUtils.allDiscountsMoney(user.getCalculationRate(), commission, couponPrice);
            String allDiscountsMoneyStr = MathUtils.formatTo2Decimals(allDiscountsMoney + "");
            buyTv.setText("省 " + allDiscountsMoneyStr + "元");
        }
    }

}