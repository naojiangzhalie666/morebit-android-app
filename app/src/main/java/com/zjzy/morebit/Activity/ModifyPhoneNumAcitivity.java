package com.zjzy.morebit.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zjzy.morebit.MainActivity;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.request.RequestAuthCodeBean;
import com.zjzy.morebit.pojo.request.RequestCheckPhoneNumBean;
import com.zjzy.morebit.pojo.request.RequestUpdateNewPhoneBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;
import com.zjzy.morebit.utils.encrypt.MD5Utils;
import com.zjzy.morebit.utils.helper.ActivityLifeHelper;
import com.zjzy.morebit.view.ClearEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Action;

/**
 * Created by YangBoTian on 2018/5/31 14:07
 * 修改手机号
 */

public class ModifyPhoneNumAcitivity extends BaseActivity {
    private static final int SECONDS = 60; // 秒数

    private TextView mTvVerfyCode;
    private EditText mEdtVerfyCode;
    LinearLayout mBtnBack;
    TextView mTitle;
    private long first = 0;
    private TextView mSubmit;
    private int type = 0;
    // private LinearLayout mLlModify1,mLlModify2;
    private ClearEditText mEdtPhoneNum, mEdtName, mEdtPassword, mEdtNewPhoneNum;
    @BindView(R.id.ll_modify1)
    LinearLayout mLlModify1;
    @BindView(R.id.ll_modify2)
    LinearLayout mLlModify2;
    private String mCheckCode;

