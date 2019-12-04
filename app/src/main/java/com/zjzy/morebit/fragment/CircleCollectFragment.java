package com.zjzy.morebit.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.UI.ActivityUtils;
import com.zjzy.morebit.view.ToolbarHelper;

/**
 - @Description:  蜜粉圈收藏
 - @Author:
 - @Time:  2019/9/4 14:21
 **/
public class CircleCollectFragment extends BaseFragment {
    private View mView;
    public CircleCollectFragment() {
    }

    public static void start(Activity activity) {
        OpenFragmentUtils.goToSimpleFragment(activity, CircleCollectFragment.class.getName(), null);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(getString(R.string.circle_my_collects));
        initView(mView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_circle_collect, container, false);
        }
        return mView;
    }

    public void initView(View view) {
        CircleDayHotFragment fragment = CircleDayHotFragment.newInstance(CircleDayHotFragment.TYPE_MY_COLLECTS);
        ActivityUtils.replaceFragmentToActivity(
                getChildFragmentManager(), fragment, R.id.fl_img);



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //EventBus.getDefault().post(new RefreshCircleEvent());
    }


}
