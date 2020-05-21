package com.zjzy.morebit.goodsvideo;

import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zjzy.morebit.Module.common.View.ReUseGridView;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goodsvideo.adapter.VideoGoodsAdapter;
import com.zjzy.morebit.goodsvideo.contract.VideoContract;
import com.zjzy.morebit.goodsvideo.presenter.VideoPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.VideoClassBean;
import com.zjzy.morebit.utils.SpaceItemDecorationUtils;

import java.util.List;

/**
 * 抖货分类
 */
public class VideoFragment extends MvpFragment<VideoPresenter> implements VideoContract.View  {

    private RecyclerView rcy_videclass;
    private String cid="1";
    private VideoGoodsAdapter videoGoodsAdapter;
    private int page=1;
    private List<ShopGoodInfo>  list;
    private SwipeRefreshLayout  swipeList;


    public static VideoFragment newInstance(String name,String id) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        Log.e("id",id);
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    protected void initData() {
        getData();

    }

    private void getData() {
        mPresenter.getVideoGoods(this,cid,page);
    }



    @Override
    protected void initView(View view) {


        Bundle arguments = getArguments();
        if (arguments != null) {
              cid = arguments.getString("id");

        }
        rcy_videclass = view.findViewById(R.id.rcy_videclass);
        swipeList=view.findViewById(R.id.swipeList);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        //设置图标的间距
        SpaceItemDecorationUtils spaceItemDecorationUtils = new SpaceItemDecorationUtils(20, 2);

        rcy_videclass.setLayoutManager(manager);
        rcy_videclass.addItemDecoration(spaceItemDecorationUtils);


        swipeList.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                initData();
                refreshLayout.finishRefresh(true);//刷新完成
            }
        });
        swipeList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initData();
            }
        });

    }



    @Override
    protected int getViewLayout() {
        return R.layout.fragment_video;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    public void onVideoClassSuccess(List<VideoClassBean> shopGoodInfo) {

    }

    @Override
    public void onVideoClassError(String throwable) {

    }

    @Override
    public void onVideoGoodsSuccess(List<ShopGoodInfo> shopGoodInfo) {
        list=  shopGoodInfo;
        if (page==1){
            videoGoodsAdapter = new VideoGoodsAdapter(getActivity(),shopGoodInfo);
            rcy_videclass.setAdapter(videoGoodsAdapter);
        }else{

            videoGoodsAdapter.loadMore(list);
            swipeList.finishLoadMore(false);
        }


    }

    @Override
    public void onVideoGoodsError() {

    }
}
