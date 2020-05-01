package com.zjzy.morebit.circle.ui;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Dialog.ReleaseGoodsDialog;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.SimpleAdapter;
import com.zjzy.morebit.adapter.holder.SimpleViewHolder;
import com.zjzy.morebit.circle.contract.ReleaseManageContract;
import com.zjzy.morebit.circle.presenter.ReleaseManagePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.network.CommonEmpty;
import com.zjzy.morebit.pojo.ReleaseManage;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.event.ReleaseManageEvent;
import com.zjzy.morebit.pojo.request.RequestReleaseGoodsDelete;
import com.zjzy.morebit.pojo.request.RequestReleaseManage;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.ViewShowUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;

/**
 * Created by YangBoTian on 2019/6/11.
 */

public class ReleaseManageFragment extends MvpFragment<ReleaseManagePresenter> implements ReleaseManageContract.View {
    //审核中
    public static final int REVIEW = 0;
    //1:已审核(审核通过)
    public static final int PASS = 1;
    //2:审核失败
    public static final int FAILURE = 2;
    //3:下架
    public static final int UNSHELVE = 3;

    private static final int REQUEST_COUNT = 10;
    @BindView(R.id.mListView)
    ReUseListView mReUseListView;
    private int page = 1;
    private ReleaseManageAdapter mAdapter;
    private CommonEmpty mEmptyView;
    private int mType;

