package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.R;

public class GoodsDeteleDialog extends Dialog {
    private TextView btn_ok,yes;
    private Context mContext;
    private GoodsDeteleDialog.OnOkListener mOkListener;
    public GoodsDeteleDialog(RxAppCompatActivity activity) {
        super(activity, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shopcardelete);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public GoodsDeteleDialog(Context context) {
        super(context, R.style.dialog);
        this.mContext = context;

    }
    private void initView() {

        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        yes = findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
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



    public void setmOkListener(GoodsDeteleDialog.OnOkListener mOkListener) {
        this.mOkListener = mOkListener;
    }


}
