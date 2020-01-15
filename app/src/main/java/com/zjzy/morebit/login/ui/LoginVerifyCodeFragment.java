package com.zjzy.morebit.login.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zjzy.morebit.App;
import com.zjzy.morebit.Module.common.Dialog.ClearSDdataDialog;
import com.zjzy.morebit.Module.common.Dialog.LoginNotRegeditDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.login.contract.InputVerifyCodeContract;
import com.zjzy.morebit.login.presenter.InputVerifyCodePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.AreaCodeBean;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.WeixinInfo;
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

public class LoginVerifyCodeFragment extends MvpFragment<InputVerifyCodePresenter> implements InputVerifyCodeContract.View {
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

    private Disposable mdDisposable;
    private int loginType;
    private String mPhone ="";
    private String mInvitationCode;
    private WeixinInfo mWeixinInfo;
    private String mId;
    private ClearSDdataDialog mRegisterDialog;
    private LoginNotRegeditDialog mloginDialog;
    private AreaCodeBean mAreaCode;

    public static void srart(Activity activity, int loginType, String phone, String invite, WeixinInfo weixinInfo, AreaCodeBean areaCodeBean) {
        Bundle bundle = new Bundle();
        bundle.putInt(C.Extras.loginType, loginType);
        bundle.putString(C.Extras.PHONE, phone);
        bundle.putSerializable(C.Extras.COUNTRY,areaCodeBean);
        bundle.putString(C.Extras.INVITATION_CODE, invite);
        if (weixinInfo != null) {
            bundle.putSerializable(C.Extras.OAUTH_WX, weixinInfo);
        }
        bundle.putBoolean(C.Extras.openFragment_isSysBar, true);
        OpenFragmentUtils.goToLoginSimpleFragment(activity, LoginVerifyCodeFragment.class.getName(), bundle);
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

        if(null == mAreaCode){
            mAreaCode = AreaCodeUtil.getDefaultCode();
        }else{
            if(!TextUtils.isEmpty(mAreaCode.getAreaCode())){
                areaCodeTv.setText("+"+mAreaCode.getAreaCode());
            }
        }

        //因为注册的界面已经检查手机和发送验证码才会进来这界面，此条件控制重复发送验证码
        if(loginType != C.sendCodeType.REGISTER && loginType != C.sendCodeType.WEIXINREGISTER){
            //请求验证码
            mPresenter.checkoutPhone(this, mPhone, loginType,mAreaCode.getAreaCode());
        }
        countDown();
        mTextSend.setText(mPhone);
        accountLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });




    }

    @Override
    protected void initView(View view) {
        mTitle.setText(R.string.input_verifycode);
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
        mPresenter.checkoutPhone(this, mPhone, loginType,mAreaCode.getAreaCode());


    }

    /**
     * 注册/登录
     */

    public void nextStep() {

        //点击后置为不可点击状态
       // mNextStep.setClickable(false);
        LoadingView.showDialog(getActivity(), "请求中...");
        ViewShowUtils.hideSoftInput(getActivity(), mVerificationCodeEditText);
        switch (loginType) {
            case C.sendCodeType.REGISTER:
                mPresenter.register(this, mPhone, mVerificationCodeEditText.getText().toString(), mInvitationCode,mAreaCode.getAreaCode());
                break;
            case C.sendCodeType.LOGIN:
                mPresenter.login(this, mPhone, mVerificationCodeEditText.getText().toString());
                break;
            case C.sendCodeType.WEIXINREGISTER:
                mPresenter.weixinRegister(this, mPhone, mInvitationCode, mVerificationCodeEditText.getText().toString(), mWeixinInfo);
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
        mTextSend.setText( mPhone);
    }

    @Override
    public void showFailureMessage(String errorMsg) {
        mBtnSend.setEnabled(true);
        mBtnSend.setTextColor(Color.parseColor("#F0F0F0"));
        mBtnSend.setBackgroundResource(R.drawable.bg_white_stroke_f0f0f0_30dp);
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
        openDialog();
    }

    @Override
    public void showRegisterFinally() {
        LoadingView.dismissDialog();
       // mNextStep.setClickable(true);
    }

    @Override
    public void showRegisterFailure(String errCode) {
       // mNextStep.setClickable(true);
        if (C.requestCode.B10051.equals(errCode)||C.requestCode.B10005.equals(errCode)) {
            //openRegisterDialog();
            //提示已经注册
            openLoginDialog();
        }
    }

    private void openRegisterDialog() {  //退出确认弹窗
        mRegisterDialog = new ClearSDdataDialog(getActivity(), R.style.dialog, "提示", "该手机号未注册", new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View view, String text) {
                LoginEditInviteFragment.start(getActivity(), mPhone, C.sendCodeType.REGISTER,null);
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
                        mBtnSend.setEnabled(false);
                        mBtnSend.setText(getString(R.string.count_down, seconds + ""));
//                        mBtnSend.setTextColor(Color.parseColor("#666666"));
//                        mBtnSend.setBackgroundResource(R.drawable.bg_white_stroke_666666_30dp);
                        mBtnSend.setTextColor(getResources().getColor(R.color.color_999999));
                        mBtnSend.setBackground(getResources().getDrawable(R.drawable.bg_white_stroke_333333_30dp));
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        //倒计时完毕置为可点击状态
                        mBtnSend.setEnabled(true);
                        mBtnSend.setText("重新发送");
//                        mBtnSend.setTextColor(Color.parseColor("#F0F0F0"));
//                        mBtnSend.setBackgroundResource(R.drawable.bg_white_stroke_f0f0f0_30dp);
                        mBtnSend.setTextColor(getResources().getColor(R.color.color_666666));
                        mBtnSend.setBackground(getResources().getDrawable(R.drawable.bg_white_stroke_666666_30dp));
                    }
                })
                .subscribe();
    }

    @Override
    public void loginError(String code) {
//        mNextStep.setClickable(true);
        mBtnSend.setEnabled(true);
        if (C.requestCode.B10051.equals(code)||C.requestCode.B10005.equals(code)) {
            openLoginDialog();
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
        mBtnSend.setEnabled(true);
        mBtnSend.setText("重新发送");
        mBtnSend.setTextColor(getResources().getColor(R.color.color_666666));
        mBtnSend.setBackground(getResources().getDrawable(R.drawable.bg_white_stroke_ffd800_14dp));
    }
}
