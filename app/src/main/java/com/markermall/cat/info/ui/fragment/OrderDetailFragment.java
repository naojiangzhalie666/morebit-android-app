package com.markermall.cat.info.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.markermall.cat.Module.common.Fragment.BaseFragment;
import com.markermall.cat.R;
import com.markermall.cat.info.ui.PopWindow.SwitchOrderTypePopWindow;
import com.markermall.cat.pojo.event.OrderLoadDataEvent;
import com.markermall.cat.utils.C;
import com.markermall.cat.utils.OpenFragmentUtils;
import com.markermall.cat.utils.action.MyAction;
import com.markermall.cat.view.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by YangBoTian on 2018/9/12.
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
    @BindView(R.id.iv_title)
    ImageView mIvTitle;

    private OrderAdapter adapter;

    private int mTeamType =1;
    private SwitchOrderTypePopWindow mPopWindow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }

    @OnClick({R.id.search, R.id.back, R.id.tv_title, R.id.iv_title})
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
            case R.id.tv_title:
            case R.id.iv_title:
                if (mPopWindow == null) {
                    mPopWindow = new SwitchOrderTypePopWindow(getActivity(), new MyAction.One<String>() {
                        @Override
                        public void invoke(String arg) {
                            if (!TextUtils.isEmpty(arg)) {
                                if (getActivity().getString(R.string.order_team_tb).equals(arg)) {
                                    mTeamType = 1;
                                } else if (getActivity().getString(R.string.order_team_other).equals(arg)) {
                                    mTeamType = 2;
                                }  else if (getActivity().getString(R.string.order_team_sl).equals(arg)) {
                                    mTeamType = 3;
                                } else {
                                    mTeamType = 1;
                                }
                                EventBus.getDefault().post(new OrderLoadDataEvent(mTeamType));
                                mTvTitle.setText(arg);
                            }
                        }
                    });
                }
                mPopWindow.showAsDropDown(mRlTitle);
                break;
            default:
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }



    private void initView() {
        List<String> columns = new ArrayList<>();
        String[] titles = getResources().getStringArray(R.array.order_type);
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
            } else if (getString(R.string.order_play).equals(homeColumns.get(position))) {
                return OrderListFragment.newInstance(1, mTeamType);
            } else if (getString(R.string.order_close).equals(homeColumns.get(position))) {
                return OrderListFragment.newInstance(3, mTeamType);
            } else if (getString(R.string.order_invalid).equals(homeColumns.get(position))) {
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
            ;
            return homeColumns.get(position);
        }

    }
}
