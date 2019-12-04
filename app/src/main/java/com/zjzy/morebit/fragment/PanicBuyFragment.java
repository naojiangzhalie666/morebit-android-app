package com.zjzy.morebit.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.ShoppingListAdapter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.PanicBuyTiemBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.requestbodybean.RequestGetTimedSpikeList;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.view.AspectRatioView;
import com.zjzy.morebit.view.ToolbarHelper;
import com.zjzy.morebit.view.helper.PanicBuyTabView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.functions.Action;


/**
 * 限时秒杀
 */
public class PanicBuyFragment extends BaseFragment {
    @BindView(R.id.ll_root)
    LinearLayout ll_root;
    @BindView(R.id.mListView)
    ReUseListView mRecyclerView;
    private ShoppingListAdapter mAdapter;
    private boolean mIsAddView;
    private List<ShopGoodInfo> listArray = new ArrayList<>();
    private View headView;
    private int pageNum = 1;
    private static final int REQUEST_COUNT = 10;
    private PanicBuyTabView mPanicBuyTabView;
    private int scrollHeight;
    private LinearLayout mLl_super_tab;
    private ImageInfo mImageInfo;


    public static void start(Activity activity, ImageInfo info) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(C.Extras.GOODSBEAN, info);
        OpenFragmentUtils.goToSimpleFragment(activity, PanicBuyFragment.class.getName(), bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_panicbuy, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(R.string.panic_buy);
        mImageInfo = (ImageInfo) getArguments().getSerializable(C.Extras.GOODSBEAN);
        initView(view);
        mPanicBuyTabView.getTabDeta((RxAppCompatActivity) getActivity());
    }


    public void initView(View view) {
        mAdapter = new ShoppingListAdapter(getActivity());
        mRecyclerView.getListView().addOnScrollListener(new RecyclerViewScrollListener());
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.item_panicbuy_list_headview, null);
        mLl_super_tab = (LinearLayout) headView.findViewById(R.id.ll_super_tab);
        AspectRatioView as_iv_banner = (AspectRatioView) headView.findViewById(R.id.as_iv_banner);
        ImageView iv_banner = (ImageView) headView.findViewById(R.id.iv_banner);
        mPanicBuyTabView = new PanicBuyTabView(getActivity());
        mPanicBuyTabView.setTabListener(new PanicBuyTabView.OnTabListner() {
            @Override
            public void getFirstData() {
                if (AppUtil.isFastCashMoneyClick(500)) {
                    return;
                }
                scrollHeight = 0;
                mRecyclerView.getListView().scrollToPosition(0);
                mRecyclerView.getSwipeList().setRefreshing(true);
                PanicBuyFragment.this.getFirstData();
            }
        });

        if (isShowHeadPicture(mImageInfo)) {  // 1图片为纵向，0位横向。只有横向才显示图片
            LoadImgUtils.setImg(getActivity(), iv_banner, mImageInfo.getBackgroundImage());
            mLl_super_tab.addView(mPanicBuyTabView);
        } else {
            as_iv_banner.setVisibility(View.GONE);
            ll_root.addView(mPanicBuyTabView);
        }
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
        mRecyclerView.setAdapterAndHeadView(headView, mAdapter);

    }


    //滑动监听
    private class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        private int mHeadViewHeight;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (isShowHeadPicture(mImageInfo) && headView != null && ll_root != null)
                try {
                    scrolledTabSuspend(dy);
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }

        /**
         * 悬浮Tab监听
         *
         * @param dy
         */
        private void scrolledTabSuspend(int dy) {
            scrollHeight = scrollHeight + dy;
            if (mHeadViewHeight == 0) {
                int TabViewHeight = mPanicBuyTabView.getHeight();
                mHeadViewHeight = headView.getHeight() - TabViewHeight;
            }
            boolean b = scrollHeight >= mHeadViewHeight;
            if (b && !mIsAddView) {
                mIsAddView = true;
                mLl_super_tab.removeView(mPanicBuyTabView);
                ll_root.addView(mPanicBuyTabView);
            } else if (scrollHeight < mHeadViewHeight && mIsAddView) {
                mIsAddView = false;
                ll_root.removeView(mPanicBuyTabView);
                mLl_super_tab.addView(mPanicBuyTabView);
            }
        }
    }

    private boolean isShowHeadPicture(ImageInfo imageInfo) {
        return !TextUtils.isEmpty(imageInfo.getBackgroundImage());
    }

    /**
     * 第一次获取数据
     */
private boolean isFirstData = true;
    public void getFirstData() {
        isFirstData = true;
        pageNum = 1;
        mRecyclerView.getListView().setNoMore(false);
        PanicBuyTiemBean tiemPosBean = mPanicBuyTabView.getTiemPosBean();
        if (mPanicBuyTabView == null || tiemPosBean == null) {
            return;
        }
        getObservable(tiemPosBean).doFinally(new Action() {
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
                        }
                    }
                });
    }

    /**
     * 加载更多数据
     */
    public void getMoreData() {
        PanicBuyTiemBean tiemPosBean = mPanicBuyTabView.getTiemPosBean();
        if (mPanicBuyTabView == null || tiemPosBean == null) {
            return;
        }
        getObservable(tiemPosBean)
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

    public Observable<BaseResponse<List<ShopGoodInfo>>> getObservable(PanicBuyTiemBean tiemPosBean) {
        return RxHttp.getInstance().getGoodsService()
                .getTimedSpikeList(new RequestGetTimedSpikeList().setHourType(tiemPosBean.getStartTime()).setPage(pageNum))
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(this.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPanicBuyTabView.destroyView();
    }
}
