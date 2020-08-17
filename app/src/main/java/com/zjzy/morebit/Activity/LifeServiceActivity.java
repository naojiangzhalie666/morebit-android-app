package com.zjzy.morebit.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.MainActivity;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.LifeAdapter;
import com.zjzy.morebit.adapter.RetailersAdapter;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ConsComGoodsInfo;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.request.RequestGoodsOrderBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.helper.ActivityLifeHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

public class LifeServiceActivity extends BaseActivity implements View.OnClickListener {
    private TextView txt_head_title;
    private LinearLayout btn_back;
    private RecyclerView mReUseListView;
    private SmartRefreshLayout refreshLayout;
    private int page = 1;
    private LifeAdapter mAdapter;
    private int order_type=5;//全部  1待返佣  2已到账
    private int teamType=7;//全部订单
    private LinearLayout dateNullView;
    private TextView btn_invite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_service);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .init();
        initView();
    }

    private void initView() {

        String title = getIntent().getStringExtra(C.Extras.ELEORDER);
        teamType = getIntent().getIntExtra(C.Extras.ELETYPE, 7);
        txt_head_title = (TextView) findViewById(R.id.txt_head_title);
        if (!TextUtils.isEmpty(title)){
            txt_head_title.setText(title);
        }
        txt_head_title.setTextSize(18);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);


        mReUseListView= (RecyclerView) findViewById(R.id.mListView);
        refreshLayout= (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        dateNullView= (LinearLayout) findViewById(R.id.dateNullView);
        mAdapter = new LifeAdapter(this,teamType);
        mReUseListView.setAdapter(mAdapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                refreshData();
            }
        });
       refreshLayout.setOnLoadMoreListener(new com.scwang.smartrefresh.layout.listener.OnLoadMoreListener() {
           @Override
           public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
               page++;
               getData(teamType);
           }
       });

        getData(teamType);
        btn_invite= (TextView) findViewById(R.id.btn_invite);
        btn_invite.setOnClickListener(this);


    }

    private void refreshData() {

        page = 1;
        getData(teamType);
        refreshLayout.finishRefresh();
    }

    private void getData(int teamType) {
        getGoodsOrder(this, order_type,  teamType,page)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new DataObserver<List<ConsComGoodsInfo>>() {
            @Override
            protected void onDataListEmpty() {
                if (page==1){
                    mReUseListView.setVisibility(View.GONE);
                    dateNullView.setVisibility(View.VISIBLE);
                }
                refreshLayout.finishLoadMore();
            }

            @Override
            protected void onSuccess(List<ConsComGoodsInfo> data) {

                if (data!=null&&data.size()!=0){
                    dateNullView.setVisibility(View.GONE);
                    mReUseListView.setVisibility(View.VISIBLE);
                    if (page==1){
                        mAdapter.setData(data);
                    }else{
                        mAdapter.addData(data);
                       refreshLayout.finishLoadMore();
                    }

                }else{
                    dateNullView.setVisibility(View.VISIBLE);
                    mReUseListView.setVisibility(View.GONE);
                   refreshLayout.finishLoadMore();
                }


            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_invite://跳转首页
                ActivityLifeHelper.getInstance().finishActivity(MainActivity.class);
                EventBus.getDefault().post(new MessageEvent(EventBusAction.ACTION_HOME));
                break;
        }
    }


    /**
     * 获取订单列表
     *
     * @param rxFragment
     * @param orderType
     */
    public Observable<BaseResponse<List<ConsComGoodsInfo>>> getGoodsOrder(RxAppCompatActivity rxFragment, int orderType, int teamType, int page) {
        RequestGoodsOrderBean requestBean = new RequestGoodsOrderBean();
        requestBean.setOrderStatus(orderType);
        requestBean.setType(teamType);
        requestBean.setPage(page);

        return RxHttp.getInstance().getUsersService()
                .getGoodsOrder(requestBean)
                .compose(RxUtils.<BaseResponse<List<ConsComGoodsInfo>>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<List<ConsComGoodsInfo>>>bindToLifecycle());
    }

}
