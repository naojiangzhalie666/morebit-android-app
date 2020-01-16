package com.zjzy.morebit.login.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zjzy.morebit.Module.common.Dialog.ClearSDdataDialog;
import com.zjzy.morebit.Module.common.Dialog.LoginNotRegeditDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.login.contract.LoginEditPhoneContract;
import com.zjzy.morebit.login.presenter.LoginEditPhonePresenter;
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
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by fengrs on 2018/8/8
 * 备注: 密码登录界面
 */

public class LoginEditPhoneFragment extends MvpFragment<LoginEditPhonePresenter> implements LoginEditPhoneContract.View {
    @BindView(R.id.edtPhone)
    EditText edtPhone;
    @BindView(R.id.tv_next)
    TextView tv_next;
    @BindView(R.id.ll_userAgreement)
    LinearLayout ll_userAgreement;
    @BindView(R.id.area)
    TextView areaCodeTv;
    @BindView(R.id.phoneTips)
    TextView phoneTips;
    private String mEdtPhoneText = "";
    private ClearSDdataDialog mDialog;
    private ClearSDdataDialog mRegisterDialog;
    private LoginNotRegeditDialog mloginDialog;
    private int loginType;

    private String mInvite;
    private String mPhone;
    private int mPhoneLenght = 11;
    private AreaCodeBean mAreaCode;
    private WeixinInfo mWeixinInfo;
    private boolean isSendCode = false;
    private String areaCode = "86";

