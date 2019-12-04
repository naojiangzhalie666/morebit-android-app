package com.zjzy.morebit.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjzy.morebit.R;

import java.util.List;

/**
 * Created by YangBoTian on 2018/7/5.
 */

public class OrderPopAdapter extends BaseAdapter{

    private List<String> list;
    private Context context;
    private int curPosition=-1;
    public OrderPopAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_pop_lv_item, null);
            viewHolder.item_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(curPosition == position){
            viewHolder.item_content.setTextColor(ContextCompat.getColor(context,R.color.color_F32F19));
        } else {
            viewHolder.item_content.setTextColor(ContextCompat.getColor(context,R.color.color_999999));
        }
        viewHolder.item_content.setText(list.get(position));
//         viewHolder.item_content.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 if(selectClickListener!=null){
//                     curPosition = position;
//                     selectClickListener.onClick(v,position,list.get(position));
//                 }
//             }
//         });
        return convertView;
    }

    private class ViewHolder {
        private TextView item_content;
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
    }

}
