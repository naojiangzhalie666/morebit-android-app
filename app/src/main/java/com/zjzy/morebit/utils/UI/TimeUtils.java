package com.zjzy.morebit.utils.UI;

import android.os.SystemClock;

public class TimeUtils {
    private static long lastOperationTime;
    /**
     * 是否频繁触发
     *
     * @return true 是
     */
    public static boolean isFrequentOperation() {
        final long now = SystemClock.elapsedRealtime();
        final long gap = now - lastOperationTime;
        if (gap < 500) {
            return true;
        } else {
            lastOperationTime = now;
            return false;
        }
    }

}
