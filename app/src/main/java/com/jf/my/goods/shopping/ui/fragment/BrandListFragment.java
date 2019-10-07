package com.jf.my.goods.shopping.ui.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jf.my.Module.common.Utils.LoadingView;
import com.jf.my.Module.common.View.ReUseGridView;
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
import com.jf.my.utils.LoadImgUtils;
import com.jf.my.view.ToolbarHelper;

import java.util.List;

import butterknife.BindView;

/**
 * Created by YangBoTian on 2018/9/12.
 * 品牌列表
 */

public class BrandListFragment extends MvpFragment<BrandSellPresenter> implements BrandSellContract.View {
    private static final int REQUEST_COUNT = 10;
    @BindView(R.id.mListView)
    ReUseGridView mReUseGridView;

    BrandSellAdapter mAdapter;
    private int page = 1;


    @Override
    public void showSuccessful(List<BrandSell> brandSells) {
        if (page == 1) {
            mAdapter.replace(brandSells);
        } else {
            if (brandSells.size() == 0) {
                mReUseGridView.getListView().setNoMore(true);
            } else {
                mAdapter.add(brandSells);
            }

        }
        page++;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {
        if (page != 1)
            mReUseGridView.getListView().setNoMore(true);

    }

    @Override
    public void getGoodsSuccessful(List<BrandSell> brandSells) {

    }

    @Override
    public void showFinally() {
        LoadingView.dismissDialog();
        mReUseGridView.getSwipeList().setRefreshing(false);
        mReUseGridView.getListView().refreshComplete(REQUEST_COUNT);
    }

    @Override
    protected void initData() {
        refreshData();
    }

    @Override
    protected void initView(View view) {
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle("全部品牌");

        mReUseGridView.getSwipeList().setOnRefreshListener(new com.jf.my.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                refreshData();
            }
        });
//        mReUseGridView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                if (!mReUseGridView.getSwipeList().isRefreshing()) {
//                    refreshData();
//                }
//
//            }
//        });
        mAdapter = new BrandSellAdapter(getActivity());
        mReUseGridView.setAdapter(mAdapter);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_brand_list;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }


    private void refreshData() {
        mPresenter.getBrandSellList(this, page);
    }

    class BrandSellAdapter extends SimpleAdapter<BrandSell, SimpleViewHolder> {

        private Context mContext;

        public BrandSellAdapter(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {
            ImageView icon = holder.viewFinder().view(R.id.icon);
            final BrandSell branSell = getItem(position);
            // String s = "https://img.gzzhitu.com/picture/20180730/153291676447381.jpg";
            if (!TextUtils.isEmpty(branSell.getBrandLogo())) {
                LoadImgUtils.setImg(getActivity(), icon, branSell.getBrandLogo());
                // GlideUtils.getInstance().LoadSupportv4FragmentRoundBitmap(BrandListFragment.this, branSell.getBrandLogo(), icon, 5);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toBrandList(branSell);
                }
            });
        }

        @Override
        protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
            return layoutInflater.inflate(R.layout.item_brand_list, parent, false);
        }
    }

    private void toBrandList(BrandSell branSell) {
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setTitle(branSell.getBranndName());
        imageInfo.setId(branSell.getId());
        GoodNewsFramgent.startGoodsByBrand(getActivity(), imageInfo);
    }
}
