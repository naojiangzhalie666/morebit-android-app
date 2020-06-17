package com.zjzy.morebit.Module.common.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Activity.GoodsDetailForJdActivity;
import com.zjzy.morebit.Activity.KoalaWebActivity;
import com.zjzy.morebit.App;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.MainActivity;
import com.zjzy.morebit.Module.common.Dialog.ProgressDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.Module.push.Logger;
import com.zjzy.morebit.R;
import com.zjzy.morebit.main.ui.fragment.SearchResultFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ProgramGetGoodsDetailBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.number.NumberGoodsList;
import com.zjzy.morebit.pojo.request.RequesKoalaBean;
import com.zjzy.morebit.pojo.request.RequesWeiBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestAnalysisTKL;
import com.zjzy.morebit.pojo.requestbodybean.RequestNumberGoodsList;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.SharedPreferencesUtils;
import com.zjzy.morebit.utils.UI.WindowUtils;
import com.zjzy.morebit.utils.helper.ActivityLifeHelper;
import com.zjzy.morebit.view.GuessGoodDialog;
import com.zjzy.morebit.view.SearchGoodsDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 基础activity
 */

public abstract class BaseActivity extends SwipeBaseActivity {

    private Unbinder mUnbinder;
    private NwtWorkRunnable mNwtWorkRunnable;
    protected ImmersionBar mImmersionBar;
    private GuessGoodDialog mGuessGoodDialog;
    private ProgressDialog dialog;
    private Runnable mLoadingRunnable;
    private Handler mHandler;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e("打开了界面----", this.getClass().getSimpleName());
        //初始化沉浸式
        mHandler = new Handler();
        if (isImmersionBarEnabled())
            initImmersionBar();
        ActivityLifeHelper.getInstance().addActivity(this);
//        registerClipEvents(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止横屏
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mUnbinder = ButterKnife.bind(this);

    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
        MobclickAgent.onResume(this);
        if (isShowAllSeek()) {
            openCopyTextDialog(); //检查剪切板内容
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (!MainActivity.isNetwork) {
            mNwtWorkRunnable = new NwtWorkRunnable();
            App.mHandler.postDelayed(mNwtWorkRunnable, 1000);
        }

//        else {
//            ActivityLifeHelper.getInstance().hideNotWorkLinkSucceed();
//
//        }
    }


    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    class NwtWorkRunnable implements Runnable {
        @Override
        public void run() {
            WindowUtils.showPopupWindow(BaseActivity.this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
        MobclickAgent.onPause(this);
        try {
            if (searchDialog != null) {
                if (searchDialog.isShowing()) {
                    searchDialog.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取剪切板内容弹出搜索窗口
     */
    protected void openCopyTextDialog() {
//        if (this)
        ClipboardManager mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        String getClipText = "";
        try {
            getClipText = mClipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
            //判断字符个数大于5
//            LogUtils.Log("getClipText" ,"  "+getClipText.length());
            if (TextUtils.isEmpty(getClipText) || getClipText.length() <= 3) {
                return;
            }
            if (LoginUtil.isSeekMobile(getClipText)) {//判断手机号
                return;
            }

            //判断验证码
            if (getClipText.length() == 6 || getClipText.length() == 10) { //6位的字母和数字组合

                Pattern p = Pattern.compile("\"^(?!\\\\d+$)(?![A-Za-z]+$)[a-zA-Z0-9]{5,6}$\"");  // 邀请码
                Pattern p1 = Pattern.compile("^[A-Za-z0-9]+$");  // 全部是数字和字母
                Matcher m = p.matcher(getClipText);
                Pattern p3 = Pattern.compile("[0-9]*");  // 数字
                Matcher m3 = p3.matcher(getClipText);
                Pattern p2 = Pattern.compile("^[a-zA-Z]*", Pattern.CASE_INSENSITIVE);
                Matcher m2 = p2.matcher(getClipText);
                Matcher m1 = p1.matcher(getClipText);
                if (m.matches()) {
                    return;
                }
                if (m2.matches()) {
                    return;
                }
                if (m1.matches()) {
                    return;
                }
                if (m2.matches() && m.matches()) {
                    return;
                }
                if (m3.matches()) {
                    return;
                }

            }
            //判断不含有链接
//            if (getClipText.startsWith("http")) {
//                return;
//            }
            //判断非客服微信号
            if (getClipText.equals(getString(R.string.sevice_weixin_num))) {
                return;
            }
            if (getClipText.matches("\\d{10}[0-9]")) { // 判断是否是淘宝订单号
                return;
            }
            if (isNumeric(getClipText)) { // 判断是否是淘宝订单号
                return;
            }
            //判断非淘口令
//            int tklIndex1 = -1;
//            int tklIndex2 = -1;
//            tklIndex1 = getClipText.indexOf("￥");
//            tklIndex2 = getClipText.lastIndexOf("￥");
//            if (tklIndex1 != -1 && tklIndex2 != -1 && tklIndex1 != tklIndex2) {
//                return;
//            }
            //判断非复制的邀请码
            UserInfo userInfo = UserLocalData.getUser();
            if (userInfo != null && userInfo.getInviteCode().equals(getClipText)) {
                return;
            }
            String o = (String) SharedPreferencesUtils.get(BaseActivity.this, C.sp.SearchText, "");
            String replaceClipText = AppUtil.replaceText(getClipText);
            MyLog.i("test", "o: " + o + " replaceClipText: " + replaceClipText + " getClipText: " + getClipText);
            if (null != mGuessGoodDialog && mGuessGoodDialog.isShowing()) {
                getAnalysis(getClipText);
            }
            if (!TextUtils.isEmpty(o) && replaceClipText.equals(o)) {
                return;
            }
//            if (getClipText.length() >= 7) {
//                String substring = getClipText.substring(getClipText.length() - 6);
//                if (C.Setting.copy_text_tag.equals(substring)) {//如果是本app复制
//                    MyLog.d("SearchActivity ", "app复制 return ");
//                    return;
//                }
//            }
            getAnalysis(getClipText);

        } catch (Exception e) {
            MyLog.d("SearchActivity ", "Exception  " + e.getMessage());
        }
    }

    /**
     * 检测剪切板内容-弹出搜索窗口
     */
    protected static Dialog searchDialog;

    protected void openSearchDialog(final String getClipStr) {

        searchDialog = new SearchGoodsDialog(BaseActivity.this, R.style.dialog, "", getClipStr, new SearchGoodsDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, String text) {
                final String textTrim = getClipStr.trim();
                ArrayList<String> asObject = (ArrayList<String>) App.getACache().getAsObject(C.sp.searchHotKey);
                if (asObject == null) {
                    asObject = new ArrayList<>();
                }
                boolean isContains = false; //是否包含

                for (String string : asObject) {
                    if (string.equals(textTrim)) {
                        isContains = true;
                        break;
                    }
                }
                if (!isContains) {
                    asObject.add(0, textTrim);
                }
                App.getACache().put(C.sp.searchHotKey, asObject);


                    //跳转到搜索页面
                    gotoSearchResultPage(textTrim);


//                Intent intent = new Intent(BaseActivity.this, SearchResultForPddActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("keyWord", textTrim);
//                bundle.putInt(SearchResultActivity.KEY_SEARCH_FROM, SearchResultForPddActivity.FROM_EXTERNAL);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                SensorsDataUtil.getInstance().searchKeyTrack(textTrim,C.BigData.SUPER_SEARCH);
            }

        });

        if (searchDialog != null) {
            try {
                searchDialog.show();
                SensorsDataUtil.getInstance().dialogSearch(getClipStr);
            } catch (Exception e) {

            }
        }
    }



    //    拼多多
    public Observable<BaseResponse<ShopGoodInfo>> getBaseResponseObservableForPdd(BaseActivity rxActivity, String goodsId) {
        ProgramGetGoodsDetailBean bean = new ProgramGetGoodsDetailBean();
        bean.setType(2);
        bean.setSearchType(1);
        bean.setGoodsId(Long.valueOf(goodsId));
        return RxHttp.getInstance().getCommonService().getJdPddGoodsDetail(bean)
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<ShopGoodInfo>>bindToLifecycle());
    }
    /*
     * 京东
     *
     * */
    public Observable<BaseResponse<ShopGoodInfo>> getBaseResponseObservableForJd(BaseActivity rxActivityt, String goodsId) {
        ProgramGetGoodsDetailBean bean = new ProgramGetGoodsDetailBean();
        bean.setType(1);
        bean.setSearchType(1);
        bean.setGoodsId(Long.valueOf(goodsId));
        return RxHttp.getInstance().getCommonService().getJdPddGoodsDetail(bean)
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(rxActivityt.<BaseResponse<ShopGoodInfo>>bindToLifecycle());
    }

    /**
     * 考拉海购
     *
     * @param rxActivity
     * @param
     * @return
     */
    public Observable<BaseResponse<ShopGoodInfo>> getBaseResponseObservableForKaoLa(BaseActivity rxActivity, String goodsId, String userId) {
        RequesKoalaBean requesKoalaBean = new RequesKoalaBean();
        requesKoalaBean.setUserId(userId);
        requesKoalaBean.setGoodsId(goodsId);
        return RxHttp.getInstance().getCommonService().getKaoLaGoodsDetail(requesKoalaBean)
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<ShopGoodInfo>>bindToLifecycle());
    }

    /**
     * 唯品会
     * @param rxActivity
     * @param
     * @return
     */
    public Observable<BaseResponse<ShopGoodInfo>> getBaseResponseObservableForWei(BaseActivity rxActivity, String  goodsId) {
        RequesWeiBean requesKoalaBean = new RequesWeiBean();
        requesKoalaBean.setGoodsId(goodsId);
        return RxHttp.getInstance().getCommonService().getWeiGoodsDetail(requesKoalaBean)
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<ShopGoodInfo>>bindToLifecycle());
    }
    private void gotoSearchResultPage(String keywords) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(C.Extras.openFragment_isSysBar, true);
        bundle.putString(C.Extras.search_keyword, keywords);
        OpenFragmentUtils.goToSimpleFragment(this, SearchResultFragment.class.getName(), bundle);

    }

