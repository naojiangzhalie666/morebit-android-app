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
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.ViewShowUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 列表新版
 */
public class TbSearchAdapter extends RecyclerView.Adapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<ShopGoodInfo> mDatas = new ArrayList<>();
    private int mType;
    private List<SelectFlag> slFlag = new ArrayList<>();
    private boolean isEditor;//收藏列表是否是编辑状态
    private String mMaterialID;
    private boolean showHotTag = true; //标识是否显示多点优选推荐

    public TbSearchAdapter(Context context) {
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
            return new ViewHolder(mInflater.inflate(R.layout.item_tbsearch, parent, false));
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
           // notifyItemRangeChanged(0,data.size());
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
        viewHolder.textview_original.setText(MathUtils.getnum(info.getVoucherPrice()));
        String  itemLabeling = info.getItemLabeling();
        if(!TextUtils.isEmpty(info.getMarkValue()) && info.getItemFrom().equals("1") && showHotTag){
            viewHolder.markTv.setVisibility(View.VISIBLE);
            viewHolder.markTv.setBackgroundResource(R.drawable.bg_search_recommod_good);
            viewHolder.markTv.setText(info.getMarkValue());
        }else if(!TextUtils.isEmpty(itemLabeling)
        && (itemLabeling !=null && !itemLabeling.equals("NULL"))){
            viewHolder.markTv.setVisibility(View.VISIBLE);
            viewHolder.markTv.setText(info.getItemLabeling());
        }else{
            viewHolder.markTv.setVisibility(View.GONE);
        }



            if (StringsUtils.isEmpty(info.getCouponPrice())) {
                viewHolder.coupon.setVisibility(View.GONE);
            } else {
                viewHolder.coupon.setVisibility(View.VISIBLE);
            }
            //店铺名称
            if (!TextUtils.isEmpty(info.getShopName())) {
                viewHolder.tv_shop_name.setText(info.getShopName());
            }

            viewHolder.coupon.setText(mContext.getString(R.string.yuan, MathUtils.getnum(info.getCouponPrice())));



             //   viewHolder.checkBox.setSelected(info.isSelect());
//                viewHolder.toDetail.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        if (onAdapterClickListener != null) {
//                            onAdapterClickListener.onLongItem(position);
//                        }
//                        return false;
//                    }
//                });
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            GoodsDetailActivity.start(mContext, info);

                    }
                });

                if (!TextUtils.isEmpty(info.getCommission())) {
                    viewHolder.commission.setText(mContext.getString(R.string.mcommission, MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), info.getCommission())));

                }

            viewHolder.momVolume.setText("销量：" + MathUtils.getSales(info.getSaleMonth()));

            //判断是淘宝还是天猫的商品
            if (info.getShopType() == 2) {
                viewHolder.good_mall_tag.setImageResource(R.drawable.tm_list_icon);
            } else {
                viewHolder.good_mall_tag.setImageResource(R.drawable.tb_list_icon);
            }
        viewHolder.textview.setText(info.getItemTitle());

            if (TextUtils.isEmpty(info.getVideoid()) || "0".equals(info.getVideoid())) {
                viewHolder.video_play.setVisibility(View.GONE);
            } else {
                viewHolder.video_play.setVisibility(View.VISIBLE);
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
        TextView textview, textview_original, momVolume, coupon, commission, tv_shop_name;
        ImageView iv_icon;
        LinearLayout return_cash;
        RelativeLayout toDetail, img_rl;
        ImageView select_tag, video_play;
        View iv_icon_bg, ll_prise;
        ImageView checkBox, good_mall_tag;
        private TextView markTv;


        public ViewHolder(View itemView) {
            super(itemView);
            markTv = (TextView)itemView.findViewById(R.id.markTv);
            textview = (TextView) itemView.findViewById(R.id.title);
            tv_shop_name = (TextView) itemView.findViewById(R.id.shop_name);
            textview_original = (TextView) itemView.findViewById(R.id.discount_price);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            momVolume = (TextView) itemView.findViewById(R.id.volume);
            coupon = (TextView) itemView.findViewById(R.id.coupon);
            toDetail = (RelativeLayout) itemView.findViewById(R.id.toDetail);
            img_rl = (RelativeLayout) itemView.findViewById(R.id.img_rl);
            good_mall_tag = (ImageView) itemView.findViewById(R.id.good_mall_tag);
            commission = (TextView) itemView.findViewById(R.id.commission);
            select_tag = (ImageView) itemView.findViewById(R.id.select_tag);
            video_play = (ImageView) itemView.findViewById(R.id.video_play);
            iv_icon_bg = (View) itemView.findViewById(R.id.iv_icon_bg);

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




    public void setShowHotTag(boolean showHotTag) {
        this.showHotTag = showHotTag;
    }
}