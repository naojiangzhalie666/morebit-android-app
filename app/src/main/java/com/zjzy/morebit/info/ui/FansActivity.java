package com.zjzy.morebit.info.ui;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.Activity.FansListFragment;
import com.zjzy.morebit.Activity.SearchFansActivity;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;

import com.zjzy.morebit.fragment.MyTeamListFragment;
import com.zjzy.morebit.info.ui.fragment.DynamicRankFragment;
import com.zjzy.morebit.utils.ActivityStyleUtil;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author YangBoTian
 * @date 2019/9/6
 * @des
 */

public class FansActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tab)
    SlidingTabLayout tabLayout;
    @BindView(R.id.rl_top)
    RelativeLayout rl_top;
    @BindView(R.id.status_bar)
    View status_bar;
    private List<MyTeamListFragment> listFragments = new ArrayList<>();
    private String[] mTitles = {"我的粉丝", "动态排行榜"};

    FansAdapter mAdapter;
    GradientDrawable tabLayoutBg;

    private float mLastOffset;
    private int curPosition;

    public static void start(Context context) {
        Intent intent = new Intent(context, FansActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(false)
                .init();
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //处理全屏显示
            ViewGroup.LayoutParams viewParams = status_bar.getLayoutParams();
            viewParams.height = ActivityStyleUtil.getStatusBarHeight(this);
            status_bar.setLayoutParams(viewParams);

        }

        setupViewPager();
    }

    private void setupViewPager() {
        mAdapter = new FansAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        tabLayout.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                MyLog.i("test","position: " +position);
//                MyLog.i("test", "positionOffset: " + positionOffset);
//                ArgbEvaluator evaluator = new ArgbEvaluator();
//                int color = 0;
//                if (position == 0) {
//                    if (positionOffset > mLastOffset) {
//                        if (positionOffset > 0.98){
//                            positionOffset =1;
//                            color = (int) evaluator.evaluate(positionOffset, ContextCompat.getColor(FansActivity.this, R.color.color_FFD800), ContextCompat.getColor(FansActivity.this, R.color.white));
//                        } else {
//                            color = (int) evaluator.evaluate(positionOffset, ContextCompat.getColor(FansActivity.this, R.color.color_FFD800), ContextCompat.getColor(FansActivity.this, R.color.white));
//                        }
//                    } else {
//                        color = (int) evaluator.evaluate(positionOffset, ContextCompat.getColor(FansActivity.this, R.color.white), ContextCompat.getColor(FansActivity.this, R.color.color_FFD800));
//                    }
//                } else {
////                    if (positionOffset > mLastOffset) {
////                    MyLog.i("test", "positionOffset: " + positionOffset);
////                        color = (int) evaluator.evaluate(positionOffset, ContextCompat.getColor(FansActivity.this, R.color.white), ContextCompat.getColor(FansActivity.this, R.color.color_FFD800));
////                    }
//                }
//                rl_top.setBackgroundColor(color);
//                mLastOffset = positionOffset;
            }

            @Override
            public void onPageSelected(int position) {
                   if(position==1){
                       rl_top.setBackgroundColor( ContextCompat.getColor(FansActivity.this, R.color.color_FFD800));
                       status_bar.setBackgroundColor(ContextCompat.getColor(FansActivity.this, R.color.color_FFD800));
                  //     tabLayoutBg.setColor( ContextCompat.getColor(FansActivity.this, R.color.color_ECECEC));
                   } else {
                       rl_top.setBackgroundColor( ContextCompat.getColor(FansActivity.this, R.color.color_FFD800));
                       status_bar.setBackgroundColor(ContextCompat.getColor(FansActivity.this, R.color.color_FFD800));
                    //   tabLayoutBg.setColor( ContextCompat.getColor(FansActivity.this, R.color.white));
                   }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class FansAdapter extends FragmentStatePagerAdapter {

        private MyTeamListFragment mCurrentFragment;

        public FansAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                FansListFragment fragment = FansListFragment.newInstance();
                return fragment;
            } else if (position == 1) {
                DynamicRankFragment fragment = DynamicRankFragment.newInstance();
                return fragment;
            }

            return FansListFragment.newInstance();
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

    public void setTopBg(float percent) {
        ArgbEvaluator evaluator = new ArgbEvaluator();
        ArgbEvaluator evaluatorSearch = new ArgbEvaluator();
        ArgbEvaluator evaluatorTabLyout = new ArgbEvaluator();
//        if (tabLayoutBg == null) {
//           tabLayoutBg = (GradientDrawable) tabLayout.getBackground();
//        }
        //滑动渐变
        int topBgColor = (int) evaluatorSearch.evaluate(percent, ContextCompat.getColor(this, R.color.color_FFD800), ContextCompat.getColor(this, R.color.white));
        int colorTabLyout = (int) evaluatorTabLyout.evaluate(percent, ContextCompat.getColor(this, R.color.white), ContextCompat.getColor(this, R.color.color_ECECEC));
        int color = (int) evaluator.evaluate(percent, ContextCompat.getColor(this, R.color.color_FFD800), ContextCompat.getColor(this, R.color.white));
     //  tabLayoutBg.setColor(colorTabLyout);
       rl_top.setBackgroundColor(topBgColor);
        status_bar.setBackgroundColor(color);
    }

    public void setImmersionBarBg(boolean isChange) {
        if (isChange) {
            ImmersionBar.with(this).fitsSystemWindows(false).statusBarDarkFont(true, 0.2f).init();
        } else {
            ImmersionBar.with(this).fitsSystemWindows(false).statusBarDarkFont(true, 0.2f).init();
        }

    }

    @OnClick({R.id.iv_right_search, R.id.iv_back})
    public void onCLick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_right_search:
                SearchFansActivity.start(this, "");
                break;
        }

    }
}
