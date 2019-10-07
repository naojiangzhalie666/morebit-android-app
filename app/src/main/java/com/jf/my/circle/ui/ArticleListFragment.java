package com.jf.my.circle.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.jf.my.Module.common.View.ReUseListView;
import com.jf.my.R;
import com.jf.my.circle.adapter.ArticleAdapter;
import com.jf.my.pojo.Article;
import com.jf.my.pojo.ArticleBody;
import com.jf.my.circle.contract.ArticleContract;
import com.jf.my.circle.presenter.ArticlePresenter;
import com.jf.my.contact.EventBusAction;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.mvp.base.frame.MvpFragment;
import com.jf.my.network.CommonEmpty;
import com.jf.my.pojo.MessageEvent;
import com.jf.my.utils.OpenFragmentUtils;
import com.jf.my.view.ToolbarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by YangBoTian on 2018/12/24.
 * 商学院
 */

public class ArticleListFragment extends MvpFragment<ArticlePresenter> implements ArticleContract.View {
    private static final int REQUEST_COUNT = 10;
    @BindView(R.id.mListView)
    ReUseListView mReUseListView;

    private int page = 1;
    private ArticleAdapter mArticleAdapter;
    private String mId;
    private String mTitle = "";
    private CommonEmpty mEmptyView;

    public static void start(Context context, String modular_id, String title) {
        Bundle args = new Bundle();
        args.putString("modular_id", modular_id);
        args.putString("title", title);
        OpenFragmentUtils.goToSimpleFragment(context, ArticleListFragment.class.getName(), args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        refreshData(true);
    }

    private void refreshData(boolean isRefresh) {
        if (isRefresh) {
            page = 1;
            mReUseListView.getListView().setNoMore(false);
            mEmptyView.setmFirstComplete(false);
        }
        ArticleBody articleBody = new ArticleBody();
        articleBody.setPage(page);
        articleBody.setModelId(mId);

        mPresenter.getArticleList(this, articleBody, mEmptyView);
    }

    @Override
    protected void initView(View view) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mId = bundle.getString("modular_id");
            mTitle = bundle.getString("title");
        }
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(mTitle);
        mEmptyView = new CommonEmpty(view, "还没有文章看哦~", R.drawable.image_meiyoushoucang);
        mReUseListView.getSwipeList().setOnRefreshListener(new com.jf.my.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData(true);
            }
        });
        mArticleAdapter = new ArticleAdapter(getActivity(),this);
        mReUseListView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                refreshData(false);
            }
        });
        mReUseListView.setAdapter(mArticleAdapter);

    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_article_list;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    public void onArticleSuccessful(List<Article> data) {
        if (page == 1) {
           mArticleAdapter.replace(data);
        } else {
            if (data.size() == 0) {
                mReUseListView.getListView().setNoMore(true);
            } else {
                mArticleAdapter.add(data);
            }
        }
        page++;
        mReUseListView.notifyDataSetChanged();
    }

    @Override
    public void onArticleEmpty() {
        if (page == 1) {
            mArticleAdapter.clear();
            mReUseListView.notifyDataSetChanged();
        } else {
            mReUseListView.getListView().setNoMore(true);
        }
    }


    @Override
    public void onArticleFinally() {
        mReUseListView.getSwipeList().setRefreshing(false);
        mReUseListView.getListView().refreshComplete(REQUEST_COUNT);
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        switch (event.getAction()) {
            case EventBusAction.LOGINA_SUCCEED:
                refreshData(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
