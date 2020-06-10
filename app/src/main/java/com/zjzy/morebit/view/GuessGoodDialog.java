package com.zjzy.morebit.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.LeadingMarginSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.ShareMoneyForKaolaActivity;
import com.zjzy.morebit.Activity.ShareMoneyForPddActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.App;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.ClearSDdataDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goods.shopping.ui.PddWebActivity;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.request.RequestMaterialLink;
import com.zjzy.morebit.pojo.request.RequestPromotionUrlBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.KaipuleUtils;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * 猜你喜欢弹窗
 */

public class GuessGoodDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private OnComissionListener listener;
    private ShopGoodInfo mData;
    private ImageView imageView, good_mall_tag;
    private TextView title;
    private TextView coupon;
    private TextView commission;
    private TextView discount_price;
    private TextView commissionTv;
    private TextView buyTv;
    private LinearLayout closeLay;
    private RelativeLayout shopLayout;
    List<ImageInfo> indexbannerdataArray = new ArrayList<>();
    //拼多多的推广链接
    private String mPromotionUrl;
    //京东领劵领劵
    private String mPromotionJdUrl;

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
        good_mall_tag = findViewById(R.id.good_mall_tag);
        commissionTv.setText("分享");
        buyTv.setText("购买");
        if (null != mData) {
            setGoodsAdImg(mData);
            if (!TextUtils.isEmpty(mData.getItemVoucherPrice())) {
                discount_price.setText("" + MathUtils.getnum(mData.getItemVoucherPrice()));
            }
            if (mData.getShopType() == 1) {
                good_mall_tag.setImageResource(R.mipmap.guess_tao_icon);
            } else if (mData.getShopType() == 4) {
                good_mall_tag.setImageResource(R.mipmap.guess_jd_icon);
                generatePromotionUrlForJd((BaseActivity) mContext, mData.getGoodsId(), mData.getCouponUrl())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                            }
                        })
                        .subscribe(new DataObserver<String>() {
                            @Override
                            protected void onSuccess(final String data) {
                                mPromotionJdUrl = data;
                            }
                        });
            } else if (mData.getShopType() == 5) {
                good_mall_tag.setImageResource(R.mipmap.guess_kaola_icon);
            } else if (mData.getShopType() == 3) {
                good_mall_tag.setImageResource(R.mipmap.guess_pdd_icon);
                generatePromotionUrlForPdd((BaseActivity) mContext, mData.getGoodsId(), mData.getCouponUrl())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                            }
                        })
                        .subscribe(new DataObserver<String>() {
                            @Override
                            protected void onSuccess(final String data) {
                                mPromotionUrl = data;
                            }
                        });
            } else if (mData.getShopType() == 2) {
                good_mall_tag.setImageResource(R.mipmap.guess_tm_icon);
            }else if (mData.getShopType()==6){
                good_mall_tag.setImageResource(R.mipmap.guess_wph_icon);
            }
            if (!TextUtils.isEmpty(mData.getItemPicture())) {
                LoadImgUtils.setImg(mContext, imageView, mData.getItemPicture());
            }
            if (!TextUtils.isEmpty(mData.getItemTitle())) {
                title.setVisibility(View.VISIBLE);
                retractTitle(title, mData.getItemTitle());
                //  StringsUtils.retractGuessTitle(good_mall_tag,title,mData.getItemTitle());
            } else {
                title.setVisibility(View.GONE);
            }

            if (!StringsUtils.isEmpty(mData.getCouponPrice())) {
                coupon.setVisibility(View.VISIBLE);
                coupon.setText(mData.getCouponPrice() + "元券");
                setBuyText(mData.getCommission(), mData.getCouponPrice());
            } else {
                coupon.setVisibility(View.INVISIBLE);
            }

            if (!StringsUtils.isEmpty(mData.getCommission())){
                commission.setVisibility(View.VISIBLE);
                if (C.UserType.operator.equals(UserLocalData.getUser(mContext).getPartner()) || C.UserType.vipMember.equals(UserLocalData.getUser(mContext).getPartner())) {
                    commission.setText(mContext.getString(R.string.commission, MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), mData.getCommission())));
                } else {
                    UserInfo userInfo1 = UserLocalData.getUser();
                    if (userInfo1 == null || TextUtils.isEmpty(UserLocalData.getToken())) {
                        commission.setText("登录赚佣金");
                    } else {
                        commission.setText(mContext.getString(R.string.commission, MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), mData.getCommission())));
                    }
