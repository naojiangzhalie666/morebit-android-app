package com.zjzy.morebit.home.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.jdsjlzx.ItemDecoration.SpaceItemDecoration;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.HomeMenuAdapter;
import com.zjzy.morebit.adapter.LimitSkillAdapter;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.goodsvideo.contract.VideoContract;
import com.zjzy.morebit.goodsvideo.presenter.VideoPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.PanicBuyTiemBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.VideoClassBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestGetTimedSpikeList;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DensityUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 秒杀商品
 */
public class LimiteSkillFragment extends BaseMainFragmeng {

    private ReUseListView mRecyclerView;
    private static final int REQUEST_COUNT = 10;
    private List<ShopGoodInfo> list = new ArrayList<>();
    private SwipeRefreshLayout swipeList;
    private LinearLayout searchNullTips_ly;
    private String type, mtitle;
    private int pageNum = 1;
    private List<ShopGoodInfo> listArray = new ArrayList<>();
    private LimitSkillAdapter mAdapter;
    private LinearLayout dataList_ly;

    public static LimiteSkillFragment newInstance(String startTime, String subTitle) {
        LimiteSkillFragment fragment = new LimiteSkillFragment();
        Bundle args = new Bundle();
        args.putSerializable(C.UserType.SKILLITEM, startTime);
        args.putSerializable(C.UserType.SKILLTITLE, subTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_litmitskill, container, false);
        initView(view);
        initmData();
        return view;
    }

    private void initmData() {


    }


    private void initView(View view) {


        Bundle arguments = getArguments();
        if (arguments != null) {
            type = (String) arguments.getSerializable(C.UserType.SKILLITEM);
            mtitle = (String) arguments.getSerializable(C.UserType.SKILLTITLE);
        }
        dataList_ly=view.findViewById(R.id.dataList_ly);
        mRecyclerView = view.findViewById(R.id.rcy_litmit);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new LimitSkillAdapter(getActivity(),type,mtitle);
        mRecyclerView.setAdapter(mAdapter);
        getFirstData();
        mRecyclerView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstData();
            }
        });


        mRecyclerView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!isFirstData)
                    getMoreData();
            }
        });

    }

    /**
     * 第一次获取数据
     */
    private boolean isFirstData = true;

    public void getFirstData() {
        isFirstData = true;
        pageNum = 1;
        mRecyclerView.getListView().setNoMore(false);
        getObservable(type).doFinally(new Action() {
            @Override
            public void run() throws Exception {
                isFirstData = false;
                mRecyclerView.getSwipeList().setRefreshing(false);
            }
        })
                .subscribe(new DataObserver<List<ShopGoodInfo>>() {
                    @Override
                    protected void onDataListEmpty() {
                        listArray.clear();
                        mAdapter.setData(listArray);
                        mRecyclerView.notifyDataSetChanged();
                        dataList_ly.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }

                    @Override
                    protected void onSuccess(List<ShopGoodInfo> goodsList) {
                        if (goodsList != null && goodsList.size() != 0) {
                            listArray.clear();
                            listArray.addAll(goodsList);
                            pageNum = pageNum + 1;
                            mAdapter.setData(goodsList);
                            //设置是否是代理商
                            mRecyclerView.notifyDataSetChanged();
                        } else {
                            listArray.clear();
                            mAdapter.setData(listArray);
                            mRecyclerView.notifyDataSetChanged();
                            dataList_ly.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }
                    }
                });
    }

    /**
     * 加载更多数据
     */
    public void getMoreData() {
        getObservable(type)
                .subscribe(new DataObserver<List<ShopGoodInfo>>() {
                    @Override
                    protected void onDataListEmpty() {
                        mRecyclerView.getListView().setNoMore(true);
                    }

                    @Override
                    protected void onSuccess(List<ShopGoodInfo> goodsList) {
                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                        if (goodsList != null && goodsList.size() > 0) {
                            listArray.addAll(goodsList);
                            pageNum = pageNum + 1;
                            mAdapter.setData(listArray);
                            mRecyclerView.notifyDataSetChanged();
                        } else {
                            mRecyclerView.getListView().setNoMore(true);
                        }
                    }
                });
    }


    public Observable<BaseResponse<List<ShopGoodInfo>>> getObservable(String type) {
        return RxHttp.getInstance().getGoodsService()
                .getTimedSpikeList(new RequestGetTimedSpikeList().setHourType(type).setPage(pageNum))
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(this.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());

    }

}
