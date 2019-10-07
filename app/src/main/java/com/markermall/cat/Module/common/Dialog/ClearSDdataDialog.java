package com.markermall.cat.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.markermall.cat.R;

/**
 * Created by Administrator on 2017/10/23.
 */

public class ClearSDdataDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView title_tv;
    private TextView cancel, ok;
    private String title;
    private String content;
    private String type = "";
    private OnOkListener okListener;
    private int okTextColor = R.color.color_808080;
    private String okText = "确定";
    private int cancelTextColor = R.color.color_808080;
    private String cancelText = "取消";
    private boolean mIsShowOk = true;
    private boolean mIsShowCancel = true;

    public void setOnCancelListner(OnCancelListner onCancelListner) {
        this.onCancelListner = onCancelListner;
    }

    private OnCancelListner onCancelListner;
    private TextView edtext;


    public ClearSDdataDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public ClearSDdataDialog(Context context, int themeResId, String title) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
    }

    public ClearSDdataDialog(Context context, int themeResId, String title, String content, OnOkListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.okListener = listener;
    }

    public ClearSDdataDialog(Context context, int themeResId, String title, String content ) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
    }

    public ClearSDdataDialog(Context context, int themeResId, String title, String content, String type, OnOkListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.type = type;
        this.okListener = listener;
    }

    protected ClearSDdataDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_clearsddata);
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
        title_tv = (TextView) findViewById(R.id.title_tv);
        edtext = (TextView) findViewById(R.id.edtext);
        if (!TextUtils.isEmpty(content)) {
            edtext.setText(content);
        }

        cancel = (TextView) findViewById(R.id.cancel);
            ok = (TextView) findViewById(R.id.ok);
        if (mIsShowOk) {
            ok.setOnClickListener(this);
            ok.setTextColor(ContextCompat.getColor(mContext, okTextColor));
            ok.setText(okText);
            ok.setVisibility(View.VISIBLE);
        } else {
            ok.setVisibility(View.GONE);
        }
        if (mIsShowCancel) {
            cancel.setOnClickListener(this);
            cancel.setText(cancelText);
            cancel.setTextColor(ContextCompat.getColor(mContext, cancelTextColor));
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

    public void goneOk() {
        mIsShowOk = false;
    }

    public void goneCancel() {
        mIsShowCancel = false;

    }

}