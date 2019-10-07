package com.markermall.cat.Module.common.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.anthony.ultimateswipetool.SwipeHelper;
import com.anthony.ultimateswipetool.activity.SwipeBackLayout;
import com.anthony.ultimateswipetool.activity.interfaces.SwipeBackActivityBase;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by fengrs on 2018/7/7.
 */

public class SwipeBaseActivity extends RxAppCompatActivity implements SwipeBackActivityBase {

    private SwipeHelper mHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isSwipe()) {
            this.mHelper = new SwipeHelper(this);
            this.mHelper.onActivityCreate();
        }
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mHelper != null) {
            this.mHelper.onPostCreate();
        }
    }

    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (mHelper != null) {
            return v == null && this.mHelper != null ? this.mHelper.findViewById(id) : v;
        } else {
            return v;
        }
    }

    public SwipeBackLayout getSwipeBackLayout() {
        if (mHelper != null) {
            return this.mHelper.getSwipeBackLayout();
        }
        return null;
    }

    public void setSwipeBackEnable(boolean enable) {
        if (this.getSwipeBackLayout() != null) {
            this.getSwipeBackLayout().setEnableGesture(enable);
        }
    }

    public void scrollToFinishActivity() {
        SwipeHelper.convertActivityToTranslucent(this);
        if (this.getSwipeBackLayout() != null) {
            this.getSwipeBackLayout().scrollToFinishActivity();
        }
    }

    public void setScrollDirection(int edgeFlags) {
        if (this.getSwipeBackLayout() != null) {
            this.getSwipeBackLayout().setEdgeTrackingEnabled(edgeFlags);
        }
    }

    /**
     * 是否滑动
     *
     * @return
     */
    public boolean isSwipe() {
        return true;
    }
}
