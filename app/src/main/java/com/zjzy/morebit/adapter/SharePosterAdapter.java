
package com.zjzy.morebit.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjzy.morebit.Module.common.Activity.ImagePagerActivity;
import com.zjzy.morebit.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SharePosterAdapter extends RecyclerView.Adapter<SharePosterAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<String> mDatas;
    private Context mContext;
    private HashMap<Integer, Boolean> isSelectedMap;
    public OnRecyclerViewItemClickListener mOnItemClickListener = null;//点击
    private DisplayImageOptions options;

    public SharePosterAdapter(Context context, List<String> datats) {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
        this.mContext = context;
        isSelectedMap = new HashMap<Integer, Boolean>();
        if (datats != null && datats.size() > 0) {
            for (int i = 0; i < datats.size(); i++) {
                setItemisSelectedMap(i, false);
            }
        }
        //设置图片视图图片资源
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.color.transparent)
                .showImageForEmptyUri(R.color.transparent)
                .showImageOnFail(R.color.transparent)
                .cacheInMemory(false).cacheOnDisc(false)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
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
        View view = mInflater.inflate(R.layout.item_poster_money,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mImg = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
        viewHolder.rb_tick = (RadioButton) view.findViewById(R.id.rb_tick);
       // viewHolder.right_boxnull = (TextView) view.findViewById(R.id.right_boxnull);
        viewHolder.rb_tick_rl = (RelativeLayout) view.findViewById(R.id.rb_tick_rl);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
//		//设置图片大小为根据屏幕宽度2分一的宽度 9:16的比例
//		int phoneWidth = DensityUtil.getPhoneWidth(mContext);
//		int paddingWidth = DensityUtil.dip2px(mContext,0);
//		int picWidth = (phoneWidth - paddingWidth) /2 ;
//		ViewGroup.LayoutParams picParams = viewHolder.mImg.getLayoutParams();
//		picParams.width = picWidth;
//		picParams.height = picWidth*16/10;
//		viewHolder.mImg.setLayoutParams(picParams);
//        String path = mDatas.get(i);
//        Glide.with(mContext).load(path).into(viewHolder.mImg);
       ImageLoader.getInstance().displayImage( "file:///"+mDatas.get(i), viewHolder.mImg, options);
        viewHolder.rb_tick_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, i);
                }
                //点击状态1
//				viewHolder.rb_tick.setChecked(getisSelectedAt(i));
            }
        });
        viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到图片查看
                Intent intent = new Intent(mContext, ImagePagerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) mDatas);
                bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, i);
                bundle.putString(ImagePagerActivity.TAOBAO_ID, "get_gther_poster");
                intent.putExtras(bundle);
                mContext.startActivity(intent);
//				Intent intent = new Intent(mContext, OneImageShowActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putString("title",(i+1) + "/"+mDatas.size());
//				bundle.putString("url",mDatas.get(i));
//				bundle.putInt("position",i);
//				bundle.putBoolean("isDel",false);
//				intent.putExtras(bundle);
//				mContext.startActivity(intent);
            }
        });
//        if (i == (mDatas.size() - 1)) {
//            viewHolder.right_boxnull.setVisibility(View.GONE);
//        } else {
//            viewHolder.right_boxnull.setVisibility(View.VISIBLE);
//    }
        if (getisSelectedAt(i)) {
            viewHolder.rb_tick.setChecked(true);
        } else {
            viewHolder.rb_tick.setChecked(false);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView mImg;
        RadioButton rb_tick;
        TextView right_boxnull;
        RelativeLayout rb_tick_rl;
    }

    //选中状态的切换------------------------------------------
    public boolean getisSelectedAt(int position) {
        //如果当前位置的key值为空，则表示该item未被选择过，返回false，否则返回true
        if (isSelectedMap.get(position) != null) {
            return isSelectedMap.get(position);
        }
        return false;
    }

    public void setItemisSelectedMap(int position, boolean isSelectedMap) {
        this.isSelectedMap.put(position, isSelectedMap);
        notifyDataSetChanged();
    }


    //回调------------------------------------------
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }


}