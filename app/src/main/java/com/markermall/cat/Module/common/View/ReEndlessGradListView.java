package com.markermall.cat.Module.common.View;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.markermall.cat.App;
import com.markermall.cat.Module.common.widget.SwipeRefreshLayout;
import com.markermall.cat.R;
import com.markermall.cat.utils.MyLog;

/**
 * 通用上拉下拉加载列表-含loading效果
 */

public class ReEndlessGradListView extends LinearLayout {

    private LRecyclerView mRecyclerView = null;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter2;
    private SwipeRefreshLayout swipeList;
    private RelativeLayout listview_rl; //列表
    private int mFirstVisibleItemPosition;
    //    private LinearLayout loading_ly;  //加载圈
//    private LinearLayout reload_ly;   //重新加载
    boolean isGrad = false;

    public ReEndlessGradListView(Context context) {
        super(context);
        init(context);
    }

    public ReEndlessGradListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ReEndlessGradListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.view_reuselistview, null);
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        initView(view, context);
    }

    private void initView(View view, Context context) {
        swipeList = (SwipeRefreshLayout) view.findViewById(R.id.swipeList);
        listview_rl = (RelativeLayout)view.findViewById(R.id.listview_rl);
        //设置卷内的颜色
        swipeList.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
        //swipeList.setProgressViewOffset(true, 50, 200);
        //设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        //swipeList.setSize(SwipeRefreshLayout.DEFAULT);

        mRecyclerView = (LRecyclerView) view.findViewById(R.id.mListView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                mFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
        swipeList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (onReLoadListener != null) {
                    getListView().setNoMore(false);
                    getSwipeList().setRefreshing(true);
                    onReLoadListener.onReload();
                }
            }
        });

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!swipeList.isRefreshing())
                    if (onReLoadListener != null)
                        onReLoadListener.onLoadMore();
            }
        });
        App.mHandler.post(new Runnable() {
            @Override
            public void run() {
                getListView().setNoMore(false);
                getSwipeList().setRefreshing(true);

            }
        });
    }

    public RelativeLayout getListviewSuper() {
        return listview_rl;
    }


    /**
     * 刷新数据
     */
    public void notifyDataSetChanged() {
        if (isGrad)
            lRecyclerViewAdapter2.notifyDataSetChanged();
        else
            lRecyclerViewAdapter.notifyDataSetChanged();
    }

    public LRecyclerView getListView() {
        return mRecyclerView;
    }

    public SwipeRefreshLayout getSwipeList() {
        return swipeList;
    }

    public boolean isGrad() {
        return isGrad;
    }

    /**
     * 切换Adapter样式
     *
     * @param context
     * @param
     */
    public void switchAdapter(Context context, int size) {
        if (isGrad) {
            isGrad = false;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setAdapter(lRecyclerViewAdapter);//必须重新setAdapter
        } else {
            isGrad = true;

            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.setAdapter(lRecyclerViewAdapter2);//必须重新setAdapter


        }

    }


    //获得RecyclerView滑动的位置
    private int[] getRecyclerViewLastPosition(LinearLayoutManager layoutManager, int size) {
        int[] pos = new int[2];
        pos[0] = layoutManager.findFirstCompletelyVisibleItemPosition();
        OrientationHelper orientationHelper = OrientationHelper.createOrientationHelper(layoutManager, OrientationHelper.VERTICAL);
        int fromIndex = 0;
        int toIndex = size;
        final int start = orientationHelper.getStartAfterPadding();
        final int end = orientationHelper.getEndAfterPadding();
        final int next = toIndex > fromIndex ? 1 : -1;
        for (int i = fromIndex; i != toIndex; i += next) {
            final View child = mRecyclerView.getChildAt(i);
            final int childStart = orientationHelper.getDecoratedStart(child);
            final int childEnd = orientationHelper.getDecoratedEnd(child);
            if (childStart < end && childEnd > start) {
                if (childStart >= start && childEnd <= end) {
                    pos[1] = childStart;
                    MyLog.d("position = " + pos[0] + " off = " + pos[1]);
                    return pos;
                }
            }
        }
        return pos;
    }


    public void setAdapter(RecyclerView.Adapter mDataAdapter) {
        lRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(lRecyclerViewAdapter);
    }

    public void setAdapter(RecyclerView.Adapter mDataAdapter, RecyclerView.Adapter mDataAdapter2) {
        lRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(lRecyclerViewAdapter);

        lRecyclerViewAdapter2 = new LRecyclerViewAdapter(mDataAdapter2);
    }

    public void setAdapterAndHeadView(View headView, RecyclerView.Adapter mDataAdapter, RecyclerView.Adapter mDataAdapter2) {
        lRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        if (headView != null) {
            lRecyclerViewAdapter.addHeaderView(headView);
        }
        mRecyclerView.setAdapter(lRecyclerViewAdapter);

        lRecyclerViewAdapter2 = new LRecyclerViewAdapter(mDataAdapter2);
        if (headView != null) {
            lRecyclerViewAdapter2.addHeaderView(headView);
        }
    }

    public void setAdapterAndHeadView(View headView, RecyclerView.Adapter mDataAdapter) {
        lRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        if (headView != null) {
            lRecyclerViewAdapter.addHeaderView(headView);
        }
        mRecyclerView.setAdapter(lRecyclerViewAdapter);

    }


    public void setAdapterAndHeadViewAndFootView(View headView, View footView, RecyclerView.Adapter mDataAdapter) {
        lRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        if (headView != null) {
            lRecyclerViewAdapter.addHeaderView(headView);
        }
        if (footView != null) {
            lRecyclerViewAdapter.addFooterView(footView);
        }
        mRecyclerView.setAdapter(lRecyclerViewAdapter);
    }

    public void setAdapterAndFootView(View footView, RecyclerView.Adapter mDataAdapter) {
        lRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        if (footView != null) {
            lRecyclerViewAdapter.addFooterView(footView);
        }
        mRecyclerView.setAdapter(lRecyclerViewAdapter);
    }

    public LRecyclerViewAdapter getlRecyclerViewAdapter() {
        return lRecyclerViewAdapter;
    }

    public void setlRecyclerViewAdapter(LRecyclerViewAdapter lRecyclerViewAdapter) {
        this.lRecyclerViewAdapter = lRecyclerViewAdapter;
    }

    public void setOnReLoadListener(OnReLoadListener listener) {
        onReLoadListener = listener;
    }

    private OnReLoadListener onReLoadListener;

    public interface OnReLoadListener {
        public void onReload();

        public void onLoadMore();
    }

    /**
     * 处理自动转屛崩溃用
     *
     * @param state
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        try {
            super.onRestoreInstanceState(state);
        } catch (Exception e) {
            state = null;
        }
    }
}