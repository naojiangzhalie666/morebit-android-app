package com.markermall.cat.view.refresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.markermall.cat.App;
import com.markermall.cat.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.internal.InternalAbstract;

/**
 * liys  2019-01-28
 * SmartRefreshLayout head
 */
public class MarkermallHeadRefresh extends InternalAbstract implements RefreshHeader {

    private TextView mTitleText;
    private ImageView mImageView;
    private final AnimationDrawable mAnimationDrawable;

    public static String REFRESH_HEADER_PULLING = "下拉可以刷新";//"下拉可以刷新";
    public static String REFRESH_HEADER_LOADING = "正在加载...";//"正在加载...";
    public static String REFRESH_HEADER_RELEASE = "释放立即刷新";
    public static String REFRESH_HEADER_FINISH = "刷新完成";//"刷新完成";
    public static String REFRESH_HEADER_FAILED = "刷新失败";//"刷新失败";

    public MarkermallHeadRefresh(Context context) {
        this(context, null);
    }

    public MarkermallHeadRefresh(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarkermallHeadRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = LayoutInflater.from(context).inflate(R.layout.markermall_refresh_head, this);
        mTitleText = view.findViewById(R.id.txt);
        mImageView = view.findViewById(R.id.imageView);
        mImageView.setImageResource(R.drawable.head_refresh);
        mAnimationDrawable = (AnimationDrawable) mImageView.getDrawable();
    }


    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        stopPullRefreshAnim(400);
        if (success) {
            mTitleText.setText(REFRESH_HEADER_FINISH);
        } else {
            mTitleText.setText(REFRESH_HEADER_FAILED);
        }
        super.onFinish(layout, success);
        return 400; //延迟n毫秒之后再弹回
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case PullDownToRefresh: //下拉过程
                if(mAnimationDrawable != null){
                    mAnimationDrawable.selectDrawable(0); //选择当前动画的第一帧
                }
                mTitleText.setText(REFRESH_HEADER_PULLING);
                break;
            case ReleaseToRefresh: //松开刷新
                mTitleText.setText(REFRESH_HEADER_RELEASE);
                break;
            case Refreshing: //loading中
                startPullRefreshAnim();
                mTitleText.setText(REFRESH_HEADER_LOADING);
                break;
//            case RefreshFinish: //loading完成 收回
//                stopPullRefreshAnim(1500);
//                break;
        }
    }

    private void startPullRefreshAnim() {
        if(mAnimationDrawable != null){
            mAnimationDrawable.start();
        }
    }

    /**
     * 停止
     * @param millisecond 延迟n毫秒停止动画
     */
    private void stopPullRefreshAnim(final long millisecond) {
        App.mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mAnimationDrawable != null && mAnimationDrawable.isRunning()){
                    mAnimationDrawable.stop();
                }
            }
        }, millisecond);
    }

}
