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
import com.zjzy.morebit.Module.common.Dialog.InvateBindDialog;
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
    private static int mid=2;
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
    private static  WeixinInfo mWeixinInfo;
    private TextView tv_bind;

   private int phoneLength = 11; //默认是中国11位
    private String areaCode = "86"; //默认是中国的86
    private AreaCodeBean mAreaCode;
    private boolean wxLoginFlag = false;
    private static String mditInviteText;
    private static String mphone;
    private TextView sub_phone;
    public static void start(Activity activity,String phone,int id,WeixinInfo weixinInfo,String mEditInviteText) {
        Bundle bundle = new Bundle();
        mid=id;
        mWeixinInfo=weixinInfo;
        mditInviteText=mEditInviteText;
        mphone=phone;
        bundle.putBoolean(C.Extras.openFragment_isSysBar, true);
        OpenFragmentUtils.goToLoginSimpleFragment(activity,LoginMainFragment.class.getName(),bundle);
    }
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
                .statusBarColor(R.color.color_F05557)
                .init();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        rl=view.findViewById(R.id.rl);
        tv_bind=view.findViewById(R.id.tv_bind);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mAreaCode = AreaCodeUtil.getDefaultCode();
        sub_phone=view.findViewById(R.id.sub_phone);
        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(checkPhone()){
                    next_login.setEnabled(true);
                    next_login.setTextColor(Color.parseColor("#FFFFFF"));
                    next_login.setBackgroundResource(R.mipmap.phone_login_next);
                }else{
                    next_login.setEnabled(false);
                    next_login.setTextColor(Color.parseColor("#999999"));
                    next_login.setBackgroundResource(R.mipmap.next_login_hiu);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if (mid==1){
            tv_bind.setVisibility(View.VISIBLE);
            sub_phone.setText("输入手机号");

        }else{
            tv_bind.setVisibility(View.GONE);
            sub_phone.setText("输入手机号");
        }
        tv_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginVerifyCodeFragment.srart(getActivity(), C.sendCodeType.BINDWEIXIN, edtPhone.getText().toString().trim(),mditInviteText, mWeixinInfo,mAreaCode);

            }
        });

        if (!TextUtils.isEmpty(mphone)){
            edtPhone.setText(mphone);
        }
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

              //  LoginEditInviteFragment.start(getActivity(),"",C.sendCodeType.REGISTER,this.mAreaCode);
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
                    if (mid==2){
                        mPresenter.checkoutPhone(this,mEditPhone,1,areaCode);
                    }else{
                        mPresenter.checkoutPhone(this,mEditPhone,6,areaCode);
                    }

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
        WeixinInfo weixinInfo = mPresenter.getWeixinInfo();
        if (mid!=2){
            if (C.requestCode.B10005.equals(code)){//手机号已注册 绑定微信
                LoginVerifyCodeFragment.srart(getActivity(), C.sendCodeType.BINDWEIXIN, edtPhone.getText().toString().trim(), mditInviteText, mWeixinInfo,mAreaCode);
                //   LoginVerifyCodeFragment.srart(getActivity(), C.sendCodeType.WEIXINREGISTER, edtPhone.getText().toString().trim(), mditInviteText, mWeixinInfo,mAreaCode);
            }
        }else{
            if (C.requestCode.B10031.equals(code)){//用户未注册
                LoginEditInviteFragment.start(getActivity(),edtPhone.getText().toString().trim(),C.sendCodeType.REGISTER,mAreaCode);
            }
        }


//        if (C.requestCode.SUCCESS.equals(code)){
//            //手机号已注册
//           // final WeixinInfo weixinInfo = mPresenter.getWeixinInfo();
//            LoginVerifyCodeFragment.srart(getActivity(), C.sendCodeType.LOGIN, edtPhone.getText().toString().trim(), mInvite, weixinInfo,mAreaCode);
//        } else if (ErrorCodeUtlis.isNuRegister(code)) {
//            //用户不存在  跳转到注册页
//           // WeixinInfo weixinInfo = mPresenter.getWeixinInfo();
//
//            LoginEditInviteFragment.start(getActivity(),edtPhone.getText().toString().trim(),weixinInfo,mAreaCode);
//        }else if(ErrorCodeUtlis.isRegister(code)){
//            //手机号已注册
//         //   final WeixinInfo weixinInfo = mPresenter.getWeixinInfo();
//           // LoginPasswordFragment.start(getActivity(),edtPhone.getText().toString().trim(),mAreaCode);
////            final ResginDialog   dialog=  new ResginDialog(getActivity(), "您已注册", "您已注册，立即登录吧！", "取消", "登录", new ResginDialog.OnOkListener() {
////                @Override
////                public void onClick(View view) {
////
////
////                }
////            });
////            dialog.setmCancelListener(new ResginDialog.OnCancelListner() {
////                @Override
////                public void onClick(View view) {
////                    dialog.dismiss();
////                }
////            });
////            dialog.show();
//
//            if (mid!=1){
//                LoginVerifyCodeFragment.srart(getActivity(), C.sendCodeType.LOGIN, edtPhone.getText().toString().trim(), mInvite, weixinInfo,mAreaCode);
//            }else{
//                LoginVerifyCodeFragment.srart(getActivity(), C.sendCodeType.BINDWEIXIN, edtPhone.getText().toString().trim(), mditInviteText, mWeixinInfo,mAreaCode);
//            }

       // }
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
        if (mid==2){
            LoginVerifyCodeFragment.srart(getActivity(), C.sendCodeType.LOGIN, edtPhone.getText().toString().trim(), mInvite, mWeixinInfo,mAreaCode);
        }else{
            LoginVerifyCodeFragment.srart(getActivity(), C.sendCodeType.WEIXINREGISTER, edtPhone.getText().toString().trim(), mditInviteText, mWeixinInfo,mAreaCode);
        }


//        mDialog = new LoginNotRegeditDialog(getActivity(), R.style.dialog, "", "", new LoginNotRegeditDialog.OnOkListener() {
//            @Override
//            public void onClick(View view, String text) {
//                //跳到注册
//                LoginEditInviteFragment.start(getActivity(),edtPhone.getText().toString().trim(),C.sendCodeType.REGISTER,mAreaCode);
//            }
//        });
//        mDialog.show();
        //跳到注册
//        if (mid!=1){
//            LoginEditInviteFragment.start(getActivity(),edtPhone.getText().toString().trim(),C.sendCodeType.REGISTER,mAreaCode);
//        }else{
//            LoginVerifyCodeFragment.srart(getActivity(), C.sendCodeType.WEIXINREGISTER, edtPhone.getText().toString().trim(), mditInviteText, mWeixinInfo,mAreaCode);
//        }
//      dialog=  new ResginDialog(getActivity(), "尚未注册", "您还没有注册喔，快去注册吧！", "取消", "注册", new ResginDialog.OnOkListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });
//        dialog.setmCancelListener(new ResginDialog.OnCancelListner() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//      dialog.show();

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
