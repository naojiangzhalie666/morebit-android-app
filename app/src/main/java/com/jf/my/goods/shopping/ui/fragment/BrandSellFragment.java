package com.jf.my.goods.shopping.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.jf.my.Activity.GoodsDetailActivity;
import com.jf.my.Module.common.View.ReUseListView;
import com.jf.my.R;
import com.jf.my.adapter.SimpleAdapter;
import com.jf.my.adapter.holder.SimpleViewHolder;
import com.jf.my.goods.shopping.contract.BrandSellContract;
import com.jf.my.goods.shopping.presenter.BrandSellPresenter;
import com.jf.my.main.ui.fragment.GoodNewsFramgent;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.mvp.base.frame.MvpFragment;
import com.jf.my.pojo.BrandSell;
import com.jf.my.pojo.ImageInfo;
import com.jf.my.pojo.ShopGoodInfo;
import com.jf.my.utils.ActivityStyleUtil;
import com.jf.my.utils.C;
import com.jf.my.utils.LoadImgUtils;
import com.jf.my.utils.MathUtils;
import com.jf.my.utils.MyLog;
import com.jf.my.utils.OpenFragmentUtils;

import java.util.List;

import butterknife.BindView;

/**
 * Created by YangBoTian on 2018/9/11.
 * 品牌特卖
 */

public class BrandSellFragment extends MvpFragment<BrandSellPresenter> implements BrandSellContract.View {
    private static final int REQUEST_COUNT = 10;

    @BindView(R.id.mListView)
    ReUseListView mReUseListView;
    RecyclerView mRecyclerView;

    private int page = 1;
    BrandSellAdapter mBrandAdapter;
    BrandSellAdapter mBrandAndGoodsAdapter;
    View headView;
    RelativeLayout mRlTop;

