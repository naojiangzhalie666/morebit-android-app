package com.zjzy.morebit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.utils.LoadImgUtils;

import java.util.ArrayList;
import java.util.List;

public class GoodDeImgAdapter extends RecyclerView.Adapter<GoodDeImgAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<String> mDatas = new ArrayList<>();
    private Context mContext;

    public GoodDeImgAdapter(Context context, List<String> datats) {
        mInflater = LayoutInflater.from(context);
        mDatas.addAll(datats);
        this.mContext = context;
    }


    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_goodsde_img,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mImg = (ImageView) view.findViewById(R.id.img);
        viewHolder.tv_text = (TextView) view.findViewById(R.id.tv_text);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        if (mDatas == null || mDatas.size() == 0) {
            return;
        }
        String s = mDatas.get(i);
        if (LoadImgUtils.isPicture(s)) {
            LoadImgUtils.setImg(mContext, viewHolder.mImg, s, R.drawable.icon_default_750);
            viewHolder.mImg.setVisibility(View.VISIBLE);
            viewHolder.tv_text.setVisibility(View.GONE);
        } else if (s.contains("<txt>")) {
            viewHolder.mImg.setVisibility(View.GONE);
            viewHolder.tv_text.setVisibility(View.VISIBLE);
            try {
                viewHolder.tv_text.setText(Html.fromHtml(s));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            viewHolder.mImg.setVisibility(View.GONE);
            viewHolder.tv_text.setVisibility(View.GONE);
        }
    }

    public void setData(List<String> datas) {
        if (datas != null) {
            mDatas.clear();
            mDatas.addAll(datas);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView mImg;
        TextView tv_text;
    }


}