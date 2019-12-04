package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.LeadingMarginSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goods.shopping.ui.view.ForeshowItemTiemView;
import com.zjzy.morebit.goods.shopping.ui.view.RecommendIndexView;
import com.zjzy.morebit.pojo.SelectFlag;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.ViewShowUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 列表新版
 */
public class ShoppingListAdapter extends RecyclerView.Adapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<ShopGoodInfo> mDatas = new ArrayList<>();
    private int mType;
    private List<SelectFlag> slFlag = new ArrayList<>();
    private boolean isEditor;//收藏列表是否是编辑状态
    private String mMaterialID;
    private boolean showHotTag = false; //标识是否显示马克猫推荐

    public ShoppingListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas == null || mDatas.size() == 0) {
            return 0;
        } else {
            if (mDatas.get(position) != null)
                return mDatas.get(position).isSearchType;
            else
                return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ViewHolder(mInflater.inflate(R.layout.item_shoppingaole2, parent, false));
        } else if (viewType == 1) {
            return new OuterViewHolder(mInflater.inflate(R.layout.item_search_type_img, parent, false));
        } else {
            return new OuterViewHolder(mInflater.inflate(R.layout.item_your_love, parent, false));
        }
    }


    public void setData(List<ShopGoodInfo> data) {
        if (data != null) {
            mDatas.clear();
            mDatas.addAll(data);
            //处理官方推荐
            if (C.GoodsListType.officialrecomList == mType) {
                slFlag.clear();
                for (int i = 0; i < mDatas.size(); i++) {
                    SelectFlag selectFlag = new SelectFlag();
                    selectFlag.setCheck("0");
                    slFlag.add(selectFlag);
                }
            }
        }
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

    /**
     * 获取官方推荐勾选值
     */
    public List<SelectFlag> getSelectFlag() {
        return slFlag;
    }


    /**
     * 设置不同的列表名称
     *
     * @param type
     */
    public void setType(int type) {
        mType = type;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ShopGoodInfo info = mDatas.get(position);
        if (info == null) return;
        if (holder instanceof OuterViewHolder) {
            if(position==0){
                ((OuterViewHolder) holder).line.setVisibility(View.GONE);
            } else {
                ((OuterViewHolder) holder).line.setVisibility(View.VISIBLE);
            }
            return;
        }
        final ViewHolder viewHolder = (ViewHolder) holder;
        LoadImgUtils.loadingCornerBitmap(mContext, viewHolder.iv_icon, MathUtils.getPicture(info), 9);
        viewHolder.textview_original.setText(mContext.getString(R.string.coupon, MathUtils.getVoucherPrice(info.getVoucherPrice())));
        viewHolder.textvihew_Preco.setText("" + MathUtils.getPrice(info.getPrice()));
        viewHolder.textvihew_Preco.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        if(!TextUtils.isEmpty(info.getMarkValue()) && info.getItemFrom().equals("1") && showHotTag){
            viewHolder.markTv.setVisibility(View.VISIBLE);
            viewHolder.markTv.setBackgroundResource(R.drawable.bg_search_recommod_good);
            viewHolder.markTv.setText(info.getMarkValue());
        }else if(!TextUtils.isEmpty(info.getItemLabeling())  ){
            viewHolder.markTv.setVisibility(View.VISIBLE);
            viewHolder.markTv.setText(info.getItemLabeling());
        }else{
            viewHolder.markTv.setVisibility(View.GONE);
        }


        try {
            if (C.UserType.member.equals(UserLocalData.getUser((Activity) mContext).getPartner()) || info.isCollect) {
                viewHolder.ll_prise.setVisibility(View.GONE);
            } else {
                if (StringsUtils.isEmpty(info.getCouponPrice())) {
                    viewHolder.ll_prise.setVisibility(View.GONE);
                } else {
                    viewHolder.ll_prise.setVisibility(View.VISIBLE);
                }


            }

            if (StringsUtils.isEmpty(info.getCouponPrice())) {
                viewHolder.return_cash.setVisibility(View.GONE);
            } else {
                viewHolder.return_cash.setVisibility(View.VISIBLE);
            }
            //店铺名称
            if (!TextUtils.isEmpty(info.getShopName())) {
                viewHolder.tv_shop_name.setText(info.getShopName());
            }

            viewHolder.coupon.setText(mContext.getString(R.string.yuan, MathUtils.getCouponPrice(info.getCouponPrice())));

            //平台补贴
            if(LoginUtil.checkIsLogin((Activity) mContext, false) && !TextUtils.isEmpty(info.getSubsidiesPrice())){
                viewHolder.subsidiesPriceTv.setVisibility(View.VISIBLE);
                String getRatioSubside = MathUtils.getMuRatioSubSidiesPrice(UserLocalData.getUser(mContext).getCalculationRate(),info.getSubsidiesPrice());
                viewHolder.subsidiesPriceTv.setText(mContext.getString(R.string.subsidiesPrice, getRatioSubside));
            }else{
                viewHolder.subsidiesPriceTv.setVisibility(View.GONE);
                viewHolder.subsidiesPriceTv.setText("");
            }


            if (info.getType() == 1) {  //如果是收藏商品
                viewHolder.commission.setVisibility(View.GONE);
                if (isEditor) {
                    viewHolder.checkBox.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.checkBox.setVisibility(View.GONE);
                }


                viewHolder.checkBox.setSelected(info.isSelect());
                viewHolder.toDetail.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (onAdapterClickListener != null) {
                            onAdapterClickListener.onLongItem(position);
                        }
                        return false;
                    }
                });
                viewHolder.toDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isEditor) {
                            info.setSelect(!info.isSelect());
                            viewHolder.checkBox.setSelected(info.isSelect());
                            if (onAdapterClickListener != null) {
                                onAdapterClickListener.onItem(position);
                            }
                        } else {
                            if (mType == C.GoodsListType.MATERIAL){
                                info.material = mMaterialID ;
                            }
                            GoodsDetailActivity.start(mContext, info);
                        }
                    }
                });
            } else {
                viewHolder.checkBox.setVisibility(View.GONE);
                viewHolder.toDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (C.GoodsListType.ForeShow_1 == mType) {
                            if (info.isCountDownEnd) {
                                ViewShowUtils.showShortToast(mContext, mContext.getString(R.string.hot_end_coming_soon));
                            } else {
                                ViewShowUtils.showShortToast(mContext, mContext.getString(R.string.hot_start_coming_soon));
                            }
                        } else {
                            if (mType == C.GoodsListType.MATERIAL){
                                info.material = mMaterialID ;
                            }
                            GoodsDetailActivity.start(mContext, info);

                        }
                    }
                });
                if (C.UserType.vipMember.equals(UserLocalData.getUser((Activity) mContext).getPartner()) || C.UserType.operator.equals(UserLocalData.getUser((Activity) mContext).getPartner())) {
                    viewHolder.commission.setText(mContext.getString(R.string.commission, MathUtils.getMuRatioComPrice(UserLocalData.getUser((Activity) mContext).getCalculationRate(), info.getCommission())));
                } else {
                    viewHolder.commission.setText(mContext.getString(R.string.upgrade_commission));
                }
            }
            viewHolder.momVolume.setText("销量：" + MathUtils.getSales(info.getSaleMonth()));

            //判断是淘宝还是天猫的商品
            if (info.getShopType() == 2) {
                viewHolder.good_mall_tag.setImageResource(R.drawable.tianmao);
            } else {
                viewHolder.good_mall_tag.setImageResource(R.drawable.taobao);
            }
            StringsUtils.retractTitle(viewHolder.good_mall_tag,viewHolder.textview,MathUtils.getTitle(info));

            if (TextUtils.isEmpty(info.getVideoid()) || "0".equals(info.getVideoid())) {
                viewHolder.video_play.setVisibility(View.GONE);
            } else {
                viewHolder.video_play.setVisibility(View.VISIBLE);
            }


            if (C.GoodsListType.officialrecomList == mType) {
                viewHolder.select_tag.setVisibility(View.VISIBLE);
                viewHolder.iv_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onSelectTagListener != null) {
                            onSelectTagListener.onSelect(position);
                        }
                    }
                });
                if (slFlag.size() > 0) {
                    if ("0".equals(slFlag.get(position).getCheck())) {
                        //未选择
                        viewHolder.iv_icon_bg.setVisibility(View.INVISIBLE);
                        viewHolder.select_tag.setImageResource(R.drawable.icon_official_gouxuan_huise);
                    } else {
                        //已选择
                        viewHolder.iv_icon_bg.setVisibility(View.VISIBLE);
                        viewHolder.select_tag.setImageResource(R.drawable.icon_official_gouxuan);
                    }
                }
            }

            if (C.GoodsListType.ForeShow_1 == mType) {
                viewHolder.iv_icon_bg.setVisibility(View.VISIBLE);
                viewHolder.coupon.setText(mContext.getString(R.string.coming_soon));

                if (viewHolder.mForeshowItemTiemView == null) {
                    viewHolder.mForeshowItemTiemView = new ForeshowItemTiemView(mContext);
                    viewHolder.img_rl.addView(viewHolder.mForeshowItemTiemView);
                    viewHolder.mForeshowItemTiemView.setTimerData(mContext, info);
                } else {
                    viewHolder.mForeshowItemTiemView.setTimerData(mContext, info);
                }
            } else if (C.GoodsListType.BigShopList == mType) {
                if (viewHolder.mRecommendIndexView == null) {
                    viewHolder.mRecommendIndexView = new RecommendIndexView(mContext);
                    viewHolder.img_rl.addView(viewHolder.mRecommendIndexView);
                    viewHolder.mRecommendIndexView.setText(mContext.getString(R.string.recommend_index) + info.getItemIndex());
                } else {
                    viewHolder.mRecommendIndexView.setText(mContext.getString(R.string.recommend_index) + info.getItemIndex());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setMaterialID(String materialID) {
        mMaterialID = materialID;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ForeshowItemTiemView mForeshowItemTiemView;
        RecommendIndexView mRecommendIndexView;
        TextView textview, textview_original, textvihew_Preco, momVolume, coupon, commission, tv_shop_name;
        ImageView iv_icon;
        LinearLayout return_cash;
        RelativeLayout toDetail, img_rl;
        ImageView select_tag, video_play;
        View iv_icon_bg, ll_prise;
        ImageView checkBox, good_mall_tag;
        private TextView markTv;
        private TextView subsidiesPriceTv;

        public ViewHolder(View itemView) {
            super(itemView);
            markTv = (TextView)itemView.findViewById(R.id.markTv);
            textview = (TextView) itemView.findViewById(R.id.title);
            tv_shop_name = (TextView) itemView.findViewById(R.id.tv_shop_name);
            textview_original = (TextView) itemView.findViewById(R.id.discount_price);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            textvihew_Preco = (TextView) itemView.findViewById(R.id.price);
            momVolume = (TextView) itemView.findViewById(R.id.sales);
            coupon = (TextView) itemView.findViewById(R.id.coupon);
            toDetail = (RelativeLayout) itemView.findViewById(R.id.toDetail);
            img_rl = (RelativeLayout) itemView.findViewById(R.id.img_rl);
            good_mall_tag = (ImageView) itemView.findViewById(R.id.good_mall_tag);
            commission = (TextView) itemView.findViewById(R.id.commission);
            select_tag = (ImageView) itemView.findViewById(R.id.select_tag);
            video_play = (ImageView) itemView.findViewById(R.id.video_play);
            iv_icon_bg = (View) itemView.findViewById(R.id.iv_icon_bg);
            ll_prise = (View) itemView.findViewById(R.id.ll_return_cash);
            checkBox = (ImageView) itemView.findViewById(R.id.checkbox);
            subsidiesPriceTv = itemView.findViewById(R.id.subsidiesPriceTv);
            return_cash = (LinearLayout) itemView.findViewById(R.id.ll_return_cash);
        }
    }

    private class OuterViewHolder extends RecyclerView.ViewHolder {
            View line;
        public OuterViewHolder(View itemView) {
            super(itemView);
            line = itemView.findViewById(R.id.line);
        }
    }


    private OnAdapterClickListener onAdapterClickListener;

    public interface OnAdapterClickListener {
        void onItem(int position);

        void onLongItem(int position);
    }

    public void setOnAdapterClickListener(OnAdapterClickListener listener) {
        onAdapterClickListener = listener;
    }

    private OnSelectTagListener onSelectTagListener;

    public interface OnSelectTagListener {
        public void onSelect(int position);
    }

    public void setOnSelectTagListener(OnSelectTagListener listener) {
        onSelectTagListener = listener;
    }


    public boolean isEditor() {
        return isEditor;
    }

    public void setEditor(boolean editor) {
        isEditor = editor;
    }

//
//    tvDes.setText(getSpannableString(label, description));
//      tvLabel.setText(label);

    /**
     * 首行缩进的SpannableString
     *
     * @param description 描述信息
     */
    private SpannableString getSpannableString(String description) {
        SpannableString spannableString = new SpannableString(description);
        int dimension = (int) mContext.getResources().getDimension(R.dimen.good_mall_wide);
        int padding = (int) mContext.getResources().getDimension(R.dimen.good_mall_wide_padding);
        LeadingMarginSpan leadingMarginSpan = new LeadingMarginSpan.Standard(dimension + padding, 0);//仅首行缩进
        spannableString.setSpan(leadingMarginSpan, 0, description.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    public void setShowHotTag(boolean showHotTag) {
        this.showHotTag = showHotTag;
    }
}