    public static void start(Activity activity, String checkCode, int type) {
        Intent intent = new Intent(activity, ModifyPhoneNumAcitivity.class);
        intent.putExtra("type", type);
        intent.putExtra("checkCode", checkCode);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone_num);
        setSystemBar();
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);
        mCheckCode = getIntent().getStringExtra("checkCode");
        initView();
    }

    private void initView() {
        mTvVerfyCode = (TextView) findViewById(R.id.tv_verify_code);
        mTitle = (TextView) findViewById(R.id.txt_head_title);
        mBtnBack = (LinearLayout) findViewById(R.id.btn_back);
        mSubmit = (TextView) findViewById(R.id.tv_submit);
        mEdtPhoneNum = (ClearEditText) findViewById(R.id.edt_phone_num);
        mEdtPassword = (ClearEditText) findViewById(R.id.edt_password);
        mEdtName = (ClearEditText) findViewById(R.id.edt_name);
        mEdtNewPhoneNum = (ClearEditText) findViewById(R.id.edt_new_phone_num);
        mEdtVerfyCode = (EditText) findViewById(R.id.edt_verify_code);
        mTitle.setText(R.string.modify_phone_num);

        if (type == 0) {
            mLlModify1.setVisibility(View.VISIBLE);
            mLlModify2.setVisibility(View.GONE);
            mSubmit.setText(R.string.bind_phone_num);
        } else {
            mLlModify1.setVisibility(View.GONE);
            mLlModify2.setVisibility(View.VISIBLE);
            mSubmit.setText(R.string.submit);
        }

        mTvVerfyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppUtil.isFastClick(500)) {
                    return;
                }

                if ("获取验证码".equals(mTvVerfyCode.getText().toString().trim()) || "重新获取".equals(mTvVerfyCode.getText().toString().trim())) {

                    getPublicTimeline();
                }

            }
        });
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 0) {
                    checkPhoneNum();
                } else {
                    bindNewPhoneNum();
                }
            }
        });
    }


    private void checkPhoneNum() {
        if (!checkInput()) {
            return;
        }

        LoadingView.showDialog(this, "请求中...");
        String phone = mEdtPhoneNum.getText().toString().trim();
        String realName = mEdtName.getText().toString().trim();
        String password = mEdtPassword.getText().toString().trim();

        String pwdMD5 = MD5Utils.getPsdMD5(password);

        RequestCheckPhoneNumBean requestBean = new RequestCheckPhoneNumBean();
        requestBean.setPhone(phone);
        requestBean.setRealName(realName);
        requestBean.setPassword(pwdMD5);
        String sign = EncryptUtlis.getSign2(requestBean);
        requestBean.setSign(sign);

        RxHttp.getInstance().getUsersService()
                .checkPhoneNum(requestBean)
//                phone,
//                realName ,
//                pwdMD5,
//                sign)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(this.<BaseResponse<String>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<String>() {

                    @Override
                    protected void onSuccess(String data) {
                        ModifyPhoneNumAcitivity.start(ModifyPhoneNumAcitivity.this,data, 1);
                    }
                }); }


    private void bindNewPhoneNum() {
        if (!checkNewPhoneInput()) {
            return;
        }

        LoadingView.showDialog(this, "请求中...");
        String phone = mEdtNewPhoneNum.getText().toString().trim();
        String verCode = mEdtVerfyCode.getText().toString().trim();



        RequestUpdateNewPhoneBean requestBean = new RequestUpdateNewPhoneBean();
        requestBean.setPhone(phone);
        requestBean.setVerCode(verCode);
        requestBean.setCheck(mCheckCode);
        String sign = EncryptUtlis.getSign2(requestBean);
        requestBean.setSign(sign);

        RxHttp.getInstance().getUsersService().bindNewPhoneNum(
//                phone,
//                verCode,
//                mCheckCode,
//                sign)
                requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(this.<BaseResponse<String>>bindToLifecycle())
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

                        Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_LONG).show();
                        LoginUtil.logout();

                        //销毁所有activity 留下main
                        ActivityLifeHelper.getInstance().finishAllActivities(ActivityLifeHelper.getInstance().getActivityInstance(MainActivity.class));

                    }
                }); }

    private boolean checkInput() {
        if (TextUtils.isEmpty(mEdtPhoneNum.getText().toString())) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mEdtName.getText().toString())) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mEdtPassword.getText().toString())) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!LoginUtil.isMobile(mEdtPhoneNum.getText().toString())) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    private boolean checkNewPhoneInput() {
        if (TextUtils.isEmpty(mEdtNewPhoneNum.getText().toString())) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!LoginUtil.isMobile(mEdtNewPhoneNum.getText().toString())) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public void getPublicTimeline() {//短信
        if (!checkNewPhoneInput()) {
            return;
        }

        LoadingView.showDialog(this, "请求中...");

        RequestAuthCodeBean requestBean = new RequestAuthCodeBean();
        requestBean.setPhone(mEdtNewPhoneNum.getText().toString().trim());
        requestBean.setType(C.sendCodeType.REVAMPPHONE);

        RxHttp.getInstance().getUsersService()
//                .sendAuthCode( mEdtNewPhoneNum.getText().toString().trim(), C.sendCodeType.REVAMPPHONE  )
//        RxHttp.getInstance().getCommonService().getAuthCode("get_users_send_code", mEdtNewPhoneNum.getText().toString().trim(), "login-new")
                .sendAuthCode(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(this.<BaseResponse<String>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        initVerifyCode();
                    }
                    @Override
                    protected void onDataNull() {
                        onSuccess("");
                    }

                    @Override
                    protected void onSuccess(String data) {
                        ModifyPhoneNumAcitivity.Counter counter = new ModifyPhoneNumAcitivity.Counter(60 * 1000, 1000); // 第一个参数是倒计时时间，后者为计时间隔，单位毫秒，这里是倒计时
                        counter.start();
                    }
                }) ;}

    private void initVerifyCode() {
        mTvVerfyCode.setEnabled(true);
        mTvVerfyCode.setText("获取验证码");
    }

    class Counter extends CountDownTimer {

        public Counter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            try {
                mTvVerfyCode.setEnabled(true);
                mTvVerfyCode.setText("重新获取");
                mTvVerfyCode.setBackgroundResource(R.drawable.background_f05557_radius_30dp);
                mTvVerfyCode.setTextColor(getResources().getColor(R.color.color_F05557));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 获取当前时间总秒数
            first = millisUntilFinished / 1000;
            if (first <= SECONDS) { // 小于一分钟 只显示秒
                mTvVerfyCode.setText((first < 10 ? "0" + first : first) + " 秒后再试");
            }

        }

    }


    /**
     * 是否弹出去全网搜索
     *
     * @return
     */
    public boolean isShowAllSeek() {
        return false;
    }

}
