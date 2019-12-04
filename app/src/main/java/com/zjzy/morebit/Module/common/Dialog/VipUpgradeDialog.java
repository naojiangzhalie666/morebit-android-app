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
 * @Description: 用户升级 提示框
 * @Author: liys
 * @CreateDate: 2019/3/16 17:21
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/3/16 17:21
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class VipUpgradeDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private View mView;
    private ImageView ivIcon;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvLeft;
    private TextView tvRight;
    private ImageView ivClose;

    private VipUpgradeDialog.OnListener mListener;

    public VipUpgradeDialog(@NonNull Context context) {
        super(context, 0);
    }

    public VipUpgradeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        initView();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialog_vip_upgrade);
        setContentView(mView);
        setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ((Activity)mContext).getWindowManager().getDefaultDisplay().getWidth()*3/4;
        getWindow().setAttributes(params);
    }

    private void initView() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_vip_upgrade, null);
        tvTitle = mView.findViewById(R.id.tv_title);
        ivIcon = mView.findViewById(R.id.iv_icon);
        tvContent = mView.findViewById(R.id.tv_content);
        tvLeft = mView.findViewById(R.id.tv_left);
        tvRight = mView.findViewById(R.id.tv_right);
        ivClose = mView.findViewById(R.id.iv_close);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        ivIcon.setOnClickListener(this);
    }

    public VipUpgradeDialog setIcon(int resId) {
        ivIcon.setBackgroundResource(resId);
        return this;
    }

    public VipUpgradeDialog setContent(String text) {
        tvContent.setText(text);
        return this;
    }

    public VipUpgradeDialog setOnListner(VipUpgradeDialog.OnListener listener) {
        this.mListener = listener;
        return this;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }
    public ImageView getIvIcon() {
        return ivIcon;
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
            case R.id.iv_close:
                this.dismiss();
                break;
            case R.id.tv_left:
                if (mListener != null) {
                    mListener.onClick(NotificationDialog.OnListener.LETF);
                }
                this.dismiss();
                break;
            case R.id.tv_right:
                if (mListener != null) {
                    mListener.onClick(VipUpgradeDialog.OnListener.RIGHT);
                }
                this.dismiss();
                break;
            case R.id.iv_icon:
                if (mListener != null) {
                    mListener.onClick(VipUpgradeDialog.OnListener.IMG);
                }
                this.dismiss();
                break;
        }
    }

    public interface OnListener {
        int LETF = 1;
        int RIGHT = 2;
        int IMG = 3;
        void onClick(int type);
    }
}