    protected void openGuessDialog(ShopGoodInfo data) {
        if (null != mGuessGoodDialog) {
            mGuessGoodDialog.setData(data);
        } else {
            mGuessGoodDialog = new GuessGoodDialog(BaseActivity.this, R.style.dialog, data, new GuessGoodDialog.OnComissionListener() {
                @Override
                public void onClick(Dialog dialog, String text) {

                }

            });
        }


        if (mGuessGoodDialog != null) {
            try {
                mGuessGoodDialog.show();
            } catch (Exception e) {

            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoadingView.closeDialog();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (ImmersionBar.with(this) != null) //一个Activity只有一个ImmersionBar对象, 即使在子类初始化, 在这里也能销毁.
            ImmersionBar.with(this).destroy();  //在BaseActivity里销毁

        if (mNwtWorkRunnable != null) {
            App.mHandler.removeCallbacks(mNwtWorkRunnable);
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        ActivityLifeHelper.getInstance().removeActivity(this);
    }

    protected void setSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
        } else {
            ActivityStyleUtil.initSystemBar(this, R.color.color_757575); //设置标题栏颜色值
        }
    }

    /**
     * Converts an intent into a {@link Bundle} suitable for use as fragment arguments.
     */
    public static Bundle intentToFragmentArguments(Intent intent) {
        Bundle arguments = new Bundle();
        if (intent == null) {
            return arguments;
        }

        final Uri data = intent.getData();
        if (data != null) {
            arguments.putParcelable("_uri", data);
        }

        final Bundle extras = intent.getExtras();
        if (extras != null) {
            arguments.putAll(intent.getExtras());
        }

        return arguments;
    }

    /**
     * 是否弹出去全网搜索
     *
     * @return
     */
    public boolean isShowAllSeek() {
        return true;
    }


    /**
     * 解析淘口令
     */
    public void getAnalysis(final String s) {
        //http://pagoda.gzmiyuan.com/WirelessShareTpwdQueryRequest.php?tkl=%EF%BF%A5QY4ebeJbZyE%EF%BF%A5
//        RxHttp.getInstance().getGoodsService().getAnalysis(s)
        Log.e("itemsoure", s + "");

        if (s.contains("https://m.tb.cn")) {//淘宝
            taoBaoDialog(s);
        } else if (s.startsWith("https://item.m.jd.com")) {//京东
            JdDialog(s);
        } else if (s.startsWith("https://mobile.yangkeduo.com")) {//拼多多
            pddDialog(s);
        } else if (s.startsWith("https://m-goods.kaola.com")) {//考拉
            kaoLaDialog(s);
        } else if (s.startsWith("https://m.vip.com/product")){//唯品会
            wphDialog(s);

        }else{
            openSearchDialog(s);
        }


    }

    private void wphDialog(final String s) {
        if (s.contains("https://m.vip.com/product")){
            String shopid = "";
            String substring = s.substring(0, s.indexOf("?"));
            String[] split = substring.split("-");
            String informationId = split[2];
            shopid = informationId.replace(".html", "");

            getBaseResponseObservableForWei(BaseActivity.this,shopid)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                        }
                    })
                    .subscribe(new DataObserver<ShopGoodInfo>() {
                        @Override
                        protected void onSuccess(final ShopGoodInfo data) {
                            if (data.getCommission()!=null&&!MathUtils.getnum(data.getCommission()).equals("0")){
                                data.setItemPicture(data.getGoodsMainPicture());
                                data.setShopType(6);
                                data.setItemVoucherPrice(data.getVipPrice());
                                data.setItemTitle(data.getGoodsName());
                                openGuessDialog(data);
                            }else{
                                openSearchDialog(s);
                            }
                        }
                    });
        }
    }

    private void kaoLaDialog(final String s) {
        if (s.contains("https://m-goods.kaola.com/product/")) {
            String shopid = "";
            String substring = s.substring(0, s.indexOf(".html"));
            shopid = substring.replace("https://m-goods.kaola.com/product/", "");
            Log.e("itemsoure", shopid + "");
            getBaseResponseObservableForKaoLa(BaseActivity.this, shopid, UserLocalData.getUser(BaseActivity.this).getId())
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                        }
                    })
                    .subscribe(new DataObserver<ShopGoodInfo>() {
                        @Override
                        protected void onSuccess(final ShopGoodInfo data) {
                            if (data.getCommission()!=null&&!MathUtils.getnum(data.getCommission()).equals("0")){
                                data.setItemPicture(data.getImageList().get(0));
                                data.setShopType(5);
                                data.setItemVoucherPrice(data.getCurrentPrice());
                                data.setItemTitle(data.getGoodsTitle());
                                openGuessDialog(data);
                            }else{
                                openSearchDialog(s);
                            }

                        }

                    });


        }

    }

    private void pddDialog(final String s) {
        if (s.contains("https://mobile.yangkeduo.com")) {
            String substring = s.substring(s.indexOf("goods_id=") + 9, s.indexOf("&page_from"));
            Log.e("itemsoure", substring + "");
            getBaseResponseObservableForPdd(BaseActivity.this, substring)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                        }
                    })
                    .subscribe(new DataObserver<ShopGoodInfo>() {
                        @Override
                        protected void onSuccess(final ShopGoodInfo data) {
                            if (data != null) {
                                data.setItemPicture(data.getImageUrl());
                                data.setShopType(3);
                                data.setItemVoucherPrice(data.getVoucherPriceForPdd());
                                openGuessDialog(data);
                            } else {
                                //跳转到搜索页面
                                openSearchDialog(s);

                            }

                        }
                        @Override
                        protected void onError(String errorMsg, String errCode) {
                            if (C.requestCode.B30421.equals(errCode)){
                                //跳转到搜索页面
                                openSearchDialog(s);
                            }
                        }

                    });

        }
    }

    private void JdDialog(final String s) {
        if (s.contains("https://item.m.jd.com/product/")) {
            String stextTrim = "";
            String substring = s.substring(0, s.indexOf(".html"));
            stextTrim = substring.replace("https://item.m.jd.com/product/", "");
            Log.e("itemsoure", stextTrim + "");
            getBaseResponseObservableForJd(BaseActivity.this, stextTrim)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                        }
                    })
                    .subscribe(new DataObserver<ShopGoodInfo>() {
                        @Override
                        protected void onSuccess(final ShopGoodInfo data) {
                            if (data != null) {
                                data.setItemPicture(data.getImageUrl());
                                data.setShopType(4);
                                data.setItemVoucherPrice(data.getVoucherPriceForPdd());
                                openGuessDialog(data);
                            } else {
                                //跳转到搜索页面
                                openSearchDialog(s);

                            }

                        }

                        @Override
                        protected void onError(String errorMsg, String errCode) {
                            if (C.requestCode.B30421.equals(errCode)){
                                //跳转到搜索页面
                                openSearchDialog(s);
                            }
                        }
                    });
        }
    }

    private void OngetDetailDataFinally(String s) {
        //跳转到搜索页面
        openSearchDialog(s);
    }

    private void taoBaoDialog(final String s) {
        RxHttp.getInstance().getGoodsService().getAnalysis(new RequestAnalysisTKL().setTkl(s))
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(this.<BaseResponse<ShopGoodInfo>>bindToLifecycle())
                .subscribe(new DataObserver<ShopGoodInfo>(false) {

                    @Override
                    protected void onSuccess(ShopGoodInfo data) {
                        if (TextUtils.isEmpty(data.getItemTitle())) {
                            return;
                        }
                        SharedPreferencesUtils.put(BaseActivity.this, C.sp.SearchText, AppUtil.replaceText(s));
                        ClipboardManager cma = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        cma.setText(s);
                        if (!TextUtils.isEmpty(data.getItemSourceId())) {
                            openGuessDialog(data);
                        } else {
                            openSearchDialog(data.getItemTitle());
                        }

                    }
                });
    }


    /**
     * 监听复制事件
     *
     * @param activity
     */
    private void registerClipEvents(final Activity activity) {
        final ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        manager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {
                    //由于每个activity都监听事件，所以这里判断下。只在最上层的activity弹出dialog
                    if (activity.hashCode() == ActivityLifeHelper.getInstance().getTopActivity().hashCode()) {
                        if (isShowAllSeek()) {
                            openCopyTextDialog(); //检查剪切板内容
                        }
                    }

                }
            }
        });
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    String getStrFromUniCode(String unicode) {
        String str = "";
        for (int i = 0; i < unicode.length(); i += 4) {
            String s = "";
            for (int j = i; j < i + 4; j++) {
                s += String.valueOf(unicode.charAt(j));
            }
            str += String.valueOf((char) Integer.valueOf(s, 16).intValue());
        }
        return str;
    }

    /**
     * 设置系统状态栏白色
     */
    protected void setStatusBarWhite() {
        ImmersionBar.with(this).statusBarColor(R.color.white).fitsSystemWindows(true).statusBarDarkFont(true, 0.2f).init();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {//不需要App内字体和UI随着系统字体的变化而改变
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /**
     * 显示loading框
     */

    public void showLoadingDialogOnUI() {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
        }
        dialog.show();


    }

    /**
     * 隐藏 loading框
     */
    public void dismissLoadingDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }


    }

    public void isMethodManager(View view) {
        InputMethodManager im = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }


}
