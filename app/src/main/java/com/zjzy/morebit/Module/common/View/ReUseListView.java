package com.zjzy.morebit.Module.common.View;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.zjzy.morebit.App;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;

/**
 * 通用上拉下拉加载列表-含loading效果
 */

public class ReUseListView extends LinearLayout{

    private LRecyclerView mRecyclerView = null;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private SwipeRefreshLayout swipeList;
    private RelativeLayout listview_rl; //列表
    private ImageView mGoTop;
    private boolean mIsDefaultRefreshing = true; //默认是否显示Refreshing
    private boolean isShowStick =true;  //是否显示置顶按钮
    protected RecyclerView.OnScrollListener mExternalOnScrollListener;
    public ReUseListView(Context context) {
        super(context);
        init(context);
    }

    public ReUseListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ReUseListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.view_reuselistview1,null);
        addView(view,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        initView(view,context);
    }

    private void initView(View view,Context context){
        swipeList = (SwipeRefreshLayout) view.findViewById(R.id.swipeList);
        mGoTop = (ImageView) view.findViewById(R.id.go_top);
        //设置卷内的颜色
        swipeList.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);


        mRecyclerView = (LRecyclerView) view.findViewById(R.id.mListView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setPullRefreshEnabled(false);
        // 第一种，直接取消动画
        RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        listview_rl = (RelativeLayout)view.findViewById(R.id.listview_rl);
        mGoTop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.scrollToPosition(0);
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
                if(mIsDefaultRefreshing){
                    getListView().setNoMore(false);
                    getSwipeList().setRefreshing(true);
                }
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerViewScrollListener());
    }

    public void isDefaultRefreshing(boolean isDefaultRefreshing){
        mIsDefaultRefreshing = isDefaultRefreshing;
    }

    public RelativeLayout getListviewSuper() {
        return listview_rl;
    }

    /**
     * 刷新数据
     */
    public void notifyDataSetChanged() {
        lRecyclerViewAdapter.notifyDataSetChanged();
    }

    public LRecyclerView getListView(){
        return  mRecyclerView;
    }

    public SwipeRefreshLayout getSwipeList(){
        return swipeList;
    }

    public void setAdapter(RecyclerView.Adapter mDataAdapter){
        lRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(lRecyclerViewAdapter);
    }

    public void setAdapterAndHeadView(View headView,RecyclerView.Adapter mDataAdapter){
        lRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        if(headView!=null){
            lRecyclerViewAdapter.addHeaderView(headView);
        }
        mRecyclerView.setAdapter(lRecyclerViewAdapter);
    }
    public void setAdapterAndHeadViewAndFootView(View headView,View footView,RecyclerView.Adapter mDataAdapter){
        lRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        if(headView!=null){
            lRecyclerViewAdapter.addHeaderView(headView);
        }
        if(footView!=null){
            lRecyclerViewAdapter.addFooterView(footView);
        }
        mRecyclerView.setAdapter(lRecyclerViewAdapter);
    }
    public void setAdapterAndFootView(View footView,RecyclerView.Adapter mDataAdapter){
        lRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        if(footView!=null){
            lRecyclerViewAdapter.addFooterView(footView);
        }
        mRecyclerView.setAdapter(lRecyclerViewAdapter);
    }
    public void setLayoutManager(RecyclerView.LayoutManager layout){
        mRecyclerView.setLayoutManager(layout);
    }
    public LRecyclerViewAdapter getlRecyclerViewAdapter() {
        return lRecyclerViewAdapter;
    }

    public void setlRecyclerViewAdapter(LRecyclerViewAdapter lRecyclerViewAdapter) {
        this.lRecyclerViewAdapter = lRecyclerViewAdapter;
    }

    public void setOnReLoadListener(OnReLoadListener listener){
        onReLoadListener = listener;
    }

    private OnReLoadListener onReLoadListener;
    public interface OnReLoadListener{
          void onReload();

        void onLoadMore();
    }

    /**
     * 处理自动转屛崩溃用
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
    //滑动监听
    private class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (mExternalOnScrollListener != null){
                mExternalOnScrollListener.onScrollStateChanged(recyclerView, newState);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager linearManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstVisibleItemPosition = linearManager.findFirstVisibleItemPosition();
            if (mExternalOnScrollListener != null){
                mExternalOnScrollListener.onScrolled(recyclerView, dx, dy);
            }
            if(isShowStick) {
                if (firstVisibleItemPosition > 3) {
                    mGoTop.setVisibility(View.VISIBLE);
                } else {
                    mGoTop.setVisibility(View.GONE);
                }
            }

        }
    }

    public boolean isShowStick() {
        return isShowStick;
    }

    public void setShowStick(boolean showStick) {
        isShowStick = showStick;
    }


    /**
     * 设置滚动监听
     * @param listener              OnScrollListener监听器
     */

    public void setOnExternalScrollListener(RecyclerView.OnScrollListener listener) {
        mExternalOnScrollListener = listener;
    }
}