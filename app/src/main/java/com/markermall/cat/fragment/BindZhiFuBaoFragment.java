package com.markermall.cat.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.markermall.cat.LocalData.UserLocalData;
import com.markermall.cat.Module.common.Fragment.BaseFragment;
import com.markermall.cat.Module.common.Utils.LoadingView;
import com.markermall.cat.R;
import com.markermall.cat.contact.EventBusAction;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.MessageEvent;
import com.markermall.cat.pojo.UserInfo;
import com.markermall.cat.pojo.request.RequestAuthCodeBean;
import com.markermall.cat.pojo.request.RequestSetAlipayBean;
import com.markermall.cat.utils.ActivityStyleUtil;
import com.markermall.cat.utils.AppUtil;
import com.markermall.cat.utils.C;
import com.markermall.cat.utils.LoginUtil;
import com.markermall.cat.utils.ViewShowUtils;
import com.markermall.cat.utils.encrypt.EncryptUtlis;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.functions.Action;


/**
 * 绑定支付宝-界面
 */
public class BindZhiFuBaoFragment extends BaseFragment implements View.OnClickListener {

    private EditText zhifubaoagin_et, zhifubao_et, edt_yanzhengma;
    private TextView tv_yanzhengma, phone;
    private long first = 0;
    private static final int SECONDS = 60; // 秒数
    private UserInfo mUsInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_bindzhifubao, container, false);
        inview(view);
        mUsInfo = UserLocalData.getUser(getActivity());
        initData();
        return view;
    }

    public void inview(View view) {
        ActivityStyleUtil.initSystemBar(getActivity(), R.color.color_757575); //设置标题栏颜色值

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
        try {
            if (mUsInfo.getTealName() != null && !"".equals(mUsInfo.getTealName())) {
                zhifubaoagin_et.setText(mUsInfo.getTealName());
            }
            if (mUsInfo.getAliPayNumber() != null && !"".equals(mUsInfo.getAliPayNumber())) {
                zhifubao_et.setText(mUsInfo.getAliPayNumber());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                tv_yanzhengma.setBackgroundResource(R.drawable.bg_ffe10a_rightround_2dp);
                tv_yanzhengma.setTextColor(getResources().getColor(R.color.color_000000));
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
        requestBean.setType(C.sendCodeType.REVAMPAIPAY);

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
                }) ;

    }

    /**
     * 绑定支付宝
     */
    public void bindZhiFuBao() {
        UserInfo usInfo = UserLocalData.getUser(getActivity());
        if (usInfo == null) {
            return;
        }
        if (TextUtils.isEmpty(zhifubaoagin_et.getText().toString().trim())) {
            ViewShowUtils.showShortToast(getActivity(), "请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(zhifubao_et.getText().toString().trim())) {
            ViewShowUtils.showShortToast(getActivity(), "输入支付宝账号");
            return;
        }
        if (TextUtils.isEmpty(edt_yanzhengma.getText().toString().trim())) {
            ViewShowUtils.showShortToast(getActivity(), "请输入验证码");
            return;
        }
        LoadingView.showDialog(getActivity(), "请求中...");
        String alipayNumber = zhifubao_et.getText().toString().trim();
        String realName = zhifubaoagin_et.getText().toString().trim();
        String verCode = edt_yanzhengma.getText().toString().trim();



        RequestSetAlipayBean requestBean = new RequestSetAlipayBean();
        requestBean.setAlipayNumber(alipayNumber);
        requestBean.setRealName(realName);
        requestBean.setVerCode(verCode);
        String sign = EncryptUtlis.getSign2(requestBean);
        requestBean.setSign(sign);

        RxHttp.getInstance().getUsersService().getUserSetAlipay(
//                alipayNumber,
//                realName,
//                verCode,
//                sign
                requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
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

                    /**
                     * 成功回调
                     *
                     * @param data 结果
                     */
                    @Override
                    protected void onSuccess(String data) {
                        ViewShowUtils.showShortToast(getActivity(), "绑定成功");
                        //更新个人数据
                        mUsInfo.setAliPayNumber(zhifubao_et.getText().toString().trim());
                        mUsInfo.setTealName(zhifubaoagin_et.getText().toString().trim());
                        EventBus.getDefault().post(new MessageEvent(EventBusAction.MAINPAGE_MYCENTER_REFRESH_DATA));
                        getActivity().finish();

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit: //提交数据
                bindZhiFuBao();
                break;
            default:
                break;

        }
    }


}
