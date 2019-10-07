package com.markermall.cat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.markermall.cat.R;

import java.util.List;

/**
 * 左侧菜单ListView的适配器
 *
 * @author Administrator
 */
public class FenleiMenuAdapter extends BaseAdapter {

    private Context context;
    private int selectItem = -1;
    private List<String> list;

    public FenleiMenuAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }

    public int getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        ViewHolder holder = null;
        if (arg1 == null) {
            holder = new ViewHolder();
            arg1 = View.inflate(context, R.layout.item_fenlei_menu, null);
            holder.tv_name = (TextView) arg1.findViewById(R.id.item_name);
            holder.leftTag = (View)arg1.findViewById(R.id.leftTag);
            arg1.setTag(holder);
        } else {
            holder = (ViewHolder) arg1.getTag();
        }
        if (arg0 == selectItem) {
            holder.tv_name.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.color_2B0900));
            holder.leftTag.setVisibility(View.VISIBLE);
        } else {
            holder.tv_name.setBackgroundColor(context.getResources().getColor(R.color.color_F6F6F6));
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.color_666666));
            holder.leftTag.setVisibility(View.GONE);
        }
        holder.tv_name.setText(list.get(arg0));
        return arg1;
    }

    static class ViewHolder {
        private TextView tv_name;
        private View leftTag;
    }
}
