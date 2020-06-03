package com.zjzy.morebit.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.SystemConfigBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.ConfigListUtlis;
import com.zjzy.morebit.utils.action.MyAction;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

/**
 * 飞猪弹窗
 */

public class FliggyDialog extends Dialog implements View.OnClickListener{

    private Context mContext;
    private GoToFliggyListener goToFliggyListener;
    private GoToFindOrderListener goToFindOrderListener;
    private TextView openFlygyTv;
    private TextView findOrderTv;
    private LinearLayout closeLay;
    private String orderUrl = null;


    public FliggyDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public FliggyDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;

    }

    public FliggyDialog(Context context, int themeResId, GoToFliggyListener listener,GoToFindOrderListener listener2) {
        super(context, themeResId);
        this.mContext = context;
        this.goToFliggyListener = listener;
        this.goToFindOrderListener = listener2;
    }


    protected FliggyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fliggy);
        setCanceledOnTouchOutside(false);
        initView();
    }



    private void initView(){
        openFlygyTv = findViewById(R.id.openFlygyTv);
        findOrderTv = findViewById(R.id.findOrderTv);
        closeLay = findViewById(R.id.closeLay);
        openFlygyTv.setOnClickListener(this);
        findOrderTv.setOnClickListener(this);
        closeLay.setOnClickListener(this);
        getOrderUrl();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.findOrderTv:
                if(null != this.goToFindOrderListener){
                    this.goToFindOrderListener.onClick(this,"");
                }
                if(!TextUtils.isEmpty(orderUrl)){
                    ShowWebActivity.start((Activity) mContext,orderUrl,"");
                }
                this.dismiss();
                break;
            case R.id.openFlygyTv:
                if(null != this.goToFliggyListener){
                    this.goToFliggyListener.onClick(this,"");
                }
                this.dismiss();
                break;
            case R.id.closeLay:
                this.dismiss();
                break;
        }
    }

    public interface GoToFliggyListener{
        void onClick(Dialog dialog, String text);
    }

    public interface GoToFindOrderListener{
        void onClick(Dialog dialog, String text);
    }

    public void getOrderUrl(){
        if(ConfigListUtlis.sSystemConfigBeenList != null){
            SystemConfigBean systemConfigBean = ConfigListUtlis.getSystemConfigBean(C.ConfigKey.ORDER_TRACKING);
            if(systemConfigBean!=null && !TextUtils.isEmpty(systemConfigBean.getSysValue())){
                orderUrl = systemConfigBean.getSysValue();
            }

        }else{
            ConfigListUtlis.getConfigList((RxAppCompatActivity) mContext,ConfigListUtlis.getConfigAllKey(), new MyAction.One<List<SystemConfigBean>>() {
                @Override
                public void invoke(List<SystemConfigBean> arg) {
                    getOrderUrl();
                }
            });
        }
    }
}