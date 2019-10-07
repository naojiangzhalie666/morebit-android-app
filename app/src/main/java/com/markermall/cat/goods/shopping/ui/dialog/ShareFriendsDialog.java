package com.markermall.cat.goods.shopping.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.markermall.cat.R;

/**
 * @author fengrs
 * @date 2019/8/31
 */
public class ShareFriendsDialog extends Dialog implements View.OnClickListener{

    private   OnCloseListener mcancelListener;
    private Context mContext;

    public ShareFriendsDialog(Context context, OnCloseListener cancelListener) {
        super(context);
        this.mContext = context;
        this.mcancelListener =cancelListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_friends);
        setCanceledOnTouchOutside(false);
        initView();
    }


    private void initView(){
      findViewById(R.id.tv_left).setOnClickListener(this);
      findViewById(R.id.tv_right).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_left:
                this.dismiss();
                break;
            case R.id.tv_right:
                this.dismiss();
                if (mcancelListener!=null){
                    mcancelListener.onClick();
                }
                break;
        }
    }

    public interface OnCloseListener{
        void onClick();
    }

}