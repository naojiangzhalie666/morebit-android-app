package com.markermall.cat.home.adpater;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.markermall.cat.Activity.GoodsDetailActivity;
import com.markermall.cat.R;
import com.markermall.cat.adapter.SimpleAdapter;
import com.markermall.cat.adapter.holder.SimpleViewHolder;
import com.markermall.cat.main.ui.fragment.GoodNewsFramgent;
import com.markermall.cat.pojo.ImageInfo;
import com.markermall.cat.pojo.ShopGoodInfo;
import com.markermall.cat.pojo.goods.HandpickBean;
import com.markermall.cat.utils.LoadImgUtils;
import com.markermall.cat.utils.MathUtils;
import com.markermall.cat.utils.SensorsDataUtil;
import com.markermall.cat.utils.StringsUtils;


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

            LoadImgUtils.setImg(mContext, banner, activityBean.getPicture());
        }
        if (activityBean.getItems() != null && activityBean.getItems().size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(linearLayoutManager);
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
            if(!TextUtils.isEmpty(item.getItemLabeling())  ){
                 markTv.setVisibility(View.VISIBLE);
                 markTv.setText(item.getItemLabeling());
            }else{
                 markTv.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(item.getPicture())) {
                LoadImgUtils.setImg(mContext, img, item.getPicture());
            }
            title.setText(MathUtils.getTitle(item));

            discount_price.setText(MathUtils.getVoucherPrice(item.getVoucherPrice()));
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

        }

        @Override
        protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
            return layoutInflater.inflate(R.layout.item_recommend_acitivty_horizontal, parent, false);
        }
    }

}
