package com.zjzy.morebit.order.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.info.ui.fragment.OrderDetailFragment;
import com.zjzy.morebit.pojo.number.GoodsOrderInfo;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.OpenFragmentUtils;

import butterknife.BindView;

/**
 * 订单页面
 * Created by haiping.liu on 2019-12-24.
 */
public class OrderTeamActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    LinearLayout btnBack;

    @BindView(R.id.ll_taobao_order)
    LinearLayout llTaobaoOrder;

    @BindView(R.id.ll_morebit_order)
    LinearLayout llMorebitOrder;

    @BindView(R.id.ll_jd_order)
    LinearLayout llJdOrder;

    @BindView(R.id.ll_pdd_order)
    LinearLayout llPddOrder;
    @BindView(R.id.ll_kl_order)
    LinearLayout llklOrder;

    public static void start(Activity activity) {
        //跳转到订单类型列表
        Intent it = new Intent(activity, OrderTeamActivity.class);
        activity.startActivity(it);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_team);
        initView();
    }
    private  void initView(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
        } else {
            ActivityStyleUtil.initSystemBar(this, R.color.color_F8F8F8); //设置标题栏颜色值
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llTaobaoOrder.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(C.Extras.order_type,C.OrderType.TAOBAO);
                OpenFragmentUtils.goToSimpleFragment(OrderTeamActivity.this,
                        OrderDetailFragment.class.getName(), bundle);
            }
        });
        llMorebitOrder.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(C.Extras.order_type,C.OrderType.YUXUAN);
                OpenFragmentUtils.goToSimpleFragment(OrderTeamActivity.this,
                        OrderDetailFragment.class.getName(), bundle);
            }
        });

        //京东
        llJdOrder.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(C.Extras.order_type,C.OrderType.JD);
                OpenFragmentUtils.goToSimpleFragment(OrderTeamActivity.this,
                        OrderDetailFragment.class.getName(), bundle);
            }
        });
        //拼多多
        llPddOrder.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(C.Extras.order_type,C.OrderType.PDD);
                OpenFragmentUtils.goToSimpleFragment(OrderTeamActivity.this,
                        OrderDetailFragment.class.getName(), bundle);
            }
        });
        //考拉
        llklOrder.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(C.Extras.order_type,C.OrderType.KAOLA);
                OpenFragmentUtils.goToSimpleFragment(OrderTeamActivity.this,
                        OrderDetailFragment.class.getName(), bundle);
            }
        });
    }

}
