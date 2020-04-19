package com.zjzy.morebit.purchase.adapter;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.App;
import com.zjzy.morebit.MainActivity;
import com.zjzy.morebit.Module.common.Dialog.PurchaseDialog;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.purchase.PurchaseActivity;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.ImageUtils;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.UI.GlideImageCircularLoader;
import com.zjzy.morebit.utils.action.MyAction;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*
 * 免单adapter
 * */
public class PurchsePosterAdapter extends RecyclerView.Adapter<PurchsePosterAdapter.ViewHolder> {
    private Context context;
    private List<ShopGoodInfo> list;
    private ArrayList<Bitmap> mUrlArrayList;
    public PurchsePosterAdapter(Context context, List<ShopGoodInfo> list, ArrayList<Bitmap> mUrlArrayList) {
        this.context=context;
        this.list=list;
        this.mUrlArrayList=mUrlArrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.itme_pruchase_poster_goods, null);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
       // LoadImgUtils.setImg(context, holder.iv_head, list.get(position).getItemPicture());
        final ShopGoodInfo info = list.get(position);
        Glide.with(context)
                .asBitmap()
                .load(list.get(position).getItemPicture())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Log.e("bbbb","onLoadStarted"+resource);
                        holder.iv_head.setImageBitmap(resource);
                    }
                });
    //  Bitmap bitMBitmap = ImageUtils.returnBitMap(list.get(position).getItemPicture());
//        Glide.with(App.getAppContext())
//                .asBitmap()
//                .load(list.get(position).getItemPicture())
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                        Drawable drawable = new BitmapDrawable(resource);
//                        holder.iv_head.setBackground(drawable);
//                    }
//
//                });

       // holder.iv_head.setImageBitmap(mUrlArrayList.get(position));
     // holder.iv_head.setImageBitmap(mUrlArrayList.get(position));

//        Glide.with(context)
//                .load(list.get(position).getItemPicture())
//                .into(holder.iv_head)
//                .preload()

        ;

//        Glide.with(context)
//                .asBitmap()
//                .load(list.get(position).getItemPicture())
//
//                .into(new Target<Bitmap>() {
//                          @Override
//                          public void onLoadStarted(@Nullable Drawable placeholder) {
//                              holder.iv_head.setImageDrawable(placeholder);
//                              Log.e("bbbb","onLoadStarted");
//                          }
//
//                          @Override
//                          public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                              Log.e("bbbb","onLoadFailed");
//                          }
//
//                          @Override
//                          public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                              holder.iv_head.setImageBitmap(resource);
//                              Log.e("bbbb","resource");
//                          }
//
//                          @Override
//                          public void onLoadCleared(@Nullable Drawable placeholder) {
//                              Log.e("bbbb","onLoadCleared");
//                          }
//
//                          @Override
//                          public void getSize(@NonNull SizeReadyCallback cb) {
//                             // Log.e("bbbb","onStop");
//                          }
//
//                          @Override
//                          public void removeCallback(@NonNull SizeReadyCallback cb) {
//                            //  Log.e("bbbb","onStop");
//                          }
//
//                          @Override
//                          public void setRequest(@Nullable Request request) {
//                           //   Log.e("bbbb","onStop");
//                          }
//
//                          @Nullable
//                          @Override
//                          public Request getRequest() {
//                              return null;
//                          }
//
//                          @Override
//                          public void onStart() {
//                              Log.e("bbbb","onStop");
//                          }
//
//                          @Override
//                          public void onStop() {
//                              Log.e("bbbb","onStop");
//                          }
//
//                          @Override
//                          public void onDestroy() {
//                                Log.e("bbbb","destory");
//                          }
//                      });
              //          LoadImgUtils.loadingCornerBitmap(App.getAppContext(), holder.iv_head, list.get(position).getItemPicture());
        holder.tv_title.setText(list.get(position).getItemTitle());
        holder.tv_price.setText(list.get(position).getCouponPrice()+"元劵");
        holder.tv_subsidy.setText("补贴"+list.get(position).getSubsidyPrice()+"元");


    }




    @Override
    public int getItemCount() {
        if (list.size()>3){
            return 3;
        }else{
            return list.size();

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title,tv_price,tv_subsidy;
        private ImageView iv_head;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_head=itemView.findViewById(R.id.iv_head);//主图
            tv_title=itemView.findViewById(R.id.tv_title);//标题
            tv_price=itemView.findViewById(R.id.tv_price);//优惠券
            tv_subsidy=itemView.findViewById(R.id.tv_subsidy);//预估收益

        }
    }
}
