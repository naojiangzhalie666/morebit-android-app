package com.zjzy.morebit.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.utils.AppUtil;

/**
 * 商品搜索弹窗
 */

public class SearchGoodsDialog extends Dialog implements View.OnClickListener{

    private Context mContext;
    private TextView title_tv;
    private TextView cancel,ok;
    private String title;
    private String content;
    private String type = "";
    private OnCloseListener listener;
    private TextView edtext;
    private LinearLayout closeLay;


    public SearchGoodsDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public SearchGoodsDialog(Context context, int themeResId, String title) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
    }

    public SearchGoodsDialog(Context context, int themeResId, String title, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.listener = listener;
    }
    public SearchGoodsDialog(Context context, int themeResId, String title, String content, String type, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.type = type;
        this.listener = listener;
    }

    protected SearchGoodsDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_searchgoods);
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
        edtext = (TextView)findViewById(R.id.edtext);
        closeLay=findViewById(R.id.closeLay);
        if(!TextUtils.isEmpty(content)){
            edtext.setText(content);
        }
        cancel = (TextView)findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        ok = (TextView)findViewById(R.id.ok);
        ok.setOnClickListener(this);
        closeLay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok:
                if(listener != null){
                    listener.onClick(this,"");
                }
                this.dismiss();
                AppUtil.clearClipboard((Activity) mContext);
                break;
            case R.id.cancel:
                this.dismiss();
                AppUtil.clearClipboard((Activity) mContext);
                break;
            case R.id.closeLay:
                this.dismiss();
                AppUtil.clearClipboard((Activity) mContext);
                break;
        }
    }

    public interface OnCloseListener{
        void onClick(Dialog dialog, String text);
    }
}