package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.zjzy.morebit.Activity.NumberGoodsDetailsActivity;
import com.zjzy.morebit.Activity.SkillClassActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.StudyRank;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * 进阶课堂标题
 * */
public class StudyTitleAdapter extends RecyclerView.Adapter<StudyTitleAdapter.ViewHolder> {
    private Context mContext;
    private List<StudyRank> list=new ArrayList<>();


    public StudyTitleAdapter(Context context) {
        this.mContext = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_study_title, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final StudyRank goods = list.get(position);

        String img = goods.getImage();
        if (!TextUtils.isEmpty(img)) {
            LoadImgUtils.loadingCornerTop(mContext, holder.title_img, img,4);

        }

        holder.title.setText(goods.getModelName()+"");
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SkillClassActivity.class);
                intent.putExtra(C.Vip.SKILLNAME,goods.getModelName());
                intent.putExtra(C.Vip.SKILLID,goods.getId());
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {

        return list.size();


    }

    public void setData(List<StudyRank> data) {
        if (data != null) {
            list.addAll(data);
            notifyItemRangeChanged(0,data.size());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView title_img;
        private TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title_img = itemView.findViewById(R.id.title_img);
            title =  itemView.findViewById(R.id.title);

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
