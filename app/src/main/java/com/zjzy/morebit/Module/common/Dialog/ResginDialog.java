package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.R;

public class ResginDialog extends Dialog {
    private TextView mBtncancel;
    private TextView btn_ok;
    private ResginDialog.OnOkListener mOkListener;
    private ResginDialog.OnCancelListner mCancelListener;
    private ResginDialog.OnCloseListner mCloseListener;
    private ClearSDdataDialog.OnOkListener okListener;
    private TextView tv_title,tv_content;
    private String title="尚未注册";
    private String content="您还没有注册喔，快去注册吧！";
    private Context mContext;
    private String cancle="取消";
    private String ok="注册";
    public ResginDialog(RxAppCompatActivity activity) {
        super(activity, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login_not_resgin);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public ResginDialog(Context context, String title, String content, String cancle,String ok,ResginDialog.OnOkListener listener) {
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
        tv_content=findViewById(R.id.tv_content);
        btn_ok.setText(ok);
        mBtncancel.setText(cancle);
        tv_content.setText(content);
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
    public void setmOkListener(ResginDialog.OnOkListener mOkListener) {
        this.mOkListener = mOkListener;
    }

    public void setmCancelListener(ResginDialog.OnCancelListner mCancelListener) {
        this.mCancelListener = mCancelListener;
    }
    public void setmCloseListener(ResginDialog.OnCloseListner mCloseListener) {
        this.mCloseListener = mCloseListener;
    }
}
