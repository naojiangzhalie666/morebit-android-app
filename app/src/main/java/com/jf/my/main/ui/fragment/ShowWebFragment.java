package com.jf.my.main.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.jf.my.Activity.ShowWebActivity;
import com.jf.my.LocalData.UserLocalData;
import com.jf.my.Module.common.Fragment.BaseFragment;
import com.jf.my.R;
import com.jf.my.pojo.UserInfo;
import com.jf.my.pojo.event.LoginSucceedEvent;
import com.jf.my.pojo.event.WebViewEvent;
import com.jf.my.utils.AppUtil;
import com.jf.my.utils.C;
import com.jf.my.utils.LogUtils;
import com.jf.my.utils.MyLog;
import com.jf.my.utils.ReadImgUtils;
import com.jf.my.utils.SensorsDataUtil;
import com.jf.my.utils.TaobaoUtil;
import com.jf.my.utils.UI.WebViewUtils;
import com.jf.my.utils.action.ACache;
import com.jf.my.utils.action.MyAction;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import butterknife.BindView;

/**
 * Created by fengrs on 2018/9/12.
 * 备注:
 */

public class ShowWebFragment extends BaseFragment {
    public static String URL = "urltext";
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.ll_error)
    LinearLayout ll_error;
    String url;
    boolean isLoginSucceed=false;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessageArray;
    private MYWebChromeClient mWebChromeClient;
    private List<String> mTaobaoLink = null;
    private Handler mHandler;

    public static ShowWebFragment newInstance(String url) {
        ShowWebFragment fragment = new ShowWebFragment();
        Bundle args = new Bundle();
        args.putString(ShowWebFragment.URL, url);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_web, container, false);

        ImageView ivError = view.findViewById(R.id.iv_error);
        ivError.setImageResource(R.drawable.image_meiyouwangluo);

        return view;
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        mTaobaoLink = (List<String>) ACache.get(getActivity()).getAsObject(C.sp.TAOBAO_LINK_CACHE);

        initBundle();
        initWebviews();
        WebViewUtils.InitSetting(getActivity(), webview, url, new MyAction.OnResult<String>() {
            @Override
            public void invoke(String arg) {
                webview.setVisibility(View.VISIBLE);
                ll_error.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                MyLog.i("test", "onError");
                ll_error.setVisibility(View.VISIBLE);
                webview.setVisibility(View.INVISIBLE);
            }
        });
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setGeolocationEnabled(true);
        webview.addJavascriptInterface(new WebJSHook(this), "shareImg");
        //如果不设置WebViewClient，请求会跳转系统浏览器
        webview.setWebViewClient(new WebViewClient() {



            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                if (mWebChromeClient != null && webview != null) {
                    mWebChromeClient.onPageCommitVisible(webview.canGoBack());
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                SensorsDataUtil.getInstance().injectJsSdk(view);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String newurl) {
                try {
                    if(null != mTaobaoLink && mTaobaoLink.size()>0){
                        boolean isFind = false;
                        for (String url: mTaobaoLink) {
                             if(newurl.contains(url)){
                                 isFind = true;
                             }
                        }
                        if(isFind){
                            if (AppUtil.isTaobaoClientAvailable(getActivity())) {
                                TaobaoUtil.showUrl(getActivity(), newurl);
                                return true;
                            }
                        }
                    }


                    if (TextUtils.isEmpty(newurl)) {
                        super.shouldOverrideUrlLoading(view, newurl);
                    }

                    //处理intent协议
                    if (newurl.startsWith("intent://")) {
                        Intent intent;
                        try {
                            intent = Intent.parseUri(newurl, Intent.URI_INTENT_SCHEME);
                            intent.addCategory("android.intent.category.BROWSABLE");
                            intent.setComponent(null);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                                intent.setSelector(null);
                            }
                            List<ResolveInfo> resolves = getActivity().getPackageManager().queryIntentActivities(intent, 0);
                            if (resolves.size() > 0) {
                                getActivity().startActivityIfNeeded(intent, -1);
                            }
                            return true;
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    // 处理自定义scheme协议
                    if (!newurl.startsWith("http")) {
                        LogUtils.Log("yxx", "处理自定义scheme-->" + newurl);
                        try {
                            // 以下固定写法
                            final Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(newurl));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            getActivity().startActivity(intent);
                        } catch (Exception e) {
                            // 防止没有安装的情况
                            e.printStackTrace();
//                            ViewShowUtils.showShortToast(activity, "您所打开的第三方App未安装！");
                        }
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return super.shouldOverrideUrlLoading(view, newurl);
            }

        });
    }

    private void initBundle() {

        if (getArguments() != null) {

            url = getArguments().getString(ShowWebFragment.URL);

            //            if (!"运营专员后台".equals(title)) {
            if (url != null) {
                UserInfo user = UserLocalData.getUser(getActivity());
                if (url.contains("?")) {
                    url = url + "&token=" + UserLocalData.getToken() + "&inviteCode=" + user.getInviteCode() + "&appVersion=" + C.Setting.app_version;
                } else {
                    url = url + "?token=" + UserLocalData.getToken() + "&inviteCode=" + user.getInviteCode() + "&appVersion=" + C.Setting.app_version;
                }
            }
            //            }
            MyLog.i("toUrl:--", url);
        }
    }


    @Subscribe  //订阅事件
    public void onEventMainThread(LoginSucceedEvent event) {
        isLoginSucceed  = true;
    }

    public void refreshWeb() {
        if (webview != null) {
            //恢复pauseTimers状态
            webview.resumeTimers();
            webview.reload();
            MyLog.i("currentThreadName  + " + Thread.currentThread().getName());

        }
    }

    private void initWebviews() {

        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        MyLog.i("test", "initWebviews: ");
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
            public void onProgressChanged(WebView view, int newProgress) {
                if (pb != null) {
                    pb.setProgress(newProgress);
                    if (newProgress == 100) {
                        pb.setVisibility(View.GONE);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }

            /**
             * 当WebView加载之后，返回 HTML 页面的标题 Title
             * @param view
             * @param title
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {
                //判断标题 title 中是否包含有“error”字段，如果包含“error”字段，则设置加载失败，显示加载失败的视图
                if (!TextUtils.isEmpty(title)) {
                    WebViewEvent webViewEvent = new WebViewEvent();
                    webViewEvent.setTitle(title);
                    EventBus.getDefault().post(webViewEvent);
                    if (title.contains("error") || title.contains("网页无法打开") || title.contains("404")) {
                        if (ll_error != null) ll_error.setVisibility(View.VISIBLE);
                        if (webview != null) webview.setVisibility(View.INVISIBLE);
                    }
                }
            }

            // For Android >=3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                MyLog.i("test", "selectImage");
                if (acceptType.equals("image/*")) {
                    if (mUploadMessage != null) {
                        mUploadMessage.onReceiveValue(null);
                        return;
                    }
                    mUploadMessage = uploadMsg;

                    selectImage();
                } else {
                    onReceiveValue();
                }
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooser(uploadMsg, "image/*");
            }

            // For Android  >= 4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg, acceptType);
            }

            // For Android  >= 5.0
            @Override
            @SuppressLint("NewApi")
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (fileChooserParams != null && fileChooserParams.getAcceptTypes() != null
                        && fileChooserParams.getAcceptTypes().length > 0 && fileChooserParams.getAcceptTypes()[0].equals("image/*")) {
                    if (mUploadMessageArray != null) {
                        mUploadMessageArray.onReceiveValue(null);
                    }
                    mUploadMessageArray = filePathCallback;
                    selectImage();
                } else {
                    onReceiveValue();
                }
                return true;
            }
        });
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList == null || selectList.size() == 0) return;
                    for (LocalMedia localMedia : selectList) {
                        if (!TextUtils.isEmpty(localMedia.getCompressPath())) {
                            File fileAlbum = new File(localMedia.getCompressPath());
                            handleFile(fileAlbum);
                        } else {
                            if (!TextUtils.isEmpty(localMedia.getPath())) {
                                File fileAlbum = new File(localMedia.getPath());
                                handleFile(fileAlbum);
                            }
                        }
                    }


                    break;
            }
        }if(requestCode == ShowWebActivity.REQUEST_COLLEGE_FEEDBACK&&resultCode==Activity.RESULT_OK){
            webview.reload();
        } else {
            onReceiveValue();
        }
    }


    private void selectImage() {
        MyLog.i("test", "selectImage1");
        ReadImgUtils.callPermission(getActivity(), new MyAction.OnResult<Uri>() {
            @Override
            public void invoke(Uri arg) {
                if (arg == null) {
                    onReceiveValue();
                }
            }

            /**
             * 返回错误
             */
            @Override
            public void onError() {
                onReceiveValue();
            }
        }, true, false);
    }


    private void handleFile(File file) {
        if (file.isFile()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (null == mUploadMessageArray) {
                    return;
                }
                Uri uri = Uri.fromFile(file);
                Uri[] uriArray = new Uri[]{uri};
                mUploadMessageArray.onReceiveValue(uriArray);
                mUploadMessageArray = null;
            } else {
                if (null == mUploadMessage) {
                    return;
                }
                Uri uri = Uri.fromFile(file);
                mUploadMessage.onReceiveValue(uri);
                mUploadMessage = null;
            }
        } else {
            onReceiveValue();
        }

    }


    private void onReceiveValue() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mUploadMessageArray != null) {
                mUploadMessageArray.onReceiveValue(null);
                mUploadMessageArray = null;

            }
        } else {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MyLog.i("currentThreadName", "sss  " + Thread.currentThread().getName());
        if (webview != null) {
            webview.onResume();
            if (isLoginSucceed){
            //恢复pauseTimers状态
                initBundle();
                webview.loadUrl(url);
                MyLog.i("currentThreadName", "url  " + url);
                if (mHandler ==null){
                    mHandler = new Handler();
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isLoginSucceed  = false;
                        refreshWeb();
                    }
             },500);
         }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (webview != null) {
            webview.onPause();
        }
    }

    @Override
    public void onDestroy() {
        if (webview != null) {
            webview.destroy();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    public void goBack() {
        // 返回上一页面
        if (webview != null) {
            webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webview.goBack();
        }
    }

    public void setWebChromeClient(MYWebChromeClient webChromeClient) {
        mWebChromeClient = webChromeClient;
    }

    public interface MYWebChromeClient {
        void onPageCommitVisible(boolean canGoBack);
    }
}