    public static void start(Activity activity, int loginType, String invite, String phone) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(C.Extras.openFragment_isSysBar, true);
        bundle.putInt(C.Extras.loginType, loginType);
        bundle.putString(C.Extras.INVITATION_CODE, invite);
        bundle.putString(C.Extras.PHONE, phone);
        OpenFragmentUtils.goToLoginSimpleFragment(activity, LoginEditPhoneFragment.class.getName(), bundle);
    }

    public static void start(Activity activity, int loginType, String invite, String phone, WeixinInfo weixinInfo,AreaCodeBean areaCode) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(C.Extras.openFragment_isSysBar, true);
        bundle.putInt(C.Extras.loginType, loginType);
        bundle.putString(C.Extras.INVITATION_CODE, invite);
        if (weixinInfo != null) {
            bundle.putSerializable(C.Extras.OAUTH_WX, weixinInfo);
        }
        bundle.putString(C.Extras.PHONE, phone);
        bundle.putSerializable(C.Extras.AREACODE,areaCode);
        OpenFragmentUtils.goToLoginSimpleFragment(activity, LoginEditPhoneFragment.class.getName(), bundle);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        loginType = getArguments().getInt(C.Extras.loginType, 1);
        mInvite = getArguments().getString(C.Extras.INVITATION_CODE, "");
        mWeixinInfo = (WeixinInfo) getArguments().getSerializable(C.Extras.OAUTH_WX);
        mPhone = getArguments().getString(C.Extras.PHONE, "");
        mAreaCode = (AreaCodeBean) getArguments().getSerializable(C.Extras.AREACODE);
        if (loginType == C.sendCodeType.REGISTER || loginType == C.sendCodeType.WEIXINREGISTER) {
            ll_userAgreement.setVisibility(View.VISIBLE);
        } else {
            ll_userAgreement.setVisibility(View.GONE);
        }
        if(null != mAreaCode && !TextUtils.isEmpty(mAreaCode.getAreaCode())){
            areaCode = mAreaCode.getAreaCode();
            areaCodeTv.setText("+"+mAreaCode.getAreaCode());
            mPhoneLenght = mAreaCode.getPhoneLength();
            edtPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mPhoneLenght)});
        }
        if (!TextUtils.isEmpty(mPhone)) {
            edtPhone.setText(mPhone);
        }

        if(!areaCode.equals("86")){
            phoneTips.setText("6-12位");
        }else{
            phoneTips.setText("11位");
        }

    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_login_edit_phone;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }


    @OnTextChanged(value = R.id.edtPhone, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterTextChanged(Editable s) {
        mEdtPhoneText = s.toString().trim();
        checkphone();
    }

    private void checkphone() {
        if("86".equals(areaCode)){
            if (mEdtPhoneText != null && mEdtPhoneText.length() == mPhoneLenght) {
                tv_next.setEnabled(true);
                tv_next.setTextColor(Color.parseColor("#FFFFFF"));
            }else {
                tv_next.setEnabled(false);
                tv_next.setTextColor(Color.parseColor("#333333"));
            }
        }else{
            if(mEdtPhoneText != null  && mEdtPhoneText.length() >= C.PHONE.MIN_LENGTH ){
                tv_next.setEnabled(true);
                tv_next.setTextColor(Color.parseColor("#FFFFFF"));
            }else{
                tv_next.setEnabled(false);
                tv_next.setTextColor(Color.parseColor("#333333"));
            }
        }
    }


    @OnClick({R.id.tv_next, R.id.userAgreementTv,R.id.areaCodeBtn,R.id.privateProtocol})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.privateProtocol:
                LoginUtil.getPrivateProtocol((RxAppCompatActivity) getActivity());
                break;
            case R.id.userAgreementTv:
                LoginUtil.getUserProtocol((RxAppCompatActivity) getActivity());
                break;
            case R.id.tv_next:
                if (AppUtil.isFastClick(500)) {
                    return;
                }
                if (!checkInput()) {
                    return;
                }
                isSendCode = false;
                LoadingView.showDialog(getActivity(), "请求中...");
                mPresenter.checkoutPhone(this, mEdtPhoneText.toString(), loginType,mAreaCode.getAreaCode());
                break;
            case R.id.areaCodeBtn:
                AreaCodeActivity.actionStart(getActivity());
                break;
        }
    }

    private boolean checkInput() {
        if (null != mEdtPhoneText && TextUtils.isEmpty(mEdtPhoneText)) {
            Toast.makeText(getActivity(), "请输入手机号", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (null != mEdtPhoneText && !LoginUtil.isMobile(mEdtPhoneText.toString())) {
            Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }


    @Override
    public void loginError(String code) {
        switch (loginType) {
            case C.sendCodeType.LOGIN:
                if (ErrorCodeUtlis.isLogin(code)) {
                    openDialog();
                }
                break;
            case C.sendCodeType.REGISTER:
            case C.sendCodeType.WEIXINREGISTER:
                if (ErrorCodeUtlis.isRegister(code)) {
                    openLoginDialog();
                }
                break;

        }
    }


    private void openLoginDialog() {  //退出确认弹窗

        ViewShowUtils.hideSoftInput(getActivity(), edtPhone);
        mloginDialog = new LoginNotRegeditDialog(getActivity(), R.style.dialog, "您已注册", "您已注册，请去登录吧！", new LoginNotRegeditDialog.OnOkListener() {
            @Override
            public void onClick(View view, String text) {
                LoginMainFragment.start(getActivity());
                ActivityLifeHelper.getInstance().removeAllActivity(LoginSinglePaneActivity.class);
            }
        });
        mloginDialog.setOkTextAndColor(R.color.color_333333, "登录");
        mloginDialog.setCancelTextAndColor(R.color.color_999999, "取消");
        mloginDialog.show();

        mloginDialog.setCancelable(false);

    }

    private void openDialog() {  //退出确认弹窗
        mDialog = new ClearSDdataDialog(getActivity(), R.style.dialog, "提示", "该手机号未注册", new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View view, String text) {
                LoginEditInviteFragment.start(getActivity(), edtPhone.getText().toString().trim(), C.sendCodeType.REGISTER,mAreaCode);
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



    @Override
    public void loginSucceed(String s) {
    }

    @Override
    public void sendCodeSuccess(String data) {
        isSendCode = true;
        ViewShowUtils.showShortToast(getActivity(), "验证码发送成功");
        LoginVerifyCodeFragment.srart(getActivity(), loginType, mEdtPhoneText, mInvite, mWeixinInfo,mAreaCode);
    }

    @Override
    public void sendCodeFail() {
        if(!isSendCode) LoginVerifyCodeFragment.srart(getActivity(), loginType, mEdtPhoneText, mInvite, mWeixinInfo,mAreaCode);
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
                    areaCodeTv.setText("+"+areaCodeBean.getAreaCode());
                    if(!areaCode.equals("86")){
                        //海外手机最大12位,最小6位
                        mPhoneLenght = C.PHONE.MAX_LENGTH;
                        phoneTips.setText("6-12位");
                    }else{
                        phoneTips.setText("11位");
                        mPhoneLenght = areaCodeBean.getPhoneLength();
                    }
                    edtPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mPhoneLenght)});
                    checkphone();
                }
            }
        }
    }
}
