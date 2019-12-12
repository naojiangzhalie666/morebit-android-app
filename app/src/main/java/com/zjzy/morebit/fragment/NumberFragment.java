package com.zjzy.morebit.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.ConsComGoodsDetailAdapter;
import com.zjzy.morebit.adapter.SimpleAdapter;
import com.zjzy.morebit.adapter.holder.SimpleViewHolder;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.goods.shopping.contract.NumberGoodsListContract;
import com.zjzy.morebit.goods.shopping.presenter.NumberGoodsListPresenter;
import com.zjzy.morebit.goods.shopping.ui.fragment.BrandSellFragment;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.BrandSell;
import com.zjzy.morebit.pojo.ConsComGoodsInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyLog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主页面中的会员页面
 * 升级入口，会员商品列表
 * Created by haiping.liu on 2019-12-04.
 */
public class NumberFragment extends MvpFragment<NumberGoodsListPresenter> implements NumberGoodsListContract.View {
    private static final int REQUEST_COUNT = 10;
    private int page = 1;
    private View mView;

    private boolean isUserHint = true;
    private boolean isVisible;

    @BindView(R.id.listview)
    ReUseListView mReUseListView;


    @BindView(R.id.status_bar)
    View status_bar;

    @BindView(R.id.dateNullView)
    LinearLayout mDateNullView;


    View headView;

    NumberGoodsAdapter mNumberGoodsAdapter;
    List<NumberGoods> mListArray = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initView(View view) {

        mNumberGoodsAdapter = new NumberGoodsAdapter(getActivity(), mListArray);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //处理全屏显示
            ViewGroup.LayoutParams viewParams = status_bar.getLayoutParams();
            viewParams.height = ActivityStyleUtil.getStatusBarHeight(getActivity());
            status_bar.setLayoutParams(viewParams);
            status_bar.setBackgroundResource(R.color.color_333333);
        }

        headView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_number_header, null);


        mReUseListView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mReUseListView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();

            }
        });
        mReUseListView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mReUseListView.getSwipeList().isRefreshing()) {
                    mPresenter.getNumberGoodsList(NumberFragment.this, page);
                }

            }
        });

        mReUseListView.setAdapterAndHeadView(headView, mNumberGoodsAdapter);


    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_number;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }
//
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyLog.d("setUserVisibleHint", "CircleFragment  " + isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
        }

    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        if (isVisible ) {
//            initData();
//        }
//    }

    @Override
    protected void initData() {
        getData();
    }

    private void refreshData() {
        page = 1;
        mReUseListView.getListView().setNoMore(false);
        mReUseListView.getSwipeList().setRefreshing(true);
        getData();
    }
    private void getData() {
        mPresenter.getNumberGoodsList(this, page);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showSuccessful(List<NumberGoods> datas) {
        MyLog.i("test", ""+ datas.size());
        if (datas.size() == 0) {
            mDateNullView.setVisibility(View.VISIBLE);
            mReUseListView.setVisibility(View.GONE);
            return;
        }
        if (page == 1) {
            mListArray.clear();
            mListArray.addAll(datas);
            mDateNullView.setVisibility(View.GONE);
            mReUseListView.setVisibility(View.VISIBLE);
        } else {
            if (datas.size() == 0) {
                mReUseListView.getListView().setNoMore(true);
            } else {
                mListArray.addAll(datas);
            }

        }
        page++;
        mNumberGoodsAdapter.setData(mListArray);
        mReUseListView.notifyDataSetChanged();
    }

    @Override
    public void showError() {
        MyLog.i("test", "onFailure: " + this);
        if (page == 1) {
            mDateNullView.setVisibility(View.VISIBLE);
            mReUseListView.setVisibility(View.GONE);
        } else {
            mDateNullView.setVisibility(View.GONE);
            mReUseListView.setVisibility(View.VISIBLE);
        }
        mReUseListView.getListView().setNoMore(true);

    }

    @Override
    public void showFinally() {
        mReUseListView.getSwipeList().setRefreshing(false);
        mReUseListView.getListView().refreshComplete(REQUEST_COUNT);
    }

    /**
     * 会员商品适配器
     */
    class NumberGoodsAdapter extends SimpleAdapter<NumberGoods, SimpleViewHolder> {

        List<NumberGoods> datas;
        Context mContext;

        public NumberGoodsAdapter(Context context,List<NumberGoods> datas) {
            super(context);
            this.datas = datas;
            mContext = context;

        }
        public void setData(List<NumberGoods> datas) {
            this.datas.clear();
            if (datas != null && datas.size() > 0) {
                this.datas.addAll(datas);
            }
        }
        @Override
        public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
            if (holder instanceof NumberGoodsHolder ){
                bindNumberGoodsHolder((NumberGoodsHolder) holder,position);
            }

        }

        private void bindNumberGoodsHolder(NumberGoodsHolder holder, int position) {
            final NumberGoods goods =  getItem(position);
            String img = goods.getImg();
            if (!TextUtils.isEmpty(img)) {
                LoadImgUtils.setImg(mContext, holder.pic, img);
            }
            holder.desc.setText(goods.getDesc());
            String price = goods.getPrice();
            if (TextUtils.isEmpty(price)){
                holder.price.setText(getResources().getString(R.string.number_goods_price,"0"));
            }else{
                holder.price.setText(getResources().getString(R.string.number_goods_price,price));
            }
            holder.morebitCorn.setText(MathUtils.getMorebitCorn(price));
            holder.itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                }
            });

        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new NumberGoodsHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_number_goods, parent, false));
        }

        public class NumberGoodsHolder extends SimpleViewHolder {
            @BindView(R.id.number_goods_pic)
            ImageView pic;

            @BindView(R.id.number_goods_desc)
            TextView desc;

            @BindView(R.id.number_goods_price)
            TextView price;

            @BindView(R.id.txt_morebit_corn)
            TextView morebitCorn;

            public NumberGoodsHolder(View itemView) {
                super(itemView);
            }
        }
    }

}
