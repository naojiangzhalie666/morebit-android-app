package com.zjzy.morebit.info.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zjzy.morebit.Activity.ConsComGoodsDeailListActivity;
import com.zjzy.morebit.Activity.MonthAgoActivity;
import com.zjzy.morebit.Module.common.Dialog.WithdrawErrorDialog;

import com.zjzy.morebit.R;
import com.zjzy.morebit.info.contract.EarningsContract;
import com.zjzy.morebit.info.presenter.EarningsPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.UserIncomeDetail;
import com.zjzy.morebit.pojo.EarningExplainBean;
import com.zjzy.morebit.pojo.MonthEarnings;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by YangBoTian on 2018/12/25.
 */

public class EarningsFragment extends MvpFragment<EarningsPresenter> implements EarningsContract.View {

    @BindView(R.id.swipeList)
    SwipeRefreshLayout swipeList;
    @BindView(R.id.status_bar)
    View status_bar;

    @BindView(R.id.withDrawTimeTv)
    TextView withDrawTimeTv;
    @BindView(R.id.month_ago)
    TextView month_ago;

    private String mTotalMoney = "";

    private TextView withdrawable,cumulativereceipt,withdrawn,unassignedintegral,unsettledearnings,earningsforecasty,estimatedpointsy
            ,exclusivefansaddedy,newordinaryfansy,estimatedearningsm,estimatedpointsm,exclusivefansaddedm
            ,newordinaryfansm,tv_title,tv_dialy,tv_month;
    private UserIncomeDetail mdata;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        withDrawTimeTv.setText("每月25～31日可提现上月【确认收货】的收益");

