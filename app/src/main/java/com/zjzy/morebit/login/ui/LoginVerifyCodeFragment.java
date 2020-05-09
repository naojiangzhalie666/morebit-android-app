package com.zjzy.morebit.login.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.App;
import com.zjzy.morebit.Module.common.Dialog.ClearSDdataDialog;
import com.zjzy.morebit.Module.common.Dialog.LoginNotRegeditDialog;
import com.zjzy.morebit.Module.common.Dialog.ResginDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.login.contract.InputVerifyCodeContract;
import com.zjzy.morebit.login.presenter.InputVerifyCodePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.AreaCodeBean;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.WeixinInfo;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.AreaCodeUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.SharedPreferencesUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.helper.ActivityLifeHelper;
import com.jkb.vcedittext.VerificationAction;
import com.jkb.vcedittext.VerificationCodeEditText;
import com.zjzy.morebit.view.ClearEditText;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


/**
 * 验证码登录
 */

public class LoginVerifyCodeFragment extends MvpFragment<InputVerifyCodePresenter> implements InputVerifyCodeContract.View, View.OnClickListener {
    public static final int LOGIN = 2;
    public static final int WEIXINREGISTER = 3;

    @BindView(R.id.verification_code)
    VerificationCodeEditText mVerificationCodeEditText;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.text_num)
    TextView mTextSend;
    @BindView(R.id.send)
    TextView mBtnSend;
    @BindView(R.id.accountLogin)
    TextView accountLogin;
    @BindView(R.id.areaCodeTv)
    TextView areaCodeTv;
    @BindView(R.id.rl_root)
    RelativeLayout rl_root;
    private Disposable mdDisposable;
    private int loginType;
    private String mPhone = "";
    private String mInvitationCode;
    private WeixinInfo mWeixinInfo;
    private String mId;
    private ClearSDdataDialog mRegisterDialog;
    private LoginNotRegeditDialog mloginDialog;
    private AreaCodeBean mAreaCode;
    private ClearEditText edtPhone;
    private RelativeLayout rl;//返回键
    private TextView ll_userAgreement, privateProtocol;
    private TextView getmsm,next_login,passwordLogin,tv_title;
    private ClearEditText edtsms;//验证码
    private boolean isCheckphone=false;

    public static void srart(Activity activity, int loginType, String phone, String invite, WeixinInfo weixinInfo, AreaCodeBean areaCodeBean) {
        Bundle bundle = new Bundle();
        bundle.putInt(C.Extras.loginType, loginType);
        bundle.putString(C.Extras.PHONE, phone);
        bundle.putSerializable(C.Extras.COUNTRY, areaCodeBean);
        bundle.putString(C.Extras.INVITATION_CODE, invite);
        if (weixinInfo != null) {
            bundle.putSerializable(C.Extras.OAUTH_WX, weixinInfo);
        }
        bundle.putBoolean(C.Extras.openFragment_isSysBar, true);
        OpenFragmentUtils.goToLoginSimpleFragment(activity, LoginVerifyCodeFragment.class.getName(), bundle);
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            loginType = bundle.getInt(C.Extras.loginType, 1);
            mPhone = bundle.getString(C.Extras.PHONE);
            mInvitationCode = bundle.getString(C.Extras.INVITATION_CODE);
            mWeixinInfo = (WeixinInfo) bundle.getSerializable(C.Extras.OAUTH_WX);
            mAreaCode = (AreaCodeBean) bundle.getSerializable(C.Extras.COUNTRY);
        }

        if (null == mAreaCode) {
            mAreaCode = AreaCodeUtil.getDefaultCode();
        } else {
            if (!TextUtils.isEmpty(mAreaCode.getAreaCode())) {
                areaCodeTv.setText("+" + mAreaCode.getAreaCode());
            }
        }

        if ( loginType== C.sendCodeType.REGISTER){
            passwordLogin.setVisibility(View.GONE);
            tv_title.setText("手机号码登录");
            edtPhone.setFocusable(false);
            edtPhone.setText(mPhone);
            if (!TextUtils.isEmpty(mPhone)){
                mPresenter.checkoutPhone(this, edtPhone.getText().toString(), loginType, mAreaCode.getAreaCode());
            }

        }
        if ( loginType== C.sendCodeType.LOGIN){
            tv_title.setText("手机号快速登录");
            edtPhone.setFocusable(false);

        }
        if (loginType== C.sendCodeType.WEIXINBIND || loginType== C.sendCodeType.WEIXINREGISTER){
            passwordLogin.setVisibility(View.GONE);
            tv_title.setText("绑定手机");
            edtPhone.setFocusable(false);
            edtPhone.setText(mPhone);
            if (!TextUtils.isEmpty(mPhone)){
                mPresenter.checkoutPhone(this, edtPhone.getText().toString(), loginType, mAreaCode.getAreaCode());
            }
        }

        if (loginType== C.sendCodeType.BINDWEIXIN){
            passwordLogin.setVisibility(View.GONE);
            tv_title.setText("");
            edtPhone.setFocusable(true);
            edtPhone.setText(mPhone);
            if (!TextUtils.isEmpty(mPhone)){
                mPresenter.checkoutPhone(this, edtPhone.getText().toString(), loginType, mAreaCode.getAreaCode());
            }

        }

        //因为注册的界面已经检查手机和发送验证码才会进来这界面，此条件控制重复发送验证码
