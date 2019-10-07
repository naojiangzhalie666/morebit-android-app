package com.markermall.cat.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.markermall.cat.Module.common.Utils.LoadingView;
import com.markermall.cat.Module.common.View.ReUseListView;
import com.markermall.cat.R;
import com.markermall.cat.main.contract.NoticeContract;
import com.markermall.cat.main.presenter.NoticePresenter;
import com.markermall.cat.main.ui.adapter.NoticeAdapter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.mvp.base.frame.MvpActivity;
import com.markermall.cat.pojo.ImageInfo;
import com.markermall.cat.utils.ActivityStyleUtil;
import com.markermall.cat.utils.DensityUtil;
import com.markermall.cat.view.RecycleViewItemLine;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: wuchaowen
 * @Description: 公告区
 **/
public class NoticeActivity  extends MvpActivity<NoticePresenter> implements NoticeContract.View {

    @BindView(R.id.listview)
    ReUseListView reUseListView;
    @BindView(R.id.listviewEmpty)
    LinearLayout listviewEmpty;
    NoticeAdapter noticeAdapter;
    private static final int REQUEST_COUNT = 10;
    public static void actionStart(Context mcontext){
        Intent intent = new Intent(mcontext,NoticeActivity.class);
        mcontext.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
        } else {
            ActivityStyleUtil.initSystemBar(this, R.color.color_757575); //设置标题栏颜色值
        }


        noticeAdapter = new NoticeAdapter(this);
        RecycleViewItemLine recycleViewItemLine = new RecycleViewItemLine(this,LinearLayoutManager.VERTICAL,DensityUtil.dip2px(this,11),R.color.color_EDEDED);
        reUseListView.getListView().addItemDecoration(recycleViewItemLine);
        reUseListView.setAdapter(noticeAdapter);

        reUseListView.getSwipeList().setOnRefreshListener(new com.markermall.cat.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstData();
            }
        });

        reUseListView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!reUseListView.getSwipeList().isRefreshing())
                    getMoreData();
            }
        });
        getFirstData();

    }

    @Override
    public void showFinally() {
        reUseListView.getListView().refreshComplete(REQUEST_COUNT);
        reUseListView.getSwipeList().setRefreshing(false);
        LoadingView.dismissDialog();
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_notice;
    }

    @OnClick({R.id.btn_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
        }

    }


    public void getFirstData() {
        reUseListView.setVisibility(View.VISIBLE);
        reUseListView.getListView().setNoMore(false);
        reUseListView.getSwipeList().setRefreshing(true);
        mPresenter.getNoticeList(this,true,14);
    }

    public void getMoreData() {
        mPresenter.getNoticeList(this,false,14);
    }

    @Override
    public void refreshData(List<ImageInfo> data) {
        listviewEmpty.setVisibility(View.GONE);
        noticeAdapter.setData(data);
    }

    @Override
    public void moreData(List<ImageInfo> data) {
        noticeAdapter.addData(data);
    }

    @Override
    public void showNoData() {
        reUseListView.getListView().setNoMore(true);
    }

    @Override
    public void refreshDateFail() {
        reUseListView.setVisibility(View.GONE);
        listviewEmpty.setVisibility(View.VISIBLE);
    }
}
