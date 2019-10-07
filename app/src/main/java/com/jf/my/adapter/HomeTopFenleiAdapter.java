package com.jf.my.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jf.my.R;
import com.jf.my.pojo.goods.GoodCategoryInfo;
import com.jf.my.utils.LoadImgUtils;
import com.jf.my.utils.MyLog;
import com.jf.my.utils.action.MyAction;

import java.util.List;

public class HomeTopFenleiAdapter extends BaseAdapter {

    private final MyAction.One<Integer> action;
    private Context context;
    private List<GoodCategoryInfo> foodDatas;

    public HomeTopFenleiAdapter(Context context, List<GoodCategoryInfo> foodDatas, MyAction.One<Integer> action) {
        this.context = context;
        this.foodDatas = foodDatas;
        this.action = action;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        GoodCategoryInfo subcategory = foodDatas.get(position);
        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_listtop_fenlei, null);
            viewHold = new ViewHold();
            viewHold.tv_name = (TextView) convertView.findViewById(R.id.item_home_name);
            viewHold.iv_icon = (ImageView) convertView.findViewById(R.id.item_album);
            viewHold.iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (action != null) {
                        action.invoke(position);
                    }
                }
            });
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        try {
            if(context.getString(R.string.choiceness).equals(subcategory.getName())){
                viewHold.iv_icon.setImageResource(R.drawable.icon_jingxuan);
            } else {
                LoadImgUtils.setImg(context,viewHold.iv_icon,subcategory.getpictrue());
            }

//            Glide.with(context)
//                    .load(subcategory.getpictrue())
//                    .placeholder(R.drawable.icon_default)
//                    .error(R.drawable.icon_default)
//                    .into(viewHold.iv_icon);
            viewHold.tv_name.setText(subcategory.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private static class ViewHold {
        private TextView tv_name;
        private ImageView iv_icon;
    }

}
