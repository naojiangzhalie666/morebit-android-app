package com.zjzy.morebit.login.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zjzy.morebit.App;
import com.zjzy.morebit.Module.common.Dialog.ClearSDdataDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.login.contract.InputVerifyCodeContract;
import com.zjzy.morebit.login.presenter.InputVerifyCodePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.WeixinInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyGsonUtils;
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
 * e
 * Created by YangBoTian on 2018/8/7.
 * 验证码输入
 */

public class InputVerifyCodeFragment extends MvpFragment<InputVerifyCodePresenter> implements InputVerifyCodeContract.View {
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
    @BindView(R.id.next_step)
    TextView mNextStep;
    private ClearSDdataDialog mDialog;

    private Disposable mdDisposable;
    private int loginType;
    private String mPhone ="";
    private String mInvitationCode;
    private WeixinInfo mWeixinInfo;
    private String mId;
    private ClearSDdataDialog mRegisterDialog;


    public static void srart(Activity activity, int loginType, String phone, String invite, WeixinInfo weixinInfo) {
        Bundle bundle = new Bundle();
        bundle.putInt(C.Extras.loginType, loginType);
        bundle.putString(C.Extras.PHONE, phone);
        bundle.putString(C.Extras.INVITATION_CODE, invite);
        if (weixinInfo != null) {
            bundle.putSerializable(C.Extras.OAUTH_WX, weixinInfo);
        }
        bundle.putBoolean(C.Extras.openFragment_isSysBar, true);
        OpenFragmentUtils.goToLoginSimpleFragment(activity, InputVerifyCodeFragment.class.getName(), bundle);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            loginType = bundle.getInt(C.Extras.loginType, 1);
            mPhone = bundle.getString(C.Extras.PHONE);
            mInvitationCode = bundle.getString(C.Extras.INVITATION_CODE);
            mWeixinInfo = (WeixinInfo) bundle.getSerializable(C.Extras.OAUTH_WX);
        }
        countDown();
        mTextSend.setText(getString(R.string.send_phone, mPhone));
    }

    @Override
    protected void initView(View view) {
        mTitle.setText(R.string.input_verifycode);
        mVerificationCodeEditText.setOnVerificationCodeChangedListener(new VerificationAction.OnVerificationCodeChangedListener() {
            @Override
            public void onVerCodeChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mVerificationCodeEditText.getText().toString().length() != 4) {
                    mNextStep.setEnabled(false);
                }
            }

            @Override
            public void onInputCompleted(CharSequence charSequence) {
                mNextStep.setEnabled(true);
            }
        });

    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_input_verify_code;
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
        mBtnSend.setTextColor(Color.parseColor("#999999"));
        mBtnSend.setBackgroundResource(R.drawable.bg_white_stroke_333333_30dp);
        countDown();
        LoadingView.showDialog(getActivity(), "请求中...");
        mPresenter.checkoutPhone(this, mPhone, loginType,"86");


    }

    /**
     * 注册/登录
     */
    @OnClick(R.id.next_step)
    public void nextStep() {

        //点击后置为不可点击状态
        mNextStep.setClickable(false);
        LoadingView.showDialog(getActivity(), "请求中...");
        ViewShowUtils.hideSoftInput(getActivity(), mVerificationCodeEditText);
        switch (loginType) {
            case C.sendCodeType.REGISTER:
                mPresenter.register(this, mPhone, mVerificationCodeEditText.getText().toString(), mInvitationCode,null);
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
        mTextSend.setText(getString(R.string.send_phone, mPhone));
    }

    @Override
    public void showFailureMessage(String errorMsg) {
        mBtnSend.setEnabled(true);
        mBtnSend.setTextColor(getResources().getColor(R.color.color_666666));
        mBtnSend.setBackground(getResources().getDrawable(R.drawable.bg_white_stroke_666666_30dp));
//        mBtnSend.setTextColor(Color.parseColor("#F0F0F0"));
//        mBtnSend.setBackgroundResource(R.drawable.bg_white_stroke_f0f0f0_30dp);
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
        mNextStep.setClickable(true);
    }

    @Override
    public void showRegisterFailure(String errCode) {
        mNextStep.setClickable(true);
        if (C.requestCode.B10051.equals(errCode)||C.requestCode.B10005.equals(errCode)) {
            openRegisterDialog();
        }
    }

    @Override
    public void showLoginFailure(String errCode) {

    }

    @Override
    public void showLoginData(UserInfo userInfo) {

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

    private void openDialog() {  //退出确认弹窗
        mDialog = new ClearSDdataDialog(getActivity(), R.style.dialog, "提示", "您已成功注册，请设置密码", new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View view, String text) {
                SetPasswordFragment.start(getActivity(), mId);
            }
        });
        mDialog.setOkTextAndColor(R.color.color_F32F19, "马上设置");
        mDialog.setCancelTextAndColor(R.color.color_1D88FE, "暂不设置");
        mDialog.setOnCancelListner(new ClearSDdataDialog.OnCancelListner() {
            @Override
            public void onClick(View view, String text) {
                ExclusiveWeChatFragment.start(getActivity(), mId);
//                ActivityLifeHelper.getInstance().finishActivity(MainActivity.class);
//                ActivityLifeHelper.getInstance().removeAllActivity(LoginSinglePaneActivity.class);
            }
        });
        mDialog.show();

        mDialog.setCancelable(false);
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
                        mBtnSend.setText(getString(R.string.count_down, seconds + ""));
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        //倒计时完毕置为可点击状态
                        mBtnSend.setEnabled(true);
                        mBtnSend.setText("重新发送");
                        mBtnSend.setTextColor(getResources().getColor(R.color.color_666666));
                        mBtnSend.setBackground(getResources().getDrawable(R.drawable.bg_white_stroke_666666_30dp));
                    }
                })
                .subscribe();
    }

    @Override
    public void loginError(String code) {

        mNextStep.setClickable(true);
    }

    @Override
    public void loginSucceed(String userJson) {

        mNextStep.setClickable(true);
        ViewShowUtils.showLongToast(getActivity(), "登录成功");
        ActivityLifeHelper.getInstance().removeAllActivity(LoginSinglePaneActivity.class);
//     ActivityLifeHelper.getInstance().finishActivity(LoginSinglePaneActivity.class);
    }

    @Override
    public void sendCodeSuccess(String data) {
        mNextStep.setClickable(true);
        ViewShowUtils.showLongToast(getActivity(), "发送验证码成功");
    }

    @Override
    public void sendCodeFail() {

    }
}
