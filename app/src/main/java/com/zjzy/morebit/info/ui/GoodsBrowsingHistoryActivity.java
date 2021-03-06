package com.zjzy.morebit.info.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.GoodsDetailForJdActivity;
import com.zjzy.morebit.Activity.GoodsDetailForKoalaActivity;
import com.zjzy.morebit.Activity.GoodsDetailForPddActivity;
import com.zjzy.morebit.Activity.GoodsDetailForWphActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.ClearSDdataDialog;
import com.zjzy.morebit.Module.common.View.ReUseGridView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.SimpleAdapter;
import com.zjzy.morebit.adapter.holder.SimpleViewHolder;
import com.zjzy.morebit.interfaces.EmptyView;
import com.zjzy.morebit.network.CommonEmpty;
import com.zjzy.morebit.pojo.EarningsMsg;
import com.zjzy.morebit.pojo.GoodsBrowsHistory;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.VerticalImageSpan;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.dao.DBManager;
import com.zjzy.morebit.view.ToolbarHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by YangBoTian on 2018/11/26.
 * 商品浏览历史
 */

public class GoodsBrowsingHistoryActivity extends BaseActivity {
    private static final int PAGE_LIMT = 10; //一页的个数
    private int page = 0;
    @BindView(R.id.mListView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_remove)
    View ll_remove;
    @BindView(android.R.id.checkbox)
    ImageView mCheckbox;
    private Drawable drawable;
    private LinearLayout searchNullTips_ly;

    private List<GoodsBrowsHistory> listArray = new ArrayList<>();
    GoodsHistoryAdapter mAdapter;
    private boolean isAllSelect;
    private boolean isEditor;
    private boolean isNoMore;
    private TextView txt_head_title,txt_head_title2;
    private LinearLayout btn_back;
    private SmartRefreshLayout refreshLayout;


    public static void start(Activity activity) {
        Intent intent = new Intent(activity, GoodsBrowsingHistoryActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_browsing_history);
        initView();
    }

