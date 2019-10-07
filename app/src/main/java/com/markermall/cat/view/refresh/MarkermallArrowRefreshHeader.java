package com.markermall.cat.view.refresh;

import android.content.Context;
import android.util.AttributeSet;

import com.github.jdsjlzx.view.ArrowRefreshHeader;
import com.markermall.cat.R;

/**
 * liys
 * LRecyclerView适配SmartRefreshLayout下拉刷新
 */
public class MarkermallArrowRefreshHeader extends ArrowRefreshHeader {
    public MarkermallArrowRefreshHeader(Context context) {
        super(context);
    }

    public MarkermallArrowRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setVisibleHeight(int height) {
        super.setVisibleHeight(1);
        setViewBackgroundColor(R.color.white);
    }

}
