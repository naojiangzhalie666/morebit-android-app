package com.jf.my.info.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by YangBoTian on 2018/12/26.
 */
public class SquareFrameLayout extends FrameLayout {

    public SquareFrameLayout(final Context context) {
        super(context);
    }

    public SquareFrameLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareFrameLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);

        if (width < height) {
            setMeasuredDimension(width, width);
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        } else {
            setMeasuredDimension(height, height);
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        }
    }
}
