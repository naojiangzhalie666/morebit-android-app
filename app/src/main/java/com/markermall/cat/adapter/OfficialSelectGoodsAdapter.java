/**
 * 
 */
package com.markermall.cat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.markermall.cat.R;
import com.markermall.cat.pojo.ShopGoodInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class OfficialSelectGoodsAdapter extends RecyclerView.Adapter<OfficialSelectGoodsAdapter.ViewHolder> {

	private LayoutInflater mInflater;
	private List<ShopGoodInfo> mDatas = new ArrayList<>();
	private Context mContext;
	public OnRecyclerViewItemClickListener mOnItemClickListener = null;//点击
	private DisplayImageOptions options;

	public OfficialSelectGoodsAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		this.mContext = context;
		//设置图片视图图片资源
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_default)
				.showImageForEmptyUri(R.drawable.icon_default)
				.showImageOnFail(R.drawable.icon_default)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	public void setData(List<ShopGoodInfo> datas){
		mDatas.clear();
		 if(datas==null || datas.size()==0){
			 return;
		 }
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
		View view = mInflater.inflate(R.layout.item_official_selectgood,
				viewGroup, false);
		ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.mImg = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
		viewHolder.del = (TextView) view.findViewById(R.id.del);
		return viewHolder;
	}

	/**
	 * 设置值
	 */
	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
		ShopGoodInfo info = mDatas.get(i);
        if(info.getPicture().startsWith("http")) {
            ImageLoader.getInstance().displayImage(info.getPicture(), viewHolder.mImg, options);
        }else{
            ImageLoader.getInstance().displayImage( info.getPicture(), viewHolder.mImg, options);
        }
		viewHolder.del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnItemClickListener != null) {
					mOnItemClickListener.onItemClick(v, i);
				}

			}
		});
//		if(i == (mDatas.size()-1)){
//			viewHolder.right_boxnull.setVisibility(View.GONE);
//		}else{
//			viewHolder.right_boxnull.setVisibility(View.VISIBLE);
//		}

	}


	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View arg0) {
			super(arg0);
		}
		ImageView mImg;
		TextView del;
	}

    //回调------------------------------------------
	public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
		this.mOnItemClickListener = listener;
	}
	public interface OnRecyclerViewItemClickListener {
		void onItemClick(View view, int position);
	}



}