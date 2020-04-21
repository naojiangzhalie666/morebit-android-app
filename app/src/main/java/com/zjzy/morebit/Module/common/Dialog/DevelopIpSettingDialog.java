package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.R;

public class DevelopIpSettingDialog extends Dialog {
    private TextView prod_tv,test_ip,dev_ip;
    private DevelopIpSettingDialog.OnProdListener mProdListener;
    private DevelopIpSettingDialog.OnTestListner mTestListener;
    private DevelopIpSettingDialog.OnDevListner mDevListener;

    public DevelopIpSettingDialog(Context context) {
        super(context, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_develop_setting_layout);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        prod_tv = findViewById(R.id.prod_tv);
        test_ip = findViewById(R.id.test_ip);
        dev_ip=findViewById(R.id.dev_ip);
        prod_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProdListener != null) {
                    mProdListener.onClick(v);
                    dismiss();
                }
            }
        });
        test_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTestListener != null) {
                    mTestListener.onClick(v);
                    dismiss();
                }
            }
        });
        dev_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDevListener != null) {
                    mDevListener.onClick(v);
                    dismiss();
                }
            }
        });
    }


    public interface OnProdListener {
        void onClick(View view);
    }

    public interface OnTestListner {
        void onClick(View view);
    }
    public interface OnDevListner {
        void onClick(View view);
    }
    public void setmProdistener(DevelopIpSettingDialog.OnProdListener mProdListener) {
        this.mProdListener = mProdListener;
    }

    public void setmTestListener(DevelopIpSettingDialog.OnTestListner mTestListener) {
        this.mTestListener = mTestListener;
    }
    public void setmDevListener(DevelopIpSettingDialog.OnDevListner mDevistener) {
        this.mDevListener = mDevistener;
    }
}
