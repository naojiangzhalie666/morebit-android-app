package com.zjzy.morebit.home.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.LimitSkillAdapter;
import com.zjzy.morebit.adapter.SelectGoodsAdapter;
import com.zjzy.morebit.goodsvideo.contract.VideoContract;
import com.zjzy.morebit.goodsvideo.presenter.VideoPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.VideoClassBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 精选商品
 */
public class SelectGoodsFragment extends MvpFragment<VideoPresenter> implements VideoContract.View  {

    private RecyclerView rcy_goods;

    private List<ShopGoodInfo>  list=new ArrayList<>();
    private SwipeRefreshLayout  swipeList;
    private LinearLayout searchNullTips_ly;


    public static SelectGoodsFragment newInstance() {
        SelectGoodsFragment fragment = new SelectGoodsFragment();
        Bundle args = new Bundle();
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

        }
        rcy_goods = view.findViewById(R.id.rcy_goods);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rcy_goods.setLayoutManager(manager);
        SelectGoodsAdapter selectGoodsAdapter=new SelectGoodsAdapter(getActivity());
        rcy_goods.setAdapter(selectGoodsAdapter);



    }



    @Override
    protected int getViewLayout() {
        return R.layout.fragment_selectgoods;
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
