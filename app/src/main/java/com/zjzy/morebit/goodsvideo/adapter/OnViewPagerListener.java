package com.zjzy.morebit.goodsvideo.adapter;

import android.content.Context;
import android.view.View;

public interface OnViewPagerListener {
    /**
     * 初始化
     */
    void onInitComplete(View view);

    /**
     * 释放
     */
    void onPageRelease(boolean isNext, int position, View view);

    /**
     * 选中
     */
    void onPageSelected(int position, boolean isBottom, View view, Context context);
}