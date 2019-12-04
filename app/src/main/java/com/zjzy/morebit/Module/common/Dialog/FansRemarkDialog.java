package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.utils.MyLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 我的团队中二维码弹窗
 */

public class FansRemarkDialog extends Dialog {


    private Context mContext;
   private EditText edt_remark;
     private TextView tv_cancel;
     private TextView tv_confirm;
     OnClickListener onClickListener;
    private  int position;
    public FansRemarkDialog(Context context) {
        super(context, R.style.dialog);
        this.mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fans_remark);
        setCanceledOnTouchOutside(true);
        initView();
    }

    private void initView() {
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_confirm = findViewById(R.id.tv_confirm);
        edt_remark = findViewById(R.id.edt_remark);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null){
                    onClickListener.onClick(v,edt_remark.getText().toString().trim(),position);
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
//        edt_remark.setFilters(new InputFilter[]{emojiFilter});

        edt_remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                MyLog.i("test","beforeTextChanged: " +s.toString());
//                if(s.length()>=12){
//                    return;
//                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MyLog.i("test","onTextChanged: " +s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                MyLog.i("test","onTextChanged: " +s.toString());
            }
        });
    }


    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setRemarkAnPosition(String remark,int position){
        if(edt_remark!=null){
            edt_remark.setText(remark);
        }
        this.position = position;
    }

    public  interface OnClickListener{
        void onClick(View v,String remark,int position);
    }

    InputFilter emojiFilter = new InputFilter() {
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                return "";
            }
            return null;
        }
    };
}