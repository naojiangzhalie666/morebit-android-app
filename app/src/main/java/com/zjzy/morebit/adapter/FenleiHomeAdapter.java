package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.main.ui.fragment.GoodNewsFramgent;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.goods.Child2;
import com.zjzy.morebit.pojo.goods.Child3;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.view.FixGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * 右侧主界面ListView的适配器
 */
public class FenleiHomeAdapter extends BaseAdapter {

    private Context context;
    private List<Child2> mFoodDatas = new ArrayList<>();
    private String superName = "";

    public void notifyData(List<Child2> foodDatas, String name) {
        mFoodDatas.clear();
        if (foodDatas != null) {
            mFoodDatas.addAll(foodDatas);
        }
        this.superName = name;
    }

    public FenleiHomeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (mFoodDatas != null) {
            return mFoodDatas.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mFoodDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_fenlei_home, null);
            viewHold = new ViewHold();
            viewHold.gridView = (FixGridView) convertView.findViewById(R.id.gridView);
            viewHold.ll_blank =  convertView.findViewById(R.id.ll_blank);
            viewHold.blank = (TextView) convertView.findViewById(R.id.blank);
            viewHold.more = (TextView) convertView.findViewById(R.id.more);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        final Child2 dataBean = mFoodDatas.get(position);
        if (dataBean == null) return convertView;
        List<Child3> getDataInfos = new ArrayList<>();
        try {
            List<Child3> getDatas = dataBean.getChild3();
            getDataInfos.clear();
            getDataInfos.addAll(getDatas);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FenleiHomeItemAdapter adapter = new FenleiHomeItemAdapter(context, getDataInfos);
        if (TextUtils.isEmpty(dataBean.getName())) {
            viewHold.ll_blank.setVisibility(View.GONE);
        }else{
            viewHold.ll_blank.setVisibility(View.VISIBLE);
            viewHold.blank.setText(dataBean.getName());

        }
        viewHold.gridView.setAdapter(adapter);
        final List<Child3> fDataInfos = getDataInfos;
        viewHold.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (fDataInfos.size() < i) return;
                Child3 child3 = fDataInfos.get(i);
                if (child3 == null) return;
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.categoryId = child3.getId() + "";
                imageInfo.setTitle(fDataInfos.get(i).getName());
                imageInfo.categoryId = fDataInfos.get(i).getName() + " " +dataBean.getName()+" "+ superName;
                MyLog.i("test","categoryId: " +imageInfo.categoryId);
                GoodNewsFramgent.start((Activity) context, imageInfo, C.GoodsListType.THREEGOODS);


            }
        });
        return convertView;
    }

    private static class ViewHold {
        private FixGridView gridView;
        private LinearLayout ll_blank;
        private TextView blank;
        private TextView more;
    }

}
