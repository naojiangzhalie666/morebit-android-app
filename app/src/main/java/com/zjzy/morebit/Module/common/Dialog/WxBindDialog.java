package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.info.model.WechatModel;
import com.zjzy.morebit.info.ui.SettingWechatActivity;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.request.RequestWechatCodeBean;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Action;

public class WxBindDialog extends Dialog {
    private TextView btn_ok;
    private Context mContext;
    private LinearLayout ll;

    private EditText mEditText;
    private ImageView diss;
    public WxBindDialog(RxAppCompatActivity activity) {
        super(activity, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wx_bind);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public WxBindDialog(Context context) {
        super(context, R.style.dialog);
        this.mContext = context;

    }
    private void initView() {

        UserInfo user = UserLocalData.getUser(mContext);
        btn_ok = findViewById(R.id.btn_ok);
        mEditText=findViewById(R.id.mEditText);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWechat();
                dismiss();
            }
        });
        if (!TextUtils.isEmpty(user.getWxNumber())){
            mEditText.setText(user.getWxNumber());
            mEditText.setSelection(mEditText.getText().toString().length());
        }
        diss=findViewById(R.id.diss);
        diss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }

    private void setWechat() {

        LoadingView.showDialog(mContext, "提交中");
        RequestWechatCodeBean bean = new RequestWechatCodeBean();
        bean.setWxNumber(mEditText.getText().toString());
        String sign2 = EncryptUtlis.getSign2(bean);
        bean.setSign(sign2);
        RxHttp.getInstance().getCommonService().saveWechatInfo(bean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onDataNull() {
                        onSuccess("");
                    }

                    @Override
                    protected void onSuccess(String data) {

                        UserInfo user = UserLocalData.getUser(mContext);
                        if (user != null) {
                            user.setWxNumber(mEditText.getText().toString());
                            EventBus.getDefault().post(new MessageEvent(EventBusAction.MAINPAGE_MYCENTER_REFRESH_DATA));
                        }
                            ViewShowUtils.showShortToast(mContext, "提交成功");
                    }
                });
    }



}
