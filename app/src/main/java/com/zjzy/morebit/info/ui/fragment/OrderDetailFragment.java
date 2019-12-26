package com.zjzy.morebit.info.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.info.ui.PopWindow.SwitchOrderTypePopWindow;
import com.zjzy.morebit.order.contract.OrderDetailContract;
import com.zjzy.morebit.pojo.event.OrderLoadDataEvent;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.view.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by haipingliu on 2019/12/25.
 */

public class OrderDetailFragment extends BaseFragment {

    @BindView(R.id.xTablayout)
    SlidingTabLayout mTablayout;

    @BindView(R.id.viewpager)
    NoScrollViewPager mViewPager;

    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    private OrderAdapter adapter;

    private int mTeamType =1;
//    private SwitchOrderTypePopWindow mPopWindow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }

    @OnClick({R.id.search, R.id.back})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.search:
                Bundle bundle = new Bundle();
                bundle.putInt(C.Extras.order_type,mTeamType);
                OpenFragmentUtils.goToSimpleFragment(getActivity(), SearchOrderFragment.class.getName(), bundle);
                break;
            case R.id.back:
                getActivity().finish();
                break;
//            case R.id.iv_title:
//                if (mPopWindow == null) {
//                    mPopWindow = new SwitchOrderTypePopWindow(getActivity(), new MyAction.One<String>() {
//                        @Override
//                        public void invoke(String arg) {
//                            if (!TextUtils.isEmpty(arg)) {
//                                if (getActivity().getString(R.string.order_team_tb).equals(arg)) {
//                                    mTeamType = 1;
//                                    mTablayout.setViewPager(mViewPager);
//                                } else if (getActivity().getString(R.string.order_team_other).equals(arg)) {
//                                    mTeamType = 2;
//                                }  else if (getActivity().getString(R.string.order_team_sl).equals(arg)) {
//                                    mTeamType = 3;
//                                } else if (getActivity().getString(R.string.order_team_yx).equals(arg)) {
//                                    mTeamType = 10;
//
//                                    mTablayout.setViewPager(mViewPager);
//                                }else {
//                                    mTeamType = 1;
//                                }
//                                EventBus.getDefault().post(new OrderLoadDataEvent(mTeamType));
//                                mTvTitle.setText(arg);
//                            }
//                        }
//                    });
//                }
//                mPopWindow.showAsDropDown(mRlTitle);
//                break;
            default:
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }



    private void initView() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            mTeamType = bundle.getInt(C.Extras.order_type, 1);
        }
        List<String> columns = new ArrayList<>();
        String[] titles ;
        if (mTeamType == 1){
            mTvTitle.setText("淘宝订单");
            titles = getResources().getStringArray(R.array.taobao_order_type);
            mTablayout.setTabPadding(25);
        }else{
            mTvTitle.setText("优选订单");
            titles = getResources().getStringArray(R.array.self_order_type);
            mTablayout.setTabPadding(15);
        }

        for (int i = 0; i < titles.length; i++) {
            columns.add(titles[i]);
        }
        setupViewPager(columns);

    }

    private void setupViewPager(final List<String> homeColumns) {
        adapter = new OrderAdapter(getChildFragmentManager(), homeColumns);
        mViewPager.setAdapter(adapter);
        mTablayout.setViewPager(mViewPager);

    }

    private class OrderAdapter extends FragmentStatePagerAdapter {
        private final List<String> homeColumns;

        public OrderAdapter(FragmentManager fragmentManager, List<String> homeColumns) {
            super(fragmentManager);
            this.homeColumns = homeColumns;
        }

        //订单状态,1.付款; 3.已结算; 4.已失效 ;5.全部
        @Override
        public Fragment getItem(int position) {
            if (getString(R.string.order_all).equals(homeColumns.get(position))) {
                return OrderListFragment.newInstance(5, mTeamType);
            } else if (getString(R.string.order_wait_pay).equals(homeColumns.get(position))) {
                return OrderListFragment.newInstance(2, mTeamType);
            } else if (getString(R.string.order_play).equals(homeColumns.get(position))) {
                return OrderListFragment.newInstance(1, mTeamType);
            } else if (getString(R.string.order_close).equals(homeColumns.get(position))) {
                return OrderListFragment.newInstance(3, mTeamType);
            } else if (getString(R.string.order_invalid).equals(homeColumns.get(position))
            || (getString(R.string.order_closed).equals(homeColumns.get(position)))) {
                return OrderListFragment.newInstance(4, mTeamType);
            } else {
                return new Fragment();
            }

        }

        @Override
        public int getCount() {
            return homeColumns.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = homeColumns.get(position);

//            if (mTeamType == 10 && title.equals("已失效")){
//                return "已关闭";
//            }
            return title;
        }

    }
}
