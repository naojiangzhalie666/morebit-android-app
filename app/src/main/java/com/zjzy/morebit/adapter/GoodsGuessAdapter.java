package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.holder.SimpleViewHolder;
import com.zjzy.morebit.goods.shopping.ui.view.RecommendIndexView;
import com.zjzy.morebit.pojo.SelectFlag;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by YangBoTian on 2018/8/22.
 */

public class GoodsGuessAdapter extends SimpleAdapter<ShopGoodInfo, SimpleViewHolder> {
    public static int LINEARLAYOUT_TYPE = 1023;   //垂直布局
    public static int GRIDLAYOUT_TYPE = 1024;   //网格布局
    public static int TYPE_HOME = 0;  //首页列表
    public static int TYPE_OFFICAL_RECOM = 1;  //官方推荐
    public static int TYPE_COLLECT = 2;  //收藏
    public static int TYPE_SECKILL = 3;  // 限时秒杀
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int LINEARLAYOUT = 3;
    private Context mContext;
    private int type = 0;
    private int mListType = 0;

    private OnSelectListener mOnSelectListener;
    private List<SelectFlag> slFlag = new ArrayList<>();
    private int layoutType = GRIDLAYOUT_TYPE;     //默认网格布局
    private String mMaterialID;

    public GoodsGuessAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new HolderLeft(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_guess, parent, false));


    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        final ShopGoodInfo item = getItem(position);
        TextView title = holder.viewFinder().view(R.id.title);
        TextView commission = holder.viewFinder().view(R.id.commission);
        TextView coupon = holder.viewFinder().view(R.id.coupon);
        TextView discount_price = holder.viewFinder().view(R.id.discount_price);
        ImageView img = holder.viewFinder().view(R.id.img);

        title.setText(MathUtils.getTitle(item));
        LoadImgUtils.setImg(mContext, img, MathUtils.getPicture(item));
        if (StringsUtils.isEmpty(item.getCouponPrice())) {
            coupon.setVisibility(View.GONE);
        } else {
            coupon.setVisibility(View.VISIBLE);
            coupon.setText(getString(R.string.yuan, MathUtils.getnum(item.getCouponPrice())));
        }
        if (!StringsUtils.isEmpty(item.getCommission())) {
            commission.setText(mContext.getString(R.string.mcommission, MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), item.getCommission())));
        } else {
            commission.setVisibility(View.GONE);
        }


        discount_price.setText(MathUtils.getnum(item.getVoucherPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListType == C.GoodsListType.MATERIAL) {
                    item.material = mMaterialID;
                }
                GoodsDetailActivity.start(mContext, item);
            }
        });


    }


    public class HolderLeft extends SimpleViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.commission)
        TextView commission;
        @BindView(R.id.img)
        ImageView img;


        @BindView(R.id.coupon)
        TextView coupon;

        @BindView(R.id.discount_price)
        TextView discount_price;


        public RecommendIndexView mRecommendIndexView;

        public HolderLeft(View itemView) {
            super(itemView);
        }
    }


    @Override
    public void add(List<ShopGoodInfo> items) {
        super.add(items);
        if (type == TYPE_OFFICAL_RECOM) {

        }

    }

    @Override
    public void replace(List<ShopGoodInfo> items) {
        super.replace(items);
        if (type == TYPE_OFFICAL_RECOM) {
            slFlag.clear();
        }
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public interface OnSelectListener {
        void onSelect(View v, int position);
    }

    public OnSelectListener getmOnSelectListener() {
        return mOnSelectListener;
    }

    public void setmOnSelectListener(OnSelectListener mOnSelectListener) {
        this.mOnSelectListener = mOnSelectListener;
    }


}
