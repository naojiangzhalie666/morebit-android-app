package com.jf.my.Module.common.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.jf.my.App;
import com.jf.my.Module.common.widget.SwipeRefreshLayout;
import com.jf.my.R;

/**
 * 通用上拉下拉加载列表-含loading效果
 */

public class ReUseGridView extends LinearLayout {

    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter lRecyclerViewAdapter;
    private SwipeRefreshLayout swipeList;
   private  int  mColumn =1;
    private ImageView mGoTop;
    private RelativeLayout listview_rl;
    protected RecyclerView.OnScrollListener mExternalOnScrollListener;
    public ReUseGridView(Context context) {
        super(context);
        init(context);
    }

    public ReUseGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.ReUseGridView);
        mColumn = array.getInt(R.styleable.ReUseGridView_column,1);
        init(context);
    }

    public ReUseGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.view_reusegridview,null);
        addView(view,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        initView(view,context);
    }

    private void initView(View view, Context context){
        swipeList = (SwipeRefreshLayout) view.findViewById(R.id.swipeList);
        mGoTop = (ImageView) view.findViewById(R.id.go_top);
        listview_rl = (RelativeLayout) view.findViewById(R.id.listview_rl);
        //设置卷内的颜色
        swipeList.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);


        mRecyclerView = (LuRecyclerView) view.findViewById(R.id.mListView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),mColumn));

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
                    if (onReLoadListener != null){
                        onReLoadListener.onLoadMore();
                    }
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerViewScrollListener());
    }



    /**
     * 刷新数据
     */
    public void notifyDataSetChanged() {
        lRecyclerViewAdapter.notifyDataSetChanged();
    }

    public LuRecyclerView getListView(){
        return  mRecyclerView;
    }

    public SwipeRefreshLayout getSwipeList(){
        return swipeList;
    }

    public void setAdapter(RecyclerView.Adapter mDataAdapter){
        lRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(lRecyclerViewAdapter);
    }

    public void setAdapterAndHeadView(View headView, RecyclerView.Adapter mDataAdapter){
        lRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        if(headView!=null){
            lRecyclerViewAdapter.addHeaderView(headView);
        }
        mRecyclerView.setAdapter(lRecyclerViewAdapter);
    }

    public void setOnReLoadListener(OnReLoadListener listener){
        onReLoadListener = listener;
    }

    private OnReLoadListener onReLoadListener;
    public interface OnReLoadListener{
        public void onReload();

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

    public void setSpanSizeLookup(LuRecyclerViewAdapter.SpanSizeLookup spanSizeLookup ){
        lRecyclerViewAdapter.setSpanSizeLookup(spanSizeLookup);
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
            if (mExternalOnScrollListener != null){
                mExternalOnScrollListener.onScrolled(recyclerView, dx, dy);
            }
            LinearLayoutManager linearManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstVisibleItemPosition = linearManager.findFirstVisibleItemPosition();
            if (firstVisibleItemPosition > 3) {
                mGoTop.setVisibility(View.VISIBLE);
            } else {
                mGoTop.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 移除完了错误提示
     *
     */
    public void removeNetworkError( ) {
        if (listview_rl == null) {
            return;
        }
        for (int i = 0; i < listview_rl.getChildCount(); i++) {
            View childAt = listview_rl.getChildAt(i);
            if (childAt.getId() == R.id.ll_error_id) {
                listview_rl.removeView(childAt);
            }
        }
    }

    /**
     * 添加网络错误提示
     *
     * @param networkError
     */
    public void showNetworkError(boolean networkError ) {
        if (listview_rl == null) {
            return;
        }
        for (int i = 0; i < listview_rl.getChildCount(); i++) {
            View childAt = listview_rl.getChildAt(i);
            if (childAt.getId() == R.id.ll_error_id) {
                setNetWorkVIewData(networkError, childAt);
                return;
            }
        }
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(listview_rl.getContext()).inflate(R.layout.view_list_error, null);
        view.setId(R.id.ll_error_id);
        listview_rl.addView(view);
        setNetWorkVIewData(networkError, view);
    }

    private void setNetWorkVIewData(boolean networkError, View view) {
        ImageView mIv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        TextView mTv_text = (TextView) view.findViewById(R.id.tv_text);
        if (networkError) {
            mIv_icon.setImageResource(R.drawable.image_meiyouwangluo);
            mTv_text.setText(App.getAppContext().getString(R.string.dtae_error));
        } else {
            mIv_icon.setImageResource(R.drawable.image_meiyousousuojilu);
            mTv_text.setText(App.getAppContext().getString(R.string.data_null));

        }
    }


    /**
     * 设置滚动监听
     * @param listener              OnScrollListener监听器
     */

    public void setOnExternalScrollListener(RecyclerView.OnScrollListener listener) {
        mExternalOnScrollListener = listener;
    }

}