package com.zjzy.morebit.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzy.morebit.R;

/**
 * Created by YangBoTian on 2019/5/13.
 */

public class CircleShareDialog extends Dialog {
    private View mView;
    private View.OnClickListener mOnClick;
    private Context mContext;
    private LinearLayout ll_dialog;
    private  int mType;

    TextView title;
    public CircleShareDialog(Context context, View.OnClickListener onClick) {
        super(context, R.style.dialog);
        mOnClick = onClick;
        mContext = context;
    }
    public CircleShareDialog(Context context, int type, View.OnClickListener onClick) {
        super(context, R.style.dialog);
        mType= type;
        mOnClick = onClick;
        mContext = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_circle);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {

        ll_dialog = findViewById(R.id.ll_dialog);


        //设置按钮监听
        findViewById(R.id.weixinFriend).setOnClickListener(mOnClick);
        findViewById(R.id.weixinCircle).setOnClickListener(mOnClick);
        findViewById(R.id.plct).setOnClickListener(mOnClick);
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
