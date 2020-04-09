package com.zjzy.morebit.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecorationUtils extends RecyclerView.ItemDecoration {

    private int space;
    private int spanCount;

    private int orientation = RecyclerView.VERTICAL;

    public SpaceItemDecorationUtils(int space, int spanCount) {
        this.space = space;
        this.spanCount = spanCount;
    }
    public SpaceItemDecorationUtils(int space, int spanCount,@RecyclerView.Orientation int orientation) {
        this.space = space;
        this.spanCount = spanCount;
        this.orientation = orientation;
    }
    private void setOrientation(@RecyclerView.Orientation int orientation) {
        this.orientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        //outRect.left = space;
        // outRect.bottom = space;
        outRect.set(0,0,space,space);
        //竖向--由于每行都只有spanCount个，所以第一个都是spanCount的倍数，把左边距设为0
      /*  if (orientation == RecyclerView.VERTICAL ) {
            //&& parent.getChildLayoutPosition(view) % spanCount == 0
            if (parent.getChildLayoutPosition(view)%spanCount==spanCount-1){
                //最右边
                outRect.set(0,0,0,space);
            }

        }else if (orientation==RecyclerView.HORIZONTAL){
           //网格布局 需要考虑 多个合并问题， 后期优化
        }*/
    }
}
