package com.markermall.cat.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.markermall.cat.R;

/**
 * Created by Administrator on 2017/10/23.
 */

public class EditSexSelectDialog extends Dialog implements View.OnClickListener{

    private Context mContext;
    private TextView title_tv;
    private EditText edtext;
    private TextView cancel,ok;
    private String title;
    private String sex = "男";
    private String content = "";
    private OnCloseListener listener;

    public EditSexSelectDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public EditSexSelectDialog(Context context, int themeResId, String title) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
    }

    public EditSexSelectDialog(Context context, int themeResId, String title, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.listener = listener;
    }
    public EditSexSelectDialog(Context context, int themeResId, String title,OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.listener = listener;
    }

    protected EditSexSelectDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sexselect);
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
        ok = (TextView)findViewById(R.id.ok);
        ok.setOnClickListener(this);
        if(title!=null && !"".equals(title)){
            title_tv.setText(title);
        }
        RadioGroup group = (RadioGroup)this.findViewById(R.id.radioGroup);
        //绑定一个匿名监听器
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                //获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
                if(radioButtonId == R.id.radioMale){  //男
                    sex = "男";
                }
                if(radioButtonId == R.id.radioFemale){  //女
                    sex = "女";
                }
            }
        });
        if(content!=null && !"".equals(content)){
            if("男".equals(content)){
                sex = "男";
                group.check(R.id.radioMale);
            }
            if("女".equals(content)){
                sex = "女";
                group.check(R.id.radioFemale);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok:
                if(listener != null){
                    listener.onClick(this, sex);
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