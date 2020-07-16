package com.zjzy.morebit.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.JdListAdapter;
import com.zjzy.morebit.adapter.WphListAdapter;
import com.zjzy.morebit.main.contract.PddContract;
import com.zjzy.morebit.main.presenter.PddListPresenter;
import com.zjzy.morebit.main.ui.fragment.GuessSearchLikeFragment;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.ProgramCatItemBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.event.SearchGoodsForJdEvent;
import com.zjzy.morebit.pojo.event.SearchGoodsForWphEvent;
import com.zjzy.morebit.pojo.request.ProgramWphBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.UI.ActivityUtils;
import com.zjzy.morebit.utils.ViewShowUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 京东的搜索结果展示
 */

public class SearchResultForWphFragment extends MvpFragment<PddListPresenter> implements PddContract.View, View.OnClickListener {

    private WphListAdapter mAdapter;
    private String keyWord = "";

    private static final int REQUEST_COUNT = 10;

    private View mView;
    @BindView(R.id.searchNullTips_ly)
    LinearLayout searchNullTips_ly;
    @BindView(R.id.listview)
    RecyclerView mRecyclerView;
    @BindView(R.id.dataList_ly)
    LinearLayout dataList_ly;
    private String minPrice,maxprice;
    boolean isUserHint = true;
    private int mPushType;
    private List<ShopGoodInfo> listArray = new ArrayList<>();


    //券后价
    private LinearLayout mTitlePostCouponPriceLl,title_zong_volume_ll;
    private ImageView mTitlePostCouponPriceIv;
    private TextView mTitlePostCouponPriceTv;
    //佣金
    private LinearLayout mTitleCommissionLl;
    private ImageView mTitleCommissionIv;
    private TextView mTitleCommissionTv;

    private TextView title_comprehensive_tv;
    //排序方向
    private int eSortDirection = C.OrderType.E_UPLIMIT_SORT_DOWN;// 0降序  1升序
    //排序类型
    private long mSortType = 0;//排序类型 0 综合排序 2 销量排序 3 价格排序 4 奖励排序
    private int page=1;
    private SmartRefreshLayout mSwipeList;

    private LinearLayout title_commission_ll2;
    private TextView title_commission_tv2;
    private ImageView title_commission_iv2,title_comprehensive_iv;
    private FrameLayout emityfragment;




    public static SearchResultForWphFragment newInstance(int type) {
        SearchResultForWphFragment searchResultForjdFragment = new SearchResultForWphFragment();
        Bundle args = new Bundle();
        args.putInt(C.Extras.pushType, type);
        searchResultForjdFragment.setArguments(args);
        return searchResultForjdFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_searchcommodity_wph, container, false);
        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyLog.d("setUserVisibleHint", "CircleFragment  " + isVisibleToUser);
        if (isVisibleToUser && isUserHint && mView != null && mPushType ==4) {
            initView(mView);
            isUserHint = false;
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPushType = getArguments().getInt(C.Extras.pushType);
        if (mPushType == 4) {
          initView(mView);
        }
        initBundle();

    }

    @Override
    protected void initData() {
        GuessSearchLikeFragment mLikeFragment = GuessSearchLikeFragment.newInstance();
        ActivityUtils.replaceFragmentToActivity(
                getChildFragmentManager(), mLikeFragment, R.id.emityfragment);
    }

    private Bundle bundle;

