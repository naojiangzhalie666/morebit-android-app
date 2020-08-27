package com.zjzy.morebit.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.NumberSubFragment;
import com.zjzy.morebit.home.fragment.AdvancedClassFragment;
import com.zjzy.morebit.home.fragment.BillDetailsFragment;
import com.zjzy.morebit.home.fragment.MembershipFragment;
import com.zjzy.morebit.home.fragment.ShoppingMallFragment;
import com.zjzy.morebit.main.ui.myview.xtablayout.XTabLayout;
import com.zjzy.morebit.utils.BirthdayUtil;
import com.zjzy.morebit.utils.SwipeDirectionDetector;

import java.util.ArrayList;
import java.util.List;

/*
 *
 * 账单明细
 * */
public class BillDetailsActivity extends BaseActivity implements View.OnClickListener {
    private TextView txt_head_title;
    private LinearLayout btn_back, btn_right;
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
        txt_head_title = (TextView) findViewById(R.id.txt_head_title);
        txt_head_title.setText("账单明细");
        txt_head_title.setOnClickListener(this);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_right = (LinearLayout) findViewById(R.id.btn_right);
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(this);
        xablayout = (XTabLayout) findViewById(R.id.xablayout);
        viewpager= (ViewPager) findViewById(R.id.viewpager);
        initViewPager();
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
            BillDetailsFragment billDetailsFragment = BillDetailsFragment.newInstance(position+1);
                return billDetailsFragment;



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
            case R.id.btn_right:
                BirthdayUtil.getInstance(BillDetailsActivity.this).showBirthdayDate(BillDetailsActivity.this, "选择月份", 0);
                break;
        }
    }
}
