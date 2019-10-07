package com.markermall.cat.main.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markermall.cat.R;
import com.markermall.cat.circle.ui.CommercialCollegeFragment;
import com.markermall.cat.fragment.base.BaseMainFragmeng;
import com.markermall.cat.utils.ActivityStyleUtil;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Author: wuchaowen
 * @Description: 此Fragment用来包商学院的fragment。因为商学院的fragmetn顶部重叠状态栏了，需要往下移
 **/
public class HomeCollegeFragment extends BaseMainFragmeng {
    private View mView;
    @BindView(R.id.status_bar)
    View status_bar;
    private CommercialCollegeFragment mChildFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_home_college, container, false);
        ButterKnife.bind(this, mView);
        initView(mView);
        return mView;
    }


    /**
     * 初始化界面
     */
    private void initView(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //处理全屏显示
            ViewGroup.LayoutParams viewParams = status_bar.getLayoutParams();
            viewParams.height = ActivityStyleUtil.getStatusBarHeight(getActivity());
            status_bar.setLayoutParams(viewParams);
            status_bar.setBackgroundResource(R.color.white);
        }

        mChildFragment = new CommercialCollegeFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, mChildFragment).commit();
    }


//    public boolean onBackPressed() {
//        if (getActivity() != null && mChildFragment != null) {
//            return  mChildFragment.onBackPressed();
//        }
//        return false;
//    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            GSYVideoManager.releaseAllVideos();
        }
    }
}
