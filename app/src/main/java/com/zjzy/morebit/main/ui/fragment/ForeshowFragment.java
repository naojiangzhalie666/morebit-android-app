package com.zjzy.morebit.main.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.utils.C;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 首页分类-热门排行
 */
public class ForeshowFragment extends BaseMainFragmeng {
    @BindView(R.id.xTablayout)
    SegmentTabLayout mTablayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private ShoppingListFragment3 mGoodsFragment;
    private ShoppingListFragment3 mGoodsFragment1;
    private ShoppingListFragment3 mGoodsFragment2;
    private View mView;
    List<BaseFragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"火热开抢中", "隔夜单", "活动即将开始"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_foreshow, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(mView);
    }


    /**
     * 初始化界面
     */
    private void initView(View view) {
//        mGoodsFragment = ShoppingListFragment2.newInstance(C.GoodsListType.ForeShow_2);
//        mCirclePagerBaens.add(new CirclePagerBaen(mGoodsFragment, getResources().getString(R.string.foreshow1)));
//        mGoodsFragment1 = ShoppingListFragment2.newInstance(C.GoodsListType.ForeShow_3);
//        mCirclePagerBaens.add(new CirclePagerBaen(mGoodsFragment1, getResources().getString(R.string.foreshow2)));
//        mGoodsFragment2 = ShoppingListFragment2.newInstance(C.GoodsListType.ForeShow_1);
//        mCirclePagerBaens.add(new CirclePagerBaen(mGoodsFragment2, getResources().getString(R.string.foreshow3)));
//        viewPager.setAdapter(new ForeshowAdapter(getChildFragmentManager()));
//        mTablayout.setupWithViewPager(viewPager);
        setupViewPager();
    }
    private void setupViewPager() {
        mGoodsFragment = ShoppingListFragment3.newInstance(C.GoodsListType.ForeShow_2);
        mFragments.add(mGoodsFragment);
//            mCirclePagerBaens.add(new CirclePagerBaen(mGoodsFragment, getResources().getString(R.string.main_circle_hotday)));
        mGoodsFragment1 = ShoppingListFragment3.newInstance(C.GoodsListType.ForeShow_3);
        mFragments.add(mGoodsFragment1);
//        mCirclePagerBaens.add(new CirclePagerBaen(mGoodsFragment1, getResources().getString(R.string.main_circle_adv)));
        mGoodsFragment2 = ShoppingListFragment3.newInstance(C.GoodsListType.ForeShow_1);
        mFragments.add(mGoodsFragment2);
//        mCirclePagerBaens.add(new CirclePagerBaen(mCommercialCollegeFragment, getResources().getString(R.string.main_commercial_college)));
        viewPager.setAdapter(new ForeshowAdapter(getChildFragmentManager()));
        mTablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTablayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });;
        mTablayout.setTabData(mTitles);



    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        getActivity().finish();
    }


    private class ForeshowAdapter extends FragmentPagerAdapter {

        public ForeshowAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            CirclePagerBaen homeColumn = mCirclePagerBaens.get(position);
            return mTitles[position];
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
