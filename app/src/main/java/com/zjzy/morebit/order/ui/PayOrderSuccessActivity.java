package com.zjzy.morebit.order.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zjzy.morebit.Activity.AppStartActivity;
import com.zjzy.morebit.MainActivity;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.event.OpenCategoryEvent;
import com.zjzy.morebit.pojo.event.OpenNumberEvent;
import com.zjzy.morebit.pojo.number.GoodsOrderInfo;
import com.zjzy.morebit.utils.C;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * 会员商品支付成功页面
 * Created by haiping.liu on 2019-12-10.
 */
public class PayOrderSuccessActivity extends BaseActivity {

    @BindView(R.id.btn_order_detail)
    TextView btnOrderDetail;

    @BindView(R.id.btn_goods_list)
    TextView btnGoodsList;

    private String mOrderId;

    public static void start(Activity activity,String orderId) {
        //跳转到订单成功页面
        Intent it = new Intent(activity, PayOrderSuccessActivity.class);
        it.putExtra("orderId",orderId);
        activity.startActivity(it);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success_finish);
        initView();
    }


    private void initView(){
        mOrderId = getIntent().getStringExtra("orderId");
        btnOrderDetail.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                NumberOrderDetailActivity.startOrderDetailActivity(PayOrderSuccessActivity.this,"",mOrderId);
                finish();
            }
        });
        btnGoodsList.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new OpenNumberEvent());
                finish();
            }
        });
    }


}
