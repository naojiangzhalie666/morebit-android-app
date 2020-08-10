package com.zjzy.morebit.Module.common.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.TeamInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.request.RequestWechatCodeBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.functions.Action;

public class CustomerDialog extends Dialog {
    private TextView tv_name,tv_wx;
    private Context mContext;
    private LinearLayout ll;
    private ImageView userIcon,copy,diss;
    private TeamInfo mdata;

    public CustomerDialog(Context context, TeamInfo data) {
        super(context, R.style.dialog);
        this.mdata=data;
        this.mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_customer);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public CustomerDialog(Context context) {
        super(context, R.style.dialog);
        this.mContext = context;

    }
    private void initView() {

        userIcon = findViewById(R.id.userIcon);
        tv_name=findViewById(R.id.tv_name);
        tv_wx=findViewById(R.id.tv_wx);
        copy=findViewById(R.id.copy);
        diss=findViewById(R.id.diss);


        if (!TextUtils.isEmpty(mdata.getHeadImg())) {
            LoadImgUtils.setImgCircle(mContext, userIcon, mdata.getHeadImg());
        } else {
            userIcon.setImageResource(R.drawable.head_icon);
        }

        if(TextUtils.isEmpty(mdata.getNickname())){
            tv_name.setText("团队长");
        } else {
            tv_name.setText(mdata.getNickname()+"");
        }

        if (TextUtils.isEmpty(mdata.getWxNumber())) {
            tv_wx.setText("微信号：未填写");
            copy.setEnabled(false);
        } else {
            tv_wx.setText("微信号："+mdata.getWxNumber());
            copy.setEnabled(true);

        }


        diss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.coayText((Activity) mContext,   mdata.getWxNumber()+"");
                Toast.makeText(mContext, "复制成功", Toast.LENGTH_SHORT).show();
            }
        });

    }



}
