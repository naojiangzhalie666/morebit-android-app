package com.zjzy.morebit.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.youth.banner.Banner;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.SkillAdapter;
import com.zjzy.morebit.adapter.SkillClassAdapter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.request.RequestBannerBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestTwoLevel;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;
import com.zjzy.morebit.view.AspectRatioView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class SkillClassActivity extends BaseActivity {

    private TextView txt_head_title;

    private RecyclerView rcy;
    private int page=1;
    private  String id;
    private SkillClassAdapter skillAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SmartRefreshLayout mSwipeList;
    private LinearLayout btn_back;
    private List<Article> list=new ArrayList<>();
    private LinearLayout searchNullTips_ly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_class);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(false)
                .statusBarColor(R.color.white)
                .init();

         initView();
        initBanner();


    }

    private void initView() {

        Intent intent = getIntent();
        id = intent.getStringExtra(C.Vip.SKILLID);
        String name = intent.getStringExtra(C.Vip.SKILLNAME);

        txt_head_title = (TextView) findViewById(R.id.txt_head_title);
        txt_head_title.setText(name+"");
        txt_head_title.setTextSize(18);


        mSwipeList= (SmartRefreshLayout) findViewById(R.id.swipeList);
        searchNullTips_ly= (LinearLayout) findViewById(R.id.searchNullTips_ly);
        skillAdapter=new SkillClassAdapter(this);
        rcy= (RecyclerView) findViewById(R.id.rcy);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        rcy.setLayoutManager(manager);
        rcy.setAdapter(skillAdapter);


        mSwipeList.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                initBanner();
                mSwipeList.finishRefresh();
            }
        });
        mSwipeList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initData();
            }
        });

        btn_back= (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void initBanner() {


        initData();


    }

    private void initData() {
        getArticleList(this,id,page)
                .subscribe(new DataObserver<List<Article>>() {

                    @Override
                    protected void onSuccess(List<Article> data) {
                        onTutorialDataSuccessful(data);
                    }

                    @Override
                    protected void onDataListEmpty() {
                        super.onDataListEmpty();
                        onTutorialDataEmpty();
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        super.onError(errorMsg, errCode);
                       onTutorialDataEmpty();
                    }
                });
    }

    private void onTutorialDataEmpty() {
        mSwipeList.finishLoadMore();
        if (page==1){
            searchNullTips_ly.setVisibility(View.VISIBLE);
            rcy.setVisibility(View.GONE);
        }else{
            rcy.setVisibility(View.VISIBLE);
            searchNullTips_ly.setVisibility(View.GONE);
        }

    }

    private void onTutorialDataSuccessful(List<Article> data) {
        if (data == null || (data != null && data.size() == 0)) {
            searchNullTips_ly.setVisibility(View.VISIBLE);
            rcy.setVisibility(View.GONE);
        }else{
            if (page==1){
                searchNullTips_ly.setVisibility(View.GONE);
                rcy.setVisibility(View.VISIBLE);
                skillAdapter.setRefreshData(data);
            }else{
                skillAdapter.setData(data);
                mSwipeList.finishLoadMore();
            }
        }

    }


    /**
     * 获取商学院教程   获取技能课程
     *
     * @param fragment
     * @return
     */
    public Observable<BaseResponse<List<Article>>> getArticleList(RxAppCompatActivity fragment, String mid, int page) {
        RequestTwoLevel requestTwoLevel=new RequestTwoLevel();
        requestTwoLevel.setModelId(mid);
        requestTwoLevel.setTwoLevel(1);
        requestTwoLevel.setPage(page);
        return RxHttp.getInstance().getCommonService().getArticleList(requestTwoLevel)
                .compose(RxUtils.<BaseResponse<List<Article>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<Article>>>bindToLifecycle());
    }


}
