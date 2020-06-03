package com.zjzy.morebit.goodsvideo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goodsvideo.VideoActivity;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.goods.VideoBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;

import java.util.ArrayList;
import java.util.List;

public class VideoGoodsAdapter extends RecyclerView.Adapter<VideoGoodsAdapter.ViewHolder> {
    private Context context;
    private List<ShopGoodInfo> list;
    public VideoGoodsAdapter(Context context,List<ShopGoodInfo> list) {
        this.context = context;
        this.list = list;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = View.inflate(context, R.layout.itme_video_goods, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }
    //加载数据
    public void loadMore(List<ShopGoodInfo> strings) {
        list.addAll(strings);
        notifyItemRangeChanged(0,strings.size());
    }

//    //刷新数据
//    public void refreshData(List<ShopGoodInfo> strings) {
//        list.addAll(0, strings);
//        notifyItemRangeChanged(0,strings.size());
//    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
//        ShopGoodInfo shopGoodInfo=new ShopGoodInfo();
//        shopGoodInfo.setItemSourceId(list.get(position).getItemId());
        final ShopGoodInfo videoBean = list.get(position);
        videoBean.setItemSourceId(list.get(position).getItemId());
        videoBean.setItemVoucherPrice(list.get(position).getItemPrice());
        ImageInfo imglist=new ImageInfo();
        imglist.setThumb(list.get(position).getItemPic());
        List<ImageInfo> mlist=new ArrayList<>();
        mlist.add(imglist);
        videoBean.setAdImgUrl(mlist);
       // holder.tv_title.setText(list.get(position).getItemTitle());
        StringsUtils.retractTitle( holder.img, holder.tv_title,list.get(position).getItemTitle());
        //LoadImgUtils.setImg(context, holder.iv_head, list.get(position).getItemPic());

        if(!list.get(position).getItemPic().equals(holder.iv_head.getTag())){//解决图片加载不闪烁的问题,可以在加载时候，对于已经加载过的item,  采用比对tag方式判断是否需要重新计算高度
            holder.iv_head.setTag(null);//需要清空tag，否则报错
            LoadImgUtils.loadingCornerTop(context, holder.iv_head, list.get(position).getItemPic(), 5);
        }
        holder.tv_num.setText(MathUtils.getSales(videoBean.getDyLikeCount()));

        holder.commission.setText(list.get(position).getCouponMoney() + "元劵");
        holder.tv_price.setText("" + list.get(position).getItemPrice());
        UserInfo userInfo1 =UserLocalData.getUser();
        if (userInfo1 == null || TextUtils.isEmpty(UserLocalData.getToken())) {
            holder.tv_coupul.setText("登录赚佣金");
        }else{
            if (C.UserType.operator.equals(UserLocalData.getUser(context).getPartner())
                    || C.UserType.vipMember.equals(UserLocalData.getUser(context).getPartner())) {
//            commission.setVisibility(View.VISIBLE);
                holder.tv_coupul.setText("预估收益"+ MathUtils.getMuRatioComPrice(UserLocalData.getUser(context).getCalculationRate(), list.get(position).getTkMoney()+"")+"元");
            }
        }

//            commission.setText(getString(R.string.upgrade_commission));
//            commission.setVisibility(View.GONE);

        //  holder.tv_coupul.setText("预估收益"+list.get(position).getTkMoney()+"元");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoActivity.start(context, (List<ShopGoodInfo>) list,position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_head,img;
        private TextView tv_title, commission, tv_price, tv_coupul,tv_num;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_head = itemView.findViewById(R.id.iv_head);//主图
            tv_title = itemView.findViewById(R.id.tv_title);//标题
            commission = itemView.findViewById(R.id.commission);//优惠券
            tv_price = itemView.findViewById(R.id.tv_price);//价格
            tv_coupul = itemView.findViewById(R.id.tv_coupul);//预估收益
            img=itemView.findViewById(R.id.img);
            tv_num=itemView.findViewById(R.id.tv_num);

        }
    }

}
