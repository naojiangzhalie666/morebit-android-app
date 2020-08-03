package com.zjzy.morebit.info.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.info.adapter.MsgEarningsAdapter;
import com.zjzy.morebit.info.contract.MsgContract;
import com.zjzy.morebit.info.model.InfoModel;
import com.zjzy.morebit.info.presenter.MsgPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.network.CommonEmpty;
import com.zjzy.morebit.pojo.EarningsMsg;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.view.ToolbarHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fengrs on 2018/12/4.
 * 收益消息
 */

public class MsgEarningsFragment2 extends MvpFragment<MsgPresenter> implements MsgContract.View {
    private static final int REQUEST_COUNT = 10;

    @BindView(R.id.mListView)
    ReUseListView mReUseListView;
    private int page = 1;
    private MsgEarningsAdapter mAdapter;

    private CommonEmpty mEmptyView;
    private int mtype;

    public static MsgEarningsFragment2 newInstance(int type) {
        Bundle args = new Bundle();
        MsgEarningsFragment2 fragment = new MsgEarningsFragment2();
        args.putInt(C.UserType.ORDERTYPE,type);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initData() {
        refreshData();
    }


    @Override
    protected void initView(View view) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mtype = arguments.getInt(C.UserType.ORDERTYPE);

        }
        mEmptyView = new CommonEmpty(view, getString(R.string.no_msg), R.drawable.image_meiyouxiaoxi);
        mAdapter = new MsgEarningsAdapter(getActivity(),mtype);
        mReUseListView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                refreshData();
            }
        });
        mReUseListView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mReUseListView.getSwipeList().isRefreshing()) {
                    mPresenter.getMsg(MsgEarningsFragment2.this, InfoModel.msgAwardType, page, mEmptyView);
                }

            }
        });
        mReUseListView.setAdapter(mAdapter);

    }


    private void refreshData() {
        mReUseListView.getSwipeList().post(new Runnable() {

            @Override
            public void run() {
                mReUseListView.getSwipeList().setRefreshing(true);
            }
        });
        page = 1;
        mReUseListView.getListView().setNoMore(false);
        mReUseListView.getListView().setMarkermallNoMore(true);
        mReUseListView.getListView().setFootViewVisibility(View.GONE);
        mReUseListView.getListView().setFooterViewHint("","仅支持查看近3个月订单","");
        mPresenter.getMsg2(this, InfoModel.msgAwardType, page,mtype, mEmptyView);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_msg_list;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @OnClick({R.id.empty_view})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.empty_view:
                refreshData();
                break;
        }
    }


    @Override
    public void onMsgSuccessful(List<EarningsMsg> data) {
        if (page == 1) {
            mAdapter.replace(data);
            mAdapter.notifyDataSetChanged();
            if(data.size()<10){
              mReUseListView.getListView().setFootViewVisibility(View.VISIBLE);
                mReUseListView.getListView().setNoMore(true);
            }
        } else {
            mAdapter.add(data);
            mAdapter.notifyDataSetChanged();
        }
        page++;
    }

    @Override
    public void onMsgfailure() {
        if (page != 1) {
            mReUseListView.getListView().setNoMore(true);
        }
    }

    @Override
    public void onMsgFinally() {
        mReUseListView.getListView().refreshComplete(REQUEST_COUNT);
        mReUseListView.getSwipeList().setRefreshing(false);
    }


}
