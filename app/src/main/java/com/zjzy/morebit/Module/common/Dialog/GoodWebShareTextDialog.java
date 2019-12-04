package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.utils.ViewShowUtils;

/**
 * web集合页分享弹窗
 */

public class GoodWebShareTextDialog extends Dialog implements View.OnClickListener{

    private Context mContext;
    private EditText title_et;
    private EditText context_et;
    private TextView cancel,ok;
    private String title;
    private String content;
    private String type = "";
    private OnCloseListener listener;

    public GoodWebShareTextDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public GoodWebShareTextDialog(Context context, int themeResId, String title) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
    }

    public GoodWebShareTextDialog(Context context, int themeResId, String title, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.listener = listener;
    }
    public GoodWebShareTextDialog(Context context, int themeResId, String title, String content, String type, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.type = type;
        this.listener = listener;
    }

    protected GoodWebShareTextDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_goodwebsharetext);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public void setTitle(String text){
        if(title_et != null){
            title_et.setText(text);
        }
    }
    public void setContent(String text){
        if(context_et != null){
            context_et.setText(text);
        }
    }

    private void initView(){
        title_et = (EditText)findViewById(R.id.title);
        context_et = (EditText)findViewById(R.id.content);
        cancel = (TextView)findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        ok = (TextView)findViewById(R.id.ok);
        ok.setOnClickListener(this);
        if(title!=null && !"".equals(title)){
            title_et.setText(title);
        }
        if(!TextUtils.isEmpty(content)){
            context_et.setText(content);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok:
                if(listener!=null){
                    String getTitle = "";
                    String getContent = "";
                    if(TextUtils.isEmpty(title_et.getText().toString().trim())){
                        getTitle = "分享标题";
                    }else{
                        getTitle = title_et.getText().toString().trim();
                    }
                    if(TextUtils.isEmpty(context_et.getText().toString().trim())){
                        getContent = "分享内容描述";
                    }else{
                        getContent = context_et.getText().toString().trim();
                    }
                    listener.onClick(GoodWebShareTextDialog.this,getTitle,getContent);
                }
                ViewShowUtils.hideSoftInput(mContext, context_et);
                this.dismiss();
                break;
            case R.id.cancel:
                this.dismiss();
                break;
        }
    }

    public interface OnCloseListener{
        void onClick(Dialog dialog, String title,String content);
    }
}