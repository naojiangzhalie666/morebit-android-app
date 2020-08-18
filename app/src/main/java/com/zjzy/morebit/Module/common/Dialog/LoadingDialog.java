package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;


import com.zjzy.morebit.R;
import com.zjzy.morebit.utils.LoadingView;

/**
 * Created by Administrator on 2017/10/23.
 */

public class LoadingDialog extends Dialog {

    private Context mContext;
    private LoadingView logingView;
    private String text;

    public LoadingDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public LoadingDialog(Context context, int themeResId, String text) {
        super(context, themeResId);
        this.mContext = context;
        this.text = text;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        logingView=findViewById(R.id.logingView);
        setCanceledOnTouchOutside(false);

    }


    @Override
    public void show() {
        super.show();
        logingView.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        logingView.stop();
    }
}