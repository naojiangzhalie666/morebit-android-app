package com.zjzy.morebit.purchase.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.SettingActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.AuthorDialog;
import com.zjzy.morebit.Module.common.Dialog.ProductDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.purchase.PurchaseActivity;
import com.zjzy.morebit.utils.GlideImageLoader;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.UI.GlideImageCircularLoader;
import com.zjzy.morebit.utils.ViewShowUtils;

import java.util.List;

import io.reactivex.functions.Action;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/*
* 好货adapter
* */
public class ProductAdapter extends  RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    private Context context;
    private List<ShopGoodInfo> list;
    String ischeck;
    public ProductAdapter(Context context, List<ShopGoodInfo> list, String ischeck) {
        this.context=context;
        this.list=list;
        this.ischeck=ischeck;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.itme_product_goods, null);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Glide.with(context).load(list.get(position).getItemPicture())
//                .transform(new RoundedCornersTransformation(corner, 0, RoundedCornersTransformation.CornerType.ALL))
//                .into(holder.iv_head);
        LoadImgUtils.loadingCornerBitmap(context, holder.iv_head,list.get(position).getItemPicture());
        //LoadImgUtils.setImg(context, holder.iv_head, list.get(position).getItemPicture());
        final ShopGoodInfo info = list.get(position);
        info.setItemSourceId(list.get(position).getItemId());
        holder.tv_title.setText(list.get(position).getItemTitle());

        if (list.get(position).getSubsidyPrice()==null){
            holder.tv_price.setVisibility(View.GONE);
        }else{
            holder.tv_price.setVisibility(View.VISIBLE);
            holder.tv_price.setText("预估收益"+list.get(position).getSubsidyPrice());
        }
        holder.tv_subsidy.setText(list.get(position).getCouponPrice()+"元劵");
        holder.tv_coupon_price.setText("¥"+list.get(position).getItemPrice());
        holder.tv_coupon_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG); //中划线
        holder.price_after.setText("¥"+list.get(position).getItemVoucherPrice());

        holder.tv_share.setOnClickListener(new View.OnClickListener() {//立即购买
            @Override
            public void onClick(View v) {
                if (!LoginUtil.checkIsLogin((Activity) context)) {
                    return;
                }
                if (TaobaoUtil.isAuth()) {//淘宝授权
                    TaobaoUtil.getAllianceAppKey((BaseActivity) context);
                    return;
                }
                if (ischeck.equals("false")){
                    GoodsDetailActivity.start(context, info);
                }else{
                    final ProductDialog productDialog=new ProductDialog((PurchaseActivity) context);

                    productDialog.setmCancelListener(new ProductDialog.OnCancelListner() {//立即分享
                        @Override
                        public void onClick(View view) {
                            if(onItemAddClick != null) {
                                onItemAddClick.onShareItemClick();
                            }
                        }
                    });

                    productDialog.setmOkListener(new ProductDialog.OnOkListener() {//畅享0元购
                        @Override
                        public void onClick(View view) {
                            if(onItemAddClick != null) {
                                onItemAddClick.onItemClick();
                            }
                        }
                    });

                    productDialog.setmCloseListener(new ProductDialog.OnCloseListner() {//关闭dialog
                        @Override
                        public void onClick(View view) {
                            productDialog.dismiss();
                        }
                    });

                    productDialog.show();
                }

            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_share,tv_title,tv_price,tv_subsidy,price_after,tv_coupon_price;
        private ImageView iv_head;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_share=itemView.findViewById(R.id.tv_share);//购买
            iv_head=itemView.findViewById(R.id.iv_head);//主图
            tv_title=itemView.findViewById(R.id.tv_title);//标题
            tv_price=itemView.findViewById(R.id.tv_price);//预估收益
            tv_subsidy=itemView.findViewById(R.id.tv_subsidy);//优惠券
            price_after=itemView.findViewById(R.id.price_after);//劵后价
            tv_coupon_price=itemView.findViewById(R.id.tv_coupon_price);//原价

        }
    }


    public static interface OnAddClickListener {
        // true add; false cancel
        public void onItemClick(); //传递boolean类型数据给activity
        public void onShareItemClick(); //传递boolean类型数据给activity
    }

    // add click callback
    OnAddClickListener onItemAddClick;

    public void setOnAddClickListener(OnAddClickListener onItemAddClick) {
        this.onItemAddClick = onItemAddClick;
    }

}
