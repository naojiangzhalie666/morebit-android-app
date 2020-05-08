package com.zjzy.morebit.login.ui;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.Module.common.Dialog.InvateBindDialog;
import com.zjzy.morebit.R;
import com.zjzy.morebit.login.contract.LoginEditInviteContract;
import com.zjzy.morebit.login.presenter.LoginEditInvitePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.AreaCodeBean;
import com.zjzy.morebit.pojo.WeixinInfo;
import com.zjzy.morebit.pojo.login.InviteUserInfoBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.AreaCodeUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.functions.Consumer;

/**
 * Created by fengrs on 2018/8/8
 * 备注: 密码登录界面
 */

public class LoginEditInviteFragment extends MvpFragment<LoginEditInvitePresenter> implements LoginEditInviteContract.View {
    @BindView(R.id.edtInvite)
    EditText edtInvite;
    @BindView(R.id.tv_invite_name)
    TextView tv_invite_name;
    @BindView(R.id.tv_next)
    TextView tv_next;
    @BindView(R.id.tv_intro)
    TextView tv_intro;
    @BindView(R.id.iv_invite_head)
    ImageView iv_invite_head;
    @BindView(R.id.rl_invite)
    RelativeLayout rl_invite;
    @BindView(R.id.errorTv)
    TextView errorTv;
    private String mEditInviteText = "";
    private String mEditInvite6Text = "";
    private InviteUserInfoBean mInviteUserInfoBean;
    private boolean mIsAnimInTop = true; //动画是否在上面
    private int mAnimHeight;
    private String mPhone;
    private WeixinInfo mWeixinInfo;
    private int mLoginType;
    private Handler mHandler;
    public static int REQ_QR_CODE = 1015;
    private String areaCode;
    private AreaCodeBean mAreaCode;
    private static final int MSG_SEARCH = 1;
    private RelativeLayout rl;//返回键
    private TextView ll_userAgreement, privateProtocol;
    private TextView tv_bind;


