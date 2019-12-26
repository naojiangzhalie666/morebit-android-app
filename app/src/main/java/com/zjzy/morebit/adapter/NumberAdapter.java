package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zjzy.morebit.R;
import com.zjzy.morebit.Activity.NumberGoodsDetailsActivity;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyLog;

import java.util.List;

/**
 * Created by YangBoTian on 2018/8/22.
 */

public class NumberAdapter extends BaseMultiItemQuickAdapter<NumberGoods, NumberAdapter.NumberGoodsHolder> {
    public static final int GOODS_TYPE = 0;
    public static final int AD_TYPE = 1;
    private int mDefHeight = 200;

    private Activity mContext;
//    private   int mWindowWidth;
//    private final int mImgWidth;

    public NumberAdapter(Activity context, List<NumberGoods> numberGoods) {
        super(numberGoods);
        MyLog.i("test", "NumberAdapter");
        mContext = context;
        addItemType(GOODS_TYPE, R.layout.item_number_goods);
//        mDefHeight  = mContext.getResources().getDimensionPixelSize(R.dimen.home_recommend_img_def_height);
//        mWindowWidth = AppUtil.getWindowWidth((Activity) context);
//        mImgWidth = (mWindowWidth - (context.getResources().getDimensionPixelSize(R.dimen.home_recommend_item_decoration) * 3))/2;
    }

    @Override
    protected void convert(final NumberGoodsHolder holder, final NumberGoods goods) {
        String img = goods.getPicUrl();
        if (!TextUtils.isEmpty(img)) {
            LoadImgUtils.setImg(mContext, holder.pic, img);
        }
        holder.desc.setText(goods.getName());
        String price = goods.getRetailPrice();
        if (TextUtils.isEmpty(price)){
            holder.price.setText(mContext.getResources().getString(R.string.number_goods_price,"0"));
        }else{
            holder.price.setText(mContext.getResources().getString(R.string.number_goods_price,price));
        }
        String moreCoin = MathUtils.getMorebitCorn(price);
        holder.morebitCorn.setText(mContext.getResources().getString(R.string.number_give_more_corn,moreCoin));
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                NumberGoodsDetailsActivity.start(mContext,String.valueOf(goods.getId()));
            }
        });

    }


    public class NumberGoodsHolder extends BaseViewHolder {

        ImageView pic;


        TextView desc;

        TextView price;


        TextView morebitCorn;

        public NumberGoodsHolder(View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.number_goods_pic);
            desc = (TextView) itemView.findViewById(R.id.number_goods_desc);
            price = (TextView)itemView.findViewById(R.id.number_goods_price);
            morebitCorn = (TextView)itemView.findViewById(R.id.txt_morebit_corn);
        }
    }






}
