package com.zjzy.morebit.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.GrayUpgradeInfo;
import com.zjzy.morebit.utils.StringsUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *  灰度升级
 */

public class GrayUpgradeDialog extends Dialog implements View.OnClickListener{

    private Context mContext;
    private TextView cancelTv;
    private TextView toUpgradeTv;
    private GrayUpgradeInfo grayUpgradeInfo = null;
    private TextView content;



    public GrayUpgradeDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public GrayUpgradeDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;

    }

    public GrayUpgradeDialog(Context context, int themeResId, GrayUpgradeInfo upgradeInfo) {
        super(context, themeResId);
        this.mContext = context;
        this.grayUpgradeInfo = upgradeInfo;
    }


    protected GrayUpgradeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gray_upgrade);
        setCanceledOnTouchOutside(false);
        initView();
    }



    private void initView(){
        content = findViewById(R.id.content);
        cancelTv = findViewById(R.id.cancelTv);
        toUpgradeTv = findViewById(R.id.toUpgradeTv);
        cancelTv.setOnClickListener(this);
        toUpgradeTv.setOnClickListener(this);
        if(null != grayUpgradeInfo && !TextUtils.isEmpty(grayUpgradeInfo.getPopUpsContent())){
            content.setText(grayUpgradeInfo.getPopUpsContent());
        }

         if(content.getText().toString().length() > 30){
             content.setText(content.getText().toString().substring(0,30)+"...");
         }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toUpgradeTv:
                if(null != grayUpgradeInfo && !TextUtils.isEmpty(grayUpgradeInfo.getJumpUrl())){
                    Map<String, String> params = new HashMap<>();
                    params.put("grayscaleVersion",grayUpgradeInfo.getVersion()+"");
                    String jumpUrl = StringsUtils.appendUrlParams(grayUpgradeInfo.getJumpUrl(),params);
                    ShowWebActivity.start((Activity) mContext,jumpUrl,"");
                }
                this.dismiss();
                break;
            case R.id.cancelTv:
                this.dismiss();
                break;

        }
    }




}