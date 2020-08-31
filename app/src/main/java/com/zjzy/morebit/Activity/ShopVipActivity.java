package com.zjzy.morebit.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.SubNumberAdapter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShopCarNumBean;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.number.NumberGoodsList;
import com.zjzy.morebit.pojo.requestbodybean.RequestNumberGoodsList;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.view.HorzProgressView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/*
* 我的VIP页面
*
* */
public class ShopVipActivity extends BaseActivity implements View.OnClickListener {
    SubNumberAdapter mAdapter;
    private RecyclerView rcy_shopmall;
    private List<NumberGoods> list;
    private int page=1;
    private LinearLayout btn_back;
    private SmartRefreshLayout refreshLayout;
    private RelativeLayout shop_car;
    private ImageView top_rcy;
    private   LinearLayoutManager manager;
    private TextView shopnum;
    private RoundedImageView vip_tou;
    private TextView vip_name,tv_coin,tv_morcion;
    private HorzProgressView horzProgressView;
    private NestedScrollView netscroll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_vip);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(false)
                .statusBarColor(R.color.color_F05557)
                .init();
        initView();
        initData();
    }

    private void initData() {
        getNumberGoodsListPresenter(this, page);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getShopCarNum();
        initViewData();
    }

    private void initViewData() {
        UserInfo info = UserLocalData.getUser(this);
        if (info != null) {
            if ("null".equals(info.getHeadImg()) || "NULL".equals(info.getHeadImg()) || TextUtils.isEmpty(info.getHeadImg())) {
                vip_tou.setImageResource(R.drawable.head_icon);
            } else {
                LoadImgUtils.setImgCircle(this, vip_tou, info.getHeadImg(), R.drawable.head_icon);
            }
            vip_name.setText(info.getNickName());
            horzProgressView.setMax(360.00);
            Long coin = info.getMoreCoin();
            String coin1;
            if (coin != null) {
                horzProgressView.setCurrentNum(info.getMoreCoin());
                coin1 = info.getMoreCoin() + "";
            } else {
                horzProgressView.setCurrentNum(0);
                coin1 = "0";
                return;
            }
            tv_coin.setText(coin1+"/360");
            Long mcoin=360-coin;
            if (mcoin>0){
                tv_morcion.setText("您还需"+mcoin+"成长值才能升级掌柜");
            }else{
                tv_morcion.setText("您还需0成长值才能升级掌柜");
            }

        }

    }

    private void initView() {
        TextView txt_head_title = (TextView) findViewById(R.id.txt_head_title);
        txt_head_title.setTextSize(18);
        txt_head_title.setText("我的VIP");
        txt_head_title.setTextColor(Color.WHITE);
        vip_tou= (RoundedImageView) findViewById(R.id.vip_tou);
        vip_name= (TextView) findViewById(R.id.vip_name);
        tv_coin= (TextView) findViewById(R.id.tv_coin);
        tv_morcion= (TextView) findViewById(R.id.tv_morcion);
        horzProgressView= (HorzProgressView) findViewById(R.id.horzProgressView);
        shop_car= (RelativeLayout) findViewById(R.id.shop_car);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        top_rcy= (ImageView) findViewById(R.id.top_rcy);
        top_rcy.setOnClickListener(this);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        shop_car.setOnClickListener(this);
        rcy_shopmall= (RecyclerView) findViewById(R.id.rcy_shopmall);
        refreshLayout= (SmartRefreshLayout) findViewById(R.id.refreshLayout);
          manager = new LinearLayoutManager(this);
        rcy_shopmall.setLayoutManager(manager);
        shopnum= (TextView) findViewById(R.id.shopnum);
        netscroll= (NestedScrollView) findViewById(R.id.netscroll);



        mAdapter=new SubNumberAdapter(ShopVipActivity.this,0);
        rcy_shopmall.setAdapter(mAdapter);

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                page++;
                initData();

            }
        });


        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                initData();
                refreshLayout.finishRefresh(true);//刷新完成
            }
        });

    }

    private void getShopCarNum() {

        shopCarNum(this)
                .subscribe(new DataObserver<ShopCarNumBean>(false) {
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
                    protected void onSuccess(ShopCarNumBean data) {
                        int goodsNum = data.getGoodsNum();
                        if (goodsNum!=0){
                            shopnum.setVisibility(View.VISIBLE);
                            if (goodsNum>99){
                                shopnum.setText("99+");
                            }else{
                                shopnum.setText(goodsNum+"");
                            }
                        }else{
                            shopnum.setVisibility(View.GONE);
                        }

                    }
                });
    }

    private void onActivityFailure() {

    }

    public Observable<BaseResponse<NumberGoodsList>> getNumberGoodsList(BaseActivity activity, int page) {
        RequestNumberGoodsList bean = new RequestNumberGoodsList();
        bean.setPage(page);
        bean.setLimit(10);
        return RxHttp.getInstance().getGoodsService().getNumberGoodsList(bean)
                .compose(RxUtils.<BaseResponse<NumberGoodsList>>switchSchedulers())
                .compose(activity.<BaseResponse<NumberGoodsList>>bindToLifecycle());
    }

    /**
     * 商品列表
     * @param activity
     * @param page
     */
    public void getNumberGoodsListPresenter(BaseActivity activity, final int page) {
        getNumberGoodsList(activity, page)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                       // showFinally();

                    }
                })
                .subscribe(new DataObserver<NumberGoodsList>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
//                        super.onError(errorMsg, errCode);
                        showError(errCode,errorMsg);
                    }

                    @Override
                    protected void onDataListEmpty() {
                        //                if (mReUseListView.getSwipeList() != null) {
                        //                    mReUseListView.getSwipeList().setRefreshing(false);
                        //                }

                    }
                    @Override
                    protected void onSuccess(NumberGoodsList data) {
                        list = data.getList();

                     if (page==1){
                         mAdapter.addData(list);
                     }else{
                         mAdapter.setData(list);
                         refreshLayout.finishLoadMore(true);
                     }
                    }
                });
    }

    public void showError(String errorNo,String msg) {
        MyLog.i("test", "onFailure: " + this);
        if ("B1100007".equals(errorNo)
                ||"B1100008".equals(errorNo)
                ||"B1100009".equals(errorNo)
                || "B1100010".equals(errorNo)) {
            ViewShowUtils.showShortToast(this,msg);
        }
       // if (page != 1)
        //    mReUseGridView.getListView().setNoMore(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shop_car://购物车
                startActivity(new Intent(this, ShopCarActivity.class));
                break;
            case R.id.top_rcy://一键置顶
                netscroll.scrollTo(0,0);
                break;
        }
    }

    //购物车数量
    public Observable<BaseResponse<ShopCarNumBean>> shopCarNum(RxAppCompatActivity fragment) {

        return RxHttp.getInstance().getSysteService().getShopCarNum()
                .compose(RxUtils.<BaseResponse<ShopCarNumBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<ShopCarNumBean>>bindToLifecycle());
    }
}
