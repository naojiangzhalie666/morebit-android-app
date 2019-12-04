package com.zjzy.morebit.circle.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.Module.push.Logger;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.adapter.ArticleAdapter;
import com.zjzy.morebit.circle.contract.TutorialContract;
import com.zjzy.morebit.circle.presenter.TutorialPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.requestbodybean.RequestTwoLevel;
import com.zjzy.morebit.utils.MyLog;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.List;

import butterknife.BindView;

/**
 * Created by JerryHo on 2019/3/15
 * Description: 教程Fragment
 */
public class TutorialFragment extends MvpFragment<TutorialPresenter> implements TutorialContract.View {
    public static final int ALL = 1;
    @BindView(R.id.recyclerview)
    ReUseListView mReUseListView;
    @BindView(R.id.dataNullView)
    LinearLayout mDataNullView;

    private int page = 1;
    private ArticleAdapter mTutorialAdapter;
    private int mId;
    private int mType = 0;

    public static TutorialFragment newInstance(int id, int type) {
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putInt("type", type);
        TutorialFragment fragment = new TutorialFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mId = bundle.getInt("id");
            mType = bundle.getInt("type", 0);
        }
    }


    @Override
    public void onTutorialDataSuccessful(List<Article> data) {
        Logger.e("==onTutorialDataSuccessful==");
        mReUseListView.getListView().refreshComplete(10);
        if (page == 1) {
            mTutorialAdapter.replace(data);
        } else {
            mDataNullView.setVisibility(View.GONE);
            mReUseListView.setVisibility(View.VISIBLE);
            if (data.size() == 0) {
                mReUseListView.getListView().setNoMore(true);
            } else {
                mTutorialAdapter.add(data);
            }
        }
        page++;
        mReUseListView.notifyDataSetChanged();

    }

    @Override
    public void onTutorialDataEmpty() {
        MyLog.i("test","refreshComplete");
        if (page == 1) {
            mDataNullView.setVisibility(View.VISIBLE);
            mReUseListView.setVisibility(View.GONE);

        }
        mReUseListView.getListView().setNoMore(true);
    }

    @Override
    public void onTutorialFinally() {
        Logger.e("==onTutorialFinally==");
        MyLog.i("test","refreshComplete");
        mReUseListView.getSwipeList().setRefreshing(false);

        mReUseListView.getListView().refreshComplete(10);
        //下拉刷新或者加载更多的时候停止播放
//        MyVideoPlayerManager.instance().releaseMyVideoPlayerPlayer();
    }


    @Override
    protected void initData() {
        initBundle();
        getTutorialData();

    }

    public void getTutorialData() {
        RequestTwoLevel requestTwoLevel = new RequestTwoLevel();
        requestTwoLevel.setTwoLevel(mId);
        requestTwoLevel.setModelId(mId+"");
        requestTwoLevel.setPage(page);
        mPresenter.getTutorialData(this, requestTwoLevel, mType);

    }


    @Override
    protected void initView(View view) {
        mTutorialAdapter = new ArticleAdapter(getActivity(), this);
        ;

        mReUseListView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                mReUseListView.getListView().setNoMore(false);
                mReUseListView.getSwipeList().setRefreshing(true);
                GSYVideoManager.releaseAllVideos();
                getTutorialData();
            }
        });
        mReUseListView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mReUseListView.getSwipeList().isRefreshing()) {
                    getTutorialData();
                }
            }
        });
        mTutorialAdapter.setStudyView(true);
        mReUseListView.setAdapter(mTutorialAdapter);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_tutorial;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

}
