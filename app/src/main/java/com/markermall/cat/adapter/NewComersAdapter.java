package com.markermall.cat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.markermall.cat.R;
import com.markermall.cat.pojo.ProtocolRuleBean;

import java.util.List;

/**
 * 新人课堂
 */
public class NewComersAdapter extends BaseAdapter {

    private Context context;
    private int selectItem = 0;
    private List<ProtocolRuleBean> list;

    public NewComersAdapter(Context context, List<ProtocolRuleBean> list) {
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
            arg1 = View.inflate(context, R.layout.item_newcomers, null);
            holder.text = (TextView) arg1.findViewById(R.id.text);
            arg1.setTag(holder);
        } else {
            holder = (ViewHolder) arg1.getTag();
        }
        holder.text.setText(list.get(arg0).getTitle());
        return arg1;
    }

    static class ViewHolder {
        private TextView text;
    }
}
