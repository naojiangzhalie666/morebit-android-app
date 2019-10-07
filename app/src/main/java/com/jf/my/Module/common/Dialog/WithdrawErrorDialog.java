package com.jf.my.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jf.my.R;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


/**
 * Created by YangBoTian on 2019/3/18.
 */

public class WithdrawErrorDialog extends Dialog {

    private TextView mBtnConfirm;
    private OnOkListener mOkListener;
    private OnCancelListner mCancelListener;
    private ClearSDdataDialog.OnOkListener okListener;
   private String mTitle,mContent;
    private Context mContext;
    private TextView mBtnTitle,mBtnContent;
    public WithdrawErrorDialog(Context context, int themeResId, String title, String content ) {
        super(context, themeResId);
        mContext = context;
        mTitle = title;
        mContent = content;
    }
    public WithdrawErrorDialog(RxAppCompatActivity activity) {
        super(activity, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_withdraw);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mBtnConfirm = findViewById(R.id.btn_confirm);
        mBtnTitle = findViewById(R.id.tv_title);
        mBtnContent = findViewById(R.id.tv_content);
        if(!TextUtils.isEmpty(mTitle)){
            mBtnTitle.setText(mTitle);
        }
        if(!TextUtils.isEmpty(mContent)){
            mBtnContent.setText(mContent);
        }
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOkListener != null) {
                    mOkListener.onClick(v);
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
