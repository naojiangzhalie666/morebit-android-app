package com.markermall.cat.circle.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.gyf.barlibrary.ImmersionBar;
import com.markermall.cat.Module.common.View.ReUseListView;
import com.markermall.cat.R;
import com.markermall.cat.circle.adapter.ArticleAdapter;
import com.markermall.cat.circle.adapter.ReviewArticleAdapter;
import com.markermall.cat.circle.contract.RecommendContract;
import com.markermall.cat.circle.presenter.RecommendPresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.mvp.base.frame.MvpActivity;
import com.markermall.cat.network.CommonEmpty;
import com.markermall.cat.pojo.Article;
import com.markermall.cat.pojo.request.RequestListBody;
import com.markermall.cat.utils.ActivityStyleUtil;
import com.markermall.cat.view.ToolbarHelper;

import java.util.List;

import butterknife.BindView;

/**
 * Created by JerryHo on 2019/3/15
 * Description: 商学院列表界面
 */
public class RecommendListActivity extends MvpActivity<RecommendPresenter> implements RecommendContract.View {
    public static final int ARTICLE_RECOMMEND= 1;   //更多推荐
    public static final int ARTICLE_REVIEW = 2;    //预览
    private static final int REQUEST_COUNT = 10;
    @BindView(R.id.mListView)
    ReUseListView mReUseListView;
    @BindView(R.id.status_bar)
    View status_bar;
    private int page;
    private ReviewArticleAdapter mReviewArticleAdapter;
    private ArticleAdapter mArticleAdapter;
    private CommonEmpty mEmptyView;
    private int type=ARTICLE_RECOMMEND;
    public static void start(Activity activity,int type) {
        Intent intent = new Intent(activity, RecommendListActivity.class);
        intent.putExtra("type",type);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStatusBarWhite();
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .init();
        type = getIntent().getIntExtra("type",ARTICLE_RECOMMEND);
       initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //处理全屏显示
            ViewGroup.LayoutParams viewParams = status_bar.getLayoutParams();
            viewParams.height = ActivityStyleUtil.getStatusBarHeight(this);
            status_bar.setLayoutParams(viewParams);

        }
        if(type==ARTICLE_RECOMMEND){
            new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(getString(R.string.college_article_recommend));
        } else {
            new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(getString(R.string.college_article_review));
        }

        mEmptyView = new CommonEmpty(this, getString(R.string.data_null), R.drawable.image_meiyousousuojilu);
        mReUseListView.getSwipeList().setOnRefreshListener(new com.markermall.cat.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(true);
            }
        });

        mReUseListView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                refreshData(false);

            }
        });
        if(type==ARTICLE_RECOMMEND){
             mArticleAdapter = new ArticleAdapter(this,this);
            mArticleAdapter.setType(ArticleAdapter.TYPE_HOTIZONTAL);
            mArticleAdapter.setStudyView(true);
            mReUseListView.setAdapter(mArticleAdapter);

        } else {
            mReviewArticleAdapter = new ReviewArticleAdapter(this);
            mReviewArticleAdapter.setType(type);
            mReUseListView.setAdapter(mReviewArticleAdapter);
        }

        refreshData(true);
    }


    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_recommend_article;
    }




    @Override
    public void onListSuccessful(List<Article> data) {
        if(type==ARTICLE_RECOMMEND){
            if (page == 1) {
                mArticleAdapter.replace(data);
            } else {
                if (data.size() == 0) {
                    mReUseListView.getListView().setNoMore(true);
                } else {
                    mArticleAdapter.add(data);
                }
            }
            mArticleAdapter.notifyDataSetChanged();
        } else {
            if (page == 1) {
                mReviewArticleAdapter.replace(data);
            } else {
                if (data.size() == 0) {
                    mReUseListView.getListView().setNoMore(true);
                } else {
                    mReviewArticleAdapter.add(data);
                }
            }
            mReviewArticleAdapter.notifyDataSetChanged();
        }

        page++;

    }
    @Override
    public void onListEmpty() {

    }

    @Override
    public void onListFinally() {
        mReUseListView.getSwipeList().setRefreshing(false);
        mReUseListView.getListView().refreshComplete(REQUEST_COUNT);
    }
    private void refreshData(boolean isRefresh) {
        if (isRefresh) {
            page = 1;
            mReUseListView.getListView().setNoMore(false);

        }
        RequestListBody pageBean = new RequestListBody();
        pageBean.setPage(page);
        mPresenter.getRecommendMoreList(this, pageBean,mEmptyView,type);
    }





}
