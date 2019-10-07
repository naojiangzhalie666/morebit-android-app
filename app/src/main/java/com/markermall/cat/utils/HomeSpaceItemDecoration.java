package com.markermall.cat.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;


/**
 * Created by YangBoTian on 2019/7/12.
 */

public class HomeSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = HomeSpaceItemDecoration.class.getName();
    private int space;
    private int columnCount;

    public HomeSpaceItemDecoration(int space, int columnCount) {
        this.space = space;
        this.columnCount = columnCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        if (parent.getChildViewHolder(view) instanceof HomeAdapter.FormalThreeVH) {}
        int position = parent.getChildAdapterPosition(view);
        if (position== 0) {
            return;
        }
        outRect.top = space/2;
        //瀑布流专属分割线
        StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();

        /**
         * 根据params.getSpanIndex()来判断左右边确定分割线
         * 第一列设置左边距为space，右边距为space/2  （第二列反之）
         */
        if (params.getSpanIndex() % 2 == 0) {
            outRect.left = space;
            outRect.right = space / 4;
        } else {
            outRect.left = space / 4;
            outRect.right = space;
        }
    }
}
