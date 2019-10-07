package com.markermall.cat.utils;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by YangBoTian on 2019/7/12.
 */

public class CollegeCategoryItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = CollegeCategoryItemDecoration.class.getName();
    private int left;
    private int right;
    private int top;
    private int bottom;

    public CollegeCategoryItemDecoration(int left, int right) {
        this.left = left;
        this.right = right;
    }
    public CollegeCategoryItemDecoration(int left, int top, int right, int bottom) {
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        if (parent.getChildViewHolder(view) instanceof HomeAdapter.FormalThreeVH) {}
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        //竖直方向的
        if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            //最后一项需要 bottom
            if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
                outRect.bottom = bottom;
            }
            outRect.top = top;
            outRect.left = left;
            outRect.right = right;
        } else {
            //最后一项需要right
            if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
                outRect.right = left;
            } else {
                outRect.right = right;
            }
            if(parent.getChildAdapterPosition(view)==0){
                outRect.left = left;
            }
            outRect.top = top;
            outRect.bottom = bottom;
        }
    }
}
