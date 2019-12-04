/**
 *
 */
package com.zjzy.morebit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.zjzy.morebit.Module.common.Activity.ImagePagerActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

public class ShareMoneyAdapter extends RecyclerView.Adapter<ShareMoneyAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private boolean mIsRadio;
    private List<ImageInfo> mDatas = new ArrayList<>();
    public OnRecyclerViewItemClickListener mOnItemClickListener = null;//点击

    public ShareMoneyAdapter(Context context, List<ImageInfo> datats,boolean isRadio) {
        mInflater = LayoutInflater.from(context);
        mDatas.clear();
        mDatas.addAll(datats);
        mIsRadio = isRadio;
        this.mContext = context;
    }

    public void setData(List<ImageInfo> datas) {
        if (datas == null || datas.size() == 0) {
            return;
        }

        mDatas.clear();
        mDatas.addAll(datas);

    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_share_money,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mImg = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
        viewHolder.rb_tick = (RadioButton) view.findViewById(R.id.rb_tick);
        viewHolder.rl_tick = (RelativeLayout) view.findViewById(R.id.rl_tick);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int pos) {
        if (pos < 0 || mDatas.size() < pos) {
            return;
        }
        final ImageInfo imageInfo = mDatas.get(pos);
        if (imageInfo == null) {
            return;
        }
        MyLog.i("test", "imageInfo.getPicture(): " + imageInfo.getPicture());
        if (imageInfo.getPicture().startsWith("http")) {
            LoadImgUtils.setImg(mContext, viewHolder.mImg, imageInfo.getPicture());
        } else {
            LoadImgUtils.setImgPath(mContext, viewHolder.mImg, imageInfo.getPicture());
        }
        viewHolder.rl_tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageInfo.isChecked = !imageInfo.isChecked;
                viewHolder.rb_tick.setChecked(imageInfo.isChecked);
                if (mIsRadio) {
                    for (int i = 0; i < mDatas.size(); i++) {
                        if (i != pos) {
                            ImageInfo imageInfo1 = mDatas.get(i);
                            imageInfo1.isChecked = false;
                            viewHolder.rb_tick.setChecked(false);
                        }
                    }
                }

                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, pos,imageInfo.isChecked );
                }
                notifyDataSetChanged();
            }
        });
        viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> arrayList = new ArrayList<String>();
                for (int i = 0; i < mDatas.size(); i++) {
                    ImageInfo imageInfo = mDatas.get(i);
                    if (imageInfo != null) {
                        arrayList.add(imageInfo.getPicture());
                    }
                }
                ImagePagerActivity.startActivity(mContext, arrayList, pos);

            }
        });
        viewHolder.rb_tick.setChecked(imageInfo.isChecked);


    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView mImg;
        RadioButton rb_tick;
        RelativeLayout rl_tick;
    }


    //回调------------------------------------------
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position, boolean isChecked);
    }


}