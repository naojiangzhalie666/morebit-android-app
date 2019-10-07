package com.markermall.cat.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.markermall.cat.Activity.GoodsDetailActivity;
import com.markermall.cat.LocalData.UserLocalData;
import com.markermall.cat.R;
import com.markermall.cat.adapter.holder.SimpleViewHolder;
import com.markermall.cat.goods.shopping.ui.view.RecommendIndexView;
import com.markermall.cat.pojo.SelectFlag;
import com.markermall.cat.pojo.ShopGoodInfo;
import com.markermall.cat.utils.C;
import com.markermall.cat.utils.LoadImgUtils;
import com.markermall.cat.utils.LoginUtil;
import com.markermall.cat.utils.MathUtils;
import com.markermall.cat.utils.StringsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by YangBoTian on 2018/8/22.
 */

public class GoodsAdapter extends SimpleAdapter<ShopGoodInfo, SimpleViewHolder> {
    public static int LINEARLAYOUT_TYPE = 1023;   //垂直布局
    public static int GRIDLAYOUT_TYPE = 1024;   //网格布局
    public static int TYPE_HOME = 0;  //首页列表
    public static int TYPE_OFFICAL_RECOM = 1;  //官方推荐
    public static int TYPE_COLLECT = 2;  //收藏
    public static int TYPE_SECKILL = 3;  // 限时秒杀
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int LINEARLAYOUT = 3;
    private Context mContext;
    private int type = 0;
    private int mListType = 0;

    private OnSelectListener mOnSelectListener;
    private List<SelectFlag> slFlag = new ArrayList<>();
    private int layoutType = GRIDLAYOUT_TYPE;     //默认网格布局
    private String mMaterialID;

    public GoodsAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    /**
     * 获取官方推荐勾选值
     */
    public List<SelectFlag> getSelectFlag() {
        return slFlag;
    }

