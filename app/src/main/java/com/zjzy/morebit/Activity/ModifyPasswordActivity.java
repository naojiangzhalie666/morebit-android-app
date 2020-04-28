package com.zjzy.morebit.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.ClearSDdataDialog;
import com.zjzy.morebit.Module.common.Dialog.LoginNotRegeditDialog;
import com.zjzy.morebit.Module.common.Dialog.PasswordSumbitDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.login.ui.AreaCodeActivity;
import com.zjzy.morebit.login.ui.LoginEditInviteFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.AreaCodeBean;
import com.zjzy.morebit.pojo.request.RequestAuthCodeBean;
import com.zjzy.morebit.pojo.request.RequestUpPasswordBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.AreaCodeUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;
import com.zjzy.morebit.utils.encrypt.MD5Utils;
import com.zjzy.morebit.utils.helper.ActivityLifeHelper;
import com.zjzy.morebit.utils.netWork.ErrorCodeUtlis;
import com.zjzy.morebit.view.ClearEditText;

import io.reactivex.functions.Action;

/**
 * Created by YangBoTian on 2018/5/30 11:48
 * 修改密码页
 */

public class ModifyPasswordActivity extends BaseActivity implements TextWatcher {

    public static final int MODIFY_PASSWORD = 1;
    public static final int FIND_PASSWORD = 2;
    ImageView showPwIv;
    private EditText mEdtVerfyCode;
    private TextView mPhone, mTvVerfyCode;
    private ClearEditText mEdtPassword, mEdtPassword2, mEdtPhone;
    private TextView mSubmit, mTvPhone;
    private long first = 0;
    private static final int SECONDS = 60; // 秒数
    ImageView mBtnBack;
    TextView mTitle;
    TextView errorReTv;
    TextView errorPwTv;
    TextView errorPhoneTv;
    TextView errorCodeTv;
    TextView areaCodeTv;
    private TextView areaCodeBtn;
    private int type = 1;
    private ClearSDdataDialog mDialog;
    String phone = "";
    private boolean isOpenEye = false;
    LoginNotRegeditDialog mLoginDialog;

    private AreaCodeBean mAreaCode;
    private int phoneLength = 11; //默认是中国11位
    private String areaCode = "86"; //默认是中国的86
    private TextView ll_userAgreement, privateProtocol;
    private RelativeLayout rl;

    @Override
    public boolean isShowAllSeek() {
        return false;
    }

