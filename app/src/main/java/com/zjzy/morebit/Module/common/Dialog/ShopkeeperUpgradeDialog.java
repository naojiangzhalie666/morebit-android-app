package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.myInfo.UpdateInfoBean;
import com.zjzy.morebit.pojo.request.RequestUpdateUserBean;
import com.zjzy.morebit.utils.C;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/*
*
* 升级弹框
* */
public class ShopkeeperUpgradeDialog extends Dialog {
    private TextView btn_ok;
    private Context mContext;
    private ImageView close;
    private ShopkeeperUpgradeDialog.OnOkListener mOkListener;
    public ShopkeeperUpgradeDialog(RxAppCompatActivity activity) {
        super(activity, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_upgrade_shopkeeper);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public ShopkeeperUpgradeDialog(Context context) {
        super(context, R.style.dialog);
        this.mContext = context;

    }
    private void initView() {
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



    }


    public interface OnOkListener {
        void onClick(View view);
    }

    public void setmOkListener(ShopkeeperUpgradeDialog.OnOkListener mOkListener) {
        this.mOkListener = mOkListener;
    }

}