//                commission.setText(mContext.getString(R.string.upgrade_commission));
                }
            }else{
                commission.setVisibility(View.INVISIBLE);
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
//        if ("1".equals(mData.getItemSource())){//京东
//            good_mall_tag.setImageResource(R.mipmap.guess_jd_icon);
//        }else

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
                AppUtil.clearClipboard((Activity) mContext);
                break;
            case R.id.commissionTv:
                if (!LoginUtil.checkIsLogin((Activity) mContext)) {
                    return;
                }

                if (mData.getShopType()==1||mData.getShopType()==2){
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
                }else if (mData.getShopType()==3){//拼多多
                    if (isGoodsLose()) return;
                    if (mData != null) {
                        mData.setAdImgUrl(indexbannerdataArray);
                    }
                    ShareMoneyForPddActivity.start((Activity) mContext, mData, mPromotionUrl);

                }else if (mData.getShopType()==4){//京东
                    if (isGoodsLose()) return;
                    if (mData != null && mPromotionJdUrl!=null) {
                        mData.setAdImgUrl(indexbannerdataArray);
                        mData.setPriceForPdd(mData.getItemVoucherPrice());
                        mData.setClickURL(mPromotionJdUrl);
                    }
                    ShareMoneyForPddActivity.start((Activity) mContext, mData, mPromotionJdUrl);

                }else if (mData.getShopType()==5){//考拉
                    if (isGoodsLose()) return;
                    if (mData != null) {
                        indexbannerdataArray.clear();
                        List<String> getBanner = mData.getImageList();
                        for (int i=0;i<mData.getImageList().size();i++){
                            ImageInfo imageInfo = new ImageInfo();
                            imageInfo.setThumb(getBanner.get(i));
                            indexbannerdataArray.add(imageInfo);
                        }
                        mData.setAdImgUrl(indexbannerdataArray);
                        mData.setPrice(mData.getMarketPrice());
                        mData.setVoucherPrice(mData.getCurrentPrice());
                        mData.setTitle(mData.getGoodsTitle());
                    }
                    ShareMoneyForKaolaActivity.start((Activity) mContext, mData, mData.getPurchaseLink());

                }else if (mData.getShopType()==6){
                    if (isGoodsLose()) return;
                    if (mData != null) {
                        indexbannerdataArray.clear();
                        List<String> getBanner = mData.getGoodsCarouselPictures();
                        for (int i=0;i<getBanner.size();i++){
                            ImageInfo imageInfo = new ImageInfo();
                            imageInfo.setThumb(getBanner.get(i));
                            indexbannerdataArray.add(imageInfo);
                        }
                        mData.setAdImgUrl(indexbannerdataArray);
                        mData.setPrice(mData.getMarketPrice());
                        mData.setVoucherPrice(mData.getVipPrice());
                        mData.setTitle(mData.getGoodsName());
                        mData.setClickURL(mData.getPurchaseLink());
                    }
                    ShareMoneyForPddActivity.start((Activity) mContext, mData, mData.getPurchaseLink());

                }

                this.dismiss();
                AppUtil.clearClipboard((Activity) mContext);
                break;
            case R.id.buyTv:
                if (!LoginUtil.checkIsLogin((Activity) mContext)) {
                    return;
                }
                if (mData.getShopType() == 1 || mData.getShopType() == 2) {
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
                } else if (mData.getShopType() == 3) {//拼多多
                    if (mPromotionUrl != null) {
                        if (isHasInstalledPdd() && mPromotionUrl.contains("https://mobile.yangkeduo.com")) {
                            String content = mPromotionUrl.replace("https://mobile.yangkeduo.com",
                                    "pinduoduo://com.xunmeng.pinduoduo");
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(content));
                            mContext.startActivity(intent);
                        } else {
                            PddWebActivity.start((Activity) mContext, mPromotionUrl, mData.getTitle());
                        }
                    }
                } else if (mData.getShopType() == 4) {//京东
                    if (mPromotionJdUrl != null) {
                        KaipuleUtils.getInstance(mContext).openUrlToApp(mPromotionJdUrl);
                    }
                } else if (mData.getShopType() == 5) {//考拉
                    if (mData.getGoodsDetail()!=null){
                        String burl = mData.getGoodsDetail().replace("https://", "kaola://");
                        if (isHasInstalledKaola()){
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(burl));
                            startActivity(intent);
                        }else{
                            ShowWebActivity.start((Activity) mContext,mData.getGoodsDetail(), "");
                        }
                    }
                }else if (mData.getShopType()==6){
                        if (mData.getPurchaseLink()!=null){
                            if (isHasInstalledWph()){
                                if (mData.getDeepLinkUrl()!=null){
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mData.getDeepLinkUrl()));
                                    startActivity(intent);
                                }
                            }else{
                                ShowWebActivity.start((Activity) mContext,mData.getPurchaseLink(), "");
                            }
                        }


                }

                this.dismiss();
                AppUtil.clearClipboard((Activity) mContext);
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


    private void retractTitle(final TextView tv, final String title) {
        if (TextUtils.isEmpty(title)) {
            return;
        }

        SpannableString spannableString = new SpannableString(title);
        LeadingMarginSpan.Standard what = new LeadingMarginSpan.Standard(40, 0);
        spannableString.setSpan(what, 0, title.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        tv.setText(spannableString);

    }


    /**
     * 拼多多
     *
     * @param rxActivity
     * @param
     * @return
     */
    public Observable<BaseResponse<String>> generatePromotionUrlForPdd(BaseActivity rxActivity,
                                                                       Long goodsId, String couponUrl) {
        RequestPromotionUrlBean bean = new RequestPromotionUrlBean();
        bean.setType(2);
        bean.setGoodsId(goodsId);
        bean.setCouponUrl(couponUrl);
        return RxHttp.getInstance().getCommonService().generatePromotionUrlForPdd(bean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<String>>bindToLifecycle());
    }
    /**
     * 京东
     * @param rxActivity
     * @param
     * @return
     */
    public Observable<BaseResponse<String>> generatePromotionUrlForJd(BaseActivity rxActivity,
                                                                      Long goodsId,String couponUrl) {
        RequestPromotionUrlBean bean = new RequestPromotionUrlBean();
        bean.setGoodsId(goodsId);
        bean.setCouponUrl(couponUrl);
        return RxHttp.getInstance().getCommonService().generatePromotionUrlForJd(bean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<String>>bindToLifecycle());
    }

    /**
     * 判断是否安装拼多多
     *
     * @return
     */
    private boolean isHasInstalledPdd() {
        return AppUtil.checkHasInstalledApp(mContext, "com.xunmeng.pinduoduo");
    }
    /**
     * 判断是否安装考拉
     *
     * @return
     */
    private boolean isHasInstalledKaola() {
        return AppUtil.checkHasInstalledApp(mContext, "com.kaola");
    }

    /**
     * 判断是否安装唯品会
     *
     * @return
     */
    private boolean isHasInstalledWph() {
        return AppUtil.checkHasInstalledApp(mContext, "com.achievo.vipshop");
    }
}