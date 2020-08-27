package com.zjzy.morebit.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.UserBalanceBean;
import com.zjzy.morebit.pojo.WithdrawDetailBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MathUtils;

import java.util.List;

import io.reactivex.Observable;

/*
 *
 * 提现分类页面
 * */
public class WithdrawalActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_fan, rl_yx, rl_activity;
    private TextView zg_qian, yx_qian, activity_qian;
    private UserBalanceBean mdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(false)
                .statusBarColor(R.color.white)
                .init();
        initView();
    }

    private void initView() {
        TextView txt_head_title = (TextView) findViewById(R.id.txt_head_title);
        txt_head_title.setTextSize(18);
        txt_head_title.setText("可提现");
        LinearLayout btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_fan = (RelativeLayout) findViewById(R.id.rl_fan);
        rl_fan.setOnClickListener(this);
        rl_yx = (RelativeLayout) findViewById(R.id.rl_yx);
        rl_yx.setOnClickListener(this);
        rl_activity = (RelativeLayout) findViewById(R.id.rl_activity);
        rl_activity.setOnClickListener(this);
        zg_qian = (TextView) findViewById(R.id.zg_qian);//自购
        yx_qian = (TextView) findViewById(R.id.yx_qian);//积分
        activity_qian = (TextView) findViewById(R.id.activity_qian);//活动
        getData();
    }

    private void getData() {
        getUserBalance(this)
                .subscribe(new DataObserver<UserBalanceBean>(false) {
                    @Override
                    protected void onDataListEmpty() {

                        onActivityFailure();
                    }

                    @Override
                    protected void onDataNull() {

                        onActivityFailure();

                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onActivityFailure();

                    }

                    @Override
                    protected void onSuccess(UserBalanceBean data) {
                        onSuccessful(data);
                    }
                });
    }

    private void onActivityFailure() {

    }

    private void onSuccessful(UserBalanceBean data) {
        if (data != null) {
            mdata=data;
            zg_qian.setText(data.getRebateAmount() == "" ? "剩余 ¥ 0" : "剩余 ¥ " + MathUtils.getnum(data.getRebateAmount()));
            yx_qian.setText(data.getIntegralAmount() == "" ? "剩余 ¥ 0" : "剩余 ¥ " + MathUtils.getnum(data.getIntegralAmount()));
            activity_qian.setText(data.getRewardAmount() == "" ? "剩余 ¥ 0" : "剩余 ¥ " + MathUtils.getnum(data.getRewardAmount()));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, CashMoneyActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable(C.Extras.WITHDRAW_ALLMONEY,mdata);
        intent.putExtras(bundle);
        switch (v.getId()) {
            case R.id.rl_fan:
                intent.putExtra(C.Extras.WITHDRAW_TYPE, 0);//提现类型
                startActivity(intent);
                break;
            case R.id.rl_yx:
                intent.putExtra(C.Extras.WITHDRAW_TYPE, 1);
                startActivity(intent);
                break;
            case R.id.rl_activity:
                intent.putExtra(C.Extras.WITHDRAW_TYPE, 2);
                startActivity(intent);
                break;
        }
    }

    /**
     * 提现金额
     *
     * @param fragment
     * @return
     */

    public Observable<BaseResponse<UserBalanceBean>> getUserBalance(RxAppCompatActivity fragment) {

        return RxHttp.getInstance().getCommonService()
                .getUserBalance()
                .compose(RxUtils.<BaseResponse<UserBalanceBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<UserBalanceBean>>bindToLifecycle());

    }
}