    private void initBundle() {
        bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            keyWord = getActivity().getIntent().getStringExtra("keyWord");
        }
    }


    public void initView(View view) {
        emityfragment=view.findViewById(R.id.emityfragment);//猜你喜欢
        title_comprehensive_tv = view.findViewById(R.id.title_comprehensive_tv);//综合
        title_zong_volume_ll=view.findViewById(R.id.title_zong_volume_ll);
        title_comprehensive_iv=view.findViewById(R.id.title_comprehensive_iv);


        mTitlePostCouponPriceLl = view.findViewById(R.id.title_post_coupon_price__ll);//价格
        mTitlePostCouponPriceTv = view.findViewById(R.id.title_post_coupon_price_tv);
        mTitlePostCouponPriceIv = view.findViewById(R.id.title_post_coupon_price_iv);

        mTitleCommissionLl = view.findViewById(R.id.title_commission_ll);//奖励
        mTitleCommissionTv = view.findViewById(R.id.title_commission_tv);
        mTitleCommissionIv = view.findViewById(R.id.title_commission_iv);



        mTitlePostCouponPriceLl.setOnClickListener(this);
        mTitleCommissionLl.setOnClickListener(this);
        title_comprehensive_tv.setOnClickListener(this);

        title_commission_ll2 = view.findViewById(R.id.title_commission_ll2);//筛选
        title_commission_tv2 = view.findViewById(R.id.title_commission_tv2);
        title_commission_iv2 = view.findViewById(R.id.title_commission_iv2);
        title_zong_volume_ll.setOnClickListener(this);
        title_commission_ll2.setOnClickListener(this);
        getFirstData(keyWord);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        //设置图标的间距
        mRecyclerView.setLayoutManager(manager);
        mSwipeList=view.findViewById(R.id.swipeList);
        mSwipeList.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                onReload();
                refreshLayout.finishRefresh(true);//刷新完成
            }
        });
        mSwipeList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                onReload();
            }
        });

    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_searchcommodity_jd;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }


    /*
     * 第一次获取数据
     */
    public void getFirstData(String keyWords) {

        if (TextUtils.isEmpty(keyWords)) {
            if (!isUserHint){
                ViewShowUtils.showShortToast(getActivity(), "请输入搜索内容");
            }
            return;
        }
        keyWord = keyWords;
        page=1;
        onReload();

    }

    private void fristSearch(String keywords) {

    }


    @Subscribe  //订阅事件
    public void onEventMainThread(SearchGoodsForWphEvent event) {
        getFirstData(event.getKeyword());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        page=1;
        switch (v.getId()) {
            case R.id.title_zong_volume_ll://综合
                requestClickRadar(null, title_comprehensive_tv, 0);
                onReload();
                title_comprehensive_iv.setImageResource(R.drawable.zong_tubiao);
                break;
            case R.id.title_post_coupon_price__ll://价格
                requestClickRadar(mTitlePostCouponPriceIv, mTitlePostCouponPriceTv, 1);
                onReload();
                title_comprehensive_iv.setImageResource(R.drawable.zong_tubiao2);
                break;
            case R.id.title_commission_ll://销量
                requestClickRadar(mTitleCommissionIv, mTitleCommissionTv, 2);
                onReload();
                title_comprehensive_iv.setImageResource(R.drawable.zong_tubiao2);
                break;
            case R.id.title_commission_ll2://筛选
                showPopupWindow2(title_commission_ll2);
           //     requestClickRadar(null, title_commission_tv2, 4);
                title_commission_iv2.setImageResource(R.drawable.shaixuan_tubaio2);
                title_commission_tv2.setTextColor(Color.parseColor("#F05557"));
                break;
        }
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
                        onReload();
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
                title_commission_tv2.setTextColor(Color.parseColor("#333333"));
                title_commission_iv2.setImageResource(R.drawable.shaixuan_tubaio);
                onReload();
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
                    title_commission_tv2.setTextColor(Color.parseColor("#333333"));
                    title_commission_iv2.setImageResource(R.drawable.shaixuan_tubaio);
                }else{
                    title_commission_iv2.setImageResource(R.drawable.shaixuan_tubaio2);
                    title_commission_tv2.setTextColor(Color.parseColor("#F05557"));
                }
            }
        });


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

        mTitlePostCouponPriceIv.setImageResource(R.drawable.icon_jiage_no);
        mTitleCommissionIv.setImageResource(R.drawable.icon_jiage_no);

        mTitlePostCouponPriceTv.setSelected(false);
        mTitleCommissionTv.setSelected(false);

        title_comprehensive_tv.setTextColor(Color.parseColor("#999999"));
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




    public void onReload() {
        ProgramWphBean programCatItemBean = new ProgramWphBean();
        programCatItemBean.setPage(page);
        programCatItemBean.setKeyword(keyWord);
        programCatItemBean.setMaxPrice(maxprice);
        programCatItemBean.setMinPrice(minPrice);

        if (eSortDirection == 0) {
            programCatItemBean.setOrder("0");
        } else {
            programCatItemBean.setOrder("1");
        }
        if (mSortType==0){
            programCatItemBean.setFieldName("");
        }else  if (mSortType==1){
            programCatItemBean.setFieldName("PRICE");
        }else  if (mSortType==2){
            programCatItemBean.setFieldName("SALES");
        }
        mPresenter.getWphGoodList(this, programCatItemBean, C.requestType.initData);
    }




    @Override
    public void setPdd(List<ShopGoodInfo> data, int loadType) {

    }

    @Override
    public void setPddError(int loadType) {

    }

    @Override
    public void setJd(List<ShopGoodInfo> data, int loadType) {

    }

    @Override
    public void setJdError(int loadType) {


    }

    @Override
    public void setWph(List<ShopGoodInfo> data, int loadType) {
        if (data!=null){
            emityfragment.setVisibility(View.GONE);
            dataList_ly.setVisibility(View.VISIBLE);
            if (page==1) {
                //mData.clear();
                mAdapter = new WphListAdapter(getActivity(), data);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                // rl_list.getListView().refreshComplete(10);
                mAdapter.setData(data);
                mSwipeList.finishLoadMore(false);
            }
        }else{
            emityfragment.setVisibility(View.VISIBLE);
            dataList_ly.setVisibility(View.GONE);
        }

    }

    @Override
    public void setWphError(int loadType) {
        if (page==1){
            mAdapter = new WphListAdapter(getActivity(), listArray);
            mRecyclerView.setAdapter(mAdapter);
            dataList_ly.setVisibility(View.GONE);
            emityfragment.setVisibility(View.VISIBLE);
        }else{
            mSwipeList.finishLoadMore(false);
        }
    }

    @Override
    public void showFinally() {
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
