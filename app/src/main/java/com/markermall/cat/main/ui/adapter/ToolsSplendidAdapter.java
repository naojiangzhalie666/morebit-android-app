package com.markermall.cat.main.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.markermall.cat.Activity.ShowWebActivity;
import com.markermall.cat.R;
import com.markermall.cat.adapter.SimpleAdapter;
import com.markermall.cat.adapter.holder.SimpleViewHolder;
import com.markermall.cat.pojo.ProtocolRuleBean;
import com.markermall.cat.utils.LoadImgUtils;

/**
 * Created by YangBoTian on 2018/9/14.
 */

public class ToolsSplendidAdapter extends SimpleAdapter<ProtocolRuleBean, SimpleViewHolder> {

    Context mContext;
    public ToolsSplendidAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        final ProtocolRuleBean item = getItem(position);
        ImageView icon = holder.viewFinder().view(R.id.icon);
        TextView title = holder.viewFinder().view(R.id.title);
        if(!TextUtils.isEmpty(item.getIcon())){
            LoadImgUtils.setImg(mContext,icon,item.getIcon());
        }
        title.setText(item.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowWebActivity.start((Activity) mContext, item.getHtmlUrl(), item.getTitle());
            }
        });

    }

    @Override
    protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        return layoutInflater.inflate(R.layout.item_tools, parent, false);
    }


}
