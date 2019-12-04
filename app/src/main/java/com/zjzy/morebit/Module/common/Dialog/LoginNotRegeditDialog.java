package com.zjzy.morebit.Module.common.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.R;

/**
 - @Description:  提示注册的dialog
 - @Author:  wuchaowen
 - @Time:  2019/4/17 15:33
 **/

public class LoginNotRegeditDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView title_tv;
    private TextView cancel, ok;
    private String title;
    private String content;
    private String type = "";
    private OnOkListener okListener;
    private int okTextColor = R.color.color_808080;
    private String okText = "注册";
    private int cancelTextColor = R.color.color_808080;
    private String cancelText = "取消";
    private boolean mIsShowOk = true;
    private boolean mIsShowCancel = true;
    private ImageView mIconIv;
    private int iconId;

    public void setOnCancelListner(OnCancelListner onCancelListner) {
        this.onCancelListner = onCancelListner;
    }

    private OnCancelListner onCancelListner;
    private TextView edtext;


    public LoginNotRegeditDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public LoginNotRegeditDialog(Context context, int themeResId, String title) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
    }

    public LoginNotRegeditDialog(Context context, int themeResId, String title, String content, OnOkListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.okListener = listener;
    }

    public LoginNotRegeditDialog(Context context, int themeResId, String title, String content ) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
    }

    public LoginNotRegeditDialog(Context context, int themeResId, String title, String content, String type, OnOkListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.type = type;
        this.okListener = listener;
    }

    protected LoginNotRegeditDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login_not_regedit);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ((Activity)mContext).getWindowManager().getDefaultDisplay().getWidth()*3/4;
        getWindow().setAttributes(params);

        setCanceledOnTouchOutside(false);
        initView();
    }

    public void setTitle(String text) {
        if (title_tv != null) {
            title_tv.setText(text);
        }
    }

    public void setContent(String text) {
    }

    private void initView() {
        mIconIv = findViewById(R.id.iconIv);
        title_tv = (TextView) findViewById(R.id.title_tv);
        edtext = (TextView) findViewById(R.id.edtext);
        if(iconId != 0){
            mIconIv.setImageResource(iconId);
        }
        if (!TextUtils.isEmpty(content)) {
            edtext.setText(content);
        }
        if(!TextUtils.isEmpty(this.title)){
            title_tv.setText(this.title);
        }

        cancel = (TextView) findViewById(R.id.cancel);
            ok = (TextView) findViewById(R.id.ok);
        if (mIsShowOk) {
            ok.setOnClickListener(this);
            ok.setText(okText);
            ok.setVisibility(View.VISIBLE);
        } else {
            ok.setVisibility(View.GONE);
        }
        if (mIsShowCancel) {
            cancel.setOnClickListener(this);
            cancel.setText(cancelText);
            cancel.setVisibility(View.VISIBLE);
        } else {
            cancel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                if (okListener != null) {
                    okListener.onClick(v, "");
                }
                this.dismiss();
                break;
            case R.id.cancel:
                if (onCancelListner != null) {
                    onCancelListner.onClick(v, "");
                }
                this.dismiss();
                break;
        }
    }

    public interface OnOkListener {
        void onClick(View view, String text);
    }

    public interface OnCancelListner {
        void onClick(View view, String text);
    }

    public void setOkTextAndColor(int color, String text) {
        if (color > 0) {
            okTextColor = color;
        }
        if (!TextUtils.isEmpty(text)) {
            okText = text;
        }
    }

    public void setCancelTextAndColor(int color, String text) {
        if (color > 0) {
            cancelTextColor = color;
        }
        if (!TextUtils.isEmpty(text)) {
            cancelText = text;
        }
    }

    public void setIcon(int id){
        iconId = id;
    }

    public void goneOk() {
        mIsShowOk = false;
    }

    public void goneCancel() {
        mIsShowCancel = false;

    }

}