package com.zjzy.morebit.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.zjzy.morebit.home.fragment.AdvancedClassFragment;
import com.zjzy.morebit.home.fragment.MembershipFragment;
import com.zjzy.morebit.home.fragment.ShoppingMallFragment;
import com.zjzy.morebit.home.fragment.WithdrawalFragment;
import com.zjzy.morebit.main.ui.myview.xtablayout.XTabLayout;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.CheckWithDrawBean;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.UserBalanceBean;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.requestbodybean.RequestCheckWithdrawBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestWithdrawBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.PageToUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;
import com.zjzy.morebit.view.GetMoneySucessDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Action;


/**
 * 提现
 * Created by Administrator on 2017/9/11 0011.
 */

public class CashMoneyActivity extends BaseActivity implements View.OnClickListener {
    private TextView txt_head_title, tv_zhifubao, tv_zhifubaoagin,txt_head_title2;
    private View status_bar;
    private LinearLayout btn_back;
    private Bundle bundle;
    private TextView freeMoney, commit;
    private EditText inputMoney;
    private int inType = 0;  // 1为消费佣金提现
    private GetMoneySucessDialog getMoneySucessDialog;
    private boolean isCanSubmit = false;
   private String mTotalMoney;
   private TextView withdrawTv;
    private XTabLayout xablayout;
    private ViewPager viewpager;
    private List<String> pagerData = new ArrayList<>();
    private List<WithdrawalFragment> list=new ArrayList<>();
    private   UserBalanceBean data;
    private int type=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashmoney);
        initBundle();
        inview();
        LoginUtil.getUserInfo(CashMoneyActivity.this, true); //获取个人信息
      //  checkWithDraw();
    }

    private void initBundle() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            inType = bundle.getInt(C.Extras.WITHDRAW_TYPE, 0);
            data = (UserBalanceBean) bundle.getSerializable(C.Extras.WITHDRAW_ALLMONEY);

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
        txt_head_title.setText("余额提现");
        txt_head_title2= (TextView) findViewById(R.id.txt_head_title2);
        txt_head_title2.setText("提现明细");
        txt_head_title2.setTextColor(Color.parseColor("#666666"));
        txt_head_title2.setTextSize(16);
        txt_head_title2.setOnClickListener(this);
        txt_head_title2.setVisibility(View.VISIBLE);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_back.setVisibility(View.VISIBLE);

        freeMoney = (TextView) findViewById(R.id.freeMoney);
        commit = (TextView) findViewById(R.id.commit);
        withdrawTv = (TextView)findViewById(R.id.withdrawTv);
        inputMoney = (EditText) findViewById(R.id.inputMoney);


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


        xablayout = (XTabLayout) findViewById(R.id.xTablayout);
        viewpager= (ViewPager) findViewById(R.id.viewpager);
        initViewPager();

        switch (inType){
            case 0:
                xablayout.getTabAt(0).select();
                break;
            case 1:
                xablayout.getTabAt(1).select();
                break;
            case 2:
                xablayout.getTabAt(2).select();
                break;
        }
    }

    private void initViewPager() {
        pagerData.add("自购返利");
        pagerData.add("优选商品积分");
        pagerData.add("活动奖励");

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(pagerAdapter);
        xablayout.setupWithViewPager(viewpager);
        viewpager.setOffscreenPageLimit(3);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                type= position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);

        }


        @Override
        public CharSequence getPageTitle(int position) {
            return pagerData == null ? "" + position : pagerData.get(position);

        }

        @Override
        public Fragment getItem(int position) {
            if (position==0){
                mTotalMoney=data.getRebateAmount();

            }else if (position==1){
                mTotalMoney=data.getIntegralAmount();
            }else {
                mTotalMoney=data.getRewardAmount();
            }
            WithdrawalFragment withdrawalFragment = WithdrawalFragment.newInstance(mTotalMoney,position+1);
                return withdrawalFragment;



        }

        @Override
        public int getCount() {
            return pagerData != null ? pagerData.size() : 0;
        }


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {

    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.txt_head_title2:
                Intent intent = new Intent(this, WithdrawDetailsActivity.class);
                intent.putExtra(C.Extras.WITHDRAW_TYPE,type+"");
                startActivity(intent);
                break;
            default:
                break;
        }

    }




}
