package com.markermall.cat.main.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.markermall.cat.App;
import com.markermall.cat.R;
import com.markermall.cat.fragment.base.BaseMainFragmeng;
import com.markermall.cat.pojo.RankingTitleBean;
import com.markermall.cat.utils.C;
import com.markermall.cat.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 首页分类-热门排行
 */
public class RankingChildFragment extends BaseMainFragmeng {

    @BindView(R.id.tab)
    SlidingTabLayout tablayout;
    @BindView(R.id.cv_tab)
    CardView cv_tab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    boolean isUserHint =true;
    private ArrayList<CirclePagerBaen> mCirclePagerBaens = new ArrayList<>();
    private View mView;
    private int mPushType;

    /**
     * 1、实时排行/2、今日排行
     *
     * @param type
     * @return
     */
    public static RankingChildFragment newInstance(int type) {
        RankingChildFragment rankingChildFragment = new RankingChildFragment();
        Bundle args = new Bundle();
        args.putInt(C.Extras.pushType, type);
        rankingChildFragment.setArguments(args);
        return rankingChildFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_ranking_child, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPushType = getArguments().getInt(C.Extras.pushType);
        if (mPushType == 1) {
            initView(mView);
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyLog.d("setUserVisibleHint", "CircleFragment  " + isVisibleToUser);
        if (isVisibleToUser && isUserHint && mView != null&&mPushType == 2) {
            initView(mView);
            isUserHint = false;
        }

    }
    /**
     * 初始化界面
     */
    private void initView(View view) {
        List<RankingTitleBean> data = (List<RankingTitleBean>) App.getACache().getAsObject(C.sp.RANKING_CATEGORY);
        if (data != null && data.size() != 0) {
            initTab(data);
        } else {
            cv_tab.setVisibility(View.GONE);
        }

    }

    private void initTab(List<RankingTitleBean> data) {
        cv_tab.setVisibility(View.VISIBLE);
        for (int i = 0; i < data.size(); i++) {
            RankingTitleBean rankingTitleBean = data.get(i);
            rankingTitleBean.setType(mPushType);
            RankingListFragment rankingListFragment = RankingListFragment.newInstance(rankingTitleBean);
            CirclePagerBaen baen = new CirclePagerBaen(rankingListFragment, rankingTitleBean.getCname());
            mCirclePagerBaens.add(baen);
        }
        viewPager.setAdapter(new ChannelAdapter(getChildFragmentManager()));
        tablayout.setViewPager(viewPager);
    }

    private class ChannelAdapter extends FragmentStatePagerAdapter {

        public ChannelAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mCirclePagerBaens.get(position).title;
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
