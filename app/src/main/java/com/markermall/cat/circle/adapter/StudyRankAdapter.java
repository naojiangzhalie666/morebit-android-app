package com.markermall.cat.circle.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.markermall.cat.Activity.ShowWebActivity;
import com.markermall.cat.R;
import com.markermall.cat.adapter.SimpleAdapter;
import com.markermall.cat.adapter.holder.SimpleViewHolder;
import com.markermall.cat.circle.CollegeListActivity;
import com.markermall.cat.pojo.StudyRank;
import com.markermall.cat.utils.LoadImgUtils;

/**
 * Created by YangBoTian on 2018/12/24.
 */

public class StudyRankAdapter extends SimpleAdapter<StudyRank, SimpleViewHolder> {

    private Context mContext;
    private int itemWidth;
    public StudyRankAdapter(Context context,int itemWidth) {
        super(context);
        this.mContext = context;
        this.itemWidth = itemWidth;
    }


    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        final StudyRank item = getItem(position);
        // TextView title = holder.viewFinder().view(R.id.title);
        ImageView im_item_icon = holder.viewFinder().view(R.id.im_item_icon);
        TextView tv_item_name = holder.viewFinder().view(R.id.tv_item_name);
        LinearLayout itemLayout = holder.viewFinder().view(R.id.itemLayout);
//        CardView cardview1 = holder.viewFinder().view(R.id.cardview1);
//        CardView cardview2 = holder.viewFinder().view(R.id.cardview2);
//        ImageView img = holder.viewFinder().view(R.id.img);
//        if (!TextUtils.isEmpty(item.getImage())) {
//            LoadImgUtils.setImg(mContext, img, item.getImage());
//        }
//
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                 if(item.getIsCollegeCategory()==1){
//                     CollegeListActivity.start((Activity)mContext,item.getModelName(),item.getId(),CollegeListActivity.CIRCLE_COURSE);
//                 } else {
//                     ShowWebActivity.start((Activity) mContext,item.getUrl(), item.getModelName());
//                 }
//
//            }
//        });
//        cardview1.setVisibility(View.VISIBLE);
//        cardview2.setVisibility(View.GONE);

        ViewGroup.LayoutParams layoutParams = itemLayout.getLayoutParams();
        layoutParams.width =itemWidth;
        itemLayout.setLayoutParams(layoutParams);
        if (!TextUtils.isEmpty(item.getImage())) {
            LoadImgUtils.setImg(mContext, im_item_icon, item.getImage());
        }
        tv_item_name.setText(item.getModelName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getIsCollegeCategory()==1){
                     CollegeListActivity.start((Activity)mContext,item.getModelName(),item.getId(),CollegeListActivity.CIRCLE_COURSE);
                 } else {
                     ShowWebActivity.start((Activity) mContext,item.getUrl(), item.getModelName());
                 }
            }
        });
    }

    @Override
    protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        return layoutInflater.inflate(R.layout.item_study_rank, parent, false);
    }
}
