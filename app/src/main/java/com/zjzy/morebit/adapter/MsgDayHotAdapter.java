package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.holder.SimpleViewHolder;
import com.zjzy.morebit.pojo.DayHotBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.ViewShowUtils;

/**
 * @Author: wuchaowen
 * @Description:
 * @Time: $date$ $time$
 **/
public class MsgDayHotAdapter extends SimpleAdapter<DayHotBean, SimpleViewHolder> {
    private static final int TIME_LINE = 0;
    private static final int CONTENT = 1;
    private static final int FOOTER = 2;
    private LayoutInflater mInflater;
    private Context mContext;

    public MsgDayHotAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        final DayHotBean item = getItem(position);
        if (getItemViewType(position) == TIME_LINE) {
            TextView timeHeaderTv = holder.viewFinder().view(R.id.msgTime);
            String currentDay = DateTimeUtils.getDatetoString(item.getSendTime());
            if (currentDay.equals("昨日")) {
                timeHeaderTv.setText(currentDay);
            } else {
                timeHeaderTv.setText(DateTimeUtils.getYMDTime(item.getSendTime()));
            }

        } else if (getItemViewType(position) == CONTENT) {
            TextView title = holder.viewFinder().view(R.id.tv_name);


            TextView tv_name = holder.viewFinder().view(R.id.title);
            tv_name.setText(item.getTitle());
            TextView contentTv = holder.viewFinder().view(R.id.content);
            contentTv.setText(item.getContent());
            ImageView iv_img = holder.viewFinder().view(R.id.iv_img);
            ImageView iv_icon = holder.viewFinder().view(R.id.iv_icon);
            TextView tv_time = holder.viewFinder().view(R.id.tv_time);
            View view_line = holder.viewFinder().view(R.id.view_line);

            if (item.getJumpLocation() == 12) {
                title.setText("活动公告");
                iv_img.setVisibility(View.GONE);
                view_line.setVisibility(View.GONE);
                iv_icon.setImageResource(R.drawable.icon_msg_notice);
            } else {
                title.setText("每日爆款");
                iv_img.setVisibility(View.VISIBLE);
                view_line.setVisibility(View.VISIBLE);
                iv_icon.setImageResource(R.drawable.icon_msg_dayhot);
            }
            tv_time.setText(DateTimeUtils.getHMSTime(item.getSendTime()));
            if (TextUtils.isEmpty(item.getItemPicture())) {
                iv_img.setImageResource(R.drawable.icon_default_750);
            } else {
                LoadImgUtils.loadingCornerBitmap(this.mContext, iv_img, item.getItemPicture(), 3);
            }
            LinearLayout itemLL = holder.viewFinder().view(R.id.itemLL);
            itemLL.setOnLongClickListener( new dayHotLongClickListener(item.getTitle() + item.getContent()));

            itemLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.getJumpLocation() == 12) {
                        ShowWebActivity.start((Activity) mContext, item.getJumpUrl(), item.getTitle());
                    } else {
                        ShopGoodInfo info = new ShopGoodInfo();
                        info.setTitle(item.getTitle());
                        info.setTaobao(item.getGoodsId());
                        GoodsDetailActivity.start(mContext, info);
                    }
                }
            });
        }
    }


    @Override
    protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        if (viewType == TIME_LINE) {
            return layoutInflater.inflate(R.layout.item_msg_dayhot_time, parent, false);
        }
//        if(viewType == FOOTER){
//            return layoutInflater.inflate(R.layout.item_msg_dayhot_footview, parent, false);
//        }
        return layoutInflater.inflate(R.layout.item_msg_dayhot, parent, false);
    }


    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getGoodsId().equals("-1")) {
            return TIME_LINE;
        }
//        }else if(getItem(position).getGoodsId().equals("-2")){
//            return FOOTER;
//        }
        return CONTENT;
    }

    private class dayHotLongClickListener implements View.OnLongClickListener {
        String text = "";

        public dayHotLongClickListener(String text) {
            this.text = text;
        }

        @Override
        public boolean onLongClick(View v) {
            if (!TextUtils.isEmpty(text)) {
                AppUtil.coayText((Activity) mContext, text);
                ViewShowUtils.showShortToast(mContext, mContext.getString(R.string.coayTextSucceed));
            }
            return true;
        }
    }
}
