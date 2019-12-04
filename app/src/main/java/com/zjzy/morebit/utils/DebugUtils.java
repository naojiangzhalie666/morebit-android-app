package com.zjzy.morebit.utils;

import android.support.annotation.IdRes;
import android.view.View;

/**
 * Created by YangBoTian on 2018/6/30.
 */

public class DebugUtils {
    public static String getResourceEntryName(View view, @IdRes int id) {
        if (view.isInEditMode()) {
            return "<unavailable while editing>";
        }
        return view.getContext().getResources().getResourceEntryName(id);
    }
}
