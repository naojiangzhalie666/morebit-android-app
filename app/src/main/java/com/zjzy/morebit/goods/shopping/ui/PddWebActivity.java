package com.zjzy.morebit.goods.shopping.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
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

public class PddWebActivity extends BaseActivity {


    @BindView(R.id.webview)
    WebView mWebview;
    @BindView(R.id.pb)
    ProgressBar pb;
    private String mUrl;
    private ToolbarWebHelper mToolbarWebHelper;
    private boolean mCanGoBack;
    private String mTitle;
    private boolean mIsStop;
    public static void start(Activity activity, String url, String title) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        //跳转到网页
        Intent it = new Intent(activity, PddWebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(C.Extras.WEBURL, url);
        bundle.putString(C.Extras.WEBTITLE, title);
        it.putExtras(bundle);
        activity.startActivity(it);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdd_web);
        ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
        initView();

    }

    private void initView() {
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
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                MyLog.d("url:",url);
                if (url.contains("pinduoduo://com.xunmeng.pinduoduo")){
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
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


            }
        });
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



}
