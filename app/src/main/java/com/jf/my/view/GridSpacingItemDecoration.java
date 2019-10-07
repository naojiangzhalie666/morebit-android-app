package com.jf.my.view;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @Author: wuchaowen
 * @Description:
 * @Time: 支持水平方向的GridLayoutManager
 **/
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int verticalSpacing;
    private int horizontalSpacing;
    private boolean includeEdge;
    private int headerOffset;
    private int footerOffset;
    private int orientation;

    //For use with GridLayoutManager
    public static GridSpacingItemDecoration newGridItemDecoration(GridLayoutManager gridLayoutManager, int verticalSpacing, int horizontalSpacing, boolean includeEdge) {
        return newGridItemDecoration(gridLayoutManager, verticalSpacing, horizontalSpacing, includeEdge, 0, 0);
    }

    //For use with GridLayoutManager
    public static GridSpacingItemDecoration newGridItemDecoration(GridLayoutManager gridLayoutManager, int spacing, boolean includeEdge) {
        return newGridItemDecoration(gridLayoutManager, spacing, spacing, includeEdge, 0, 0);
    }

    //For use with GridLayoutManager
    public static GridSpacingItemDecoration newGridItemDecoration(GridLayoutManager gridLayoutManager, int spacing, boolean includeEdge, int headerOffset, int footerOffset) {
        return new GridSpacingItemDecoration(gridLayoutManager.getSpanCount(), spacing, spacing, includeEdge, gridLayoutManager.getOrientation(), headerOffset, footerOffset);
    }

    //For use with GridLayoutManager
    public static GridSpacingItemDecoration newGridItemDecoration(GridLayoutManager gridLayoutManager, int verticalSpacing, int horizontalSpacing, boolean includeEdge, int headerOffset, int footerOffset) {
        return new GridSpacingItemDecoration(gridLayoutManager.getSpanCount(), verticalSpacing, horizontalSpacing, includeEdge, gridLayoutManager.getOrientation(), headerOffset, footerOffset);
    }

    //For use with LinearLayoutManager
    public static GridSpacingItemDecoration newLinearItemDecoration(LinearLayoutManager linearLayoutManager, int spacing, boolean includeEdge) {
        return newLinearItemDecoration(linearLayoutManager, spacing, spacing, includeEdge, 0, 0);
    }

    //For use with LinearLayoutManager
    public static GridSpacingItemDecoration newLinearItemDecoration(LinearLayoutManager linearLayoutManager, int verticalSpacing, int horizontalSpacing, boolean includeEdge, int headerOffset, int footerOffset) {
        return new GridSpacingItemDecoration(1, verticalSpacing, horizontalSpacing, includeEdge, linearLayoutManager.getOrientation(), headerOffset, footerOffset);
    }

    //private constructor
    private GridSpacingItemDecoration(int spanCount, int verticalSpacing, int horizontalSpacing, boolean includeEdge, int orientation, int headerOffset, int footerOffset) {
        this.spanCount = spanCount;
        this.verticalSpacing = verticalSpacing;
        this.horizontalSpacing = horizontalSpacing;
        this.includeEdge = includeEdge;
        this.headerOffset = headerOffset;
        this.footerOffset = footerOffset;
        this.orientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view) - headerOffset; // item position
        int column = position % spanCount; // item column

        if (position < 0 || position >= parent.getAdapter().getItemCount() - footerOffset - headerOffset) {
            return;
        }

        int left = 0, right = 0, top = 0, bottom = 0;

        int leftRightSpacing = orientation == OrientationHelper.VERTICAL ? horizontalSpacing : verticalSpacing;
        int topBottomSpacing = orientation == OrientationHelper.VERTICAL ? verticalSpacing : horizontalSpacing;

        if (includeEdge) {
            left = leftRightSpacing - column * leftRightSpacing / spanCount;
            right = (column + 1) * leftRightSpacing / spanCount;

            if (position < spanCount) { // top edge
                top = topBottomSpacing;
            }
            bottom = topBottomSpacing; // item bottom
        } else {
            left = column * leftRightSpacing / spanCount;
            right = leftRightSpacing - (column + 1) * leftRightSpacing / spanCount;
            if (position >= spanCount) {
                top = topBottomSpacing; // item top
            }
        }

        if (orientation == OrientationHelper.HORIZONTAL) {
            outRect.left = top;
            outRect.right = bottom;
            outRect.top = left;
            outRect.bottom = right;
        } else {
            outRect.left = left;
            outRect.right = right;
            outRect.top = top;
            outRect.bottom = bottom;
        }
    }
}