package com.zjzy.morebit.Activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.MainActivity;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.ShopCarAdapter;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.order.ConfirmOrderActivity2;
import com.zjzy.morebit.order.ui.ConfirmOrderActivity;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.ShopCarGoodsBean;
import com.zjzy.morebit.pojo.ShopCarNumBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.number.GoodsOrderInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.helper.ActivityLifeHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/*
 *优选商城购物车
 *
 * */
public class ShopCarActivity extends BaseActivity {
    private LinearLayout btn_back, dateNullView;
    private RecyclerView rcy_shopcar;
    private SmartRefreshLayout refreshLayout;
    private TextView btn_invite;
    private ShopCarAdapter shopCarAdapter;
    private ImageView checkbox;
    private boolean isAll = false;
    private RelativeLayout rl_check;
    private List<ShopCarGoodsBean.CartListBean> cartList;
    private LinearLayout ll_botton;
    private TextView shop_price;
    private ShopCarGoodsBean.CartTotalBean total;
    private TextView shop_count;
    private String zong="0";
    private int count=0;
    private BigDecimal result3;
    private String getnum="0";
    private List<ShopCarGoodsBean.CartListBean> cartList2=new ArrayList<>();
    private LinearLayout ll_di;

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
        EventBus.getDefault().register(this);
    }

    private void initView() {
        TextView txt_head_title = (TextView) findViewById(R.id.txt_head_title);
        txt_head_title.setTextSize(18);
        txt_head_title.setText("购物车");
        btn_invite = (TextView) findViewById(R.id.btn_invite);
        btn_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLifeHelper.getInstance().finishActivity(MainActivity.class);
            }
        });
        shop_price = (TextView) findViewById(R.id.shop_price);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        dateNullView = (LinearLayout) findViewById(R.id.dateNullView);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rcy_shopcar = (RecyclerView) findViewById(R.id.rcy_shopcar);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rcy_shopcar.setLayoutManager(manager);
        shopCarAdapter = new ShopCarAdapter(this);
        rcy_shopcar.setAdapter(shopCarAdapter);
        checkbox = (ImageView) findViewById(R.id.checkbox);
        rl_check = (RelativeLayout) findViewById(R.id.rl_check);
        ll_botton = (LinearLayout) findViewById(R.id.ll_botton);
        shop_count= (TextView) findViewById(R.id.shop_count);
        ll_di= (LinearLayout) findViewById(R.id.ll_di);
        getData();



        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();


            }
        });






        rl_check.setOnClickListener(new View.OnClickListener() {//全选
            @Override
            public void onClick(View v) {
                if (!isAll) {
                    shopCarAdapter.isAll(true);
                    checkbox.setSelected(true);
                    int count=0;
                    for (int i=0;i<cartList.size();i++){
                        if (cartList.get(i).isOnSale()){
                           cartList.get(i).setChecked(true);
                           count++;
                        }

                    }
                    getZong();
                    shop_count.setText("结算"+"("+count+")");
                    isAll = true;
                } else {
                    checkbox.setSelected(true);
                    for (int i=0;i<cartList.size();i++){
                        if (cartList.get(i).isOnSale()){
                            cartList.get(i).setChecked(false);
                        }

                    }
                    zong="0";
                    shopCarAdapter.isAll(false);
                    checkbox.setSelected(false);
                    isAll = false;
                    count=0;
                    getZong();
                    shop_count.setText("结算"+"(0)");

                }
                shopCarAdapter.notifyDataSetChanged();

            }
        });
        shopCarAdapter.setOnAddClickListener(new ShopCarAdapter.OnAddClickListener() {
            @Override
            public void onShareClick(int position) {
                cartList.remove(position);
                getZong();
                isAll=true;
                for (ShopCarGoodsBean.CartListBean cartListBean : cartList){
                    if (cartListBean.isOnSale()){
                        if (!cartListBean.isChecked()){
                            isAll=false;
                        }
                    }
                }
                if (isAll){
                    checkbox.setSelected(true);

                }else{
                    checkbox.setSelected(false);
                }
                if (cartList.size()==0||cartList==null){
                    ll_di.setVisibility(View.GONE);
                    dateNullView.setVisibility(View.VISIBLE);
                }else{
                    ll_di.setVisibility(View.VISIBLE);
                    dateNullView.setVisibility(View.GONE);
                }



            }

            @Override
            public void onCheckNum(int position, boolean ischeck) {
                if (ischeck){
                    cartList.get(position).setChecked(true);
                    getZong();


                }else{
                    cartList.get(position).setChecked(false);
                    getZong();
                }

                isAll=true;
                for (ShopCarGoodsBean.CartListBean cartListBean : cartList){
                    if (cartListBean.isOnSale()){
                        if (!cartListBean.isChecked()){
                            isAll=false;
                        }
                    }
                }
               if (isAll){
                   checkbox.setSelected(true);

               }else{
                   checkbox.setSelected(false);
               }




            }


        });



        shop_count.setOnClickListener(new View.OnClickListener() {//结算
            @Override
            public void onClick(View v) {
                getCheck();
                Log.e("sssss",result3+"");
                if (cartList2==null||cartList2.size()==0){
                    ToastUtils.showShort("请先勾选一个商品");
                }else{
                    ConfirmOrderActivity2.start(ShopCarActivity.this,getnum+"",cartList2);
                }

            }
        });

    }





    private void reSect(){//刷新重置
        shopCarAdapter.isAll(false);
        shop_count.setText("结算"+"(0)");
        shop_price.setText("¥0");
        checkbox.setSelected(false);
        isAll=false;

    }
    private void getCheck(){//选中商品
        cartList2.clear();
        for (int i=0;i<cartList.size();i++){
            if (cartList.get(i).isOnSale()&&cartList.get(i).isChecked()){
               cartList2.add(cartList.get(i));
            }
        }
    }

