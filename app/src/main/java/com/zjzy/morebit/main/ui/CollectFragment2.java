package com.zjzy.morebit.main.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Dialog.ClearSDdataDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.SimpleAdapter;
import com.zjzy.morebit.adapter.holder.SimpleViewHolder;
import com.zjzy.morebit.main.contract.CollectContract;
import com.zjzy.morebit.main.presenter.CollectPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.network.CommonEmpty;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.view.ToolbarHelper;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fengrs on 2018/8/27.
 * 首页 收藏界面
 */

public class CollectFragment2 extends MvpFragment<CollectPresenter> implements CollectContract.View {
    //    @BindView(R.id.zhiding_tv)
//    ImageView zhiding_tv;
    @BindView(R.id.mListView)
    ReUseListView mRecyclerView;

    @BindView(R.id.ll_remove)
    View ll_remove;
    @BindView(android.R.id.checkbox)
    ImageView mCheckbox;
    @BindView(R.id.toolbar_right_title)
    TextView editor;
    @BindView(R.id.ll_check_all)
    LinearLayout ll_check_all;
    @BindView(R.id.share)
    TextView share;
    @BindView(R.id.delete)
    TextView delete;

    private List<ShopGoodInfo> collectArray = new ArrayList<>(); //收藏
    int mPage = 1;
    private int scrollHeight = 0; //滚动距离
    private CollectAdapter mGuessGoodsAdapter;
    private boolean mICollectfDataEmpty = false;
    private View mHeadView;

    private static final int REQUEST_COUNT = 10;
    private boolean isEditor;
    private String mCheckIds = "";
    private CommonEmpty mEmptyView;
    private boolean isAllSelect;
    private boolean isNoMore;

//    public void onResume() {
//        super.onResume();
//        if (UserLocalData.isCollect) {
//            UserLocalData.isCollect = false;
//            initData();
//        }
//    }

    public static CollectFragment2 newInstance() {
        CollectFragment2 collectFragment2 = new CollectFragment2();
        return collectFragment2;
    }

    @Override
    protected void initData() {
        MyLog.i("test","getData");
        getData(true);
    }

