package com.zjzy.morebit.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Activity.SearchActivity;
import com.zjzy.morebit.Activity.ShortVideoPlayActivity;
import com.zjzy.morebit.Module.common.Activity.ImagePagerActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.MarkermallCircleInfo;
import com.zjzy.morebit.pojo.MarkermallCircleItemInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * 每日好货图片
 * */
public class PhotoDialyAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<String> list = new ArrayList<>();
    private MarkermallCircleInfo circleInfo;
    private int circletype;


    public PhotoDialyAdapter(Context context, List<String> picture, MarkermallCircleInfo circleInfo, int circletype) {
        this.context = context;
        this.list = picture;
        this.circleInfo = circleInfo;
        this.circletype=circletype;
    }


    @Override
    public int getItemViewType(int position) {
        int viewType = list.size();
        if (viewType == 1) {
            return viewType;
        } else if (viewType == 2) {
            return viewType;
        } else {
            return viewType;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;
        if (viewType == 1) {
            view = LayoutInflater.from(context).inflate(R.layout.itme_dialy_photo2, parent, false);
            return new ViewHolder2(view);
        } else if (viewType == 2) {
            view = LayoutInflater.from(context).inflate(R.layout.itme_dialy_photo3, parent, false);
            return new ViewHolder3(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.itme_dialy_photo, parent, false);
            return new ViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);
        MarkermallCircleItemInfo info2 = circleInfo.getShareRangItems().get(position);
        if (viewType == 1) {
            ViewHolder2 holder2 = (ViewHolder2) holder;
            LoadImgUtils.setImg(context, holder2.img2, info2.getPicture());
            clickPhoto(position, holder, info2);
            getPhoto(holder2);
            if (!TextUtils.isEmpty(info2.getItemVideoid())){
                holder2.img_bo.setVisibility(View.VISIBLE);
            }else{
                holder2.img_bo.setVisibility(View.GONE);
            }
        } else if (viewType == 2) {
            ViewHolder3 holder3 = (ViewHolder3) holder;
            LoadImgUtils.setImg(context, holder3.img3, info2.getPicture());
            clickPhoto(position, holder,info2);
            getPhoto(holder3);
            if (!TextUtils.isEmpty(info2.getItemVideoid())){
                holder3.img_bo.setVisibility(View.VISIBLE);
            }else{
                holder3.img_bo.setVisibility(View.GONE);
            }
        } else {
            ViewHolder holder1 = (ViewHolder) holder;
            LoadImgUtils.setImg(context, holder1.img, info2.getPicture());
            clickPhoto(position, holder, info2);
            getPhoto(holder1);
            if (!TextUtils.isEmpty(info2.getItemVideoid())){
                holder1.img_bo.setVisibility(View.VISIBLE);
            }else{
                holder1.img_bo.setVisibility(View.GONE);
            }
        }

    }

    private void getPhoto(ViewHolder2 holder2) {

            List<ShopGoodInfo> goods = circleInfo.getGoods();
            for (ShopGoodInfo info : goods) {

                    if (info.getIsExpire() == 0) {
                        holder2.tv_qiang.setVisibility(View.GONE);
                    } else {

                        holder2.tv_qiang.setVisibility(View.VISIBLE);
                    }



        }


    }


    private void getPhoto(ViewHolder3 holder3) {
            List<ShopGoodInfo> goods = circleInfo.getGoods();
            for (ShopGoodInfo info : goods) {

                if (info.getIsExpire() == 0) {

                    holder3.tv_qiang.setVisibility(View.GONE);
                } else {

                    holder3.tv_qiang.setVisibility(View.VISIBLE);
                }


        }
    }


    private void getPhoto(ViewHolder holder1) {

            List<ShopGoodInfo> goods = circleInfo.getGoods();
            for (ShopGoodInfo info : goods) {

                if (info.getIsExpire() == 0) {

                    holder1.tv_qiang.setVisibility(View.GONE);
                } else {

                    holder1.tv_qiang.setVisibility(View.VISIBLE);
                }

            }

    }


    private void clickPhoto(final int position, RecyclerView.ViewHolder holder, final MarkermallCircleItemInfo info2) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {//查看大图
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(info2.getItemVideoid())){
                    Intent it = new Intent(context, ShortVideoPlayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(C.Extras.ITEMVIDEOID, info2.getItemVideoid());
                    it.putExtras(bundle);
                    context.startActivity(it);

                }else{
                    Intent intent = new Intent(context, ImagePagerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) list);
                    bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });


    }


    @Override
    public int getItemCount() {


        return list == null ? 0 : list.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img,img_bo;
        private TextView tv_qiang;


        public ViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);//
            tv_qiang = itemView.findViewById(R.id.tv_qiang);
            img_bo=itemView.findViewById(R.id.img_bo);

        }
    }


    public class ViewHolder2 extends RecyclerView.ViewHolder {

        private ImageView img2,img_bo;
        private TextView tv_qiang;


        public ViewHolder2(View itemView) {
            super(itemView);

            img2 = itemView.findViewById(R.id.img2);//
            tv_qiang = itemView.findViewById(R.id.tv_qiang);
            img_bo=itemView.findViewById(R.id.img_bo);

        }
    }


    public class ViewHolder3 extends RecyclerView.ViewHolder {

        private ImageView img3,img_bo;
        private TextView tv_qiang;


        public ViewHolder3(View itemView) {
            super(itemView);

            img3 = itemView.findViewById(R.id.img3);//
            tv_qiang = itemView.findViewById(R.id.tv_qiang);
            img_bo=itemView.findViewById(R.id.img_bo);


        }
    }

    public static interface OnAddClickListener {

        public void onShareClick(int postion);
    }

    // add click callback
    OnAddClickListener onItemAddClick;

    public void setOnAddClickListener(OnAddClickListener onItemAddClick) {
        this.onItemAddClick = onItemAddClick;
    }
}
