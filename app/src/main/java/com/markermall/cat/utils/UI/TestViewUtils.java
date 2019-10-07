package com.markermall.cat.utils.UI;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;

/**
 * Created by fengrs on 2018/7/28.
 * textview 的处理工具类
 */

public class TestViewUtils {

    /**
     * 设置字体背景颜色
     *
     * @param text
     * @param color
     * @param end
     * @return
     */
    public static SpannableStringBuilder getColorSpannable(String text, int color, int end) {
        return getColorSpannable(text, color, 0, end);

    }

    /**
     * 设置字体背景颜色
     *
     * @param text
     * @param color
     * @param start
     * @param end
     * @return
     */
    public static SpannableStringBuilder getColorSpannable(String text, int color, int start, int end) {

        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(text);
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(color);
        spannableStringBuilder2.setSpan(backgroundColorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder2;

    }

}
