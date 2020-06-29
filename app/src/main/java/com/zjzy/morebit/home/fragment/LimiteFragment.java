package com.zjzy.morebit.home.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.github.jdsjlzx.ItemDecoration.SpaceItemDecoration;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.HomeMenuAdapter;
import com.zjzy.morebit.adapter.LimitAdapter;
import com.zjzy.morebit.goodsvideo.contract.VideoContract;
import com.zjzy.morebit.goodsvideo.presenter.VideoPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.PanicBuyingListBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.VideoClassBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DensityUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 秒杀商品
 */
public class LimiteFragment extends MvpFragment<VideoPresenter> implements VideoContract.View  {

    private RecyclerView rcy_litmit;

    private List<ShopGoodInfo>  list=new ArrayList<>();
    private SwipeRefreshLayout  swipeList;
    private LinearLayout searchNullTips_ly;
    private List<PanicBuyingListBean.TimeListBean.ItemListBean> itemList;


    public static LimiteFragment newInstance( List<PanicBuyingListBean.TimeListBean.ItemListBean> itemList) {
        LimiteFragment fragment = new LimiteFragment();
        Bundle args = new Bundle();
        args.putSerializable(C.UserType.TIMESKILL, (Serializable) itemList);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void initData() {
        getData();

    }

    private void getData() {

    }



    @Override
    protected void initView(View view) {


        Bundle arguments = getArguments();
        if (arguments != null) {
             itemList = (List<PanicBuyingListBean.TimeListBean.ItemListBean>) arguments.getSerializable(C.UserType.TIMESKILL);
        }
        rcy_litmit = view.findViewById(R.id.rcy_litmit);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        rcy_litmit.setLayoutManager(manager);

        LimitAdapter limitAdapter = new LimitAdapter(getActivity(),itemList);
        rcy_litmit.setAdapter(limitAdapter);



    }



    @Override
    protected int getViewLayout() {
        return R.layout.fragment_litmitgoods;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    public void onVideoClassSuccess(List<VideoClassBean> shopGoodInfo) {

    }

    @Override
    public void onVideoClassError(String throwable) {

    }

    @Override
    public void onVideoGoodsSuccess(List<ShopGoodInfo> shopGoodInfo) {




    }

    @Override
    public void onVideoGoodsError() {

    }

    @Override
    public void onCommissionGoodsSuccess(List<ShopGoodInfo> shopGoodInfo) {

    }

    @Override
    public void onCommissionGoodsError() {

    }
}
