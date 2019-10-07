package com.markermall.cat.interfaces;

import android.support.annotation.DrawableRes;

/**
 * Created by YangBoTian on 2018/9/25.
 */

public interface EmptyView {
    void onError(Throwable tr);
    void setMessage(String message);
    void setIcon(@DrawableRes int drawableId);
}
