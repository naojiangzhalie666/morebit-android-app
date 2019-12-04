package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.zjzy.morebit.Module.common.View.LoadingView;
import com.zjzy.morebit.R;

/**
 * Created by Administrator on 2017/10/23.
 */

public class LoadingDialog extends Dialog implements View.OnClickListener{

    private Context mContext;
    private LoadingView loadView;
    private String text;

    public LoadingDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public LoadingDialog(Context context, int themeResId, String text) {
        super(context, themeResId);
        this.mContext = context;
        this.text = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(false);
        initView();
    }


    private void initView(){
        loadView = (LoadingView)findViewById(R.id.loadView);
        if(text!=null && !"".equals(text)){
            loadView.setLoadingText(text);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok:
                break;
            case R.id.cancel:
                break;
        }
    }

}