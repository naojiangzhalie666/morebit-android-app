package com.zjzy.morebit.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.LocalData.CommonLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.MarkermallCircleAdapter;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.helper.ActivityLifeHelper;

/**
 * 授权用户隐私弹窗
 */

public class AuthForPersonDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView title_tv;
    private EditText edtext;
    private TextView cancel, ok;
    private String title;
    private String content = "";
    private OnAuthListener listener;

    private TextView tvUserPrivate;
    private TextView tvPrivate;

    public AuthForPersonDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public AuthForPersonDialog(Context context, int themeResId, String title) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
    }

    public AuthForPersonDialog(Context context,  OnAuthListener listener) {
        super(context);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.listener = listener;
    }

    public AuthForPersonDialog(Context context, int themeResId,  OnAuthListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
    }

    protected AuthForPersonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_person_private);
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

        tvUserPrivate = (TextView)findViewById(R.id.ll_userAgreement);
        tvUserPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUtil.getUserProtocolForHome((BaseActivity) mContext);
            }
        });
        tvPrivate =(TextView)findViewById(R.id.privateProtocol);
        tvPrivate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                LoginUtil.getPrivateProtocolForHome((BaseActivity) mContext);
            }
        });
        cancel = (TextView) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        ok = (TextView)findViewById(R.id.ok);
        ok.setOnClickListener(this);
//        if (!TextUtils.isEmpty(content))
//        tv_content.setText( content );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                if (listener != null) {
                    listener.onAuthClick();
                }
                this.dismiss();
                break;
            case R.id.cancel:
                this.dismiss();
                listener.onNoAuthClick();
                break;
        }
    }

    public interface OnAuthListener {
        void onAuthClick();
        void onNoAuthClick();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //虚拟的返回键
            if (!CommonLocalData.getAuthedStatus()){
                // 清空数据
                MarkermallCircleAdapter.mShareCountId = 0;
                ActivityLifeHelper.getInstance().finishAllActivities();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}