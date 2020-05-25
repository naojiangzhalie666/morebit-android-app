package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.CircleDayHotFragment;
import com.zjzy.morebit.pojo.MarkermallCircleInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.ShareUtil;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.view.CommercialShareDialog;

import java.util.ArrayList;
import java.util.List;


public class MarkermallCirleItmeGoodsAdapter extends BaseMYShareAdapter<MarkermallCirleItmeGoodsAdapter.GoodsViewHolder> {

    private LayoutInflater mInflater;
    private MarkermallCircleInfo mDatas;
    private MyAction.One<String> mUpdataShareTextAction;
    private String mMainTitle,mSubTitle;
    CommercialShareDialog shareDialog;
    public MarkermallCirleItmeGoodsAdapter(Context context, MarkermallCircleInfo images) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        mDatas = images;
    }


    @Override
    public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsViewHolder(mInflater.inflate(R.layout.itme_cirle_adapter_goods, parent, false));
    }

    @Override
    public void onBindViewHolder(GoodsViewHolder holder, final int position) {

        if (mDatas == null) {
            return;
        }
        final ShopGoodInfo info = mDatas.getGoods().get(position);
        holder.tv_coupon_price.setText("券后价"+mContext.getString(R.string.income_yuan,  MathUtils.getnum(info.getVoucherPrice())));
        String muRatioComPrice = MathUtils.getMuRatioComPrice(UserLocalData.getUser().getCalculationRate(), info.getCommission());
        holder.tv_title.setText(MathUtils.getTitle(info));

        if(!TextUtils.isEmpty(info.getItemLabeling())  ){
            holder. markTv.setVisibility(View.VISIBLE);
            holder.markTv.setText(info.getItemLabeling());
        }else{
            holder.markTv.setVisibility(View.GONE);
        }
        LoadImgUtils.loadingCornerBitmap(mContext, holder.iv_head, MathUtils.getPicture(info));

        UserInfo userInfo1 =UserLocalData.getUser();
        if (userInfo1 == null || TextUtils.isEmpty(UserLocalData.getToken())) {
            holder.tv_share.setText(mContext.getString(R.string.now_login));
        }else{
            holder.tv_share.setText(mContext.getString(R.string.share_price_new, muRatioComPrice));
        }
        holder.tv_price.setText("券额"+mContext.getString(R.string.income_yuan, MathUtils.getnum(info.getCouponPrice())));
        if (MathUtils.getnum(info.getCouponPrice()).equals("0")){
            holder.tv_price.setVisibility(View.GONE);
            holder.tv_coupon_price.setText("活动价"+mContext.getString(R.string.income_yuan,  MathUtils.getnum(info.getVoucherPrice())));
        }


        if (info.getIsExpire() == 1) {//过期
            holder.iv_head_qg_bg.setVisibility(View.VISIBLE);
            holder.iv_head_qg.setVisibility(View.VISIBLE);
        } else {
            holder.iv_head_qg_bg.setVisibility(View.GONE);
            holder.iv_head_qg.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info.getIsExpire() == 1) {//过期
                    ViewShowUtils.showShortToast(mContext,"商品已被抢光！");
                    return;
                }
                List<ShopGoodInfo> osgData = GoodsUtil.CirleInfoToGoodInfo(mDatas);
//                SensorsDataUtil.getInstance().mifenClickTrack(mMainTitle,mSubTitle,"发圈",position,info.getItemSourceId(),info.getItemTitle(),"","",info.getPicture(),info.getItemTitle(),"",2,"");
                if (osgData != null) {
                    GoodsDetailActivity.start(mContext, osgData.get(position));
                }
            }
        });
        holder.tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SensorsDataUtil.getInstance().mifenClickTrack(mMainTitle,mSubTitle,"发圈",position,info.getItemSourceId(),info.getItemTitle(),"","",info.getPicture(),info.getItemTitle(),"",2,"");
                if (!LoginUtil.checkIsLogin((Activity) mContext)) return;
                if (TaobaoUtil.isAuth()) {   //淘宝授权
                    TaobaoUtil.getAllianceAppKey((BaseActivity) mContext);
                } else {
                    if (mUpdataShareTextAction != null) {
                        mUpdataShareTextAction.invoke("");
                    }
                    showShareDialog(info);
                }
            }
        });


    }

    private void showShareDialog(final ShopGoodInfo info) {
        showChoosePicDialog(TYPE_GOODS_IMAGE,new MyAction.One<Integer>() {
            @Override
            public void invoke(Integer which) {
                if (which == 0) {
                    openShareDialog(info, CircleDayHotFragment.TypeCommodity);
//                    share(info, CircleDayHotFragment.TypeCommodityImg);
                } else {
                    openShareDialog(info, CircleDayHotFragment.TypeCommodityImg);
//                    share(info, CircleDayHotFragment.TypeCommodity);
                }
            }
        });
    }

    private void share(final ShopGoodInfo item, int type, int sharePlatform) {
        switch (type) {
            case CircleDayHotFragment.TypeCommodityImg:
                List<String> pictures = new ArrayList<>();
                if (!TextUtils.isEmpty(item.getPicture())) {
                    pictures.add(item.getPicture());
                }
                shareImg(mDatas, pictures, sharePlatform);
//                shareImg(mDatas, pictures);
                break;
            case CircleDayHotFragment.TypeCommodity:
                List<ShopGoodInfo> osgData = new ArrayList<>();
                osgData.add(item);
//                shareGoods(mDatas, osgData);
                shareGoods(mDatas, osgData, sharePlatform);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas == null) {
            return 0;
        } else {
            return mDatas.getGoods() == null ? 0 : mDatas.getGoods().size();
        }
    }

    public class GoodsViewHolder extends RecyclerView.ViewHolder {
        TextView tv_price;
        TextView tv_coupon_price;
        TextView tv_title;
        TextView tv_share;
        TextView markTv;
        ImageView iv_head;
        ImageView iv_head_qg_bg;
        ImageView iv_head_qg;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_coupon_price = itemView.findViewById(R.id.tv_coupon_price);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_share = itemView.findViewById(R.id.tv_share);
            markTv = itemView.findViewById(R.id.markTv);
            iv_head = itemView.findViewById(R.id.iv_head);
            iv_head_qg = itemView.findViewById(R.id.iv_head_qg);
            iv_head_qg_bg = itemView.findViewById(R.id.iv_head_qg_bg);
        }
    }

    public void seData(MarkermallCircleInfo item) {
        mDatas = item;
        notifyDataSetChanged();
    }

    public void setUpdataShareTextAction(MyAction.One<String> action) {
        mUpdataShareTextAction = action;
    }
    public void setTitle(String mMainTitle,String mSubTitle) {
        this.mMainTitle = mMainTitle;
        this.mSubTitle = mSubTitle;
    }

    private void openShareDialog(final ShopGoodInfo item, final int type) {
        shareDialog = new CommercialShareDialog(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.weixinFriend: //分享到好友
                        share(item, type, ShareUtil.WechatType);
                        break;
                    case R.id.weixinCircle: //分享到朋友圈
                        share(item, type, ShareUtil.WeMomentsType);
                        break;
                    case R.id.qqFriend: //分享到QQ
                        share(item, type, ShareUtil.QQType);
                        break;
                    case R.id.qqRoom: //分享到QQ空间
                        share(item, type, ShareUtil.QQZoneType);
                        break;
                    case R.id.sinaWeibo: //分享到新浪微博
                        share(item, type, ShareUtil.WeiboType);
                        break;
                    default:
                        break;

                }

                shareDialog.dismiss();
            }
        });

        if (!shareDialog.isShowing()) {
            shareDialog.show();
        }
    }

}