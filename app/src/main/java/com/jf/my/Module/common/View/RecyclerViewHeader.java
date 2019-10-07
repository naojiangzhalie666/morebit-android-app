package com.jf.my.Module.common.View;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.jf.my.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.internal.InternalAbstract;


/**
 * @author YangBoTian
 * @date 2019/5/30
 */
public class RecyclerViewHeader extends InternalAbstract implements RefreshHeader {
    /**
     * 刷新动画视图
     */
    private ImageView mProgressView;
    private final AnimationDrawable mAnimationDrawable;

    public RecyclerViewHeader(Context context) {
        this(context, null);
        setGravity(Gravity.CENTER_HORIZONTAL);
        mProgressView = new ImageView(context);
    }

    public RecyclerViewHeader(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.refresh_header, this);

        mProgressView = view.findViewById(R.id.imageView);
        mProgressView.setImageResource(R.drawable.head_refresh);
        mAnimationDrawable = (AnimationDrawable) mProgressView.getDrawable();
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        mAnimationDrawable.stop();
        super.onFinish(layout, success);
        //延迟n毫秒之后再弹回
        return 50;
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        super.onStartAnimator(refreshLayout, height, maxDragHeight);
        mAnimationDrawable.start();
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            //下拉过程
            case PullDownToRefresh:
                break;
            //松开刷新
            case ReleaseToRefresh:
                break;
            //loading中
            case Refreshing:
                startPullRefreshAnim();
                break;
            default:
                break;
        }
    }

    private void startPullRefreshAnim() {
        if (mAnimationDrawable != null) {
            mAnimationDrawable.start();
        }
    }
//
//    /**
//     * 停止
//     * @param millisecond 延迟n毫秒停止动画
//     */
//    private void stopPullRefreshAnim(final long millisecond) {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(mAnimationDrawable != null && mAnimationDrawable.isRunning()){
//                    mAnimationDrawable.stop();
//                }
//            }
//        }, millisecond);
//    }

}
