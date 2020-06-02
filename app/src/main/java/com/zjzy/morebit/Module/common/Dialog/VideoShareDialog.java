package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.R;

public class VideoShareDialog extends Dialog {
    private TextView btn_ok;
    private VideoShareDialog.OnOkListener mOkListener;
    private ClearSDdataDialog.OnOkListener okListener;
    private TextView tv_share;
    private String title="";
    private Context mContext;
    private ImageView close;
    public VideoShareDialog(RxAppCompatActivity activity) {
        super(activity, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_video_share);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public VideoShareDialog(Context context, String title, VideoShareDialog.OnOkListener listener) {
        super(context, R.style.dialog);
        this.mContext = context;
        this.title = title;
        this.mOkListener = listener;

    }
    private void initView() {
        btn_ok = findViewById(R.id.btn_ok);
        tv_share=findViewById(R.id.tv_share);
        close=findViewById(R.id.close);
        tv_share.setText(title);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOkListener != null) {
                    mOkListener.onClick(v);
                    dismiss();
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    public interface OnOkListener {
        void onClick(View view);
    }


    public void setmOkListener(VideoShareDialog.OnOkListener mOkListener) {
        this.mOkListener = mOkListener;
    }


}