    private void initView() {
        ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
  //      mEmptyView = new CommonEmpty(this, "您还没有浏览任何商品哦~", R.drawable.image_meiyoushoucang);
//        ((EmptyView) mEmptyView).setMessage("您还没有浏览任何商品哦~");
//        ((EmptyView) mEmptyView).setIcon(R.drawable.image_meiyoushoucang);
        txt_head_title2= (TextView) findViewById(R.id.txt_head_title2);
        txt_head_title= (TextView) findViewById(R.id.txt_head_title);
        searchNullTips_ly= (LinearLayout) findViewById(R.id.searchNullTips_ly);
        txt_head_title.setText("我的足迹");
        txt_head_title2.setText("删除");
        txt_head_title2.setTextColor(ContextCompat.getColor(this, R.color.color_333333));
        refreshLayout= (SmartRefreshLayout) findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getFirstData();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getMoreData();
            }
        });


        initData();
    }

    private void getMoreData() {
        List<GoodsBrowsHistory> list = DBManager.getInstance().queryGoodsHistoryListByTime(page);
        MyLog.i("test", "list: " + list);
        if (list != null && list.size() > 0) {
            MyLog.i("test", "list: " + list.size());
            page++;
            listArray.addAll(handlerData(list));
            mAdapter.replace(listArray);
            setItemStatus(isAllSelect);
            mAdapter.notifyDataSetChanged();
            refreshLayout.finishLoadMore();

        } else {
            MyLog.i("test", "setNoMore: ");
            isNoMore = true;
           refreshLayout.finishLoadMore();

        }


        //mRecyclerView.getListView().refreshComplete(PAGE_LIMT);
    }

    private void initData() {

        LinearLayoutManager manager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new GoodsHistoryAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        getFirstData();

    }

    private void getFirstData() {
        page = 0;
     refreshLayout.finishLoadMore();
        List<GoodsBrowsHistory> list = DBManager.getInstance().queryGoodsHistoryListByTime(page);
        MyLog.i("test", "list: " + list);
        if (list != null && list.size() > 0) {
            listArray.clear();
            listArray.addAll(handlerData(list));
            page++;
            mAdapter.replace(listArray);
           // mRecyclerView.removeNetworkError();
            searchNullTips_ly.setVisibility(View.GONE);
            txt_head_title2.setVisibility(View.VISIBLE);
        } else {
            txt_head_title2.setVisibility(View.GONE);
            reset();
            mAdapter.clear();
            searchNullTips_ly.setVisibility(View.VISIBLE);

        }
        setItemStatus(isAllSelect);
        refreshLayout.finishRefresh();
        mAdapter.notifyDataSetChanged();
    }

    private void reset() {
        isEditor = false;
        isAllSelect = false;
        isNoMore = false;
        ll_remove.setVisibility(View.GONE);
        txt_head_title2.setVisibility(View.GONE);
       // mRecyclerView.showNetworkError(false);
    }

    @OnClick({R.id.share, R.id.ll_check_all, R.id.txt_head_title2,R.id.btn_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.share:
                if (checkIsSelect()) {
                    openlogoutDialog();
                } else {
                    ViewShowUtils.showShortToast(this, "请选中要删除的商品");
                }

                break;
            case R.id.ll_check_all:
                if (isAllSelect) {
                    setItemStatus(false);
                    isAllSelect = false;
                } else {
                    setItemStatus(true);
                    isAllSelect = true;
                }
                mCheckbox.setSelected(isAllSelect);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.txt_head_title2:
                if (isEditor) {
                    isEditor = false;
                    ll_remove.setVisibility(View.GONE);
                    txt_head_title2.setVisibility(View.VISIBLE);
                    mAdapter.setEditor(false);
                    txt_head_title2.setText("删除");
                    txt_head_title2.setTextColor(Color.parseColor("#333333"));
                } else {
                    isEditor = true;
                    ll_remove.setVisibility(View.VISIBLE);

                    txt_head_title2.setVisibility(View.VISIBLE);
                    mAdapter.setEditor(true);
                    txt_head_title2.setText("完成");
                    txt_head_title2.setTextColor(Color.parseColor("#F05557"));


                }
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * @param select
     */
    private void setItemStatus(boolean select) {
        for (GoodsBrowsHistory goodsHistory : mAdapter.getItems()) {
            goodsHistory.setSelect(select);
        }
    }

    /**
     * 根据时间选择
     *
     * @param select
     */
    private void setSelectOderByTime(boolean select, String time) {

        for (GoodsBrowsHistory goodsHistory : mAdapter.getItems()) {
            if (goodsHistory.getViewType() == 0) {
                String date = DateTimeUtils.getYmd1(goodsHistory.getBrowse_time() + "");
                if (DateTimeUtils.IsToday(date)) {
                    goodsHistory.setTime("今日");
                } else if (DateTimeUtils.IsYesterday(date)) {
                    goodsHistory.setTime("昨日");
                } else {
                    goodsHistory.setTime(date);
                }
                if (time.equals(goodsHistory.getTime())) {
                    goodsHistory.setSelect(select);
                }

            }

        }
    }

    private boolean checkIsSelect() {
        boolean isSelect = false;
        for (GoodsBrowsHistory goodsHistory : mAdapter.getItems()) {
            if (goodsHistory.getViewType() == 0) {
                if (goodsHistory.isSelect() == true) {
                    isSelect = true;
                    break;
                }
            }
        }
        return isSelect;
    }

    private void deleteGoodsHistory() {
        if (isAllSelect) {
            DBManager.getInstance().cleanGoodsHistoryList();
            MyLog.i("test", "clean");
        } else {
            List<GoodsBrowsHistory> list = new ArrayList<>();
            for (GoodsBrowsHistory goodsHistory : mAdapter.getItems()) {
                if (goodsHistory.getViewType() == 0) {
                    if (goodsHistory.isSelect) {
                        list.add(goodsHistory);
                    }
                }
            }
            DBManager.getInstance().deleteGoodsHistoryList(list);
        }

        ViewShowUtils.showShortToast(this, "删除成功");
        getFirstData();
    }


    private ClearSDdataDialog deleteDialog;

    private void openlogoutDialog() {  //删除确认弹窗
        deleteDialog = new ClearSDdataDialog(this, R.style.dialog, "提示", "你确认要删除这些商品吗?", new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View view, String text) {
                deleteGoodsHistory();

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

    class GoodsHistoryAdapter extends SimpleAdapter<GoodsBrowsHistory, SimpleViewHolder> {
        Context mContext;
        private boolean isEditor;//收藏列表是否是编辑状态

        public GoodsHistoryAdapter(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        public int getItemViewType(int position) {
            if (getItem(position).getViewType() == 1) {
                return 1;
            }
            return 0;
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {
            final GoodsBrowsHistory item = getItem(position);
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
                TextView title = holder.viewFinder().view(R.id.tv_title);
                TextView commission = holder.viewFinder().view(R.id.tv_zhuan);
                TextView coupon = holder.viewFinder().view(R.id.coupon);
               TextView discount_price = holder.viewFinder().view(R.id.tv_price);
               // TextView price = holder.viewFinder().view(R.id.tv_price);
                TextView tv_shop_name = holder.viewFinder().view(R.id.shop_name);
                TextView sales = holder.viewFinder().view(R.id.volnum);
//                TextView browse_time = holder.viewFinder().view(R.id.browse_time);
                ImageView goods_bg = holder.viewFinder().view(R.id.goods_bg);
                ImageView video_play = holder.viewFinder().view(R.id.video_play);
                ImageView good_mall_tag = holder.viewFinder().view(R.id.good_mall_tag);
                LinearLayout ll_shop_name=holder.viewFinder().view(R.id.ll_shop_name);
                View line1 = holder.viewFinder().view(R.id.line1);
                View line2 = holder.viewFinder().view(R.id.line2);
                final ImageView checkbox = holder.viewFinder().view(R.id.checkbox);
             //   LinearLayout ll_return_cash = holder.viewFinder().view(R.id.ll_return_cash);
//                if (!TextUtils.isEmpty(item.getItemPicture())) {
                LoadImgUtils.setImg(mContext, goods_bg, item.getItemPicture());
//                TextView subsidiesPriceTv = holder.viewFinder().view(R.id.subsidiesPriceTv);
//                } else {
//                    goods_bg.setImageResource(R.drawable.);
//                }
//                browse_time.setText(TimeUtils.millis2String(item.getBrowse_time()));
                //判断是淘宝还是天猫的商品
                int shopType = item.getShopType();


                if (!TextUtils.isEmpty(item.getItemTitle())) {
                    title.setText(item.getItemTitle());
                   // StringsUtils.retractTitle(good_mall_tag,title,);
                }
                if (!StringsUtils.isShowVideo(item.getItemVideoid())) {
                    video_play.setVisibility(View.GONE);
                } else {
                    video_play.setVisibility(View.VISIBLE);
                }
                if (StringsUtils.isEmpty(item.getCouponPrice() + "")) {
                    coupon.setVisibility(View.GONE);
                } else {
                    coupon.setVisibility(View.VISIBLE);
                    coupon.setText(getString(R.string.yuan, MathUtils.getnum(item.getCouponPrice())));
                }
                if(position+1<getItems().size()){
                    if(TextUtils.isEmpty(getItem(position+1).getBrowse_time()+"")&&!TextUtils.isEmpty(getItem(position+1).getTime())){
                       // line.setVisibility(View.GONE);
                    } else {
                       // line.setVisibility(View.VISIBLE);
                    }
                }
//                commission.setVisibility(View.GONE);
                //店铺名称
                if (!TextUtils.isEmpty(item.getShopName())) {
                    tv_shop_name.setText(item.getShopName());
                }

                    if (!TextUtils.isEmpty(item.getCommission())) {
                        commission.setText( "赚 ¥ "+MathUtils.getMuRatioComPrice(C.SysConfig.NUMBER_COMMISSION_PERCENT_VALUE, item.getCommission()));
                    }


//                //平台补贴
//                if(LoginUtil.checkIsLogin((Activity) mContext, false) && !TextUtils.isEmpty(item.getSubsidiesPrice())){
//                    subsidiesPriceTv.setVisibility(View.VISIBLE);
//                    String getRatioSubside = MathUtils.getMuRatioSubSidiesPrice(UserLocalData.getUser(mContext).getCalculationRate(), item.getSubsidiesPrice());
//                    subsidiesPriceTv.setText(mContext.getString(R.string.subsidiesPrice, getRatioSubside));
//                }else{
//                    subsidiesPriceTv.setVisibility(View.GONE);
//                    subsidiesPriceTv.setText("");
//                }
                MyLog.i("test", "item.getShopName(): " + item.getShopName() + " url: " + item.getItemPicture());

                    discount_price.setText(MathUtils.getnum(item.getItemVoucherPrice()));

//                price.setText(MathUtils.getnum(item.getItemPrice()));
//                price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

                sales.setText(getString(R.string.sales, MathUtils.getSales(item.getSaleMonth())));

                if (isEditor) {
                    line1.setVisibility(View.GONE);
                    line2.setVisibility(View.VISIBLE);
                    checkbox.setVisibility(View.VISIBLE);
                } else {
                    line1.setVisibility(View.VISIBLE);
                    line2.setVisibility(View.GONE);
                    checkbox.setVisibility(View.GONE);
                }
                checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.setSelect(!item.isSelect());
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
                checkbox.setSelected(item.isSelect());
                switch (shopType){
                    case 2:
                      //  drawable = mContext.getResources().getDrawable(R.drawable.tianmao);
                       good_mall_tag.setImageResource(R.drawable.tm_list_icon);
                        break;
                    case 1:
                       // drawable = mContext.getResources().getDrawable(R.drawable.taobao);
                       good_mall_tag.setImageResource(R.drawable.tb_list_icon);
                        break;
                    case 3:
                       // drawable = mContext.getResources().getDrawable(R.drawable.pdd_icon);
                         good_mall_tag.setImageResource(R.mipmap.pdd_list_icon);
                        break;
                    case 4:
                       // drawable = mContext.getResources().getDrawable(R.mipmap.jdong_icon);
                        good_mall_tag.setImageResource(R.mipmap.jd_icon);
                        break;
                    case 5:
                      //  drawable = mContext.getResources().getDrawable(R.mipmap.kaola);
                     //   good_mall_tag.setImageResource(R.mipmap.kaola);
                        sales.setVisibility(View.GONE);
                        ll_shop_name.setVisibility(View.INVISIBLE);
                        break;
                    case 6:
                       // drawable = mContext.getResources().getDrawable(R.mipmap.wph_icon);
                     //   good_mall_tag.setImageResource(R.mipmap.wph_icon);
                        ll_shop_name.setVisibility(View.INVISIBLE);
                        sales.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isEditor) {
                            checkbox.performClick();
                        } else {
                            ShopGoodInfo shopGoodInfo = (ShopGoodInfo) MyGsonUtils.jsonToBean(MyGsonUtils.beanToJson(item), ShopGoodInfo.class);

                            if (shopGoodInfo == null) return;
                            if (item.getShopType() == 3){
                                String itemSourceId = item.getItemSourceId();
                                shopGoodInfo.setGoodsId(Long.parseLong(itemSourceId));
                                shopGoodInfo.setItemSource("2");
                                GoodsDetailForPddActivity.start(mContext,shopGoodInfo);
                            }else if (item.getShopType() == 4){
                                String itemSourceId = item.getItemSourceId();
                                shopGoodInfo.setGoodsId(Long.parseLong(itemSourceId));
                                shopGoodInfo.setItemSource("1");
                                GoodsDetailForJdActivity.start(mContext,shopGoodInfo);
                            }else if (item.getShopType() == 5){
                                GoodsDetailForKoalaActivity.start(GoodsBrowsingHistoryActivity.this,shopGoodInfo.getItemSourceId());
                            }else if (item.getShopType() == 6){
                                GoodsDetailForWphActivity.start(GoodsBrowsingHistoryActivity.this,shopGoodInfo.getItemSourceId());
                            }else{
                                GoodsUtil.checkGoods(GoodsBrowsingHistoryActivity.this, shopGoodInfo.getItemSourceId(), new MyAction.One<ShopGoodInfo>() {
                                    @Override
                                    public void invoke(ShopGoodInfo arg) {
                                        MyLog.i("test", "arg: " + arg);
                                        GoodsDetailActivity.start(mContext, arg);
                                    }
                                });
                            }


                        }
                    }
                });
            }


        }

        private boolean checkSelect() {
            boolean isAllSelect = true;
            for (GoodsBrowsHistory goodsHistory : mAdapter.getItems()) {
                if (!goodsHistory.isSelect()) {
                    isAllSelect = false;
                }
            }
            return isAllSelect;
        }


        private boolean checkSelectByTime(String time) {
            boolean isAllSelect = true;

            for (GoodsBrowsHistory goodsHistory : mAdapter.getItems()) {
                if (goodsHistory.getViewType() == 0) {
                    String date = DateTimeUtils.getYmd1(goodsHistory.getBrowse_time() + "");
                    if (DateTimeUtils.IsToday(date)) {
                        goodsHistory.setTime("今日");
                    } else if (DateTimeUtils.IsYesterday(date)) {
                        goodsHistory.setTime("昨日");
                    } else {
                        goodsHistory.setTime(date);
                    }
                    if (time.equals(goodsHistory.getTime())) {
                        if (!goodsHistory.isSelect()) {
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
            if (viewType == EarningsMsg.TWO_TYPE)
                return new TimeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_history_time, parent, false));
            return super.onCreateViewHolder(parent, viewType);
        }

        public class TimeViewHolder extends SimpleViewHolder {
            public TimeViewHolder(View itemView) {
                super(itemView);
            }
        }
    }


    public List<GoodsBrowsHistory> handlerData(List<GoodsBrowsHistory> datas) {
        List<GoodsBrowsHistory> list = new ArrayList<>();
        List<GoodsBrowsHistory> allList = new ArrayList<>();
        allList.addAll(listArray);
        allList.addAll(datas);
        int index = listArray.size();
        for (int i = index; i < allList.size(); i++) {
            String date = DateTimeUtils.getYmd1(allList.get(i).getBrowse_time() + "");
            if (i == 0) {
                if (!TextUtils.isEmpty(date)) {
                    GoodsBrowsHistory goodsBrowsHistory = new GoodsBrowsHistory();
                    goodsBrowsHistory.setViewType(1);
                    if (DateTimeUtils.IsToday(date)) {
                        goodsBrowsHistory.setTime("今日");
                    } else if (DateTimeUtils.IsYesterday(date)) {
                        goodsBrowsHistory.setTime("昨日");
                    } else {
                        goodsBrowsHistory.setTime(date);
                    }
                    list.add(goodsBrowsHistory);
                }
                list.add(allList.get(i));
            } else {
                String date1 = DateTimeUtils.getYmd1(allList.get(i - 1).getBrowse_time() + "");
                if (!TextUtils.isEmpty(date) && !date.equals(date1)) {
                    GoodsBrowsHistory goodsBrowsHistory = new GoodsBrowsHistory();
                    goodsBrowsHistory.setViewType(1);
                    if (DateTimeUtils.IsToday(date)) {
                        goodsBrowsHistory.setTime("今日");
                    } else if (DateTimeUtils.IsYesterday(date)) {
                        goodsBrowsHistory.setTime("昨日");
                    } else {
                        goodsBrowsHistory.setTime(date);
                    }

                    list.add(goodsBrowsHistory);
                }
                list.add(allList.get(i));
            }

        }
        return list;
    }
}
