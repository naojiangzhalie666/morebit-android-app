package com.zjzy.morebit.main.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zjzy.morebit.Module.common.View.ReEndlessGradListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.GoodsAdapter;
import com.zjzy.morebit.adapter.ShoppingListAdapter;
import com.zjzy.morebit.adapter.ShoppingListAdapter2;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.main.contract.RankingContract;
import com.zjzy.morebit.main.presenter.ShoppingListPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UI.BaseTitleTabBean;
import com.zjzy.morebit.pojo.event.LogoutEvent;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.view.AspectRatioView;
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

public class ShoppingListFragment2 extends MvpFragment<ShoppingListPresenter> implements RankingContract.View, ReEndlessGradListView.OnReLoadListener {
    public static String Type = "loadType";
    public static String IMAGE_INFO = "IMAGE_INFO"; // 广告加载的跳转info
    public static int TYPEGOODSBYBRAND = 4; //  根据品牌ID获取商品列表

    @BindView(R.id.rl_list)
    ReEndlessGradListView rl_list;
    @BindView(R.id.ll_root)
    LinearLayout ll_root;

    private static final int REQUEST_COUNT = 10;
    private ShoppingListAdapter2 mAdapter;
    private GoodsAdapter mAdapter1;

    private int mType;
    private String mActivity_id = "";
    private String mMaterial = "0";
    private String mCategoryId = "";
    LinearLayout headView = null;
    private Handler mHandler;
    private boolean mIsAddView;
    private LinearLayout mLl_super_tab;
    private ShoppingTabView mShoppingTabView;
    private ImageInfo mImageInfo;

    public static ShoppingListFragment2 newInstance(int loadType) {
        Bundle args = new Bundle();
        args.putInt(ShoppingListFragment2.Type, loadType);
        args.putSerializable(ShoppingListFragment2.IMAGE_INFO, new ImageInfo());
        ShoppingListFragment2 fragment = new ShoppingListFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    public static ShoppingListFragment2 newInstance(Bundle args) {
        ShoppingListFragment2 fragment = new ShoppingListFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        if (rl_list.getSwipeList() != null)
            rl_list.getSwipeList().setRefreshing(true);
        onReload();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mHandler = new Handler();
    }


    @Override
    protected void initView(View view) {
        if (getArguments() == null) return;

        mImageInfo = (ImageInfo) getArguments().getSerializable(ShoppingListFragment2.IMAGE_INFO);
        if (getArguments() != null && mImageInfo != null) {
            mType = getArguments().getInt(ShoppingListFragment2.Type);
            mActivity_id = mImageInfo.getId() + "";
            mMaterial =  mImageInfo.getUrl();
            mCategoryId = mImageInfo.categoryId + "";
            headView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.view_goods_news_list_head, null);
            ImageView iv_banner = (ImageView) view.findViewById(R.id.iv_banner);
            mLl_super_tab = (LinearLayout) view.findViewById(R.id.ll_super_tab);
            AspectRatioView as_iv_banner = (AspectRatioView) view.findViewById(R.id.as_iv_banner);
            initShoppingTabVIew();
            if (isShowHeadPicture(mImageInfo)) {  // 1图片为纵向，0位横向。只有横向才显示图片
                LoadImgUtils.setImg(getActivity(), iv_banner, mImageInfo.getBackgroundImage());
                mLl_super_tab.addView(mShoppingTabView);
            } else {
                as_iv_banner.setVisibility(View.GONE);
                ll_root.addView(mShoppingTabView);
            }
            if (C.GoodsListType.DiscountStores == mType || C.GoodsListType.WHAT_LIKE == mType || C.GoodsListType.MATERIAL == mType) {
                mShoppingTabView.setVisibility(View.GONE);
            //    mLl_super_tab.setVisibility(View.GONE);
            }
        }


        mAdapter = new ShoppingListAdapter2(getActivity());
        mAdapter1 = new GoodsAdapter(getActivity());
        mAdapter1.setMaterialID(mMaterial);
        mAdapter.setMaterialID(mMaterial);
        mAdapter.setType(mType);
        mAdapter1.setListType(mType);
        rl_list.getListView().setNestedScrollingEnabled(false);
        rl_list.setAdapter(mAdapter, mAdapter1);

        rl_list.setOnReLoadListener(this);
//        mGoTop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rl_list.getListView().scrollToPosition(0);
//                scrollHeight = 0;
//            }
//        });
        if (mType == C.GoodsListType.WHAT_LIKE) {
            rl_list.switchAdapter(getActivity(), 1);
        }
        rl_list.getListView().addOnScrollListener(new RecyclerViewScrollListener());
    }


