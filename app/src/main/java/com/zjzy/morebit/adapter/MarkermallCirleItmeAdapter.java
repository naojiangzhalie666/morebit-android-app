package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.ShortVideoPlayActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.Module.common.Activity.ImagePagerActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.MarkermallCircleInfo;
import com.zjzy.morebit.pojo.MarkermallCircleItemInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.utils.CopyPropertiesUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.ViewShowUtils;

import java.util.ArrayList;
import java.util.List;


public class MarkermallCirleItmeAdapter extends RecyclerView.Adapter {

    private boolean mIsOneImg;
    private LayoutInflater mInflater;
    private Context mContext;
    private MarkermallCircleInfo mDatas;
    private int mImgWidth;
    private int mWindowWidth;
    private String mMainTitle,mSubTitle;
    public MarkermallCirleItmeAdapter(Context context, MarkermallCircleInfo images, int loadType, int imgWidth, boolean isOneImg, int windowWidth) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        mDatas = images;
        this.mImgWidth = imgWidth;
        this.mWindowWidth = windowWidth;
        this.mIsOneImg = isOneImg;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImgViewHolder(mInflater.inflate(R.layout.itme_cirle_adapter_img, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (mDatas == null) {
            return;
        }
        final ImgViewHolder holder1 = (ImgViewHolder) holder;

        if (mDatas.getShareRangItems() != null && mDatas.getShareRangItems().size() > 0) {
            final MarkermallCircleItemInfo circleItemInfo = mDatas.getShareRangItems().get(position);
            if (circleItemInfo != null) {
                LoadImg(circleItemInfo.getPicture(), holder1, mIsOneImg);
                if (!StringsUtils.isEmpty(circleItemInfo.getItemVideoid())) {  //视频
                    ((ImgViewHolder) holder).iv_expire.setImageResource(R.drawable.circle_icon__goods_play);
                    holder1.iv_expire.setVisibility(View.VISIBLE);
                    holder1.tv_after_discount.setVisibility(View.GONE);
                    holder1.iv_head_qg_bg.setVisibility(View.VISIBLE);
                } else if (!TextUtils.isEmpty(circleItemInfo.getItemSourceId())) {  //商品
                    holder1.iv_expire.setImageResource(R.drawable.circle_icon_goods_expire);
                    if (circleItemInfo.getIsExpire() == 1) {  //已过期/失效
                        holder1.iv_expire.setVisibility(View.VISIBLE);
                        holder1.tv_after_discount.setVisibility(View.GONE);
                        holder1.iv_head_qg_bg.setVisibility(View.VISIBLE);
                    } else {
                        holder1.tv_after_discount.setVisibility(View.VISIBLE);
                        holder1.iv_expire.setVisibility(View.GONE);
                        holder1.iv_head_qg_bg.setVisibility(View.GONE);
                        holder1.tv_after_discount.setText(mContext.getString(R.string.coupon_add_text, MathUtils.getVoucherPrice(circleItemInfo.getItemVoucherPrice())));
                    }

                } else {       //图片
                    holder1.iv_expire.setVisibility(View.GONE);
                    holder1.tv_after_discount.setVisibility(View.GONE);
                }
                holder1.iv_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        SensorsDataUtil.getInstance().mifenClickTrack(mMainTitle,mSubTitle,"发圈",position,circleItemInfo.getItemSourceId(),circleItemInfo.getItemTitle(),"","",circleItemInfo.getPicture(),circleItemInfo.getItemTitle(),"",0,"");
                        if (!StringsUtils.isEmpty(circleItemInfo.getItemVideoid())) {  //视频
                            ShortVideoPlayActivity.start((Activity) mContext,1,circleItemInfo.getItemVideoid());
                        } else if (!TextUtils.isEmpty(circleItemInfo.getItemSourceId())) { //商品
                            if (circleItemInfo.getIsExpire() == 1) {//过期
                                ViewShowUtils.showShortToast(mContext,"商品已被抢光！");
                                return;
                            }
                            ShopGoodInfo shopGoodInfo = new ShopGoodInfo();
                            try {
                                CopyPropertiesUtil.copyProperties(circleItemInfo, shopGoodInfo);
                                GoodsDetailActivity.start(mContext, shopGoodInfo);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else if(5 == circleItemInfo.getImageSource()){
                            //外站图片
                            if(!TextUtils.isEmpty(circleItemInfo.getLink())){
                                ShowWebActivity.start((Activity) mContext,circleItemInfo.getLink(),circleItemInfo.getPictureTitle());
                            }

                        } else {   //图片
                            List<MarkermallCircleItemInfo> markermallCircleItemInfoList =mDatas.getShareRangItems();
                            List<String> pictures = new ArrayList<String>();
                            for(MarkermallCircleItemInfo markermallCircleItemInfo : markermallCircleItemInfoList){
                                if(StringsUtils.isEmpty(markermallCircleItemInfo.getItemVideoid())&&StringsUtils.isEmpty(markermallCircleItemInfo.getItemSourceId())&&!TextUtils.isEmpty(markermallCircleItemInfo.getPicture())){
                                    pictures.add(markermallCircleItemInfo.getPicture());
                                }
                            }
                            int index=0;
                            for(int i=0;i<pictures.size();i++){
                                if(pictures.get(i).equals(circleItemInfo.getPicture())){
                                    index = i;
                                }
                            }
                            Intent intent = new Intent(mContext, ImagePagerActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putStringArrayList(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) pictures);
                            bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, index);
                            bundle.putString(ImagePagerActivity.TAOBAO_ID, mDatas.getName());
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }
                    }
                });
            }
        } else {
            holder1.iv_head_qg_bg.setVisibility(View.GONE);
            String url = mDatas.getPicture().get(position);
            LoadImg(url, holder1, mIsOneImg);
            holder1.iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ImagePagerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) mDatas.getPicture());
                    bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                    bundle.putString(ImagePagerActivity.TAOBAO_ID, mDatas.getName());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });

        }

    }

    private void LoadImg(final String imgUrl, ImgViewHolder holder1, boolean isOneIcon) {

        if (isOneIcon) {
            int height = mDatas.getHeight();
            int width = mDatas.getWidth();
            double w2h = 0;
            if (height != 0 && width != 0) {
                double h2w = height * 1.0 / width * 1.0;//宽高比
                height = (int) (h2w * mWindowWidth);
                width = mWindowWidth;
                if (height < width) {
                    height = height * 2 / 3;
                    width = width * 2 / 3;
                } else {
                    height = height * 1 / 3;
                    width = width * 1 / 3;
                }
                if (height > 1000) {
                    height = mContext.getResources().getDimensionPixelSize(R.dimen.circle_itme_one_img_height);
                }
            } else {
                height = mWindowWidth / 3;
                width = mWindowWidth / 3;
            }
            holder1.cv_img.setLayoutParams(new RelativeLayout.LayoutParams((int) width, (int) height));
            holder1.iv_icon.setLayoutParams(new RelativeLayout.LayoutParams((int) width, (int) height));
            LoadImgUtils.setImg(mContext, holder1.iv_icon, imgUrl);
        } else {
            if (mImgWidth > 0) {
                holder1.cv_img.setLayoutParams(new RelativeLayout.LayoutParams(mImgWidth, mImgWidth));
                holder1.iv_icon.setLayoutParams(new RelativeLayout.LayoutParams(mImgWidth, mImgWidth));
                LoadImgUtils.setImg(mContext, holder1.iv_icon, imgUrl);
            }
        }
    }


    @Override
    public int getItemCount() {
        if (mDatas == null) {
            return 0;
        }
        if (mDatas.getShareRangItems() != null && mDatas.getShareRangItems().size() > 0) {
            return mDatas.getShareRangItems().size();
        } else {
            return mDatas.getPicture() == null ? 0 : mDatas.getPicture().size();
        }
    }

    public void seData(MarkermallCircleInfo item) {
        mDatas = item;
        notifyDataSetChanged();
    }

    public void seIsOneImg(boolean isOneImg) {
        mIsOneImg = isOneImg;
    }

    private class ImgViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_icon;
        ImageView iv_expire;
        CardView cv_img;
        TextView tv_after_discount;
        ImageView iv_head_qg_bg;

        public ImgViewHolder(View itemView) {
            super(itemView);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            iv_expire = (ImageView) itemView.findViewById(R.id.iv_expire);
            cv_img = (CardView) itemView.findViewById(R.id.cv_img);
            tv_after_discount = (TextView) itemView.findViewById(R.id.tv_after_discount);
            iv_head_qg_bg = (ImageView) itemView.findViewById(R.id.iv_head_qg_bg);
        }
    }

    private class GoodsViewHolder extends RecyclerView.ViewHolder {

        public GoodsViewHolder(View itemView) {
            super(itemView);
        }
    }
    public void setTitle(String mMainTitle,String mSubTitle) {
        this.mMainTitle = mMainTitle;
        this.mSubTitle = mSubTitle;
    }
}