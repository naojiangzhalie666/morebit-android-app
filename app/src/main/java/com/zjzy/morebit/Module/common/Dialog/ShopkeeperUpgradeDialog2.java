package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.R;

/*
*
* 升级黑金弹框
* */
public class ShopkeeperUpgradeDialog2 extends Dialog {
    private TextView btn_ok,btn_close;
    private Context mContext;
    private ImageView close;
    private ShopkeeperUpgradeDialog2.OnOkListener mOkListener;
    public ShopkeeperUpgradeDialog2(RxAppCompatActivity activity) {
        super(activity, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_upgrade_shopkeeper2);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public ShopkeeperUpgradeDialog2(Context context) {
        super(context, R.style.dialog);
        this.mContext = context;

    }
    private void initView() {
        btn_close=findViewById(R.id.btn_close);
        close=findViewById(R.id.close);
        btn_ok = findViewById(R.id.btn_ok);
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
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



    }


    public interface OnOkListener {
        void onClick(View view);
    }

    public void setmOkListener(ShopkeeperUpgradeDialog2.OnOkListener mOkListener) {
        this.mOkListener = mOkListener;
    }

}