    public static void start(Activity activity, int type, String phone,AreaCodeBean areaCodeBean) {
        Intent intent = new Intent(activity, ModifyPasswordActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("phone", phone);
        intent.putExtra(C.Extras.COUNTRY,areaCodeBean);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(false)
                .statusBarColor(R.color.color_FFD4CF)
                .init();
        setContentView(R.layout.activity_modify_password);

        type = getIntent().getIntExtra("type", 1);
        initView();
    }

    private void initView() {
        errorCodeTv = (TextView) findViewById(R.id.errorCodeTv);
        errorPhoneTv = (TextView) findViewById(R.id.errorPhoneTv);
        errorReTv = (TextView) findViewById(R.id.errorRePw);
        errorPwTv = (TextView) findViewById(R.id.errorPw);
        mEdtVerfyCode = (EditText) findViewById(R.id.edt_verify_code);
        mPhone = (TextView) findViewById(R.id.tv_phone);
        showPwIv = (ImageView) findViewById(R.id.showPwIv);
        mEdtPassword = (ClearEditText) findViewById(R.id.edt_password);
        mEdtPassword.setFilters(new InputFilter[]{AppUtil.getTrimInputFilter()});
        mEdtPassword2 = (ClearEditText) findViewById(R.id.edt_password2);
        mEdtPassword2.setFilters(new InputFilter[]{AppUtil.getTrimInputFilter()});
        mBtnBack = (ImageView) findViewById(R.id.iv_back);
        mTvVerfyCode = (TextView) findViewById(R.id.tv_verify_code);
        mTitle = (TextView) findViewById(R.id.txt_head_title);
        mEdtPhone = (ClearEditText) findViewById(R.id.edt_phone);
        areaCodeTv = (TextView)findViewById(R.id.areaCodeTv);
        areaCodeBtn = (TextView) findViewById(R.id.areaCodeBtn);
        String phone = getIntent().getStringExtra("phone");
        mAreaCode = (AreaCodeBean) getIntent().getSerializableExtra(C.Extras.COUNTRY);
        rl= (RelativeLayout) findViewById(R.id.rl);
        privateProtocol = (TextView) findViewById(R.id.privateProtocol);
        privateProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUtil.getPrivateProtocol(ModifyPasswordActivity.this);
            }
        });
        ll_userAgreement = (TextView) findViewById(R.id.ll_userAgreement);
        ll_userAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUtil.getUserProtocol(ModifyPasswordActivity.this);
            }
        });
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (!TextUtils.isEmpty(phone)) {
            mEdtPhone.setText(phone);
        }

        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        errorPhoneTv.setText("");
        if (type == MODIFY_PASSWORD) {
            mTitle.setText(R.string.find_password);
            areaCodeTv.setVisibility(View.GONE);
            areaCodeBtn.setVisibility(View.GONE);
            mEdtPhone.setVisibility(View.GONE);
            mTvPhone.setVisibility(View.VISIBLE);
            mPhone.setText(LoginUtil.hideNumber(UserLocalData.getUser(this).getPhone()));
            errorPhoneTv.setVisibility(View.INVISIBLE);

            //修改密码这里后面要增加区号，暂时先默认是中国
            if(null == mAreaCode){
                mAreaCode = AreaCodeUtil.getDefaultCode();
            }

        } else {
            mTitle.setText(R.string.find_password);
            mEdtPhone.setVisibility(View.VISIBLE);
            areaCodeTv.setVisibility(View.VISIBLE);
            areaCodeBtn.setVisibility(View.GONE);
            mTvPhone.setVisibility(View.GONE);
            errorPhoneTv.setVisibility(View.VISIBLE);
            if(null != mAreaCode){
                phoneLength = mAreaCode.getPhoneLength();
                areaCode = mAreaCode.getAreaCode();
                areaCodeTv.setText("+"+mAreaCode.getAreaCode());
                mEdtPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(phoneLength)});
            }
        }

        mSubmit = (TextView) findViewById(R.id.tv_submit);
      mSubmit.setEnabled(false);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modifyPassword();

            }
        });
        mTvVerfyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppUtil.isFastClick(500)) {
                    return;
                }

                if ("获取验证码".equals(mTvVerfyCode.getText().toString().trim()) || "重新获取".equals(mTvVerfyCode.getText().toString().trim())) {
                    getPublicTimeline();
                } else {
                    ToastUtils.showShort("验证码已发送,请稍后再获取");
                }

            }
        });
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        showPwIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOpenEye) {
                    showPwIv.setImageResource(R.drawable.icon_login_pw_visiable);
                    isOpenEye = true;
                    //密码可见
                    mEdtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mEdtPassword2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    showPwIv.setImageResource(R.drawable.icon_login_pw_invisiable);
                    isOpenEye = false;
                    //密码不可见
                    mEdtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mEdtPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                if (null != mEdtPassword && !TextUtils.isEmpty(mEdtPassword.getText().toString().trim())) {
                    mEdtPassword.setSelection(mEdtPassword.getText().length());
                }
                if (null != mEdtPassword2 && !TextUtils.isEmpty(mEdtPassword2.getText().toString().trim())) {
                    mEdtPassword2.setSelection(mEdtPassword2.getText().length());
                }
            }
        });
        areaCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AreaCodeActivity.actionStart(ModifyPasswordActivity.this);
            }
        });
        checkTextListener();

    }

    private void modifyPassword() {
        if (!checkPhone(true) || !checkCode(true) || !checkPassword(true) || !checkRePassword(true)) {
            return;
        }

        LoadingView.showDialog(this, "请求中...");

        if (FIND_PASSWORD == type) {
            phone = mEdtPhone.getText().toString();
        } else {
            phone = UserLocalData.getUser(this).getPhone();
        }
        String password = mEdtPassword.getText().toString();
        String verCode = mEdtVerfyCode.getText().toString();
        String pwdMD5 = MD5Utils.getPsdMD5(password);

        RequestUpPasswordBean requestBean = new RequestUpPasswordBean();
        requestBean.setPhone(phone);
        requestBean.setPassword(pwdMD5);
        requestBean.setVerCode(verCode);
        String sign = EncryptUtlis.getSign2(requestBean);
        requestBean.setSign(sign);


        RxHttp.getInstance().getUsersService()
                //                .modifyPassword( phone, pwdMD5, verCode,sign)
                .modifyPassword(requestBean)
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
                    protected void onError(String errorMsg, String errCode) {
                        if (ErrorCodeUtlis.isLogin(errCode)) {
                            openDialog();
                        }
                    }

                    @Override
                    protected void onSuccess(String data) {
                        if (type == MODIFY_PASSWORD) {
                            ViewShowUtils.showShortToast(ModifyPasswordActivity.this, "修改成功");
                            LoginUtil.logout();
                            ActivityLifeHelper.getInstance().removeAllActivity(SettingActivity.class);
                            finish();
                        } else {
                            PasswordSumbitDialog passwordSumbitDialog = new PasswordSumbitDialog(ModifyPasswordActivity.this, R.style.dialog);
                            passwordSumbitDialog.setOnListner(new PasswordSumbitDialog.OnListener() {
                                @Override
                                public void onClick() {
                                    finish();
                                }
                            });
                            passwordSumbitDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    finish();
                                }
                            });
                            passwordSumbitDialog.show();
                        }
                    }
                });

    }

    private void openDialog() {  //退出确认弹窗
        mLoginDialog = new LoginNotRegeditDialog(this, R.style.dialog, "", "", new LoginNotRegeditDialog.OnOkListener() {
            @Override
            public void onClick(View view, String text) {
                //跳到注册
                LoginEditInviteFragment.start(ModifyPasswordActivity.this, phone, C.sendCodeType.REGISTER,null);
            }
        });
        mLoginDialog.show();
    }

    public void getPublicTimeline() {//短信
        LoadingView.showDialog(this, "请求中...");
        String phone = "";
        if (FIND_PASSWORD == type) {
            phone = mEdtPhone.getText().toString();
        } else {
            phone = UserLocalData.getUser(this).getPhone() == null ? "" : UserLocalData.getUser(this).getPhone();
        }

        RequestAuthCodeBean requestBean = new RequestAuthCodeBean();
        requestBean.setPhone(phone);

        requestBean.setAreaCode(mAreaCode.getAreaCode());
        requestBean.setType(C.sendCodeType.REVAMPPWD);

        RxHttp.getInstance().getUsersService()
                //                .sendAuthCode(phone, C.sendCodeType.REVAMPPWD  )
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
                        ToastUtils.showShort("验证码获取成功");
                        mTvVerfyCode.setTextColor(getResources().getColor(R.color.color_999999));
                        mTvVerfyCode.setBackground(getResources().getDrawable(R.drawable.background_radius_f2f2f2_4dp));
                        ModifyPasswordActivity.Counter counter = new ModifyPasswordActivity.Counter(60 * 1000, 1000); // 第一个参数是倒计时时间，后者为计时间隔，单位毫秒，这里是倒计时
                        counter.start();
                    }
                });
    }

    private void initVerifyCode() {
        mTvVerfyCode.setEnabled(true);
        mTvVerfyCode.setText("获取验证码");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

//               if(checkInput()){
//                   mSubmit.setEnabled(true);
//               }else{
//                   mSubmit.setEnabled(false);
//               }
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
                mTvVerfyCode.setTextColor(getResources().getColor(R.color.color_666666));
                mTvVerfyCode.setBackground(getResources().getDrawable(R.drawable.bg_color_666666_5dp));
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

    private boolean checkInputEmpty() {
        if (null != mEdtPhone && TextUtils.isEmpty(mEdtPhone.getText().toString())) {
            return false;
        }
        if (null != mEdtVerfyCode && TextUtils.isEmpty(mEdtVerfyCode.getText().toString())) {
            return false;
        }
        if (null != mEdtVerfyCode && !TextUtils.isEmpty(mEdtVerfyCode.getText().toString()) && mEdtVerfyCode.getText().toString().length() < 4) {
            return false;
        }

        if (null != mEdtPassword && TextUtils.isEmpty(mEdtPassword.getText().toString())) {
            return false;
        }
        if (null != mEdtPassword2 && TextUtils.isEmpty(mEdtPassword2.getText().toString())) {
            return false;
        }
        return true;
    }

    private void resetErrorTv() {
        errorPhoneTv.setText("");
        errorCodeTv.setText("");
        errorPwTv.setText("");
        errorReTv.setText("");
    }

    private boolean checkPhone(boolean showError) {
        if (type == FIND_PASSWORD) {
            int inputLength = mEdtPhone.getText().toString().length();
            if (null != mEdtPhone && "86".equals(areaCode) && inputLength > 0 && inputLength < phoneLength) {
                errorPhoneTv.setVisibility(View.VISIBLE);
                if (showError) errorPhoneTv.setText("请输入正确的手机号");
                return false;
            }else if(null != mEdtPhone && inputLength> 0 && inputLength < C.PHONE.MIN_LENGTH){
                errorPhoneTv.setVisibility(View.VISIBLE);
                if (showError) errorPhoneTv.setText("请输入正确的手机号");
                return false;
            }
        }
        errorPhoneTv.setText("");
        return true;
    }

    private boolean checkCode(boolean showError) {
        if (null != mEdtVerfyCode && !TextUtils.isEmpty(mEdtVerfyCode.getText().toString()) && mEdtVerfyCode.getText().toString().length() < 4) {
            errorCodeTv.setVisibility(View.VISIBLE);
            if (showError) errorCodeTv.setText("请输入4位验证码");
            return false;
        }

        if (null != mEdtVerfyCode && TextUtils.isEmpty(mEdtVerfyCode.getText().toString())) {
            errorCodeTv.setVisibility(View.VISIBLE);
            if (showError) errorCodeTv.setText("请输入验证码");
            return false;
        }
        errorCodeTv.setText("");
        return true;
    }


    private boolean checkPassword(boolean showError) {
        String errorText = "";
        boolean checkFlag = true;

        if (null != mEdtPassword && TextUtils.isEmpty(mEdtPassword.getText().toString())) {
            errorText = "请输入密码";
            checkFlag = false;
        }
        if (null != mEdtPassword && mEdtPassword.getText().toString().trim().length() < 6) {
            errorText = "密码至少输入6位";
            checkFlag = false;
        }
        if (showError) errorPwTv.setText(errorText);
        return checkFlag;
    }

    private boolean checkRePassword(boolean showError) {
        String errorText = "";
        boolean checkFlag = true;
        String pwd = mEdtPassword.getText().toString().trim();
        if (!checkPassword(false)) {
            return false;
        }

        if (null != mEdtPassword2 && TextUtils.isEmpty(mEdtPassword2.getText().toString().trim())) {
            errorText = "请输入确认密码";
            checkFlag = false;
        }

        boolean correctPwd = AppUtil.isCorrectPwd(this, pwd);
        if (!correctPwd) {
            if (showError) errorReTv.setText(getString(R.string.edit_pwd_error));
            return correctPwd;
        } else {
            checkFlag = correctPwd;
        }
        if (null != mEdtPassword && mEdtPassword2 != null && !mEdtPassword.getText().toString().equals(mEdtPassword2.getText().toString().trim())) {
            errorText = "两次密码不一致";
            checkFlag = false;
        }
        if (showError) errorReTv.setText(errorText);
        return checkFlag;
    }


    private void checkTextListener() {
        mEdtVerfyCode.addTextChangedListener(new CustomTextWatcher(mEdtVerfyCode));
        mEdtPassword.addTextChangedListener(new CustomTextWatcher(mEdtPassword));
        mEdtPassword2.addTextChangedListener(new CustomTextWatcher(mEdtPassword2));
        mEdtPhone.addTextChangedListener(new CustomTextWatcher(mEdtPhone));

        mEdtPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    checkPhone(true);
                }
            }
        });

        mEdtVerfyCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    checkCode(true);
                }
            }
        });

        mEdtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //失去焦点
                    checkPassword(true);
                }
            }
        });
        mEdtPassword2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //失去焦点
                    checkRePassword(true);
                }
            }
        });
    }


    private class CustomTextWatcher implements TextWatcher {
        private TextView view;

        public CustomTextWatcher(TextView view) {
            if (view instanceof TextView)
                this.view = (TextView) view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (checkInputEmpty()) {
                resetErrorTv();
                mSubmit.setEnabled(true);
                mSubmit.setBackgroundResource(R.drawable.image_dengluanniu_huangse);
                mSubmit.setTextColor(Color.WHITE);
            } else {
               mSubmit.setEnabled(false);
                mSubmit.setBackgroundResource(R.drawable.image_dengluanniu_huise);
                mSubmit.setTextColor(Color.parseColor("#999999"));
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == AreaCodeActivity.REQ_AREACODE && data != null) {
            AreaCodeBean areaCodeBean = (AreaCodeBean) data.getSerializableExtra(C.Extras.COUNTRY);
            if(null != areaCodeBean){
                this.mAreaCode = areaCodeBean;
                if(!TextUtils.isEmpty(areaCodeBean.getAreaCode())){
                    areaCode = areaCodeBean.getAreaCode();
                    if(!areaCode.equals("86")){
                        //海外手机最大12位,最小6位
                        phoneLength = C.PHONE.MAX_LENGTH;
                    }else{
                        phoneLength = areaCodeBean.getPhoneLength();
                    }
                    mEdtPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(phoneLength)});
                    areaCodeTv.setText("+"+areaCodeBean.getAreaCode());
                }
            }
        }
    }

}
