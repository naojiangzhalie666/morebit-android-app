package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.R;

public class ProductDialog extends Dialog {
    private TextView mBtncancel;
    private TextView mBtnConfirm;
    private ProductDialog.OnOkListener mOkListener;
    private ProductDialog.OnCancelListner mCancelListener;
    private ProductDialog.OnCloseListner mCloseListener;
    private ClearSDdataDialog.OnOkListener okListener;


    public ProductDialog(RxAppCompatActivity activity) {
        super(activity, R.style.dialog);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_product);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mBtnConfirm = findViewById(R.id.btn_confirm);
        mBtncancel = findViewById(R.id.btn_cancel);
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOkListener != null) {
                    mOkListener.onClick(v);
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
    public void setmOkListener(ProductDialog.OnOkListener mOkListener) {
        this.mOkListener = mOkListener;
    }

    public void setmCancelListener(ProductDialog.OnCancelListner mCancelListener) {
        this.mCancelListener = mCancelListener;
    }
    public void setmCloseListener(ProductDialog.OnCloseListner mCloseListener) {
        this.mCloseListener = mCloseListener;
    }
}
