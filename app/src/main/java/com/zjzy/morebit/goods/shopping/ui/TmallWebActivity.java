package com.zjzy.morebit.goods.shopping.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.home.ByItemSourceIdBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.UI.WebViewUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.view.helper.ToolbarWebHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Action;

/**
 * Created by fengrs on 2019/3/17.
 * 天猫国际
 */

public class TmallWebActivity extends BaseActivity {
    @BindView(R.id.re_search)
    RelativeLayout re_search;
    @BindView(R.id.show_commission)
    LinearLayout show_commission;
    @BindView(R.id.rl_share)
    RelativeLayout rl_share;
    @BindView(R.id.rl_bottom_view)
    RelativeLayout rl_bottom_view;
    @BindView(R.id.tv_buy)
    TextView tv_buy;
    @BindView(R.id.tv_earnings)
    TextView tv_earnings;
    @BindView(R.id.tv_search_concessional)
    TextView tv_search_concessional;
    @BindView(R.id.webview)
    WebView mWebview;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.buyRl)
    RelativeLayout buyRl;
    @BindView(R.id.shareTv)
    TextView shareTv;

    private String mUrl;
    private String mItemSourceId = "";
    private ToolbarWebHelper mToolbarWebHelper;
    private boolean mCanGoBack;
    private ShopGoodInfo mShopGoodInfo;
    private String mTitle;
    private String itemUrl;
    private boolean mIsStop;
    public static void start(Activity activity, String url, String title) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        //跳转到网页
        Intent it = new Intent(activity, TmallWebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(C.Extras.WEBURL, url);
        bundle.putString(C.Extras.WEBTITLE, title);
        it.putExtras(bundle);
        activity.startActivity(it);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmall_web);
        ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
        initView();
        setCommissionGone(true);
    }

    private void initView() {
        //webview卡顿优化
        mWebview.setBackgroundColor(Color.TRANSPARENT);
        WebSettings settings = mWebview.getSettings();
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);//开启DOM缓存，关闭的话H5自身的一些操作是无效的
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setBlockNetworkImage(true);
        mUrl = getIntent().getStringExtra(C.Extras.WEBURL);
        String title = getIntent().getStringExtra(C.Extras.WEBTITLE);
        if (TextUtils.isEmpty(mUrl)) return;
        if (!TextUtils.isEmpty(title)) {
            mToolbarWebHelper = new ToolbarWebHelper(this).setCustomTitle(title).setCustomOff(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }).setCustomBack(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (mIsStop) {
                            return;
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Instrumentation inst = new Instrumentation();
                                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                            }
                        }).start();

                    } catch (Exception e) {
                        e.printStackTrace();
                        finish();
                    }
                }
            }).setCustomRefresh(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mWebview != null) {
                        //恢复pauseTimers状态
                        if (mWebview == null) return;
                        mWebview.resumeTimers();
                        mWebview.reload();
                        MyLog.i("currentThreadName  + " + Thread.currentThread().getName());

                    }
                }
            });
        }

        WebViewUtils.InitSetting(this, mWebview, mUrl, new MyAction.OnResult<String>() {
            @Override
            public void invoke(String arg) {
            }

            @Override
            public void onError() {
                MyLog.i("test", "onError");
            }
        });
        mWebview.loadUrl(mUrl);
        mWebview.setWebChromeClient(new WebChromeClient() {

            /**
             * 当WebView加载之后，返回 HTML 页面的标题 Title
             * @param view
             * @param title
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (!TextUtils.isEmpty(title)) {
                    mTitle = title;

                }
            }
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (pb != null) {
                    pb.setProgress(newProgress);
                    if (newProgress == 100) {
                        pb.setVisibility(View.GONE);
                    }else {
                        if (pb.getVisibility() == View.GONE)
                            pb.setVisibility(View.VISIBLE);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }




        });
        mWebview.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mWebview.getSettings().setBlockNetworkImage(false);//网页图片延迟加载
                if (!TextUtils.isEmpty(mTitle) && !mTitle.contains("http")) {
                    mToolbarWebHelper.setCustomTitle(mTitle);
                }
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                if (mWebview == null)return;
                if (mWebview != null) {
                    mCanGoBack = mWebview.canGoBack();
                }
                MyLog.d("TmallWebActivity", "onPageCommitVisible  url   " + url);
                MyLog.d("TmallWebActivity", "onPageCommitVisible  mCanGoBack   " + mCanGoBack);

                mToolbarWebHelper.setOffVisibility(mCanGoBack ? View.VISIBLE : View.GONE);

                if (url.contains("detail") && mCanGoBack) {
                    if (TextUtils.isEmpty(url)) return;
                    itemUrl = url;
                    Map<String, String> stringStringMap = AppUtil.URLRequest(url);

                    for (Map.Entry<String, String> entry : stringStringMap.entrySet()) {
                        String key = entry.getKey().toString();
                        String value = entry.getValue().toString();
                        if ("id".equals(key) && !TextUtils.isEmpty(value)) {
                            mItemSourceId = value;
                            setCommissionGone(false);
                        }
                    }
                } else {
                    setCommissionGone(true);
                    showCommission(false);
                }
            }
        });
    }

    @OnClick({R.id.re_search, R.id.rl_share, R.id.tv_buy, R.id.rl_bottom_view,R.id.buyRl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.re_search:
                searchConcessional();
                break;
            case R.id.rl_share:
                if (TaobaoUtil.isAuth()) {//淘宝授权
                    TaobaoUtil.getAllianceAppKey((BaseActivity) this);
                } else {
                    if (isGoodsLose()) return;
                    ArrayList<ImageInfo> arrayList = new ArrayList<>();
                    if (mShopGoodInfo != null && mShopGoodInfo.getItemBanner() != null) {
                        List<String> banner = mShopGoodInfo.getItemBanner();
                        for (int i = 0; i < banner.size(); i++) {
                            String s = StringsUtils.checkHttp(banner.get(i));
                            if (TextUtils.isEmpty(s)) return;
                            if (LoadImgUtils.isPicture(s)) {
                                ImageInfo imageInfo = new ImageInfo();
                                imageInfo.setThumb(banner.get(i));
                                arrayList.add(imageInfo);
                            }
                        }
                        mShopGoodInfo.setAdImgUrl(arrayList);
                    }
                    LoadingView.showDialog(this, "");
                    GoodsUtil.getTaoKouLing(TmallWebActivity.this, mShopGoodInfo, null);
                }
                break;
            case R.id.buyRl:
                if(!TextUtils.isEmpty(itemUrl)){
                    TaobaoUtil.showUrl(TmallWebActivity.this,itemUrl);
                }
                break;
            case R.id.tv_buy:
                if (TaobaoUtil.isAuth()) {//淘宝授权
                    TaobaoUtil.getAllianceAppKey((BaseActivity) this);
                } else {
                    if (isGoodsLose()) return;
                    GoodsUtil.getCouponInfo(this, mShopGoodInfo);
                }
                break;
            case R.id.rl_bottom_view:

                break;
        }
    }

    /**
     * 搜索优惠券
     */
    private void searchConcessional() {

        LoadingView.showDialog(this);
        ByItemSourceIdBean requestBean = new ByItemSourceIdBean();
        requestBean.setItemSourceId(mItemSourceId);
        RxHttp.getInstance().getGoodsService()
                .getItemByItemSourceId(requestBean)
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(this.<BaseResponse<ShopGoodInfo>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<ShopGoodInfo>() {

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        showErrorView(true);
                        tv_earnings.setText(getString(R.string.search_no_concessional));
                        tv_earnings.setVisibility(View.VISIBLE);
                        tv_buy.setText("自购省");
                        shareTv.setText("分享赚");
                    }

                    @Override
                    protected void onSuccess(ShopGoodInfo data) {
                        mShopGoodInfo = data;
                        String string = getString(R.string.commission, MathUtils.getMuRatioComPrice(UserLocalData.getUser((Activity) TmallWebActivity.this).getCalculationRate(), mShopGoodInfo.getCommission()));
                        tv_earnings.setText(string);
                        tv_earnings.setVisibility(View.VISIBLE);
                        tv_buy.setText("自购省￥"+ MathUtils.getMuRatioComPrice(UserLocalData.getUser((Activity) TmallWebActivity.this).getCalculationRate(), mShopGoodInfo.getCommission())+"元");
                        shareTv.setText("分享赚￥"+ MathUtils.getMuRatioComPrice(UserLocalData.getUser((Activity) TmallWebActivity.this).getCalculationRate(), mShopGoodInfo.getCommission())+"元");
                        showCommission(true);
                       // setBottomBtnCommiss(mShopGoodInfo);
                    }
                });
    }

    /**
     * 是否隐藏优惠券UI
     */
    private void setCommissionGone(boolean isVisibility) {
        if(null == tv_earnings || null == tv_search_concessional || null == rl_bottom_view){
            return;
        }
        if (isVisibility)
            tv_earnings.setText(getString(R.string.tmall_search_commission));
        if (C.UserType.member.equals(UserLocalData.getUser(this).getPartner())) {
            tv_earnings.setVisibility(View.GONE);
            tv_search_concessional.setText(getString(R.string.search_concessional_vip));
        } else {
            tv_search_concessional.setText(getString(R.string.search_concessional));
            tv_earnings.setVisibility(isVisibility ? View.GONE : View.VISIBLE);
        }
        rl_bottom_view.setVisibility(isVisibility ? View.GONE : View.VISIBLE);
    }

    /**
     * 是否
     */
    private void showCommission(boolean isVisibility) {
        buyRl.setVisibility(View.GONE);
        if (isVisibility) {
            show_commission.setVisibility(View.VISIBLE);
            re_search.setVisibility(View.GONE);
        } else {
            re_search.setVisibility(View.VISIBLE);
            show_commission.setVisibility(View.GONE);
        }

    }

    private void showErrorView(boolean isShow){
        if(isShow){
            buyRl.setVisibility(View.VISIBLE);
            re_search.setVisibility(View.GONE);
        }else{
            buyRl.setVisibility(View.GONE);
            re_search.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIsStop = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsStop = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 商品是否过期
     *
     * @return
     */
    private boolean isGoodsLose() {
        if (!LoginUtil.checkIsLogin(this)) {
            return true;
        }
        if (AppUtil.isFastClick(200)) {
            return true;
        }
        if (mShopGoodInfo == null) {
            return true;
        }

        return false;
    }


    private void setBottomBtnCommiss(ShopGoodInfo mData){
        if (TextUtils.isEmpty(UserLocalData.getUser(this).getPartner()) || C.UserType.member.equals(UserLocalData.getUser(this).getPartner())) {
            shareTv.setText("分享赚￥"+MathUtils.getMuRatioComPrice(UserLocalData.getUser(this).getCalculationRate(), mData.getCommission())+"元");
        } else {
            if (!StringsUtils.isEmpty(mData.getCommission())) {
                if ("分享".equals(shareTv.getText().toString())) {
                    String muRatioComPrice = MathUtils.getMuRatioComPrice(UserLocalData.getUser(this).getCalculationRate(), mData.getCommission());
                    setBuyText(mData.getCommission(), mData.getCouponPrice());
                    if (!TextUtils.isEmpty(muRatioComPrice)) {
                        shareTv.setText(getString(R.string.goods_share_moeny, muRatioComPrice));
                    }
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
        boolean isLogin = LoginUtil.checkIsLogin(this, false);
        boolean invalidMoney = MathUtils.checkoutInvalidMoney(couponPrice);// 是否有券
        if (!isLogin && !invalidMoney) {//（1）未登录，商品没券：立即购买
            tv_buy.setText(getString(R.string.immediately_buy));
        } else if (isLogin && !invalidMoney && C.UserType.member.equals(user.getPartner())) {//（6）已登录，普通会员，商品没券：立即购买
            tv_buy.setText(getString(R.string.immediately_buy));
        } else {
            double allDiscountsMoney = MathUtils.allDiscountsMoney(user.getCalculationRate(), commission, couponPrice);
            String allDiscountsMoneyStr = MathUtils.formatTo2Decimals(allDiscountsMoney + "");
            tv_buy.setText(getString(R.string.immediately_buy_discounts, allDiscountsMoneyStr));
        }
    }

}
