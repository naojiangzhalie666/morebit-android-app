package com.zjzy.morebit.main.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;
import android.widget.LinearLayout;

import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.PddJdListAdapter;
import com.zjzy.morebit.adapter.RankingListAdapter;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.main.contract.PddContract;
import com.zjzy.morebit.main.contract.RankingContract;
import com.zjzy.morebit.main.presenter.PddListPresenter;
import com.zjzy.morebit.main.presenter.ShoppingListPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.ProgramCatItemBean;
import com.zjzy.morebit.pojo.RankingTitleBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UI.BaseTitleTabBean;
import com.zjzy.morebit.pojo.event.LogoutEvent;
import com.zjzy.morebit.pojo.pddjd.JdPddProgramItem;
import com.zjzy.morebit.pojo.pddjd.PddJdTitleTypeItem;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.view.helper.ShoppingTabView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by fengrs on 2018/9/10.
 * 备注:
 */

public class PddListFragment extends MvpFragment<PddListPresenter> implements PddContract.View, ReUseListView.OnReLoadListener {

    public static String PDDJDTITLETYPEITEM = "PddJdTitleTypeItem"; // 广告加载的跳转info
    @BindView(R.id.mListView)
    ReUseListView rl_list;
    @BindView(R.id.ll_root)
    LinearLayout ll_root;


    private static final int REQUEST_COUNT = 10;
    private PddJdListAdapter mAdapter;
    private PddJdTitleTypeItem mPddJdTitleTypeItem;



    public static PddListFragment newInstance(PddJdTitleTypeItem bean) {
        PddListFragment fragment = new PddListFragment();
        Bundle args = new Bundle();
        args.putSerializable(PddListFragment.PDDJDTITLETYPEITEM, bean);
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
        mPddJdTitleTypeItem = (PddJdTitleTypeItem) getArguments().getSerializable(PddListFragment.PDDJDTITLETYPEITEM);
        mAdapter = new PddJdListAdapter(getActivity());
        rl_list.setAdapter(mAdapter);
        rl_list.setOnReLoadListener(this);

    }




    /**
     * 获取资源ID
     *
     * @return
     */
    @Override
    protected int getViewLayout() {
        return R.layout.fragment_pdd_list;
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
        if (mPddJdTitleTypeItem == null) return;
        ProgramCatItemBean programCatItemBean = new ProgramCatItemBean();
        programCatItemBean.setCatId(Integer.valueOf(mPddJdTitleTypeItem.getTabNo()));
        mPresenter.getJdPddGoodsList(this,  programCatItemBean, C.requestType.initData);
    }

    @Override
    public void onLoadMore() {
        if (mPddJdTitleTypeItem == null) return;
        ProgramCatItemBean programCatItemBean = new ProgramCatItemBean();
        programCatItemBean.setCatId(Integer.valueOf(mPddJdTitleTypeItem.getTabNo()));
        mPresenter.getJdPddGoodsList(this, programCatItemBean,C.requestType.loadMore);
    }




    ArrayList<ShopGoodInfo> mData = new ArrayList<>();

    @Override
    public void showFinally() {
        if (rl_list.getSwipeList() != null)
            rl_list.getSwipeList().setRefreshing(false);

    }


    @Override
    public void setPdd(List<ShopGoodInfo> data, int loadType) {
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
    public void setPddError(int loadType) {
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
