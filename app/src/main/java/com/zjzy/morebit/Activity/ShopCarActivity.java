package com.zjzy.morebit.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zjzy.morebit.MainActivity;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.ShopCarAdapter;
import com.zjzy.morebit.utils.helper.ActivityLifeHelper;

/*
*优选商城购物车
*
* */
public class ShopCarActivity extends BaseActivity {
    private LinearLayout btn_back,dateNullView;
    private RecyclerView rcy_shopcar;
    private SmartRefreshLayout refreshLayout;
    private TextView btn_invite;
    private   ShopCarAdapter shopCarAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);
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
        txt_head_title.setText("购物车");
        btn_invite= (TextView) findViewById(R.id.btn_invite);
        btn_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLifeHelper.getInstance().finishActivity(MainActivity.class);
            }
        });
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        dateNullView= (LinearLayout) findViewById(R.id.dateNullView);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rcy_shopcar= (RecyclerView) findViewById(R.id.rcy_shopcar);
        refreshLayout= (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rcy_shopcar.setLayoutManager(manager);
          shopCarAdapter=new ShopCarAdapter(this);
        rcy_shopcar.setAdapter(shopCarAdapter);
    }
}
