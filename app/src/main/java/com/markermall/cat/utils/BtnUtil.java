package com.markermall.cat.utils;

import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/12/14.
 */

public class BtnUtil {

    public BtnUtil(){

    }

    /**
     * 设置按钮可以拖动
     * @param view
     * @param screenWidth
     * @param screenHeight
     * @param rootView
     */
    public void setBtnToMove(View view,final int screenWidth,final int screenHeight,View rootView,final OnMoveBtnClickListener onMoveBtnClickListener){
        final BtnMoveTouchListener btnMoveTouchListener = new BtnMoveTouchListener(screenWidth,screenHeight,rootView);
        view.setOnTouchListener(btnMoveTouchListener);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onMoveBtnClickListener!=null){
                    if (btnMoveTouchListener.getClickOrMove()) {
                        onMoveBtnClickListener.onClick();
                    }
                }
            }
        });
    }

    public interface OnMoveBtnClickListener{
        public void onClick();
    }

    public class BtnMoveTouchListener implements View.OnTouchListener{
        int lastX, lastY; // 记录移动的最后的位置
        int downX;
        int downY;
        public boolean clickormove = false;
        int screenWidth;
        int screenHeight;
        View rootView;

        public BtnMoveTouchListener(int screenWidth,int screenHeight,View rootView){
             this.screenWidth = screenWidth;
             this.screenHeight = screenHeight;
             this.rootView = rootView;
        }

        public boolean getClickOrMove(){
            return clickormove;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int ea = event.getAction();//获取事件类型
            switch (ea) {
                case MotionEvent.ACTION_DOWN: // 按下事件
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    downX = lastX;
                    downY = lastY;
                    break;
                case MotionEvent.ACTION_MOVE: // 拖动事件
                    // 移动中动态设置位置
                    int dx = (int) event.getRawX() - lastX;//位移量X
                    int dy = (int) event.getRawY() - lastY;//位移量Y
                    int left = v.getLeft() + dx;
                    int top = v.getTop() + dy;
                    int right = v.getRight() + dx;
                    int bottom = v.getBottom() + dy;

                    //++限定按钮被拖动的范围
                    if (left < 0) {
                        left = 0;
                        right = left + v.getWidth();
                    }
                    if (right > screenWidth) {
                        right = screenWidth;
                        left = right - v.getWidth();
                    }
                    if (top < 0) {
                        top = 0;
                        bottom = top + v.getHeight();
                    }
                    if (bottom > rootView.getHeight()) {
                        bottom = rootView.getHeight();
                        top = bottom - v.getHeight();
                    }
                    //--限定按钮被拖动的范围
//                        v.layout(left, top, right, bottom);//按钮重画
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    layoutParams.setMargins(left, top, screenWidth-right, screenHeight-bottom);
                    v.setLayoutParams(layoutParams);

                    // 记录当前的位置
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_UP: // 弹起事件
                    //判断是单击事件或是拖动事件，位移量大于5则断定为拖动事件
                    if (Math.abs((int) (event.getRawX() - downX)) > 5
                            || Math.abs((int) (event.getRawY() - downY)) > 5)
                        clickormove = false;
                    else
                        clickormove = true;
                    break;
            }
            return false;
        }
    }
}
