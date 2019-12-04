package com.zjzy.morebit.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zjzy.morebit.R;

/**
 * 提交免单淘宝订单号
 */

public class BaseTextDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView title_tv, tv_order;
    private TextView cancel, ok;
    private String title;
    private String content = "";
    private OnCloseListener listener;
    private String titleText;
    private String cancelText;
    private String contextText;
    private String onText;

    public BaseTextDialog(Context context) {
        super(context, R.style.dialog);
        this.mContext = context;
    }

    public BaseTextDialog(Context context, int themeResId, String title) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
    }

    public BaseTextDialog(Context context, int themeResId, String title, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.listener = listener;
    }

    public BaseTextDialog(Context context, int themeResId, String title, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.listener = listener;
    }

    protected BaseTextDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_base_text);
        setCanceledOnTouchOutside(false);
        initView();
    }

    @Override
    public void show() {
        super.show();
        if (title_tv != null) {
            if (TextUtils.isEmpty(titleText)) {
                title_tv.setVisibility(View.GONE);
            } else {
                title_tv.setVisibility(View.VISIBLE);
                title_tv.setText(titleText);
            }
        }
        if (cancel != null) {
            if (TextUtils.isEmpty(cancelText)) {
                cancel.setVisibility(View.GONE);
            } else {
                cancel.setVisibility(View.VISIBLE);
                cancel.setText(cancelText);
            }
        }
        if (tv_order != null) {
            if (TextUtils.isEmpty(contextText)) {
                tv_order.setVisibility(View.GONE);
            } else {
                tv_order.setVisibility(View.VISIBLE);
                tv_order.setText(contextText);
            }
        }
        if (ok != null) {
            if (TextUtils.isEmpty(onText)) {
                ok.setVisibility(View.GONE);
            } else {
                ok.setVisibility(View.VISIBLE);
                ok.setText(onText);
            }
        }
    }

    public BaseTextDialog setTitle(String text) {
        this.titleText = text;
        return this;
    }

    public BaseTextDialog setCancel(String text) {
        this.cancelText = text;

        return this;
    }

    public BaseTextDialog setContent(String text) {
        this.contextText = text;
        return this;
    }

    public void setOnCloseListener(OnCloseListener listener) {
        this.listener = listener;
    }

    public BaseTextDialog setOk(String text) {
        this.onText = text;
        return this;
    }


    private void initView() {
        title_tv = (TextView) findViewById(R.id.title_tv);
        cancel = (TextView) findViewById(R.id.cancel);
        tv_order = (TextView) findViewById(R.id.tv_order);
        cancel.setOnClickListener(this);
        ok = (TextView) findViewById(R.id.ok);
        ok.setOnClickListener(this);
//        if (TextUtils.isEmpty(title)) {
//            edtext.setVisibility(View.GONE);
//
//        }
//        if (TextUtils.isEmpty(title)) {
//            edtext.setVisibility(View.GONE);
//        }
//        if (TextUtils.isEmpty(title)) {
//            edtext.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                if (listener != null) {
                    listener.onClick(this, "");
                }
                this.dismiss();
                break;
            case R.id.cancel:
                this.dismiss();
                break;
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, String text);
    }


}