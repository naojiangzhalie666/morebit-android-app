package com.zjzy.morebit.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.info.ui.fragment.OrderDetailFragment;
import com.zjzy.morebit.order.ui.OrderTeamActivity;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;

/*
 *
 * 我的订单
 *
 * */
public class MyOrderActivity extends BaseActivity implements View.OnClickListener {
    private TextView txt_head_title, order_title, optimization_title, life_title;
    private LinearLayout btn_back;
    private RelativeLayout search_rl;
    private ImageInfo orderSearch;
    private LinearLayout order_all, order_tao, order_jd, order_pdd, order_wph, order_kl, yx_all,life_kb,life_ele,
    yx_zhi,yx_fa,yx_shou,yx_wan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .init();
        initView();
    }

    private void initView() {

        Intent intent = getIntent();
        orderSearch = (ImageInfo) intent.getSerializableExtra(C.Extras.SEARCHINFO);
        txt_head_title = (TextView) findViewById(R.id.txt_head_title);
        txt_head_title.setText("我的订单");
        txt_head_title.setTextSize(18);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        order_title = (TextView) findViewById(R.id.order_title);
        order_title.getPaint().setFakeBoldText(true);
        optimization_title = (TextView) findViewById(R.id.optimization_title);
        optimization_title.getPaint().setFakeBoldText(true);
        life_title = (TextView) findViewById(R.id.life_title);
        life_title.getPaint().setFakeBoldText(true);
        search_rl = (RelativeLayout) findViewById(R.id.search_rl);
        search_rl.setOnClickListener(this);
        order_all = (LinearLayout) findViewById(R.id.order_all);
        order_all.setOnClickListener(this);
        order_tao = (LinearLayout) findViewById(R.id.order_tao);
        order_tao.setOnClickListener(this);
        order_jd = (LinearLayout) findViewById(R.id.order_jd);
        order_jd.setOnClickListener(this);
        order_pdd = (LinearLayout) findViewById(R.id.order_pdd);
        order_pdd.setOnClickListener(this);
        order_wph = (LinearLayout) findViewById(R.id.order_wph);
        order_wph.setOnClickListener(this);
        order_kl = (LinearLayout) findViewById(R.id.order_kl);
        order_kl.setOnClickListener(this);
        yx_all = (LinearLayout) findViewById(R.id.yx_all);
        yx_all.setOnClickListener(this);
        life_ele= (LinearLayout) findViewById(R.id.life_ele);
        life_ele.setOnClickListener(this);
        life_kb= (LinearLayout) findViewById(R.id.life_kb);
        life_kb.setOnClickListener(this);
        yx_zhi= (LinearLayout) findViewById(R.id.yx_zhi);
        yx_zhi.setOnClickListener(this);
        yx_fa= (LinearLayout) findViewById(R.id.yx_fa);
        yx_fa.setOnClickListener(this);
        yx_shou= (LinearLayout) findViewById(R.id.yx_shou);
        yx_shou.setOnClickListener(this);
        yx_wan= (LinearLayout) findViewById(R.id.yx_wan);
        yx_wan.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.search_rl:
                GoodsUtil.getOrderSearch(this);
                break;
            case R.id.order_all://全部订单
                RetailersActivity.start(this, 0);
                break;
            case R.id.order_tao://淘宝订单
                RetailersActivity.start(this, 1);
                break;
            case R.id.order_jd://京东订单
                RetailersActivity.start(this, 2);
                break;
            case R.id.order_pdd://拼多多订单
                RetailersActivity.start(this, 4);
                break;
            case R.id.order_wph://唯品会订单
                RetailersActivity.start(this, 6);
                break;
            case R.id.order_kl://考拉订单
                RetailersActivity.start(this, 5);
                break;
            case R.id.yx_all://订单优选全部
                Bundle bundle = new Bundle();
                bundle.putInt(C.Extras.order_type, C.OrderType.YUXUAN);
                bundle.putInt(C.Extras.YXTYPE,0);
                OpenFragmentUtils.goToSimpleFragment(MyOrderActivity.this, OrderDetailFragment.class.getName(), bundle);
                break;
            case R.id.yx_zhi://订单优选待支付
                Bundle bundle1 = new Bundle();
                bundle1.putInt(C.Extras.order_type, C.OrderType.YUXUAN);
                bundle1.putInt(C.Extras.YXTYPE,1);
                OpenFragmentUtils.goToSimpleFragment(MyOrderActivity.this, OrderDetailFragment.class.getName(), bundle1);
                break;
            case R.id.yx_fa://订单优选待发货
                Bundle bundle2 = new Bundle();
                bundle2.putInt(C.Extras.order_type, C.OrderType.YUXUAN);
                bundle2.putInt(C.Extras.YXTYPE,2);
                OpenFragmentUtils.goToSimpleFragment(MyOrderActivity.this, OrderDetailFragment.class.getName(), bundle2);
                break;
            case R.id.yx_shou://订单优选待收货
                Bundle bundle3 = new Bundle();
                bundle3.putInt(C.Extras.order_type, C.OrderType.YUXUAN);
                bundle3.putInt(C.Extras.YXTYPE,3);
                OpenFragmentUtils.goToSimpleFragment(MyOrderActivity.this, OrderDetailFragment.class.getName(), bundle3);
                break;
            case R.id.yx_wan://订单优选已完成
                Bundle bundle4 = new Bundle();
                bundle4.putInt(C.Extras.order_type, C.OrderType.YUXUAN);
                bundle4.putInt(C.Extras.YXTYPE,4);
                OpenFragmentUtils.goToSimpleFragment(MyOrderActivity.this, OrderDetailFragment.class.getName(), bundle4);
                break;
            case R.id.life_ele://饿了么
                Intent intent = new Intent(MyOrderActivity.this, LifeServiceActivity.class);
                intent.putExtra(C.Extras.ELEORDER,"饿了么订单");
                intent.putExtra(C.Extras.ELETYPE,7);
                startActivity(intent);
                break;
            case R.id.life_kb://口碑
                Intent intent2 = new Intent(MyOrderActivity.this, LifeServiceActivity.class);
                intent2.putExtra(C.Extras.ELEORDER,"口碑订单");
                intent2.putExtra(C.Extras.ELETYPE,8);
                startActivity(intent2);
                break;
        }
    }
}
