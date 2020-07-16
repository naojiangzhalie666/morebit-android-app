package com.zjzy.morebit.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.wireless.security.open.middletier.fc.IFCActionCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.ShoppingListForPddAdapter;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.main.ui.fragment.GuessSearchLikeFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UI.BaseTitleTabBean;
import com.zjzy.morebit.pojo.event.SearchGoodsForPddEvent;
import com.zjzy.morebit.pojo.event.SearchGoodsForTaobaoEvent;
import com.zjzy.morebit.pojo.request.RequestSearchForPddBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.UI.ActivityUtils;
import com.zjzy.morebit.utils.ViewShowUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 拼多多的搜索结果展示
 */

public class SearchResultForPddFragment extends BaseMainFragmeng implements View.OnClickListener {

    private ShoppingListForPddAdapter mAdapter;
    private List<ShopGoodInfo> listArray = new ArrayList<>();
    private String keyWord = "";

    private static final int REQUEST_COUNT = 10;

    private View mView;


    private int mPage = 1;
    private ArrayList<BaseTitleTabBean> tabList = new ArrayList<>();
    private int mSelectedPos;
    @BindView(R.id.searchNullTips_ly)
    LinearLayout searchNullTips_ly;
    @BindView(R.id.listview)
    ReUseListView mRecyclerView;
    @BindView(R.id.dataList_ly)
    LinearLayout dataList_ly;
    boolean isUserHint =true;
    private int mPushType;
    private boolean zong=true;
    private boolean yong1=false;
    private boolean yong2=false;

    //销量
    private LinearLayout mTitleSalesVolumeLl,title_zong_volume_ll;
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

    private TextView title_comprehensive_tv;
    private ImageView title_comprehensive_iv;
    //排序方向
    private int eSortDirection = C.OrderType.E_UPLIMIT_SORT_DOWN;// 0降序  1升序
    //排序类型
    private long mSortType = 0;//排序类型 0 综合排序 2 销量排序 3 价格排序 4 奖励排序
    private ImageView yimg3,yimg2;
    private String minPrice,maxprice;
    private boolean isCoupon = false;//优惠券是否选中
    private String coupon = "0";
    private FrameLayout emityfragment;





