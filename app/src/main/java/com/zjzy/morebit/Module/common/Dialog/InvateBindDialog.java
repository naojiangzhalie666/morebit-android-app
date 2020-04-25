package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.R;

public class InvateBindDialog extends Dialog {
    private TextView mBtncancel;
    private TextView btn_ok;
    private InvateBindDialog.OnOkListener mOkListener;
    private InvateBindDialog.OnCancelListner mCancelListener;
    private InvateBindDialog.OnCloseListner mCloseListener;
    private ClearSDdataDialog.OnOkListener okListener;
    private TextView tv_title;
    private String title="";
    private String content="确定绑定Ta为你的邀请人绑定后将无法修改";
    private Context mContext;
    private String cancle="取消";
    private String ok="确定";
    public InvateBindDialog(RxAppCompatActivity activity) {
        super(activity, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bind_invate);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public InvateBindDialog(Context context, String title, String content, String cancle, String ok, InvateBindDialog.OnOkListener listener) {
        super(context, R.style.dialog);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.mOkListener = listener;
        this.cancle=cancle;
        this.ok=ok;
    }
    private void initView() {
        btn_ok = findViewById(R.id.btn_ok);
        mBtncancel = findViewById(R.id.btn_cancel);
        tv_title=findViewById(R.id.tv_title);
        tv_title.setText(title);
        btn_ok.setOnClickListener(new View.OnClickListener() {
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
    public void setmOkListener(InvateBindDialog.OnOkListener mOkListener) {
        this.mOkListener = mOkListener;
    }

    public void setmCancelListener(InvateBindDialog.OnCancelListner mCancelListener) {
        this.mCancelListener = mCancelListener;
    }
    public void setmCloseListener(InvateBindDialog.OnCloseListner mCloseListener) {
        this.mCloseListener = mCloseListener;
    }
}
