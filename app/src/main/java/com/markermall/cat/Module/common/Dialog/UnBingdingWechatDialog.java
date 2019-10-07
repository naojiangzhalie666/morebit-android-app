package com.markermall.cat.Module.common.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.markermall.cat.R;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


/**
 * Created by fengrs on 2019/9/6.
 * 解绑微信
 */

public class UnBingdingWechatDialog extends Dialog {

    private TextView mBtncancel;
    private TextView mBtnConfirm;
    private OnOkListener mOkListener;
    private OnCancelListner mCancelListener;
    private ClearSDdataDialog.OnOkListener okListener;

    public UnBingdingWechatDialog(RxAppCompatActivity activity) {
        super(activity, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_unbinding_wx);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mBtnConfirm = findViewById(R.id.btn_confirm);
        mBtncancel = findViewById(R.id.btn_cancel);
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOkListener != null) {
                    mOkListener.onClick(v);
                }
            }
        });
        mBtncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mCancelListener != null) {
                    mCancelListener.onClick(v);
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

    public void setmOkListener(OnOkListener mOkListener) {
        this.mOkListener = mOkListener;
    }

    public void setmCancelListener(OnCancelListner mCancelListener) {
        this.mCancelListener = mCancelListener;
    }
}
