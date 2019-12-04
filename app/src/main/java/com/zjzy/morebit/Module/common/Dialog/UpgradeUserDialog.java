package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zjzy.morebit.R;


/**
 * Created by YangBoTian on 2019/3/18.
 */

public class UpgradeUserDialog extends Dialog {

    private ImageView mBtncancel;
    private ImageView mBtnConfirm;
    private OnOkListener mOkListener;
    private OnCancelListner mCancelListener;
    private ClearSDdataDialog.OnOkListener okListener;
    private Context mContext;
    public UpgradeUserDialog(Context context) {
        super(context, R.style.dialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_upgrade_user);
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
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        window.setAttributes(setLayoutParams(params));
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

    public void setCancelListener(OnCancelListner mCancelListener) {
        this.mCancelListener = mCancelListener;
    }

    public WindowManager.LayoutParams setLayoutParams(
            WindowManager.LayoutParams lp) {
        lp.width = (int) getScreenWidth(mContext); // 设置宽度
        return lp;
    }

    /**
     * 得到屏幕高度
     *
     * @param context
     * @return
     * @author shc DateTime 2014-11-20 下午3:04:44
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();

        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }
}
