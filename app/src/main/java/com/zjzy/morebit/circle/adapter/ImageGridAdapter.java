package com.zjzy.morebit.circle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.ui.ReleaseGoodsActivity;
import com.zjzy.morebit.fragment.base.BaseFeedBackFragment;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by YangBoTian on 2019/6/14.
 */

public class ImageGridAdapter extends BaseAdapter {

    private LayoutInflater inflater;
      private Context mContext;
    private List<String> mUrls;
    private int mType;
    public ImageGridAdapter(Context context, List<String> urls,int type) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        mUrls = urls;
        mType = type;
    }

    public int getCount() {
        return mUrls.size();
    }

    @Override
    public String getItem(int position) {
        return mUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_release_goods_selectpic_gridview, parent, false);
            holder.image = (RoundedImageView) convertView.findViewById(R.id.imageView);
            holder.add_ly = (RelativeLayout) convertView.findViewById(R.id.add_ly);
            holder.rlDel = (RelativeLayout) convertView.findViewById(R.id.rl_del);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String path = mUrls.get(position);
        MyLog.i("test", "path: " + path);
        if ("toAddPic".equals(path.toString())) {

            holder.image.setImageResource(R.color.transparent);
            holder.add_ly.setVisibility(View.VISIBLE);
            holder.rlDel.setVisibility(View.GONE);
        } else {
            LoadImgUtils.setImg(mContext, holder.image, path);
            holder.add_ly.setVisibility(View.GONE);
            holder.rlDel.setVisibility(View.VISIBLE);
        }
        if(mType==ReleaseGoodsActivity.SUBMIT){
            holder.rlDel.setVisibility(View.VISIBLE);
            holder.rlDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemDeleteImgClickListener != null) {
                        mOnItemDeleteImgClickListener.onImgDelete(position);
                    }
                }
            });
        }else {
            holder.rlDel.setVisibility(View.GONE);
        }


        return convertView;
    }

    class ViewHolder {
        RoundedImageView image;
        RelativeLayout add_ly;
        RelativeLayout rlDel;
    }

    private BaseFeedBackFragment.OnItemDeleteImgClickListener mOnItemDeleteImgClickListener;

    public void setOnItemClickListener(BaseFeedBackFragment.OnItemDeleteImgClickListener onItemDeleteImgClickListener) {
        this.mOnItemDeleteImgClickListener = onItemDeleteImgClickListener;
    }

}