    private boolean isShowHeadPicture(ImageInfo imageInfo) {
        return imageInfo != null && !TextUtils.isEmpty(imageInfo.getBackgroundImage());
    }

    private void initShoppingTabVIew() {
        mShoppingTabView = new ShoppingTabView(getActivity(), mType);

        mShoppingTabView.setmOkListener(new ShoppingTabView.OnTabListner() {
            @Override
            public void switchItmeStye() {
                scrollHeight = 0;
                MyLog.d("ShoppingListFragment2", "  rl_list.isGrad()   " + rl_list.isGrad());
                rl_list.switchAdapter(getActivity(), 1);
                if (rl_list.isGrad() == true) {
                    mShoppingTabView.setSwitchSelected(true);
                } else {
                    mShoppingTabView.setSwitchSelected(false);
                }
            }

            @Override
            public void onReload() {
                rl_list.getListView().scrollToPosition(0);
                scrollHeight = 0;
                ShoppingListFragment2.this.onReload();
            }
        });
    }

    /**
     * 获取资源ID
     *
     * @return
     */
    @Override
    protected int getViewLayout() {
        return R.layout.fragment_shopping_list2;
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
            rl_list.getSwipeList().setRefreshing(false);
        if (mShoppingTabView == null) return;
        BaseTitleTabBean baseTitleTabBean = getBaseTitleTabBean();
        mPresenter.getRankings(this, mType, C.requestType.initData, baseTitleTabBean);
    }

    @Override
    public void onLoadMore() {
        if (mShoppingTabView == null) return;
        BaseTitleTabBean baseTitleTabBean = getBaseTitleTabBean();
        mPresenter.getRankings(this, mType, C.requestType.loadMore, baseTitleTabBean);
    }

    private BaseTitleTabBean getBaseTitleTabBean() {
        BaseTitleTabBean baseTitleTabBean = mShoppingTabView.getTabBean();
        baseTitleTabBean.categoryId = mCategoryId;
        baseTitleTabBean.activity_id = mActivity_id;
        baseTitleTabBean.material = mMaterial;
        return baseTitleTabBean;
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
        if (headView != null && headView.getVisibility() == View.GONE) {
            headView.setVisibility(View.VISIBLE);
        }
        if (loadType == C.requestType.initData) {
            mData.clear();
            mAdapter1.replace(data);
        } else {
            rl_list.getListView().refreshComplete(10);
            mAdapter1.add(data);
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
            mAdapter1.replace(mData);
            mAdapter.setData(mData);
            rl_list.notifyDataSetChanged();
            showNetworkError(rl_list.getListviewSuper(), false);
            if (headView != null) {
                headView.setVisibility(View.GONE);
            }
        }
    }

    private int scrollHeight;

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
            LinearLayoutManager linearManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstVisibleItemPosition = linearManager.findFirstVisibleItemPosition();
//            if (firstVisibleItemPosition > 3) {
//                mGoTop.setVisibility(View.VISIBLE);
//            } else {
//                mGoTop.setVisibility(View.GONE);
//            }

            if (isShowHeadPicture(mImageInfo) && headView != null && mShoppingTabView != null && mLl_super_tab != null && ll_root != null)
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
                int TabViewHeight = mShoppingTabView.getHeight();
                mHeadViewHeight = headView.getHeight() - TabViewHeight;
            }
            boolean b = scrollHeight >= mHeadViewHeight;
            if (b && !mIsAddView) {
                mIsAddView = true;
              //  mLl_super_tab.removeView(mShoppingTabView);
                ll_root.addView(mShoppingTabView);
            } else if (scrollHeight < mHeadViewHeight && mIsAddView) {
                mIsAddView = false;
                ll_root.removeView(mShoppingTabView);
                mLl_super_tab.addView(mShoppingTabView);
            }
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

        if (mHandler != null)
            mHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
    }
}
