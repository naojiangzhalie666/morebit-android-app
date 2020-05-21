package com.zjzy.morebit.main.ui.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class PddListFragment extends MvpFragment<PddListPresenter> implements PddContract.View, ReUseListView.OnReLoadListener, View.OnClickListener {

    public static String PDDJDTITLETYPEITEM = "PddJdTitleTypeItem"; // 广告加载的跳转info
    @BindView(R.id.mListView)
    ReUseListView rl_list;
    @BindView(R.id.ll_root)
    LinearLayout ll_root;


    private static final int REQUEST_COUNT = 10;
    private PddJdListAdapter mAdapter;
    private PddJdTitleTypeItem mPddJdTitleTypeItem;

    //销量
    private LinearLayout mTitleSalesVolumeLl;
    private ImageView mTitleSalesVolumeIv;
    private TextView mTitleSalesVolumeTv;
    //券后价
    private LinearLayout mTitlePostCouponPriceLl;
    private ImageView mTitlePostCouponPriceIv;
    private TextView mTitlePostCouponPriceTv;
    //佣金
    private LinearLayout mTitleCommissionLl;
    private ImageView mTitleCommissionIv;
    private TextView mTitleCommissionTv;

    private LinearLayout title_commission_ll;//奖励
    private TextView title_comprehensive_tv;
    //排序方向
    private int eSortDirection = C.OrderType.E_UPLIMIT_SORT_DOWN;// 0降序  1升序
    //排序类型
    private long mSortType = 1;//排序类型 1 综合排序 2 销量排序 3 价格排序 4 奖励排序
    //优惠券
    private LinearLayout title_coupon_ll;
    private ImageView title_coupon_iv;
    private boolean isCoupon = false;//优惠券是否选中
    private String coupon="0";



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

        rl_list.setOnReLoadListener(this);
        rl_list.getSwipeList().setEnableRefresh(false);

        title_comprehensive_tv=view.findViewById(R.id.title_comprehensive_tv);//综合

        mTitleSalesVolumeLl = view.findViewById(R.id.title_sales_volume_ll);//销量
        mTitleSalesVolumeTv = view.findViewById(R.id.title_sales_volume_tv);
        mTitleSalesVolumeIv = view.findViewById(R.id.title_sales_volume_iv);

        mTitlePostCouponPriceLl = view.findViewById(R.id.title_post_coupon_price__ll);//价格
        mTitlePostCouponPriceTv = view.findViewById(R.id.title_post_coupon_price_tv);
        mTitlePostCouponPriceIv = view.findViewById(R.id.title_post_coupon_price_iv);

        mTitleCommissionLl = view.findViewById(R.id.title_commission_ll);//奖励
        mTitleCommissionTv = view.findViewById(R.id.title_commission_tv);
        mTitleCommissionIv = view.findViewById(R.id.title_commission_iv);




        title_coupon_ll=view.findViewById(R.id.title_coupon_ll);//优惠券
        title_coupon_iv=view.findViewById(R.id.title_coupon_iv);


        mTitleSalesVolumeLl.setOnClickListener(this);
        mTitlePostCouponPriceLl.setOnClickListener(this);
        mTitleCommissionLl.setOnClickListener(this);
        title_comprehensive_tv.setOnClickListener(this);
        title_coupon_ll.setOnClickListener(this);



        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshpdd");//名字
        getActivity().registerReceiver(mRefreshBroadcastReceiver, intentFilter);
    }


    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshpdd")) {  //接收到广播通知的名字，在当前页面应与注册名称一致
                onReload();//需要去做的事
            }
        }
    };



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
        if (eSortDirection==0){
            programCatItemBean.setOrder("desc");
        }else{
            programCatItemBean.setOrder("asc");
        }
        if (mSortType==1){
            programCatItemBean.setSort("");
        }else if (mSortType==2){
            programCatItemBean.setSort("inOrderCount30Days");
        }else if (mSortType==3){
            programCatItemBean.setSort("price");
        }else if (mSortType==4){
            programCatItemBean.setSort("commissionShare");
        }
        programCatItemBean.setCoupon(coupon);
        mPresenter.getJdPddGoodsList(this,  programCatItemBean, C.requestType.initData);
    }

    @Override
    public void onLoadMore() {
        if (mPddJdTitleTypeItem == null) return;
        ProgramCatItemBean programCatItemBean = new ProgramCatItemBean();
        programCatItemBean.setCatId(Integer.valueOf(mPddJdTitleTypeItem.getTabNo()));
        programCatItemBean.setCoupon(coupon);
        if (eSortDirection==0){
            programCatItemBean.setOrder("desc");
        }else{
            programCatItemBean.setOrder("asc");
        }
        if (mSortType==1){
            programCatItemBean.setSort("");
        }else if (mSortType==2){
            programCatItemBean.setSort("inOrderCount30Days");
        }else if (mSortType==3){
            programCatItemBean.setSort("price");
        }else if (mSortType==4){
            programCatItemBean.setSort("commissionShare");
        }
        programCatItemBean.setCoupon(coupon);
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
//           mData.clear();
//        } else {
//            rl_list.getListView().refreshComplete(10);
//        }else{
            mAdapter = new PddJdListAdapter(getActivity(),data);
            rl_list.setAdapter(mAdapter);

        }else if (loadType== C.requestType.loadMore){

            //mData.addAll(data);
            mAdapter.setData(data);

        }

    }

    @Override
    public void setPddError(int loadType) {
        if (loadType == C.requestType.loadMore) {
            rl_list.getListView().setNoMore(true);
        } else {
            if (mData != null){
                mData.clear();
            }
            mAdapter.setData(mData);
            rl_list.notifyDataSetChanged();
            showNetworkError(rl_list.getListviewSuper(), false);

        }
    }

    @Override
    public void setJd(List<ShopGoodInfo> data, int loadType) {

    }

    @Override
    public void setJdError(int loadType) {

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
        getActivity().unregisterReceiver(mRefreshBroadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_comprehensive_tv://综合
                requestClickRadar(null, title_comprehensive_tv, 1);
                onReload();
                break;
            case R.id.title_sales_volume_ll://销量
                requestClickRadar(mTitleSalesVolumeIv, mTitleSalesVolumeTv, 2);
                onReload();
                break;
            case R.id.title_post_coupon_price__ll://价格
                requestClickRadar(mTitlePostCouponPriceIv, mTitlePostCouponPriceTv, 3);
                onReload();
                break;
            case R.id.title_commission_ll://奖励
                requestClickRadar(mTitleCommissionIv, mTitleCommissionTv, 4);
                onReload();
                break;
            case R.id.title_coupon_ll://优惠券
                clickCoupro();
                onReload();
                break;
        }
    }


    private void requestClickRadar(ImageView clickIv, TextView textView, int orderType) {
        if (orderType == 1) {
            //综合只有降序
            eSortDirection = C.OrderType.E_UPLIMIT_SORT_DOWN;
        } else {
            if (!textView.isSelected()){
                eSortDirection=C.OrderType.E_UPLIMIT_SORT_DOWN;
            }else {
                eSortDirection = eSortDirection == C.OrderType.E_UPLIMIT_SORT_DOWN ? C.OrderType.E_UPLIMIT_SORT_UP : C.OrderType.E_UPLIMIT_SORT_DOWN;
            }
        }
        mSortType = orderType;
        resetTitleRankDrawable(clickIv, textView, eSortDirection);

//        mLoadMoreHelper.loadData();
    }
    private void resetTitleRankDrawable(ImageView clickIv, TextView textView, int eSortDir) {
        //对点击的重置图
        mTitleSalesVolumeIv.setImageResource(R.drawable.icon_jiage_no);
        mTitlePostCouponPriceIv.setImageResource(R.drawable.icon_jiage_no);
        mTitleCommissionIv.setImageResource(R.drawable.icon_jiage_no);

        mTitleSalesVolumeTv.setSelected(false);
        mTitlePostCouponPriceTv.setSelected(false);
        mTitleCommissionTv.setSelected(false);
        title_comprehensive_tv.setTextColor(Color.parseColor("#999999"));
        mTitleSalesVolumeTv.setTextColor(Color.parseColor("#999999"));
        mTitlePostCouponPriceTv.setTextColor(Color.parseColor("#999999"));
        mTitleCommissionTv.setTextColor(Color.parseColor("#999999"));
        //对点击的设置 图片
        if (clickIv != null) {
            clickIv.setImageResource(getDrawableIdBySortDir(eSortDir));
        }
        if (textView != null) {
            textView.setSelected(true);
            textView.setTextColor(Color.parseColor("#FF645B"));
        }
    }

    private int getDrawableIdBySortDir(int sortDir) {
        int res = R.drawable.icon_jiage_no;
        switch (sortDir) {
            case 0:
                res = R.drawable.icon_jiage_down;
                break;
            case 1:
                res = R.drawable.icon_jiage_up;
                break;
        }
        return res;
    }

    /**
     * 修改优惠卷状态
     */
    private void clickCoupro() {

        if (isCoupon) {
            isCoupon = false;
            coupon="0";
            title_coupon_iv.setImageResource(R.drawable.check_no);
//            couponTv.setTextColor(ContextCompat.getColor(getActivity(),R.color.tv_tablay_text));
//            mRecyclerView.getSwipeList().setRefreshing(true);
            //  getFirstData(keyWord);
            //重新读取数据
        } else {
            isCoupon = true;
            coupon="1";
            title_coupon_iv.setImageResource(R.drawable.check_yes);
//            couponTv.setTextColor(ContextCompat.getColor(getActivity(),R.color.color_333333));
//            mRecyclerView.getSwipeList().setRefreshing(true);
            //getFirstData(keyWord);
            //重新读取数据
        }
    }


}
