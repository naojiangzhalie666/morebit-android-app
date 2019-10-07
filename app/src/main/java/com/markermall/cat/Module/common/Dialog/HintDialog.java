package com.markermall.cat.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.markermall.cat.R;

/**
 * Created by Administrator on 2017/10/23.
 */

public class HintDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView title_tv;
    private TextView cancel;
    private String title;
    private String content;
    private @StringRes int mText;



    private TextView edtext;

    public HintDialog(Context context, int themeResId, String title, String content ) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
    }


    public HintDialog(Context context, int themeResId, String title,@StringRes int text) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        mText = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_hint);
        setCanceledOnTouchOutside(false);
        initView();
    }




    private void initView() {
        title_tv = (TextView) findViewById(R.id.title_tv);
        edtext = (TextView) findViewById(R.id.edtext);
        if (!TextUtils.isEmpty(content)) {
            edtext.setText(content);
        } else {
            edtext.setText(mText);
        }
        if(!TextUtils.isEmpty(title)){
            title_tv.setText(title);
        }
        cancel = (TextView) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
    }

    public void setContentText(String content){
        if(edtext!=null){
            edtext.setText(content);
        }
    }

    public void setContentText(@StringRes int content){
        if(edtext!=null){
            edtext.setText(content);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                this.dismiss();
                break;
        }
    }

}