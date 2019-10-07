package com.markermall.cat.utils;

import android.support.v4.app.FragmentActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by JerryHo on 2019/1/14
 * Description: 隐藏显示虚拟键盘
 */
public class SoftInputUtil {

    public static void hideSoftInputAt(EditText et) {

        if (et != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) et.getContext().getSystemService(FragmentActivity.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null)
                inputMethodManager.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }

    public static void showSoftInputAt(EditText et) {

        if (et != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) et.getContext().getSystemService(FragmentActivity.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null)
                inputMethodManager.showSoftInput(et, InputMethodManager.SHOW_FORCED);
        }
    }

}
