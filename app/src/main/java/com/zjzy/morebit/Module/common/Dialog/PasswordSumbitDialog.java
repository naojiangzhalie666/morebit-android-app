package com.zjzy.morebit.Module.common.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zjzy.morebit.R;

/**
 - @Description:  找回密码提交成功
 - @Author:  wuchaowen
 - @Time:  2019/4/17 11:36
 **/
public class PasswordSumbitDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private View mView;
    private ImageView image;


    private PasswordSumbitDialog.OnListener mListener;

    public PasswordSumbitDialog(@NonNull Context context) {
        super(context, 0);
    }

    public PasswordSumbitDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        initView();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
        setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ((Activity)mContext).getWindowManager().getDefaultDisplay().getWidth()*3/4;
        getWindow().setAttributes(params);
    }

    private void initView() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_login_password_sumbit, null);
        image = mView.findViewById(R.id.image);
        image.setOnClickListener(this);
    }



    public PasswordSumbitDialog setOnListner(PasswordSumbitDialog.OnListener listener) {
        this.mListener = listener;
        return this;
    }



    @Override
    public void show() {
        if(!isShowing()){
            super.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image:
                if (mListener != null) {
                    mListener.onClick();
                }
                this.dismiss();
                break;
        }
    }

    public interface OnListener {

        void onClick();
    }
}