package com.markermall.cat.Module.common.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by YangBoTian on 2019/1/13.
 */

public abstract class LazyLoadFragment extends BaseFragment {
    /**
     * 标记已加载完成，保证懒加载只能加载一次
     */
    private boolean hasLoaded = false;
    /**
     * 标记Fragment是否已经onCreate
     */
    private boolean isCreated = false;
    /**
     * 界面对于用户是否可见
     */
    private boolean isVisibleToUser = false;
    private View mView;

    public LazyLoadFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = getView(inflater, getLayoutId(), container);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view, savedInstanceState);
    }


    private View getView(LayoutInflater inflater, int layoutId, ViewGroup container) {
        return inflater.inflate(layoutId, container, false);
    }

    public void init(View view, Bundle savedInstanceState) {
        isCreated = true;
        initViews(view, savedInstanceState);
        lazyLoad(view, null);
    }

    /**
     * 监听界面是否展示给用户，实现懒加载
     * 这个方法也是网上的一些方法用的最多的一个，我的思路也是这个，不过把整体思路完善了一下
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //注：关键步骤
        this.isVisibleToUser = isVisibleToUser;
        lazyLoad(mView, null);
    }


    /**
     * 懒加载方法，获取数据什么的放到这边来使用，在切换到这个界面时才进行网络请求
     */
    private void lazyLoad(View view, Bundle savedInstanceState) {

        //如果该界面不对用户显示、已经加载、fragment还没有创建，
        //三种情况任意一种，不获取数据
        if (!isVisibleToUser || hasLoaded || !isCreated) {
            return;
        }
        lazyInit(view, savedInstanceState);
        //注：关键步骤，确保数据只加载一次
        hasLoaded = true;
    }


    /**
     * 子类必须实现的方法，这个方法里面的操作都是需要懒加载的
     */
    public abstract void lazyInit(View view, Bundle savedInstanceState);

    public abstract void initViews(View view, Bundle savedInstanceState);

    public abstract int getLayoutId();
}
