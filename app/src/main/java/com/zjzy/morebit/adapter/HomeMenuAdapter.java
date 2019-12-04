package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description: 首页的icon
 **/
public class HomeMenuAdapter extends RecyclerView.Adapter<HomeMenuAdapter.ViewHolder> {
    private int mItmeWidth;
    private LayoutInflater mInflater;
    private List<ImageInfo> mDatas = new ArrayList<>();
    private Context mContext;
    private FragmentManager fm;
    private int screenWidth = 0;

    public HomeMenuAdapter(Context mContext, int itmeWidth) {
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        mItmeWidth = itmeWidth;
    }


    public void clearData() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void setData(List<ImageInfo> data) {

        if (data != null) {
            mDatas.clear();
            mDatas.addAll(data);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public HomeMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = null;
        HomeMenuAdapter.ViewHolder viewHolder = null;
        view = mInflater.inflate(R.layout.item_view, null, false);
        if (null != view) {
            viewHolder = new HomeMenuAdapter.ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeMenuAdapter.ViewHolder holder, int position) {
        final ImageInfo imageInfo = mDatas.get(position);
        if (imageInfo != null) {
            if (null != holder.itemMainTitle) {
                holder.itemMainTitle.setText(mDatas.get(position).getTitle());
            }
            if (null != holder.imageView) {
                if (mItmeWidth != 0) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mItmeWidth, mItmeWidth);
                    holder.imageView.setLayoutParams(layoutParams);
                    LinearLayout.LayoutParams itmeLayoutParams = new LinearLayout.LayoutParams(mItmeWidth,  LinearLayout.LayoutParams.WRAP_CONTENT );
                    holder.itemLayout.setLayoutParams(itmeLayoutParams );
                }
                LoadImgUtils.setImg(mContext, holder.imageView, imageInfo.getPicture());
            }
        }

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BannerInitiateUtils.gotoAction((Activity) mContext, imageInfo);
                BannerInitiateUtils.statisticsStartAdOnclick((BaseActivity) mContext, imageInfo.getId() + "", 1);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView itemMainTitle;
        private LinearLayout itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.im_item_icon);
            itemMainTitle = itemView.findViewById(R.id.tv_item_name);
            itemLayout = itemView.findViewById(R.id.itemLayout);
        }
    }
}