    public static ReleaseManageFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        ReleaseManageFragment fragment = new ReleaseManageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        refreshData(true);
    }

    @Override
    protected void initView(View view) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mType = bundle.getInt("type", 0);
        }
        mEmptyView = new CommonEmpty(mView, getString(R.string.data_null), R.drawable.image_meiyousousuojilu);
        mAdapter = new ReleaseManageAdapter(getActivity());
        mReUseListView.getSwipeList().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                refreshData(true);
            }
        });
        mReUseListView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mReUseListView.getSwipeList().isRefreshing()) {
                    refreshData(false);
                }

            }
        });
        mReUseListView.setAdapter(mAdapter);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_release_manage;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }


    private void refreshData(boolean isRefresh) {

        if (isRefresh) {
            page = 1;
            mReUseListView.getListView().setNoMore(false);
            mReUseListView.getSwipeList().setRefreshing(true);
        }
        RequestReleaseManage requestReleaseManage = new RequestReleaseManage();
        requestReleaseManage.setPage(page);
        requestReleaseManage.setIsShow(mType);
        requestReleaseManage.setRows(REQUEST_COUNT);
        mPresenter.getReleaseManageList(this, requestReleaseManage, mEmptyView);

    }

    @Override
    public void onSuccessful(List<ReleaseManage> data) {
        MyLog.i("test","data: " +data);
        if (page == 1) {
            mAdapter.replace(data);
        } else {
            if (data.size() == 0) {
                MyLog.i("test","0");
                mReUseListView.getListView().setNoMore(true);
                mAdapter.clear();
            } else {
                mAdapter.add(data);
            }
        }
        page++;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEmpty() {
        mReUseListView.getListView().setNoMore(true);
        if(page==1){
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFinally() {
        mReUseListView.getSwipeList().setRefreshing(false);
        mReUseListView.getListView().refreshComplete(REQUEST_COUNT);
    }

    @Override
    public void onNull(int position) {
        deleteGoods(position);

    }

    private void deleteGoods(int position) {
        ViewShowUtils.showShortToast(getActivity(), R.string.delete_success);
        List<ReleaseManage> items = mAdapter.getItems();
        items.remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, items.size() - position);
    }

    @Override
    public void onGoodsDeleteSuc(String data, int position) {
        deleteGoods(position);
    }


    class ReleaseManageAdapter extends SimpleAdapter<ReleaseManage, SimpleViewHolder> {

        private LayoutInflater mInflater;
        private Context mContext;
        int type;

        public ReleaseManageAdapter(Context context) {
            super(context);
            this.mContext = context;
        }


        @Override
        public void onBindViewHolder(SimpleViewHolder holder, final int position) {
            final ReleaseManage item = getItem(position);
            ImageView img = holder.viewFinder().view(R.id.img);
            TextView title = holder.viewFinder().view(R.id.title);
            ImageView good_mall_tag = holder.viewFinder().view(R.id.good_mall_tag);
            LinearLayout ll_return_cash = holder.viewFinder().view(R.id.ll_return_cash);
            TextView commission = holder.viewFinder().view(R.id.commission);
            TextView coupon = holder.viewFinder().view(R.id.coupon);
            TextView discount_price = holder.viewFinder().view(R.id.discount_price);
            TextView price = holder.viewFinder().view(R.id.price);
            TextView tv_shop_name = holder.viewFinder().view(R.id.tv_shop_name);
            TextView tv_release_time = holder.viewFinder().view(R.id.tv_release_time);
            TextView tv_type = holder.viewFinder().view(R.id.tv_type);
            LinearLayout ll_time = holder.viewFinder().view(R.id.ll_time);
            TextView tv_apply_time = holder.viewFinder().view(R.id.tv_apply_time);
            TextView tv_audit_time = holder.viewFinder().view(R.id.tv_audit_time);
            TextView tv_see_copywriter = holder.viewFinder().view(R.id.tv_see_copywriter);
            TextView tv_delete = holder.viewFinder().view(R.id.tv_delete);
            TextView tv_remark = holder.viewFinder().view(R.id.tv_remark);
            if (item.getGoods() != null && item.getGoods().size() > 0) {
                ShopGoodInfo shopGoodInfo = item.getGoods().get(0);
                title.setText(MathUtils.getTitle(shopGoodInfo));
                    LoadImgUtils.setImg(mContext, img,MathUtils .getPicture(shopGoodInfo));
                if (StringsUtils.isEmpty(shopGoodInfo.getCouponPrice())) {
                    ll_return_cash.setVisibility(View.GONE);
                } else {
                    ll_return_cash.setVisibility(View.VISIBLE);
                }

                //判断是淘宝还是天猫的商品
                if (shopGoodInfo.getShopType() == 2) {
                    good_mall_tag.setImageResource(R.drawable.tianmao);
                } else {
                    good_mall_tag.setImageResource(R.drawable.taobao);
                }

                coupon.setText(getString(R.string.yuan, MathUtils.getnum(shopGoodInfo.getCouponPrice())));
                 MyLog.i("test","commission: " +shopGoodInfo.getCommission());
                 MyLog.i("test","commission1: " +MathUtils.getMuRatioComPrice(UserLocalData.getUser().getCalculationRate(), shopGoodInfo.getCommission()));
                if (C.UserType.operator.equals(UserLocalData.getUser().getPartner()) || C.UserType.vipMember.equals(UserLocalData.getUser().getPartner()) || !TextUtils.isEmpty(shopGoodInfo.getCommission())) {
                    MyLog.i("test","commission: " +shopGoodInfo.getCommission());
                    commission.setVisibility(View.VISIBLE);
                    commission.setText(getString(R.string.commission, MathUtils.getMuRatioComPrice(UserLocalData.getUser().getCalculationRate(), shopGoodInfo.getCommission())));
                } else {
//                    commission.setText(getString(R.string.upgrade_commission));
                    commission.setVisibility(View.GONE);
                }
                //店铺名称
                if (!TextUtils.isEmpty(shopGoodInfo.getShopName())) {
                    tv_shop_name.setText(shopGoodInfo.getShopName());
                }
                discount_price.setText(getString(R.string.income, MathUtils.getnum(shopGoodInfo.getVoucherPrice())));
                price.setText(getString(R.string.income, MathUtils.getnum(shopGoodInfo.getPrice())));
                price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            }


            if (mType == PASS) {   //  已上架
                ll_time.setVisibility(View.GONE);
                tv_release_time.setVisibility(View.VISIBLE);
                tv_release_time.setTextColor(ContextCompat.getColor(mContext, R.color.color_999999));
                type = ReleaseGoodsActivity.NOT_SUBMIT;
                if(!"null".equals(item.getReviewTime())&&!TextUtils.isEmpty(item.getReviewTime())){
                    tv_release_time.setVisibility(View.VISIBLE);
                    tv_release_time.setText("发布时间：" + item.getReviewTime());
                } else{
                    tv_release_time.setVisibility(View.GONE);
                }

            } else if (mType == REVIEW) {   //审核中
                type = ReleaseGoodsActivity.SUBMIT;
                ll_time.setVisibility(View.GONE);
                tv_release_time.setVisibility(View.VISIBLE);
                if(!"null".equals(item.getCreateTime())&&!TextUtils.isEmpty(item.getCreateTime())){
                    tv_release_time.setVisibility(View.VISIBLE);
                    tv_release_time.setText("申请时间：" + item.getCreateTime());
                } else{
                    tv_release_time.setVisibility(View.GONE);
                }
                tv_release_time.setTextColor(ContextCompat.getColor(mContext, R.color.color_FF8500));
            } else if (mType == FAILURE) {   //审核失败
                type = ReleaseGoodsActivity.SUBMIT;
                tv_release_time.setVisibility(View.GONE);
                ll_time.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(item.getRemark())&&!"null".equals(item.getRemark())) {
                    tv_remark.setVisibility(View.VISIBLE);
                    tv_remark.setText("失败原因：" + item.getRemark());
                } else {
                    tv_remark.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(item.getCreateTime())&&!"null".equals(item.getCreateTime())) {
                    tv_apply_time.setVisibility(View.VISIBLE);
                    tv_apply_time.setText("申请时间：" + item.getCreateTime());
                } else {
                    tv_apply_time.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(item.getReviewTime())&&!"null".equals(item.getReviewTime())) {
                    tv_audit_time.setVisibility(View.VISIBLE);
                    tv_audit_time.setText("审核时间：" + item.getReviewTime());
                } else {
                    tv_audit_time.setVisibility(View.GONE);
                }
            } else if (mType == UNSHELVE) {   //已下架
                ll_time.setVisibility(View.VISIBLE);
                tv_release_time.setVisibility(View.GONE);
                type = ReleaseGoodsActivity.NOT_SUBMIT;
                if (!TextUtils.isEmpty(item.getCreateTime())&&!"null".equals(item.getCreateTime())) {
                    tv_apply_time.setVisibility(View.VISIBLE);
                    tv_apply_time.setText("申请时间：" + item.getCreateTime());
                } else {
                    tv_apply_time.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(item.getDownTime())&&!"null".equals(item.getDownTime())) {
                    tv_audit_time.setVisibility(View.VISIBLE);
                    tv_audit_time.setText("下架时间：" + item.getDownTime());
                } else {
                    tv_audit_time.setVisibility(View.GONE);
                }
            }

            StringBuffer stringBuffer = new StringBuffer();
            if (item.getOpens() != null) {
                for (int i = 0; i < item.getOpens().size(); i++) {
                    if(!"null".equals(item.getOpens().get(i).getName())&&!TextUtils.isEmpty(item.getOpens().get(i).getName())){
                        stringBuffer.append(item.getOpens().get(i).getName());
                        if (i < item.getOpens().size() - 1) {
                            stringBuffer.append(",");
                        }
                    }
                }
                stringBuffer.append("/");
                for (int i = 0; i < item.getOpens().size(); i++) {
                    if(!"null".equals(item.getOpens().get(i).getTwoLevelName())&&!TextUtils.isEmpty(item.getOpens().get(i).getTwoLevelName())){
                        stringBuffer.append(item.getOpens().get(i).getTwoLevelName());
                        if (i < item.getOpens().size() - 1) {
                            stringBuffer.append(",");
                        }
                    }
                }
                MyLog.i("test", "name: " + stringBuffer.toString());
                if(!TextUtils.isEmpty(stringBuffer.toString())){
                    tv_type.setVisibility(View.VISIBLE);
                    tv_type.setText("发布专题："+stringBuffer.toString());
                } else {
                    tv_type.setText(View.VISIBLE);
                }
            }
            tv_see_copywriter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReleaseGoodsActivity.start(mContext, item.getGoods(), item, type, 0);
                }
            });
            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDialog(item, position);
                }
            });
        }

        private void showDeleteDialog(final ReleaseManage item, final int position) {
            ReleaseGoodsDialog releaseGoodsDialog = new ReleaseGoodsDialog(getActivity(), R.style.dialog, "删除商品", "确定要删除已上架商品吗？", ReleaseGoodsDialog.DELETE);
            releaseGoodsDialog.setmOkListener(new ReleaseGoodsDialog.OnOkListener() {
                @Override
                public void onClick(View view) {
                    RequestReleaseGoodsDelete requestReleaseGoodsDelete = new RequestReleaseGoodsDelete();
                    requestReleaseGoodsDelete.setIsShow(mType);
                    if (item.getId() > 0) {
                        MyLog.i("test", "delete: ");
                        requestReleaseGoodsDelete.setShareId(item.getId() + "");
                    }
                    mPresenter.getReleaseGoodsDelete(ReleaseManageFragment.this, requestReleaseGoodsDelete, position);
                }
            });
            releaseGoodsDialog.show();
        }

        @Override
        protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
            return layoutInflater.inflate(R.layout.item_release_manage, parent, false);
        }
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(ReleaseManageEvent event) {
        if(getUserVisibleHint()){
            MyLog.i("test","refreshData" );
            refreshData(true);
        }
    }
}
