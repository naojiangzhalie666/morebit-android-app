package com.jf.my.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

/**
 * @Author: wuchaowen
 * @Description: 判断滑动事件 纵向 父控件控制 横向 自己控制 不给父控件控制权限
 **/
public class CustomRecyclerView extends RecyclerView {
    // 起始坐标
    private float startX = 0;
    private float startY = 0;

    public CustomRecyclerView(@NonNull Context context) {
        super(context);
    }

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }




    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                getParent().requestDisallowInterceptTouchEvent(true);
                startX = event.getX();
                startY = event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                //1.来到新的坐标
                float endX = event.getX();
                float endY = event.getY(); event.getRawX();
                //2.计算偏移量
                float distanceX = endX - startX;
                float distanceY = endY - startY;
                //3.判断滑动方向
                if(Math.abs(distanceX) > Math.abs(distanceY)) {
                    //水平方向滑动
                    ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }else {
                    //竖值方向滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            }
            case MotionEvent.ACTION_UP: {
               // getParent().requestDisallowInterceptTouchEvent(false);
                break;
            }
        }


        return super.dispatchTouchEvent(event);

    }
}

