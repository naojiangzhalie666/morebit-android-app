package com.markermall.cat.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.markermall.cat.R;

/**
 * 消费佣金规则说明
 */

public class ConsCommissionRuleDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView title_tv;
    private EditText edtext;
    private TextView cancel, ok;
    private String title;
    private String content = "";
    private OnCloseListener listener;
    private TextView tv_content;

    public ConsCommissionRuleDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public ConsCommissionRuleDialog(Context context, int themeResId, String title) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
    }

    public ConsCommissionRuleDialog(Context context, int themeResId, String title, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.listener = listener;
    }

    public ConsCommissionRuleDialog(Context context, int themeResId, String title, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.listener = listener;
    }

    protected ConsCommissionRuleDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_conscommission_rule);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public void setTitle(String text) {
        if (title_tv != null) {
            title_tv.setText(text);
        }
    }

    public void setContent(String text) {
        if (edtext != null) {
            edtext.setText(text);
        }
    }

    private void initView() {
        title_tv = (TextView) findViewById(R.id.title_tv);
        cancel = (TextView) findViewById(R.id.cancel);
        tv_content = (TextView) findViewById(R.id.tv_content);
        cancel.setOnClickListener(this);
//        ok = (TextView)findViewById(R.id.ok);
//        ok.setOnClickListener(this);
        if (title != null && !"".equals(title)) {
//            title_tv.setText(title);
        }
        if (!TextUtils.isEmpty(content))
        tv_content.setText( content );
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