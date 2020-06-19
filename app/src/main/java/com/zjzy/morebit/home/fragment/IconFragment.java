package com.zjzy.morebit.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goodsvideo.adapter.VideoGoodsAdapter;
import com.zjzy.morebit.goodsvideo.contract.VideoContract;
import com.zjzy.morebit.goodsvideo.presenter.VideoPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.VideoClassBean;
import com.zjzy.morebit.utils.SpaceItemDecorationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 抖货分类
 */
public class IconFragment extends MvpFragment<VideoPresenter> implements VideoContract.View  {

    private RecyclerView rcy_icon;

    private List<ShopGoodInfo>  list=new ArrayList<>();
    private SwipeRefreshLayout  swipeList;
    private LinearLayout searchNullTips_ly;


    public static IconFragment newInstance() {
        IconFragment fragment = new IconFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState);

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
        rcy_icon = view.findViewById(R.id.rcy_icon);


    }



    @Override
    protected int getViewLayout() {
        return R.layout.fragment_icon;
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
