package com.zjzy.morebit.Module.common.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.R;

/**
 * Created by Administrator on 2017/10/23.
 */

public class NotificationDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private View mView;
    private ImageView ivIcon;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvLeft;
    private TextView tvRight;

    private NotificationDialog.OnListener mListener;

    public NotificationDialog(@NonNull Context context) {
        super(context, 0);
    }

    public NotificationDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        initView();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialog_notification);
        setContentView(mView);
        setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ((Activity)mContext).getWindowManager().getDefaultDisplay().getWidth()*3/4;
        getWindow().setAttributes(params);
    }

    private void initView() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_notification, null);
        ivIcon = mView.findViewById(R.id.iv_icon);
        tvTitle = mView.findViewById(R.id.tv_title);
        tvContent = mView.findViewById(R.id.tv_content);
        tvLeft = mView.findViewById(R.id.tv_left);
        tvRight = mView.findViewById(R.id.tv_right);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    public NotificationDialog setIcon(int resId) {
        ivIcon.setBackgroundResource(resId);
        return this;
    }

    public NotificationDialog setTitle(String text) {
        tvTitle.setText(text);
        return this;
    }

    public NotificationDialog setContent(String text) {
        tvContent.setText(text);
        return this;
    }

    public NotificationDialog setOnListner(NotificationDialog.OnListener listener) {
        this.mListener = listener;
        return this;
    }

    public ImageView getIvIcon() {
        return ivIcon;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public TextView getTvContent() {
        return tvContent;
    }

    public TextView getTvLeft() {
        return tvLeft;
    }

    public TextView getTvRight() {
        return tvRight;
    }

    @Override
    public void show() {
        if(!isShowing()){
            super.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                if (mListener != null) {
                    mListener.onClick(OnListener.LETF);
                }
                this.dismiss();
                break;
            case R.id.tv_right:
                if (mListener != null) {
                    mListener.onClick(OnListener.RIGHT);
                }
                this.dismiss();
                break;
        }
    }

    public interface OnListener {
        int LETF = 1;
        int RIGHT = 2;
        void onClick(int type);
    }
}