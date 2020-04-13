package com.zjzy.morebit.circle.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.youth.banner.Banner;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.contract.CommercialCollegeContract;
import com.zjzy.morebit.circle.presenter.CommercialCollegePresenter;
import com.zjzy.morebit.fragment.CircleDayHotFragment;
import com.zjzy.morebit.fragment.CircleSearchFragment;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.CategoryListChildDtos;
import com.zjzy.morebit.pojo.CollegeHome;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MarkermallInformation;
import com.zjzy.morebit.pojo.StudyRank;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.view.AspectRatioView;
import com.zjzy.morebit.view.CollegeCategoryPopWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangBoTian on 2019/6/11.
 * 商学院二级分类
 */

public class CircleBusinessFragment extends MvpFragment<CommercialCollegePresenter> implements CommercialCollegeContract.View , View.OnClickListener {
    SlidingTabLayout mTablayout;
    ViewPager mViewPager;
    private AppCompatImageButton more_category;
    private LinearLayout categoryLayout;
    private CategoryAdapter mAdapter;
    private View mView;
    private List<StudyRank> mChilds=new ArrayList<>();
    private int mTwoLevelId;
    private String mMainTitle;
    private CollegeCategoryPopWindow popWindow;
    List<String> homeColumns;
    View  searchLayout;
    private Banner mBanner;
    private AspectRatioView rsv_banner;
    public static CircleBusinessFragment newInstance(List<CategoryListChildDtos> child, int oneLevelId, String mainTitle) {
        Bundle args = new Bundle();
        args.putSerializable("mChild", (ArrayList) child);
        args.putInt("oneLevelId", oneLevelId);
        args.putString("mainTitle", mainTitle);
        CircleBusinessFragment fragment = new CircleBusinessFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
////        ViewGroup parent = (ViewGroup) mView.getParent();
////        if (parent != null) {
////            parent.removeView(mView);
////        }
//
//
//        return mView;
//    }

    @Override
    protected void initData() {
        mPresenter.getStudyRank(this);
        mPresenter.getCommercialBanner(this); //获取轮播图
    }

    @Override
    protected void initView(View view) {

            iniView(view);

    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_business_category;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    private void iniView(View view) {
        MyLog.i("test", "initView");

        categoryLayout = view.findViewById(R.id.categoryLayout);
        mTablayout = view.findViewById(R.id.tablayout);
        mViewPager = view.findViewById(R.id.viewpager);
        more_category = view.findViewById(R.id.more_category);
        searchLayout = view.findViewById(R.id.searchLayout);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchArticleListActitivty.start(getActivity());
            }
        });
        more_category.setOnClickListener(this);

        mBanner = view.findViewById(R.id.banner);//轮播图
        rsv_banner=view.findViewById(R.id.rsv_banner);


    }




    private void setupViewPager(List<StudyRank> data) {

        mAdapter = new CategoryAdapter(getChildFragmentManager(), data);
        mViewPager.setAdapter(mAdapter);
        mTablayout.setViewPager(mViewPager);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more_category:
                if(null == popWindow){
                    popWindow = new CollegeCategoryPopWindow(getActivity(), homeColumns, new MyAction.One<Integer>() {
                        @Override
                        public void invoke(Integer arg) {
                            if(arg == -1){
                                more_category.setImageResource(R.drawable.icon_circle_category_down);
                                popWindow = null;
                            }else{
                                popWindow.dismiss();
                                mViewPager.setCurrentItem(arg);
                            }

                        }
                    });
                    popWindow.showAsDropDown(mTablayout);
                    more_category.setImageResource(R.drawable.icon_circle_category_up);
                }
                break;
        }
    }

    @Override
    public void onBannerSuccessful(List<ImageInfo> data) {
        if (data!=null){
            rsv_banner.setVisibility(View.VISIBLE);
            BannerInitiateUtils.setBrandBanner(getActivity(), data, mBanner);
        }else{
            rsv_banner.setVisibility(View.GONE);
        }

    }

    @Override
    public void onStudyRankSuccessful(List<StudyRank> data) {
        mChilds.addAll(data);
        homeColumns = new ArrayList<>();
        if(null != mChilds && mChilds.size() > 4){
            categoryLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < mChilds.size(); i++) {
                homeColumns.add(mChilds.get(i).getModelName());
            }
        }else{
            categoryLayout.setVisibility(View.GONE);
        }
        if(mChilds !=null){
            setupViewPager(mChilds);
        }

    }

    @Override
    public void onStudyRankEmpty() {

    }

    @Override
    public void onArticleSuccessful(List<CollegeHome> data) {

    }

    @Override
    public void onArticleEmpty() {

    }

    @Override
    public void onArticleFinally() {

    }

    @Override
    public void onCarouselSuc(List<MarkermallInformation> data) {

    }

    private class CategoryAdapter extends FragmentPagerAdapter {
        private final List<StudyRank> homeColumns;

        public CategoryAdapter(FragmentManager fragmentManager, List<StudyRank> datas) {
            super(fragmentManager);
            this.homeColumns = datas;
        }

        @Override
        public Fragment getItem(int position) {
            StudyRank imageInfo = homeColumns.get(position);
            return TutorialFragment.newInstance(Integer.valueOf(imageInfo.getId()),0);
        }

        @Override
        public int getCount() {
            return homeColumns.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            MyLog.i("test", "this: " + this);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return homeColumns.get(position).getModelName();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MyLog.i("test", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLog.i("test", "onDestroy");
    }
}
