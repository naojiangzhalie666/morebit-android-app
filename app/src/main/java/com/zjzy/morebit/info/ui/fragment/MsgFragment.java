package com.zjzy.morebit.info.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.Activity.SettingNotificationActivity;
import com.zjzy.morebit.App;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.MsgDayHotAdapter;
import com.zjzy.morebit.info.contract.MsgDayHotContract;
import com.zjzy.morebit.info.presenter.MsgDayHotPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.network.CommonEmpty;
import com.zjzy.morebit.pojo.DayHotBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.NotificationsUtils;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by fengrs on 2018/12/4.
 */

public class MsgFragment extends MvpFragment<MsgDayHotPresenter> implements MsgDayHotContract.View {
    private static final int REQUEST_COUNT = 10;
    @BindView(R.id.msgNoticationRl)
    RelativeLayout msgNoticationRl;
    private final static int fans = 2;
    private final static int sys = 1;
    private final static int all = 4;
    private int mNureadConut;
    @BindView(R.id.mListView)
    ReUseListView mReUseListView;
    private int page = 1;
    private MsgDayHotAdapter mAdapter;

    private CommonEmpty mEmptyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }




    @Override
    protected void initData() {
        refreshData();
    }

    @Override
    protected void initView(View view) {
        mEmptyView = new CommonEmpty(view, getString(R.string.no_msg), R.drawable.image_dayhot_empty);


        mAdapter = new MsgDayHotAdapter(getActivity());
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
                    mReUseListView.getListView().setNoMore(true);
//                    mPresenter.getMsg(MsgFragment.this, mEmptyView);
                }

            }
        });
        mReUseListView.getListView().setMarkermallNoMore(true);
        mReUseListView.setAdapter(mAdapter);



        initPush();

    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_msg;
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
    public BaseView getBaseView() {
        return this;
    }


    private void initPush() {
//        Bundle extras = getArguments();
//        if (extras != null&&mTablayout!=null) {
//            int inType = extras.getInt(C.Extras.pushType, 0);
//            switch (inType) {
//                case C.Push.sysMsg://系统消息
//                    mTablayout.setCurrentTab(0);
//                    break;
//                case C.Push.awardMsg://收益消息
//                    mTablayout.setCurrentTab(1);
//                    break;
//                default:
//                    break;
//            }
//        }
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
        mPresenter.getMsg(this, mEmptyView);
    }

    @Override
    public void onMsgSuccessful(List<DayHotBean> data) {
        mAdapter.replace(handlerData(data));
        mAdapter.notifyDataSetChanged();

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




    @OnClick({ R.id.back,R.id.msgSetIv,R.id.openNoticationBtn,R.id.closeNoticationIv,R.id.earningLay,R.id.fansLay,R.id.sysLay,R.id.feedbackLay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.msgSetIv:
                startActivity(new Intent(getActivity(), SettingNotificationActivity.class));
                break;
            case R.id.openNoticationBtn:
                NotificationsUtils.openSystemNotifications(getActivity());
                break;
            case R.id.closeNoticationIv:
                SharedPreferencesUtils.put(App.getAppContext(), C.sp.isShowMsgNotication, false);
                msgNoticationRl.setVisibility(View.GONE);
                break;
            case R.id.earningLay:
                OpenFragmentUtils.goToSimpleFragment(getActivity(), MsgEarningsFragment.class.getName(), new Bundle());
                break;
            case R.id.fansLay:
                OpenFragmentUtils.goToSimpleFragment(getActivity(), MsgFansFragment.class.getName(), new Bundle());
                break;
            case R.id.sysLay:
                OpenFragmentUtils.goToSimpleFragment(getActivity(), MsgSysFragment.class.getName(), new Bundle());
                break;
            case R.id.feedbackLay:
                OpenFragmentUtils.goToSimpleFragment(getActivity(), MsgFeedbackFragment.class.getName(), new Bundle());
                break;
            default:
                break;
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public List<DayHotBean> handlerData(List<DayHotBean> data){
        List<DayHotBean> dayHotBeans = new ArrayList<>();
        String lastTime = "";
        if(null != data && data.size()>0){
            //通过时间判断是今天的商品不显示时间分线
            for (int i = 0; i < data.size(); i++) {
                DayHotBean item = data.get(i);
                if(!lastTime.equals(DateTimeUtils.getYMDTime(item.getSendTime()))){
                    //lastTime = DateTimeUtils.getDatetoString(item.getSendTime());
                    String isTodayTime = DateTimeUtils.getDatetoString(item.getSendTime());
                    if(!isTodayTime.equals("今天")){
                        String currentTime = DateTimeUtils.getYMDTime(item.getSendTime());
                        if(!currentTime.equals(lastTime)){
                            lastTime = currentTime;
                            DayHotBean timeLineBean = new DayHotBean();
                            timeLineBean.setGoodsId("-1");
                            timeLineBean.setSendTime(item.getSendTime());
                            dayHotBeans.add(timeLineBean);
                        }

                    }

                }
                dayHotBeans.add(item);
            }
        }
//        DayHotBean timeLineBean = new DayHotBean();
//        timeLineBean.setGoodsId("-2");
//        dayHotBeans.add(timeLineBean);

        return dayHotBeans;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkNoticationEnable();
    }

    private void checkNoticationEnable(){
        boolean isshow = (boolean) SharedPreferencesUtils.get(App.getAppContext(), C.sp.isShowMsgNotication, true);
        if(!NotificationsUtils.isNotificationEnabled(getActivity()) && isshow){
            //显示消息通知提示
            msgNoticationRl.setVisibility(View.VISIBLE);
        }else{
            SharedPreferencesUtils.put(App.getAppContext(), C.sp.isShowMsgNotication, false);
            msgNoticationRl.setVisibility(View.GONE);
        }
    }
}
