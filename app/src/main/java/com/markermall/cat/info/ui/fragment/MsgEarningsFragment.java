package com.markermall.cat.info.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.markermall.cat.Module.common.View.ReUseListView;
import com.markermall.cat.R;
import com.markermall.cat.info.adapter.MsgEarningsAdapter;
import com.markermall.cat.info.contract.MsgContract;
import com.markermall.cat.info.model.InfoModel;
import com.markermall.cat.info.presenter.MsgPresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.mvp.base.frame.MvpFragment;
import com.markermall.cat.network.CommonEmpty;
import com.markermall.cat.pojo.EarningsMsg;
import com.markermall.cat.utils.DateTimeUtils;
import com.markermall.cat.view.ToolbarHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fengrs on 2018/12/4.
 * 收益消息
 */

public class MsgEarningsFragment extends MvpFragment<MsgPresenter> implements MsgContract.View {
    private static final int REQUEST_COUNT = 10;

    @BindView(R.id.mListView)
    ReUseListView mReUseListView;
    private int page = 1;
    private MsgEarningsAdapter mAdapter;

    private CommonEmpty mEmptyView;

    public static MsgEarningsFragment newInstance() {
        Bundle args = new Bundle();
        MsgEarningsFragment fragment = new MsgEarningsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initData() {
        refreshData();
    }


    @Override
    protected void initView(View view) {
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle("收益消息");
        mEmptyView = new CommonEmpty(view, getString(R.string.no_msg), R.drawable.image_meiyouxiaoxi);
        mAdapter = new MsgEarningsAdapter(getActivity());
        mReUseListView.getSwipeList().setOnRefreshListener(new com.markermall.cat.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
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
                    mPresenter.getMsg(MsgEarningsFragment.this, InfoModel.msgAwardType, page, mEmptyView);
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
        mReUseListView.getListView().setFooterViewHint("","仅显示最近7天收益消息","");
        mPresenter.getMsg(this, InfoModel.msgAwardType, page, mEmptyView);
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
            mAdapter.replace(handlerData(data));
            mAdapter.notifyDataSetChanged();
            if(data.size()<10){
                mReUseListView.getListView().setFootViewVisibility(View.VISIBLE);
                mReUseListView.getListView().setNoMore(true);
            }
        } else {
            mAdapter.add(handlerData(data));
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

    public static List<EarningsMsg> handlerData(List<EarningsMsg> data){
        List<EarningsMsg> earningsMsgList = new ArrayList<>();
        String lastTime = "";
        if(null != data && data.size()>0){
            //通过时间判断是今天的商品不显示时间分线
            for (int i = 0; i < data.size(); i++) {
                EarningsMsg item = data.get(i);
                if(!lastTime.equals(DateTimeUtils.getYMDTime(item.getCreateTime()))){
                    //lastTime = DateTimeUtils.getDatetoString(item.getSendTime());
                    String isTodayTime = DateTimeUtils.getDatetoString(item.getCreateTime());
                    if(!isTodayTime.equals("今天")){
                        String currentTime = DateTimeUtils.getYMDTime(item.getCreateTime());
                        if(!currentTime.equals(lastTime)){
                            lastTime = currentTime;
                            EarningsMsg earningsMsg = new EarningsMsg();
                            earningsMsg.setViewType(EarningsMsg.TWO_TYPE);
                            earningsMsg.setCreateTime(item.getCreateTime());
                            earningsMsgList.add(earningsMsg);
                        }

                    }

                }
                earningsMsgList.add(item);
            }
        }

        return earningsMsgList;
    }
}
