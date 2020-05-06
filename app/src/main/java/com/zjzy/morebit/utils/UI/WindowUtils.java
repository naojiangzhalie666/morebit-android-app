package com.zjzy.morebit.utils.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.zjzy.morebit.R;

/**
 * 弹窗辅助类
 *
 * @ClassName WindowUtils
 */
public class WindowUtils {
    private static final String LOG_TAG = "WindowUtils";

    /**
     * 显示弹出框
     *
     * @param context
     */
    public static void showPopupWindow(final Context context) {

        View view = setUpView(context);
        view.setId(R.id.view_not_work_link_error);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 类型
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        // 设置flag
        int flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;

        // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;

//    params.format = PixelFormat.FLAG_NOT_TOUCH_MODAL;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.MATCH_PARENT;
        ViewGroup sDecorView = (ViewGroup) ((Activity) context).getWindow().getDecorView();
        if (sDecorView != null) {
            boolean isadd = isIsadd(sDecorView);
            if (!isadd) {
                sDecorView.addView(view, params);
            }
        }
    }

    private static boolean isIsadd(ViewGroup sDecorView) {
        boolean isadd = false;
        for (int i = 0; i < sDecorView.getChildCount(); i++) {
            View childAt = sDecorView.getChildAt(i);
            if (childAt.getId() == R.id.view_not_work_link_error) {
                isadd = true;
            }
        }
        return isadd;
    }

    private static View getAddView(ViewGroup sDecorView) {
        View view = null;
        for (int i = 0; i < sDecorView.getChildCount(); i++) {
            View childAt = sDecorView.getChildAt(i);
            if (childAt.getId() == R.id.view_not_work_link_error) {
                view = childAt;
            }
        }
        return view;
    }

    /**
     * 隐藏弹出框
     */
    public static void hidePopupWindow(final Context context) {

        ViewGroup sDecorView = (ViewGroup) ((Activity) context).getWindow().getDecorView();
        boolean isadd = isIsadd(sDecorView);
        if (sDecorView != null && isadd) {
            View addView = getAddView(sDecorView);
            if (addView != null) {
                sDecorView.removeView(addView);
            }
        }
    }


    public static View setUpView(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.broken_net,
                null);
//        view.findViewById(R.id.rl_net).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
//                context.startActivity(intent);
//            }
//        });
//        view.setFocusableInTouchMode(true);
//    positiveBtn.setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        // 打开安装包
//        // 隐藏弹窗
//        WindowUtils.hidePopupWindow();
//      }
//    });
//    Button negativeBtn = (Button) view.findViewById(R.id.iv_icon);
//    negativeBtn.setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        WindowUtils.hidePopupWindow();
//      }
//    });
        // 点击窗口外部区域可消除
        // 这点的实现主要将悬浮窗设置为全屏大小，外层有个透明背景，中间一部分视为内容区域
        // 所以点击内容区域外部视为点击悬浮窗外部
//    final View popupWindowView = view.findViewById(R.id.iv_icon);// 非透明的内容区域
//    view.setOnTouchListener(new View.OnTouchListener() {
//      @Override
//      public boolean onTouch(View v, MotionEvent event) {
//        int x = (int) event.getX();
//        int y = (int) event.getY();
//        Rect rect = new Rect();
//        popupWindowView.getGlobalVisibleRect(rect);
//        if (!rect.contains(x, y)) {
//          WindowUtils.hidePopupWindow();
//        }
//        return true;
//      }
//    });
//    // 点击back键可消除
//        view.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                return true;
//            }
//        });
        return view;
    }
}