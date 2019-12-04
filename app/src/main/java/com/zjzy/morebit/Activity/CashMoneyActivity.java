package com.zjzy.morebit.Activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.CheckWithDrawBean;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.requestbodybean.RequestCheckWithdrawBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestWithdrawBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.PageToUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;
import com.zjzy.morebit.view.GetMoneySucessDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.reactivex.functions.Action;


/**
 * 提现
 * Created by Administrator on 2017/9/11 0011.
 */

public class CashMoneyActivity extends BaseActivity implements View.OnClickListener {
    private TextView txt_head_title, tv_zhifubao, tv_zhifubaoagin;
    private View status_bar;
    private LinearLayout btn_back;
    private Bundle bundle;
    private TextView freeMoney, commit;
    private EditText inputMoney;
    private int inType = 1;  // 2为代理佣金提现 1为消费佣金提现
    private GetMoneySucessDialog getMoneySucessDialog;
    private boolean isCanSubmit = false;
   private String mTotalMoney;
   private TextView withdrawTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashmoney);
        initBundle();
        inview();
        LoginUtil.getUserInfo(CashMoneyActivity.this, true); //获取个人信息
        checkWithDraw();
    }

    private void initBundle() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            inType = bundle.getInt("inType", 2);
            mTotalMoney = bundle.getString("totalMoney");
        }
    }

    public void inview() {
        EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
        } else {
            ActivityStyleUtil.initSystemBar(this, R.color.color_757575); //设置标题栏颜色值
        }

        txt_head_title = (TextView) findViewById(R.id.txt_head_title);
        txt_head_title.setText("提现");
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_back.setVisibility(View.VISIBLE);

        freeMoney = (TextView) findViewById(R.id.freeMoney);
        commit = (TextView) findViewById(R.id.commit);
        withdrawTv = (TextView)findViewById(R.id.withdrawTv);
        inputMoney = (EditText) findViewById(R.id.inputMoney);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commit();
            }
        });

        tv_zhifubao = (TextView) findViewById(R.id.zhifubao_et);
        tv_zhifubaoagin = (TextView) findViewById(R.id.zhifubaoagin_et);

        inputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //判断是否显示删除按钮
                if (TextUtils.isEmpty(inputMoney.getText().toString().trim())) {
                    isCanSubmit = false;
                } else {
                    isCanSubmit = true;
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        if (event.getAction().equals(EventBusAction.MAINPAGE_MYCENTER_REFRESH_DATA)) { //更新个人信息
            initViewData();//更新数据
        }
    }

    /**
     * 提交数据
     */
    private void commit() {
        if (TextUtils.isEmpty(inputMoney.getText().toString().trim())) {
            ViewShowUtils.showShortToast(CashMoneyActivity.this, "请输入提现金额");
            return;
        }
        if (AppUtil.isFastCashMoneyClick(5000)) {
            ViewShowUtils.showShortToast(CashMoneyActivity.this, "亲,正在帮您操作,请稍后再试!");
            return;
        }
        getToMoney();
    }

    private void initViewData() {
        try {
            UserInfo userInfo = UserLocalData.getUser(CashMoneyActivity.this);
            if (userInfo == null) {
                return;
            }
            if (inType == 2) { //代理佣金
                String agent_money = userInfo.getAgent_money();
                freeMoney.setText(mTotalMoney);
            }
            if (inType == 1) { //消费佣金
                freeMoney.setText(mTotalMoney);
            }
            tv_zhifubao.setText(userInfo.getAliPayNumber());
            tv_zhifubaoagin.setText(userInfo.getTealName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 佣金提现申请
     */
    public void getToMoney() {
        UserInfo userInfo = UserLocalData.getUser(CashMoneyActivity.this);
//        if (userInfo == null || TextUtils.isEmpty(userInfo.getId())) {
//            ViewShowUtils.showShortToast(CashMoneyActivity.this, "数据异常");
//            return;
//        }
        try {
            double getfreeMoney = Double.parseDouble(freeMoney.getText().toString().trim());
            double getMoney = Double.parseDouble(inputMoney.getText().toString().trim());
            if (getMoney <= 0) {
                ViewShowUtils.showShortToast(CashMoneyActivity.this, "提现金额不能为0");
                return;
            }
            if (getfreeMoney == 0 || getMoney > getfreeMoney) {
                ViewShowUtils.showShortToast(CashMoneyActivity.this, "余额不足");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoadingView.showDialog(CashMoneyActivity.this, "请求中...");

        RequestWithdrawBean requestWithdrawBean = new RequestWithdrawBean();
        String sign = EncryptUtlis.getSign2(requestWithdrawBean.setAmount(inputMoney.getText().toString().trim()));
        requestWithdrawBean.setAmount(inputMoney.getText().toString().trim()).setSign(sign);

        RxHttp.getInstance().getUsersService().getWithdraw(requestWithdrawBean)
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
                    protected void onSuccess(String data) {
                        Bundle bundle = new Bundle();
                        bundle.putString("title", "提现");
                        bundle.putString("fragmentName", "GetMoneySucessFragment");
                        bundle.putString("TypeName", "提现");
                        PageToUtil.goToSimpleFragment(CashMoneyActivity.this, bundle);
                        LoginUtil.getUserInfo(CashMoneyActivity.this, false); //获取个人信息
                        finish();
                    }
                });


        //        RxHttp.getInstance().getUsersService().getWithdraw(inputMoney.getText().toString().trim(), "")
//                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
//                .compose(this.<BaseResponse<String>>bindToLifecycle())
//                .doFinally(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        LoadingView.dismissDialog();
//                    }
//                })
//                .subscribe(new DataObserver<String>() {
//                    @Override
//                    protected void onDataNull() {
//                        onSuccess("");
//                    }
//
//                    @Override
//                    protected void onSuccess(String data) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("title", "提现");
//                        bundle.putString("fragmentName", "GetMoneySucessFragment");
//                        bundle.putString("TypeName", "提现");
//                        PageToUtil.goToSimpleFragment(CashMoneyActivity.this, bundle);
//                        LoginUtil.getUserInfo(CashMoneyActivity.this, false); //获取个人信息
//                        finish();
//                    }
//                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }

    }


    private void checkWithDraw(){
        RequestCheckWithdrawBean requestCheckWithdrawBean = new RequestCheckWithdrawBean(inType);
        RxHttp.getInstance().getUsersService().checkWithdrawalRecord(requestCheckWithdrawBean)
                .compose(RxUtils.<BaseResponse<CheckWithDrawBean>>switchSchedulers())
                .compose(this.<BaseResponse<CheckWithDrawBean>>bindToLifecycle())
                .subscribe(new DataObserver<CheckWithDrawBean>() {
                    @Override
                    protected void onDataNull() {

                    }

                    @Override
                    protected void onSuccess(CheckWithDrawBean data) {
                        if(null != data && data.getStatus() == 0){
                           commit.setEnabled(false);
                            withdrawTv.setVisibility(View.VISIBLE);
                            if(null != data.getMsg() && !TextUtils.isEmpty(data.getMsg())){
                                withdrawTv.setText(data.getMsg());
                            }
                        }else{
                            commit.setEnabled(true);
                            withdrawTv.setVisibility(View.GONE);
                        }
                    }
                });

    }

}
