package com.zjzy.morebit.goodsvideo.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.tencent.mm.opensdk.utils.Log;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goodsvideo.VideoActivity;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.goods.VideoBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.VerticalImageSpan;

import java.util.ArrayList;
import java.util.List;

public class VideoGoodsAdapter extends RecyclerView.Adapter<VideoGoodsAdapter.ViewHolder> {
    private Context context;
    private List<ShopGoodInfo> list;
    private String cid;
    private  int page;

    public VideoGoodsAdapter(Context context, List<ShopGoodInfo> list, String cid, int page) {
        this.context = context;
        this.list = list;
        this.cid=cid;
        this.page=page;
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


        SpannableString spannableString = new SpannableString("  " + videoBean.getItemTitle());
        Drawable drawable = context.getResources().getDrawable(R.drawable.tb_list_icon);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        spannableString.setSpan(new VerticalImageSpan(drawable), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_title.setText(spannableString);


        if(!list.get(position).getItemPic().equals(holder.iv_head.getTag())){//解决图片加载不闪烁的问题,可以在加载时候，对于已经加载过的item,  采用比对tag方式判断是否需要重新计算高度
            holder.iv_head.setTag(null);//需要清空tag，否则报错
            LoadImgUtils.loadingCornerBitmap(context, holder.iv_head, list.get(position).getItemPic(), 5);
        }
        holder.tv_num.setText(MathUtils.getSale(videoBean.getDyLikeCount()));

        holder.commission.setText(list.get(position).getCouponMoney() + "元劵");
        holder.tv_price.setText("" + list.get(position).getItemPrice());


        if (!TextUtils.isEmpty(list.get(position).getTkMoney() )){
            holder.tv_coupul.setText("赚 ¥ " +MathUtils.getMuRatioComPrice(UserLocalData.getUser(context).getCalculationRate(), list.get(position).getTkMoney() + ""));

        }

//            commission.setText(getString(R.string.upgrade_commission));
//            commission.setVisibility(View.GONE);

        //  holder.tv_coupul.setText("预估收益"+list.get(position).getTkMoney()+"元");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginUtil.checkIsLogin((Activity) context)) {
                    Log.e("sfsdf","cid+"+cid+"position+"+position+"page+"+page);
                    VideoActivity.start(context, (List<ShopGoodInfo>) list,position,cid,page);
                }

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