    public static void start(Activity activity) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(C.Extras.openFragment_isSysBar, true);
        OpenFragmentUtils.goToSimpleFragment(activity, BrandSellFragment.class.getName(), bundle);
    }

    @Override
    protected void initData() {
        refreshData();
    }

    private void refreshData() {
        mPresenter.getBrandSellList(this, page);
        mPresenter.getBrandSellGoodsList(this, page);
    }

    @Override
    protected void initView(View view) {

        headView = LayoutInflater.from(getActivity()).inflate(R.layout.brand_sell_head, null);
        mRecyclerView = (RecyclerView) headView.findViewById(R.id.recyclerview);
        mRlTop = headView.findViewById(R.id.rl_top);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 4);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //处理全屏显示
            LinearLayout.LayoutParams viewParams = (LinearLayout.LayoutParams) mRlTop.getLayoutParams();
            int top = ActivityStyleUtil.getStatusBarHeight(getActivity()) + 10;
            viewParams.setMargins(0, top, 0, 0);
            mRlTop.setLayoutParams(viewParams);
        }
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mBrandAdapter = new BrandSellAdapter(getActivity(), BrandSellAdapter.TYPE_BRAND);
        mBrandAndGoodsAdapter = new BrandSellAdapter(getActivity(), BrandSellAdapter.TYPE_BRAND_GOODS);
        mReUseListView.getSwipeList().setOnRefreshListener(new com.jf.my.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                mReUseListView.getListView().setNoMore(false);
                refreshData();

            }
        });
        mReUseListView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mReUseListView.getSwipeList().isRefreshing()) {
                    mPresenter.getBrandSellGoodsList(BrandSellFragment.this, page);
                }

            }
        });
        mReUseListView.setAdapterAndHeadView(headView, mBrandAndGoodsAdapter);
        mRecyclerView.setAdapter(mBrandAdapter);
        headView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @Override
    protected int getViewLayout() {
        return R.layout.brand_sell;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }


    @Override
    public void showSuccessful(List<BrandSell> brandSells) {
        MyLog.i("test", "size: " + brandSells.size());
        BrandSell bransell = new BrandSell();
        bransell.setBranndName(getString(R.string.check_more));
        if (brandSells.size() > 7) {
            brandSells = brandSells.subList(0, 7);
        }

        brandSells.add(bransell);
        setBrandSells(brandSells);
    }

    @Override
    public void showError() {
        if (page != 1)
            mReUseListView.getListView().setNoMore(true);
    }

    @Override
    public void getGoodsSuccessful(List<BrandSell> brandSells) {
        if (page == 1) {
            mBrandAndGoodsAdapter.replace(brandSells);
        } else {
            if (brandSells.size() == 0) {
                mReUseListView.getListView().setNoMore(true);
            } else {
                mBrandAndGoodsAdapter.add(brandSells);
            }

        }
        page++;
        mBrandAndGoodsAdapter.notifyDataSetChanged();
    }

    private void setBrandSells(List<BrandSell> brandSells) {
        mBrandAdapter.replace(brandSells);
        mBrandAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFinally() {
        mReUseListView.getListView().refreshComplete(REQUEST_COUNT);
        mReUseListView.getSwipeList().setRefreshing(false);
    }


    class BrandSellAdapter extends SimpleAdapter<BrandSell, SimpleViewHolder> {
        public static final int TYPE_BRAND = 1;//头部品牌列表
        public static final int TYPE_BRAND_GOODS = 2;   //品牌+商品列表
        private int mType;
        private Context mContext;

        public BrandSellAdapter(Context context, int type) {
            super(context);
            mType = type;
            mContext = context;
        }

        @Override
        public int getItemViewType(int position) {
            if (mType == TYPE_BRAND) {
                return TYPE_BRAND;
            } else if (mType == TYPE_BRAND_GOODS) {
                return TYPE_BRAND_GOODS;
            }
            return TYPE_BRAND;
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {

            if (holder instanceof HolderBrand) {
                BindHolderBand((HolderBrand) holder, position);
            } else if (holder instanceof HolderBrandAndGoods) {
                BindHolderBrandAndGoods((HolderBrandAndGoods) holder, position);
            }
        }

        private void BindHolderBrandAndGoods(HolderBrandAndGoods holder, int position) {
            final BrandSell branSell = getItem(position);
            if (!TextUtils.isEmpty(branSell.getBrandLogo())) {
                LoadImgUtils.setImg(mContext, holder.userIcon, branSell.getBrandLogo());
            }
            if (!TextUtils.isEmpty(branSell.getBackImage())) {
                LoadImgUtils.setImg(mContext, holder.top_bg, branSell.getBackImage());
            }
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.recyclerview.setLayoutManager(linearLayoutManager);
//            title.setText(branSell.getBranndName());
            GoodsAdapter goodsAdapter = new GoodsAdapter(mContext, branSell.getItems());
            holder.recyclerview.setAdapter(goodsAdapter);
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toBrandList(branSell);
                }
            });
        }

        private void BindHolderBand(HolderBrand holder, int position) {
            final BrandSell branSell = getItem(position);
            if (!TextUtils.isEmpty(branSell.getBrandLogo())) {
                LoadImgUtils.loadingCornerBitmap(mContext, holder.icon, branSell.getBrandLogo(), 5);
//                GlideUtils.getInstance().LoadSupportv4FragmentRoundBitmap(BrandSellFragment.this,branSell.getBrandLogo(),holder.icon, 5);
            }

            if (getString(R.string.check_more).equals(branSell.getBranndName())) {
                holder.more.setVisibility(View.VISIBLE);
                holder.icon.setVisibility(View.GONE);
                //holder.more.setText(branSell.getBranndName());
            } else {
                holder.icon.setVisibility(View.VISIBLE);
                holder.more.setVisibility(View.GONE);
            }
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenFragmentUtils.goToSimpleFragment(mContext, BrandListFragment.class.getName(), new Bundle());
                }
            });
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toBrandList(branSell);
                }
            });

        }

        private void toBrandList(BrandSell branSell) {
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setTitle(branSell.getBranndName());
            imageInfo.setId(branSell.getId());
            GoodNewsFramgent.startGoodsByBrand(getActivity(), imageInfo);
        }


        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case TYPE_BRAND:
                    return new HolderBrand(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brand, parent, false));
                case TYPE_BRAND_GOODS:
                    return new HolderBrandAndGoods(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brand_sell, parent, false));
                default:
                    return new HolderBrand(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brand, parent, false));
            }

        }

        public class HolderBrand extends SimpleViewHolder {
            @BindView(R.id.icon)
            ImageView icon;
            @BindView(R.id.more)
            CardView more;

            public HolderBrand(View itemView) {
                super(itemView);
            }
        }

        public class HolderBrandAndGoods extends SimpleViewHolder {
            @BindView(R.id.userIcon)
            ImageView userIcon;
            //            @BindView(R.id.title)
//            TextView title;
            @BindView(R.id.more)
            ImageView more;
            @BindView(R.id.top_bg)
            ImageView top_bg;
            @BindView(R.id.recyclerview)
            RecyclerView recyclerview;

            public HolderBrandAndGoods(View itemView) {
                super(itemView);
            }
        }
    }

    class GoodsAdapter extends SimpleAdapter<ShopGoodInfo, SimpleViewHolder> {
        List<ShopGoodInfo> datas;
        Context mContext;

        public GoodsAdapter(Context context, List<ShopGoodInfo> datas) {
            super(context, datas);
            this.datas = datas;
            mContext = context;
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {
            final ShopGoodInfo goods = getItem(position);
            ImageView img = holder.viewFinder().view(R.id.img);
            TextView title = holder.viewFinder().view(R.id.title);
            TextView discount_coupon = holder.viewFinder().view(R.id.discount_coupon);
            TextView discount_price = holder.viewFinder().view(R.id.discount_price);
            TextView price = holder.viewFinder().view(R.id.price);
            TextView markTv = holder.viewFinder().view(R.id.markTv);
            price.setText("¥" + MathUtils.getPrice(goods.getPrice()));
            price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            LoadImgUtils.loadingCornerBitmap(mContext, img, MathUtils.getPicture(goods) );
            if(!TextUtils.isEmpty(goods.getItemLabeling())  ) {
                markTv.setVisibility(View.VISIBLE);
                markTv.setText(goods.getItemLabeling());
            }else{
                markTv.setVisibility(View.GONE);
            }
            discount_price.setText(MathUtils.getVoucherPrice(goods.getVoucherPrice()));
            discount_coupon.setText(getString(R.string.discount_coupon, MathUtils.getCouponPrice(goods.getCouponPrice())));
            title.setText(MathUtils.getTitle(goods));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoodsDetailActivity.start(getActivity(), goods);
                }
            });
        }

        @Override
        protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
            return layoutInflater.inflate(R.layout.item_brand_sell_goods, parent, false);
        }
    }

}
