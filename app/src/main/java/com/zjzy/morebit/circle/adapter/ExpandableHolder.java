package com.zjzy.morebit.circle.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.R;

import drawthink.expandablerecyclerview.holder.BaseViewHolder;

/**
 * Created by YangBoTian on 2019/6/13.
 */

public class ExpandableHolder extends BaseViewHolder {

    public ImageView iv_arrow;
    public ImageView iv_select;
    public TextView tv_one_title;
    public TextView tv_two_title;

    public ExpandableHolder(Context ctx, View itemView, int viewType) {
        super(ctx,itemView, viewType);
        tv_one_title = (TextView) itemView.findViewById(R.id.tv_one_title);
        tv_two_title = (TextView) itemView.findViewById(R.id.tv_two_title);
        iv_select = (ImageView) itemView.findViewById(R.id.iv_select);
        iv_arrow = (ImageView) itemView.findViewById(R.id.iv_arrow);
    }

    @Override
    public int getGroupViewResId() {
        return R.id.group;
    }

    @Override
    public int getChildViewResId() {
        return R.id.child;
    }
}
