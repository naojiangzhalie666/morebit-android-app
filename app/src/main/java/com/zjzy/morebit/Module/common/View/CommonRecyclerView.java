package com.zjzy.morebit.Module.common.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zjzy.morebit.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * 通用上拉下拉加载列表-含loading效果
 */

public class CommonRecyclerView extends LinearLayout{
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private BaseQuickAdapter mAdapter;
    private ImageView stick;

    private RecyclerViewScrollListener mRecyclerViewScrollListener;
    private int mSpanCount = 2;
    private Context mContext;

    public CommonRecyclerView(@NonNull Context context) {
        super(context);
        mContext = context;
        init(context);
    }

    public CommonRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context);
    }

    public CommonRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context);
    }


    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.common_recycler_view, null);
        addView(view, new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        initView(context);
    }

    private void initView(Context context) {
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        stick = findViewById(R.id.stick);
        recyclerView.addOnScrollListener(new RecyclerViewScrollListener());
    }


    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        if (mLayoutManager != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        }

    }

    public void setLayoutManager(GridLayoutManager layoutManager, int spanCount) {
        this.mLayoutManager = layoutManager;
        this.mSpanCount = spanCount;
        if (mLayoutManager != null) {
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, mSpanCount));
        }
    }
    public void setLayoutManager(GridLayoutManager manager ) {
        if (recyclerView != null) {
            recyclerView.setLayoutManager(manager);
        }
    }


    //滑动监听
    private class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (mRecyclerViewScrollListener != null) {
                mRecyclerViewScrollListener.onScrollStateChanged(recyclerView, newState);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (mRecyclerViewScrollListener != null) {
                mRecyclerViewScrollListener.onScrolled(recyclerView, dx, dy);
            }
            LinearLayoutManager layoutManager = null;
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            }
            if (layoutManager != null) {
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition > 3) {
                    stick.setVisibility(View.VISIBLE);
                } else {
                    stick.setVisibility(View.GONE);
                }
            }

        }
    }

    public void setmRecyclerViewScrollListener(RecyclerViewScrollListener mRecyclerViewScrollListener) {
        this.mRecyclerViewScrollListener = mRecyclerViewScrollListener;
    }

    public void setAdapter(BaseQuickAdapter adapter, RefreshAndLoadMoreListener listener) {
        mAdapter = adapter;
        if (listener != null) {
            RefreshAndLoadMore refreshAndLoadMore = new RefreshAndLoadMore(listener);
            if (mAdapter != null) {
                mAdapter.setOnLoadMoreListener(refreshAndLoadMore, recyclerView);
            }
            if (refreshLayout != null) {
                refreshLayout.setOnRefreshListener(refreshAndLoadMore);
            }
        }
        recyclerView.setAdapter(mAdapter);
    }

    public RefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }


    public class RefreshAndLoadMore implements BaseQuickAdapter.RequestLoadMoreListener, OnRefreshListener {

        private final RefreshAndLoadMoreListener Listener;

        public RefreshAndLoadMore(RefreshAndLoadMoreListener listener) {
            this.Listener = listener;
        }

        @Override
        public void onLoadMoreRequested() {
            refreshLayout.finishRefresh();

            if (Listener != null) {
                Listener.onLoadMoreRequested();
            }
        }

        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            if (Listener != null) {
                Listener.onRefresh();
            }
        }
    }

    public interface RefreshAndLoadMoreListener {
        void onLoadMoreRequested();

        void onRefresh();
    }
}