//        if (loginType != C.sendCodeType.REGISTER && loginType != C.sendCodeType.WEIXINREGISTER) {
//
//        }
        edtPhone.setText(mPhone);
        //请求验证码
//      else{
//            isCheckphone=true;
//        }
        //countDown();
        // mTextSend.setText(mPhone);

        accountLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });


    }

    @Override
    protected void initView(View view) {

        edtPhone = view.findViewById(R.id.edtPhone);
        rl = view.findViewById(R.id.rl);
        rl.setOnClickListener(this);
        privateProtocol = view.findViewById(R.id.privateProtocol);
        privateProtocol.setOnClickListener(this);
        ll_userAgreement = view.findViewById(R.id.ll_userAgreement);
        ll_userAgreement.setOnClickListener(this);
        getmsm = view.findViewById(R.id.getmsm);//获取验证码
        getmsm.setOnClickListener(this);
        next_login=view.findViewById(R.id.next_login);//登录
        next_login.setOnClickListener(this);
        edtsms=view.findViewById(R.id.edtsms);
        passwordLogin=view.findViewById(R.id.passwordLogin);//密码登录
        passwordLogin.setOnClickListener(this);
        mTitle.setText(R.string.input_verifycode);
        tv_title=view.findViewById(R.id.tv_title);
        rl_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMethodManager(rl_root);
            }
        });
        edtsms.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(edtsms.getText().toString().length()==4){
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

        mVerificationCodeEditText.setOnVerificationCodeChangedListener(new VerificationAction.OnVerificationCodeChangedListener() {
            @Override
            public void onVerCodeChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mVerificationCodeEditText.getText().toString().length() != 4) {
                    //mNextStep.setEnabled(false);
                }
            }

            @Override
            public void onInputCompleted(CharSequence charSequence) {
                //mNextStep.setEnabled(true);
                MyLog.d("aaaaaaaaaaaaaaaaaaa");
                nextStep();
            }
        });

    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_login_verify_code;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    /**
     * 发送验证码
     */
    @OnClick(R.id.send)
    public void sendMsg() {
        //点击后置为不可点击状态
        mBtnSend.setEnabled(false);
        mBtnSend.setTextColor(Color.parseColor("#666666"));
        mBtnSend.setBackgroundResource(R.drawable.bg_white_stroke_666666_30dp);
        LoadingView.showDialog(getActivity(), "请求中...");
        mPresenter.checkoutPhone(this, mPhone, loginType, mAreaCode.getAreaCode());


    }

    /**
     * 注册/登录
     */

    public void nextStep() {

        //点击后置为不可点击状态
        // mNextStep.setClickable(false);
        LoadingView.showDialog(getActivity(), "请求中...");
        ViewShowUtils.hideSoftInput(getActivity(), edtsms);
        switch (loginType) {
            case C.sendCodeType.REGISTER:
                mPresenter.register(this, edtPhone.getText().toString(), edtsms.getText().toString(), mInvitationCode, mAreaCode.getAreaCode());
                break;
            case C.sendCodeType.LOGIN:
                mPresenter.login(this, edtPhone.getText().toString(), edtsms.getText().toString());
                break;
            case C.sendCodeType.WEIXINREGISTER:
                mPresenter.weixinRegister(this, edtPhone.getText().toString(), mInvitationCode, edtsms.getText().toString(), mWeixinInfo);
                break;
            case C.sendCodeType.BINDWEIXIN:
                mPresenter.weixinLogin(this,  edtPhone.getText().toString(), edtsms.getText().toString(), mWeixinInfo);
                break;


            default:
                break;
        }
    }

    @OnClick(R.id.iv_back)
    public void back() {
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mdDisposable != null) {
            mdDisposable.dispose();
        }
    }

    @Override
    public void showFinally() {
        LoadingView.dismissDialog();
    }

    @Override
    public void showData(String msg) {
        countDown();
        //mTextSend.setText( mPhone);
        edtPhone.setText(mPhone);
    }

    @Override
    public void showFailureMessage(String errorMsg) {
//        mBtnSend.setEnabled(true);
//        mBtnSend.setTextColor(Color.parseColor("#F0F0F0"));
//        mBtnSend.setBackgroundResource(R.drawable.bg_white_stroke_f0f0f0_30dp);

        getmsm.setEnabled(true);
        getmsm.setTextColor(Color.parseColor("#F0F0F0"));
        getmsm.setBackgroundResource(R.drawable.bg_white_stroke_f0f0f0_30dp);
    }

    @Override
    public void showRegisterData(UserInfo userInfo) {
        SharedPreferencesUtils.put(App.getAppContext(), C.sp.isShowGuide, true);
        SharedPreferencesUtils.put(App.getAppContext(), C.sp.isShowGuideCircle, true);
        SharedPreferencesUtils.put(App.getAppContext(), C.sp.isShowGuideMine, true);
        String s = MyGsonUtils.beanToJson(userInfo);
        if (TextUtils.isEmpty(s)) {
            return;
        }
        mId = userInfo.getPhone();
        LoginUtil.LoginSuccess(userInfo, getActivity());
        ViewShowUtils.showLongToast(getActivity(), "登录成功");
        ActivityLifeHelper.getInstance().removeAllActivity(LoginSinglePaneActivity.class);
        //openDialog();
    }

    @Override
    public void showRegisterFinally() {
        LoadingView.dismissDialog();
        // mNextStep.setClickable(true);
    }

    @Override
    public void showRegisterFailure(String errCode) {
        // mNextStep.setClickable(true);
        if (C.requestCode.B10051.equals(errCode) || C.requestCode.B10005.equals(errCode)) {
            //openRegisterDialog();
            //提示已经注册
           // openLoginDialog();
        }
    }

    @Override
    public void showLoginFailure(String errCode) {

    }

    @Override
    public void showLoginData(UserInfo userInfo) {
        SharedPreferencesUtils.put(App.getAppContext(), C.sp.isShowGuide, true);
        SharedPreferencesUtils.put(App.getAppContext(), C.sp.isShowGuideCircle, true);
        SharedPreferencesUtils.put(App.getAppContext(), C.sp.isShowGuideMine, true);
        String s = MyGsonUtils.beanToJson(userInfo);
        if (TextUtils.isEmpty(s)) {
            return;
        }
        mId = userInfo.getPhone();
        LoginUtil.LoginSuccess(userInfo, getActivity());
        Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
//        ActivityLifeHelper.getInstance().finishActivity(LoginSinglePaneActivity.class);
        ActivityLifeHelper.getInstance().removeAllActivity(LoginSinglePaneActivity.class);
    }

    private void openRegisterDialog() {  //退出确认弹窗
        mRegisterDialog = new ClearSDdataDialog(getActivity(), R.style.dialog, "提示", "该手机号未注册", new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View view, String text) {
                LoginEditInviteFragment.start(getActivity(), mPhone, C.sendCodeType.REGISTER, null);
            }
        });
        mRegisterDialog.setOkTextAndColor(R.color.color_F32F19, "去注册");
        mRegisterDialog.setCancelTextAndColor(R.color.color_1D88FE, "朕知道了");
