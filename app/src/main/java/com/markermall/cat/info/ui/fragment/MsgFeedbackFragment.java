package com.markermall.cat.info.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.markermall.cat.Module.common.View.ReUseListView;
import com.markermall.cat.R;
import com.markermall.cat.adapter.SimpleAdapter;
import com.markermall.cat.adapter.holder.SimpleViewHolder;
import com.markermall.cat.info.adapter.MsgEarningsAdapter;
import com.markermall.cat.info.contract.MsgContract;
import com.markermall.cat.info.presenter.MsgPresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.mvp.base.frame.MvpFragment;
import com.markermall.cat.network.CommonEmpty;
import com.markermall.cat.pojo.EarningsMsg;
import com.markermall.cat.utils.DateTimeUtils;
import com.markermall.cat.utils.DensityUtil;
import com.markermall.cat.view.ToolbarHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by YangBoTian on 2019/5/15.
 * 反馈答复消息
 */

public class MsgFeedbackFragment extends MvpFragment<MsgPresenter> implements MsgContract.View {
    private static final int REQUEST_COUNT = 10;
    @BindView(R.id.mListView)
    ReUseListView mReUseListView;
    private int page = 1;
    private MsgAdapter mAdapter;
    private List<EarningsMsg> listArray = new ArrayList<>();
    private CommonEmpty mEmptyView;
    public static MsgFeedbackFragment newInstance() {
        Bundle args = new Bundle();
        MsgFeedbackFragment fragment = new MsgFeedbackFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getViewLayout() {
//        return R.layout.fragment_msg_feedback;
        return R.layout.fragment_msg_list;
    }

    @Override
    public void onMsgSuccessful(List<EarningsMsg> data) {
        if (page == 1) {
            listArray.clear();
            List<EarningsMsg> list = handlerData(data);
            listArray.addAll(list);
            mAdapter.replace(listArray);
            mAdapter.notifyDataSetChanged();
            if(data.size()<10){
                mReUseListView.getListView().setFootViewVisibility(View.VISIBLE);
                mReUseListView.getListView().setNoMore(true);
            }
        } else {
            List<EarningsMsg> list = handlerData(data);
            listArray.addAll(list);
            mAdapter.replace(listArray);
            mAdapter.notifyDataSetChanged();
        }
        page++;
    }


    @Override
    public void onMsgfailure() {
        mReUseListView.getListView().setNoMore(true);
    }

    @Override
    public void onMsgFinally() {
        mReUseListView.getListView().refreshComplete(REQUEST_COUNT);
        mReUseListView.getSwipeList().setRefreshing(false);
    }

    @Override
    protected void initData() {
        refreshData();
    }

    @Override
    protected void initView(View view) {
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle("反馈回复")
//                .setCustomRightTitle("投诉", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ConfigModel.getInstance().getConfigForKey((RxAppCompatActivity) getActivity(), C.SysConfig.COMPLAINT_SUGGESTION)
//                        .subscribe(new DataObserver<HotKeywords>() {
//                            @Override
//                            protected void onSuccess(HotKeywords data) {
//                                String sysValue = data.getSysValue();
//                                if (!TextUtils.isEmpty(sysValue)) {
//                                    ShowWebActivity.start(getActivity(), sysValue, getString(R.string.complaint_suggestion));
//                                }
//                            }
//                        });
//            }
//        })
        ;
        mEmptyView = new CommonEmpty(view, getString(R.string.no_msg), R.drawable.image_meiyouxiaoxi);
        mAdapter = new MsgAdapter(getActivity());
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
                    mPresenter.getFeedbackMsg(MsgFeedbackFragment.this, page, mEmptyView);
                }

            }
        });
        mReUseListView.getListView().setMarkermallNoMore(true);
        mReUseListView.setAdapter(mAdapter);
    }


    public  List<EarningsMsg> handlerData(List<EarningsMsg> datas){
        List<EarningsMsg> list = new ArrayList<>();
        List<EarningsMsg> allList = new ArrayList<>();
        allList.addAll(listArray);
        allList.addAll(datas);
        int index =listArray.size();
        for (int i = index; i <allList.size() ; i++) {
            String date = allList.get(i).getYearMonthDay();
              if(i==0){
                   if(!TextUtils.isEmpty(date)&&!DateTimeUtils.IsToday(date)){
                       EarningsMsg earningsMsg = new EarningsMsg();
                       earningsMsg.setViewType(EarningsMsg.TWO_TYPE);
                        if(DateTimeUtils.IsYesterday(date)){
                            earningsMsg.setYearMonthDay("昨日");
                        } else{
                            earningsMsg.setYearMonthDay(date);
                        }
                       list.add(earningsMsg);
                   }
                  list.add(allList.get(i));
              } else {
                  if(!TextUtils.isEmpty(date)&&!date.equals(allList.get(i-1).getYearMonthDay())){
                      EarningsMsg earningsMsg = new EarningsMsg();
                      earningsMsg.setViewType(EarningsMsg.TWO_TYPE);
                      if(DateTimeUtils.IsYesterday(date)){
                          earningsMsg.setYearMonthDay("昨日");
                      } else{
                          earningsMsg.setYearMonthDay(date);
                      }

                      list.add(earningsMsg);
                  }
                  list.add(allList.get(i));
              }

        }
        return list;
    }

    private void refreshData() {
        mReUseListView.getSwipeList().post(new Runnable() {

            @Override
            public void run() {
                mReUseListView.getSwipeList().setRefreshing(true);
            }
        });
        page = 1;
        mReUseListView.getListView().setFootViewVisibility(View.GONE);
        mReUseListView.getListView().setNoMore(false);
        mPresenter.getFeedbackMsg(this, page, mEmptyView);
    }
    @Override
    public BaseView getBaseView() {
        return this;
    }


    class MsgAdapter extends SimpleAdapter<EarningsMsg, SimpleViewHolder> {

        private LayoutInflater mInflater;
        private Context mContext;

        public MsgAdapter(Context context) {
            super(context);
            this.mContext = context;
        }

        @Override
        public int getItemViewType(int position) {
            if(getItem(position).getViewType()==EarningsMsg.TWO_TYPE){
                return EarningsMsg.TWO_TYPE;
            }
            return EarningsMsg.ONE_TYPE;
        }
        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {
            EarningsMsg item = getItem(position);
            if( holder instanceof MsgEarningsAdapter.TimeViewHolder){
                TextView time = holder.viewFinder().view(R.id.time);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) time.getLayoutParams();
                layoutParams.setMargins(DensityUtil.dip2px(mContext,(float)11),DensityUtil.dip2px(mContext,(float)13),0,DensityUtil.dip2px(mContext,(float)3));
                time.setLayoutParams(layoutParams);
                time.setText(item.getYearMonthDay());
            } else {
                TextView tv_content = holder.viewFinder().view(R.id.tv_feedback);
                TextView tv_time = holder.viewFinder().view(R.id.tv_time);
                TextView  tv_title= holder.viewFinder().view(R.id.tv_content);
                tv_title.setText(item.getTitle());
                tv_time.setText(item.getHourMinutesSecond());
                tv_content.setText(item.getReplyContent());
            }

        }

        @Override
        protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
            return layoutInflater.inflate(R.layout.item_msg_feedback, parent, false);
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == EarningsMsg.TWO_TYPE)
                return new MsgEarningsAdapter.TimeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_time, parent, false));
            return super.onCreateViewHolder(parent, viewType);
        }
    }
}
