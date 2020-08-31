package com.zjzy.morebit.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Activity.CashMoneyActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.MembershipAdapter1;
import com.zjzy.morebit.adapter.MembershipAdapter2;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.info.contract.VipContract;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.CheckWithDrawBean;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.RequestWithdrawBean2;
import com.zjzy.morebit.pojo.RequestWithdrawDetailBean;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.VipBean;
import com.zjzy.morebit.pojo.WithdrawDetailBean;
import com.zjzy.morebit.pojo.request.base.RequestBaseOs;
import com.zjzy.morebit.pojo.requestbodybean.RequestCheckWithdrawBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.CommInterface;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.PageToUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.reactivex.Observable;

/**
 * 提现
 */
public class WithdrawalFragment extends BaseMainFragmeng implements View.OnClickListener {

    private EditText etUserName;
    private TextView zhifubaoagin_et, zhifubao_et, tv_xiu1, tv_xiu2, commit, tv_all, all_ti, withdrawTv;
    private Runnable runnable;
    Handler handler = new Handler();
    private String allMoney;
    private int type = 1;
    private int mtype;
    private    String s="";
    public static WithdrawalFragment newInstance(String allMoney, int type) {
        WithdrawalFragment fragment = new WithdrawalFragment();
        Bundle args = new Bundle();
        args.putString(C.Extras.WITHDRAW_MONEY, allMoney);
        args.putInt(C.Extras.WITHDRAW_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cashmoney, container, false);
        initView(view);
        checkData();

        return view;
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        checkData();
    }

    private void checkData() {
        CommInterface.checkWithdrawalRecord(this)
                .subscribe(new DataObserver<CheckWithDrawBean>(false) {
                    @Override
                    protected void onDataListEmpty() {


                    }

                    @Override
                    protected void onDataNull() {

                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onActivityFailure(errorMsg);

                    }

                    @Override
                    protected void onSuccess(CheckWithDrawBean data) {
                        withdrawTv.setText(data.getMsg() + "");
                        commit.setBackgroundResource(R.drawable.background_d3d3d3_radius_30dp);
                        commit.setEnabled(false);
                        mtype = data.getStatus();
                    }
                });
    }


    private void getData() {
        CommInterface.getUserWithdrawApplyNew(this, etUserName.getText().toString(), type)
                .subscribe(new DataObserver<String>(false) {
                    @Override
                    protected void onDataListEmpty() {


                    }

                    @Override
                    protected void onDataNull() {

                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onActivityFailure(errorMsg);

                    }

                    @Override
                    protected void onSuccess(String data) {
                        ToastUtils.showShort(data + "");
                        checkData();
                        etUserName.setText("");
                    }
                });

    }

    private void onActivityFailure(String errorMsg) {
        ToastUtils.showShort(errorMsg + "");
    }


    private void initView(View view) {


        Bundle arguments = getArguments();
        if (arguments != null) {
            allMoney = arguments.getString(C.Extras.WITHDRAW_MONEY);
            type = arguments.getInt(C.Extras.WITHDRAW_TYPE);
        }
        withdrawTv = view.findViewById(R.id.withdrawTv);
        all_ti = view.findViewById(R.id.all_ti);
        tv_all = view.findViewById(R.id.tv_all);
        tv_all.setText("可提现" + MathUtils.getnum(allMoney));
        zhifubaoagin_et = view.findViewById(R.id.zhifubaoagin_et);
        zhifubao_et = view.findViewById(R.id.zhifubao_et);
        tv_xiu1 = view.findViewById(R.id.tv_xiu1);
        tv_xiu2 = view.findViewById(R.id.tv_xiu2);
        tv_xiu1.setOnClickListener(this);
        tv_xiu2.setOnClickListener(this);
        commit = view.findViewById(R.id.commit);
        all_ti.setOnClickListener(new View.OnClickListener() {//全部提现
            @Override
            public void onClick(View v) {
                etUserName.setText(MathUtils.getnum(String.valueOf(Math.floor(Double.valueOf(allMoney)))));
            }
        });
        //设置"用户名"提示文字的大小
        etUserName = view.findViewById(R.id.inputMoney);
        SpannableString s = new SpannableString("最小提现金额1元");
        AbsoluteSizeSpan textSize = new AbsoluteSizeSpan(16, true);
        s.setSpan(textSize, 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        etUserName.setHint(s);
        etUserName.addTextChangedListener(new TextWatcher() {//监听输入框字体大小
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    //不加粗
                    etUserName.getPaint().setFakeBoldText(false);
                } else {
                    //加粗
                    etUserName.getPaint().setFakeBoldText(true);
                    if (zhifubaoagin_et.getText().toString().length() > 0 && zhifubao_et.getText().toString().length() > 0 && mtype != 0) {
                        commit.setBackgroundResource(R.drawable.background_f05557_radius_30dp);
                        commit.setEnabled(true);
                    } else {
                        commit.setBackgroundResource(R.drawable.background_d3d3d3_radius_30dp);
                        commit.setEnabled(false);
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                //如果在800毫秒之内又输入了，就会再次返回，然后就再次remove掉了上次
//如果300毫秒内不输入了，就不会执行remove了，就直接跳转了
//原理：每次都执行跳转流程，但是有个条件，下一次不会返回的情况下，下次一旦返回就把上次的跳转操作取消了
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                    // Log.v("tag", "---" + s.toString());
                }
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        //Log.v("tag", "跳转======" + s.toString());
                        isMethodManager(etUserName);//关闭输入法弹框
                    }
                };
                //  Log.v("tag", "(((((" + s.toString());
                handler.postDelayed(runnable, 800);

            }
        });


        commit.setOnClickListener(new View.OnClickListener() {//提现
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        initViewData();
    }

    private void initViewData() {
        try {
            UserInfo userInfo = UserLocalData.getUser(getActivity());
            if (userInfo == null) {
                return;
            }
            if (TextUtils.isEmpty(userInfo.getTealName())) {
                tv_xiu1.setVisibility(View.GONE);
                initClick(zhifubaoagin_et);
            } else {
                tv_xiu1.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(userInfo.getAliPayNumber())) {
                tv_xiu2.setVisibility(View.GONE);
                initClick(zhifubao_et);
            } else {
                tv_xiu2.setVisibility(View.VISIBLE);
            }
            String aliPayNumber = userInfo.getAliPayNumber();
            String substring = aliPayNumber.substring(0, 3);
            String substring1 = aliPayNumber.replace(substring, "");
            s="";
            for (int i=0;i<substring1.length();i++){
                s= s+"*";
            }
            zhifubao_et.setText(substring+s);
            zhifubaoagin_et.setText(userInfo.getTealName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initClick(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageToUtil.goToUserInfoSimpleFragment(getActivity(), "绑定支付宝账号", "BindZhiFuBaoFragment");
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_xiu1:
                PageToUtil.goToUserInfoSimpleFragment(getActivity(), "绑定支付宝账号", "BindZhiFuBaoFragment");
                break;
            case R.id.tv_xiu2:
                PageToUtil.goToUserInfoSimpleFragment(getActivity(), "绑定支付宝账号", "BindZhiFuBaoFragment");
                break;
        }

    }
}