private void getZong(){//得到总价
         int  count=0;
      result3=new BigDecimal(zong);
    BigDecimal result2=null;
        for (int i=0;i<cartList.size();i++){
            if (cartList.get(i).isOnSale()){
                if (cartList.get(i).isChecked()){
                    String number = cartList.get(i).getNumber();
                    String price = cartList.get(i).getPrice();

                    BigDecimal num1 = new BigDecimal(number);
                    BigDecimal num2 = new BigDecimal(price);
                    BigDecimal result = num1.multiply(num2);//乘
                    result3 = result3.add(result);//加
                    count++;
                }
            }

        }

    if (result3!=null){
        String s = String.valueOf(result3);
          getnum = MathUtils.getnum(s);
        shop_price.setText("¥"+ getnum);

    }

    shop_count.setText("结算"+"("+count+")");
}



    private void getData() {
        shopCarNGoods(this)
                .subscribe(new DataObserver<ShopCarGoodsBean>(false) {
                    @Override
                    protected void onDataListEmpty() {
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    protected void onDataNull() {
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    protected void onSuccess(ShopCarGoodsBean data) {
                        refreshLayout.finishRefresh();
                        total = data.getCartTotal();
                        cartList = data.getCartList();
                        Log.e("ggggg", cartList.size() + "");
                        if (cartList.size() != 0) {
                            refreshLayout.setVisibility(View.VISIBLE);
                            dateNullView.setVisibility(View.GONE);
                            ll_botton.setVisibility(View.VISIBLE);
                            shopCarAdapter.addData(data.getCartList());
                           reSect();
                        } else {
                            refreshLayout.setVisibility(View.GONE);
                            dateNullView.setVisibility(View.VISIBLE);
                            ll_botton.setVisibility(View.GONE);
                        }


                    }
                });
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        if (event.getAction().equals(EventBusAction.SHOPCAR_REFRSH)) {
            getData();

        }
    }

    //购物车商品
    public Observable<BaseResponse<ShopCarGoodsBean>> shopCarNGoods(RxAppCompatActivity fragment) {

        return RxHttp.getInstance().getSysteService().getShopCarIndex()
                .compose(RxUtils.<BaseResponse<ShopCarGoodsBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<ShopCarGoodsBean>>bindToLifecycle());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
