package com.markermall.cat.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.markermall.cat.R;
import com.markermall.cat.utils.Validator;
import com.markermall.cat.utils.ViewShowUtils;

/**
 * Created by Administrator on 2017/10/23.
 */

public class EditOneLineTextDialog extends Dialog implements View.OnClickListener{

    private Context mContext;
    private TextView title_tv;
    private EditText edtext;
    private TextView cancel,ok;
    private String title;
    private String content;
    private String type = "";
    private OnCloseListener listener;

    public EditOneLineTextDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public EditOneLineTextDialog(Context context, int themeResId, String title) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
    }

    public EditOneLineTextDialog(Context context, int themeResId, String title,String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.listener = listener;
    }
    public EditOneLineTextDialog(Context context, int themeResId, String title,String content,String type, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.type = type;
        this.listener = listener;
    }

    protected EditOneLineTextDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_editonelinetext);
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
        edtext = (EditText)findViewById(R.id.edtext);
        cancel = (TextView)findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        ok = (TextView)findViewById(R.id.ok);
        ok.setOnClickListener(this);
        if(title!=null && !"".equals(title)){
            title_tv.setText(title);
        }
        if(!TextUtils.isEmpty(content)){
            edtext.setText(content);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok:
                if(TextUtils.isEmpty(edtext.getText().toString().trim())){
                    ViewShowUtils.showShortToast(mContext,"请输入内容");
                    return;
                }
                if("password".equals(type)){
                    if(!Validator.isPassword(edtext.getText().toString().trim())){
                        ViewShowUtils.showShortToast(mContext,"请输入不少于6位的字母、数字的密码");
                        return;
                    }
                }
                if("email".equals(type)){
                    if(!Validator.isEmail(edtext.getText().toString().trim())){
                        ViewShowUtils.showShortToast(mContext,"请输入正确的邮箱");
                        return;
                    }
                }
                if(listener != null){
                    listener.onClick(this, edtext.getText().toString().trim());
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