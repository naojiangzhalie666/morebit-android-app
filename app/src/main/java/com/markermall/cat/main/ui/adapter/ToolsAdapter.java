package com.markermall.cat.main.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.markermall.cat.R;
import com.markermall.cat.adapter.SimpleAdapter;
import com.markermall.cat.adapter.holder.SimpleViewHolder;
import com.markermall.cat.pojo.ImageInfo;
import com.markermall.cat.utils.LoadImgUtils;
import com.markermall.cat.utils.UI.BannerInitiateUtils;

/**
 * Created by YangBoTian on 2018/9/14.
 */

public class ToolsAdapter extends SimpleAdapter<ImageInfo, SimpleViewHolder> {

    Context mContext;
    public ToolsAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        final ImageInfo item = getItem(position);
        ImageView icon = holder.viewFinder().view(R.id.icon);
        TextView title = holder.viewFinder().view(R.id.title);
        if(!TextUtils.isEmpty(item.getPicture())){
            LoadImgUtils.setImg(mContext,icon,item.getPicture());
        }
        title.setText(item.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BannerInitiateUtils.gotoAction((Activity) mContext, item);
            }
        });

    }

    @Override
    protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        return layoutInflater.inflate(R.layout.item_tools, parent, false);
    }


}