        mPresenter.getUserIncomeDetail(this);


    }


    @Override
    protected void initView(View view) {




//        EarningDetailFragment otherFragment = EarningDetailFragment.newInstance(C.OrderType.OTHER);
//        otherFragment.setEarningBalanceCallback(callback);


//        fragments.add(otherFragment);
        swipeList.setEnabled(true);
        swipeList.setNestedScrollingEnabled(true);
        //设置进度View下拉的起始点和结束点，scale 是指设置是否需要放大或者缩小动画
        swipeList.setProgressViewOffset(true, -0, 100);
        //设置进度View下拉的结束点，scale 是指设置是否需要放大或者缩小动画
        swipeList.setProgressViewEndTarget(true, 180);
        //设置进度View的组合颜色，在手指上下滑时使用第一个颜色，在刷新中，会一个个颜色进行切换
        swipeList.setColorSchemeColors(Color.parseColor("#FF645B"));
        //设置触发刷新的距离
        swipeList.setDistanceToTriggerSync(200);
        swipeList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                swipeList.setRefreshing(false);
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //处理全屏显示
            ViewGroup.LayoutParams viewParams = status_bar.getLayoutParams();
            viewParams.height = ActivityStyleUtil.getStatusBarHeight(getActivity());
            status_bar.setLayoutParams(viewParams);
        }


        withdrawable=view.findViewById(R.id.withdrawable);//可提现
        cumulativereceipt=view.findViewById(R.id.cumulativereceipt);//累计到账
        withdrawn=view.findViewById(R.id.withdrawn);//已提现
        unsettledearnings=view.findViewById(R.id.unsettledearnings);//未结算收益
        unassignedintegral=view.findViewById(R.id.unassignedintegral);//未归属积分
        earningsforecasty=view.findViewById(R.id.earningsforecasty);//昨日预估收益
        estimatedpointsy=view.findViewById(R.id.estimatedpointsy);//昨日预估积分
        exclusivefansaddedy=view.findViewById(R.id.exclusivefansaddedy);//昨日专属粉丝新增
        newordinaryfansy=view.findViewById(R.id.newordinaryfansy);//昨日普通粉丝新增
        estimatedearningsm=view.findViewById(R.id.estimatedearningsm);//本月预估收益
        estimatedpointsm=view.findViewById(R.id.estimatedpointsm);//本月预估积分
        exclusivefansaddedm=view.findViewById(R.id.exclusivefansaddedm);//本月专属粉丝新增
        newordinaryfansm=view.findViewById(R.id.newordinaryfansm);//本月普通粉丝新增

         tv_title = view.findViewById(R.id.tv_title);
        tv_title.getPaint().setFakeBoldText(true);
        tv_dialy=view.findViewById(R.id.tv_dialy);
        tv_dialy.getPaint().setFakeBoldText(true);
        tv_month=view.findViewById(R.id.tv_month);
        tv_month.getPaint().setFakeBoldText(true);


    }




    @Override
    protected int getViewLayout() {
        return R.layout.fragment_earnings;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }




    @Override
    public void onIncomeFinally() {
        swipeList.setRefreshing(false);
    }

    @Override
    public void checkWithdrawTime() {
        AppUtil.gotoCashMoney(getActivity(), mTotalMoney);
    }

    @Override
    public void checkWithdrawTimeError(String errMsg, String errCode) {
        if (C.requestCode.dataNull.equals(errCode)) {  //因为成功的话data会为空，所以判断下
            AppUtil.gotoCashMoney(getActivity(), mTotalMoney);
        } else {
            withdrawErrorDialog(errMsg);
        }

    }

    @Override
    public void getDaySuccess(UserIncomeDetail data) {//我的收益
        if (data!=null){
            mdata=data;
            withdrawable.setText(data.getBalance()+"");
            cumulativereceipt.setText(data.getTotalIncome()+"");
            withdrawn.setText(data.getReceiveAmount()+"");
            unsettledearnings.setText(data.getUnsettleAmount()+"");
            unassignedintegral.setText(data.getUnsettleIntegral()+"");
            earningsforecasty.setText(data.getYesterdayEstimateMoney()+"");
            estimatedpointsy.setText(data.getYesterdayIntegral()+"");
            exclusivefansaddedy.setText(data.getYesterdayDirectUser()+"");
            newordinaryfansy.setText(data.getYesterdayIndirectUser()+"");
            estimatedearningsm.setText(data.getThisMonthEstimateMoney()+"");
            estimatedpointsm.setText(data.getThisMonthIntegral()+"");
            exclusivefansaddedm.setText(data.getThisMonthDirectUser()+"");
            newordinaryfansm.setText(data.getThisMonthIndirectUser()+"");
        }

    }

    @Override
    public void getDayError(String errMsg, String errCode) {

    }

    @Override
    public void onExplainSuccess(EarningExplainBean data) {

    }


    @OnClick({R.id.bill_details, R.id.withdraw, R.id.btn_back,R.id.month_ago})
    public void onCLick(View v) {
        switch (v.getId()) {
            case R.id.bill_details:   //账单详情
                Intent communityIt = new Intent(getActivity(), ConsComGoodsDeailListActivity.class);
                Bundle communityBundle = new Bundle();
                communityBundle.putString("title", "账单明细");
                communityBundle.putString("fragmentName", "ConsComDetailListFragment");
                communityIt.putExtras(communityBundle);
                startActivity(communityIt);
                break;
            case R.id.withdraw:   //提现
//                if (!UserLocalData.getUser(getActivity()).isNeedAuth()) {
//                    if (AppUtil.isFastClick(500)) return;
                mPresenter.checkWithdrawTime(this);
//                } else {
//                    TaobaoUtil.getAllianceAppKey((BaseActivity) getActivity());
//                }

                break;
            case R.id.btn_back:   //箭头退出
                getActivity().finish();
                break;
            case R.id.month_ago:    //上月月报
                Intent intent = new Intent(getActivity(), MonthAgoActivity.class);
                intent.putExtra(C.Extras.EARNING,mdata);
                startActivity(intent);
                break;

        }
    }








    WithdrawErrorDialog withdrawErrorDialog;

    private void withdrawErrorDialog(String title) {  //淘宝取消授权弹框
        if (withdrawErrorDialog == null) {
            withdrawErrorDialog = new WithdrawErrorDialog(getActivity(), R.style.dialog, "温馨提示", title);
            withdrawErrorDialog.setmOkListener(new WithdrawErrorDialog.OnOkListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

        if (!withdrawErrorDialog.isShowing()) {
            withdrawErrorDialog.show();
        }

    }










}
