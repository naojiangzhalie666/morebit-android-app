package com.zjzy.morebit.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.zjzy.morebit.purchase.adapter.PurchseAdapter;

/**
 * Created by Administrator on 2017/10/16.
 */

public class FixRecyclerView extends RecyclerView {
    public FixRecyclerView(Context context) {
        super(context);
    }

    public FixRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixRecyclerView(Context context, AttributeSet attrs,
                           int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
