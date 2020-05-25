package com.zjzy.morebit.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;


/**
 * data:2019/12/26
 * author:jyx
 * function:
 */
public class AutoInterceptViewGroup extends FrameLayout {

    public AutoInterceptViewGroup(@NonNull Context context) {
        super(context);
    }

    public AutoInterceptViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoInterceptViewGroup(@NonNull Context context, @Nullable AttributeSet attrs,
                                  int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.i(tag, ev.getAction() + "--" + isAutoPlay);


        if (mOnClickListener != null) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                  Log.i("limit", "dispatchTouchEvent: paly");
                mOnClickListener.paly();
            } else if (action == MotionEvent.ACTION_DOWN) {
                if (isAutoPlay){
                    Log.i("limit", "dispatchTouchEvent: stop");
                    mOnClickListener.stop();
                }

            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isAutoPlay = true;

    public void setAutoPlay(boolean autoPlay) {
        this.isAutoPlay = autoPlay;
    }

    //接口回调
    public interface OnClickListener {
        void paly();

        void stop();
    }

    public OnClickListener mOnClickListener;

    public void setOnsClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }


}
