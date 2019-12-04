package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zjzy.morebit.R;


/**
 * Created by Administrator on 2017/10/23.
 */

public class NetLoadingDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private ProgressBar pbLoad;
    private String text;
    private TextView tv;

    public NetLoadingDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public NetLoadingDialog(Context context, int themeResId, String text) {
        super(context, themeResId);
        this.mContext = context;
        this.text = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCanceledOnTouchOutside(false);
        //去掉默认的title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉白色边角 我的小米手机在xml里设置 android:background="@android:color/transparent"居然不生效
        //所以在代码里设置，不知道是不是小米手机的原因
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_netloading);
        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.LinearLayout);
        linearLayout.getBackground().setAlpha(210);

        initView();
    }


    private void initView() {
        pbLoad = (ProgressBar) findViewById(R.id.progressBar1);
        tv = (TextView) findViewById(R.id.tv);
        if (tv != null&& !TextUtils.isEmpty(text)) {
            tv.setText(text);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                break;
            case R.id.cancel:
                break;
        }
    }

}