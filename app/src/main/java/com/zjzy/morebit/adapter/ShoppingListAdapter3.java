package com.zjzy.morebit.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.zjzy.morebit.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 列表新版
 */
public class ShoppingListAdapter3 extends RecyclerView.Adapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<ShopGoodInfo> mDatas = new ArrayList<>();
    private int mType;
    private List<SelectFlag> slFlag = new ArrayList<>();
    private boolean isEditor;//收藏列表是否是编辑状态
    private String mMaterialID;
    private boolean showHotTag = true; //标识是否显示多点优选推荐

    public ShoppingListAdapter3(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new ViewHolder(mInflater.inflate(R.layout.item_shoppingaole4, parent, false));


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

        final ViewHolder viewHolder = (ViewHolder) holder;
        LoadImgUtils.loadingCornerBitmap(mContext, viewHolder.iv_icon, MathUtils.getPicture(info), 9);
        viewHolder.textview_original.setText(MathUtils.getnum(info.getVoucherPrice()));
        viewHolder.textvihew_Preco.setText("¥ " + MathUtils.getnum(info.getPrice()));
        viewHolder.textvihew_Preco.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
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
        viewHolder.momVolume.setText("销量：" + MathUtils.getSales(info.getSaleMonth()));

        viewHolder.textview.setText(info.getItemTitle()+"");








            viewHolder.coupon.setText(mContext.getString(R.string.yuan, MathUtils.getnum(info.getCouponPrice())));


            if (!TextUtils.isEmpty(info.getCommission())){
                viewHolder.commission.setText(mContext.getString(R.string.mcommission, MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), info.getCommission())));
            }


            if (TextUtils.isEmpty(info.getVideoid()) || "0".equals(info.getVideoid())) {
                viewHolder.video_play.setVisibility(View.GONE);
            } else {
                viewHolder.video_play.setVisibility(View.VISIBLE);
            }


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoodsDetailActivity.start(mContext, info);
                }
            });



            if (mDatas.size()-1==position&&mDatas.size()!=0){
                viewHolder.line.setVisibility(View.GONE);
            }else{
                viewHolder.line.setVisibility(View.VISIBLE);
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
        View iv_icon_bg, ll_prise,line;
        ImageView checkBox, good_mall_tag;
        private TextView markTv;


        public ViewHolder(View itemView) {
            super(itemView);
            markTv = (TextView)itemView.findViewById(R.id.markTv);
            textview = (TextView) itemView.findViewById(R.id.title);
            textview_original = (TextView) itemView.findViewById(R.id.discount_price);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            textvihew_Preco = (TextView) itemView.findViewById(R.id.price);
            momVolume = (TextView) itemView.findViewById(R.id.sales);
            coupon = (TextView) itemView.findViewById(R.id.coupon);
            img_rl = (RelativeLayout) itemView.findViewById(R.id.img_rl);
            commission = (TextView) itemView.findViewById(R.id.commission);
            select_tag = (ImageView) itemView.findViewById(R.id.select_tag);
            video_play = (ImageView) itemView.findViewById(R.id.video_play);
            iv_icon_bg = (View) itemView.findViewById(R.id.iv_icon_bg);
            line=itemView.findViewById(R.id.line);

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


}