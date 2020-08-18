package com.zjzy.morebit.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
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
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.GoodsDialyAdapter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.MarkermallCircleInfo;
import com.zjzy.morebit.pojo.request.RequestMarkermallCircleBean;
import com.zjzy.morebit.utils.C;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

//发圈类目内容
public class CircleActivity extends BaseActivity implements View.OnClickListener {
    private TextView txt_head_title;
    private LinearLayout btn_back;
    private  String oneLevelId,type,title,twoLevelId;
    private RecyclerView mListView;
    private int page=1;
    private GoodsDialyAdapter goodsDialyAdapter;
    private LinearLayout dateNullView;
    private String circletype;
    private String threeLevelId;
    private SmartRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .init();
        initView();
        initData();
    }


    private void initView() {
        oneLevelId = getIntent().getStringExtra(C.Circle.CIRCLE_ONEID);
        type = getIntent().getStringExtra(C.Circle.CIRCLE_TYPE);
        title = getIntent().getStringExtra(C.Circle.CIRCLE_FUTITLE);
        twoLevelId = getIntent().getStringExtra(C.Circle.CIRCLE_TWOID);
        circletype=  getIntent().getStringExtra(C.Circle.CIRCLEFRAGMENT);
        threeLevelId=getIntent().getStringExtra(C.Circle.CIRCLE_THREEID);
        txt_head_title = (TextView) findViewById(R.id.txt_head_title);
        if (!TextUtils.isEmpty(title)){
            txt_head_title.setText(title);
        }
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        mListView= (RecyclerView) findViewById(R.id.listview);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        dateNullView= (LinearLayout) findViewById(R.id.dateNullView);
          goodsDialyAdapter=new GoodsDialyAdapter(this, Integer.parseInt(circletype));
        LinearLayoutManager manager=new LinearLayoutManager(this);
        mListView.setLayoutManager(manager);
        mListView.setAdapter(goodsDialyAdapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                initData();
                refreshLayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadMoreListener(new com.scwang.smartrefresh.layout.listener.OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initData();
            }
        });


    }


    private void initData() {
        getMarkermallCircle()
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {


                    }
                })
                .subscribe(new DataObserver<List<MarkermallCircleInfo>>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
//                        super.onError(errorMsg, errCode);
                        showError(errCode, errorMsg);
                    }

                    @Override
                    protected void onDataListEmpty() {
                        if (page==1){
                            mListView.setVisibility(View.GONE);
                            dateNullView.setVisibility(View.VISIBLE);
                        }
                       refreshLayout.finishLoadMore();
                    }

                    @Override
                    protected void onSuccess(List<MarkermallCircleInfo> data) {
                        showSuccessful(data);
                    }
                });

    }

    private void showSuccessful(List<MarkermallCircleInfo> data) {
        if (data!=null&&data.size()!=0){
            dateNullView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            if (page==1){
                goodsDialyAdapter.setData(data);
            }else{
                goodsDialyAdapter.addData(data);
               refreshLayout.finishLoadMore();
            }

        }else{
            dateNullView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            refreshLayout.finishLoadMore();
        }


    }

    private void showError(String errCode, String errorMsg) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }


    //获取发圈数据
    public Observable<BaseResponse<List<MarkermallCircleInfo>>> getMarkermallCircle() {
        RequestMarkermallCircleBean requestBean = new RequestMarkermallCircleBean();
        requestBean.setPage(page);
        requestBean.setOneLevelId(oneLevelId);
        requestBean.setTwoLevelId(twoLevelId);
        requestBean.setType(Integer.parseInt(type));
        requestBean.setThreeLevelId(threeLevelId);


        return RxHttp.getInstance().getGoodsService().getMarkermallCircle(requestBean)
                .compose(RxUtils.<BaseResponse<List<MarkermallCircleInfo>>>switchSchedulers())
                .compose(this.<BaseResponse<List<MarkermallCircleInfo>>>bindToLifecycle());
    }


}
