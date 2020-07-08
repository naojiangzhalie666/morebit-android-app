package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.PanicBuyFragment;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.PanicBuyingListBean;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description: 二级模块
 **/
public class LimitAdapter extends RecyclerView.Adapter<LimitAdapter.ViewHolder>{
    public static final int DISPLAY_HORIZONTAL = 0; //横向
    private LayoutInflater mInflater;
    private List<ImageInfo> mDatas = new ArrayList<>();
    private Context mContext;
    private List<PanicBuyingListBean.TimeListBean.ItemListBean> itemList;

    public LimitAdapter(Context context,List<PanicBuyingListBean.TimeListBean.ItemListBean> itemList) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.itemList=itemList;
    }



    @Override
    public LimitAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;

             view = mInflater.inflate(R.layout.item_litmitskillgoods, viewGroup, false);


        LimitAdapter.ViewHolder viewHolder = new LimitAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LimitAdapter.ViewHolder holder, final int position) {
        final ImageInfo mImageInfo = new ImageInfo();
        mImageInfo.setClassId(4);
        mImageInfo.setId(363);
        mImageInfo.setSplicePid("0");
        mImageInfo.setType("4");
        mImageInfo.setTitle("限时抢购");
        LoadImgUtils.loadingCornerBitmap(mContext, holder.img, itemList.get(position).getItemPicture(), 4);
        if (!TextUtils.isEmpty(itemList.get(position).getItemTitle())){
            holder.tv_title.setText(itemList.get(position).getItemTitle()+"");
        }


        if (!TextUtils.isEmpty(itemList.get(position).getItemVoucherPrice())){
            holder.tv_price.setText(MathUtils.getnum(itemList.get(position).getItemVoucherPrice())+"");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PanicBuyFragment.start((Activity) mContext, mImageInfo);//跳限时秒杀

            }
        });

    }

    @Override
    public int getItemCount() {
        if (itemList.size()>2){
            return 3;
        }else{
           return itemList.size();
        }


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tv_title,tv_price;
        public ViewHolder(View itemView) {
            super(itemView);
            img= itemView.findViewById(R.id.img);
            tv_title= itemView.findViewById(R.id.tv_title);
            tv_price= itemView.findViewById(R.id.tv_price);
        }
    }
}
