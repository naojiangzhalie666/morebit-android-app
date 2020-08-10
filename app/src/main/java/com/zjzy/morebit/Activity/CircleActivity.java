package com.zjzy.morebit.Activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.gyf.barlibrary.ImmersionBar;
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
    private ReUseListView mListView;
    private int page=1;
    private GoodsDialyAdapter goodsDialyAdapter;
    private LinearLayout dateNullView;
    private String circletype;
    private String threeLevelId;
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
        mListView= (ReUseListView) findViewById(R.id.mListView);
        dateNullView= (LinearLayout) findViewById(R.id.dateNullView);
          goodsDialyAdapter=new GoodsDialyAdapter(this, Integer.parseInt(circletype));
        LinearLayoutManager manager=new LinearLayoutManager(this);
        mListView.setLayoutManager(manager);
        mListView.setAdapter(goodsDialyAdapter);

        mListView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListView.getSwipeList().post(new Runnable() {

                    @Override
                    public void run() {
                        mListView.getSwipeList().setRefreshing(true);
                    }
                });
                page = 1;
                initData();
            }
        });
        mListView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mListView.getSwipeList().isRefreshing()) {
                    page++;
                    initData();
                }

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
                        mListView.getListView().setNoMore(true);
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
                mListView.getListView().setNoMore(true);
            }

        }else{
            dateNullView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            mListView.getListView().setNoMore(true);
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