    public static SearchResultForPddFragment newInstance(int type) {
        SearchResultForPddFragment searchResultForPddFragment = new SearchResultForPddFragment();
        Bundle args = new Bundle();
        args.putInt(C.Extras.pushType, type);
        searchResultForPddFragment.setArguments(args);
        return searchResultForPddFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_searchcommodity_pdd, container, false);
        initBundle();
            initmData(mView);
        return mView;
    }

    private void initmData(View view) {
        emityfragment=view.findViewById(R.id.emityfragment);//猜你喜欢
        title_zong_volume_ll = view.findViewById(R.id.title_zong_volume_ll);//综合
        title_comprehensive_tv=view.findViewById(R.id.title_comprehensive_tv);
        title_comprehensive_iv=view.findViewById(R.id.title_comprehensive_iv);


        mTitleSalesVolumeLl = view.findViewById(R.id.title_sales_volume_ll);//销量
        mTitleSalesVolumeTv = view.findViewById(R.id.title_sales_volume_tv);
        mTitleSalesVolumeIv = view.findViewById(R.id.title_sales_volume_iv);

        mTitlePostCouponPriceLl = view.findViewById(R.id.title_post_coupon_price__ll);//价格
        mTitlePostCouponPriceTv = view.findViewById(R.id.title_post_coupon_price_tv);
        mTitlePostCouponPriceIv = view.findViewById(R.id.title_post_coupon_price_iv);

        mTitleCommissionLl = view.findViewById(R.id.title_commission_ll);//奖励
        mTitleCommissionTv = view.findViewById(R.id.title_commission_tv);
        mTitleCommissionIv = view.findViewById(R.id.title_commission_iv);

        yimg3=view.findViewById(R.id.yimg3);//优惠券开关
        yimg2=view.findViewById(R.id.yimg2);


        yimg3.setOnClickListener(this);
        title_zong_volume_ll.setOnClickListener(this);
        mTitleSalesVolumeLl.setOnClickListener(this);
        mTitlePostCouponPriceLl.setOnClickListener(this);
        mTitleCommissionLl.setOnClickListener(this);
        title_comprehensive_tv.setOnClickListener(this);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyLog.d("setUserVisibleHint", "CircleFragment  " + isVisibleToUser);
        if (isVisibleToUser && isUserHint && mView != null&&mPushType == 2) {
            initView();
            isUserHint = false;

        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPushType = getArguments().getInt(C.Extras.pushType);
        if (mPushType == 2) {
            initView();
        }



    }
    private Bundle bundle;
    private void initBundle() {
        bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            keyWord = getActivity().getIntent().getStringExtra("keyWord");
            //mSearchType = getIntent().getIntExtra(KEY_SEARCH_TYPE,SEARCH_APP);
            //fromSearch = getIntent().getIntExtra(KEY_SEARCH_FROM,FROM_SELF);
        }
    }



    /**
     * 重新加载数据
     */
    private void reLoadData() {
        getFirstData(keyWord);
    }

    public void initView() {
        GuessSearchLikeFragment mLikeFragment = GuessSearchLikeFragment.newInstance();
        ActivityUtils.replaceFragmentToActivity(
                getChildFragmentManager(), mLikeFragment, R.id.emityfragment);
        ActivityStyleUtil.initSystemBar(getActivity(), R.color.white); //设置标题栏颜色值
        mAdapter = new ShoppingListForPddAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstData(keyWord);
            }
        });

        mRecyclerView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mRecyclerView.getSwipeList().isRefreshing())
                    getMoreData();
            }
        });
        //默认选择第一个
        reLoadData();
    }





    /*
     * 第一次获取数据
     */
    public void getFirstData(String keyWords) {
        if (TextUtils.isEmpty(keyWords)) {
           mRecyclerView.getSwipeList().setRefreshing(false);
            if (!isUserHint){
                ViewShowUtils.showShortToast(getActivity(), "请输入搜索内容");
            }
            return;
        }
        keyWord = keyWords;
        mPage = 1;
        fristSearch(keyWord);
//        mRecyclerView.getListView().setNoMore(false);
//        mRecyclerView.getSwipeList().setRefreshing(true);
    }

    private void fristSearch(String keywords) {

            mRecyclerView.getSwipeList().setRefreshing(true);
            mRecyclerView.getListView().setNoMore(false);
            getObservable( keywords)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            mRecyclerView.getSwipeList().setRefreshing(false);
                        }
                    })
                    .subscribe(new DataObserver<List<ShopGoodInfo> >() {
                        @Override
                        protected void onError(String errorMsg, String errCode) {
                            emityfragment.setVisibility(View.VISIBLE);
                            dataList_ly.setVisibility(View.GONE);
                        }

                        @Override
                        protected void onSuccess(List<ShopGoodInfo> data) {
                            mRecyclerView.getSwipeList().setRefreshing(false);
                            dataList_ly.setVisibility(View.VISIBLE);
                            if (data != null && data.size() != 0) {
                                listArray.clear();
                                listArray.addAll(data);
                                mAdapter.replace(listArray);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                emityfragment.setVisibility(View.GONE);

                            } else {
                                emityfragment.setVisibility(View.VISIBLE);
                                dataList_ly.setVisibility(View.GONE);
                            }
                            mRecyclerView.notifyDataSetChanged();
                            if (data != null && data.size() == 0){
                                mRecyclerView.getListView().setNoMore(true);
                            }
                        }



                    });



    }

    private Observable<BaseResponse<List<ShopGoodInfo>>> getObservable(String keywords) {
        RequestSearchForPddBean requestBean = new RequestSearchForPddBean();
        if (eSortDirection == 0) {
            requestBean.setOrder("desc");
        } else {
            requestBean.setOrder("asc");
        }
        if (mSortType==0){//综合
            requestBean.setSort("");
        }else if (mSortType==1){//佣金
            requestBean.setSort("commissionShare");
        }else if (mSortType==2){//销量
            requestBean.setSort("inOrderCount30Days");
        }else if (mSortType==3){//价格
            requestBean.setSort("price");
        }
        requestBean.setCoupon(coupon);
        requestBean.setMinPrice(minPrice);
        requestBean.setMaxPrice(maxprice);
        requestBean.setPage(mPage);
        requestBean.setKeyword(keywords);
        return RxHttp.getInstance().getGoodsService().getSearchGoodsListForPdd(requestBean)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(this.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());
    }

    /**
     * 加载更多数据
     */
    public void getMoreData() {
        getMoreSearch(keyWord);
    }

    private void getMoreSearch( String keywords) {
        getObservable(keywords)
                .subscribe(new DataObserver<List<ShopGoodInfo>>() {
                    @Override
                    protected void onDataListEmpty() {
                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                        mRecyclerView.getListView().setNoMore(true);
                    }

                    @Override
                    protected void onSuccess(List<ShopGoodInfo>  data) {
                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                        mPage++;
                        if (data != null) {
                            if (data.size() > 0) {
                                listArray.addAll(data);
                                mAdapter.replace(listArray);
                               // mRecyclerView.notifyDataSetChanged();

                            } else {
                                mRecyclerView.getListView().setNoMore(true);
                            }
                        }
                    }
                });
    }
    @Subscribe  //订阅事件
    public void onEventMainThread(SearchGoodsForPddEvent event) {
        getFirstData(event.getKeyword());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        mPage=1;
        switch (v.getId()) {
            case R.id.title_zong_volume_ll://综合
                showPopupWindow(title_zong_volume_ll);
                requestClickRadar(null, title_comprehensive_tv, 0);
                //  onReload();
                title_comprehensive_iv.setImageResource(R.drawable.zong_tubiao);
                break;
            case R.id.title_sales_volume_ll://销量
                requestClickRadar(mTitleSalesVolumeIv, mTitleSalesVolumeTv, 2);
                title_comprehensive_iv.setImageResource(R.drawable.zong_tubiao2);
                reLoadData();

                break;
            case R.id.title_post_coupon_price__ll://价格
                requestClickRadar(mTitlePostCouponPriceIv, mTitlePostCouponPriceTv, 3);
                title_comprehensive_iv.setImageResource(R.drawable.zong_tubiao2);
                reLoadData();

                break;
            case R.id.title_commission_ll://筛选
                showPopupWindow2(mTitleCommissionLl);
                mTitleCommissionTv.setTextColor(Color.parseColor("#F05557"));
              //  requestClickRadar(null, mTitleCommissionTv, 0);
                mTitleCommissionIv.setImageResource(R.drawable.shaixuan_tubaio2);
                break;
            case R.id.yimg3://优惠券
                if (!isCoupon){
                    coupon="1";
                    yimg3.setImageResource(R.drawable.yhj_kai);
                    isCoupon=true;
                    yimg2.setVisibility(View.VISIBLE);
                }else{
                    coupon="0";
                    yimg3.setImageResource(R.drawable.yhj_guan);
                    isCoupon=false;
                    yimg2.setVisibility(View.GONE);
                }
                reLoadData();
                break;
            case R.id.title_comprehensive_tv:
                showPopupWindow(title_zong_volume_ll);
                break;
        }
    }

    private void requestClickRadar(ImageView clickIv, TextView textView, int orderType) {
        if (orderType == 0) {
            //综合只有降序
            eSortDirection = C.OrderType.E_UPLIMIT_SORT_DOWN;
        } else {
            if (!textView.isSelected()) {
                eSortDirection = C.OrderType.E_UPLIMIT_SORT_DOWN;
            } else {
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

        mTitleSalesVolumeTv.setSelected(false);
        mTitlePostCouponPriceTv.setSelected(false);


        title_comprehensive_tv.setTextColor(Color.parseColor("#999999"));
        title_comprehensive_tv.setTextColor(Color.parseColor("#999999"));
        mTitleSalesVolumeTv.setTextColor(Color.parseColor("#999999"));
        mTitlePostCouponPriceTv.setTextColor(Color.parseColor("#999999"));

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

    private void showPopupWindow2(View view){
        //加载布局
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_shai, null);
        //更改背景颜色
//        inflate.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        final PopupWindow mPopupWindow = new PopupWindow(inflate);
        //设置SelectPicPopupWindow弹出窗体的宽
        mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //点击其他地方隐藏,false为无反应
        mPopupWindow.setFocusable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //对他进行便宜
            mPopupWindow.showAsDropDown(view,0,0, Gravity.BOTTOM);
        }
        //对popupWindow进行显示
        mPopupWindow.update();

        final EditText tv_min = inflate.findViewById(R.id.tv_min);
        final EditText tv_max= inflate.findViewById(R.id.tv_max);
        TextView tv_sure = inflate.findViewById(R.id.tv_sure);
        final TextView tv_chong= inflate.findViewById(R.id.tv_chong);
        if (!TextUtils.isEmpty(minPrice)){
            tv_min.setText(minPrice);
        }else{
            tv_min.setText("");
        }
        if (!TextUtils.isEmpty(maxprice)){
            tv_max.setText(maxprice);
        }else{
            tv_max.setText("");
        }
        tv_sure.setOnClickListener(new View.OnClickListener() {//确定
            @Override
            public void onClick(View v) {
                minPrice=tv_min.getText().toString();
                maxprice=tv_max.getText().toString();

                if (TextUtils.isEmpty(maxprice)&&TextUtils.isEmpty(minPrice)){
                    ToastUtils.showShort("金额不能为空");
                }else{
                    int int1 = Integer.parseInt(minPrice);
                    int int2 = Integer.parseInt(maxprice);
                    if (int1>int2){
                        ToastUtils.showShort("最高价不得低于最低价");
                    }else{
                        reLoadData();
                        mPopupWindow.dismiss();
                        hideInput();
                    }

                }


            }
        });
        tv_chong.setOnClickListener(new View.OnClickListener() {//重置
            @Override
            public void onClick(View v) {

                maxprice="";
                minPrice="";
                hideInput();
                mTitleCommissionTv.setTextColor(Color.parseColor("#333333"));
                mTitleCommissionIv.setImageResource(R.drawable.shaixuan_tubaio);
                reLoadData();
                mPopupWindow.dismiss();

            }
        });
        LinearLayout ll = inflate.findViewById(R.id.ll);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideInput();
                if (TextUtils.isEmpty(maxprice)&&TextUtils.isEmpty(minPrice)){
                    mTitleCommissionTv.setTextColor(Color.parseColor("#333333"));
                    mTitleCommissionIv.setImageResource(R.drawable.shaixuan_tubaio);
                }else{
                    mTitleCommissionTv.setTextColor(Color.parseColor("#F05557"));
                    mTitleCommissionIv.setImageResource(R.drawable.shaixuan_tubaio2);
                }
            }
        });


    }



    private void showPopupWindow(View view){
        //加载布局
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_zong, null);
        //更改背景颜色
