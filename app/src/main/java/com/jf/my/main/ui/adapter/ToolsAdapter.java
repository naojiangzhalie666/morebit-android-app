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
