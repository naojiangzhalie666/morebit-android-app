
package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.zjzy.morebit.R;
import com.zjzy.morebit.Activity.NumberGoodsDetailsActivity;
import com.zjzy.morebit.pojo.ConsComGoodsInfo;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.ViewShowUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 消费佣金订单明细
 */
public class ConsComGoodsDetailAdapter extends RecyclerView.Adapter {
    private int teamType = 0;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<ConsComGoodsInfo> mDatas = new ArrayList<>();

    public ConsComGoodsDetailAdapter(Context context, List<ConsComGoodsInfo> datas) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;

        if (datas != null && datas.size() > 0) {
            mDatas.addAll(datas);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new PlatFormViewHolder(mInflater.inflate(R.layout.item_order_taobao_pdd, parent, false));
        } else {
            return new SelfViewHolder(mInflater.inflate(R.layout.item_order_goods_detail, parent, false));
        }
    }

    public void setData(List<ConsComGoodsInfo> datas) {
        mDatas.clear();
        if (datas != null && datas.size() > 0) {
            mDatas.addAll(datas);
        }
    }


    @Override
    public int getItemViewType(int position) {
        ConsComGoodsInfo info = mDatas.get(position);
        if (info.getType() == 10) {
            return 1;
        }
        return 0;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PlatFormViewHolder) {
            platformViewHolder((PlatFormViewHolder) holder, position);
        } else if (holder instanceof SelfViewHolder) {
            selfViewHolder((SelfViewHolder) holder, position);
        }
    }

    private void selfViewHolder(SelfViewHolder viewHolder, final int position) {
        final ConsComGoodsInfo info = mDatas.get(position);
        if (TextUtils.isEmpty(info.getItemImg())) {
            viewHolder.pic.setImageResource(R.drawable.icon_default);
        } else {
            LoadImgUtils.setImg(mContext, viewHolder.pic, info.getItemImg());
        }


        try {
            viewHolder.title.setText(info.getItemName());
            if (!TextUtils.isEmpty(info.getCreateTime()) && !"0".equals(info.getCreateTime())) {
                viewHolder.createDay.setText("创建: " + info.getCreateTime());
            } else {
                viewHolder.createDay.setText("");
            }
            String goodsPrice = info.getPaymentAmount();
            if (!TextUtils.isEmpty(goodsPrice)) {
                viewHolder.goods_price.setText(goodsPrice);
            } else {
                viewHolder.goods_price.setText("");
            }
            String goodsNum = info.getGoodsNum();
            if (!TextUtils.isEmpty(goodsNum)) {
                viewHolder.order_googs_num.setText(mContext.getResources().getString(R.string.number_goods_count, goodsNum));
            } else {
                viewHolder.order_googs_num.setText(mContext.getResources().getString(R.string.number_goods_count, "0"));
            }
            String unit = info.getUnit();
            if (!TextUtils.isEmpty(goodsNum)) {
                viewHolder.order_goods_unit.setText(unit);
            } else {
                viewHolder.order_goods_unit.setText("");
            }
            //订单
            String orderNo = info.getOrderSn();
            if (!TextUtils.isEmpty(orderNo)) {
                viewHolder.tv_order_no.setText(mContext.getResources().getString(R.string.number_order_no, orderNo));
            } else {
                viewHolder.tv_order_no.setText("");
            }

            viewHolder.tv_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtil.coayText((Activity) mContext, info.getOrderSn());
                    ViewShowUtils.showShortToast(mContext, mContext.getString(R.string.coayTextSucceed));
                }
            });


            if ("4".equals(info.getStatus())) {//已关闭
                viewHolder.tv_order_status.setText("已关闭");
                viewHolder.shipGoodsTv.setVisibility(View.GONE);
                viewHolder.go_goods_detail_tv.setVisibility(View.VISIBLE);
                viewHolder.go_goods_detail_tv.setText("重新购买");
                viewHolder.go_goods_detail_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //重新购买，商品详情
                        if (info.isOnSale()){
                            NumberGoodsDetailsActivity.start((Activity) mContext, info.getItemId());
                        }else{
                            ToastUtils.showLong("商品已下架");
                        }

                    }
                });
                viewHolder.receiverGoodsTv.setVisibility(View.GONE);
                viewHolder.go_goods_pay_tv.setVisibility(View.GONE);
                viewHolder.tv_num.setVisibility(View.GONE);
                viewHolder.tv_date.setVisibility(View.GONE);

            } else if ("3".equals(info.getStatus())) {//已结算
                viewHolder.tv_order_status.setText("已结算");
                Log.e("wwww","物流");
                viewHolder.shipGoodsTv.setVisibility(View.VISIBLE);
                viewHolder.shipGoodsTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSelfOrderClickListener != null) {
                            onSelfOrderClickListener.onShip(info.getOrderSn(), position);
                        }
                    }
                });
                viewHolder.go_goods_detail_tv.setVisibility(View.VISIBLE);
                viewHolder.go_goods_detail_tv.setText("再次购买");
                viewHolder.go_goods_detail_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //重新购买，商品详情
                        if (info.isOnSale()){
                            NumberGoodsDetailsActivity.start((Activity) mContext, info.getItemId());
                        }else{
                            ToastUtils.showLong("商品已下架");
                        }
                    }
                });

                viewHolder.receiverGoodsTv.setVisibility(View.GONE);
                viewHolder.go_goods_pay_tv.setVisibility(View.GONE);

                viewHolder.tv_num.setVisibility(View.VISIBLE);
                viewHolder.tv_num.setText("送"+goodsPrice+"成长值");
                if(!TextUtils.isEmpty(info.getSettlementTime())){
                    viewHolder.tv_date.setVisibility(View.VISIBLE);
                    viewHolder.tv_date.setText("结算日期 :  "+info.getSettlementTime());
                }else{
                    viewHolder.tv_date.setVisibility(View.GONE);
                }


            } else if ("1".equals(info.getStatus())) {//已支付
                viewHolder.tv_order_status.setText("已支付");
                viewHolder.shipGoodsTv.setVisibility(View.GONE);
                viewHolder.receiverGoodsTv.setVisibility(View.GONE);
                viewHolder.go_goods_detail_tv.setVisibility(View.VISIBLE);
                viewHolder.go_goods_detail_tv.setText("再次购买");
                viewHolder.go_goods_detail_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //重新购买，商品详情
                        if (info.isOnSale()){
                            NumberGoodsDetailsActivity.start((Activity) mContext, info.getItemId());
                        }else{
                            ToastUtils.showLong("商品已下架");
                        }
                    }
                });
                viewHolder.tv_num.setVisibility(View.VISIBLE);
                viewHolder.tv_num.setText("送"+goodsPrice+"成长值");
                viewHolder.go_goods_pay_tv.setVisibility(View.GONE);
                viewHolder.tv_date.setVisibility(View.GONE);
            } else if ("6".equals(info.getStatus())) {
                viewHolder.tv_order_status.setText("待收货");
                Log.e("wwww","物流");
                viewHolder.shipGoodsTv.setVisibility(View.VISIBLE);
                viewHolder.receiverGoodsTv.setVisibility(View.VISIBLE);
                viewHolder.go_goods_detail_tv.setVisibility(View.GONE);
                viewHolder.go_goods_pay_tv.setVisibility(View.GONE);
                viewHolder.receiverGoodsTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSelfOrderClickListener != null) {
                            onSelfOrderClickListener.onReceiveGoods(info.getOrderSn(), position);
                        }
                    }
                });
                viewHolder.tv_num.setVisibility(View.GONE);
                viewHolder.tv_date.setVisibility(View.GONE);

            } else if ("2".equals(info.getStatus())) {//待支付

                viewHolder.tv_order_status.setText("待支付");
                viewHolder.shipGoodsTv.setVisibility(View.GONE);
                viewHolder.go_goods_detail_tv.setVisibility(View.GONE);
                viewHolder.receiverGoodsTv.setVisibility(View.GONE);
                viewHolder.go_goods_pay_tv.setVisibility(View.VISIBLE);
                viewHolder.go_goods_pay_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //再次支付
                        onSelfOrderClickListener.onPay(info.getOrderSn(), position);
                    }
                });
                viewHolder.tv_num.setVisibility(View.GONE);
                viewHolder.tv_date.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!info.isSelf()){
            viewHolder.shipGoodsTv.setVisibility(View.GONE);
            viewHolder.go_goods_detail_tv.setVisibility(View.GONE);
            viewHolder.tv_num.setVisibility(View.GONE);
            viewHolder.go_goods_pay_tv.setVisibility(View.GONE);
        }
        viewHolder.iten_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onAdapterClickListener != null) {
                    onAdapterClickListener.onItem(position);
                }
            }
        });
    }

    private void platformViewHolder(PlatFormViewHolder viewHolder, final int position){
        final ConsComGoodsInfo info = mDatas.get(position);
        if (TextUtils.isEmpty(info.getItemImg())) {
            viewHolder.pic.setImageResource(R.drawable.icon_default);
        } else {
            if (info.getItemImg().startsWith("//")){
                String replace = info.getItemImg().replace("//", "http://");
                LoadImgUtils.setImg(mContext, viewHolder.pic, replace);
            }else{
                LoadImgUtils.setImg(mContext, viewHolder.pic, info.getItemImg());
            }

        }


        try {
            viewHolder.title.setText(info.getItemName());
            if (!TextUtils.isEmpty(info.getCreateTime()) && !"0".equals(info.getCreateTime())) {
                viewHolder.createDay.setText("创建时间: " + info.getCreateTime());
            } else {
                viewHolder.createDay.setText("");
            }
            String goodsPrice = info.getPaymentAmount();
            if (!TextUtils.isEmpty(goodsPrice)) {
                viewHolder.goods_price.setText(goodsPrice);
            } else {
                viewHolder.goods_price.setText("");
            }
            String goodsNum = info.getGoodsNum();
            if (!TextUtils.isEmpty(goodsNum)) {
                viewHolder.order_googs_num.setText(mContext.getResources().getString(R.string.number_goods_count, goodsNum));
            } else {
                viewHolder.order_googs_num.setText(mContext.getResources().getString(R.string.number_goods_count, "0"));
            }
            String unit = info.getUnit();
            if (!TextUtils.isEmpty(goodsNum)) {
                viewHolder.order_goods_unit.setText(unit);
            } else {
                viewHolder.order_goods_unit.setText("");
            }
            //佣金
            String commission = info.getCommission();
            if (!TextUtils.isEmpty(commission) && !commission.equals("0") && !commission.equals("null")) {
                viewHolder.number_yongjin_tv.setVisibility(View.VISIBLE);
                viewHolder.number_yongjin_tv.setText(mContext.getResources().getString(R.string.number_yujin, commission));
            } else {
                viewHolder.number_yongjin_tv.setText("");
                viewHolder.number_yongjin_tv.setVisibility(View.GONE);
            }

            //订单
            String orderNo = info.getOrderSn();
            if (!TextUtils.isEmpty(orderNo)) {
                viewHolder.tv_order_no.setText(mContext.getResources().getString(R.string.number_order_no, orderNo));
            } else {
                viewHolder.tv_order_no.setText("");
            }


            viewHolder.tv_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtil.coayText((Activity) mContext, info.getOrderSn());
                    ViewShowUtils.showShortToast(mContext, mContext.getString(R.string.coayTextSucceed));
                }
            });

            //结算时间
            String settleDate = info.getSettlementTime();
            if (!TextUtils.isEmpty(settleDate)) {
                if (info.getType()==2){
                    if ("1".equals(info.getStatus())){
                        viewHolder.rl_settle_date.setVisibility(View.GONE);
                        viewHolder.tv_settle_date.setVisibility(View.GONE);
                    }else{
                        viewHolder.rl_settle_date.setVisibility(View.VISIBLE);
                        viewHolder.tv_settle_date.setText("结算日期："+settleDate);
                    }
                }else{
                    viewHolder.rl_settle_date.setVisibility(View.VISIBLE);
                    viewHolder.tv_settle_date.setText("结算日期："+settleDate);
                }

            } else {
                viewHolder.rl_settle_date.setVisibility(View.GONE);
                viewHolder.tv_settle_date.setVisibility(View.GONE);
            }
            if ("4".equals(info.getStatus())) {//已失效
                viewHolder.tv_order_status.setText("已失效");
                viewHolder.number_yongjin_tv.setBackgroundResource(R.drawable.bg_yujin_item2);
                viewHolder.number_yongjin_tv.setTextColor(Color.parseColor("#D3D3D3"));
            } else if ("3".equals(info.getStatus())) {//已结算
                viewHolder.tv_order_status.setText("已结算");


            } else if ("1".equals(info.getStatus())) {//已支付
                viewHolder.tv_order_status.setText("已付款");

            }

            if (info.isZeroOrder()==true){//0元购
                viewHolder.tv_new_person.setVisibility(View.VISIBLE);
            }else{
                viewHolder.tv_new_person.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.iten_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onAdapterClickListener != null) {
                    onAdapterClickListener.onItem(position);
                }
            }
        });
    }

