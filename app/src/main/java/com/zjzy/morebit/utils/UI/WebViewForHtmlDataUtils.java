package com.zjzy.morebit.utils.UI;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.blankj.utilcode.util.ToastUtils;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.action.MyAction;

import java.io.ObjectInputStream;

/**
 * Created by fengrs on 2018/7/30.
 */

public class WebViewForHtmlDataUtils {

    public static void InitSetting(final Activity activity, final WebView webview, final String htmlData, final MyAction.OnResult<String> action) {
        if (action != null) action.invoke("");

        //声明WebSettings子类
        WebSettings webSettings = webview.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小


        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setSavePassword(false);

        webview.setVerticalScrollBarEnabled(false);
        webview.setHorizontalScrollBarEnabled(false);
// 设置可以支持缩放
        webSettings.setSupportZoom(true);
// 扩大比例的缩放
        webSettings.setUseWideViewPort(true);
// 自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setPluginState(WebSettings.PluginState.ON);
        // 加载空白error
        webSettings.setDomStorageEnabled(true);
        webview.getSettings().setJavaScriptEnabled(true);

        SensorsDataUtil.getInstance().showUpWebView(webview, false);
        //使图片自适应完美解决
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>*{margin:0;padding:0;}img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        String content = "<html>" + head + "<body>" + htmlData + "</body></html>";
        // *{margin:0;padding:0这个是关键解决空白问题的代码，完美解决所有问题，

        webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);


        // 返回键后退
        webview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {  //表示按返回键
//                        时的操作
                        webview.goBack();   //后退

                        return true;    //已处理
                    }
                }
                return false;
            }
        });
    }

}
