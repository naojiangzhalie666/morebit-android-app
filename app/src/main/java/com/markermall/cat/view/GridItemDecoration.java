package com.markermall.cat.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * @Author: wuchaowen
 * @Description:
 **/
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private boolean mShowLastLine;
    private int mHorizonSpan;
    private int mVerticalSpan;
    private int mLeftPaddingH;
    private int mRightPaddingH;
    private int mTopPaddingH;
    private int mBottomPaddingH;

    private GridItemDecoration(int horizonSpan,int verticalSpan,int color,boolean showLastLine,int leftPadding,int rightPadding,int topPadding,int bottomPadding) {
        this.mHorizonSpan = horizonSpan;
        this.mShowLastLine = showLastLine;
        this.mVerticalSpan = verticalSpan;
        this.mLeftPaddingH = leftPadding;
        this.mRightPaddingH = rightPadding;
        this.mTopPaddingH = topPadding;
        this.mBottomPaddingH = bottomPadding;
        mDivider = new ColorDrawable(color);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            //最后一行底部横线不绘制
            if (isLastRaw(parent,i,getSpanCount(parent),childCount) && !mShowLastLine){
                continue;
            }
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int leftPadding = 0;
            if((i+1) % getSpanCount(parent) == 0 ){
                leftPadding = 0;
            }else{
                if(getSpanCount(parent)>=3){
                    if((i+1) % 2 == 0 ){
                        if((i+3) % 3 ==0){
                            leftPadding = this.mLeftPaddingH;
                        }else{
                            leftPadding = 0;
                        }

                    }else{
                        if(i %3 !=0){
                            leftPadding = 0;
                        }else{
                            leftPadding = this.mLeftPaddingH;
                        }

                    }
                }else{
                    leftPadding = this.mLeftPaddingH;
                }

            }


            final int left = child.getLeft() - params.leftMargin +leftPadding;
            final int right = child.getRight() + params.rightMargin- ((i+1) % getSpanCount(parent) == 0? this.mRightPaddingH: 0);
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mHorizonSpan;

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

//    private void drawHorizontal(Canvas c, RecyclerView parent) {
//        int childCount = parent.getChildCount();
//
//        for (int i = 0; i < childCount; i++) {
//            View child = parent.getChildAt(i);
//
//            //最后一行底部横线不绘制
//            if (isLastRaw(parent,i,getSpanCount(parent),childCount) && !mShowLastLine){
//                continue;
//            }
//            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
//            final int left = child.getLeft() - params.leftMargin +((i+1) % getSpanCount(parent) == 0? 0: this.mLeftPaddingH);
//            final int right = child.getRight() + params.rightMargin- ((i+1) % getSpanCount(parent) == 0? this.mRightPaddingH: 0);
//            final int top = child.getBottom() + params.bottomMargin;
//            final int bottom = top + mHorizonSpan;
//
//            mDivider.setBounds(left, top, right, bottom);
//            mDivider.draw(c);
//        }
//    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if((parent.getChildViewHolder(child).getAdapterPosition() + 1) % getSpanCount(parent) == 0){
                continue;
            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int topPadding = 0;
            if(i == 0 ){
                topPadding = this.mTopPaddingH;
            }else{
                if(getSpanCount(parent)>=3){
                    if((i+1) % 2 == 0 ){
                        if((i+1)<3){
                            topPadding = this.mTopPaddingH;
                        }else{
                            topPadding = 0;
                        }

                    }else{
                        topPadding = 0;
                    }
                }else{
                    topPadding = 0;
                }

            }


            int bottomPadding = 0;
            if(i== (childCount-getSpanCount(parent)) ){
                bottomPadding = this.mBottomPaddingH;
            }else{
                if(getSpanCount(parent)>=3){
                    if(i== (childCount-getSpanCount(parent)+1)){
                        bottomPadding = this.mBottomPaddingH;
                    }else{
                        bottomPadding = 0;
                    }
                }else{
                    bottomPadding = 0;
                }

            }

            final int top = child.getTop() - params.topMargin+ topPadding ;
            Log.d("iii",i+"");
            Log.d("iii","childCount="+childCount);
            final int bottom = child.getBottom() + params.bottomMargin + mHorizonSpan - bottomPadding;
            final int left = child.getRight() + params.rightMargin;
            int right = left + mVerticalSpan;
//            //满足条件( 最后一行 && 不绘制 ) 将vertical多出的一部分去掉;
            if (i==childCount-1) {
                right -= mVerticalSpan;
            }
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

//    private void drawVertical(Canvas c, RecyclerView parent) {
//        int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            final View child = parent.getChildAt(i);
//            if((parent.getChildViewHolder(child).getAdapterPosition() + 1) % getSpanCount(parent) == 0){
//                continue;
//            }
//            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
//            final int top = child.getTop() - params.topMargin+ (i==0? this.mTopPaddingH:0) ;
//            Log.d("iii",i+"");
//            Log.d("iii","childCount="+childCount);
//            final int bottom = child.getBottom() + params.bottomMargin + mHorizonSpan - (i== (childCount-getSpanCount(parent))? this.mBottomPaddingH:0);
//            final int left = child.getRight() + params.rightMargin;
//            int right = left + mVerticalSpan;
////            //满足条件( 最后一行 && 不绘制 ) 将vertical多出的一部分去掉;
//            if (i==childCount-1) {
//                right -= mVerticalSpan;
//            }
//            mDivider.setBounds(left, top, right, bottom);
//            mDivider.draw(c);
//        }
//    }

    /**
     * 计算偏移量
     * */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();

        if (itemPosition < 0){
            return;
        }

        int column = itemPosition % spanCount;
        int bottom;

        int left = column * mVerticalSpan / spanCount;
        int right = mVerticalSpan - (column + 1) * mVerticalSpan / spanCount;

        if (isLastRaw(parent, itemPosition, spanCount, childCount)){
            if (mShowLastLine){
                bottom = mHorizonSpan;
            }else{
                bottom = 0;
            }
        }else{
            bottom = mHorizonSpan;
        }
        outRect.set(left, 0, right, bottom);
    }

    /**
     * 获取列数
     * */
    private int getSpanCount(RecyclerView parent) {
        // 列数
        int mSpanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            mSpanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            mSpanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return mSpanCount;
    }

    /**
     * 是否最后一行
     * @param parent     RecyclerView
     * @param pos        当前item的位置
     * @param spanCount  每行显示的item个数
     * @param childCount child个数
     * */
    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager) {
            return getResult(pos,spanCount,childCount);
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // StaggeredGridLayoutManager 且纵向滚动
                return getResult(pos,spanCount,childCount);
            } else {
                // StaggeredGridLayoutManager 且横向滚动
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean getResult(int pos,int spanCount,int childCount){
        int remainCount = childCount % spanCount;//获取余数
        //如果正好最后一行完整;
        if (remainCount == 0){
            if(pos >= childCount - spanCount){
                return true; //最后一行全部不绘制;
            }
        }else{
            if (pos >= childCount - childCount % spanCount){
                return true;
            }
        }
        return false;
    }

    /**
     * 使用Builder构造
     * */
    public static class Builder {
        private Context mContext;
        private Resources mResources;
        private boolean mShowLastLine;
        private int mHorizonSpan;
        private int mVerticalSpan;
        private int mColor;
        private int mLeftPaddingH;
        private int mRightPaddingH;
        private int mTopPaddingV;
        private int mBottomPaddingH;

        public Builder(Context context) {
            mContext = context;
            mResources = context.getResources();
            mShowLastLine = true;
            mHorizonSpan = 0;
            mVerticalSpan = 0;
            mLeftPaddingH = 0;
            mRightPaddingH = 0;
            mTopPaddingV = 0;
            mBottomPaddingH = 0;
            mColor = Color.WHITE;
        }

        /**
         * 通过资源文件设置分隔线颜色
         */
        public Builder setColorResource(@ColorRes int resource) {
            setColor(ContextCompat.getColor(mContext, resource));
            return this;
        }

        /**
         * 设置颜色
         */
        public Builder setColor(@ColorInt int color) {
            mColor = color;
            return this;
        }

        /**
         * 通过dp设置垂直间距
         * */
        public Builder setVerticalSpan(@DimenRes int vertical) {
            this.mVerticalSpan = mResources.getDimensionPixelSize(vertical);
            return this;
        }

        /**
         * 通过px设置垂直间距
         * */
        public Builder setVerticalSpan(float mVertical) {
            this.mVerticalSpan = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, mVertical, mResources.getDisplayMetrics());
            return this;
        }

        /**
         * 通过dp设置水平间距
         * */
        public Builder setHorizontalSpan(@DimenRes int horizontal) {
            this.mHorizonSpan = mResources.getDimensionPixelSize(horizontal);
            return this;
        }

        /**
         * 通过px设置水平间距
         * */
        public Builder setHorizontalSpan(float horizontal) {
            this.mHorizonSpan = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, horizontal, mResources.getDisplayMetrics());
            return this;
        }

        /**
         * 设置水平线的左右padding
         * @param leftPadding
         * @param rightPadding
         * @return
         */
        public Builder setHorizontalPadding(float leftPadding,float rightPadding){
            this.mLeftPaddingH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, leftPadding, mResources.getDisplayMetrics());
            this.mRightPaddingH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, rightPadding, mResources.getDisplayMetrics());
            return this;
        }

        /**
         * 设置垂直的上下padding
         * @param topPadding
         * @param bottomPadding
         * @return
         */
        public Builder setVerticaPadding(float topPadding,float bottomPadding){
            this.mTopPaddingV = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, topPadding, mResources.getDisplayMetrics());
            this.mBottomPaddingH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, bottomPadding, mResources.getDisplayMetrics());
            return this;
        }

        /**
         * 是否最后一条显示分割线
         * */
        public GridItemDecoration.Builder setShowLastLine(boolean show){
            mShowLastLine = show;
            return this;
        }

        public GridItemDecoration build() {
            return new GridItemDecoration(mHorizonSpan, mVerticalSpan, mColor,mShowLastLine,mLeftPaddingH,mRightPaddingH,mTopPaddingV,mBottomPaddingH);
        }
    }
}
