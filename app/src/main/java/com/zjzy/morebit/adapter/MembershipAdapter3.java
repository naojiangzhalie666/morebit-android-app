package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.DoorGodCategoryBean;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.VipBean;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * 会员权益title
 * */
public class MembershipAdapter3 extends RecyclerView.Adapter<MembershipAdapter3.ViewHolder> {
    private Context context;
    private List<VipBean.VoBean> list = new ArrayList<>();


    public MembershipAdapter3(Context context) {
        this.context = context;

    }

    public void setData(List<VipBean.VoBean> data) {
        Log.e("sssss","捡来的7"+data);
        if (data != null) {
            list.clear();
            list.addAll(data);
            Log.e("sssss","捡来的6");
            notifyDataSetChanged();
        }
    }


    public void addData(List<VipBean.VoBean> data) {
        if (data != null) {
            list.addAll(data);
            notifyItemRangeChanged(0, data.size());
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.itme_membership3, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VipBean.VoBean info = list.get(position);
        final ImageInfo imageInfo = new ImageInfo();

        imageInfo.setId(info.getId());
        imageInfo.setMediaType(info.getMediaType());
        imageInfo.setClassId(info.getClassId());
        imageInfo.setTitle(info.getTitle());
        imageInfo.setUrl(info.getUrl());
        imageInfo.setOpen(info.getOpen());
        imageInfo.setSort(info.getSort());
        imageInfo.setSubTitle(info.getSubTitle());
        if (!TextUtils.isEmpty(info.getPicture())){
            LoadImgUtils.setImgCircle(context,holder.img,info.getPicture());
        }
        if (!TextUtils.isEmpty(info.getTitle())){
            holder.title.setText(info.getTitle());
            holder.title.getPaint().setFakeBoldText(true);
        }

        if (!TextUtils.isEmpty(info.getSubTitle())){
            holder.content.setText(info.getSubTitle());
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BannerInitiateUtils.gotoAction((Activity) context, imageInfo);
                BannerInitiateUtils.statisticsStartAdOnclick((BaseActivity) context, imageInfo.getId() + "", 1);
            }
        });



    }


    @Override
    public int getItemCount() {

        return list == null ? 0 : list.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView title,content;
        private ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);//标题
            img=itemView.findViewById(R.id.img);//icon
            content=itemView.findViewById(R.id.content);

        }
    }

    public static interface OnAddClickListener {

        public void onShareClick();
    }

    // add click callback
    OnAddClickListener onItemAddClick;

    public void setOnAddClickListener(OnAddClickListener onItemAddClick) {
        this.onItemAddClick = onItemAddClick;
    }
}
