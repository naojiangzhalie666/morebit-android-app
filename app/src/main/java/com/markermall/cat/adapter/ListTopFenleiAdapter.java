package com.markermall.cat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.markermall.cat.R;
import com.markermall.cat.pojo.goods.Child3;
import com.markermall.cat.utils.LoadImgUtils;
import com.markermall.cat.utils.action.MyAction;

import java.util.List;

public class ListTopFenleiAdapter extends BaseAdapter {

    private Context context;
    private List<Child3> foodDatas;
private MyAction.OnResult<Integer> action;
    public ListTopFenleiAdapter(Context context, List<Child3> foodDatas,MyAction.OnResult<Integer> action) {
        this.context = context;
        this.foodDatas = foodDatas;
        this.action = action;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Child3 subcategory = foodDatas.get(position);
        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_listtop_fenlei, null);
            viewHold = new ViewHold();
            viewHold.tv_name = (TextView) convertView.findViewById(R.id.item_home_name);
            viewHold.iv_icon = (ImageView) convertView.findViewById(R.id.item_album);
            convertView.setTag(viewHold);
            viewHold.iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("title", "");
//                    bundle.putString("fragmentName", "CategoryListFragment");
//                    bundle.putString("TypeName", "二级分类");
//                    bundle.putString("keywords", foodDatas.get(position).getName());
//                    bundle.putString("cidName", foodDatas.get(position).getName());
//                    bundle.putString(CategoryListFragment.CATEGORY_ID, foodDatas.get(position).getParentId() + "");
//                    PageToUtil.goToSimpleFragment(context, bundle);
                    action.invoke(position);

//                    ImageInfo imageInfo = new ImageInfo();
//                    imageInfo.setTitle(foodDatas.get(position).getName());
//                    imageInfo.categoryId = foodDatas.get(position).getName()+ "";
//                    GoodNewsFramgent.start((Activity) context, imageInfo, C.GoodsListType.THREEGOODS);
                }
            });
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        viewHold.tv_name.setText(subcategory.getName());

        LoadImgUtils.setImg(context,viewHold.iv_icon,subcategory.getPictrue());
//        Glide.with(context)
//                .load(subcategory.getpictrue())
//                .placeholder(R.drawable.icon_default)
//                .error(R.drawable.icon_default)
//                .into(viewHold.iv_icon);
        return convertView;


    }

    private static class ViewHold {
        private TextView tv_name;
        private ImageView iv_icon;
    }

}
