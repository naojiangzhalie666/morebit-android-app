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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
public class NumberVipUpgradeDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private View mView;
    private LinearLayout updateLeaderGrade;
    private LinearLayout updateVipGrade;

    private ImageView ivClose;

    private OnListener mListener;

    public NumberVipUpgradeDialog(@NonNull Context context) {
        super(context, 0);
    }

    public NumberVipUpgradeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        initView();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
        setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ((Activity)mContext).getWindowManager().getDefaultDisplay().getWidth()*3/4;
        getWindow().setAttributes(params);
    }

    private void initView() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.upgrade_vip_dialog_content_layout, null);
        updateLeaderGrade = mView.findViewById(R.id.update_leader_grade);
        updateVipGrade = mView.findViewById(R.id.update_vip_grade);

        ivClose = mView.findViewById(R.id.iv_close);

        updateLeaderGrade.setOnClickListener(this);
        updateVipGrade.setOnClickListener(this);
        ivClose.setOnClickListener(this);

    }



    public NumberVipUpgradeDialog setOnListner(OnListener listener) {
        this.mListener = listener;
        return this;
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
            case R.id.update_leader_grade:
                if (mListener != null) {
                    mListener.onClick(OnListener.LEADER);
                }
                this.dismiss();
                break;
            case R.id.update_vip_grade:
                if (mListener != null) {
                    mListener.onClick(OnListener.VIP);
                }
                this.dismiss();
                break;
        }
    }

    public interface OnListener {
         int VIP = 1;
         int LEADER = 2;
        void onClick(int type);
    }
}