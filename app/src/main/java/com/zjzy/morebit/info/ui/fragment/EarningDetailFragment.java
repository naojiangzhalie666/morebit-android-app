package com.zjzy.morebit.info.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjzy.morebit.Module.common.Dialog.EarningsHintNewsDialog;
import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.MineFragment;
import com.zjzy.morebit.info.contract.EarningsDetailContract;
import com.zjzy.morebit.info.presenter.EarningsDetailPresenter;
import com.zjzy.morebit.interfaces.EarningBalanceCallback;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.DayEarnings;
import com.zjzy.morebit.pojo.EarningExplainBean;
import com.zjzy.morebit.pojo.MonthEarnings;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MathUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * - @Description:  淘宝，京东，其他收入预估
 * - @Author:
 * - @Time:  2019/8/31 10:55
 **/
public class EarningDetailFragment  extends MvpFragment<EarningsDetailPresenter> implements EarningsDetailContract.View {
    @BindView(R.id.tv_today_money)
    TextView tv_today_money;
    @BindView(R.id.tv_today_estimate_money)
    TextView tv_today_estimate_money;
    @BindView(R.id.tv_today_paymen_total)
    TextView tv_today_paymen_total;
    @BindView(R.id.tv_yesterday_money)
    TextView tv_yesterday_money;
    @BindView(R.id.tv_yesterday_estimate_money)
    TextView tv_yesterday_estimate_money;
    @BindView(R.id.tv_yesterday_payment_total)
    TextView tv_yesterday_payment_total;
    @BindView(R.id.tv_month_estimate_money)
    TextView tv_month_estimate_money;
    @BindView(R.id.tv_month_money)
    TextView tv_month_money;
    @BindView(R.id.tv_last__month_estimate_money)
    TextView tv_last__month_estimate_money;
    @BindView(R.id.tv_last_month_money)
    TextView tv_last_month_money;
    private View mView;
    EarningsHintNewsDialog mMonthdialog;
    EarningsHintNewsDialog mDayialog;

    private int orderType = 0;
    EarningExplainBean mExplainData;
    EarningBalanceCallback earningBalanceCallback ;
    public static EarningDetailFragment newInstance(int orderType) {
        Bundle args = new Bundle();
        args.putInt("orderType", orderType);
        EarningDetailFragment fragment = new EarningDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setEarningBalanceCallback(EarningBalanceCallback earningBalanceCallback) {
        this.earningBalanceCallback = earningBalanceCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_earning_report, container, false);
        ButterKnife.bind(this, mView);
        initView(mView);
        return mView;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            orderType = bundle.getInt("orderType");
        }

        refresh();
        if(null == mExplainData){
            mPresenter.getEarningsExplain(this);
        }

    }

    public void refresh(){
        if(orderType == C.OrderType.TAOBAO){
            initTaobaoData();
        }else{
            getData();
        }
    }


    @Override
    protected void initView(View view) {

    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_earning_report;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    private void getData() {
         mPresenter.getPlatformDayIncome(this,orderType);
         mPresenter.getPlatformMonthIncome(this,orderType);
    }



    private void initTaobaoData() {
        MineFragment.mMonthEarnings = null;
        if (MineFragment.mDayEarnings == null || MineFragment.mMonthEarnings == null)
            getData();
        if (MineFragment.mDayEarnings == null)
            mPresenter.getPlatformDayIncome(this,orderType);
        else
            setDayEarnings(MineFragment.mDayEarnings);

        if (MineFragment.mMonthEarnings == null)
            mPresenter.getPlatformMonthIncome(this,orderType);
        else
            setMonthEarnings(MineFragment.mMonthEarnings);


    }


    private  void setDayEarnings(DayEarnings earnings) {
        if (earnings == null) {
            return;
        }
        MineFragment.mDayEarnings = earnings;
        //今日
        tv_today_estimate_money.setText(getString(R.string.income, MathUtils.getMoney(earnings.getTodayEstimateMoney())));
        tv_today_paymen_total.setText(earnings.getTodayPamentTotal());
        tv_today_money.setText(getString(R.string.income, MathUtils.getMoney(earnings.getTodayMoney())));
        //昨天
        tv_yesterday_money.setText(getString(R.string.income, MathUtils.getMoney(earnings.getYesterdayMoney())));
        tv_yesterday_payment_total.setText(earnings.getYestredayPaymentTotal());
        tv_yesterday_estimate_money.setText(getString(R.string.income, MathUtils.getMoney(earnings.getYesterdayEstimateMoney())));
    }

    private  void setMonthEarnings(MonthEarnings earnings) {
        if (earnings == null) {
            return;
        }
        MineFragment.mMonthEarnings = earnings;
        //本月
        tv_month_estimate_money.setText(getString(R.string.income, MathUtils.getMoney(earnings.getThisMonthEstimateMoney())));
        tv_month_money.setText(getString(R.string.income, MathUtils.getMoney(earnings.getThisMonthMoney())));
        //上月
        tv_last_month_money.setText(getString(R.string.income, MathUtils.getMoney(earnings.getPrevMonthMoney())));
        tv_last__month_estimate_money.setText(getString(R.string.income, MathUtils.getMoney(earnings.getPrevMonthEstimateMoney())));


    }


    @OnClick({R.id.tv_day_hint,R.id.tv_month_hint})
    public void onCLick(View v) {
        switch (v.getId()) {
            case R.id.tv_day_hint:
                openDaydialog();
                break;
            case R.id.tv_month_hint:

                openMonthdialog();
                break;
        }
    }


    private void openMonthdialog( ) {  //月预估说明dialog
        if (mMonthdialog == null) {
            mMonthdialog = new EarningsHintNewsDialog(getActivity());
        }

        if (!mMonthdialog.isShowing()) {
            mMonthdialog.show(1, mExplainData);
        }
    }

    private void openDaydialog() {  //日预估说明dialog
        if (mDayialog == null) {
            mDayialog = new EarningsHintNewsDialog(getActivity());
        }

        if (!mDayialog.isShowing()) {
            mDayialog.show(0, mExplainData);
        }
    }

    @Override
    public void onIncomeFinally() {
        if(null != this.earningBalanceCallback){
            this.earningBalanceCallback.refreshComplete();
        }
    }

    @Override
    public void onPlatformDayIncomeSuccessful(DayEarnings data, int type) {
            if(null != data){
                setDayEarnings(data);
                if(null != earningBalanceCallback){
                    earningBalanceCallback.getBalance(data);
                }
            }
    }

    @Override
    public void onPlatformMonthIncomeSuccessful(MonthEarnings data, int type) {
        if(null != data){
            setMonthEarnings(data);
        }
    }

    @Override
    public void onExplainSuccess(EarningExplainBean data) {
        if (null != data) {
            mExplainData = data;
        }
    }


}
