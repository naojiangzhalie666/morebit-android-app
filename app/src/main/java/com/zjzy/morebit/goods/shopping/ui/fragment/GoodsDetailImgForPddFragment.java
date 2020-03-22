package com.zjzy.morebit.goods.shopping.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.goods.shopping.contract.GoodsDetailImgContract;
import com.zjzy.morebit.goods.shopping.presenter.GoodsDetailImgForPddPresenter;
import com.zjzy.morebit.goods.shopping.presenter.GoodsDetailImgPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.GoodsDetailForPdd;
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

public class GoodsDetailImgForPddFragment extends BaseMainFragmeng  {

    @BindView(R.id.webViewNet)
    MyWebView webViewNet;
    @BindView(R.id.imgList)
    FixRecyclerView imgList;
    private GoodDeImgAdapter mGoodDeImgAdapter;
    private GoodsDetailForPdd mGoodsDetailForPdd;
    protected View mView;




    public static GoodsDetailImgForPddFragment newInstance(GoodsDetailForPdd goodsDetailForPdd) {
        GoodsDetailImgForPddFragment fragment = new GoodsDetailImgForPddFragment();
        Bundle args = new Bundle();
        args.putSerializable(C.Extras.GOODS_DETAIL_PDD, goodsDetailForPdd);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_goods_detail_img_pdd, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        initBundle();
        initListData();

        showDeImgSuccess(mGoodsDetailForPdd.getGoodsDetails());
    }

    private void initListData() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http:默认图片.png");
        arrayList.add("http:默认图片.png");
        arrayList.add("http:默认图片.png");
        arrayList.add("http:默认图片.png");

        List<String> goodsDetails = mGoodsDetailForPdd.getGoodsDetails();
        if (goodsDetails.isEmpty()){
            mGoodsDetailForPdd.setGoodsDetails(arrayList);
        }
        mGoodDeImgAdapter = new GoodDeImgAdapter(getActivity(), mGoodsDetailForPdd.getGoodsDetails());
        imgList.setLayoutManager(new LinearLayoutManager(getActivity()));
        imgList.setAdapter(mGoodDeImgAdapter);
    }

    private void initBundle() {
        mGoodsDetailForPdd = (GoodsDetailForPdd) getArguments().getSerializable(C.Extras.GOODS_DETAIL_PDD);
    }



    /**
     * 获取img成功
     *
     * @param data
     */

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

        mGoodDeImgAdapter.setData(arrayList);
        mGoodDeImgAdapter.notifyDataSetChanged();
        EventBus.getDefault().post(new GoodsHeightUpdateEvent());

    }


}
