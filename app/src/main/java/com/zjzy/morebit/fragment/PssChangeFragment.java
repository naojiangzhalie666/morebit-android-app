package com.zjzy.morebit.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.zjzy.morebit.Activity.ModifyPasswordActivity;
import com.zjzy.morebit.Activity.SettingActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Dialog.PasswordSumbitDialog;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.request.RequestAuthCodeBean;
import com.zjzy.morebit.pojo.request.RequestSetAlipayBean;
import com.zjzy.morebit.pojo.request.RequestUpPasswordBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;
import com.zjzy.morebit.utils.encrypt.MD5Utils;
import com.zjzy.morebit.utils.helper.ActivityLifeHelper;
import com.zjzy.morebit.utils.netWork.ErrorCodeUtlis;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.functions.Action;


/**
 * 修改密码-界面
 */
public class PssChangeFragment extends BaseFragment implements View.OnClickListener {

    private EditText zhifubaoagin_et, zhifubao_et, edt_yanzhengma;
    private TextView tv_yanzhengma, phone;
    private long first = 0;
    private static final int SECONDS = 60; // 秒数
    private UserInfo mUsInfo;
    private int phoneLength = 12; //默认是中国11位

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_changepss, container, false);
        inview(view);
        mUsInfo = UserLocalData.getUser(getActivity());
        initData();
        return view;
    }

    public void inview(View view) {
        ActivityStyleUtil.initSystemBar(getActivity(), R.color.white); //设置标题栏颜色值

        zhifubaoagin_et = (EditText) view.findViewById(R.id.zhifubaoagin_et);
        zhifubao_et = (EditText) view.findViewById(R.id.zhifubao_et);
        phone = (TextView) view.findViewById(R.id.phone);
        tv_yanzhengma = (TextView) view.findViewById(R.id.tv_yanzhengma);
        edt_yanzhengma = (EditText) view.findViewById(R.id.edt_yanzhengma);
        tv_yanzhengma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtil.isFastClick(500)) {
                    return;
                }

                if ("获取验证码".equals(tv_yanzhengma.getText().toString().trim()) || "重新获取".equals(tv_yanzhengma.getText().toString().trim())) {
                    getPublicTimeline();
                }
            }
        });
        view.findViewById(R.id.submit).setOnClickListener(this);
    }

    /**
     * 初始化界面数据
     */
    private void initData() {
        if (mUsInfo == null) {
            return;
        }
        phone.setText(LoginUtil.hideNumber(mUsInfo.getPhone()));



    }

    class Counter extends CountDownTimer {

        public Counter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            try {
                tv_yanzhengma.setEnabled(true);
                tv_yanzhengma.setText("重新获取");
                tv_yanzhengma.setBackgroundResource(R.drawable.bg_stroke_f05557_15dp);
                tv_yanzhengma.setTextColor(getResources().getColor(R.color.color_F05557));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 获取当前时间总秒数
            first = millisUntilFinished / 1000;
            if (first <= SECONDS) { // 小于一分钟 只显示秒
                tv_yanzhengma.setText((first < 10 ? "0" + first : first) + " 秒后再试");
            }

        }

    }

    /**
     * 获取短信
     */
    public void getPublicTimeline() {
        UserInfo usInfo = UserLocalData.getUser(getActivity());
        if (usInfo == null || TextUtils.isEmpty(usInfo.getPhone())) {
            return;
        }
        LoadingView.showDialog(getActivity(), "请求中...");

        RequestAuthCodeBean requestBean = new RequestAuthCodeBean();
        requestBean.setPhone(usInfo.getPhone());
        requestBean.setType(C.sendCodeType.REVAMPPWD);

        RxHttp.getInstance().getUsersService()
//                .sendAuthCode(usInfo.getPhone(), C.sendCodeType.REVAMPAIPAY )
                .sendAuthCode(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<String>() {

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        tv_yanzhengma.setEnabled(true);
                        tv_yanzhengma.setText("获取验证码");
                    }

                    @Override
                    protected void onDataNull() {
                        onSuccess("");
                    }

                    /**
                     * 成功回调
                     *
                     * @param data 结果
                     */
                    @Override
                    protected void onSuccess(String data) {
                        tv_yanzhengma.setEnabled(false);
                        Counter counter = new Counter(60 * 1000, 1000); // 第一个参数是倒计时时间，后者为计时间隔，单位毫秒，这里是倒计时
                        counter.start();
                        ViewShowUtils.showLongToast(getActivity(), "验证码发送成功");

                    }
                });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit: //提交数据
                modifyPassword();
                break;
            default:
                break;

        }
    }

    private void modifyPassword() {
        if (!checkCode(true) || !checkPassword(true) || !checkRePassword(true)) {
            return;
        }

        LoadingView.showDialog(getActivity(), "请求中...");

        String mphone = UserLocalData.getUser(getActivity()).getPhone();
        String password = zhifubaoagin_et.getText().toString();
        String verCode = edt_yanzhengma.getText().toString();
        String pwdMD5 = MD5Utils.getPsdMD5(password);


        RequestUpPasswordBean requestBean = new RequestUpPasswordBean();
        requestBean.setPhone(mphone);
        requestBean.setPassword(pwdMD5);
        requestBean.setVerCode(verCode);
        String sign = EncryptUtlis.getSign2(requestBean);
        requestBean.setSign(sign);


        RxHttp.getInstance().getUsersService()
                //                .modifyPassword( phone, pwdMD5, verCode,sign)
                .modifyPassword(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(this.<BaseResponse<String>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onDataNull() {
                        onSuccess("");
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        if (ErrorCodeUtlis.isLogin(errCode)) {

                        }
                    }

                    @Override
                    protected void onSuccess(String data) {

                            ViewShowUtils.showShortToast(getActivity(), "修改成功");
                            getActivity().finish();
                        }
                });
    }



    private boolean checkCode(boolean showError) {
        if (null != edt_yanzhengma && !TextUtils.isEmpty(edt_yanzhengma.getText().toString()) && edt_yanzhengma.getText().toString().length() < 4) {
            ToastUtils.showShort("请输入4位验证码");
            return false;
        }

        if (null != edt_yanzhengma && TextUtils.isEmpty(edt_yanzhengma.getText().toString())) {
            ToastUtils.showShort("请输入验证码");
            return false;
        }
        return true;
    }


    private boolean checkPassword(boolean showError) {
        String errorText = "";
        boolean checkFlag = true;
        String reg = "[\\pP\\p{Punct}]";

        if (null != zhifubaoagin_et && TextUtils.isEmpty(zhifubaoagin_et.getText().toString())) {
            ToastUtils.showShort("请输入密码");
            checkFlag = false;
        }
        if (null != zhifubaoagin_et && zhifubaoagin_et.getText().toString().trim().length() < 8 || zhifubaoagin_et.getText().toString().trim().length() > 16 && null != zhifubaoagin_et) {
            ToastUtils.showShort("密码必须是8-16位的数字、字母组合");
            checkFlag = false;
        }
        if (!isLetterDigit(zhifubaoagin_et.getText().toString())/*||mEdtPassword.getText().toString().matches(reg)*/) {
            ToastUtils.showShort("密码必须是8-16位的数字、字母组合");
            checkFlag = false;

        }
        return checkFlag;
    }

    private boolean checkRePassword(boolean showError) {
        String errorText = "";
        boolean checkFlag = true;
        String pwd = zhifubaoagin_et.getText().toString().trim();
        if (!checkPassword(false)) {
            return false;
        }

        if (null != zhifubao_et && TextUtils.isEmpty(zhifubao_et.getText().toString().trim())) {
            ToastUtils.showShort("请输入确认密码");
            checkFlag = false;
        }

        boolean correctPwd = AppUtil.isCorrectPwd(getActivity(), pwd);
        if (!correctPwd) {
            ToastUtils.showShort("密码必须是8-16位的数字、字母组合");
            return correctPwd;
        } else {
            checkFlag = correctPwd;
        }
        if (null != zhifubaoagin_et && zhifubao_et != null && !zhifubaoagin_et.getText().toString().equals(zhifubao_et.getText().toString().trim())) {
            ToastUtils.showShort("两次密码不一致");
            checkFlag = false;
        }
        return checkFlag;
    }

    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }
            if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }

        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;

    }

}
