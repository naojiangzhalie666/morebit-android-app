package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.HomeRecommendGoods;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;
import com.zjzy.morebit.view.AspectRatioView;

import java.util.List;

/**
 * Created by YangBoTian on 2018/8/22.
 */

public class HomeRecommendAdapter extends BaseMultiItemQuickAdapter<HomeRecommendGoods, BaseViewHolder> {
    public static final int GOODS_TYPE = 0;
    public static final int AD_TYPE = 1;
    private int mDefHeight = 200;

    private Context mContext;
    private   int mWindowWidth;
    private final int mImgWidth;

    public HomeRecommendAdapter(Context context, List<HomeRecommendGoods> homeRecommendGoods) {
        super(homeRecommendGoods);
        MyLog.i("test", "HomeRecommendAdapter");
        mContext = context;
        addItemType(HomeRecommendGoods.TYPE_AD, R.layout.home_recyclerview_item_ad);
        addItemType(HomeRecommendGoods.TYPE_GOODS, R.layout.home_recyclerview_item_goods);
        mDefHeight  = mContext.getResources().getDimensionPixelSize(R.dimen.home_recommend_img_def_height);
        mWindowWidth = AppUtil.getWindowWidth((Activity) context);
        mImgWidth = (mWindowWidth - (context.getResources().getDimensionPixelSize(R.dimen.home_recommend_item_decoration) * 3))/2;
    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder, HomeRecommendGoods homeRecommendGoods) {
//        MyLog.i("test","convert");
        switch (homeRecommendGoods.getItemType()) {
            case HomeRecommendGoods.TYPE_AD:
                final ImageInfo imageInfo = homeRecommendGoods.getImageInfo();
//                MyLog.i("test","TYPE_AD");
                if (imageInfo != null) {
                    AspectRatioView ar_img = baseViewHolder.getView(R.id.ar_img);
                    ImageView img = baseViewHolder.getView(R.id.img);
                    float height = mDefHeight ;
                    if (imageInfo.getHeight() != 0){
                        height = imageInfo.getHeight();
                    }
                    float width = mImgWidth;
                    if (imageInfo.getWidth() != 0){
                        width = imageInfo.getWidth();
                    }
                    float v = width / height;
                    ar_img.setAspectRatio(v);
//                    if (imageInfo.getHeight() != 0) {
//                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, imageInfo.getHeight());
//                        cv_img.setLayoutParams(layoutParams);
//                    } else {
//                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight);
//                        cv_img.setLayoutParams(layoutParams);
//                    }
                    LoadImgUtils.setImg(mContext, img, imageInfo.getPicture());
                    baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BannerInitiateUtils.gotoAction((Activity) mContext, imageInfo);
                        }
                    });
                }
                break;
            case HomeRecommendGoods.TYPE_GOODS:
                final ShopGoodInfo item = homeRecommendGoods.getShopGoodInfo();
                if (item != null) {
//                    MyLog.i("test","TYPE_GOODS： " +item.getTitle());
                    TextView title = baseViewHolder.getView(R.id.title);
                    TextView commission = baseViewHolder.getView(R.id.commission);
                    TextView coupon = baseViewHolder.getView(R.id.coupon);
                    TextView discount_price = baseViewHolder.getView(R.id.discount_price);
                    TextView price = baseViewHolder.getView(R.id.price);
                    TextView tv_shop_name = baseViewHolder.getView(R.id.tv_shop_name);
                    TextView sales = baseViewHolder.getView(R.id.sales);

                    ImageView img = baseViewHolder.getView(R.id.img);
                    ImageView video_play = baseViewHolder.getView(R.id.video_play);
                    ImageView good_mall_tag = baseViewHolder.getView(R.id.good_mall_tag);
                    LinearLayout return_cash = baseViewHolder.getView(R.id.ll_return_cash);
                    StringsUtils.retractTitle( good_mall_tag, title,MathUtils.getTitle(item));
                    LoadImgUtils.setImg(mContext, img, MathUtils.getPicture(item));
                    TextView markTv = baseViewHolder.getView(R.id.markTv);
                    TextView subsidiesPriceTv = baseViewHolder.getView(R.id.subsidiesPriceTv);
                    if(LoginUtil.checkIsLogin((Activity) mContext, false) && !TextUtils.isEmpty(item.getSubsidiesPrice())){
                        subsidiesPriceTv.setVisibility(View.VISIBLE);
                        String getRatioSubside = MathUtils.getMuRatioSubSidiesPrice(UserLocalData.getUser(mContext).getCalculationRate(), item.getSubsidiesPrice());
                        subsidiesPriceTv.setText(mContext.getString(R.string.subsidiesPrice, getRatioSubside));
                    }else{
                        subsidiesPriceTv.setVisibility(View.GONE);
                        subsidiesPriceTv.setText("");
                    }

                    if(!TextUtils.isEmpty(item.getItemLabeling())  ){
                        markTv.setVisibility(View.VISIBLE);
                        markTv.setText(item.getItemLabeling());
                    }else{
                        markTv.setVisibility(View.GONE);
                    }
                    if (StringsUtils.isEmpty(item.getCouponPrice())) {
                        return_cash.setVisibility(View.GONE);
                    } else {
                        return_cash.setVisibility(View.VISIBLE);
                    }

                    //判断是淘宝还是天猫的商品
                    if (item.getShopType() == 2) {
                        good_mall_tag.setImageResource(R.drawable.tianmao);
                    } else {
                        good_mall_tag.setImageResource(R.drawable.taobao);
                    }
                    if (!StringsUtils.isShowVideo(item.getVideoid())) {
                        video_play.setVisibility(View.GONE);
                    } else {
                        video_play.setVisibility(View.VISIBLE);
                    }
                    coupon.setText(mContext.getString(R.string.yuan, MathUtils.getCouponPrice(item.getCouponPrice())));
                    if (C.UserType.operator.equals(UserLocalData.getUser(mContext).getPartner()) || C.UserType.vipMember.equals(UserLocalData.getUser(mContext).getPartner())) {
                        if(!StringsUtils.isEmpty(item.getCommission())){
                            commission.setText(mContext.getString(R.string.commission, MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), item.getCommission())));
                        } else {
                            commission.setVisibility(View.GONE);
                        }

                    } else {
                        UserInfo userInfo1 =UserLocalData.getUser();
                        if (userInfo1 == null || TextUtils.isEmpty(UserLocalData.getToken())) {
                            commission.setText(mContext.getString(R.string.commission, MathUtils.getMuRatioComPrice(C.SysConfig.NUMBER_COMMISSION_PERCENT_VALUE, item.getCommission())));
                        }else{
                            commission.setText(mContext.getString(R.string.commission, MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), item.getCommission())));
                        }
//                        commission.setText(mContext.getString(R.string.upgrade_commission));
                    }
                    //店铺名称
                    if (!TextUtils.isEmpty(item.getShopName())) {
                        tv_shop_name.setText(item.getShopName());
                    }
                    discount_price.setText(mContext.getString(R.string.coupon, MathUtils.getVoucherPrice(item.getVoucherPrice())));
                    price.setText(mContext.getString(R.string.income, MathUtils.getPrice(item.getPrice())));
                    price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    sales.setText(mContext.getString(R.string.sales, MathUtils.getSales(item.getSaleMonth())));

                    baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SensorsDataUtil.getInstance().browseProductTrack("多点优选推荐",baseViewHolder.getPosition(),item.getItemSourceId(),item.getTitle(),item.getPrice(),"","","","");
                            GoodsDetailActivity.start(mContext, item);
                        }
                    });
                }
                break;
        }
    }

}
