package com.jf.my.view.refresh;

import android.content.Context;
import android.util.AttributeSet;

import com.github.jdsjlzx.view.ArrowRefreshHeader;
import com.jf.my.R;

/**
 * liys
 * LRecyclerView适配SmartRefreshLayout下拉刷新
 */
public class MiYuanArrowRefreshHeader extends ArrowRefreshHeader {
    public MiYuanArrowRefreshHeader(Context context) {
        super(context);
    }

    public MiYuanArrowRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setVisibleHeight(int height) {
        super.setVisibleHeight(1);
        setViewBackgroundColor(R.color.white);
    }

}
