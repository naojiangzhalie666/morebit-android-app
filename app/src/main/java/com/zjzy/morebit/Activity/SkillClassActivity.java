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
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
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
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;
import com.zjzy.morebit.view.AspectRatioView;

import java.util.List;

import io.reactivex.Observable;

public class SkillClassActivity extends BaseActivity {

    private TextView txt_head_title;
    private Banner banner;
    private AspectRatioView rsv_banner;
    private RecyclerView rcy;
    private int page=1;
    private SkillClassAdapter skillAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SmartRefreshLayout mSwipeList;
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
         txt_head_title = (TextView) findViewById(R.id.txt_head_title);
        txt_head_title.setText("技能课堂");
        txt_head_title.setTextSize(18);
         banner = (Banner) findViewById(R.id.banner);
        rsv_banner= (AspectRatioView) findViewById(R.id.rsv_banner);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeList= (SmartRefreshLayout) findViewById(R.id.swipeList);
        mSwipeList.setEnableRefresh(false);
        rcy= (RecyclerView) findViewById(R.id.rcy);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        rcy.setLayoutManager(manager);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setNestedScrollingEnabled(true);
        //设置进度View下拉的起始点和结束点，scale 是指设置是否需要放大或者缩小动画
        swipeRefreshLayout.setProgressViewOffset(true, -0, 100);
        //设置进度View下拉的结束点，scale 是指设置是否需要放大或者缩小动画
        swipeRefreshLayout.setProgressViewEndTarget(true, 180);
        //设置进度View的组合颜色，在手指上下滑时使用第一个颜色，在刷新中，会一个个颜色进行切换
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF645B"));
        //设置触发刷新的距离
        swipeRefreshLayout.setDistanceToTriggerSync(200);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                initBanner();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        mSwipeList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initData();
            }
        });

    rcy.setNestedScrollingEnabled(false);

    }

    /**
     * 获取技能课程
     *
     * @param rxActivity
     * @return
     */
    public Observable<BaseResponse<List<Article>>> getSkillClass(SkillClassActivity rxActivity, int page) {
        RequestBannerBean requestBean = new RequestBannerBean();
        requestBean.setOs(1);
        requestBean.setRows(10);
        requestBean.setPage(page);

        return RxHttp.getInstance().getGoodsService().getVipSkillClass(requestBean)
                .compose(RxUtils.<BaseResponse<List<Article>>>switchSchedulers())
                .compose(rxActivity.<BaseResponse<List<Article>>>bindToLifecycle());
    }
    private void initBanner() {
        RequestBannerBean requestBean = new RequestBannerBean();
        requestBean.setType(20);
        requestBean.setOs(1);
        RxHttp.getInstance().getCommonService().getBanner(requestBean)//获取banner
                .compose(RxUtils.<BaseResponse<List<ImageInfo>>>switchSchedulers())
                .compose(this.<BaseResponse<List<ImageInfo>>>bindToLifecycle())
                .subscribe(new DataObserver<List<ImageInfo>>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
//                        ar_title_banner.setVisibility(View.GONE);
//                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    protected void onSuccess(List<ImageInfo> data) {
                     //   swipeRefreshLayout.setRefreshing(false);

                        if (data!=null){
                            rsv_banner.setVisibility(View.VISIBLE);
                            BannerInitiateUtils.setBrandBanner(SkillClassActivity.this, data, banner);
                        }else{
                            rsv_banner.setVisibility(View.GONE);
                        }

                    }

                });

        initData();


    }

    private void initData() {
        getSkillClass(this,page).compose(RxUtils.<BaseResponse<List<Article>>>switchSchedulers())
                .compose(this.<BaseResponse<List<Article>>>bindToLifecycle())
                .subscribe(new DataObserver<List<Article>>() {
                    @Override
                    protected void onSuccess(List<Article> data) {
                        if (data!=null){
                            if (page==1){
                                skillAdapter=new SkillClassAdapter(SkillClassActivity.this,data);
                                if (skillAdapter!=null){
                                    rcy.setAdapter(skillAdapter);
                                }
                            }else{
                                skillAdapter.setData(data);
                                mSwipeList.finishLoadMore(true);
                            }


                        }
                    }
                });
    }

}
