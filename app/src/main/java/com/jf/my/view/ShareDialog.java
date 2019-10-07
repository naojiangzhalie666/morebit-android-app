package com.jf.my.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.jf.my.R;

/**
 * Created by YangBoTian on 2019/5/13.
 */

public class ShareDialog extends Dialog {
    private View mView;
    private View.OnClickListener mOnClick;
    private Context mContext;

    public ShareDialog(Context context, View.OnClickListener onClick) {
        super(context, R.style.dialog);
        mOnClick = onClick;
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {

        //设置按钮监听
        findViewById(R.id.weixinFriend).setOnClickListener(mOnClick);
        findViewById(R.id.weixinCircle).setOnClickListener(mOnClick);
        findViewById(R.id.sinaWeibo).setOnClickListener(mOnClick);
        findViewById(R.id.qqFriend).setOnClickListener(mOnClick);
        findViewById(R.id.qqRoom).setOnClickListener(mOnClick);
        findViewById(R.id.rl_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        WindowManager windowManager = ((Activity)mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int)(display.getWidth()); //设置宽度
        getWindow().setAttributes(lp);
    }
}
