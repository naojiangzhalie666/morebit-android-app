package com.jf.my.info.ui.fragment;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jf.my.Activity.FansListFragment;
import com.jf.my.Module.common.Fragment.BaseFragment;
import com.jf.my.R;
import com.jf.my.fragment.MyTeamListFragment;
import com.jf.my.info.ui.FansActivity;
import com.jf.my.utils.MyLog;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Dell on 2019/8/31.
 */

public class DynamicRankFragment extends BaseFragment {
    private static final String ASC = "asc";  //正序
    private static final String DESC = "desc";  //倒序
    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private String[] mTitles = {"活跃时间", "近七日预估佣金", "近七日拉新"};
    private List<MyTeamListFragment> mFragments = new ArrayList<>();
    DynamicRankAdapter mAdapter;

    private int curPosition = 0;
    private boolean isSelect = true;

    public static DynamicRankFragment newInstance() {
        DynamicRankFragment fragment = new DynamicRankFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic_rank, container, false);
//        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        setupViewPager();
    }

    private void setupViewPager() {
//        FragmentPagerItems pages = new FragmentPagerItems(getActivity());
//        for (String titleResId : mTitles) {
//            pages.add(FragmentPagerItem.of(titleResId, MsgFansFragment.class));
//
//        }
//        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
//                getChildFragmentManager(), pages);
        mFragments.clear();
        for (int i = 0; i < mTitles.length; i++) {
            if (i == 0) {
                MyTeamListFragment fragment = MyTeamListFragment.newInstance(MyTeamListFragment.TYPE_DYNAMIC_RANK_ACTIVITY, MyTeamListFragment.TYPE_DYNAMIC_RANK);
                fragment.setOrder(DESC);
                mFragments.add(fragment);
            } else if (i == 1) {
                MyTeamListFragment fragment = MyTeamListFragment.newInstance(MyTeamListFragment.TYPE_DYNAMIC_RANK_COMMISSION, MyTeamListFragment.TYPE_DYNAMIC_RANK);
                mFragments.add(fragment);
            } else if (i == 2) {
                MyTeamListFragment fragment = MyTeamListFragment.newInstance(MyTeamListFragment.TYPE_DYNAMIC_RANK_NEW, MyTeamListFragment.TYPE_DYNAMIC_RANK);
                mFragments.add(fragment);
            }
        }
        mAdapter = new DynamicRankAdapter(getChildFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewpagertab.setViewPager(viewPager);
        viewpagertab.setOnTabClickListener(new SmartTabLayout.OnTabClickListener() {
            @Override
            public void onTabClicked(int position) {
                if (curPosition == position) {
                    if (isSelect) {
                        View view = viewpagertab.getTabAt(position);
                        ImageView imageView = view.findViewById(R.id.iv_sort);
                        imageView.setImageResource(R.drawable.icon_sort_up);
                        isSelect = false;
                        mFragments.get(position).setOrder(ASC);
                        mFragments.get(position).getRankFirstData(ASC);
                    } else {
                        View view = viewpagertab.getTabAt(position);
                        ImageView imageView = view.findViewById(R.id.iv_sort);
                        imageView.setImageResource(R.drawable.icon_sort_down);
                        isSelect = true;
                        mFragments.get(position).setOrder(DESC);
                        mFragments.get(position).getRankFirstData(DESC);
                    }
                } else {
                    curPosition = position;
                    isSelect = true;
                    for (int i = 0; i < mTitles.length; i++) {
                        View view = viewpagertab.getTabAt(i);
                        ImageView imageView = view.findViewById(R.id.iv_sort);
                        imageView.setImageResource(R.drawable.icon_sort_default);
                    }
                    View view = viewpagertab.getTabAt(position);
                    ImageView imageView = view.findViewById(R.id.iv_sort);
                    imageView.setImageResource(R.drawable.icon_sort_down);
                    mFragments.get(position).setOrder(DESC);
                    mFragments.get(position).getRankFirstData(DESC);
                }

            }
        });
        View view = viewpagertab.getTabAt(0);
        ImageView imageView = view.findViewById(R.id.iv_sort);
        imageView.setImageResource(R.drawable.icon_sort_down);

    }

    private class DynamicRankAdapter extends FragmentStatePagerAdapter {


        public DynamicRankAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

    }
}
