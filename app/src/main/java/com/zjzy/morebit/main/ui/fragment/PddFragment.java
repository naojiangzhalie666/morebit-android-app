package com.zjzy.morebit.main.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.App;
import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.RankingTitleBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.C;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 首页拼多多
 */
public class PddFragment extends BaseMainFragmeng {
    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.view_bar)
    View view_bar;




    private ArrayList<CirclePagerBaen> mCirclePagerBaens = new ArrayList<>();
    private PddChildFragment mCircleDayHotFragment;
    private View mView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_pdd, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.with(getActivity())
                .titleBar(view_bar)    //解决状态栏和布局重叠问题，任选其一
                .init();

        initView(view);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        getActivity().finish();
    }

    /**
     * 初始化界面
     */
   private void initView(View view) {
        mCircleDayHotFragment = PddChildFragment.newInstance(1);
        mCirclePagerBaens.add(new CirclePagerBaen(mCircleDayHotFragment, getResources().getString(R.string.pdd_tab)));
        viewPager.setAdapter(new ChannelAdapter(getChildFragmentManager()));
        tab.setViewPager(viewPager);
    }


    private class ChannelAdapter extends FragmentStatePagerAdapter {


        public ChannelAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public CharSequence getPageTitle(int position) {
//            return mCirclePagerBaens.get(position).title;
            return "";
        }

        @Override
        public Fragment getItem(int position) {
            return mCirclePagerBaens.get(position).mFragment;
        }

        @Override
        public int getCount() {
            return mCirclePagerBaens.size();
        }
    }


    public class CirclePagerBaen {
        public Fragment mFragment;
        public String title;

        public CirclePagerBaen(Fragment fragment, String title) {
            mFragment = fragment;
            this.title = title;
        }

    }


}
