package com.zjzy.morebit.Activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.main.ui.fragment.ShowWebFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.event.WebViewEvent;
import com.zjzy.morebit.pojo.event.WebViewUpdataColorEvent;
import com.zjzy.morebit.pojo.request.RequesKoalaBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.LogUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.view.helper.ToolbarWebHelper;

import org.greenrobot.eventbus.Subscribe;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/*
*
* 考拉webview
* */
public class KoalaWebActivity extends BaseActivity {

    private View mViewBar,mViewLine;
    private RelativeLayout mRlToolbar;
    private TextView mToolbarTtle;
    ImageView mIvBack,mIvRefresh,mIvOff;
    private ToolbarWebHelper mToolbarWebHelper;
    private boolean mIsStop;
    private String url = "";
    private String title = "";
    private Bundle bundle;
    private WebView web;
    private LinearLayout rl_bottom_view;
        private TextView directBuyTv;


    public static void start(Activity activity, String url, String title) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        //跳转到网页
        Intent it = new Intent(activity, KoalaWebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        it.putExtras(bundle);
        activity.startActivity(it);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koala_web);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                 //解决状态栏和布局重叠问题，任选其一
                .fitsSystemWindows(false)
                .statusBarColor(R.color.white)
                .init();
        initBundle();
        initView();

    }

    private void initView() {
        directBuyTv= (TextView) findViewById(R.id.directBuyTv);
        rl_bottom_view= (LinearLayout) findViewById(R.id.rl_bottom_view);
        mViewBar=findViewById(R.id.view_bar);
        mViewLine=findViewById(R.id.view_line);
        mRlToolbar= (RelativeLayout) findViewById(R.id.ll_toolbar);
        mToolbarTtle= (TextView) findViewById(R.id.toolbar_title);
        mIvBack= (ImageView) findViewById(R.id.iv_back);
        mIvRefresh= (ImageView) findViewById(R.id.iv_refresh);
        mIvOff= (ImageView) findViewById(R.id.iv_off);
        web= (WebView) findViewById(R.id.web);

        WebSettings webSettings = web.getSettings();

        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存

        webSettings.setJavaScriptEnabled(true);//设置能够解析Javascript

        webSettings.setDomStorageEnabled(true);//设置适应Html5 //重点是这个设置
        web.loadUrl(url);
        mToolbarWebHelper = new ToolbarWebHelper(this).setCustomTitle(title).setCustomOff(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).setCustomBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (mIsStop) {
                                return;
                            }
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    if (web.canGoBack()) {
                                        web.goBack();//返回上个页面
                                        rl_bottom_view.setVisibility(View.GONE);
                                        return;
                                    } else {
                                        finish();
                                    }
                                }
                            });


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
                if (web != null) {
                    //恢复pauseTimers状态
                    web.resumeTimers();
                    web.reload();
                    MyLog.i("currentThreadName  + " + Thread.currentThread().getName());

                }
            }
        });
        //如果不设置WebViewClient，请求会跳转系统浏览器
        web.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);


            }

            @Override
            public void onPageFinished(final WebView view, final String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String newurl) {
                try {

                    if (!TextUtils.isEmpty(newurl)) {
                        if (newurl.contains("https://m-goods.kaola.com/product/")) {
                            String shopid = "";
                            String substring = newurl.substring(0, newurl.indexOf(".html"));
                            shopid = substring.replace("https://m-goods.kaola.com/product/", "");
                            Log.e("uuuu",newurl+"列表");
                            final String finalShopid = shopid;
                            getBaseResponseObservableForKaoLa(KoalaWebActivity.this,shopid, UserLocalData.getUser(KoalaWebActivity.this).getId())
                                    .doFinally(new Action() {
                                        @Override
                                        public void run() throws Exception {
                                        }
                                    })
                                    .subscribe(new DataObserver<ShopGoodInfo>() {
                                        @Override
                                        protected void onSuccess(final ShopGoodInfo data) {
                                           if (data.getCommission()!=null&&!MathUtils.getnum(data.getCommission()).equals("0")){
                                               goodsInfo(finalShopid);//分佣商品跳转考拉详情
                                           }else{
                                               view.loadUrl(newurl); //无分佣商品
                                               rl_bottom_view.setVisibility(View.VISIBLE);
                                               directBuyTv.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if (newurl.contains("https://m-goods.kaola.com/product/")){
                                                           String replace = newurl.replace("https://m-goods.kaola.com", "kaola://goods.kaola.com");
                                                           if (isHasInstalledKaola()){
                                                               Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(replace));
                                                               startActivity(intent);
                                                           }else{
                                                               view.loadUrl(newurl);
                                                           }


                                                       }
                                                       rl_bottom_view.setVisibility(View.GONE);

                                                   }
                                               });

                                           }
                                        }
                                    });
                            return true;
                        }

                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return super.shouldOverrideUrlLoading(view, newurl);
            }

        });

    }


    /**
     * 考拉海购
     * @param rxActivity
     * @param
     * @return
     */
    public Observable<BaseResponse<ShopGoodInfo>> getBaseResponseObservableForKaoLa(BaseActivity rxActivity, String  goodsId, String userId) {
        RequesKoalaBean requesKoalaBean = new RequesKoalaBean();
        requesKoalaBean .setUserId(userId);
        requesKoalaBean.setGoodsId(goodsId);
        return RxHttp.getInstance().getCommonService().getKaoLaGoodsDetail(requesKoalaBean)
                .compose(RxUtils.<BaseResponse<ShopGoodInfo>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<ShopGoodInfo>>bindToLifecycle());
    }
    private void goodsInfo(String shopid) {
        GoodsDetailForKoalaActivity.start(this,shopid);

    }

    private void initBundle() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("title");
            url = bundle.getString("url");
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
    @Subscribe
    public void onEventMainThread(WebViewUpdataColorEvent event) {
        //修改颜色
        if ( mRlToolbar != null && mViewLine != null && mViewBar != null && event != null) {
            if (!TextUtils.isEmpty(event.getColorStr()) && StringsUtils.checkColor(event.getColorStr().trim())) {
                int color = Color.parseColor(event.getColorStr().trim());
                mRlToolbar.setBackgroundColor(color);
                mViewBar.setBackgroundColor(color);
                mViewLine.setBackgroundColor(color);

                if ( mIvOff != null && mIvRefresh != null &&mIvBack != null &&mToolbarTtle != null ){
                    if (event.getIconType() == 0) {
                        mToolbarTtle.setTextColor(ContextCompat.getColor(this, R.color.white));
                        mIvBack.setImageResource(R.drawable.btn_title_return_icon_white);
                        mIvRefresh.setImageResource(R.drawable.web_refresh_icon_white);
                        mIvOff.setImageResource(R.drawable.icon_guanbi_off_white);
                    }else {
                        mToolbarTtle.setTextColor(ContextCompat.getColor(this, R.color.color_333333));
                        mIvBack.setImageResource(R.drawable.btn_title_return_icon);
                        mIvRefresh.setImageResource(R.drawable.web_refresh_icon);
                        mIvOff.setImageResource(R.drawable.icon_guanbi_off);

                    }
                }
            }
        }
    }

    @Subscribe
    public void onEventMainThread(WebViewEvent event) {
        //h5标题修改，因为首页二级只有id，只有webview加载了h5，取到title，同时修改这里的标题
        if (!TextUtils.isEmpty(event.getTitle())) {
            setWebTitle(event.getTitle());
        }
    }

    public void setWebTitle(@Nullable String title) {
        if (!TextUtils.isEmpty(title) && !title.startsWith("http")) {
            mToolbarWebHelper.setCustomTitle(title);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && web.canGoBack()) {
            web.goBack();//返回上个页面
            rl_bottom_view.setVisibility(View.GONE);
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    /**
     * 判断是否安装考拉
     *
     * @return
     */
    private boolean isHasInstalledKaola() {
        return AppUtil.checkHasInstalledApp(this, "com.kaola");
    }
}
