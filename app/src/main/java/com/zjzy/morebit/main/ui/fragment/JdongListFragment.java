package com.zjzy.morebit.main.ui.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.JdListAdapter;
import com.zjzy.morebit.adapter.PddJdListAdapter;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.main.contract.PddContract;
import com.zjzy.morebit.main.presenter.PddListPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.ProgramCatItemBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UI.BaseTitleTabBean;
import com.zjzy.morebit.pojo.event.LogoutEvent;
import com.zjzy.morebit.pojo.pddjd.PddJdTitleTypeItem;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.SpaceItemDecorationUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by fengrs on 2018/9/10.
 * 备注:京东列表页
 */

public class JdongListFragment extends MvpFragment<PddListPresenter> implements PddContract.View, View.OnClickListener {

    public static String PDDJDTITLETYPEITEM = "PddJdTitleTypeItem"; // 广告加载的跳转info
    @BindView(R.id.mListView)
    RecyclerView rl_list;
    private TabLayout mTabLayout;
    private ArrayList<BaseTitleTabBean> tabList = new ArrayList<>();
    private SmartRefreshLayout mSwipeList;


    private static final int REQUEST_COUNT = 10;
    private JdListAdapter mAdapter;
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
    private long mSortType = 0;//排序类型 0 综合排序 2 销量排序 3 价格排序 4 奖励排序
    //自营
    private LinearLayout title_support_ll;
    private ImageView title_support_iv;

    //优惠券
    private LinearLayout title_coupon_ll;
    private ImageView title_coupon_iv;
    private boolean isCoupon = false;//优惠券是否选中
    private boolean isSupport = false;//自营是否选中
    private String coupon="0";
    private String self="0";
    private int page=1;








    public static JdongListFragment newInstance(PddJdTitleTypeItem bean) {
        JdongListFragment fragment = new JdongListFragment();
        Bundle args = new Bundle();
        args.putSerializable(JdongListFragment.PDDJDTITLETYPEITEM, bean);
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
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tl_tab);
        if (getArguments() == null) return;
        mPddJdTitleTypeItem = (PddJdTitleTypeItem) getArguments().getSerializable(JdongListFragment.PDDJDTITLETYPEITEM);

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


        title_support_ll=view.findViewById(R.id.title_support_ll);//自营
        title_support_iv=view.findViewById(R.id.title_support_iv);

        title_coupon_ll=view.findViewById(R.id.title_coupon_ll);//优惠券
        title_coupon_iv=view.findViewById(R.id.title_coupon_iv);


        mTitleSalesVolumeLl.setOnClickListener(this);
        mTitlePostCouponPriceLl.setOnClickListener(this);
        mTitleCommissionLl.setOnClickListener(this);
        title_comprehensive_tv.setOnClickListener(this);
        title_coupon_ll.setOnClickListener(this);
        title_support_ll.setOnClickListener(this);



        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshjd");//名字
        getActivity().registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        //设置图标的间距
        rl_list.setLayoutManager(manager);
        mSwipeList=view.findViewById(R.id.swipeList);
        mSwipeList.setEnableRefresh(false);
        mSwipeList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                onReload();
            }
        });



    }




    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshjd")) {  //接收到广播通知的名字，在当前页面应与注册名称一致
                referShload();//刷新
            }
        }
    };

    private void referShload() {
        page=1;
        onReload();
    }


    /**
     * 获取资源ID
     *
     * @return
     */
    @Override
    protected int getViewLayout() {
        return R.layout.fragment_jd_list;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }


    public void onReload() {
        ProgramCatItemBean programCatItemBean = new ProgramCatItemBean();
        programCatItemBean.setEliteId(mPddJdTitleTypeItem.getEliteId());
        programCatItemBean.setPage(page);
        if (eSortDirection==0){
            programCatItemBean.setOrder("desc");
        }else{
            programCatItemBean.setOrder("asc");
        }
        programCatItemBean.setSort(String.valueOf(mSortType));
        programCatItemBean.setCoupon(coupon);
        programCatItemBean.setSelf(self);
        mPresenter.getJdGoodsList(this,  programCatItemBean, C.requestType.initData);
    }


//    public void onLoadMore() {
//        if (mPddJdTitleTypeItem == null) return;
//        ProgramCatItemBean programCatItemBean = new ProgramCatItemBean();
//        programCatItemBean.setEliteId(mPddJdTitleTypeItem.getEliteId());
//        programCatItemBean.setCoupon(coupon);
//        if (eSortDirection==0){
//            programCatItemBean.setOrder("desc");
//        }else{
//            programCatItemBean.setOrder("asc");
//        }
//        programCatItemBean.setSort(String.valueOf(mSortType));
//        programCatItemBean.setSelf(self);
//        mPresenter.getJdGoodsList(this, programCatItemBean,C.requestType.loadMore);
//    }




    ArrayList<ShopGoodInfo> mData = new ArrayList<>();

    @Override
    public void showFinally() {


    }


    @Override
    public void setPdd(List<ShopGoodInfo> data, int loadType) {

    }

    @Override
    public void setPddError(int loadType) {

    }

    @Override
    public void setJd(List<ShopGoodInfo> data, int loadType) {
        if (page==1) {
            mAdapter = new JdListAdapter(getActivity(), data);
            if (mAdapter!=null){
                rl_list.setAdapter(mAdapter);
            }
        } else  {
            mAdapter.setData(data);
            mSwipeList.finishLoadMore(true);
        }
    }
    @Override
    public void setJdError(int loadType) {
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mRefreshBroadcastReceiver);
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.title_comprehensive_tv://综合
                    requestClickRadar(null, title_comprehensive_tv, 0);
                    onReload();
                    break;
                case R.id.title_sales_volume_ll://销量
                    requestClickRadar(mTitleSalesVolumeIv, mTitleSalesVolumeTv, 4);
                    onReload();
                    break;
                case R.id.title_post_coupon_price__ll://价格
                    requestClickRadar(mTitlePostCouponPriceIv, mTitlePostCouponPriceTv, 1);
                    onReload();
                    break;
                case R.id.title_commission_ll://奖励
                    requestClickRadar(mTitleCommissionIv, mTitleCommissionTv, 3);
                    onReload();
                    break;
                case R.id.title_support_ll://自营
                    clickSupport();
                    onReload();
                    break;
                case R.id.title_coupon_ll://优惠券
                    clickCoupro();
                    onReload();
                    break;
            }
    }

    private void requestClickRadar(ImageView clickIv, TextView textView, int orderType) {
        if (orderType == 0) {
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
        } else {
            isCoupon = true;
            coupon="1";
            title_coupon_iv.setImageResource(R.drawable.check_yes);
        }
    }

    /**
     * 修改自营状态
     */
    private void clickSupport() {

        if (isSupport) {
            isSupport = false;
            self="0";
            title_support_iv.setImageResource(R.drawable.check_no);
        } else {
            isSupport = true;
            self="1";
            title_support_iv.setImageResource(R.drawable.check_yes);
        }
    }




}
