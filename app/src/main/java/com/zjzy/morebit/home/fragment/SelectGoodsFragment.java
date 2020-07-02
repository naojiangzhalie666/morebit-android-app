package com.zjzy.morebit.home.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.LimitSkillAdapter;
import com.zjzy.morebit.adapter.SelectGoodsAdapter;
import com.zjzy.morebit.adapter.SimpleAdapter;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.goodsvideo.contract.VideoContract;
import com.zjzy.morebit.goodsvideo.presenter.VideoPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.VideoClassBean;
import com.zjzy.morebit.utils.StringsUtils;


import java.util.ArrayList;
import java.util.List;

/**
 * 精选商品
 */
public class SelectGoodsFragment extends BaseMainFragmeng {

    private LRecyclerView rcy_goods;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selectgoods, container, false);
        initView(view);
        return view;
    }



    private void getData() {

    }




    private void initView(View view) {


        Bundle arguments = getArguments();
        if (arguments != null) {

        }
        rcy_goods = view.findViewById(R.id.rcy_goods);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rcy_goods.setLayoutManager(manager);

        SelectGoodsAdapter selectGoodsAdapter=new SelectGoodsAdapter(getActivity());

        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(selectGoodsAdapter);
        rcy_goods.setAdapter(mLRecyclerViewAdapter);
        //add a HeaderView
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.item_selectgoods_head, null, false);
        TextView tv_icon = header.findViewById(R.id.tv_icon);
        TextView tv_title = header.findViewById(R.id.tv_title);
        StringsUtils.retractTitle(tv_icon,tv_title, "特仑苏牛奶大减价了 快来买啊实践活动放松放松房间里刷卡缴费卡洛斯");
        mLRecyclerViewAdapter.addHeaderView(header);

    }






}
