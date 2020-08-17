package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.Activity.NumberGoodsDetailsActivity;
import com.zjzy.morebit.Module.common.Dialog.GoodsDeteleDialog;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.order.ConfirmOrderActivity2;
import com.zjzy.morebit.pojo.RequestIscheckShopcarBean;
import com.zjzy.morebit.pojo.ShopCarGoodsBean;
import com.zjzy.morebit.pojo.request.RequestAddShopcarBean;
import com.zjzy.morebit.pojo.request.RequestDeleteShopcarBean;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.ViewShowUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/*
 * 购物车adapter
 * */
public class ShopCarOrderAdapter extends RecyclerView.Adapter<ShopCarOrderAdapter.ViewHolder> {
    private Context mContext;
    private List<ShopCarGoodsBean.CartListBean> list = new ArrayList<>();

    public ShopCarOrderAdapter(Context context) {
        this.mContext = context;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_shopcar_order, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ShopCarGoodsBean.CartListBean listBean = list.get(position);
        LoadImgUtils.loadingCornerBitmap(mContext, holder.img_confirm_order_goods, listBean.getPicUrl(),5);
        holder.txt_confirm_order_goods_title.setText(listBean.getGoodsName());
        if (!TextUtils.isEmpty(listBean.getPrice())) {
            holder.txt_confirm_order_goods_price.setText(listBean.getPrice() + "");
        }

        if (!TextUtils.isEmpty(listBean.getNumber())) {
            holder.txt_confirm_order_goods_count.setText("x" + listBean.getNumber());
        }
        List<String> specifications = listBean.getSpecifications();
        if (specifications != null && specifications.size() > 0) {
            holder.txt_confirm_order_goods_spec.setText("规格：" + specifications.get(0));
        }




    }




    @Override
    public int getItemCount() {

        return list == null ? 0 : list.size();


    }

    public void addData(List<ShopCarGoodsBean.CartListBean> data) {
        if (data != null) {
            list.clear();
            list.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void setData(List<ShopCarGoodsBean.CartListBean> data) {
        if (data != null) {
            list.addAll(data);
            notifyItemRangeChanged(0, data.size());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_confirm_order_goods;
        private TextView txt_confirm_order_goods_title, txt_confirm_order_goods_spec, txt_confirm_order_goods_price, txt_confirm_order_goods_count;


        public ViewHolder(View itemView) {
            super(itemView);

            img_confirm_order_goods = itemView.findViewById(R.id.img_confirm_order_goods);//主图
            txt_confirm_order_goods_title = itemView.findViewById(R.id.txt_confirm_order_goods_title);//标题
            txt_confirm_order_goods_spec = itemView.findViewById(R.id.txt_confirm_order_goods_spec);//规格
            txt_confirm_order_goods_price = itemView.findViewById(R.id.txt_confirm_order_goods_price);//价格
            txt_confirm_order_goods_count = itemView.findViewById(R.id.txt_confirm_order_goods_count);//数量
        }
    }


    public static interface OnAddClickListener {

        public void onShareClick(int position);
        public void onCheckNum(int position, boolean ischeck);
      //  public void onCheckNum2(int position);

    }

    // add click callback
    OnAddClickListener onItemAddClick;

    public void setOnAddClickListener(OnAddClickListener onItemAddClick) {
        this.onItemAddClick = onItemAddClick;
    }

}