    @Override
    protected void initView(View view) {
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(getString(R.string.my_collect));
        editor.setVisibility(View.VISIBLE);
        editor.setText(R.string.editor);
        mGuessGoodsAdapter = new CollectAdapter(getActivity());
        mRecyclerView.setAdapter(mGuessGoodsAdapter);
        mEmptyView = new CommonEmpty(view, "您还没有收藏任何商品哦~", R.drawable.image_meiyoushoucang);

        mRecyclerView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollHeight = 0; //重置滚动高度为0
                MyLog.i("test","getData");
                getData(true);
            }
        });

        mRecyclerView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mRecyclerView.getSwipeList().isRefreshing())
                    MyLog.i("test","getData");
                    getData(false);
            }
        });
        ll_check_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllSelect) {
                    setItemStatus(false);
                    isAllSelect = false;
                } else {
                    setItemStatus(true);
                    isAllSelect = true;
                }
                mCheckbox.setSelected(isAllSelect);
                mGuessGoodsAdapter.notifyDataSetChanged();
            }
        });
        share.setText("分享");
        delete.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_collect2;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }


    private void getData(boolean isRefresh) {
        if (isRefresh) {
            mPage = 1;
            mRecyclerView.getListView().setNoMore(false);
            mEmptyView.setmFirstComplete(false);
        }

        mPresenter.getCollectData(this, mPage, mEmptyView);
    }


    @OnClick({R.id.delete, R.id.toolbar_right_title, R.id.empty_view,R.id.share})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete:
                if (checkIsSelect()) {
                    openlogoutDialog(0);
//                    LoadingView.showDialog(getActivity(), "");
//                    String ids = getIds();
//                    mPresenter.getDeleteCollection(this, ids);
                }
                break;
            case R.id.toolbar_right_title:
                if (isEditor) {
                    isEditor = false;
                    mGuessGoodsAdapter.setEditor(false);
                    ll_remove.setVisibility(View.GONE);
                    editor.setText(R.string.editor);
                } else {
                    isEditor = true;
                    mGuessGoodsAdapter.setEditor(true);
                    ll_remove.setVisibility(View.VISIBLE);
                    editor.setText(R.string.finish);

                }
                mGuessGoodsAdapter.notifyDataSetChanged();
                break;
            case R.id.empty_view:
                MyLog.i("test","getData");
                getData(true);
                break;
            case R.id.share:
                shareGoods();
                break;
        }
    }

    private void reset() {
        isEditor = false;
        isAllSelect = false;
        isNoMore = false;
        ll_remove.setVisibility(View.GONE);
        editor.setVisibility(View.GONE);

    }

    private String getIds() {
        if (collectArray.size() == 0) return "";
        String ids = "";
        for (int i = 0; i < collectArray.size(); i++) {
            if (collectArray.get(i).isSelect()) {
                ids = ids + collectArray.get(i).getId() + ",";
                MyLog.i("test", "id: " + ids);
            }

        }
        if (!TextUtils.isEmpty(ids)) {
            ids = ids.substring(0, ids.length() - 1);
        }

        MyLog.i("test", "ids: " + ids);
        return ids;
    }

    /**
     * @param select
     */
    private void setItemStatus(boolean select) {
        for (ShopGoodInfo shopGoodInfo : mGuessGoodsAdapter.getItems()) {
            shopGoodInfo.setSelect(select);
        }
    }

    @Override
    public void showCollectFailure(String errorMsg, String errorCode) {
        if (mPage == 1) {
            if (C.requestCode.dataListEmpty.equals(errorCode) || C.requestCode.dataNull.equals(errorCode)) {
                collectArray.clear();
                mRecyclerView.notifyDataSetChanged();
                reset();
            }

        } else {
            if (C.requestCode.dataListEmpty.equals(errorCode) || C.requestCode.dataNull.equals(errorCode)) {
                isNoMore = true;
            }
            mRecyclerView.getListView().setNoMore(true);
        }
    }

    @Override
    public void showCollectSuccess(List<ShopGoodInfo> data) {
        if (mPage == 1) {
            collectArray.clear();
            collectArray.addAll(handlerData(data));
            mGuessGoodsAdapter.replace(collectArray);
        } else {
            collectArray.addAll(handlerData(data));
            mGuessGoodsAdapter.replace(collectArray);
        }
        setItemStatus(isAllSelect);
        mPage++;
        mRecyclerView.notifyDataSetChanged();

    }


    @Override
    public void showDeleteSuccess() {
        ViewShowUtils.showShortToast(getActivity(), getString(R.string.delete_success));
        mCheckIds = "";
        mCheckbox.setSelected(false);
        mEmptyView.setmFirstComplete(false);
        getData(true);
    }


    @Override
    public void showFinally() {
        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
        mRecyclerView.getSwipeList().setRefreshing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            getData(true);
        }
    }


    private ClearSDdataDialog deleteDialog;

    private void openlogoutDialog(final int position) {  //退出确认弹窗
        deleteDialog = new ClearSDdataDialog(getActivity(), R.style.dialog, "提示", "你确认要删除这些商品吗?", new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View view, String text) {
                deleteGoods();
            }
        });
        deleteDialog.setOkTextAndColor(R.color.color_F32F19, "立即删除");
        deleteDialog.setCancelTextAndColor(R.color.color_2B0900, "取消");
        deleteDialog.setOnCancelListner(new ClearSDdataDialog.OnCancelListner() {
            @Override
            public void onClick(View view, String text) {

            }
        });
        deleteDialog.show();
    }

    private void deleteGoods() {
        StringBuffer ids = new StringBuffer();
        for (int i = 0; i < mGuessGoodsAdapter.getItems().size(); i++) {
            ShopGoodInfo shopGoodInfo = mGuessGoodsAdapter.getItem(i);
            if (shopGoodInfo.getType() == 0) {
                if (shopGoodInfo.isSelect()) {
                    ids.append(shopGoodInfo.getId() + ",");
                }
            }
        }
        MyLog.i("test", "ids.to: " + ids.toString());
        MyLog.i("test", "ids: " + ids.toString().substring(0, ids.lastIndexOf(",")));
        mPresenter.getDeleteCollection(CollectFragment2.this, ids.toString());
    }

    public List<ShopGoodInfo> handlerData(List<ShopGoodInfo> datas) {
        List<ShopGoodInfo> list = new ArrayList<>();
        List<ShopGoodInfo> allList = new ArrayList<>();
        allList.addAll(collectArray);
        allList.addAll(datas);
        int index = collectArray.size();
        for (int i = index; i < allList.size(); i++) {
            String date = DateTimeUtils.getYMDTime(allList.get(i).getCreateTime());
            if (i == 0) {
                if (!TextUtils.isEmpty(date)) {
                    ShopGoodInfo shopGoodInfo = new ShopGoodInfo();
                    shopGoodInfo.setType(1);
                    if (DateTimeUtils.IsToday(date)) {
                        shopGoodInfo.setTime("今日");
                    } else if (DateTimeUtils.IsYesterday(date)) {
                        shopGoodInfo.setTime("昨日");
                    } else {
                        shopGoodInfo.setTime(date);
                    }
                    list.add(shopGoodInfo);
                }
                list.add(allList.get(i));
            } else {
                String date1 = DateTimeUtils.getYMDTime(allList.get(i - 1).getCreateTime());
                if (!TextUtils.isEmpty(date) && !date.equals(date1)) {
                    ShopGoodInfo shopGoodInfo = new ShopGoodInfo();
                    shopGoodInfo.setType(1);
                    if (DateTimeUtils.IsToday(date)) {
                        shopGoodInfo.setTime("今日");
                    } else if (DateTimeUtils.IsYesterday(date)) {
                        shopGoodInfo.setTime("昨日");
                    } else {
                        shopGoodInfo.setTime(date);
                    }

                    list.add(shopGoodInfo);
                }
                list.add(allList.get(i));
            }

        }
        return list;
    }

    class CollectAdapter extends SimpleAdapter<ShopGoodInfo, SimpleViewHolder> {
        Context mContext;
        private boolean isEditor;//收藏列表是否是编辑状态

        public CollectAdapter(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        public int getItemViewType(int position) {
            if (getItem(position).getType() == 1) {
                return 1;
            }
            return 0;
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {
            final ShopGoodInfo item = getItem(position);
            if (holder instanceof TimeViewHolder) {
                TextView time = holder.viewFinder().view(R.id.time);
                ImageView checkbox = holder.viewFinder().view(R.id.checkbox_time);
                time.setText(item.getTime());
                if (isEditor) {
                    if (checkSelectByTime(item.getTime())) {
                        item.setSelect(true);
                    } else {
                        item.setSelect(false);
                    }
                    checkbox.setVisibility(View.VISIBLE);
                } else {
                    checkbox.setVisibility(View.GONE);
                }
                checkbox.setSelected(item.isSelect());
                holder.viewFinder().view(R.id.checkbox_time_ll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.setSelect(!item.isSelect());
                        setSelectOderByTime(item.isSelect(), item.getTime());
                        if (checkSelect() && isNoMore) {
                            isAllSelect = true;
                            mCheckbox.setSelected(true);
                        } else {
                            isAllSelect = false;
                            mCheckbox.setSelected(false);
                        }
                        notifyDataSetChanged();
                    }
                });
            } else {
                TextView title = holder.viewFinder().view(R.id.title);
                TextView commission = holder.viewFinder().view(R.id.commission);
                TextView coupon = holder.viewFinder().view(R.id.coupon);
                TextView discount_price = holder.viewFinder().view(R.id.discount_price);
                TextView price = holder.viewFinder().view(R.id.price);
                TextView tv_shop_name = holder.viewFinder().view(R.id.tv_shop_name);
                TextView sales = holder.viewFinder().view(R.id.sales);
//                TextView browse_time = holder.viewFinder().view(R.id.browse_time);
                ImageView goods_bg = holder.viewFinder().view(R.id.goods_bg);
                ImageView video_play = holder.viewFinder().view(R.id.video_play);
                ImageView good_mall_tag = holder.viewFinder().view(R.id.good_mall_tag);
                final ImageView checkbox = holder.viewFinder().view(R.id.checkbox);
                 View line = holder.viewFinder().view(R.id.line);
                LinearLayout ll_return_cash = holder.viewFinder().view(R.id.ll_return_cash);
                TextView subsidiesPriceTv = holder.viewFinder().view(R.id.subsidiesPriceTv);

//                if (!TextUtils.isEmpty(item.getItemPicture())) {
                LoadImgUtils.setImg(mContext, goods_bg, item.getPicture());
//                } else {
//                    goods_bg.setImageResource(R.drawable.);
//                }
//                browse_time.setText(TimeUtils.millis2String(item.getBrowse_time()));
                //判断是淘宝还是天猫的商品
                if (item.getShopType()==2) {
                    good_mall_tag.setImageResource(R.drawable.tianmao);
                } else {
                    good_mall_tag.setImageResource(R.drawable.taobao);
                }
                if (!TextUtils.isEmpty(item.getTitle())) {
                    StringsUtils.retractTitle(good_mall_tag,title,item.getTitle());
                }
                if (!StringsUtils.isShowVideo(item.getVideoid())) {
                    video_play.setVisibility(View.GONE);
                } else {
                    video_play.setVisibility(View.VISIBLE);
                }
                if (StringsUtils.isEmpty(item.getCouponPrice() + "")) {
                    ll_return_cash.setVisibility(View.GONE);
                } else {
                    ll_return_cash.setVisibility(View.VISIBLE);
                    coupon.setText(getString(R.string.yuan, MathUtils.getCouponPrice(item.getCouponPrice())));
                }
//                commission.setVisibility(View.GONE);
                //店铺名称
                if (!TextUtils.isEmpty(item.getShopName())) {
                    tv_shop_name.setText(item.getShopName());
                }
                if (C.UserType.operator.equals(UserLocalData.getUser(mContext).getPartner()) || C.UserType.vipMember.equals(UserLocalData.getUser(mContext).getPartner())) {
                    commission.setText(getString(R.string.commission, MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), item.getCommission())));
                } else {
                    UserInfo userInfo1 =UserLocalData.getUser();
                    if (userInfo1 == null || TextUtils.isEmpty(UserLocalData.getToken())) {
                        commission.setText(getString(R.string.commission, MathUtils.getMuRatioComPrice(C.SysConfig.NUMBER_COMMISSION_PERCENT_VALUE, item.getCommission())));
                    }else{
                        commission.setText(getString(R.string.commission, MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), item.getCommission())));
                    }
