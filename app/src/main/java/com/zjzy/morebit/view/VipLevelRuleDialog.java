package com.zjzy.morebit.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zjzy.morebit.R;

/**
 * 会员等级弹窗
 */

public class VipLevelRuleDialog extends Dialog implements View.OnClickListener{

    private Context mContext;
    private TextView title_tv;
    private EditText edtext;
    private TextView cancel,ok,fans_title,fans_progress;
    private String title;
    private String fansTitle = "";
    private String fansProgress = "";
    private OnCloseListener listener;

    public VipLevelRuleDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public VipLevelRuleDialog(Context context, int themeResId, String title) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
    }

    public VipLevelRuleDialog(Context context, int themeResId, String title, String fansTitle,String fansProgress, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.fansTitle = fansTitle;
        this.fansProgress = fansProgress;
        this.listener = listener;
    }
    public VipLevelRuleDialog(Context context, int themeResId, String title, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.listener = listener;
    }

    protected VipLevelRuleDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_viplevel_rule);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public void setTitle(String text){
        if(title_tv != null){
            title_tv.setText(text);
        }
    }
    public void setContent(String text){
        if(edtext != null){
            edtext.setText(text);
        }
    }

    private void initView(){
        title_tv = (TextView)findViewById(R.id.title_tv);
        cancel = (TextView)findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        fans_title = (TextView)findViewById(R.id.fans_title);
        fans_progress = (TextView)findViewById(R.id.fans_progress);
//        ok = (TextView)findViewById(R.id.ok);
//        ok.setOnClickListener(this);
        if(title!=null && !"".equals(title)){
            title_tv.setText(title);
        }
        if(fansTitle!=null && !"".equals(fansTitle)){
            fans_title.setText(fansTitle);
        }
        if(fansProgress!=null && !"".equals(fansProgress)){
            fans_progress.setText(fansProgress);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok:
                if(listener != null){
                    listener.onClick(this, "");
                }
                this.dismiss();
                break;
            case R.id.cancel:
                this.dismiss();
                break;
        }
    }

    public interface OnCloseListener{
        void onClick(Dialog dialog, String text);
    }

}