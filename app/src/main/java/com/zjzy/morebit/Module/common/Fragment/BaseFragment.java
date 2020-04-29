package com.zjzy.morebit.Module.common.Fragment;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjzy.morebit.Module.push.Logger;
import com.zjzy.morebit.R;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基础Fragment
 */

public abstract class BaseFragment extends RxFragment {
    private Unbinder mUnbinder;
    //是否可见状态
    private boolean isVisible;

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e("打开了界面----", this.getClass().getSimpleName());
    }

    /**
     * The Fragment's UI is just a simple text view showing its instance
     * number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null)
            mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (registered){
            EventBus.getDefault().unregister(true);
        }
//        if (App.mRefWatcher != null)
//            App.mRefWatcher.watch(this);
    }

    /**
     * 移除完了错误提示
     *
     * @param viewGroup
     */
    public void removeNetworkError(ViewGroup viewGroup) {
        if (viewGroup == null) {
            return;
        }
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt.getId() == R.id.ll_error_id) {
                viewGroup.removeView(childAt);
            }
        }
    }

    /**
     * 添加网络错误提示
     *
     * @param viewGroup
     * @param networkError
     */
    public void showNetworkError(ViewGroup viewGroup, boolean networkError) {
        if (viewGroup == null) {
            return;
        }
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt.getId() == R.id.ll_error_id) {
                setNetWorkVIewData(networkError, childAt);
                return;
            }
        }
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.view_list_error, null);
        view.setId(R.id.ll_error_id);
        viewGroup.addView(view);
        setNetWorkVIewData(networkError, view);
    }

    private void setNetWorkVIewData(boolean networkError, View view) {
        ImageView mIv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        TextView mTv_text = (TextView) view.findViewById(R.id.tv_text);
        if (networkError) {

            mIv_icon.setImageResource(R.drawable.image_meiyouwangluo);
            mTv_text.setText(getString(R.string.dtae_error));
        } else {
            mIv_icon.setImageResource(R.drawable.image_meiyousousuojilu);
            mTv_text.setText(getString(R.string.data_null));

        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            isVisible = true;
            onVisible();
        }else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            isVisible = true;
            onVisible();
        }else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible(){

    }

    protected void onInvisible(){}


    protected boolean isFragmentVisiable(){
        return isVisible;
    }




}