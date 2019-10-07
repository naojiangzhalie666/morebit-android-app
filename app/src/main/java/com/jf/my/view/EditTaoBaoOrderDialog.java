package com.jf.my.view;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jf.my.R;
import com.jf.my.utils.ViewShowUtils;


/**
 * 输入淘宝订单号
 * Created by fengrs on 2018/6/6.
 */

public class EditTaoBaoOrderDialog extends Dialog {
    public static final int THEME_DEFAULT = R.style.commom_dialog;

    protected Context mContext;
    private View view;
    TextView mConfirm;
    TextView btn_cancel;
    private OnConfirmListener mOnConfirmListener;

    private EditText ed_order;

    public EditTaoBaoOrderDialog(Context context) {
        super(context, THEME_DEFAULT);
        mContext = context;
        init(R.layout.dialog_edit_taobao_order);
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
        btn_cancel = (TextView) view.findViewById(R.id.btn_cancel);
        ed_order = (EditText) view.findViewById(R.id.ed_order);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnConfirmListener != null) {
                    Editable text = ed_order.getText();
                    if (TextUtils.isEmpty(text.toString())) {
                        ViewShowUtils.showLongToast(mContext, "请输入淘宝订单号");
                    } else {
                        mOnConfirmListener.onClick(text.toString());
                    }
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
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
        void onClick(String view);
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.mOnConfirmListener = onConfirmListener;
    }
}
