package com.zjzy.morebit.circle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.CategoryListChildDtos;
import com.zjzy.morebit.pojo.CategoryListDtos;


import java.util.List;

import drawthink.expandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import drawthink.expandablerecyclerview.bean.RecyclerViewData;

/**
 * Created by YangBoTian on 2019/6/13.
 */

public class ExtendableListViewAdapter extends BaseRecyclerViewAdapter<CategoryListDtos, CategoryListChildDtos,ExpandableHolder> {

    private Context ctx;
    private LayoutInflater mInflater;

    public ExtendableListViewAdapter(Context ctx, List<RecyclerViewData> datas) {
        super(ctx, datas);
        mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
    }

    /**
     * head View数据设置
     * @param holder
     * @param groupPos
     * @param position
     * @param groupData
     */
    @Override
    public void onBindGroupHolder(ExpandableHolder holder, int groupPos,int position,CategoryListDtos groupData) {
        holder.tv_one_title.setText(groupData.getTitle());
        if(groupData.getChild()!=null&&groupData.getChild().size()>0){
            if (!groupData.isExpanded()) {
                holder.iv_arrow.setImageResource(R.drawable.icon_xiangxia);
            } else {
                holder.iv_arrow.setImageResource(R.drawable.icon_xiangshang);
            }
        } else {
            if (groupData.isExpanded()) {
                holder.iv_arrow.setImageResource(R.drawable.icon_release_goods_xuanzhong);
            } else {
                holder.iv_arrow.setImageResource(R.drawable.icon_release_goods_weixuanzhong);
            }
        }

    }

    /**
     * child View数据设置
     * @param holder
     * @param groupPos
     * @param childPos
     * @param position
     * @param childData
     */
    @Override
    public void onBindChildpHolder(ExpandableHolder holder, int groupPos,int childPos,int position, CategoryListChildDtos childData) {
        holder.tv_two_title.setText(childData.getTitle());
           if(childData.isSelect()){
               holder.iv_select.setImageResource(R.drawable.icon_release_goods_xuanzhong);
           } else {
               holder.iv_select.setImageResource(R.drawable.icon_release_goods_weixuanzhong);
           }
    }

    @Override
    public View getGroupView(ViewGroup parent) {
        return mInflater.inflate(R.layout.item_one_subject,parent,false);
    }

    @Override
    public View getChildView(ViewGroup parent) {
        return mInflater.inflate(R.layout.item_two_subject,parent,false);
    }

    @Override
    public ExpandableHolder createRealViewHolder(Context ctx, View view, int viewType) {
        return new ExpandableHolder(ctx,view,viewType);
    }

    /**
     * true 全部可展开
     * fasle  同一时间只能展开一个
     * */
    @Override
    public boolean canExpandAll() {
        return true;
    }

}
