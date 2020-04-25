package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.R;

public class WxResginDialog extends Dialog {
    private TextView btn_ok;
    private WxResginDialog.OnOkListener mOkListener;
    private TextView tv_title,tv_contact;
    private String title="提示";
    private String content="当前微信未绑定多点优选账号\n 立即注册绑定，畅享百万优惠";
    private Context mContext;
    private String ok="注册";
    public WxResginDialog(RxAppCompatActivity activity) {
        super(activity, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login_wx_resgin);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public WxResginDialog(Context context, String title, String content, String ok, WxResginDialog.OnOkListener listener) {
        super(context, R.style.dialog);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.mOkListener = listener;
        this.ok=ok;
    }
    private void initView() {
        btn_ok = findViewById(R.id.btn_ok);
        tv_title=findViewById(R.id.tv_title);
        tv_contact=findViewById(R.id.tv_content);
        tv_contact.setText(content);
        btn_ok.setOnClickListener(new View.OnClickListener() {
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
    public interface OnCloseListner {
        void onClick(View view);
    }
    public void setmOkListener(WxResginDialog.OnOkListener mOkListener) {
        this.mOkListener = mOkListener;
    }

}