//        mDialog.setOnCancelListner(new ClearSDdataDialog.OnCancelListner() {
//            @Override
//            public void onClick(View view, String text) {
//
//            }
//        });
        mRegisterDialog.show();
    }


    private void openLoginDialog() {
//        mloginDialog = new LoginNotRegeditDialog(getActivity(), R.style.dialog, "您已注册", "您已注册，请去登录吧！", new LoginNotRegeditDialog.OnOkListener() {
//            @Override
//            public void onClick(View view, String text) {
//                LoginMainFragment.start(getActivity());
//                ActivityLifeHelper.getInstance().removeAllActivity(LoginSinglePaneActivity.class);
//            }
//        });
//        mloginDialog.setOkTextAndColor(R.color.color_333333, "登录");
//        mloginDialog.setCancelTextAndColor(R.color.color_999999, "取消");
//        mloginDialog.show();
//
//        mloginDialog.setCancelable(false);
        final ResginDialog   dialog=  new ResginDialog(getActivity(), "您已注册", "您已注册，立即登录吧！", "取消", "登录", new ResginDialog.OnOkListener() {
            @Override
            public void onClick(View view) {
                LoginMainFragment.start(getActivity());
              ActivityLifeHelper.getInstance().removeAllActivity(LoginSinglePaneActivity.class);
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

    private void openDialog() {
        mloginDialog = new LoginNotRegeditDialog(getActivity(), R.style.dialog, "注册成功！", "是否前往设置密码？", new LoginNotRegeditDialog.OnOkListener() {
            @Override
            public void onClick(View view, String text) {
                SetPasswordFragment.start(getActivity(), mId);
            }
        });
        mloginDialog.setIcon(R.drawable.login_regedit_success);
        mloginDialog.setOkTextAndColor(R.color.color_333333, "设置密码");
        mloginDialog.setCancelTextAndColor(R.color.color_333333, "暂不设置");
        mloginDialog.setOnCancelListner(new LoginNotRegeditDialog.OnCancelListner() {
            @Override
            public void onClick(View view, String text) {
                ExclusiveWeChatFragment.start(getActivity(), mId);
            }
        });
        mloginDialog.show();

        mloginDialog.setCancelable(false);
    }


    /**
     * 验证码获取倒计时
     */
    private void countDown() {
        //从0开始发射61个数字为：0-60依次输出，延时0s执行，每1s发射一次。
        mdDisposable = Flowable.intervalRange(0, 61, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        int seconds = (int) (60 - aLong);
                        getmsm.setEnabled(false);
                        getmsm.setText(getString(R.string.count_down, seconds + ""));
//                        mBtnSend.setTextColor(Color.parseColor("#666666"));
//                        mBtnSend.setBackgroundResource(R.drawable.bg_white_stroke_666666_30dp);
                        getmsm.setTextColor(getResources().getColor(R.color.color_999999));
                        getmsm.setBackground(getResources().getDrawable(R.drawable.background_radius_f2f2f2_4dp));
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        //倒计时完毕置为可点击状态
                        getmsm.setEnabled(true);
                        getmsm.setText("重新发送");
//                        mBtnSend.setTextColor(Color.parseColor("#F0F0F0"));
//                        mBtnSend.setBackgroundResource(R.drawable.bg_white_stroke_f0f0f0_30dp);
                        getmsm.setTextColor(getResources().getColor(R.color.color_666666));
                        getmsm.setBackground(getResources().getDrawable(R.drawable.bg_color_666666_5dp));
                    }
                })
                .subscribe();
    }

    @Override
    public void loginError(String code) {
//        mNextStep.setClickable(true);
        //   mBtnSend.setEnabled(true);
        getmsm.setEnabled(true);
        if (C.requestCode.B10031.equals(code)) {
            ViewShowUtils.showLongToast(getActivity(), "账号未注册，请先注册");
            LoginEditInviteFragment.start(getActivity(), edtPhone.getText().toString(), mWeixinInfo, mAreaCode);
        }else{//已注册

        }

    }

    @Override
    public void loginSucceed(String userJson) {

        // mNextStep.setClickable(true);
        ViewShowUtils.showLongToast(getActivity(), "登录成功");
        ActivityLifeHelper.getInstance().removeAllActivity(LoginSinglePaneActivity.class);
//     ActivityLifeHelper.getInstance().finishActivity(LoginSinglePaneActivity.class);
    }

    @Override
    public void sendCodeSuccess(String data) {
        countDown();
        ViewShowUtils.showLongToast(getActivity(), "验证码发送成功");
    }

    @Override
    public void sendCodeFail() {
//        mBtnSend.setEnabled(true);
//        mBtnSend.setText("重新发送");
//        mBtnSend.setTextColor(getResources().getColor(R.color.color_666666));
//        mBtnSend.setBackground(getResources().getDrawable(R.drawable.bg_white_stroke_ffd800_14dp));

        getmsm.setEnabled(true);
        getmsm.setText("重新发送");
        getmsm.setTextColor(getResources().getColor(R.color.color_666666));
        getmsm.setBackground(getResources().getDrawable(R.drawable.bg_color_666666_5dp));
    }

    @Override
    public void goToRegister() {
        getmsm.setEnabled(false);
        getmsm.setTextColor(Color.parseColor("#999999"));
        getmsm.setBackgroundResource(R.drawable.background_radius_f2f2f2_4dp);
        LoadingView.showDialog(getActivity(), "请求中...");
        mPresenter.checkoutPhone(this, edtPhone.getText().toString(), loginType, mAreaCode.getAreaCode());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl:
                getActivity().finish();
                break;
            case R.id.privateProtocol:
                LoginUtil.getPrivateProtocol((RxAppCompatActivity) getActivity());
                break;
            case R.id.ll_userAgreement:
                LoginUtil.getUserProtocol((RxAppCompatActivity) getActivity());
                break;
            case R.id.getmsm:
                //点击后置为不可点击状态
                if ("".equals(mInvitationCode)){
                    mPresenter.checkoutPhone2(this, edtPhone.getText().toString(), 1, mAreaCode.getAreaCode());
                }else{
                    getmsm.setEnabled(false);
                    getmsm.setTextColor(Color.parseColor("#999999"));
                    getmsm.setBackgroundResource(R.drawable.background_radius_f2f2f2_4dp);
                    LoadingView.showDialog(getActivity(), "请求中...");
                    mPresenter.checkoutPhone(this, edtPhone.getText().toString(), loginType, mAreaCode.getAreaCode());
                }
                break;
            case R.id.next_login:
               // ToastUtils.showLong("111");
                if (TextUtils.isEmpty(edtsms.getText().toString())){
                    ToastUtils.showLong("验证码不能为空");
                }else{
                  //  mPresenter.login(this, mPhone, edtsms.getText().toString());
                    nextStep();
                }

                break;
            case R.id.passwordLogin:
                LoginPasswordFragment.start(getActivity(),edtPhone.getText().toString(),mAreaCode);
                break;
        }
    }



}
