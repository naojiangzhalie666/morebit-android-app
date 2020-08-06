package com.zjzy.morebit.main.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.GoodsDetailForJdActivity;
import com.zjzy.morebit.Activity.GoodsDetailForKoalaActivity;
import com.zjzy.morebit.Activity.GoodsDetailForPddActivity;
import com.zjzy.morebit.Activity.GoodsDetailForWphActivity;
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
import com.zjzy.morebit.pojo.TeamInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.VerticalImageSpan;
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
    private Drawable drawable;

    private List<ShopGoodInfo> collectArray = new ArrayList<>(); //收藏
    int mPage = 1;
    private int scrollHeight = 0; //滚动距离
    private CollectAdapter mGuessGoodsAdapter;
    private boolean mICollectfDataEmpty = false;
    private View mHeadView;

    private static final int REQUEST_COUNT = 10;
    private boolean isEditor;
    private String mCheckIds = "";
    // private CommonEmpty mEmptyView;
    private boolean isAllSelect;
    private boolean isNoMore;
    private LinearLayout searchNullTips_ly;

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
        MyLog.i("test", "getData");
        getData(true);
    }

    @Override
    protected void initView(View view) {
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(getString(R.string.my_collect));
        editor.setVisibility(View.VISIBLE);
        editor.setText(R.string.editor);
        editor.setTextColor(Color.parseColor("#F05557"));
        searchNullTips_ly = view.findViewById(R.id.searchNullTips_ly);
        mGuessGoodsAdapter = new CollectAdapter(getActivity());
        mRecyclerView.setAdapter(mGuessGoodsAdapter);
        //    mEmptyView = new CommonEmpty(view, "",0);

        mRecyclerView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollHeight = 0; //重置滚动高度为0
                MyLog.i("test", "getData");
                getData(true);
            }
        });

        mRecyclerView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mRecyclerView.getSwipeList().isRefreshing())
                    MyLog.i("test", "getData");
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
            //   mEmptyView.setmFirstComplete(false);
        }

        mPresenter.getCollectData(this, mPage);
    }


    @OnClick({R.id.toolbar_right_title, R.id.share})
    public void onClick(View v) {
        switch (v.getId()) {

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

            case R.id.share:
                // shareGoods();
                /*if (checkIsSelect()) {
                    openlogoutDialog(0);
//                    LoadingView.showDialog(getActivity(), "");
//                    String ids = getIds();
//                    mPresenter.getDeleteCollection(this, ids);
                }*/
                break;
        }
    }

    private void reset() {
        isEditor = false;
        isAllSelect = false;
        isNoMore = false;
        ll_remove.setVisibility(View.GONE);
        editor.setVisibility(View.GONE);
        searchNullTips_ly.setVisibility(View.VISIBLE);

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
//        for (ShopGoodInfo shopGoodInfo : mGuessGoodsAdapter.getItems()) {
//            shopGoodInfo.setSelect(select);
//        }
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
        if (data != null) {
            mRecyclerView.setVisibility(View.VISIBLE);
            searchNullTips_ly.setVisibility(View.GONE);
            if (mPage == 1) {
                mGuessGoodsAdapter.setData(data);
            } else {
                mGuessGoodsAdapter.addData(data);
            }
        } else {
            searchNullTips_ly.setVisibility(View.VISIBLE);
        }

        setItemStatus(isAllSelect);
        mPage++;


    }


    @Override
    public void showDeleteSuccess() {
        ViewShowUtils.showShortToast(getActivity(), getString(R.string.delete_success));
        mCheckIds = "";
        mCheckbox.setSelected(false);
        // mEmptyView.setmFirstComplete(false);
        getData(true);
    }

    @Override
    public void showEmity() {
        if (mPage == 1) {
            searchNullTips_ly.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
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
//        for (int i = 0; i < mGuessGoodsAdapter.getItems().size(); i++) {
//            ShopGoodInfo shopGoodInfo = mGuessGoodsAdapter.getItem(i);
//            if (shopGoodInfo.getType() == 0) {
//                if (shopGoodInfo.isSelect()) {
//                    ids.append(shopGoodInfo.getId() + ",");
//                }
//            }
//        }
        MyLog.i("test", "ids.to: " + ids.toString());
        MyLog.i("test", "ids: " + ids.toString().substring(0, ids.lastIndexOf(",")));
        mPresenter.getDeleteCollection(CollectFragment2.this, ids.toString());
        ;
    }


    class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder> {
        Context mContext;
        private boolean isEditor;//收藏列表是否是编辑状态
        private List<ShopGoodInfo> list=new ArrayList<>();


        public CollectAdapter(Context context) {
            mContext = context;

        }
        public void setData(List<ShopGoodInfo> data) {
            if (data != null) {
                list.clear();
                list.addAll(data);
                notifyDataSetChanged();
            }
        }


        public void addData(List<ShopGoodInfo> data) {
            if (data != null) {
                list.addAll(data);
                notifyItemRangeChanged(0, data.size());
            }
        }



        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final ShopGoodInfo item = list.get(position);
            LoadImgUtils.loadingCornerBitmap(mContext, holder.goods_bg, item.getItemPicture(), 5);

            if (!StringsUtils.isShowVideo(item.getVideoid())) {
                holder.video_play.setVisibility(View.GONE);
            } else {
                holder.video_play.setVisibility(View.VISIBLE);
            }
            if (StringsUtils.isEmpty(item.getCouponPrice() + "")) {
                holder.coupon.setVisibility(View.GONE);
            } else {
                holder.coupon.setVisibility(View.VISIBLE);
                holder.coupon.setText(getString(R.string.yuan, MathUtils.getnum(item.getCouponPrice())));
            }
//

            if (!TextUtils.isEmpty(item.getCommission())) {
                holder.commission.setText("赚 ¥ " + MathUtils.getMuRatioComPrice(C.SysConfig.NUMBER_COMMISSION_PERCENT_VALUE, item.getCommission()));
            }

            holder.discount_price.setText(MathUtils.getnum(item.getItemVoucherPrice()));


            if (isEditor) {
                holder.checkbox.setVisibility(View.VISIBLE);
            } else {
                holder.checkbox.setVisibility(View.GONE);
            }
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.setSelect(!item.isSelect());
                    if (/*checkSelect() && */isNoMore) {
                        isAllSelect = true;
                        holder.checkbox.setSelected(true);
                    } else {
                        isAllSelect = false;
                        holder.checkbox.setSelected(false);
                    }
                    notifyDataSetChanged();
                }
            });
            holder.checkbox.setSelected(item.isSelect());

            if (!TextUtils.isEmpty(item.getItemTitle())) {
                holder.title.setText(item.getItemTitle());
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEditor) {
                        holder.checkbox.performClick();
                    } else {
                        ShopGoodInfo shopGoodInfo = (ShopGoodInfo) MyGsonUtils.jsonToBean(MyGsonUtils.beanToJson(item), ShopGoodInfo.class);

                        if (shopGoodInfo == null) return;
                        if (item.getShopType() == 3) {
                            String itemSourceId = item.getItemSourceId();
                            shopGoodInfo.setGoodsId(Long.parseLong(itemSourceId));
                            shopGoodInfo.setItemSource("2");
                            shopGoodInfo.setPrice(item.getPrice());
                            GoodsDetailForPddActivity.start(mContext, shopGoodInfo);
                        } else if (item.getShopType() == 4) {
                            String itemSourceId = item.getItemSourceId();
                            shopGoodInfo.setGoodsId(Long.parseLong(itemSourceId));
                            shopGoodInfo.setItemSource("1");
                            shopGoodInfo.setPrice(item.getPrice());
                            GoodsDetailForJdActivity.start(mContext, shopGoodInfo);
                        } else if (item.getShopType() == 5) {
                            GoodsDetailForKoalaActivity.start(getActivity(), shopGoodInfo.getItemSourceId());
                        } else if (item.getShopType() == 6) {
                            GoodsDetailForWphActivity.start(getActivity(), shopGoodInfo.getItemSourceId());
                        } else {
                            GoodsUtil.checkGoods((RxAppCompatActivity) mContext, shopGoodInfo.getItemSourceId(), new MyAction.One<ShopGoodInfo>() {
                                @Override
                                public void invoke(ShopGoodInfo arg) {
                                    MyLog.i("test", "arg: " + arg);
                                    GoodsDetailActivity.start(mContext, arg);
                                }
                            });

                        }
//                            GoodsDetailActivity.start(mContext, shopGoodInfo);
                    }
                }
            });
        }

