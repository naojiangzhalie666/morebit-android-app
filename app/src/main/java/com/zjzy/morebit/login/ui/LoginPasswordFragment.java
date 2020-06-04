package com.zjzy.morebit.login.ui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.uuzuche.lib_zxing.DisplayUtil;
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
    @BindView(R.id.rl_root)
    RelativeLayout rl_root;
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
    private RelativeLayout rl;//返回键
    private TextView next_login;
    private NestedScrollView netscroll;
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
        mPhone = getArguments().getString(C.Extras.PHONE, "");
        mAreaCode = (AreaCodeBean) getArguments().getSerializable(C.Extras.COUNTRY);
        mEdtPhoneText = mPhone.trim();
        rl=view.findViewById(R.id.rl);




        rl_root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){

            //当键盘弹出隐藏的时候会 调用此方法。
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //获取当前界面可视部分
              getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight =  getActivity().getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;
                if (heightDifference>0){
                    rl_root.scrollTo(0,100);
                }else{
                    rl_root.scrollTo(0,0);
                }
                Log.d("Keyboard Size", "Size: " + heightDifference);
            }

        });
        next_login=view.findViewById(R.id.next_login);

        if (!TextUtils.isEmpty(mPhone)) {
            phoneTv.setText(mPhone);
        }
        //密码不可见
        edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(edt_password.getText().toString().length()>7){
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
        rl_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMethodManager(rl_root);
            }
        });

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


    @OnClick({R.id.login,R.id.password_login,R.id.forget_password,R.id.rl,R.id.next_login})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.password_login:
                LoginVerifyCodeFragment.srart(getActivity(), C.sendCodeType.LOGIN, mEdtPhoneText, mInvite, mWeixinInfo,mAreaCode);
                break;
            case R.id.forget_password:
                ModifyPasswordActivity.start(getActivity(), ModifyPasswordActivity.FIND_PASSWORD, mPhone.trim(),mAreaCode);
                break;
            case R.id.login:
//                if (AppUtil.isFastClick(1000)) {
//                    return;
//                }
//                if (!checkInput()) {
//                    return;
//                }
//                mPresenter.Passwordlogin(this,mEdtPhoneText.toString(),mEdtPasswordText.toString(),mAreaCode.getAreaCode());
                break;
            case R.id.rl:
                getActivity().finish();
                isMethodManager(rl);
                break;
            case R.id.next_login:
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
            ToastUtils.showLong("密码不正确");
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

    @Override
    public void goToRegister() {

    }

    @Override
    public void getLocalWx(WeixinInfo weixinInfo) {

    }

    @Override
    public void getWxError(String code) {

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
