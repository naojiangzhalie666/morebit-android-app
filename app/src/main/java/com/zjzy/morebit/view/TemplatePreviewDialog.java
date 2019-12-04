package com.zjzy.morebit.view;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.R;


/**
 * Created by yangbotian on 2017/8/8.
 */

public class TemplatePreviewDialog extends Dialog {
    public static final int THEME_DEFAULT = R.style.commom_dialog;

    protected Context mContext;
    private View view;
    TextView mConfirm;
    ImageView btn_cancel;
    private  OnConfirmListener mOnConfirmListener ;
    private  OnCancelListener mOnCancelListener ;
    String mContent;
    private TextView mTvContent;
    public TemplatePreviewDialog(Context context,String content) {
        super(context, THEME_DEFAULT);
        mContext = context;
        mContent = content;
        init(R.layout.dialog_template_preview);
    }


    private void init(int layoutId) {
        view = LayoutInflater.from(mContext).inflate(layoutId, null);
        setContentView(view);
        Window window = getWindow();
        initUi(view);
        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        window.setAttributes(setLayoutParams(params));
    }

    private void initUi(View view) {
        mConfirm = (TextView) view.findViewById(R.id.btn_confirm);
        btn_cancel = (ImageView) view.findViewById(R.id.btn_cancel);
        mTvContent = (TextView) view.findViewById(R.id.content);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnConfirmListener!=null){
                    mOnConfirmListener.onClick(v);
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(mOnCancelListener!=null){
                    mOnCancelListener.onClick(v);
                }
            }
        });
        mTvContent.setText(mContent);
    }

    @Override
    public void show() {
        if (!isShowing()) {
            super.show();
        }
    }

    public WindowManager.LayoutParams setLayoutParams(
            WindowManager.LayoutParams lp) {
        lp.width = (int) getScreenWidth(mContext); // 设置宽度
        return lp;
    }


    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
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

    public interface OnConfirmListener {
        void onClick(View view);
    }

    public interface OnCancelListener {
        void onClick(View view);
    }
    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.mOnConfirmListener = onConfirmListener;
    }

    public void setmOnCancelListener(OnCancelListener mOnCancelListener) {
        this.mOnCancelListener = mOnCancelListener;
    }
}
