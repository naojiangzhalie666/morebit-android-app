package com.zjzy.morebit.Module.common.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.zjzy.morebit.R;
import com.zjzy.morebit.utils.AppUtil;


/**
 * Created by fengrs on 2019/5/14.
 */

public class OpenPushDialog extends Dialog {


    private final Activity mActivity;

    public OpenPushDialog(Activity activity) {
        super(activity, R.style.dialog);
        this.mActivity= activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_open_push);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.tv_to_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.gotoSet(mActivity);
            }
        });

    }
}
