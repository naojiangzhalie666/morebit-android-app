package com.zjzy.morebit.main.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.NineAdapter;
import com.zjzy.morebit.adapter.RankingAdapter;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ProgramCatItemBean;
import com.zjzy.morebit.pojo.RankTimeBean;
import com.zjzy.morebit.pojo.RequestNineBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.request.RequestBannerBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MathUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;


/**
 * 首页分类-9.9包邮
 */
public class NineFragment extends BaseMainFragmeng implements View.OnClickListener {


    @BindView(R.id.iv_head_bg)
    ImageView iv_head_bg;


    private View mView;
    private int mHeadBgHeight;
    private RecyclerView rcy_rank;
    private NestedScrollView nscorll;
    private int page = 1;
    private NineAdapter nineAdapter;
    //销量
    private LinearLayout mTitleSalesVolumeLl;
    private ImageView mTitleSalesVolumeIv;
    private TextView mTitleSalesVolumeTv;
    //券后价
    private LinearLayout mTitlePostCouponPriceLl;
    private ImageView mTitlePostCouponPriceIv;
    private TextView mTitlePostCouponPriceTv;
    private String order="desc";
    private String sort="itemIndex";


    private TextView title_comprehensive_tv;
    //排序方向
    private int eSortDirection = C.OrderType.E_UPLIMIT_SORT_DOWN;// 0降序  1升序
    //排序类型
    private long mSortType = 0;//排序类型 0 综合排序 2 销量排序 3 价格排序 4 奖励排序


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_nine, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.with(getActivity())
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(false)
                .statusBarColor(R.color.white)
                //解决状态栏和布局重叠问题，任选其一
                .init();
        initView(mView);

        initmData();


    }

    private void initmData() {
        if (mSortType==0){
            sort="itemIndex";
        }else if (mSortType==1){
            sort="itemVoucherPrice";
        }else{
            sort="saleMonth";
        }
        if (eSortDirection==0){
            order="desc";
        }else{
            order="asc";
        }

        getFreeShipping(this, page,order,sort)
                .subscribe(new DataObserver<List<ShopGoodInfo>>(false) {
                    @Override
                    protected void onDataListEmpty() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onDataNull() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onActivityFailure();
                    }

                    @Override
                    protected void onSuccess(List<ShopGoodInfo> data) {
                        onGetRealTime(data);
                    }
                });

    }

    private void onGetRealTime(List<ShopGoodInfo> data) {
        if (page == 1) {
            nineAdapter = new NineAdapter(getActivity(), data);
            rcy_rank.setAdapter(nineAdapter);
        } else {
            nineAdapter.setData(data);
        }
    }

    private void onActivityFailure() {

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        getActivity().finish();
    }

    /**
     * 初始化界面
     */
    private void initView(View view) {


        title_comprehensive_tv=view.findViewById(R.id.title_comprehensive_tv);//综合

        title_comprehensive_tv.setOnClickListener(this);

        mTitleSalesVolumeLl = view.findViewById(R.id.title_sales_volume_ll);//劵后价
        mTitleSalesVolumeTv = view.findViewById(R.id.title_sales_volume_tv);
        mTitleSalesVolumeIv = view.findViewById(R.id.title_sales_volume_iv);
        mTitleSalesVolumeLl.setOnClickListener(this);
        mTitlePostCouponPriceLl = view.findViewById(R.id.title_post_coupon_price__ll);//销量
        mTitlePostCouponPriceTv = view.findViewById(R.id.title_post_coupon_price_tv);
        mTitlePostCouponPriceIv = view.findViewById(R.id.title_post_coupon_price_iv);
        mTitlePostCouponPriceLl.setOnClickListener(this);

        rcy_rank = view.findViewById(R.id.rcy_rank);
        nscorll = view.findViewById(R.id.nscorll);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rcy_rank.setLayoutManager(manager);
        rcy_rank.setNestedScrollingEnabled(false);
        nscorll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //判断是否滑到的底部
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    page++;
                    initmData();

                }

            }
        });

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

        mTitleSalesVolumeTv.setSelected(false);
        mTitlePostCouponPriceTv.setSelected(false);

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

    //9.9包邮
    public Observable<BaseResponse<List<ShopGoodInfo>>> getFreeShipping(RxFragment fragment, int page,String order,String sort) {
        RequestNineBean bean =new RequestNineBean();
        bean.setOrder(order);
        bean.setPage(page);
        bean.setSort(sort);
        return RxHttp.getInstance().getSysteService().getFreeShipping(bean)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());
    }

    @Override
    public void onClick(View v) {
        page=1;
        switch (v.getId()){
            case R.id.title_comprehensive_tv://综合
                requestClickRadar(null, title_comprehensive_tv, 0);
                initmData();
                break;
            case R.id.title_sales_volume_ll://劵后价
                requestClickRadar(mTitleSalesVolumeIv, mTitleSalesVolumeTv, 1);
                initmData();
                break;
            case R.id.title_post_coupon_price__ll://销量
                requestClickRadar(mTitlePostCouponPriceIv, mTitlePostCouponPriceTv, 2);
                initmData();
                break;
        }
    }
}
