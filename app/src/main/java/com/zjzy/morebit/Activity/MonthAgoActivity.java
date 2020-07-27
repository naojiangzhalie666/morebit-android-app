package com.zjzy.morebit.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.UserIncomeDetail;
import com.zjzy.morebit.utils.C;

import java.io.Serializable;

/*
*
* 上月月报
* */
public class MonthAgoActivity extends BaseActivity {
    private TextView txt_head_title,time_title,fans_huo,fans_add,yugujifen,yugushoyi;
    private LinearLayout btn_back;
    private UserIncomeDetail mdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(false)
                .statusBarColor(R.color.white)
                .init();
        setContentView(R.layout.activity_month_ago);



        initView();
    }

    private void initView() {
         mdata = (UserIncomeDetail) getIntent().getSerializableExtra(C.Extras.EARNING);
        txt_head_title = (TextView) findViewById(R.id.txt_head_title);
        txt_head_title.setText("上月月报");
        txt_head_title.getPaint().setFakeBoldText(true);
        txt_head_title.setTextSize(18);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        time_title= (TextView) findViewById(R.id.time_title);//时间
        yugushoyi= (TextView) findViewById(R.id.yugushoyi);//预估收益
        yugujifen= (TextView) findViewById(R.id.yugujifen);//预估积分
        fans_add= (TextView) findViewById(R.id.fans_add);//新增粉丝
        fans_huo= (TextView) findViewById(R.id.fans_huo);//粉丝活跃
        time_title.getPaint().setFakeBoldText(true);
        time_title.setText(mdata.getPreDateStr()+"");
        yugushoyi.setText(mdata.getPrevMonthEstimateMoney()+"");
        yugujifen.setText(mdata.getPrevMonthIntegral()+"");
        fans_add.setText(mdata.getPrevMonthDirectUser()+"");
        fans_huo.setText(mdata.getPrevMonthIndirectUser()+"");






    }
}
