package com.jf.my.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


/**
 * Created by Administrator on 2017/10/17.
 */

public class ViewShowUtils {

    public static void showShortToast(Context context, String text) {
        try {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showShortToast(Context context, int textRes) {
        try {
            String string = context.getResources().getString(textRes);
            Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showLongToast(Context context, String text) {
        try {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏键盘输入
     *
     * @param context
     * @param view
     */
    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    /**
     * 显示键盘输入
     *
     * @param context
     * @param view
     */
    public static void showSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 复制内容到粘贴版
     */
    public static void copyText(Context context, String text, String showMsg) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(text);
        if (!TextUtils.isEmpty(showMsg)) {
            Toast.makeText(context, showMsg, Toast.LENGTH_SHORT).show();
        }
    }
}
