package com.jf.my.login.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jf.my.LocalData.UserLocalData;
import com.jf.my.Module.common.Utils.LoadingView;
import com.jf.my.R;
import com.jf.my.login.contract.SetPasswordContract;
import com.jf.my.login.presenter.SetpasswordPresenter;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.mvp.base.frame.MvpFragment;
import com.jf.my.utils.AppUtil;
import com.jf.my.utils.C;
import com.jf.my.utils.MyLog;
import com.jf.my.utils.OpenFragmentUtils;
import com.jf.my.utils.ViewShowUtils;
import com.jf.my.utils.fire.BuglyUtils;
import com.jf.my.utils.helper.ActivityLifeHelper;
import com.jf.my.view.ClearEditText;

import butterknife.BindView;

/**
 * Created by YangBoTian on 2018/5/29 22:00
 * 注册设置登录密码页
 */

public class SetPasswordFragment extends MvpFragment<SetpasswordPresenter> implements SetPasswordContract.View {

    @BindView(R.id.edt_password)
    ClearEditText mEdtPassword;
    @BindView(R.id.edt_password2)
    ClearEditText mEdtPassword2;
    @BindView(R.id.register_set_password)
    TextView mSetPassword;
    @BindView(R.id.showPwIv)
    ImageView showPwIv;
    @BindView(R.id.errorPw)
    TextView errorPw;
    @BindView(R.id.errorRePw)
    TextView errorRePw;
    @BindView(R.id.lh_head)
    LoginHeadView loginHeadView;

    private String mId = "";
    private boolean isOpenEye = false;
    private boolean isSetting;   //是否正在设置密码中


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (null != activity) {
            ((LoginSinglePaneActivity) activity).setSwipeBackEnable(false);
        }
    }

    public static void start(Activity activity, String id) {
        Bundle extras = new Bundle();
        extras.putString(C.Extras.ID, id);
        extras.putBoolean(C.Extras.isBackPressed, false);
        OpenFragmentUtils.goToLoginSimpleFragment(activity, SetPasswordFragment.class.getName(), extras);
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }


    @Override
    protected void initData() {

        mId = getArguments().getString(C.Extras.ID);
    }

    @Override
    protected void initView(View view) {
        mEdtPassword.setFilters(new InputFilter[]{AppUtil.getTrimInputFilter()});
        mEdtPassword2.setFilters(new InputFilter[]{AppUtil.getTrimInputFilter()});
        mSetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLog.i("test", "mRegister");

//           ExclusiveWeChatFragment.start(SetPasswordFragment.this,mId);
                ViewShowUtils.hideSoftInput(getActivity(), mEdtPassword2);
                setPassword();
            }
        });
        mEdtPassword.addTextChangedListener(mPwTextWatcher);
        mEdtPassword2.addTextChangedListener(mRePwTextWatcher);
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

        mEdtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //失去焦点
                    checkPassword(true);
                } else {

                }
            }
        });
        mEdtPassword2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //失去焦点
                    checkRePassword(true);
                } else {

                }
            }
        });
        loginHeadView.setOnBackListener(new LoginHeadView.OnBackListener() {
            @Override
            public void onClick() {
                ExclusiveWeChatFragment.start(getActivity(), mId);
            }
        });
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_set_password;
    }


    public void setPassword() {
        if (!checkPassword(true) || !checkRePassword(true)) {
            return;
        }
        LoadingView.showDialog(getActivity(), "请求中...");
        mPresenter.setPassword(this, mId, mEdtPassword.getText().toString().trim(), mEdtPassword2.getText().toString().trim());
    }


    private boolean checkPassword(boolean showError) {
        if (null != mEdtPassword && TextUtils.isEmpty(mEdtPassword.getText().toString())) {
            BuglyUtils.e("SetPasswordFragment", "请输入密码");
            if (showError) errorPw.setText("请输入密码");
            return false;
        }
        if (null != mEdtPassword && mEdtPassword.getText().toString().trim().length() < 8) {
            BuglyUtils.e("SetPasswordFragment", "密码至少输入8位");
            if (showError) errorPw.setText(getString(R.string.edit_pwd_error));
            return false;
        }

        if(null != mEdtPassword){
            boolean correctPwd = AppUtil.isCorrectPwd(getActivity(), mEdtPassword.getText().toString());
            if (!correctPwd) {
                if (showError) errorPw.setText(getString(R.string.edit_pwd_error));
                return false;
            }
        }


        if (null != errorPw) errorPw.setText("");
        return true;
    }

    private boolean checkRePassword(boolean showError) {
        if(null == mEdtPassword || null == mEdtPassword2){
            return false;
        }
        String pwd = mEdtPassword.getText().toString().trim();
        if (null != mEdtPassword2 && TextUtils.isEmpty(mEdtPassword2.getText().toString().trim())) {
            BuglyUtils.e("SetPasswordFragment", "请输入确认密码");
            //if(showError) errorRePw.setText("请输入确认密码");
            return false;
        }

        if (null != mEdtPassword2 && mEdtPassword2.getText().toString().trim().length() < 8) {
            BuglyUtils.e("SetPasswordFragment", "密码至少输入8位");
            return false;
        }


        boolean correctPwd = AppUtil.isCorrectPwd(getActivity(), pwd);
        if (!correctPwd) {
            if (showError) errorRePw.setText(getString(R.string.edit_pwd_error));
            return false;
        }

        if (null != mEdtPassword && mEdtPassword2 != null && !mEdtPassword.getText().toString().equals(mEdtPassword2.getText().toString().trim())) {
            BuglyUtils.e("SetPasswordFragment", "两次输入的密码不一致");
            if (showError) errorRePw.setText("两次输入的密码不一致");
            return false;
        }
        if (null != errorRePw) errorRePw.setText("");
        return true;
    }


    private TextWatcher mPwTextWatcher = new TextWatcher() {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }


        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        //一般我们都是在这个里面进行我们文本框的输入的判断，上面两个方法用到的很少
        @Override
        public void afterTextChanged(Editable s) {
            checkPassword(false);
        }
    };

    private TextWatcher mRePwTextWatcher = new TextWatcher() {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }


        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        //一般我们都是在这个里面进行我们文本框的输入的判断，上面两个方法用到的很少
        @Override
        public void afterTextChanged(Editable s) {
            if (checkPassword(false) && checkRePassword(true)) {
                mSetPassword.setEnabled(true);
            } else {
                mSetPassword.setEnabled(false);
            }
        }
    };


    public void gotoMain() {
//        ActivityLifeHelper.getInstance().finishActivity(MainActivity.class);
        ActivityLifeHelper.getInstance().removeAllActivity(LoginSinglePaneActivity.class);
    }

    @Override
    public void showFinally() {
        LoadingView.dismissDialog();
    }

    @Override
    public void showData(String data) {
        ViewShowUtils.showShortToast(getActivity(), data);
        ExclusiveWeChatFragment.start(getActivity(), mId);
    }

    @Override
    public void showFailureMessage(String errorMsg) {
        BuglyUtils.putErrorToBugly(" Phone  " + UserLocalData.mPhone);
    }


}
