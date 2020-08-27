package com.zjzy.morebit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.home.fragment.BillDetailsFragment;
import com.zjzy.morebit.home.fragment.WithdrawDetailsFragment;
import com.zjzy.morebit.main.ui.myview.xtablayout.XTabLayout;
import com.zjzy.morebit.utils.BirthdayUtil;
import com.zjzy.morebit.utils.C;

import java.util.ArrayList;
import java.util.List;

/*
 *
 * 提现明细
 * */
public class WithdrawDetailsActivity extends BaseActivity implements View.OnClickListener {
    private TextView txt_head_title;
    private LinearLayout btn_back;
    private XTabLayout xablayout;
    private ViewPager viewpager;
    private List<String> pagerData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(false)
                .statusBarColor(R.color.white)
                .init();
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra(C.Extras.WITHDRAW_TYPE);
        txt_head_title = (TextView) findViewById(R.id.txt_head_title);
        txt_head_title.setText("提现明细");
        txt_head_title.setOnClickListener(this);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        xablayout = (XTabLayout) findViewById(R.id.xablayout);
        viewpager= (ViewPager) findViewById(R.id.viewpager);
        initViewPager();
        if (!TextUtils.isEmpty(stringExtra)){
            if ("0".equals(stringExtra)){
                xablayout.getTabAt(0).select();
            }else if ("1".equals(stringExtra)){
                xablayout.getTabAt(1).select();
            }else{
                xablayout.getTabAt(2).select();
            }
        }

    }

    private void initViewPager() {
        pagerData.add("返利");
        pagerData.add("积分");
        pagerData.add("奖励");

       PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(pagerAdapter);
        xablayout.setupWithViewPager(viewpager);
        viewpager.setOffscreenPageLimit(3);
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
            WithdrawDetailsFragment withdrawDetailsFragment = WithdrawDetailsFragment.newInstance(position+1);
                return withdrawDetailsFragment;



        }

        @Override
        public int getCount() {
            return pagerData != null ? pagerData.size() : 0;
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
