package com.jf.my.Module.common.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jf.my.R;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


/**
 * Created by YangBoTian on 2019/3/18.
 */

public class AuthorDialog extends Dialog {

    private TextView mBtncancel;
    private TextView mBtnConfirm;
    private OnOkListener mOkListener;
    private OnCancelListner mCancelListener;
    private ClearSDdataDialog.OnOkListener okListener;

    public AuthorDialog(RxAppCompatActivity activity) {
        super(activity, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_author);
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

    public void setmOkListener(OnOkListener mOkListener) {
        this.mOkListener = mOkListener;
    }

    public void setmCancelListener(OnCancelListner mCancelListener) {
        this.mCancelListener = mCancelListener;
    }
}
