package com.zjzy.morebit.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.TimeBean;
import com.zjzy.morebit.pojo.UserBalanceBean;
import com.zjzy.morebit.pojo.UserIncomeByTime;
import com.zjzy.morebit.pojo.UserIncomeDetail;
import com.zjzy.morebit.pojo.requestbodybean.RequestIncomeByTimeBean;
import com.zjzy.morebit.utils.BirthdayUtil;
import com.zjzy.morebit.utils.C;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;

import io.reactivex.Observable;

/*
 *
 * 上月月报
 * */
public class MonthAgoActivity extends BaseActivity {
    private TextView txt_head_title, time_title, fans_huo, fans_add, yugujifen, yugushoyi,yugu_activity;
    private LinearLayout btn_back;
    private UserIncomeDetail mdata;
    private String time="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(false)
                .statusBarColor(R.color.white)
                .init();
        setContentView(R.layout.activity_month_ago);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
    }

    private void initView() {
        mdata = (UserIncomeDetail) getIntent().getSerializableExtra(C.Extras.EARNING);
        txt_head_title = (TextView) findViewById(R.id.txt_head_title);
        LinearLayout btn_right = (LinearLayout) findViewById(R.id.btn_right);
        btn_right.setVisibility(View.VISIBLE);
        txt_head_title.setText("历史月报");
        txt_head_title.setTextSize(18);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        yugu_activity= (TextView) findViewById(R.id.yugu_activity);//预估活动奖励
        time_title = (TextView) findViewById(R.id.time_title);//时间
        yugushoyi = (TextView) findViewById(R.id.yugushoyi);//预估收益
        yugujifen = (TextView) findViewById(R.id.yugujifen);//预估积分
        fans_add = (TextView) findViewById(R.id.fans_add);//新增粉丝
        fans_huo = (TextView) findViewById(R.id.fans_huo);//粉丝活跃
        time_title.setText(mdata.getPreDateStr() + "");
        yugushoyi.setText(mdata.getPrevMonthEstimateZgMoney()+"");
        yugujifen.setText(mdata.getPrevMonthIntegral()+"");
        fans_add.setText(mdata.getPrevMonthDirectUser()+"");
        fans_huo.setText(mdata.getPrevMonthIndirectUser()+"");
        yugu_activity.setText(mdata.getPrevMonthEstimateRewardMoney()+"");
        time_title.getPaint().setFakeBoldText(true);

        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BirthdayUtil.getInstance(MonthAgoActivity.this).showBirthdayDate2(MonthAgoActivity.this, "选择月份",time_title, 1);
            }
        });

    }
    @Subscribe  //订阅事件
    public void onEventMainThread(TimeBean evBean) {
        time = evBean.getTime();
        getData();
    }
    private void getData() {
        getUserIncomeByTime(this,time)
                .subscribe(new DataObserver<UserIncomeByTime>(false) {
                    @Override
                    protected void onDataListEmpty() {



                    }

                    @Override
                    protected void onDataNull() {

                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onActivityFailure();

                    }

                    @Override
                    protected void onSuccess(UserIncomeByTime data) {
                        onSuccessful(data);
                    }
                });
    }

    private void onSuccessful(UserIncomeByTime data) {
        if (data!=null){
            yugushoyi.setText(data.getEstimateZgMoney() + "");
            yugujifen.setText(data.getEstimateIntegral() + "");
            fans_add.setText(data.getDirectUser() + "");
            fans_huo.setText(data.getIndirectUser() + "");
            yugu_activity.setText(data.getEstimateRewardMoney()+"");
        }
    }

    private void onActivityFailure() {

    }


    /**
     * 我的收益-历史月报
     *
     * @param fragment
     * @return
     */

    public Observable<BaseResponse<UserIncomeByTime>> getUserIncomeByTime(RxAppCompatActivity fragment,String time) {
        RequestIncomeByTimeBean incomeByTimeBean=new RequestIncomeByTimeBean();
        if (!TextUtils.isEmpty(time)){
            incomeByTimeBean.setTime(time);
        }
        return RxHttp.getInstance().getCommonService()
                .getUserIncomeByTime(incomeByTimeBean)
                .compose(RxUtils.<BaseResponse<UserIncomeByTime>>switchSchedulers())
                .compose(fragment.<BaseResponse<UserIncomeByTime>>bindToLifecycle());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
