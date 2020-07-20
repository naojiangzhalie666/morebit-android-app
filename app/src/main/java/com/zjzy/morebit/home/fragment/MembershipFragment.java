package com.zjzy.morebit.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.SkillAdapter;
import com.zjzy.morebit.adapter.StudyTitleAdapter;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.StudyRank;
import com.zjzy.morebit.pojo.request.RequestListBody;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 *会员权益
 */
public class MembershipFragment extends BaseMainFragmeng {


    private RecyclerView rcy_title,rcy_icon;



    public static MembershipFragment newInstance() {
        MembershipFragment fragment = new MembershipFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_membership, container, false);
        getData();
        getStudyRank();
        initView(view);
        return view;
    }

    private void getStudyRank() {


    }


    private void getData() {

    }




    private void initView(View view) {


        Bundle arguments = getArguments();
        if (arguments != null) {
        }

        rcy_title = view.findViewById(R.id.rcy_title);
        rcy_icon=view.findViewById(R.id.rcy_icon);


    }



}
