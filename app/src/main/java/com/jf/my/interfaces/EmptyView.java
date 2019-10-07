package com.jf.my.interfaces;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * Created by YangBoTian on 2018/9/25.
 */

public interface EmptyView {
    void onError(Throwable tr);
    void setMessage(String message);
    void setIcon(@DrawableRes int drawableId);
}
