package com.zjzy.morebit.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.ShoppingListAdapter;
import com.zjzy.morebit.adapter.TbSearchAdapter;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShopGoodBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UI.BaseTitleTabBean;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.event.SearchGoodsForTaobaoEvent;
import com.zjzy.morebit.pojo.request.RequestSearchBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.MyLog;
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
 * 搜索结果展示
 */

public class SearchResultForTaobaoFragment extends BaseMainFragmeng implements View.OnClickListener {
    public static final String KEY_SEARCH_TYPE = "searchType";
    public static final String KEY_SEARCH_FROM = "searchFrom";
    public  static final int SEARCH_APP = 1; //搜索APP
    public static final int SEARCH_NETWORK = 0; //搜索全网
    public  static final int FROM_SELF = 2; //来自搜索界面
    public static final int FROM_EXTERNAL = 3; //来自外部全局的搜索
    private TbSearchAdapter mAdapter;
    private List<ShopGoodInfo> listArray = new ArrayList<>();
    private List<ShopGoodInfo> withinArray = new ArrayList<>(); //内网
    private List<ShopGoodInfo> outerArray = new ArrayList<>(); // 全网
    private String keyWord = "";
    private Bundle bundle;

    private static final int REQUEST_COUNT = 10;

    private TabLayout mTabLayout;
    private boolean isCouponShowOff = false;
    private String mMinId = "1";
    private String mPage = "1";
    private boolean isOneSwitchConpon = true; // 是否第一次切换优惠卷
    private ArrayList<BaseTitleTabBean> tabList = new ArrayList<>();
    private int mSelectedPos;
    @BindView(R.id.searchNullTips_ly)
    LinearLayout searchNullTips_ly;
    @BindView(R.id.listview)
    ReUseListView mRecyclerView;
//    @BindView(R.id.search_et)
//    EditText etSearch;
    @BindView(R.id.dataList_ly)
    LinearLayout dataList_ly;
//    @BindView(R.id.list_serach_key)
//    ListView mListView;
//    @BindView(R.id.iv_clear)
//    ImageView iv_clear;

//    private ArrayList<String> mTaobaoSearchData = new ArrayList<>();
//    private SearchHotAdapter mTaobaoSearchAdapter;
    boolean isClserSearch = false;
    private boolean isFrist = true;
    private int mSearchType =1;
    private int fromSearch = FROM_SELF;
    private boolean isLoadData = false;
    private View mView;
    boolean isUserHint =true;
    private int mPushType;
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
    private boolean zong=true;
    private boolean yong1=false;
    private boolean yong2=false;


