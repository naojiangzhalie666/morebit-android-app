package com.jf.my.login.ui.areacode;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * divider decoration
 */

public class AreaCodeDividerDecoration extends RecyclerView.ItemDecoration {
    private final Drawable mDivider;
    private final int mPaddingLeft;
    private final int mPaddingRight;

    public AreaCodeDividerDecoration(Drawable divider, int paddingLeft, int paddingRight) {
        mDivider = divider;
        mPaddingLeft = paddingLeft;
        mPaddingRight = paddingRight;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            mDivider.setBounds(mPaddingLeft, child.getBottom() - 1, child.getRight() - mPaddingRight, child.getBottom());
            mDivider.draw(c);
        }
    }
}
