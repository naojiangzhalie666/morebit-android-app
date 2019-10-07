package com.markermall.cat.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.markermall.cat.R;

/**
 * Created by Administrator on 2017/10/23.
 */

public class AppUpgradeDialog extends Dialog implements View.OnClickListener{

    private Context mContext;
    private TextView title_tv;
    private TextView cancel,ok;
    private String title;
    private String content;
    private String type = "";
    private OnCloseListener listener;
    private boolean isUpgrade = false;
    private TextView edtext;

    public AppUpgradeDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public AppUpgradeDialog(Context context, int themeResId, String title) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
    }

    public AppUpgradeDialog(Context context, int themeResId, String title,String content,boolean isUpgrade, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.isUpgrade = isUpgrade;
        this.listener = listener;
    }
    public AppUpgradeDialog(Context context, int themeResId, String title, String content, String type, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.type = type;
        this.listener = listener;
    }

    protected AppUpgradeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_appupgrade);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public void setTitle(String text){
        if(title_tv != null){
            title_tv.setText(text);
        }
    }
    public void setContent(String text){
    }

    private void initView(){
        title_tv = (TextView)findViewById(R.id.title_tv);
        cancel = (TextView)findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        ok = (TextView)findViewById(R.id.ok);
        ok.setOnClickListener(this);
        edtext = (TextView)findViewById(R.id.edtext);
        try {
            edtext.setText(content == null ? "" : Html.fromHtml(content));
            edtext.setMovementMethod(ScrollingMovementMethod.getInstance());
        }catch (Exception e){
            e.printStackTrace();
        }
        if(isUpgrade){//强制更新
            cancel.setText("退出程序");
            ok.setText("更新软件");
        }else{
            cancel.setText("取消");
            ok.setText("更新");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok:
                if(!isUpgrade) {
                    this.dismiss();
                }
                if(listener != null){
                    listener.onOk(this,isUpgrade);
                }
                break;
            case R.id.cancel:
                if(!isUpgrade) {
                    this.dismiss();
                }
                if(listener != null){
                    listener.onCancel(this,isUpgrade);
                }
                break;
        }
    }

    public interface OnCloseListener{
        void onOk(Dialog dialog, boolean isUpgrade);
        void onCancel(Dialog dialog, boolean isUpgrade);
    }
}