//        inflate.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        final PopupWindow mPopupWindow = new PopupWindow(inflate);
        //设置SelectPicPopupWindow弹出窗体的宽
        mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //点击其他地方隐藏,false为无反应
        mPopupWindow.setFocusable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //对他进行便宜
            mPopupWindow.showAsDropDown(view,0,0, Gravity.BOTTOM);
        }
        //对popupWindow进行显示
        mPopupWindow.update();


        final ImageView img1 = inflate.findViewById(R.id.img1);
        final ImageView img2 = inflate.findViewById(R.id.img2);
        final ImageView img3 = inflate.findViewById(R.id.img3);

        final TextView tv1 = inflate.findViewById(R.id.tv1);
        final TextView tv2 = inflate.findViewById(R.id.tv2);
        final TextView tv3 = inflate.findViewById(R.id.tv3);

        RelativeLayout rl1=inflate.findViewById(R.id.rl1);
        RelativeLayout rl2=inflate.findViewById(R.id.rl2);
        RelativeLayout rl3=inflate.findViewById(R.id.rl3);

        if (zong){
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.GONE);
            img3.setVisibility(View.GONE);
            tv1.setTextColor(Color.parseColor("#F05557"));
            tv2.setTextColor(Color.parseColor("#999999"));
            tv3.setTextColor(Color.parseColor("#999999"));
        }else  if (yong1){
            img1.setVisibility(View.GONE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.GONE);
            tv1.setTextColor(Color.parseColor("#999999"));
            tv2.setTextColor(Color.parseColor("#F05557"));
            tv3.setTextColor(Color.parseColor("#999999"));
        }else if (yong2){
            img1.setVisibility(View.GONE);
            img2.setVisibility(View.GONE);
            img3.setVisibility(View.VISIBLE);
            tv1.setTextColor(Color.parseColor("#999999"));
            tv2.setTextColor(Color.parseColor("#999999"));
            tv3.setTextColor(Color.parseColor("#F05557"));
        }
        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zong=true;
                yong1=false;
                yong2=false;

                title_comprehensive_tv.setText("综合");
                title_comprehensive_tv.setTextColor(Color.parseColor("#F05557"));
                eSortDirection=0;
                mSortType=0;
                reLoadData();
                mPopupWindow.dismiss();

            }
        });

        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zong=false;
                yong1=true;
                yong2=false;

                title_comprehensive_tv.setText("佣金比例");
                title_comprehensive_tv.setTextColor(Color.parseColor("#F05557"));
                eSortDirection=0;
                mSortType=1;
                reLoadData();
                mPopupWindow.dismiss();
            }
        });
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zong=false;
                yong1=false;
                yong2=true;
                title_comprehensive_tv.setTextColor(Color.parseColor("#F05557"));
                title_comprehensive_tv.setText("佣金比例");

                eSortDirection=1;
                mSortType=1;
                reLoadData();
                mPopupWindow.dismiss();
            }
        });

    }
    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(
                InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}
