package com.zjzy.morebit.goods.shopping.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.GoodDeImgAdapter;
import com.zjzy.morebit.goods.shopping.contract.GoodsDetailImgContract;
import com.zjzy.morebit.goods.shopping.contract.NumberGoodsDetailImgContract;
import com.zjzy.morebit.goods.shopping.presenter.GoodsDetailImgPresenter;
import com.zjzy.morebit.goods.shopping.presenter.NumberGoodsDetailImgPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.event.GoodsHeightUpdateEvent;
import com.zjzy.morebit.pojo.goods.GoodsImgDetailBean;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.number.NumberGoodsInfo;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.UI.WebViewForHtmlDataUtils;
import com.zjzy.morebit.utils.UI.WebViewUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.view.FixRecyclerView;
import com.zjzy.morebit.view.MyWebView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by fnegs on 2018/11/3.
 * 详情图片
 */

public class NumberGoodsDetailImgFragment extends MvpFragment<NumberGoodsDetailImgPresenter> implements NumberGoodsDetailImgContract.View {

    @BindView(R.id.webViewNet)
    MyWebView webViewNet;

    private String htmlData;


    @Override
    protected int getViewLayout() {
        return R.layout.fragment_number_goods_detail_img;
    }

    public static NumberGoodsDetailImgFragment newInstance() {
        NumberGoodsDetailImgFragment fragment = new NumberGoodsDetailImgFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(C.Extras.GOODS_CONTENTS, htmlData);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View view) {

        if (webViewNet == null ) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            webViewNet.setNestedScrollingEnabled(false);
        }
        webViewNet.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
        webViewNet.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            public void onProgressChanged(WebView view, int newProgress) {
                EventBus.getDefault().post(new GoodsHeightUpdateEvent());
                super.onProgressChanged(view, newProgress);
            }
        });




    }

    public void loadHtmlData(String htmlData){
        WebViewForHtmlDataUtils.InitSetting(getActivity(), webViewNet, htmlData, new MyAction.OnResult<String>() {
            @Override
            public void invoke(String arg) {
                if (webViewNet != null)
                    webViewNet.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                if (webViewNet != null)
                    webViewNet.setVisibility(View.GONE);
            }
        });
        EventBus.getDefault().post(new GoodsHeightUpdateEvent());
    }







    @Override
    protected void initData() {
    }



    @Override
    public BaseView getBaseView() {
        return this;
    }


    @Override
    public void onDestroy() {
        if (webViewNet != null) {
            webViewNet.clearHistory();
            ((ViewGroup) webViewNet.getParent()).removeView(webViewNet);
            webViewNet.destroy();
            webViewNet = null;
        }
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (webViewNet != null) {
            webViewNet.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (webViewNet != null) {
            webViewNet.onPause();
        }
    }


}
