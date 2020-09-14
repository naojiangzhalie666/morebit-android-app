package com.zjzy.morebit.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.base.BaseFeedBackFragment;
import com.zjzy.morebit.utils.UI.ActivityUtils;

/**
 * App反馈
 */

public class AppFeedBackFragment extends BaseFragment {


    private String mGid;
    private BaseFeedBackFragment mInfoFragment;

    public static AppFeedBackFragment newInstance() {
        return new AppFeedBackFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_app_feedback, container, false);
        initBundle();
        initView(view);
        showAnimFragment();
        return view;
    }

    private void showAnimFragment() {
        // Create the fragment
        mInfoFragment = BaseFeedBackFragment.newInstance();
        mInfoFragment.setGId(mGid);
        ActivityUtils.replaceFragmentToActivity(
                getActivity().getSupportFragmentManager(), mInfoFragment, R.id.select_img);
    }

    private void initBundle() {
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            mGid = bundle.getString("gid");
        }
    }

    public void initView(View view) {


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mInfoFragment!=null){
            mInfoFragment.onActivityResult(requestCode,resultCode,data);
        }
    }



}