//                    commission.setText(getString(R.string.upgrade_commission));
                }
                discount_price.setText(getString(R.string.income, MathUtils.getVoucherPrice(item.getVoucherPrice())));
                price.setText(getString(R.string.income, MathUtils.getPrice(item.getPrice())));
                price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                sales.setText(getString(R.string.sales, MathUtils.getSales(item.getSaleMonth())));
                //平台补贴
//                if(!TextUtils.isEmpty(item.getSubsidiesPrice())){
//                    subsidiesPriceTv.setVisibility(View.VISIBLE);
//                    String getRatioSubside = MathUtils.getMuRatioSubSidiesPrice(UserLocalData.getUser(mContext).getCalculationRate(),item.getSubsidiesPrice());
//                    subsidiesPriceTv.setText(mContext.getString(R.string.subsidiesPrice, getRatioSubside));
//                }else{
//                    subsidiesPriceTv.setVisibility(View.GONE);
//                    subsidiesPriceTv.setText("");
//                }

                if(position+1<getItems().size()){
                    if(TextUtils.isEmpty(getItem(position+1).getCreateTime())&&!TextUtils.isEmpty(getItem(position+1).getTime())){
                        line.setVisibility(View.GONE);
                    } else {
                        line.setVisibility(View.VISIBLE);
                    }
                }
                if (isEditor) {
                    checkbox.setVisibility(View.VISIBLE);
                } else {
                    checkbox.setVisibility(View.GONE);
                }
                checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.setSelect(!item.isSelect());
                        if (checkSelect() && isNoMore) {
                            isAllSelect = true;
                            checkbox.setSelected(true);
                        } else {
                            isAllSelect = false;
                            checkbox.setSelected(false);
                        }
                        notifyDataSetChanged();
                    }
                });
                checkbox.setSelected(item.isSelect());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isEditor) {
                            checkbox.performClick();
                        } else {
                            ShopGoodInfo shopGoodInfo = (ShopGoodInfo) MyGsonUtils.jsonToBean(MyGsonUtils.beanToJson(item), ShopGoodInfo.class);

                            if (shopGoodInfo == null) return;

                            GoodsUtil.checkGoods((RxAppCompatActivity) mContext, shopGoodInfo.getItemSourceId(), new MyAction.One<ShopGoodInfo>() {
                                @Override
                                public void invoke(ShopGoodInfo arg) {
                                    MyLog.i("test", "arg: " + arg);
                                    GoodsDetailActivity.start(mContext, arg);
                                }
                            });
//                            GoodsDetailActivity.start(mContext, shopGoodInfo);
                        }
                    }
                });
            }


        }

        private boolean checkSelect() {
            boolean isAllSelect = true;
            for (ShopGoodInfo shopGoodInfo : mGuessGoodsAdapter.getItems()) {
                if (!shopGoodInfo.isSelect()) {
                    isAllSelect = false;
                }
            }
            return isAllSelect;
        }


        private boolean checkSelectByTime(String time) {
            boolean isAllSelect = true;

            for (ShopGoodInfo shopGoodInfo : mGuessGoodsAdapter.getItems()) {
                if (shopGoodInfo.getType() == 0) {
                    String date = DateTimeUtils.getYMDTime(shopGoodInfo.getCreateTime() + "");
                    if (DateTimeUtils.IsToday(date)) {
                        shopGoodInfo.setTime("今日");
                    } else if (DateTimeUtils.IsYesterday(date)) {
                        shopGoodInfo.setTime("昨日");
                    } else {
                        shopGoodInfo.setTime(date);
                    }
                    if (time.equals(shopGoodInfo.getTime())) {
                        if (!shopGoodInfo.isSelect()) {
                            isAllSelect = false;
                        }
                    }
                }

            }
            return isAllSelect;
        }

        @Override
        protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
            return layoutInflater.inflate(R.layout.item_goods_history, parent, false);
        }

        public boolean isEditor() {
            return isEditor;
        }

        public void setEditor(boolean editor) {
            isEditor = editor;
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 1)
                return new TimeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_history_time, parent, false));
            return super.onCreateViewHolder(parent, viewType);
        }

        public class TimeViewHolder extends SimpleViewHolder {
            public TimeViewHolder(View itemView) {
                super(itemView);
            }
        }
    }


    /**
     * 根据时间选择
     *
     * @param select
     */

    private void setSelectOderByTime(boolean select, String time) {

        for (ShopGoodInfo shopGoodInfo : mGuessGoodsAdapter.getItems()) {
            if (shopGoodInfo.getType() == 0) {
                String date = DateTimeUtils.getYMDTime(shopGoodInfo.getCreateTime() + "");
                if (DateTimeUtils.IsToday(date)) {
                    shopGoodInfo.setTime("今日");
                } else if (DateTimeUtils.IsYesterday(date)) {
                    shopGoodInfo.setTime("昨日");
                } else {
                    shopGoodInfo.setTime(date);
                }
                if (time.equals(shopGoodInfo.getTime())) {
                    shopGoodInfo.setSelect(select);
                }

            }

        }
    }

    private boolean checkIsSelect() {
        boolean isSelect = false;
        for (ShopGoodInfo goodsHistory : mGuessGoodsAdapter.getItems()) {
            if (goodsHistory.getType() == 0) {
                if (goodsHistory.isSelect() == true) {
                    isSelect = true;
                    break;
                }
            }
        }
        return isSelect;
    }

    /**
     * 分享商品
     * @param item
     * @param osgData
     */
    protected void shareGoods() {
        List<ShopGoodInfo> shopGoodInfos = new ArrayList<>();
        for(int i=0;i<mGuessGoodsAdapter.getItems().size();i++){
            if(mGuessGoodsAdapter.getItem(i).getType()==0){
                if(mGuessGoodsAdapter.getItem(i).isSelect()){
                    shopGoodInfos.add(mGuessGoodsAdapter.getItem(i));
                }

            }
        }
        if(shopGoodInfos.size()>9){
            ViewShowUtils.showShortToast(getActivity(),"最多可以选9个商品");
            return;
        }
        GoodsUtil.SharePosterList2(getActivity(), shopGoodInfos, new MyAction.OnResult<String>() {
            @Override
            public void invoke(String arg) {
                LoadingView.dismissDialog();
            }

            @Override
            public void onError() {
                LoadingView.dismissDialog();
            }
        });
    }
}
