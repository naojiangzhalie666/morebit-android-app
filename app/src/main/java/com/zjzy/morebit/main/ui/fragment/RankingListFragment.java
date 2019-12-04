package com.zjzy.morebit.main.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.RankingListAdapter;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.main.contract.RankingContract;
import com.zjzy.morebit.main.presenter.ShoppingListPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.RankingTitleBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UI.BaseTitleTabBean;
import com.zjzy.morebit.pojo.event.LogoutEvent;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.view.helper.ShoppingTabView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by fengrs on 2018/9/10.
 * 备注:
 */

public class RankingListFragment extends MvpFragment<ShoppingListPresenter> implements RankingContract.View, ReUseListView.OnReLoadListener {

    public static String RANKINGTITLEBEAN = "RankingTitleBean"; // 广告加载的跳转info
    @BindView(R.id.mListView)
    ReUseListView rl_list;
    @BindView(R.id.ll_root)
    LinearLayout ll_root;


    private static final int REQUEST_COUNT = 10;
    private RankingListAdapter mAdapter;
    private RankingTitleBean mRankingTitleBean;
    private ShoppingTabView mShoppingTabView;


    public static RankingListFragment newInstance(RankingTitleBean bean) {
        RankingListFragment fragment = new RankingListFragment();
        Bundle args = new Bundle();
        args.putSerializable(RankingListFragment.RANKINGTITLEBEAN, bean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        onReload();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public int mScrolledY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(View view) {
        if (getArguments() == null) return;
        mRankingTitleBean = (RankingTitleBean) getArguments().getSerializable(RankingListFragment.RANKINGTITLEBEAN);
        mAdapter = new RankingListAdapter(getActivity());
        rl_list.setAdapter(mAdapter);
        rl_list.setOnReLoadListener(this);
        initShoppingTabVIew();
    }


    private void initShoppingTabVIew() {
        mShoppingTabView = new ShoppingTabView(getActivity(), C.GoodsListType.RANKING_NEWS);

        mShoppingTabView.setmOkListener(new ShoppingTabView.OnTabListner() {
            @Override
            public void switchItmeStye() {

            }

            @Override
            public void onReload() {
                RankingListFragment.this.onReload();
            }
        });
        ll_root.addView(mShoppingTabView);
    }


    /**
     * 获取资源ID
     *
     * @return
     */
    @Override
    protected int getViewLayout() {
        return R.layout.fragment_ranking_list;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    public void onReload() {
        if (rl_list == null) return;
        rl_list.getListView().setNoMore(false);
        if (rl_list.getSwipeList() != null)
            rl_list.getSwipeList().setRefreshing(true);
        if (mRankingTitleBean == null) return;
        getBaseTitleTabBean();
        mPresenter.getRankingsNews(this, C.requestType.initData, mRankingTitleBean);
    }

    @Override
    public void onLoadMore() {
        if (mRankingTitleBean == null) return;
        getBaseTitleTabBean();
        mPresenter.getRankingsNews(this, C.requestType.loadMore, mRankingTitleBean);
    }

    private void getBaseTitleTabBean() {
        if (mShoppingTabView != null) {
            BaseTitleTabBean baseTitleTabBean = mShoppingTabView.getTabBean();
            if (baseTitleTabBean != null) {
                mRankingTitleBean.setOrder(baseTitleTabBean.order);
                mRankingTitleBean.setSort(baseTitleTabBean.where);
            }
        }
    }


    ArrayList<ShopGoodInfo> mData = new ArrayList<>();

    @Override
    public void showFinally() {
        if (rl_list.getSwipeList() != null)
            rl_list.getSwipeList().setRefreshing(false);

    }


    @Override
    public void setRankings(List<ShopGoodInfo> data, int loadType) {
        rl_list.getListView().refreshComplete(REQUEST_COUNT);
        removeNetworkError(rl_list.getListviewSuper());
        if (loadType == C.requestType.initData) {
            mData.clear();
        } else {
            rl_list.getListView().refreshComplete(10);
        }
        mData.addAll(data);
        mAdapter.setData(mData);
        rl_list.notifyDataSetChanged();
    }

    @Override
    public void setRankingsError(int loadType) {
        if (loadType == C.requestType.loadMore) {
            rl_list.getListView().setNoMore(true);
        } else {
            mData.clear();
            mAdapter.setData(mData);
            rl_list.notifyDataSetChanged();
            showNetworkError(rl_list.getListviewSuper(), false);

        }
    }


    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        switch (event.getAction()) {
            case EventBusAction.LOGINA_SUCCEED:
                if (rl_list != null)
                    rl_list.notifyDataSetChanged();
                break;

        }
    }


    @Subscribe  //订阅事件
    public void onEventMainThread(LogoutEvent event) {
        if (rl_list != null)
            rl_list.notifyDataSetChanged();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
