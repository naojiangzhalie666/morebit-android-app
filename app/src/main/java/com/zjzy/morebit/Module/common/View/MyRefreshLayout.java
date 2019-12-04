package com.zjzy.morebit.Module.common.View;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * @author YangBoTian
 * @date 2019/6/1
 */
public class MyRefreshLayout extends SmartRefreshLayout {
    public MyRefreshLayout(Context context) {
        this(context,null);

    }

    public MyRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addView(new RecyclerViewHeader(context));
    }

}
