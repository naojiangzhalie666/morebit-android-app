package com.zjzy.morebit.login.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.tencent.mm.opensdk.utils.Log;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.Module.common.Dialog.WxResginDialog;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.login.contract.LoginMainContract;
import com.zjzy.morebit.login.presenter.LoginMainPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.AreaCodeBean;
import com.zjzy.morebit.pojo.WeixinInfo;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.AreaCodeUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LogUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.helper.ActivityLifeHelper;
import com.zjzy.morebit.utils.netWork.ErrorCodeUtlis;

/**
 * 登录默认页面
 */
public class LoginFragment extends MvpFragment<LoginMainPresenter> implements LoginMainContract.View, View.OnClickListener {

    private RelativeLayout rl;//返回键
    private LinearLayout wx_ll,phone_ll;
    private TextView ll_userAgreement,privateProtocol;
    private boolean wxLoginFlag = false;
    private AreaCodeBean mAreaCode;
    private WeixinInfo mWeixinInfo;

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static void start(Activity activity) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(C.Extras.openFragment_isSysBar, true);
        OpenFragmentUtils.goToLoginSimpleFragment(activity,LoginFragment.class.getName(),bundle);
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
        rl = view.findViewById(R.id.rl);
        rl.setOnClickListener(this);
        phone_ll=view.findViewById(R.id.phone_ll);
        phone_ll.setOnClickListener(this);
        wx_ll=view.findViewById(R.id.wx_ll);
        wx_ll.setOnClickListener(this);
        privateProtocol=view.findViewById(R.id.privateProtocol);
        privateProtocol.setOnClickListener(this);
        ll_userAgreement=view.findViewById(R.id.ll_userAgreement);
        ll_userAgreement.setOnClickListener(this);
        mAreaCode = AreaCodeUtil.getDefaultCode();
    }


    @Override
    protected int getViewLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl:
                getActivity().finish();
                break;
            case R.id.phone_ll:
                LoginMainFragment.start(getActivity(),"",2,mWeixinInfo,"");
                break;
            case R.id.wx_ll:
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
            case R.id.privateProtocol:
                LoginUtil.getPrivateProtocol((RxAppCompatActivity) getActivity());
                break;
            case R.id.ll_userAgreement:
                LoginUtil.getUserProtocol((RxAppCompatActivity) getActivity());
                break;
        }
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

    @Override
    public void goToWx() {

    }

    /**
     * 微信未注册
     * @param code
     */
    @Override
    public void loginError(String code) {
        WeixinInfo weixinInfo = mPresenter.getWeixinInfo();
        if (ErrorCodeUtlis.isNuRegister(code)) {
            //用户不存在  跳转到注册页
//            WxResginDialog dialog = new WxResginDialog(getActivity(), "", "当前微信未绑定多点优选账号\n立即注册绑定，畅享百万优惠", "", new WxResginDialog.OnOkListener() {
//                @Override
//                public void onClick(View view) {
//
//                   // LoginMainFragment.start(getActivity());
//                }
//            });
//            dialog.show();
            ViewShowUtils.showShortToast(getActivity(), "当前微信未绑定多点优选账号\n立即注册绑定，畅享百万优惠");
            LoginEditInviteFragment.start(getActivity(), "", weixinInfo, mAreaCode);


        }else if (C.requestCode.B10357.equals(code)){//扫码来的用户，手机号为空
            LoginMainFragment.start(getActivity(),"",1,weixinInfo,"");
        }
    }

    @Override
    public void loginSucceed(String s) {
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
    public void onResume() {
        super.onResume();
        if(wxLoginFlag){
            wxLoginFlag = false;
            LoadingView.dismissDialog();
        }
    }
}
