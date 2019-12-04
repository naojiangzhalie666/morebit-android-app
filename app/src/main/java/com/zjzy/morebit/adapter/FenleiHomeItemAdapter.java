package com.zjzy.morebit.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.goods.Child3;
import com.zjzy.morebit.utils.LoadImgUtils;

import java.util.List;

public class FenleiHomeItemAdapter extends BaseAdapter {

    private Context context;
    private List<Child3> foodDatas;

    public FenleiHomeItemAdapter(Context context, List<Child3> foodDatas) {
        this.context = context;
        this.foodDatas = foodDatas;
    }


    @Override
    public int getCount() {
        if (foodDatas != null) {
            return foodDatas.size();
        } else {
            return 10;
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
    public int getItemViewType(int position) {
        return TextUtils.isEmpty(foodDatas.get(position).getName())?1:0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if(type==1){
            MoreViewHold moreViewHold = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_fenlei_home_category_more, null);
                moreViewHold = new MoreViewHold();
                moreViewHold.more = (TextView) convertView.findViewById(R.id.more);
                convertView.setTag(moreViewHold);
            } else {
                moreViewHold = (MoreViewHold) convertView.getTag();
            }
            moreViewHold.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            Child3 subcategory = foodDatas.get(position);
            ViewHold viewHold = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_fenlei_home_category, null);
                viewHold = new ViewHold();
                viewHold.tv_name = (TextView) convertView.findViewById(R.id.item_home_name);
                viewHold.iv_icon = (ImageView) convertView.findViewById(R.id.item_album);
                convertView.setTag(viewHold);
            } else {
                viewHold = (ViewHold) convertView.getTag();
            }
            viewHold.tv_name.setText(subcategory.getName());

            LoadImgUtils.setImg(context,viewHold.iv_icon,subcategory.getPictrue());
//            Glide.with(context)
//                    .load(subcategory.getpictrue())
//                    .placeholder(R.drawable.icon_default)
//                    .error(R.drawable.icon_default)
//                    .into(viewHold.iv_icon);
        }

        return convertView;


    }

    private static class ViewHold {
        private TextView tv_name;
        private ImageView iv_icon;
    }
    private static class MoreViewHold {
        private TextView more;
    }
}
