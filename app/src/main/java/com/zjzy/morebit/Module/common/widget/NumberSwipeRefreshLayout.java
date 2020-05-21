/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zjzy.morebit.Module.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.view.refresh.MarkermallHeadRefresh;
import com.zjzy.morebit.view.refresh.NumberlHeadRefresh;

public class NumberSwipeRefreshLayout extends SmartRefreshLayout {
    public  boolean isAutoRefresh = true;//是否自动刷新 (修改时:需要在构造方法前使用)

    NumberlHeadRefresh headRefresh;
    OnRefreshListener mOnRefreshListener;
    boolean isFirst = true;


    public NumberSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public NumberSwipeRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberSwipeRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if(getBackground()==null){ //添加默认背景色
            setBackgroundResource(R.color.transparent);
        }

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        headRefresh = new NumberlHeadRefresh(context);
        headRefresh.setBackgroundColor(Color.parseColor("#EDCFB1"));
        headRefresh.setLayoutParams(layoutParams);
        addView(headRefresh, 0);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,  R.styleable.SwipeRefresh);
        isAutoRefresh = typedArray.getBoolean( R.styleable.SwipeRefresh_is_auto_refresh, true);

        if(isAutoRefresh){
            autoRefresh();
        }else{
            isFirst = false;
            isAutoRefresh = true;
        }
    }

    @Override
    public boolean autoRefresh(){
        return autoRefresh(50, 100, 1, false);
    }

    public void setRefreshing(boolean isRefreshing){
        super.finishRefresh(true);

    }

    public boolean isRefreshing(){

        return false;
    }

    //设置颜色
    public void setColorSchemeResources(int a, int b, int c, int d){

    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener){
        mOnRefreshListener = onRefreshListener;
        super.setOnRefreshListener(new com.scwang.smartrefresh.layout.listener.OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if(isFirst){
                    isFirst = false;
                    setRefreshing(true);
                    return;
                }
                mOnRefreshListener.onRefresh();
            }
        });

    }

    public interface OnRefreshListener{
        public void onRefresh();
    }
}