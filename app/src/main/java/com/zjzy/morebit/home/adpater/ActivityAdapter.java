package com.zjzy.morebit.home.adpater;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.SimpleAdapter;
import com.zjzy.morebit.adapter.holder.SimpleViewHolder;
import com.zjzy.morebit.main.ui.fragment.GoodNewsFramgent;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.goods.HandpickBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.SpaceItemDecorationUtils;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.UI.SpaceItemRightUtils;


/**
 * Created by YangBoTian on 2018/9/14.
 */

public class ActivityAdapter extends SimpleAdapter<HandpickBean, SimpleViewHolder> {
    Context mContext;

    public ActivityAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        final HandpickBean activityBean = getItem(position);
        RecyclerView mRecyclerView = holder.viewFinder().view(R.id.recyclerview_activity);
        ImageView banner = holder.viewFinder().view(R.id.banner);
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setId(activityBean.getId());
                imageInfo.setTitle(activityBean.getTitle());
                imageInfo.setPicture(activityBean.getPicture());
                if (!TextUtils.isEmpty(activityBean.getBackgroundImage())) {
                    imageInfo.setBackgroundImage(activityBean.getBackgroundImage());
                } else {
                    imageInfo.setBackgroundImage(activityBean.getPicture());
                }
                SensorsDataUtil.getInstance().setAcitivityClickTrack("", +activityBean.getId() + "");
                GoodNewsFramgent.start((Activity) mContext, imageInfo);
//                BannerInitiateUtils.gotoAction((Activity) mContext, activityBean);
            }
        });

        if (!TextUtils.isEmpty(activityBean.getPicture())) {

            LoadImgUtils.loadingCornerTop(mContext, banner, activityBean.getPicture(),5);
        }
        if (activityBean.getItems() != null && activityBean.getItems().size() > 0) {
            GridLayoutManager manager = new GridLayoutManager(mContext, 3);
            //设置图标的间距
            SpaceItemRightUtils spaceItemDecorationUtils = new SpaceItemRightUtils(20, 3);
            mRecyclerView.addItemDecoration(spaceItemDecorationUtils);
            mRecyclerView.setLayoutManager(manager);
            ActivityHorizontalAdapter activityHorizontalAdapter = new ActivityHorizontalAdapter(mContext);
            activityHorizontalAdapter.replace(activityBean.getItems());
            mRecyclerView.setAdapter(activityHorizontalAdapter);
        }

    }

    @Override
    protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        return layoutInflater.inflate(R.layout.item_recommend_acitivty, parent, false);
    }


    class ActivityHorizontalAdapter extends SimpleAdapter<ShopGoodInfo, SimpleViewHolder> {

        public ActivityHorizontalAdapter(Context context) {
            super(context);
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {
            final ShopGoodInfo item = getItem(position);
            ImageView img = holder.viewFinder().view(R.id.img);
            TextView title = holder.viewFinder().view(R.id.title);
            TextView markTv = holder.viewFinder().view(R.id.markTv);
            TextView discount_price = holder.viewFinder().view(R.id.discount_price);
            TextView coupon = holder.viewFinder().view(R.id.coupon);
            TextView price = holder.viewFinder().view(R.id.price);
            TextView tv_coupul=holder.viewFinder().view(R.id.tv_coupul);
            if(!TextUtils.isEmpty(item.getItemLabeling())  ){
                 markTv.setVisibility(View.VISIBLE);
                 markTv.setText(item.getItemLabeling());
            }else{
                 markTv.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(item.getPicture())) {
                LoadImgUtils.loadingCornerTop(mContext,img,item.getPicture(),5);
               // LoadImgUtils.setImg(mContext, img, item.getPicture());
            }
            title.setText(MathUtils.getTitle(item));

            discount_price.setText(item.getItemPrice());
            price.setText("¥"+MathUtils.getVoucherPrice(item.getVoucherPrice()));
            discount_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GoodsDetailActivity.start(mContext, item);
                }
            });
            if (StringsUtils.isEmpty(item.getCouponPrice())) {
                coupon.setVisibility(View.GONE);
            } else {
                coupon.setVisibility(View.VISIBLE);
                coupon.setText(mContext.getString(R.string.yuan, MathUtils.getCouponPrice(item.getCouponPrice())));
            }
            if (C.UserType.operator.equals(UserLocalData.getUser(mContext).getPartner())
                    || C.UserType.vipMember.equals(UserLocalData.getUser(mContext).getPartner())) {
                tv_coupul.setText("预估收益" + MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), item.getCommission() + "") + "元");
            } else {
                UserInfo userInfo1 = UserLocalData.getUser();
                if (userInfo1 == null || TextUtils.isEmpty(UserLocalData.getToken())) {
                    tv_coupul.setText("预估收益" + MathUtils.getMuRatioComPrice(C.SysConfig.NUMBER_COMMISSION_PERCENT_VALUE, item.getCommission() + "") + "元");
                } else {
                    tv_coupul.setText("预估收益" + MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), item.getCommission() + "") + "元");
                }


            }


        }

        @Override
        public int getItemCount() {
            return 3;
        }

        @Override
        protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
            return layoutInflater.inflate(R.layout.item_recommend_acitivty_horizontal, parent, false);
        }
    }

}
