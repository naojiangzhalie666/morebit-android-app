
package com.zjzy.morebit.info.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.SimpleAdapter;
import com.zjzy.morebit.adapter.holder.SimpleViewHolder;
import com.zjzy.morebit.pojo.EarningsMsg;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;

/**
 * 消费佣金订单明细
 */
public class MsgEarningsAdapter extends SimpleAdapter<EarningsMsg, SimpleViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private int type;


    public MsgEarningsAdapter(Context context, int mtype) {
        super(context);
        this.mContext = context;
        type=mtype;
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        final EarningsMsg item = getItem(position);
        if (item == null) return;

        TextView tv_name = holder.viewFinder().view(R.id.tv_name);
        TextView tv_order_sn = holder.viewFinder().view(R.id.tv_order_sn);
        TextView tv_order_type = holder.viewFinder().view(R.id.tv_order_type);
        TextView tv_order_money = holder.viewFinder().view(R.id.tv_order_money);
        TextView earnings = holder.viewFinder().view(R.id.earnings);
        TextView tv_data = holder.viewFinder().view(R.id.tv_data);
        if (TextUtils.isEmpty(item.getCommission()) || TextUtils.isEmpty(item.getOrderSource()) || TextUtils.isEmpty(item.getPayPrice())) {
            earnings.setVisibility(View.GONE);
            tv_order_type.setVisibility(View.GONE);
            tv_order_money.setVisibility(View.GONE);
        } else {
            earnings.setVisibility(View.VISIBLE);
            tv_order_type.setVisibility(View.VISIBLE);
            tv_order_money.setVisibility(View.VISIBLE);
            tv_order_type.setText(getString(R.string.order_type, item.getOrderSource()));
            String totalIncome = MathUtils.getTotleSubSidies(item.getCommission(), item.getSubsidy());
            if (type==3){
                tv_order_money.setText("失效金额："+item.getPayPrice()+"元");

                earnings.setText("失效佣金："+totalIncome+"元");
            }else{
                if (type==2){
                    tv_order_money.setText("付款金额："+item.getPayPrice()+"元");
                    earnings.setVisibility(View.GONE);
                }else{
                    earnings.setVisibility(View.VISIBLE);
                    tv_order_money.setText("付款金额："+item.getPayPrice()+"元");
                    earnings.setText("赚："+totalIncome+"元");
                }
            }

        }
        tv_order_sn.setText(getString(R.string.order_sn, item.getOrderId()));
        tv_name.setText(item.getTitle());
        tv_data.setText(item.getCreateTime());
    }


    @Override
    protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        return layoutInflater.inflate(R.layout.item_msg_notice, parent, false);
    }

    public static class TimeViewHolder extends SimpleViewHolder {
        TextView time;

        public TimeViewHolder(View itemView) {
            super(itemView);
        }
    }
}