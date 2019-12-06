package com.zjzy.morebit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.utils.MyLog;

import org.greenrobot.eventbus.EventBus;

/**
 * 主页面中的会员页面
 * 升级入口，会员商品列表
 * Created by haiping.liu on 2019-12-04.
 */
public class NumberFragment extends BaseMainFragmeng {

    private View mView;

    private boolean isUserHint = true;
    private boolean isVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_number, container, false);
        return mView;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyLog.d("setUserVisibleHint", "CircleFragment  " + isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (isVisible ) {
//            initData();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
