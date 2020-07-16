package com.zjzy.morebit.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.holder.SimpleViewHolder;
import com.zjzy.morebit.goods.shopping.ui.view.RecommendIndexView;
import com.zjzy.morebit.pojo.SelectFlag;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.blankj.utilcode.util.StringUtils.getString;

/**
 * Created by YangBoTian on 2018/8/22.
 */

public class SearchGuessAdapter extends RecyclerView.Adapter<SearchGuessAdapter.ViewHolder> {


    private Context mContext;//
    private List<ShopGoodInfo> mdata = new ArrayList<>();

    public SearchGuessAdapter(Context context) {

        this.mContext = context;

    }

    public void setData(List<ShopGoodInfo> data) {
        if (data != null) {
            mdata.addAll(data);
            notifyItemRangeChanged(0, data.size());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(mContext).inflate(R.layout.item_search_guess, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ShopGoodInfo item = mdata.get(position);
        holder.title.setText(MathUtils.getTitle(item));
        LoadImgUtils.loadingCornerBitmap(mContext, holder.iv_icon, item.getItemPicture(), 8);
        if (StringsUtils.isEmpty(item.getCouponPrice())) {
            holder.coupon.setVisibility(View.GONE);
        } else {
            holder.coupon.setVisibility(View.VISIBLE);
            holder.coupon.setText(getString(R.string.yuan, MathUtils.getnum(item.getCouponPrice())));
        }
        if (!StringsUtils.isEmpty(item.getCommission())) {
            holder.commission.setText(mContext.getString(R.string.mcommission, MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), item.getCommission())));
        } else {
            holder.commission.setVisibility(View.GONE);
        }
        if (item.getShoptype() == 1) {
            holder.good_mall_tag.setImageResource(R.mipmap.guess_tao_icon);
        } else {
            holder.good_mall_tag.setImageResource(R.mipmap.guess_tm_icon);
        }


        holder.volume.setText(MathUtils.getnum(item.getItemPrice()));
        holder.volume.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        holder.discount_price.setText(MathUtils.getnum(item.getItemVoucherPrice()));
        holder.shop_name.setText(item.getShopName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoodsDetailActivity.start(mContext, item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

//    @Override
//    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
//        final ShopGoodInfo item = getItem(position);
//        TextView title = holder.viewFinder().view(R.id.title);
//        TextView commission = holder.viewFinder().view(R.id.commission);
//        TextView coupon = holder.viewFinder().view(R.id.coupon);
//        TextView discount_price = holder.viewFinder().view(R.id.discount_price);
//        ImageView img = holder.viewFinder().view(R.id.img);
//
//        title.setText(MathUtils.getTitle(item));
//        LoadImgUtils.setImg(mContext, img, MathUtils.getPicture(item));
//        if (StringsUtils.isEmpty(item.getCouponPrice())) {
//            coupon.setVisibility(View.GONE);
//        } else {
//            coupon.setVisibility(View.VISIBLE);
//            coupon.setText(getString(R.string.yuan, MathUtils.getnum(item.getCouponPrice())));
//        }
//        if (!StringsUtils.isEmpty(item.getCommission())) {
//            commission.setText(mContext.getString(R.string.mcommission, MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), item.getCommission())));
//        } else {
//            commission.setVisibility(View.GONE);
//        }
//
//
//        discount_price.setText(MathUtils.getSale(item.getSaleMonth()));
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (mListType == C.GoodsListType.MATERIAL) {
//                    item.material = mMaterialID;
//                }
//                GoodsDetailActivity.start(mContext, item);
//            }
//        });
//
//
//    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, commission, coupon, discount_price, volume, shop_name;
        private ImageView iv_icon, good_mall_tag;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            commission = itemView.findViewById(R.id.commission);
            coupon = itemView.findViewById(R.id.coupon);
            discount_price = itemView.findViewById(R.id.discount_price);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            volume = itemView.findViewById(R.id.volume);
            shop_name = itemView.findViewById(R.id.shop_name);
            good_mall_tag = itemView.findViewById(R.id.good_mall_tag);
        }
    }
}
