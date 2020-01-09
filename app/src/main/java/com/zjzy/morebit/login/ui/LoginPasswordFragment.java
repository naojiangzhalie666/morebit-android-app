package com.zjzy.morebit.login.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zjzy.morebit.Activity.ModifyPasswordActivity;
import com.zjzy.morebit.Module.common.Dialog.ClearSDdataDialog;
import com.zjzy.morebit.R;
import com.zjzy.morebit.login.contract.LoginPasswordContract;
import com.zjzy.morebit.login.presenter.LoginPasswordPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.AreaCodeBean;
import com.zjzy.morebit.pojo.WeixinInfo;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.helper.ActivityLifeHelper;
import com.zjzy.morebit.utils.netWork.ErrorCodeUtlis;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by fengrs on 2018/8/8
 * 备注: 密码登录界面
 */

public class LoginPasswordFragment extends MvpFragment<LoginPasswordPresenter> implements LoginPasswordContract.View {
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.phoneTv)
    TextView phoneTv;
    @BindView(R.id.showPwIv)
    ImageView showPwIv;
    @BindView(R.id.errorTv)
    TextView errorTv;
    @BindView(R.id.areaCodeTv)
    TextView areaCodeTv;
    private String mEdtPhoneText, mEdtPasswordText;
    private ClearSDdataDialog mDialog;
    private String mPhone;
    private boolean isOpenEye = false;
    private int loginType;
    private String mInvite;
    private WeixinInfo mWeixinInfo;
    private AreaCodeBean mAreaCode;
    private int phoneLength = 11; //默认是中国11位
    private String areaCode = "86";
    @Override
    protected int getViewLayout() {
        return R.layout.activity_login_password1;
    }



    public static void start(Activity activity, String phone, AreaCodeBean areaCodeBean) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(C.Extras.openFragment_isSysBar, true);
        bundle.putString(C.Extras.PHONE,phone);
        bundle.putSerializable(C.Extras.COUNTRY,areaCodeBean);
        OpenFragmentUtils.goToLoginSimpleFragment(activity, LoginPasswordFragment.class.getName(), bundle);
    }


    @Override
    protected void initData() {
    }

    @Override
    protected void initView(View view) {
        mPhone = getArguments().getString(C.Extras.PHONE, "");
        mAreaCode = (AreaCodeBean) getArguments().getSerializable(C.Extras.COUNTRY);
        mEdtPhoneText = mPhone.trim();
        if (!TextUtils.isEmpty(mPhone)) {
            phoneTv.setText(mPhone);
        }
        showPwIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isOpenEye) {
                    showPwIv.setImageResource(R.drawable.icon_login_pw_visiable);
                    isOpenEye = true;
                    //密码可见
                    edt_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    showPwIv.setImageResource(R.drawable.icon_login_pw_invisiable);
                    isOpenEye = false;
                    //密码不可见
                    edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                if(null != edt_password && !TextUtils.isEmpty(edt_password.getText().toString().trim())){
                    edt_password.setSelection(edt_password.getText().length());
                }
            }
        });
        edt_password.setFilters(new InputFilter[]{AppUtil.getTrimInputFilter()});
        if(null != mAreaCode && !TextUtils.isEmpty(mAreaCode.getAreaCode())){
            areaCodeTv.setText("+"+mAreaCode.getAreaCode());
            phoneLength = mAreaCode.getPhoneLength();
            areaCode = mAreaCode.getAreaCode();
        }
    }




    @OnTextChanged(value = R.id.edt_password, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterTextChangedPwd(Editable s) {
        mEdtPasswordText = s.toString().trim();
        errorTv.setVisibility(View.INVISIBLE);
        errorTv.setText("");
        if(!TextUtils.isEmpty(areaCode) && "86".equals(areaCode)){
            if (mEdtPhoneText != null && mEdtPasswordText != null && mEdtPhoneText.length() == phoneLength && mEdtPasswordText.length() > 1) {
                login.setEnabled(true);
                login.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                login.setEnabled(false);
                login.setTextColor(Color.parseColor("#333333"));
            }
        }else{
            //海外手机
            if (mEdtPhoneText != null && mEdtPasswordText != null &&  mEdtPasswordText.length() > 1) {
                login.setEnabled(true);
                login.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                login.setEnabled(false);
                login.setTextColor(Color.parseColor("#333333"));
            }
        }
    }

    @OnClick(R.id.iv_back)
    public void back() {
        getActivity().finish();
    }


    @OnClick({R.id.login,R.id.password_login,R.id.forget_password,})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.password_login:
                LoginVerifyCodeFragment.srart(getActivity(), C.sendCodeType.LOGIN, mEdtPhoneText, mInvite, mWeixinInfo,mAreaCode);
                break;
            case R.id.forget_password:
                ModifyPasswordActivity.start(getActivity(), ModifyPasswordActivity.FIND_PASSWORD, mPhone.trim(),mAreaCode);
                break;
            case R.id.login:
                if (AppUtil.isFastClick(1000)) {
                    return;
                }
                if (!checkInput()) {
                    return;
                }
                mPresenter.Passwordlogin(this,mEdtPhoneText.toString(),mEdtPasswordText.toString(),mAreaCode.getAreaCode());
                break;


        }
    }

    private boolean checkInput() {
        if (null != mEdtPhoneText && TextUtils.isEmpty(mEdtPhoneText)) {
            Toast.makeText(getActivity(), "请输入手机号", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (null != mEdtPasswordText && TextUtils.isEmpty(mEdtPasswordText)) {
            Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (null != mEdtPhoneText && !LoginUtil.isMobile(mEdtPhoneText.toString())) {
            Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }
    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    public void loginError(String  code) {
        if (ErrorCodeUtlis.isLogin(code)) {
            //用户不存在  跳转到注册页
            openDialog();
        }else if(C.requestCode.B10003.equals(code)){
            //密码不正确
            errorTv.setVisibility(View.VISIBLE);
            errorTv.setText("密码不正确");
        }
    }

    @Override
    public void loginSucceed(String userJson) {
        ViewShowUtils.showLongToast(getActivity(), "登录成功");
        ActivityLifeHelper.getInstance().removeAllActivity(LoginSinglePaneActivity.class);
    }

    @Override
    public void sendCodeSuccess(String data) {

    }

    @Override
    public void sendCodeFail() {

    }

    private void openDialog() {  //退出确认弹窗
        mDialog = new ClearSDdataDialog(getActivity(), R.style.dialog, "提示", "该手机号未注册", new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View view, String text) {
                LoginEditInviteFragment.start(getActivity(),mPhone.trim(),C.sendCodeType.REGISTER,null);
            }
        });
        mDialog.setOkTextAndColor(R.color.color_F32F19, "去注册");
        mDialog.setCancelTextAndColor(R.color.color_1D88FE, "朕知道了");
//        mDialog.setOnCancelListner(new ClearSDdataDialog.OnCancelListner() {
//            @Override
//            public void onClick(View view, String text) {
//
//            }
//        });
        mDialog.show();
    }



}
