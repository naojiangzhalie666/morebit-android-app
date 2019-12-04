
package com.zjzy.morebit.info.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.SimpleAdapter;
import com.zjzy.morebit.adapter.holder.SimpleViewHolder;
import com.zjzy.morebit.pojo.EarningsMsg;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.MathUtils;

/**
 * 消费佣金订单明细
 */
public class MsgEarningsAdapter extends SimpleAdapter<EarningsMsg, SimpleViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;

    public MsgEarningsAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(getItem(position).getViewType()==EarningsMsg.TWO_TYPE){
            return EarningsMsg.TWO_TYPE;
        }

        return EarningsMsg.ONE_TYPE;
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        final EarningsMsg item = getItem(position);
        if (item == null)return;
        if(holder instanceof TimeViewHolder){
            TextView time = holder.viewFinder().view(R.id.time);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) time.getLayoutParams();
            layoutParams.setMargins(DensityUtil.dip2px(mContext,(float)12),DensityUtil.dip2px(mContext,(float)11),0,DensityUtil.dip2px(mContext,(float)4));
            time.setLayoutParams(layoutParams);
            String currentDay = DateTimeUtils.getDatetoString(item.getCreateTime());
            if(currentDay.equals("昨日")){
                time.setText(currentDay);
            }else{
                time.setText(DateTimeUtils.getYMDTime(item.getCreateTime()));
            }

        } else {
            TextView tv_content = holder.viewFinder().view(R.id.tv_content);
            TextView tv_time = holder.viewFinder().view(R.id.tv_time);
            TextView tv_name = holder.viewFinder().view(R.id.tv_name);
            TextView tv_order_sn = holder.viewFinder().view(R.id.tv_order_sn);
            TextView tv_order_type = holder.viewFinder().view(R.id.tv_order_type);
            TextView tv_order_money = holder.viewFinder().view(R.id.tv_order_money);
            TextView tv_order_subsidy_money = holder.viewFinder().view(R.id.tv_order_subsidy_money);
            TextView earnings = holder.viewFinder().view(R.id.earnings);
            TextView tv_order_commission = holder.viewFinder().view(R.id.tv_order_comission);
            if(!TextUtils.isEmpty(item.getSubsidy())){
                tv_order_commission.setVisibility(View.VISIBLE);
                tv_order_subsidy_money.setVisibility(View.VISIBLE);
                tv_order_subsidy_money.setText(getString(R.string.order_subsidy_money,item.getSubsidy()));
            }else{
                tv_order_commission.setVisibility(View.GONE);
                tv_order_subsidy_money.setText("");
                tv_order_subsidy_money.setVisibility(View.GONE);
            }
            if(TextUtils.isEmpty(item.getCommission())||TextUtils.isEmpty(item.getOrderSource())||TextUtils.isEmpty(item.getPayPrice())){
                earnings.setVisibility(View.GONE);
                tv_order_type.setVisibility(View.GONE);
                tv_order_money.setVisibility(View.GONE);
            } else {
                earnings.setVisibility(View.VISIBLE);
                tv_order_type.setVisibility(View.VISIBLE);
                tv_order_money.setVisibility(View.VISIBLE);
                tv_order_type.setText(getString(R.string.order_type,item.getOrderSource()));
                tv_order_money.setText(getString(R.string.order_money,item.getPayPrice()));
                tv_order_commission.setText(getString(R.string.order_commission,item.getCommission()));
                String totalIncome = MathUtils.getTotleSubSidies(item.getCommission(),item.getSubsidy());
                earnings.setText(getString(R.string.order_earnings,totalIncome));
            }
            tv_order_sn.setText(getString(R.string.order_sn,item.getOrderId()));
            tv_time.setText(DateTimeUtils.getHMSTime(item.getCreateTime()));
            tv_name.setText(item.getTitle());
            tv_content.setText(item.getMessage());
        }

    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == EarningsMsg.TWO_TYPE)
            return new TimeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_time, parent, false));
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        return layoutInflater.inflate(R.layout.item_msg_notice, parent, false);
    }

    public static class  TimeViewHolder extends SimpleViewHolder{
          TextView time;

        public TimeViewHolder(View itemView) {
            super(itemView);
        }
    }
}