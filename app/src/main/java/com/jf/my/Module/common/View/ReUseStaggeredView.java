package com.jf.my.Module.common.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jf.my.Module.common.widget.SwipeRefreshLayout;
import com.jf.my.R;
import com.jf.my.utils.HomeSpaceItemDecoration;
import com.jf.my.utils.MyLog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * 通用上拉下拉加载列表-含loading效果
 */

public class ReUseStaggeredView extends LinearLayout {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private BaseQuickAdapter mAdapter;
    private ImageView stick;


    private int mSpanCount = 2;
    private Context mContext;
    protected RecyclerView.OnScrollListener mExternalOnScrollListener;
    public ReUseStaggeredView(@NonNull Context context) {
        super(context);
        mContext = context;
        init(context);
    }

    public ReUseStaggeredView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context);
    }

    public ReUseStaggeredView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
//        GridLayoutManager layoutManager = new GridLayoutManager(mContext,2);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.addItemDecoration(new HomeSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.home_recommend_item_decoration), 2));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerViewScrollListener());
    }




    //滑动监听
    public  class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
//            ((StaggeredGridLayoutManager)recyclerView.getLayoutManager()).invalidateSpanAssignments(); //防止第一行到顶部有空
            if (mExternalOnScrollListener != null) {
                mExternalOnScrollListener.onScrollStateChanged(recyclerView, newState);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (mExternalOnScrollListener != null) {
                mExternalOnScrollListener.onScrolled(recyclerView, dx, dy);
            }
            RecyclerView.LayoutManager layoutManager = null;
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

            } else if(recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager){
                layoutManager = recyclerView.getLayoutManager();
//                int[] first = new int[2];
//                int[] firstVisibleItemPosition = ((StaggeredGridLayoutManager)layoutManager).findFirstVisibleItemPositions(first);
//                if (firstVisibleItemPosition[0] > 3) {
//                    stick.setVisibility(View.VISIBLE);
//                } else {
//                    stick.setVisibility(View.GONE);
//                }
            }


        }
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
        MyLog.i("test","setAdapter");
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
//            refreshLayout.finishRefresh();
            if (Listener != null) {
                Listener.onLoadMoreRequested();
            }
        }

        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            if (Listener != null) {
                Listener.onRefresh(refreshLayout);
            }
        }
    }

    public interface RefreshAndLoadMoreListener {
        void onLoadMoreRequested();

        void onRefresh(@NonNull RefreshLayout refreshLayout);
    }

    public SwipeRefreshLayout getSwipeList(){
        return refreshLayout;
    }
    /**
     * 设置滚动监听
     * @param listener              OnScrollListener监听器
     */

    public void setOnExternalScrollListener(RecyclerView.OnScrollListener listener) {
        mExternalOnScrollListener = listener;
    }
}