//        private boolean checkSelect() {
//            boolean isAllSelect = true;
//            for (ShopGoodInfo shopGoodInfo : mGuessGoodsAdapter.getItems()) {
//                if (!shopGoodInfo.isSelect()) {
//                    isAllSelect = false;
//                }
//            }
//            return isAllSelect;
//        }


        public boolean isEditor() {
            return isEditor;
        }

        public void setEditor(boolean editor) {
            isEditor = editor;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_goods_history2, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;

        }



        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView title, commission, coupon, discount_price;
            private ImageView goods_bg, video_play, checkbox;

            public ViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.tv_title);
                commission = itemView.findViewById(R.id.tv_zhuan);
                coupon = itemView.findViewById(R.id.coupon);
                discount_price = itemView.findViewById(R.id.tv_price);

                goods_bg = itemView.findViewById(R.id.goods_bg);
                video_play = itemView.findViewById(R.id.video_play);

                checkbox = itemView.findViewById(R.id.checkbox);
            }
        }
    }


//    private boolean checkIsSelect() {
//        boolean isSelect = false;
//        for (ShopGoodInfo goodsHistory : mGuessGoodsAdapter.getItems()) {
//            if (goodsHistory.getType() == 0) {
//                if (goodsHistory.isSelect() == true) {
//                    isSelect = true;
//                    break;
//                }
//            }
//        }
//        return isSelect;
//    }


}
