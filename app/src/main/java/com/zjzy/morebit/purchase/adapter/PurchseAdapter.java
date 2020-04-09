package com.zjzy.morebit.purchase.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Module.common.Dialog.PurchaseDialog;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.purchase.PurchaseActivity;
import com.zjzy.morebit.utils.LoadImgUtils;

import java.util.List;

/*
 * 免单adapter
 * */
public class PurchseAdapter extends RecyclerView.Adapter<PurchseAdapter.ViewHolder> {
    private Context context;
    private List<ShopGoodInfo> list;
    public PurchseAdapter(Context context, List<ShopGoodInfo> list) {
        this.context=context;
        this.list=list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.itme_pruchase_goods, null);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      //  LoadImgUtils.setImg(context, holder.iv_head, list.get(position).getItemPicture());
        final ShopGoodInfo info = list.get(position);
        LoadImgUtils.loadingCornerBitmap(context, holder.iv_head,list.get(position).getItemPicture());
        holder.tv_title.setText(list.get(position).getItemTitle());
        holder.tv_price.setText(list.get(position).getCouponPrice()+"元劵");
        holder.tv_subsidy.setText("补贴"+list.get(position).getSubsidyPrice()+"元");
        holder.tv_coupon_price.setText("¥"+list.get(position).getItemPrice());
        holder.tv_coupon_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG); //中划线
        holder.tv_share.setOnClickListener(new View.OnClickListener() {//立即购买
            @Override
            public void onClick(View v) {

                final PurchaseDialog purchaseDialog=new PurchaseDialog((PurchaseActivity) context);

                purchaseDialog.setmCancelListener(new PurchaseDialog.OnCancelListner() {//查看商品
                    @Override
                    public void onClick(View view) {
                        GoodsDetailActivity.start(context, info);
                    }
                });

                purchaseDialog.setmOkListener(new PurchaseDialog.OnOkListener() {//立即分享
                    @Override
                    public void onClick(View view) {

                    }
                });

                purchaseDialog.setmCloseListener(new PurchaseDialog.OnCloseListner() {//关闭dialog
                    @Override
                    public void onClick(View view) {
                        purchaseDialog.dismiss();
                    }
                });

                purchaseDialog.show();

            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_share,tv_title,tv_price,tv_subsidy,tv_coupon_price;
        private ImageView iv_head;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_share=itemView.findViewById(R.id.tv_share);//购买
            iv_head=itemView.findViewById(R.id.iv_head);//主图
            tv_title=itemView.findViewById(R.id.tv_title);//标题
            tv_price=itemView.findViewById(R.id.tv_price);//优惠券
            tv_subsidy=itemView.findViewById(R.id.tv_subsidy);//预估收益
            tv_coupon_price=itemView.findViewById(R.id.tv_coupon_price);//劵后价
        }
    }
}