//    private void taoBaoViewHolder(ViewHolder viewHolder, final int position) {
//        final ConsComGoodsInfo info = mDatas.get(position);
//
//
//        if (TextUtils.isEmpty(info.getItemImg())) {
//            viewHolder.pic.setImageResource(R.drawable.icon_default);
//        } else {
//            LoadImgUtils.setImg(mContext, viewHolder.pic, info.getItemImg());
//        }
//
//        try {
//            viewHolder.title.setText(info.getItemName());
//            if (!TextUtils.isEmpty(info.getCreateTime()) && !"0".equals(info.getCreateTime())) {
//                viewHolder.createDay.setText("创建: " + info.getCreateTime());
//            } else {
//                viewHolder.createDay.setText("");
//            }
//            viewHolder.xfyugu_tv.setText("¥" + info.getPaymentAmount());
//            if (!TextUtils.isEmpty(info.getCommission())) {
//                viewHolder.yjyugu_tv.setVisibility(View.VISIBLE);
//                viewHolder.yjyugu_tv.setText("赚佣" + info.getCommission() + "元");
//            } else {
//                viewHolder.yjyugu_tv.setVisibility(View.GONE);
//            }
//
//
//            if ("4".equals(info.getStatus())) {//失效4
//                if (teamType == 10) {
//                    viewHolder.jiesuanDay.setText("已关闭");
//                } else {
//                    viewHolder.jiesuanDay.setText("已失效");
//                }
//
//                viewHolder.yjyugu_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
//                viewHolder.jiesuanDay.setTextColor(ContextCompat.getColor(mContext, R.color.color_999999));
//                viewHolder.jiesuanDay_date.setVisibility(View.GONE);
//                viewHolder.yjyugu_tv.setBackgroundResource(R.drawable.bg_quan_2dp);
//                viewHolder.yjyugu_tv.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffe800));
//
//            } else {
//
//                if ("3".equals(info.getStatus())) {  //已结算
//                    viewHolder.jiesuanDay_date.setVisibility(View.VISIBLE);
//                    viewHolder.jiesuanDay.setText("已结算");
//                    viewHolder.jiesuanDay.setTextColor(ContextCompat.getColor(mContext, R.color.color_4FCC84));
//                    if (!TextUtils.isEmpty(info.getSettlementTime()) && !"0".equals(info.getSettlementTime())) {
//                        viewHolder.jiesuanDay_date.setText("结算日期: " + info.getSettlementTime());
//                    } else {
//                        viewHolder.jiesuanDay.setText("");
//                    }
//                } else {
//
//                    viewHolder.jiesuanDay.setText("已付款");
//                    viewHolder.jiesuanDay.setTextColor(ContextCompat.getColor(mContext, R.color.color_4FCC84));
//                    viewHolder.jiesuanDay_date.setVisibility(View.GONE);
//                }
//                viewHolder.yjyugu_tv.getPaint().setFlags(0); //还原
//                viewHolder.yjyugu_tv.getPaint().setAntiAlias(true);//抗锯齿
//                viewHolder.yjyugu_tv.invalidate();
//                viewHolder.yjyugu_tv.setBackgroundResource(R.drawable.bg_quan_2dp);
//                viewHolder.yjyugu_tv.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffe800));
//            }
//
//            viewHolder.tv_order.setText("订单单号: " + info.getOrderSn());
//            viewHolder.tv_copy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    AppUtil.coayText((Activity) mContext, info.getOrderSn());
//                    ViewShowUtils.showShortToast(mContext, mContext.getString(R.string.coayTextSucceed));
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        viewHolder.iten_rl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (onAdapterClickListener != null) {
//                    onAdapterClickListener.onItem(position);
//                }
//            }
//        });
//    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    private class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView title, createDay, jiesuanDay, xfyugu_tv, yjyugu_tv, tv_order, jiesuanDay_date;
        View bottomLine;
        LinearLayout iten_rl;
        TextView  tv_copy;


        public ViewHolder(View itemView) {
            super(itemView);
            iten_rl = (LinearLayout) itemView.findViewById(R.id.iten_rl);
            pic = (ImageView) itemView.findViewById(R.id.pic);
            title = (TextView) itemView.findViewById(R.id.title);
            createDay = (TextView) itemView.findViewById(R.id.createDay);
            jiesuanDay = (TextView) itemView.findViewById(R.id.jiesuanDay);
            xfyugu_tv = (TextView) itemView.findViewById(R.id.xfyugu_tv);
            jiesuanDay_date = (TextView) itemView.findViewById(R.id.jiesuanDay_date);
            yjyugu_tv = (TextView) itemView.findViewById(R.id.yjyugu_tv);
            tv_order = (TextView) itemView.findViewById(R.id.tv_order);
            bottomLine = (View) itemView.findViewById(R.id.bottomLine);

            tv_copy = itemView.findViewById(R.id.tv_copy);
        }
    }

    //平台商品
    private class PlatFormViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView title, createDay, tv_order_status, goods_price, order_googs_num, order_goods_unit, number_yongjin_tv;

        LinearLayout iten_rl;
        TextView tv_copy, tv_order_no;
        TextView tv_new_person,tv_settle_date;
        RelativeLayout rl_settle_date;

        public PlatFormViewHolder(View itemView) {
            super(itemView);
            iten_rl = (LinearLayout) itemView.findViewById(R.id.iten_rl);
            pic = (ImageView) itemView.findViewById(R.id.pic);
            title = (TextView) itemView.findViewById(R.id.title);
            createDay = (TextView) itemView.findViewById(R.id.createDay);
            tv_order_status = (TextView) itemView.findViewById(R.id.tv_order_status);
            goods_price = (TextView) itemView.findViewById(R.id.goods_price);
            order_googs_num = (TextView) itemView.findViewById(R.id.order_googs_num);
            order_goods_unit = (TextView) itemView.findViewById(R.id.order_goods_unit);
            //赚取佣金 会员没有佣金，可以不显示
            number_yongjin_tv = (TextView) itemView.findViewById(R.id.number_yongjin_tv);
            tv_order_no = (TextView) itemView.findViewById(R.id.tv_order_no);
            tv_copy = (TextView) itemView.findViewById(R.id.tv_copy);
            tv_new_person = (TextView)itemView.findViewById(R.id.tv_new_person);
            tv_settle_date = (TextView)itemView.findViewById(R.id.tv_settle_date);
            rl_settle_date = (RelativeLayout)itemView.findViewById(R.id.rl_settle_date);
        }
    }

    //自营商品
    private class SelfViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView title, createDay, tv_order_status, goods_price, order_googs_num, order_goods_unit,tv_num,tv_date;

        LinearLayout iten_rl;
        TextView tv_copy, shipGoodsTv, receiverGoodsTv, go_goods_detail_tv, go_goods_pay_tv, tv_order_no;
        RelativeLayout rl_receiver_ggods;

        public SelfViewHolder(View itemView) {
            super(itemView);
            iten_rl = (LinearLayout) itemView.findViewById(R.id.iten_rl);
            pic = (ImageView) itemView.findViewById(R.id.pic);
            title = (TextView) itemView.findViewById(R.id.title);
            createDay = (TextView) itemView.findViewById(R.id.createDay);
            tv_order_status = (TextView) itemView.findViewById(R.id.tv_order_status);
            goods_price = (TextView) itemView.findViewById(R.id.goods_price);
            order_googs_num = (TextView) itemView.findViewById(R.id.order_googs_num);
            order_goods_unit = (TextView) itemView.findViewById(R.id.order_goods_unit);
            //赚取佣金 会员没有佣金，可以不显示
            //number_yongjin_tv = (TextView) itemView.findViewById(R.id.number_yongjin_tv);
            tv_order_no = (TextView) itemView.findViewById(R.id.tv_order_no);

            tv_copy = itemView.findViewById(R.id.tv_copy);
            shipGoodsTv = (TextView) itemView.findViewById(R.id.ship_goods_tv);
            receiverGoodsTv = (TextView) itemView.findViewById(R.id.receiver_goods_tv);
            rl_receiver_ggods = (RelativeLayout) itemView.findViewById(R.id.rl_receiver_ggods);
            go_goods_detail_tv = (TextView) itemView.findViewById(R.id.go_goods_detail_tv);
            go_goods_pay_tv = (TextView) itemView.findViewById(R.id.go_goods_pay_tv);
            tv_num=itemView.findViewById(R.id.tv_num);//成长值
            tv_date=itemView.findViewById(R.id.tv_date);//结算日期
        }
    }

    private OnSelfOrderClickListener onSelfOrderClickListener;

    public interface OnSelfOrderClickListener {

        public void onReceiveGoods(String orderId, int position);

        public void onShip(String orderId, int position);

        public void onPay(String orderId, int position);
    }

    public void setOnSelfOrderClickListener(OnSelfOrderClickListener listener) {
        onSelfOrderClickListener = listener;
    }

    private OnAdapterClickListener onAdapterClickListener;

    public interface OnAdapterClickListener {
        public void onItem(int position);
    }

    public void setOnAdapterClickListener(OnAdapterClickListener listener) {
        onAdapterClickListener = listener;
    }

}