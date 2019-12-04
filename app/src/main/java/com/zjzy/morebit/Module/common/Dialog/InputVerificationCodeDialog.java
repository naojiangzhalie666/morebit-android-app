package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.request.RequestAuthCodeBean;
import com.zjzy.morebit.pojo.request.RequestLoginCodeBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


/**
 * 我的团队中二维码弹窗
 */

public class InputVerificationCodeDialog extends Dialog {

    private Disposable mdDisposable;
    private TextView tv_verify_code;
    private TextView submit;
    private TextView tv_phone;
    private EditText et_verication_code;
    private ImageView iv_close;
    private RxAppCompatActivity mRxActivity;

    public InputVerificationCodeDialog(RxAppCompatActivity activity) {
        super(activity, R.style.dialog);
        mRxActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_verication_code);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        tv_verify_code = (TextView) findViewById(R.id.tv_verify_code);
        submit = (TextView) findViewById(R.id.submit);
        et_verication_code = (EditText) findViewById(R.id.et_verication_code);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_verify_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVericationCode();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroyAccount();
            }
        });
        tv_phone.setText(LoginUtil.hideNumber(UserLocalData.getUser(mRxActivity).getPhone()));
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    /**
     * 验证码获取倒计时
     */
    private void countDown() {
        tv_verify_code.setEnabled(false);
        //从0开始发射61个数字为：0-60依次输出，延时0s执行，每1s发射一次。
        mdDisposable = Flowable.intervalRange(0, 61, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        int seconds = (int) (60 - aLong);
                        tv_verify_code.setText(mRxActivity.getString(R.string.count_down, seconds + ""));
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        //倒计时完毕置为可点击状态
                        tv_verify_code.setEnabled(true);
                        tv_verify_code.setText("重新发送");
                    }
                })
                .subscribe();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mdDisposable != null) {
            mdDisposable.dispose();
        }
    }

    private void getVericationCode() {

        RequestAuthCodeBean requestBean = new RequestAuthCodeBean();
        requestBean.setPhone(UserLocalData.getUser(mRxActivity).getPhone());
        requestBean.setType(C.sendCodeType.logout);

        RxHttp.getInstance().getUsersService()
                .sendAuthCode(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(mRxActivity.<BaseResponse<String>>bindToLifecycle())
                .subscribe(new DataObserver<String>() {

                    @Override
                    protected void onDataNull() {
                        MyLog.i("test","onDataNull: ");
                        countDown();
                    }

                    @Override
                    protected void onSuccess(String data) {
                        MyLog.i("test","data: " +data);
                        countDown();
                    }
                });

    }

    /**
     * 账号注销
     */
    private void destroyAccount() {
        if (TextUtils.isEmpty(et_verication_code.getText().toString())) {
            ViewShowUtils.showShortToast(mRxActivity, "验证码不能为空");
            return;
        }
        if (et_verication_code.getText().toString().length() != 4) {
            ViewShowUtils.showShortToast(mRxActivity, "请输入4位验证码");
            return;
        }
        RequestLoginCodeBean bean = new RequestLoginCodeBean();
        bean.setVerCode(et_verication_code.getText().toString());
        RxHttp.getInstance().getCommonService().getAccountDestroy(bean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(mRxActivity.<BaseResponse<String>>bindToLifecycle())
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        ViewShowUtils.showShortToast(mRxActivity, data);
                        logout();
                    }
                });

    }

    private void logout() {
        LoginUtil.logout();
        mRxActivity.finish();
    }
}