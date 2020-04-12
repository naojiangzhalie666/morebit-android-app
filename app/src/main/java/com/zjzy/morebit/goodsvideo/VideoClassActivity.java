package com.zjzy.morebit.goodsvideo;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goodsvideo.contract.VideoContract;
import com.zjzy.morebit.goodsvideo.presenter.VideoPresenter;
import com.zjzy.morebit.main.ui.myview.xtablayout.XTabLayout;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpActivity;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.VideoClassBean;

import java.util.ArrayList;
import java.util.List;

/*
 *
 * 视频分类页面
 * */
public class VideoClassActivity extends MvpActivity<VideoPresenter> implements VideoContract.View {

    private XTabLayout tabLayout;
    private ViewPager video_pager;
    private List<VideoFragment> mFragments;

    private NewsPagerAdapter mAdapter;

    private List<VideoClassBean> mNewsTypeList = new ArrayList<>();

    private List<String> title;
    private TextView txt_head_title;
    private LinearLayout btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_video_class);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(false)
                .statusBarColor(R.color.white)
                .init();
        mPresenter.getVideoClass(this);//获取抖货条目
        initData();
        initView();
        initViewPager();


    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_video_class;
    }

    private void initView() {

        tabLayout = (XTabLayout) findViewById(R.id.xTablayout);
        video_pager = (ViewPager) findViewById(R.id.video_pager);
        txt_head_title=(TextView)findViewById(R.id.txt_head_title);
        txt_head_title.setText("抖货");
        txt_head_title.setTextSize(18);
          btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    //测试数据
    private void initData() {


    }

    private void initViewPager() {


    }

    @Override
    public void onVideoClassSuccess(List<VideoClassBean> shopGoodInfo) {
        mNewsTypeList.addAll(shopGoodInfo);
        title = new ArrayList<>();
        for (int i = 0; i < shopGoodInfo.size(); i++) {
            title.add(shopGoodInfo.get(i).getTabName());
        }
        mFragments = new ArrayList<>();
        for (int i = 0; i < shopGoodInfo.size(); i++) {
            VideoFragment fragment = null;
            fragment = new VideoFragment();
            mFragments.add(fragment.newInstance(shopGoodInfo.get(i).getTabName(),shopGoodInfo.get(i).getTabNo()));
        }
        mAdapter = new NewsPagerAdapter(getSupportFragmentManager(), mFragments, mNewsTypeList,title);
        video_pager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(video_pager);
        video_pager.setOffscreenPageLimit(title.size());

    }

    @Override
    public void onVideoClassError(String throwable) {

    }

    @Override
    public void onVideoGoodsSuccess(List<ShopGoodInfo> shopGoodInfo) {

    }

    @Override
    public void onVideoGoodsError() {

    }

    public class NewsPagerAdapter extends FragmentPagerAdapter {
        private List<VideoFragment> mFragments;
        private List<VideoClassBean> mNewsTypes;
        private List<String> title;
        public NewsPagerAdapter(FragmentManager fm, List<VideoFragment> fragments, List<VideoClassBean> mewsTypes, List<String> stitle) {
            super(fm);
            mFragments = fragments;
            mNewsTypes = mewsTypes;
            title=stitle;
        }

        @Override
        public Fragment getItem(int position) {
            VideoClassBean videoClassBean = mNewsTypes.get(position);
           // VideoFragment sFragments = (VideoFragment) mFragments.get(position);
   //         List<Child2> childs = homeColumn.getChild2();
//                return CategoryListFragment.newInstance(homeColumn.getName(), childs);
//

//        VideoFragment.newInstance(mNewsTypes.get(position).getTabNo());

            return VideoFragment.newInstance(mNewsTypes.get(position).getTabName(),mNewsTypes.get(position).getTabNo());
        // return VideoFragment.newInstance(mNewsTypes.get(position).getTabNo());
        }

        @Override
        public int getCount() {
            return mFragments == null ? 0: mFragments.size();
        }


        //    为Tabayout设置主题名称
        @Override
        public CharSequence getPageTitle(int position) {
            return title == null ? "" + position : title.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);

        }


    }


//    private class HomeAdapter extends FragmentPagerAdapter {
//        private List<GoodCategoryInfo> mHomeColumns = new ArrayList<>();
//
//        public HomeAdapter(FragmentManager fragmentManager) {
//            super(fragmentManager);
//        }
//
//
//        public void setHomeColumns(List<GoodCategoryInfo> homeColumns) {
//            mHomeColumns.clear();
//            if (homeColumns == null && homeColumns.size() == 0) {
//                return;
//            }
//            if (mHomeColumns.size() == 1) {
//                for (GoodCategoryInfo info : homeColumns) {
//                    GoodCategoryInfo goodCategoryInfo = mHomeColumns.get(0);
//                    if (goodCategoryInfo.getName().equals(info.getName())) {
//                        continue;
//                    }
//                    mHomeColumns.add(info);
//                }
//            } else {
//                mHomeColumns.addAll(homeColumns);
//            }
//        }
//
//
//        @Override
//        public Fragment getItem(int position) {
//            GoodCategoryInfo homeColumn = mHomeColumns.get(position);
//            if (getString(R.string.choiceness).equals(homeColumn.getName())) {
//                HomeRecommendFragment homeRecommendFragment = HomeRecommendFragment.newInstance();
//                homeRecommendFragment.setUpdateColorCallback(HomeFragment.this);
//                fragments.add(homeRecommendFragment);
//                return homeRecommendFragment;
//            } else if (getString(R.string.what_like).equals(homeColumn.getName())) {
//                ShoppingListFragment2 whatLikeFragment = ShoppingListFragment2.newInstance(C.GoodsListType.WHAT_LIKE);
//                return whatLikeFragment;
//            } else {
//                List<Child2> childs = homeColumn.getChild2();
//                return CategoryListFragment.newInstance(homeColumn.getName(), childs);
//
//            }
//
//        }
//
//        @Override
//        public int getCount() {
//            return mHomeColumns.size();
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            GoodCategoryInfo homeColumn = mHomeColumns.get(position);
//            return homeColumn.getName();
//        }
//
//    }
}

