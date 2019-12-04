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
import com.zjzy.morebit.goods.shopping.presenter.GoodsDetailImgPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.event.GoodsHeightUpdateEvent;
import com.zjzy.morebit.pojo.goods.GoodsImgDetailBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
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

public class GoodsDetailImgFragment extends MvpFragment<GoodsDetailImgPresenter> implements GoodsDetailImgContract.View {

    @BindView(R.id.webViewNet)
    MyWebView webViewNet;
    @BindView(R.id.imgList)
    FixRecyclerView imgList;
    private GoodDeImgAdapter mGoodDeImgAdapter;
    private String mImgRequestUrl;
    private String taobaoImgStart = "https://h5api.m.taobao.com/h5";
    private ShopGoodInfo mGoodsInfo;


    @Override
    protected int getViewLayout() {
        return R.layout.fragment_goods_detail_img;
    }

    public static GoodsDetailImgFragment newInstance(ShopGoodInfo goodsInfo) {
        GoodsDetailImgFragment fragment = new GoodsDetailImgFragment();
        Bundle args = new Bundle();
        args.putSerializable(C.Extras.GOODSBEAN, goodsInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        initBundle();

        initListData();

    }

    private void initListData() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http:默认图片.png");
        arrayList.add("http:默认图片.png");
        arrayList.add("http:默认图片.png");
        arrayList.add("http:默认图片.png");
        mGoodDeImgAdapter = new GoodDeImgAdapter(getActivity(), arrayList);
        imgList.setLayoutManager(new LinearLayoutManager(getActivity()));
        imgList.setAdapter(mGoodDeImgAdapter);
    }

    private void initBundle() {
        mGoodsInfo = (ShopGoodInfo) getArguments().getSerializable(C.Extras.GOODSBEAN);
    }

    @Override
    public void showFinally() {

    }

    /**
     * 获取web成功
     *
     * @param data
     */
    @Override
    public void showImgWebSuccess(String data) {
        if (!TextUtils.isEmpty(mImgRequestUrl)) {
            mPresenter.getImgData(GoodsDetailImgFragment.this, mImgRequestUrl, mGoodsInfo);
            return;
        }

        imgList.setVisibility(View.GONE);
        if (webViewNet == null || TextUtils.isEmpty(data)) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            webViewNet.setNestedScrollingEnabled(false);
        }
        webViewNet.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    MyLog.d("ShowGoodsDetailsWebFragment", "" + request.getUrl().toString());
                    if (request.getUrl().toString().contains(taobaoImgStart)) {
                        mImgRequestUrl = request.getUrl().toString();
                        mPresenter.getImgData(GoodsDetailImgFragment.this, mImgRequestUrl, mGoodsInfo);
                    }
                }
                return super.shouldInterceptRequest(view, request);
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


        WebViewUtils.InitSetting(getActivity(), webViewNet, data, new MyAction.OnResult<String>() {
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

    /**
     * 获取img成功
     *
     * @param data
     */
    @Override
    public void showDeImgSuccess(List<String> data) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (!data.get(i).endsWith("gif")) {// 过滤gif图片
                if (LoadImgUtils.isPicture(AppUtil.jointUrl(data.get(i)))) {
                    arrayList.add(AppUtil.jointUrl(data.get(i)));
                }
            }
        }
        imgList.setVisibility(View.VISIBLE);
        webViewNet.setVisibility(View.GONE);
        mGoodDeImgAdapter.setData(arrayList);
        mGoodDeImgAdapter.notifyDataSetChanged();
        EventBus.getDefault().post(new GoodsHeightUpdateEvent());
        if (arrayList.size() != 0) {
            GoodsImgDetailBean picUrls = mGoodsInfo.getPicUrls();
            if (picUrls != null) {
                GoodsImgDetailBean.Bean a = picUrls.getA();
                if (a == null) {
                    a = new GoodsImgDetailBean.Bean(arrayList);
                    picUrls.setA(a);
                } else {
                    a.setContent(arrayList);
                    picUrls.setA(a);
                }
            }
        }
    }

    @Override
    protected void initData() {
    }


    public GoodsImgDetailBean setModuleDescUrlData(GoodsImgDetailBean picUrls, ShopGoodInfo goodsInfo, int analysisFlag) {
        if (mGoodsInfo.getPicUrls() == null) {
            if (picUrls != null) {
                mGoodsInfo.setPicUrls(picUrls);
            }
        } else {
            picUrls = mGoodsInfo.getPicUrls();
        }
        mPresenter.getModuleDescUrlData(this, goodsInfo, picUrls,analysisFlag);
        return picUrls;
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
