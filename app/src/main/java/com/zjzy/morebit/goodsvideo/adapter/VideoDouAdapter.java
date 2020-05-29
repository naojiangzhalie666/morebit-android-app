package com.zjzy.morebit.goodsvideo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.zjzy.morebit.R;

/*
*
* 抖货视频adapter
* */
public class VideoDouAdapter extends RecyclerView.Adapter<VideoDouAdapter.ViewHolder> {
    private Context context;



    public VideoDouAdapter(Context context) {
        this.context = context;


    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View inflate = LayoutInflater.from(context).inflate(R.layout.item_videodou, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView closs, tv_title, tv_price, tv_subsidy, tv_num, tv_coupon_price, tv_buy, tv_share;
        private VideoView videoView;
        private ImageView iv_head, img_stop;
        private RelativeLayout r1;
        public ViewHolder(View itemView) {
            super(itemView);
            closs = (TextView) itemView.findViewById(R.id.closs);
            videoView = (VideoView) itemView.findViewById(R.id.videoView);//视频
            iv_head = (ImageView) itemView.findViewById(R.id.iv_head);//主图
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);//标题
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);//优惠券
            tv_subsidy = (TextView) itemView.findViewById(R.id.tv_subsidy);//预估收益
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);//销量
            tv_coupon_price = (TextView) itemView.findViewById(R.id.tv_coupon_price);//商品价格
            tv_buy = (TextView) itemView.findViewById(R.id.tv_buy);//立即购买
            tv_share = (TextView) itemView.findViewById(R.id.tv_share);//分享

            img_stop = (ImageView) itemView.findViewById(R.id.img_stop);
            r1 = (RelativeLayout) itemView.findViewById(R.id.r1);
        }
    }
}
