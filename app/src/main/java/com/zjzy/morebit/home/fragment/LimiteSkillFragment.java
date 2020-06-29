package com.zjzy.morebit.home.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.github.jdsjlzx.ItemDecoration.SpaceItemDecoration;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.HomeMenuAdapter;
import com.zjzy.morebit.adapter.LimitSkillAdapter;
import com.zjzy.morebit.goodsvideo.contract.VideoContract;
import com.zjzy.morebit.goodsvideo.presenter.VideoPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.VideoClassBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 秒杀商品
 */
public class LimiteSkillFragment extends MvpFragment<VideoPresenter> implements VideoContract.View  {

    private RecyclerView rcy_litmit;

    private List<ShopGoodInfo>  list=new ArrayList<>();
    private SwipeRefreshLayout  swipeList;
    private LinearLayout searchNullTips_ly;


    public static LimiteSkillFragment newInstance() {
        LimiteSkillFragment fragment = new LimiteSkillFragment();
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
        rcy_litmit = view.findViewById(R.id.rcy_litmit);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rcy_litmit.setLayoutManager(manager);
        LimitSkillAdapter limitSkillAdapter=new LimitSkillAdapter(getActivity());
        rcy_litmit.setAdapter(limitSkillAdapter);



    }



    @Override
    protected int getViewLayout() {
        return R.layout.fragment_litmitskill;
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