    public static SearchResultForTaobaoFragment newInstance(int type) {
        SearchResultForTaobaoFragment searchResultForTaobaoFragment = new SearchResultForTaobaoFragment();
        Bundle args = new Bundle();
        args.putInt(C.Extras.pushType, type);
        searchResultForTaobaoFragment.setArguments(args);
        return searchResultForTaobaoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_searchcommodity, container, false);
        initmData(mView);
        return mView;
    }

    private void initmData(View view) {
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPushType = getArguments().getInt(C.Extras.pushType);
        if (mPushType == 1) {
            initBundle();
            initView();
        }



    }

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
        ActivityStyleUtil.initSystemBar(getActivity(), R.color.white); //设置标题栏颜色值
        mAdapter = new TbSearchAdapter(getActivity());
        mAdapter.setShowHotTag(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.getSwipeList().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        mRecyclerView.getListView().setItemAnimator(null);

//        mRecyclerView.getListView().getItemAnimator().setAddDuration(0);
//        mRecyclerView.getListView().getItemAnimator().setRemoveDuration(0);
//        mRecyclerView.getListView().getItemAnimator().setChangeDuration(0);
//        mRecyclerView.getListView().getItemAnimator().setMoveDuration(0);
//        ((SimpleItemAnimator) mRecyclerView.getListView().getItemAnimator()).setSupportsChangeAnimations(false);





        //默认选择第一个
        reLoadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyLog.d("setUserVisibleHint", "taobaoFragment  " + isVisibleToUser);
        if (isVisibleToUser && isUserHint && mView != null&&mPushType == 2) {
            initView();
            isUserHint = false;
        }

    }









    /*
     * 第一次获取数据
     */
    public void getFirstData(String keyWords) {
        keyWord = keyWords;
        if (TextUtils.isEmpty(keyWord)) {
            mRecyclerView.getSwipeList().setRefreshing(false);
            ViewShowUtils.showShortToast(getActivity(), "请输入搜索内容");
            return;
        }

        mPage = "1";
        mMinId = "1";
        mSearchType =1;
        mRecyclerView.getListView().setNoMore(false);
        mRecyclerView.getSwipeList().setRefreshing(true);
        fristSearch(coupon, keyWord, mMinId);
    }

    private void fristSearch(String coupon, String keywords, String minId) {
        mRecyclerView.getSwipeList().setRefreshing(true);
        getObservable(coupon, keywords, minId)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRecyclerView.getSwipeList().setRefreshing(false);

                    }
                })
                .subscribe(new DataObserver<ShopGoodBean>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        listArray.clear();
                        mAdapter.setData(listArray);
                      mRecyclerView.notifyDataSetChanged();
                        if (isOneSwitchConpon) {
                            isOneSwitchConpon = false;
                        } else {
                            searchNullTips_ly.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    protected void onSuccess(ShopGoodBean data) {
                        mRecyclerView.getSwipeList().setRefreshing(false);
                        dataList_ly.setVisibility(View.VISIBLE);

                        List<ShopGoodInfo> goodsList = data.getData();
                        if (goodsList != null && goodsList.size() != 0) {
                            mMinId = data.getMinId();
                            mPage = data.getPage();
                            mSearchType = data.getSearchType();
                            searchNullTips_ly.setVisibility(View.GONE);
                            listArray.clear();
                            List<ShopGoodInfo> shopGoodInfos = partList(goodsList);
                            mAdapter.setData(shopGoodInfos);
                            //设置是否是代理商
                            UserInfo userInfo = UserLocalData.getUser(getActivity());
                          //  mAdapter.notifyItemRangeChanged(0,shopGoodInfos.size());
                          mRecyclerView.notifyDataSetChanged();
                        } else {
                             listArray.clear();
                            mAdapter.setData(listArray);
                          mRecyclerView.notifyDataSetChanged();
                           // mAdapter.notifyItemRangeChanged(0,listArray.size());

                            if (isOneSwitchConpon) {
                                isOneSwitchConpon = false;
                            } else {
                                searchNullTips_ly.setVisibility(View.VISIBLE);
                            }
                        }
                        if (data.getIsPageEnd() == 1){
                            mRecyclerView.getListView().setNoMore(true);
                        }
                    }
                });
    }

    private Observable<BaseResponse<ShopGoodBean>> getObservable(String coupon, String keywords, String min_id) {
        RequestSearchBean requestBean = new RequestSearchBean();
        if (eSortDirection == 0) {
            requestBean.setOrder("desc");
        } else {
            requestBean.setOrder("asc");
        }
        if (mSortType==0){//综合
            requestBean.setSort("");
        }else if (mSortType==2){//佣金
            requestBean.setSort("commission");
        }else if (mSortType==4){//销量
            requestBean.setSort("saleMonth");
        }else if (mSortType==1){//价格
            requestBean.setSort("itemVoucherPrice");
        }
        requestBean.setMaxPrice(maxprice);
        requestBean.setMinPrice(minPrice);
        requestBean.setPage(mPage);
        requestBean.setCoupon(coupon);
        requestBean.setKeywords(keywords);
        requestBean.setMinId(min_id + "");
        requestBean.setSearchType(mSearchType);
        return RxHttp.getInstance().getGoodsService().getSearchGoodsList(requestBean)
                .compose(RxUtils.<BaseResponse<ShopGoodBean>>switchSchedulers())
                .compose(this.<BaseResponse<ShopGoodBean>>bindToLifecycle());

    }

    /**
     * 加载更多数据
     */
    public void getMoreData() {
//        keyWord = etSearch.getText().toString().trim();

        getMoreSearch(coupon, keyWord, mMinId);
    }

    private void getMoreSearch(String coupon, String keywords, String minId) {
        getObservable(coupon, keywords, minId)
                .subscribe(new DataObserver<ShopGoodBean>() {
                    @Override
                    protected void onDataListEmpty() {
                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                        mRecyclerView.getListView().setNoMore(true);
                    }

                    @Override
                    protected void onSuccess(ShopGoodBean data) {
                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                        List<ShopGoodInfo> goodsList = data.getData();
                        if (goodsList != null) {
                            if (goodsList.size() > 0) {
                                mMinId = data.getMinId();
                                mPage = data.getPage();
                                mSearchType = data.getSearchType();
                                List<ShopGoodInfo> shopGoodInfos = partList(goodsList);
                                mAdapter.setData(shopGoodInfos);
                                //设置是否是代理商
                                mRecyclerView.notifyDataSetChanged();
                            } else {
                                mRecyclerView.getListView().setNoMore(true);
                            }
                        }
                        if (data.getIsPageEnd() == 1){
                            mRecyclerView.getListView().setNoMore(true);
                        }
                    }
                });
    }

    /**
     * 分离全网 和内网
     *
     * @param goodsList
     */
    private List<ShopGoodInfo> partList(List<ShopGoodInfo> goodsList) {
        listArray.addAll(goodsList);
        outerArray.clear();
        withinArray.clear();
        for (int i = 0; i < listArray.size(); i++) {
            ShopGoodInfo shopGoodInfo = listArray.get(i);
            if (shopGoodInfo.isSearchType == 1) { // 排除分割
                continue;
            }
            if ("0".equals(shopGoodInfo.getItemFrom())) {//全给
                outerArray.add(shopGoodInfo);
            } else {
                withinArray.add(shopGoodInfo);
            }
        }
        listArray.clear();
        listArray.addAll(withinArray);
        if (outerArray != null && outerArray.size() != 0) {
            listArray.add(new ShopGoodInfo(1));
            listArray.addAll(outerArray);
        }
        return listArray;
    }








    @Subscribe  //订阅事件
    public void onEventMainThread(SearchGoodsForTaobaoEvent event) {
        getFirstData(event.getKeyword());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        mPage="1";
        switch (v.getId()) {
            case R.id.title_zong_volume_ll://综合
                showPopupWindow(title_zong_volume_ll);
                requestClickRadar(null, title_comprehensive_tv, 0);
                //  onReload();
                mTitleCommissionIv.setImageResource(R.drawable.shaixuan_tubaio);
                title_comprehensive_iv.setImageResource(R.drawable.zong_tubiao);
                break;
            case R.id.title_sales_volume_ll://销量
                requestClickRadar(mTitleSalesVolumeIv, mTitleSalesVolumeTv, 4);
                reLoadData();
                mTitleCommissionIv.setImageResource(R.drawable.shaixuan_tubaio);
                title_comprehensive_iv.setImageResource(R.drawable.zong_tubiao2);
                break;
            case R.id.title_post_coupon_price__ll://价格
                requestClickRadar(mTitlePostCouponPriceIv, mTitlePostCouponPriceTv, 1);
                reLoadData();
                mTitleCommissionIv.setImageResource(R.drawable.shaixuan_tubaio);
                title_comprehensive_iv.setImageResource(R.drawable.zong_tubiao2);
                break;
            case R.id.title_commission_ll://筛选
                showPopupWindow2(mTitleCommissionLl);
                requestClickRadar(null, mTitleCommissionTv, 3);
                mTitleCommissionIv.setImageResource(R.drawable.shaixuan_tubaio2);
                title_comprehensive_iv.setImageResource(R.drawable.zong_tubiao2);
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
        mTitleCommissionTv.setSelected(false);

        title_comprehensive_tv.setTextColor(Color.parseColor("#999999"));
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
        }
        if (!TextUtils.isEmpty(maxprice)){
            tv_max.setText(maxprice);
        }
        tv_sure.setOnClickListener(new View.OnClickListener() {//确定
            @Override
            public void onClick(View v) {
                minPrice=tv_min.getText().toString();
                maxprice=tv_max.getText().toString();
                int int1 = Integer.parseInt(minPrice);
                int int2 = Integer.parseInt(maxprice);
                if (TextUtils.isEmpty(maxprice)&&TextUtils.isEmpty(minPrice)){
                    ToastUtils.showShort("金额不能为空");
                }else{
                    if (int1>int2){
                        ToastUtils.showShort("最高价不得低于最低价");
                    }else{
                        hideInput();
                        reLoadData();
                        mPopupWindow.dismiss();
                    }

                }



            }
        });
        tv_chong.setOnClickListener(new View.OnClickListener() {//重置
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                hideInput();

            }
        });



    }



    private void showPopupWindow(View view){
        //加载布局
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_zong, null);
        //更改背景颜色m
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
                title_comprehensive_tv.setTextColor(Color.parseColor("#F05557"));
                title_comprehensive_tv.setText("综合");

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
                title_comprehensive_tv.setTextColor(Color.parseColor("#F05557"));
                title_comprehensive_tv.setText("佣金比例");

                eSortDirection=0;
                mSortType=2;
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
                mSortType=2;
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
