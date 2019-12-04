package com.zjzy.morebit.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zjzy.morebit.App;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.goods.BandingAliRelationBean;
import com.zjzy.morebit.pojo.request.RequestALiCodeBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.UI.WebViewUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.view.ToolbarHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by YangBoTian on 2019/2/16.
 * 淘宝授权web
 */

public class ChannelWebActivity extends BaseActivity {
    @BindView(R.id.webview)
    WebView mWebview;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.iv_content)
    ImageView mIvContent;
    @BindView(R.id.tv_redirects)
    TextView mTvRedirects;
    @BindView(R.id.iv_retry)
    ImageView mIvRetry;
    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    private String mUrl;
    public static final int AUTHO_LOADING = 1;
    public static final int AUTHO_ERROR = 2; // 重试
    public static final int AUTHO_SUCCEED = 3;
    public static final int AUTHO_ERROR_FINISH = 4;//关闭
    private int mResultType;

    public static void start(Activity activity, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        //跳转到网页
        Intent it = new Intent(activity, ChannelWebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        it.putExtras(bundle);
        activity.startActivity(it);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_web);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
        } else {
            ActivityStyleUtil.initSystemBar(this, R.color.color_757575); //设置标题栏颜色值
        }
        initView();
    }

    private void initView() {
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(getString(R.string.accredit));
        mUrl = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(mUrl)) return;
        WebViewUtils.InitSetting(this, mWebview, mUrl, new MyAction.OnResult<String>() {
            @Override
            public void invoke(String arg) {
            }

            @Override
            public void onError() {
                MyLog.i("test", "onError");
            }
        });//http://127.0.0.1:12345/error?code=Q8IlEnPxRM5maJaop2swBBGD13117303&state=1212
        mWebview.loadUrl(mUrl);
        mWebview.setWebChromeClient(new WebChromeClient() {


            public void onProgressChanged(WebView view, int newProgress) {
                if (pb != null) {
                    pb.setProgress(newProgress);
                    if (newProgress == 100) {
                        pb.setVisibility(View.GONE);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }


        });
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                MyLog.i("test", "url   " + url);
                if (url.contains("code=")) {
                    try {
                        String[] temp = url.split("code=");
                        if (temp != null && temp.length >= 2) {
                            String code = temp[1].substring(0, temp[1].indexOf("&"));
                            MyLog.i("test", "code: " + code);
                            sendCode(code);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    showIVContentVisibility(ChannelWebActivity.AUTHO_LOADING,"");
                    mWebview.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }


        });
    }

    /**
     * 传code给后台
     *
     * @param code
     */
    private void sendCode(String code) {
        RxHttp.getInstance().getUsersService().senALiCode(new RequestALiCodeBean().setAccessCode(code))
                .compose(RxUtils.<BaseResponse<BandingAliRelationBean>>switchSchedulers())
                .compose(this.<BaseResponse<BandingAliRelationBean>>bindToLifecycle())
                .subscribe(new DataObserver<BandingAliRelationBean>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        showIVContentVisibility(ChannelWebActivity.AUTHO_ERROR, getString(R.string.taobao_autho_retry));
                    }

                    @Override
                    protected void onSuccess(BandingAliRelationBean data) {
                        mResultType = data.getResultType();
                        switch (mResultType) {// 0 默认 ,1 重试  ,2 关闭   , 3 成功 ,
                            case 1:
                                showIVContentVisibility(ChannelWebActivity.AUTHO_ERROR, data.getErrMsg());
                                break;
                            case 2:
                                showIVContentVisibility(ChannelWebActivity.AUTHO_ERROR_FINISH, data.getErrMsg());
                                break;
                            case 3:
                                UserLocalData.getUser().setNeedAuth(false);
                                UserLocalData.setUser(ChannelWebActivity.this, UserLocalData.getUser());
                                showIVContentVisibility(ChannelWebActivity.AUTHO_SUCCEED, "");
                                App.mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ChannelWebActivity.this.finish();

                                    }
                                }, 1200);
                                break;

                            default:
                                break;
                        }

                    }
                });
    }

    @OnClick(R.id.iv_retry)
    public void onViewClicked() {
        switch (mResultType) {// 0 默认 ,1 重试  ,2 关闭   , 3 成功 ,
            case 0:
            case 1:
                mLlContent.setVisibility(View.GONE);
                mWebview.setVisibility(View.VISIBLE);
                mWebview.loadUrl(mUrl);
                mWebview.reload();
                break;
            case 2:
                ChannelWebActivity.this.finish();
                break;
            default:
                break;
        }
    }


    public void showIVContentVisibility(int type, String text) {
        mLlContent.setVisibility(View.VISIBLE);
        mIvRetry.setVisibility(View.GONE);
        switch (type) {
            case ChannelWebActivity.AUTHO_LOADING:

                mIvContent.setImageResource(R.drawable.icon_taobao_accredit_loading);
                mTvRedirects.setText(getString(R.string.taobao_autho_loading));
                break;
            case ChannelWebActivity.AUTHO_ERROR:
                mIvContent.setImageResource(R.drawable.icon_taobao_accredit_error);
                mIvRetry.setVisibility(View.VISIBLE);
                mTvRedirects.setText(text == null ? "" : text);
                mIvRetry.setImageResource(R.drawable.icon_taobao_accredit_retry);
                break;
            case ChannelWebActivity.AUTHO_SUCCEED:
                mIvContent.setImageResource(R.drawable.icon_taobao_accredit_succeed);
                mTvRedirects.setText(getString(R.string.automatically_redirects));

                break;
            case ChannelWebActivity.AUTHO_ERROR_FINISH:
                mIvContent.setImageResource(R.drawable.icon_taobao_accredit_error);
                mTvRedirects.setText(text == null ? "" : text);
                mIvRetry.setVisibility(View.VISIBLE);
                mIvRetry.setImageResource(R.drawable.icon_taobao_accredit_close);
                break;

            default:
                break;
        }
    }
}
