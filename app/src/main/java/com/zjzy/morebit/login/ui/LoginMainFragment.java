package com.zjzy.morebit.login.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.Module.common.Dialog.LoginNotRegeditDialog;
import com.zjzy.morebit.Module.common.Dialog.ResginDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.login.contract.LoginMainContract;
import com.zjzy.morebit.login.presenter.LoginMainPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.AreaCodeBean;
import com.zjzy.morebit.pojo.WeixinInfo;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.AreaCodeUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.WechatUtil;
import com.zjzy.morebit.utils.helper.ActivityLifeHelper;
import com.zjzy.morebit.utils.netWork.ErrorCodeUtlis;
import com.zjzy.morebit.view.ClearEditText;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fengrs on 2018/8/7
 * 备注: 登录主页
 */

public class LoginMainFragment extends MvpFragment<LoginMainPresenter> implements LoginMainContract.View {
   @BindView(R.id.rl_root)
    RelativeLayout rl_root;
   @BindView(R.id.next_login)
    TextView next_login;
   @BindView(R.id.edtPhone)
   ClearEditText edtPhone;
   @BindView(R.id.areaCodeTv)
   TextView areaCodeTv;

   @BindView(R.id.ll_weixin_btn)
    LinearLayout llWeixinBtn;

   @BindView(R.id.ll_mobile_register)
   LinearLayout llMobileRegister;

   LoginNotRegeditDialog mDialog;
   private ResginDialog dialog;
   private RelativeLayout rl;
    private String mInvite;
    private WeixinInfo mWeixinInfo;

   private int phoneLength = 11; //默认是中国11位
    private String areaCode = "86"; //默认是中国的86
    private AreaCodeBean mAreaCode;
    private boolean wxLoginFlag = false;
    public static void start(Activity activity) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(C.Extras.openFragment_isSysBar, true);
        OpenFragmentUtils.goToLoginSimpleFragment(activity,LoginMainFragment.class.getName(),bundle);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(getActivity())
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(false)
                .statusBarColor(R.color.color_FFD4CF)
                .init();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        rl=view.findViewById(R.id.rl);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mAreaCode = AreaCodeUtil.getDefaultCode();
        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(checkPhone()){
                    next_login.setEnabled(true);
                    next_login.setTextColor(Color.parseColor("#FFFFFF"));
                }else{
                    next_login.setEnabled(false);
                    next_login.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_login_main;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }


    @OnClick({R.id.iv_back, R.id.ll_mobile_register, R.id.ll_weixin_btn,R.id.next_login,R.id.ll_userAgreement,R.id.areaCodeBtn,R.id.privateProtocol})
    public void onclick(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.ll_mobile_register:
                //aaaa
//                ModifyPasswordActivity.start(this, ModifyPasswordActivity.FIND_PASSWORD, "");

                LoginEditInviteFragment.start(getActivity(),"",C.sendCodeType.REGISTER,this.mAreaCode);
                break;
//            case R.id.login:
//                LoginPasswordFragment.start(getActivity());
//                break;
            case R.id.ll_weixin_btn:
                if (AppUtil.isFastClick(1000)) {
                    return;
                }
                if (!AppUtil.isWeixinAvilible(getActivity())) {
                    ViewShowUtils.showShortToast(getActivity(), "请先安装微信客户端");
                    return;
                }
                wxLoginFlag = true;
                mPresenter.goToWXLogin(this);
                break;
            case R.id.next_login:
               String  mEditPhone = edtPhone.getText().toString().trim();
               if (!isMobile()){
                   ToastUtils.showLong("请输入正确的手机号");
                   return;
               }
                if (mEditPhone != null  && checkPhone()) {
                    mPresenter.checkoutPhone(this,mEditPhone,6,areaCode);
                }

                break;
            case R.id.ll_userAgreement:
                LoginUtil.getUserProtocol((RxAppCompatActivity) getActivity());
                break;
            case R.id.privateProtocol:
                LoginUtil.getPrivateProtocol((RxAppCompatActivity) getActivity());
                break;
            case R.id.areaCodeBtn:
                AreaCodeActivity.actionStart(getActivity());
                break;
        }
    }

    public boolean checkPhone(){
        String  mEditPhone = edtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(mEditPhone)) {
            return false;
        }

        if (mEditPhone != null  && "86".equals(areaCode) && mEditPhone.length() < phoneLength || mEditPhone.length()> phoneLength) {
            return false;
        }else if(mEditPhone != null  && mEditPhone.length() < C.PHONE.MIN_LENGTH ){
            return false;
        }

        return true;
    }

    //判断手机号格式是否正确
    public  boolean isMobile() {
        String  mEditPhone = edtPhone.getText().toString().trim();
        return Pattern.matches("^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$", mEditPhone);
    }

    /**
     * 微信未注册
     * @param code
     */
    @Override
    public void loginError(String code) {
        if (ErrorCodeUtlis.isNuRegister(code)) {
            //用户不存在  跳转到注册页

            WeixinInfo weixinInfo = mPresenter.getWeixinInfo();
            LoginEditInviteFragment.start(getActivity(),edtPhone.getText().toString().trim(),weixinInfo,mAreaCode);
        }else if(ErrorCodeUtlis.isRegister(code)){
            //手机号已注册
           // LoginPasswordFragment.start(getActivity(),edtPhone.getText().toString().trim(),mAreaCode);
            LoginVerifyCodeFragment.srart(getActivity(), C.sendCodeType.LOGIN, edtPhone.getText().toString().trim(), mInvite, mWeixinInfo,mAreaCode);
        }
    }


    @Override
    public void loginSucceed(String userJson) {
        Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
//        ActivityLifeHelper.getInstance().finishActivity(LoginSinglePaneActivity.class);
        ActivityLifeHelper.getInstance().removeAllActivity(LoginSinglePaneActivity.class);
    }

    @Override
    public void sendCodeSuccess(String data) {

    }

    @Override
    public void sendCodeFail() {

    }


    @Override
    public void goToRegister() {
//        mDialog = new LoginNotRegeditDialog(getActivity(), R.style.dialog, "", "", new LoginNotRegeditDialog.OnOkListener() {
//            @Override
//            public void onClick(View view, String text) {
//                //跳到注册
//                LoginEditInviteFragment.start(getActivity(),edtPhone.getText().toString().trim(),C.sendCodeType.REGISTER,mAreaCode);
//            }
//        });
//        mDialog.show();

      dialog=  new ResginDialog(getActivity(), "", "", "", "", new ResginDialog.OnOkListener() {
            @Override
            public void onClick(View view) {
                //跳到注册
              LoginEditInviteFragment.start(getActivity(),edtPhone.getText().toString().trim(),C.sendCodeType.REGISTER,mAreaCode);
            }
        });
        dialog.setmCancelListener(new ResginDialog.OnCancelListner() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
      dialog.show();

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
                        phoneLength = C.PHONE.MAX_LENGTH;
                    }else{
                        phoneLength = areaCodeBean.getPhoneLength();
                    }
                    edtPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(phoneLength)});
                    if(checkPhone()){
                        next_login.setEnabled(true);
                    }else{
                        next_login.setEnabled(false);
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(wxLoginFlag){
            wxLoginFlag = false;
            LoadingView.dismissDialog();
        }

    }
}
