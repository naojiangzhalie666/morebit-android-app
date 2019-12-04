package com.zjzy.morebit.info.ui.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.zjzy.morebit.Activity.ConsComGoodsDeailListActivity;
import com.zjzy.morebit.Module.common.Dialog.WithdrawErrorDialog;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.MineFragment;
import com.zjzy.morebit.info.contract.EarningsContract;
import com.zjzy.morebit.info.presenter.EarningsPresenter;
import com.zjzy.morebit.interfaces.EarningBalanceCallback;
import com.zjzy.morebit.main.model.ConfigModel;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.DayEarnings;
import com.zjzy.morebit.pojo.HotKeywords;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.view.ChildViewPager;
import com.zjzy.morebit.view.ConsCommissionRuleDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by YangBoTian on 2018/12/25.
 */

public class EarningsFragment extends MvpFragment<EarningsPresenter> implements EarningsContract.View {

    @BindView(R.id.tv_rule)
    TextView tv_rule;
    @BindView(R.id.balance)
    TextView balance;
    @BindView(R.id.swipeList)
    SwipeRefreshLayout swipeList;
    @BindView(R.id.status_bar)
    View status_bar;
    @BindView(R.id.orderTabLayout)
    SlidingTabLayout mTablayout;
    @BindView(R.id.orderViewPager)
    ChildViewPager mViewPager;
    @BindView(R.id.withDrawTimeTv)
    TextView withDrawTimeTv;
    private CategoryAdapter mAdapter;
    private ConsCommissionRuleDialog consCommissionRuleDialog;
    private String mTotalMoney = "";
//    String[] tagTitle = new String[]{"淘宝","苏宁","其他"};
    String[] tagTitle = new String[]{"淘宝","优选"};
    private List<EarningDetailFragment> fragments = null;
    private int currentTab = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
//        SystemConfigBean systemConfigBean = ConfigListUtlis.getSystemConfigBean(C.ConfigKey.WEB_WITHDRAW_TIME);
//        if(null != systemConfigBean && C.ConfigKey.WEB_WITHDRAW_TIME.equals(systemConfigBean.getSysKey()) && !TextUtils.isEmpty(systemConfigBean.getSysValue())){
//            withDrawTimeTv.setText(getString(R.string.withdraw_time,systemConfigBean.getSysValue()));
//        }
        withDrawTimeTv.setText("每月25~31号可提现上月结算收益");
    }

    @Override
    protected void initView(View view) {
        initBundle();
        EarningDetailFragment taobaoFragment = EarningDetailFragment.newInstance(C.OrderType.TAOBAO);
        taobaoFragment.setEarningBalanceCallback(callback);
        EarningDetailFragment yuxuanFragment = EarningDetailFragment.newInstance(C.OrderType.YUXUAN);
        yuxuanFragment.setEarningBalanceCallback(callback);
//        EarningDetailFragment otherFragment = EarningDetailFragment.newInstance(C.OrderType.OTHER);
//        otherFragment.setEarningBalanceCallback(callback);
        fragments = new ArrayList<>();
        fragments.add(taobaoFragment);
        fragments.add(yuxuanFragment);
//        fragments.add(otherFragment);
        swipeList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               if(null != mAdapter){
                   mAdapter.update(currentTab);
               }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //处理全屏显示
            ViewGroup.LayoutParams viewParams = status_bar.getLayoutParams();
            viewParams.height = ActivityStyleUtil.getStatusBarHeight(getActivity());
            status_bar.setLayoutParams(viewParams);
        }
        tv_rule.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_rule.getPaint().setAntiAlias(true);//抗锯齿
        setupViewPager(tagTitle);

    }

    private void initBundle() {
        MineFragment.mMonthEarnings = null;
        if (MineFragment.mDayEarnings == null || MineFragment.mMonthEarnings == null)
            swipeList.setRefreshing(true);
        if(null != MineFragment.mDayEarnings){
            setDayEarnings(MineFragment.mDayEarnings);
        }
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




    @OnClick({R.id.bill_details, R.id.withdraw, R.id.back, R.id.tv_rule})
    public void onCLick(View v) {
        switch (v.getId()) {
            case R.id.bill_details:   //账单详情
                Intent communityIt = new Intent(getActivity(), ConsComGoodsDeailListActivity.class);
                Bundle communityBundle = new Bundle();
                communityBundle.putString("title", "账单详情");
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
            case R.id.back:   //箭头退出
                getActivity().finish();
                break;
            case R.id.tv_rule:    //规则说明
                getRule();
                break;

        }
    }



    public void setDayEarnings(DayEarnings earnings) {
        if (earnings == null) {
            return;
        }
        MineFragment.mDayEarnings = earnings;
        mTotalMoney = MathUtils.getMoney(earnings.getTotalMoney());
        balance.setText(getString(R.string.income, mTotalMoney));

    }



    private void getRule() {
        if (consCommissionRuleDialog == null) {
            ConfigModel.getInstance().getConfigForKey((RxAppCompatActivity) getActivity(), C.SysConfig.WEB_WALLET_RULE)

                    .subscribe(new DataObserver<HotKeywords>() {
                        @Override
                        protected void onSuccess(HotKeywords data) {
                            String sysValue = data.getSysValue();
                            if (!TextUtils.isEmpty(sysValue)) {
                                consCommissionRuleDialog = new ConsCommissionRuleDialog(getActivity(), R.style.dialog, "规则说明", sysValue, null);
                                consCommissionRuleDialog.show();
                            }
                        }
                    });
        } else {
            consCommissionRuleDialog.show();
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






    private void setupViewPager(String[] tagTitle) {

        mAdapter = new CategoryAdapter(getChildFragmentManager(), tagTitle);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mAdapter);
        mTablayout.setViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentTab = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private class CategoryAdapter extends FragmentPagerAdapter {
        private final String[] homeColumns;
        private List<String> tagList = new ArrayList<String>();
        private FragmentManager mFragmentManager;

        public CategoryAdapter(FragmentManager fragmentManager, String[] datas) {
            super(fragmentManager);
            this.mFragmentManager = fragmentManager;
            this.homeColumns = datas;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            tagList.add(makeFragmentName(container.getId(), getItemId(position)));
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object){
            super.destroyItem(container, position, object);
            tagList.remove(makeFragmentName(container.getId(), getItemId(position)));
        }

        private  String makeFragmentName(int viewId, long id) {
            return "android:switcher:" + viewId + ":" + id;
        }

        public void update(int position){
            Fragment fragment = mFragmentManager.findFragmentByTag(tagList.get(position));
            if(fragment == null){
                return;
            }
            if(fragment instanceof EarningDetailFragment){
                EarningDetailFragment earningDetailFragment = (EarningDetailFragment) fragment;
                earningDetailFragment.refresh();
            }
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return homeColumns.length;
        }



        @Override
        public CharSequence getPageTitle(int position) {
            return homeColumns[position];
        }

    }

    EarningBalanceCallback callback = new EarningBalanceCallback() {
        @Override
        public void getBalance(DayEarnings earnings) {
            if(null != earnings){
                setDayEarnings(earnings);
            }
        }

        @Override
        public void refreshComplete() {
            swipeList.setRefreshing(false);
        }
    };
}
