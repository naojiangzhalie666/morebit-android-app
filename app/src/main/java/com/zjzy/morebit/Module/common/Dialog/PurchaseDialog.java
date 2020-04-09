package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.R;

public class PurchaseDialog extends Dialog {
    private TextView mBtncancel;
    private TextView mBtnConfirm;
    private PurchaseDialog.OnOkListener mOkListener;
    private PurchaseDialog.OnCancelListner mCancelListener;
    private PurchaseDialog.OnCloseListner mCloseListener;
    private ClearSDdataDialog.OnOkListener okListener;
    private ImageView btn_close;

    public PurchaseDialog(RxAppCompatActivity activity) {
        super(activity, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_purchase);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mBtnConfirm = findViewById(R.id.btn_confirm);
        mBtncancel = findViewById(R.id.btn_cancel);
        btn_close=findViewById(R.id.btn_close);
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOkListener != null) {
                    mOkListener.onClick(v);
                    dismiss();
                }
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCloseListener != null) {
                    mCloseListener.onClick(v);
                    dismiss();
                }
            }
        });
        mBtncancel.setOnClickListener(new View.OnClickListener() {
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
    public interface OnCloseListner {
        void onClick(View view);
    }
    public void setmOkListener(PurchaseDialog.OnOkListener mOkListener) {
        this.mOkListener = mOkListener;
    }

    public void setmCancelListener(PurchaseDialog.OnCancelListner mCancelListener) {
        this.mCancelListener = mCancelListener;
    }
    public void setmCloseListener(PurchaseDialog.OnCloseListner mCloseListener) {
        this.mCloseListener = mCloseListener;
    }
}