    private Handler mSearchHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //搜索请求
            mPresenter.getInviteUserInfo(LoginEditInviteFragment.this, mEditInviteText);
        }
    };


    public static void start(Activity activity, String phone, int loginType, AreaCodeBean areaCodeBean) {
        Bundle bundle = new Bundle();
        bundle.putString(C.Extras.PHONE, phone);
        bundle.putSerializable(C.Extras.AREACODE, areaCodeBean);
        bundle.putBoolean(C.Extras.openFragment_isSysBar, true);
        bundle.putInt(C.Extras.loginType, loginType);
        OpenFragmentUtils.goToLoginSimpleFragment(activity, LoginEditInviteFragment.class.getName(), bundle);
    }

    public static void start(Activity activity, String phone, WeixinInfo weixinInfo, AreaCodeBean areaCodeBean) {
        Bundle bundle = new Bundle();
        bundle.putString(C.Extras.PHONE, phone);
        bundle.putBoolean(C.Extras.openFragment_isSysBar, true);
        if (weixinInfo != null) {
            bundle.putInt(C.Extras.loginType, C.sendCodeType.WEIXINREGISTER);
            bundle.putSerializable(C.Extras.OAUTH_WX, weixinInfo);
        } else {
            bundle.putInt(C.Extras.loginType, C.sendCodeType.REGISTER);

        }
        bundle.putSerializable(C.Extras.AREACODE, areaCodeBean);
        OpenFragmentUtils.goToLoginSimpleFragment(activity, LoginEditInviteFragment.class.getName(), bundle);
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
    protected int getViewLayout() {
        return R.layout.fragment_login_edit_invite;
    }

    @Override
    protected void initData() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mAnimHeight = getResources().getDimensionPixelSize(R.dimen.sp_24/*login_edit_invite_anim_height*/);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPhone = bundle.getString(C.Extras.PHONE, "");
            mAreaCode = (AreaCodeBean) bundle.getSerializable(C.Extras.AREACODE);
            mWeixinInfo = (WeixinInfo) bundle.getSerializable(C.Extras.OAUTH_WX);
            mLoginType = bundle.getInt(C.Extras.loginType);
        }

        if (mLoginType == C.sendCodeType.WEIXINREGISTER || mLoginType == C.sendCodeType.REGISTER) {
            if (null == mAreaCode) {
                mAreaCode = AreaCodeUtil.getDefaultCode();
            }
        }

        if (mLoginType == C.sendCodeType.WEIXINREGISTER){
            tv_bind.setVisibility(View.VISIBLE);
        }

        areaCode = mAreaCode.getAreaCode();
    }

    @Override
    protected void initView(View view) {
//        String coyeTextIsInvite = LoginUtil.getCoyeTextIsInvite(getActivity());
//        if (!TextUtils.isEmpty(coyeTextIsInvite)) {
//            edtInvite.setText(coyeTextIsInvite);
//        }

        rl = view.findViewById(R.id.rl);
        tv_bind=view.findViewById(R.id.tv_bind);//已有账号立即绑定
        privateProtocol = view.findViewById(R.id.privateProtocol);
        ll_userAgreement = view.findViewById(R.id.ll_userAgreement);
        edtInvite.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        edtInvite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(edtInvite.getText().toString().length()==6||edtInvite.getText().toString().length()==11){
                    tv_next.setEnabled(true);
                    tv_next.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_next.setBackgroundResource(R.mipmap.phone_login_next);
                }else{
                    tv_next.setEnabled(false);
                    tv_next.setTextColor(Color.parseColor("#999999"));
                    tv_next.setBackgroundResource(R.mipmap.next_login_hiu);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }


    @OnTextChanged(value = R.id.edtInvite, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterTextChanged(Editable s) {
        mInviteUserInfoBean = null;
        mEditInviteText = s.toString();


        if (!TextUtils.isEmpty(areaCode) && !"86".equals(areaCode) && mEditInviteText.length() >= 6) {
            if (mEditInviteText.length() >= C.PHONE.MIN_LENGTH && mEditInviteText.length() <= C.PHONE.MAX_LENGTH) {
                if (mSearchHandler.hasMessages(MSG_SEARCH)) {
                    mSearchHandler.removeMessages(MSG_SEARCH);
                }
                mSearchHandler.sendEmptyMessageDelayed(MSG_SEARCH, 1000);

            }
        } else {
            errorTv.setText("");
            errorTv.setVisibility(View.GONE);
            tv_next.setEnabled(false);
            // tv_next.setTextColor(Color.parseColor("#333333"));
            if (!mIsAnimInTop) {
                mIsAnimInTop = true;
                hideAnim(rl_invite);
                TranslateTopAnim(tv_next);
            }
        }


        if (!TextUtils.isEmpty(areaCode) && "86".equals(areaCode)) {
            if (mEditInviteText.length() == 6 || mEditInviteText.length() == 11) {
                if (!AppUtil.isFastClick(400)) {
                    if (mEditInviteText.length() == 6) {
                        errorTv.setText("");
                        errorTv.setVisibility(View.GONE);
                        mEditInvite6Text = mEditInviteText;
                        if (mHandler == null) {
                            mHandler = new Handler();
                        }
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mEditInvite6Text.equals(mEditInviteText)) {
                                    mPresenter.getInviteUserInfo(LoginEditInviteFragment.this, mEditInviteText);
                                }
                            }
                        }, 1000);
                    }

                    if (mEditInviteText.length() == 11 && LoginUtil.isMobile(mEditInviteText)) {
                        mPresenter.getInviteUserInfo(this, mEditInviteText);

                    }
                }
            } else {
                tv_next.setEnabled(false);
                //   tv_next.setTextColor(Color.parseColor("#333333"));
                if (!mIsAnimInTop) {
                    mIsAnimInTop = true;
                    hideAnim(rl_invite);
                    TranslateTopAnim(tv_next);
                }
            }
        }

    }


    @OnClick({R.id.tv_next, R.id.iv_req_qr, R.id.privateProtocol, R.id.ll_userAgreement, R.id.rl,R.id.tv_bind})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.iv_req_qr:
                // 二维码扫码
                final ArrayList<Boolean> arrayList = new ArrayList<>();
                RxPermissions rxPermission = new RxPermissions(getActivity());
                rxPermission
                        .requestEach(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                if (permission.granted) {
                                    arrayList.add(true);
                                    // 用户已经同意该权限
                                    if (!arrayList.contains(false) && arrayList.size() == 3) {
                                        Intent intent = new Intent(getActivity(), ScanQRCodeActivity.class);
                                        startActivityForResult(intent, REQ_QR_CODE);
                                    }
                                } else {
                                    arrayList.add(false);
                                    // 用户拒绝了该权限，并且选中『不再询问』
                                    if (arrayList.size() == 3) {
                                        if (arrayList.get(0)) { // 相机已有
                                            ViewShowUtils.showShortToast(getActivity(), R.string.call_premission_read_sd);
                                        } else {
                                            ViewShowUtils.showShortToast(getActivity(), R.string.call_premission_camera);
                                        }
                                        AppUtil.goSetting(getActivity());

                                    }else {
                                        ViewShowUtils.showShortToast(getActivity(), "请开启相应权限");
                                        AppUtil.goSetting(getActivity());
                                    }
                                }
                            }
                        });

                break;
            case R.id.tv_next:
                final InvateBindDialog dialog = new InvateBindDialog(getActivity(), tv_invite_name.getText().toString()+"-"+"多点优选"+"-"+edtInvite.getText().toString(), "", "", "", new InvateBindDialog.OnOkListener() {
                    @Override
                    public void onClick(View view) {
                        if (AppUtil.isFastClick(500)) {
                            return;
                        }
                        if (mInviteUserInfoBean == null) {
                            ViewShowUtils.showShortToast(getActivity(), "信息有误哦,请稍后重试");
                            return;
                        }
                        if (mLoginType == C.sendCodeType.WEIXINREGISTER){
                            Log.e("mEditInvitefragment",edtInvite.getText().toString());
                            LoginMainFragment.start(getActivity(),1,mWeixinInfo,edtInvite.getText().toString());
                            return;
                        }
                        LoginVerifyCodeFragment.srart(getActivity(), mLoginType, mPhone, mEditInviteText, mWeixinInfo,mAreaCode);
                      //  LoginEditPhoneFragment.start(getActivity(), mLoginType, mEditInviteText, mPhone, mWeixinInfo, mAreaCode);
                    }
                });
                dialog.show();
                dialog.setmCancelListener(new InvateBindDialog.OnCancelListner() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                break;
            case R.id.areaCodeBtn:
                AreaCodeActivity.actionStart(getActivity());
                break;
            case R.id.privateProtocol:
                LoginUtil.getPrivateProtocol((RxAppCompatActivity) getActivity());
                break;
            case R.id.ll_userAgreement:
                LoginUtil.getUserProtocol((RxAppCompatActivity) getActivity());
                break;
            case R.id.rl:
                getActivity().finish();
                break;
            case R.id.tv_bind:
                LoginVerifyCodeFragment.srart(getActivity(), C.sendCodeType.BINDWEIXIN, "", "", mWeixinInfo,mAreaCode);
                break;

        }
    }


    @Override
    public void loginError(String code) {
        tv_next.setEnabled(false);
        //  tv_next.setTextColor(Color.parseColor("#333333"));
        if (!mIsAnimInTop) {
            hideAnim(rl_invite);
            TranslateTopAnim(tv_next);
            mIsAnimInTop = true;
        }
        mInviteUserInfoBean = null;

    }

    @Override
    public void loginSucceed(String s) {
    }

    @Override
    public void sendCodeSuccess(String data) {

    }

    @Override
    public void sendCodeFail() {

    }

    @Override
    public void setInviteUserInfo(InviteUserInfoBean data) {
        ViewShowUtils.hideSoftInput(LoginEditInviteFragment.this.getActivity(), edtInvite);
        errorTv.setText("");
        errorTv.setVisibility(View.GONE);
//        iv_invite_head.setVisibility(View.VISIBLE);
//        tv_invite_name.setVisibility(View.VISIBLE);
//        rl_invite.setVisibility(View.VISIBLE);
        if (mIsAnimInTop) {
            mIsAnimInTop = false;
            showAnim(rl_invite);
            TranslateButtomAnim(tv_next);
        }
        this.mInviteUserInfoBean = data;
        tv_next.setEnabled(true);
        tv_next.setTextColor(Color.parseColor("#FFFFFF"));
        if (!TextUtils.isEmpty(data.getUserhead())) {
            LoadImgUtils.setImgCircle(getActivity(), iv_invite_head, data.getUserhead());
        } else {
            iv_invite_head.setImageResource(R.drawable.image_login_head_icon);
        }
        tv_intro.setText(data.getIntro());
        if (TextUtils.isEmpty(data.getNickname())) {
            tv_invite_name.setText(data.getPhone());
        } else {
            tv_invite_name.setText(data.getNickname());
        }
    }

    @Override
    public void getInviteInfoFail(String msg) {
        errorTv.setVisibility(View.VISIBLE);
        errorTv.setText(msg);
        if (!mIsAnimInTop) {
            hideAnim(rl_invite);
            TranslateTopAnim(tv_next);
            mIsAnimInTop = true;
        }
        mInviteUserInfoBean = null;
    }

    public void hideAnim(View view) {
        /**
         * @param fromAlpha 开始的透明度，取值是0.0f~1.0f，0.0f表示完全透明， 1.0f表示和原来一样
         * @param toAlpha 结束的透明度，同上
         */
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
        //设置动画持续时长
        alphaAnimation.setDuration(500);
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        alphaAnimation.setFillAfter(true);
        //设置动画结束之后的状态是否是动画开始时的状态，true，表示是保持动画开始时的状态
//        alphaAnimation.setFillBefore(true);
        //设置动画的重复模式：反转REVERSE和重新开始RESTART
//        alphaAnimation.setRepeatMode(AlphaAnimation.REVERSE);
        //设置动画播放次数
        alphaAnimation.setRepeatCount(0);
        //开始动画
        view.startAnimation(alphaAnimation);
//        //清除动画
//        view.clearAnimation();
//        //同样cancel()也能取消掉动画
//        alphaAnimation.cancel();
    }


    public void showAnim(View view) {
        /**
         * @param fromAlpha 开始的透明度，取值是0.0f~1.0f，0.0f表示完全透明， 1.0f表示和原来一样
         * @param toAlpha 结束的透明度，同上
         */
        /**
         * @param fromAlpha 开始的透明度，取值是0.0f~1.0f，0.0f表示完全透明， 1.0f表示和原来一样
         * @param toAlpha 结束的透明度，同上
         */
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1.0f);
        //设置动画持续时长
        alphaAnimation.setDuration(500);
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        alphaAnimation.setFillAfter(true);
        //设置动画结束之后的状态是否是动画开始时的状态，true，表示是保持动画开始时的状态
//        alphaAnimation.setFillBefore(true);
        //设置动画的重复模式：反转REVERSE和重新开始RESTART
//        alphaAnimation.setRepeatMode(AlphaAnimation.REVERSE);
        //设置动画播放次数
        alphaAnimation.setRepeatCount(0);
        //开始动画

        view.startAnimation(alphaAnimation);
//        //清除动画
//        view.clearAnimation();
//        //同样cancel()也能取消掉动画
//        alphaAnimation.cancel();
    }

    public void TranslateTopAnim(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", mAnimHeight, 0);
        animator.setDuration(500);
        animator.setRepeatCount(0);
        animator.start();
    }

    public void TranslateButtomAnim(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0, mAnimHeight);
        animator.setDuration(500);
        animator.setRepeatCount(0);
        animator.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == REQ_QR_CODE && data != null) {
            String ewmText = data.getStringExtra(C.Extras.ewmText);
            if (!TextUtils.isEmpty(ewmText)) {
                edtInvite.setText(ewmText);
            }
        }
    }


}
