
package com.markermall.cat.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.markermall.cat.R;

import java.util.ArrayList;
import java.util.List;


public class SearchHotAdapter extends BaseAdapter {

    private Context context;
    private List<String> foodDatas= new ArrayList<>();

    public SearchHotAdapter(Context context, List<String> foodDatas) {
        this.context = context;
        this.foodDatas.clear();
        this.foodDatas.addAll(foodDatas);
    }

    @Override
    public int getCount() {
        if (foodDatas != null) {
            return foodDatas.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return foodDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchHotAdapter.ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.search_hot_key_itme, null);
            viewHold = new SearchHotAdapter.ViewHold();
            viewHold.name = (TextView) convertView.findViewById(R.id.name);
            viewHold.name.setMaxLines(2);
            viewHold.name.setEllipsize(TextUtils.TruncateAt.END);
            convertView.setTag(viewHold);
        } else {
            viewHold = (SearchHotAdapter.ViewHold) convertView.getTag();
        }
        if (!TextUtils.isEmpty(foodDatas.get(position)))
        viewHold.name.setText(Html.fromHtml(foodDatas.get(position)));
        return convertView;
    }

    public void setData(ArrayList<String> data) {
        foodDatas.clear();
        if (data != null) {
           // Collections.reverse(data);
            foodDatas.addAll(data);
        }
    }


    private static class ViewHold {
        private TextView name;
    }


}
