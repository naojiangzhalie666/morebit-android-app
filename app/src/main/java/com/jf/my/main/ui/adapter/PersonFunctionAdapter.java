package com.jf.my.main.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jf.my.R;
import com.jf.my.adapter.SimpleAdapter;
import com.jf.my.adapter.holder.SimpleViewHolder;
import com.jf.my.pojo.ImageInfo;
import com.jf.my.utils.LoadImgUtils;
import com.jf.my.utils.UI.BannerInitiateUtils;

/**
 * Created by YangBoTian on 2018/9/14.
 */

public class PersonFunctionAdapter extends SimpleAdapter<ImageInfo, SimpleViewHolder> {

    Context mContext;
    public PersonFunctionAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        final ImageInfo item = getItem(position);
        ImageView icon = holder.viewFinder().view(R.id.icon);
        ImageView iv_mark = holder.viewFinder().view(R.id.iv_mark);
        TextView title = holder.viewFinder().view(R.id.tv_title);
        TextView subTitle = holder.viewFinder().view(R.id.tv_sub_title);
        if(!TextUtils.isEmpty(item.getPicture())){
            LoadImgUtils.setImg(mContext,icon,item.getPicture());
        }
        title.setText(item.getTitle());
        subTitle.setText(item.getSubTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BannerInitiateUtils.gotoAction((Activity) mContext, item);
            }
        });
       if (item.getMark()==1){
           iv_mark.setVisibility(View.VISIBLE);
       } else {
           iv_mark.setVisibility(View.GONE);
       }
    }

    @Override
    protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        return layoutInflater.inflate(R.layout.mine_recycler_item, parent, false);
    }


}
