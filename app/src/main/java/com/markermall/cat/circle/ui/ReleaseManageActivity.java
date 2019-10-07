package com.markermall.cat.circle.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.markermall.cat.Module.common.Activity.BaseActivity;
import com.markermall.cat.R;
import com.markermall.cat.fragment.MyTeamListFragment;
import com.markermall.cat.utils.C;
import com.markermall.cat.view.ToolbarHelper;

import butterknife.BindView;

/**
 * Created by YangBoTian on 2019/6/11.
 * 发布管理
 */

public class ReleaseManageActivity extends BaseActivity {
    @BindView(R.id.tablayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    private ReleaseManageAdapter mAdapter;
    private String[] mTitles = {"已发布", "审核中", "审核失败","已下架"};
    private int mPushType;
    public static void start(Activity activity,int pushType) {
        Intent intent = new Intent(activity, ReleaseManageActivity.class);
        intent.putExtra("pushType",pushType);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_manage);
        setStatusBarWhite();
        initView();
    }

    private void initView() {
        mPushType = getIntent().getIntExtra("pushType",0);
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(R.string.release_manage);
        setupViewPager(mTitles);
        if(C.Push.SHARE_MSG_PASS ==mPushType){
            mViewPager.setCurrentItem(0);
        } else if(C.Push.SHARE_MSG_FAILURE == mPushType){
            mViewPager.setCurrentItem(2);
        } else if(C.Push.SHARE_MSG_UNSHELVE == mPushType){
            mViewPager.setCurrentItem(3);
        }
    }
    private void setupViewPager (String[] homeColumns) {
        mAdapter = new ReleaseManageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setViewPager(mViewPager);
    }
    private class ReleaseManageAdapter extends FragmentPagerAdapter {

        private MyTeamListFragment mCurrentFragment;

        public ReleaseManageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            //0:审核中，1:已审核(审核通过)，2:审核失败，3:下架，4:删除
            if("已发布".equals(mTitles[position])){
                return ReleaseManageFragment.newInstance(ReleaseManageFragment.PASS);
            }  else if("审核中".equals(mTitles[position])){
                return ReleaseManageFragment.newInstance(ReleaseManageFragment.REVIEW);
            } else if("审核失败".equals(mTitles[position])){
                return ReleaseManageFragment.newInstance(ReleaseManageFragment.FAILURE);
            }else if("已下架".equals(mTitles[position])){
                return ReleaseManageFragment.newInstance(ReleaseManageFragment.UNSHELVE);
            }
            return ReleaseManageFragment.newInstance(ReleaseManageFragment.REVIEW);
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
