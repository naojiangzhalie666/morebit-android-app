package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.R;


/**
 * Created by YangBoTian on 2019/3/18.
 */

public class PurchaseRuleDialog extends Dialog {


    private OnCancelListner mCancelListener;
    private ClearSDdataDialog.OnOkListener okListener;
    private ImageView btn_close;
    private String url;

    public PurchaseRuleDialog(RxAppCompatActivity activity,String url) {
        super(activity, R.style.dialog);
        this.url=url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_purchaserule);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        btn_close=findViewById(R.id.btn_close);
        WebView webView = findViewById(R.id.webview);
        webView.getSettings().setGeolocationEnabled(true);
        //启用支持DOM Storage
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl("https://blog.csdn.net/RoseChilde/article/details/102576994");
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCancelListener != null) {
                    mCancelListener.onClick(v);
                    dismiss();
                }
            }
        });
    }


    public interface OnOkListener {
        void onClick(View view);
    }

    public interface OnCancelListner {
        void onClick(View view);
    }

    public void setmCancelListener(OnCancelListner mCancelListener) {
        this.mCancelListener = mCancelListener;
    }
}