    @Override
    public int getItemViewType(int position) {
        if (LINEARLAYOUT_TYPE == layoutType) {
            return LINEARLAYOUT;
        } else {
            if (position % 2 == 0) {
                return LEFT;
            } else {
                return RIGHT;
            }

        }

    }


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case LEFT:
                return new HolderLeft(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_left, parent, false));
            case RIGHT:
                return new HolderLeft(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_right, parent, false));
            case LINEARLAYOUT:
                return new HolderLeft(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_linearlayout, parent, false));
            default:
                return new HolderLeft(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_right, parent, false));

        }
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        final ShopGoodInfo item = getItem(position);
        TextView title = holder.viewFinder().view(R.id.title);
        TextView commission = holder.viewFinder().view(R.id.commission);
        TextView coupon = holder.viewFinder().view(R.id.coupon);
        TextView discount_price = holder.viewFinder().view(R.id.discount_price);
        TextView price = holder.viewFinder().view(R.id.price);
        TextView tv_shop_name = holder.viewFinder().view(R.id.tv_shop_name);
        TextView sales = holder.viewFinder().view(R.id.sales);
        ImageView img = holder.viewFinder().view(R.id.img);
        ImageView bg = holder.viewFinder().view(R.id.bg);
        ImageView video_play = holder.viewFinder().view(R.id.video_play);
        ImageView good_mall_tag = holder.viewFinder().view(R.id.good_mall_tag);
        ImageView select = holder.viewFinder().view(R.id.select);
        RelativeLayout img_rl = holder.viewFinder().view(R.id.img_rl);
        TextView markTv = holder.viewFinder().view(R.id.markTv);
        StringsUtils.retractTitle( good_mall_tag, title,MathUtils.getTitle(item));
        TextView subsidiesPriceTv = holder.viewFinder().view(R.id.subsidiesPriceTv);
        if(!TextUtils.isEmpty(item.getItemLabeling())  ){
            markTv.setVisibility(View.VISIBLE);
            markTv.setText(item.getItemLabeling());
        }else{
            markTv.setVisibility(View.GONE);
        }
        LoadImgUtils.setImg(mContext, img, MathUtils.getPicture(item));
        if (StringsUtils.isEmpty(item.getCouponPrice())) {
            coupon.setVisibility(View.GONE);
        } else {
            coupon.setVisibility(View.VISIBLE);
            coupon.setText(getString(R.string.yuan, MathUtils.getCouponPrice(item.getCouponPrice())));
        }

        //判断是淘宝还是天猫的商品
        if (item.getShopType() == 2) {
            good_mall_tag.setImageResource(R.drawable.tianmao);
        } else {
            good_mall_tag.setImageResource(R.drawable.taobao);
        }
        if (!StringsUtils.isShowVideo(item.getVideoid())) {
            video_play.setVisibility(View.GONE);
        } else {
            video_play.setVisibility(View.VISIBLE);
        }

        if (type == TYPE_COLLECT) {
            commission.setVisibility(View.GONE);
        } else {
            commission.setVisibility(View.VISIBLE);
        }
        if (C.UserType.operator.equals(UserLocalData.getUser(mContext).getPartner()) || C.UserType.vipMember.equals(UserLocalData.getUser(mContext).getPartner())) {
//            commission.setVisibility(View.VISIBLE);
            commission.setText(getString(R.string.commission, MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), item.getCommission())));
        } else {
            commission.setText(getString(R.string.upgrade_commission));
//            commission.setVisibility(View.GONE);
        }
        //平台补贴
        if(LoginUtil.checkIsLogin((Activity) mContext, false) && !TextUtils.isEmpty(item.getSubsidiesPrice())){
            subsidiesPriceTv.setVisibility(View.VISIBLE);
            String getRatioSubside = MathUtils.getMuRatioSubSidiesPrice(UserLocalData.getUser(mContext).getCalculationRate(), item.getSubsidiesPrice());
            subsidiesPriceTv.setText(mContext.getString(R.string.subsidiesPrice, getRatioSubside));
        }else{
            subsidiesPriceTv.setVisibility(View.GONE);
            subsidiesPriceTv.setText("");
        }


        //店铺名称
        if (!TextUtils.isEmpty(item.getShopName())) {
            tv_shop_name.setText(item.getShopName());
        }
        discount_price.setText(getString(R.string.coupon, MathUtils.getVoucherPrice(item.getVoucherPrice())));
        price.setText(getString(R.string.income, MathUtils.getPrice(item.getPrice())));
        price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        sales.setText(getString(R.string.sales, MathUtils.getSales(item.getSaleMonth())));
        if (type == TYPE_OFFICAL_RECOM) { //官方推荐
            select.setVisibility(View.VISIBLE);
            if (mOnSelectListener != null) {
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnSelectListener.onSelect(v, position);
                    }
                });
            }
            if (slFlag.size() > 0) {
                if ("0".equals(slFlag.get(position).getCheck())) {
                    //未选择
                    bg.setVisibility(View.GONE);
                    select.setImageResource(R.drawable.icon_tupianweixuanzhong);
                } else {
                    //已选择
                    bg.setVisibility(View.VISIBLE);
                    bg.setImageResource(R.drawable.bg_home_list_grad_img_top);
                    bg.setScaleType(ImageView.ScaleType.FIT_XY);
                    select.setImageResource(R.drawable.icon_tupianxuanzhong);
                }
            }
        } else if (type == TYPE_SECKILL && item.getGrab_type() == 2) {
            bg.setVisibility(View.VISIBLE);
            bg.setImageResource(R.drawable.image_yiqiangguang);
            bg.setScaleType(ImageView.ScaleType.CENTER);
        } else {
            bg.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListType == C.GoodsListType.MATERIAL){
                    item.material = mMaterialID ;
                }
                GoodsDetailActivity.start(mContext, item);
            }
        });


        if (C.GoodsListType.BigShopList == mListType) {
            if (holder instanceof HolderLeft) {
                HolderLeft holderLeft = (HolderLeft) holder;
                if (holderLeft.mRecommendIndexView == null) {
                    holderLeft.mRecommendIndexView = new RecommendIndexView(mContext);
                    holderLeft.mRecommendIndexView.switchGridlayout();
                    img_rl.addView(holderLeft.mRecommendIndexView);
                }

                holderLeft.mRecommendIndexView.setText(mContext.getString(R.string.recommend_index) + item.getItemIndex());
            }
        }
    }

    public void setMaterialID(String materialID) {
        mMaterialID = materialID;
    }


    public class HolderLeft extends SimpleViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.commission)
        TextView commission;
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.select)
        ImageView select;
        @BindView(R.id.bg)
        ImageView bg;
        @BindView(R.id.sales)
        TextView sales;
        @BindView(R.id.coupon)
        TextView coupon;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.discount_price)
        TextView discount_price;

        @BindView(R.id.tv_shop_name)
        TextView tv_shop_name;
        public RecommendIndexView mRecommendIndexView;

        public HolderLeft(View itemView) {
            super(itemView);
        }
    }


    @Override
    public void add(List<ShopGoodInfo> items) {
        super.add(items);
        if (type == TYPE_OFFICAL_RECOM) {
            setSelectFlags(items);
        }

    }

    @Override
    public void replace(List<ShopGoodInfo> items) {
        super.replace(items);
        if (type == TYPE_OFFICAL_RECOM) {
            slFlag.clear();
            setSelectFlags(items);
        }
    }

    public void setListType(int listType) {
        mListType = listType;
    }

    private void setSelectFlags(List<ShopGoodInfo> items) {
        for (int i = 0; i < items.size(); i++) {
            SelectFlag selectFlag = new SelectFlag();
            selectFlag.setCheck("0");
            slFlag.add(selectFlag);
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public interface OnSelectListener {
        void onSelect(View v, int position);
    }

    public OnSelectListener getmOnSelectListener() {
        return mOnSelectListener;
    }

    public void setmOnSelectListener(OnSelectListener mOnSelectListener) {
        this.mOnSelectListener = mOnSelectListener;
    }

    /**
     * 官方推荐设置勾选值
     */
    public void setSelectFlag(List<SelectFlag> flags) {
        if (flags == null) {
            flags = new ArrayList<>();
        }
        slFlag.clear();
        if (flags.size() > 0) {
            slFlag.addAll(flags);
        }
    }

}
