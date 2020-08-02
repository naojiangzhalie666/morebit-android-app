package com.zjzy.morebit.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Module.common.Activity.ImagePagerActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.MarkermallCircleInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
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


    public PhotoDialyAdapter(Context context, List<String> picture, MarkermallCircleInfo circleInfo) {
        this.context = context;
        this.list = picture;
        this.circleInfo = circleInfo;
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
        if (viewType == 1) {
            ViewHolder2 holder2 = (ViewHolder2) holder;
            LoadImgUtils.setImg(context, holder2.img2, list.get(position));
            clickPhoto(position, holder);
            getPhoto(holder2);
        } else if (viewType == 2) {
            ViewHolder3 holder3 = (ViewHolder3) holder;
            LoadImgUtils.setImg(context, holder3.img3, list.get(position));
            clickPhoto(position, holder);
            getPhoto(holder3);
        } else {
            ViewHolder holder1 = (ViewHolder) holder;
            LoadImgUtils.setImg(context, holder1.img, list.get(position));
            clickPhoto(position, holder);
            getPhoto(holder1);
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


    private void getPhoto( ViewHolder3 holder3) {
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


    private void clickPhoto(final int position, RecyclerView.ViewHolder holder) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {//查看大图
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImagePagerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) list);
                bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {


        return list == null ? 0 : list.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView tv_qiang;


        public ViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);//
            tv_qiang = itemView.findViewById(R.id.tv_qiang);


        }
    }


    public class ViewHolder2 extends RecyclerView.ViewHolder {

        private ImageView img2;
        private TextView tv_qiang;


        public ViewHolder2(View itemView) {
            super(itemView);

            img2 = itemView.findViewById(R.id.img2);//
            tv_qiang = itemView.findViewById(R.id.tv_qiang);


        }
    }


    public class ViewHolder3 extends RecyclerView.ViewHolder {

        private ImageView img3;
        private TextView tv_qiang;


        public ViewHolder3(View itemView) {
            super(itemView);

            img3 = itemView.findViewById(R.id.img3);//
            tv_qiang = itemView.findViewById(R.id.tv_qiang);


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
