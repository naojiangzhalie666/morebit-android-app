
package com.zjzy.morebit.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.AgentDetailList;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

public class ConsComDetailAdapter extends RecyclerView.Adapter {


    private LayoutInflater mInflater;
    private Context mContext;
    private List<AgentDetailList> mDatas = new ArrayList<>();


    public ConsComDetailAdapter(Context context, List<AgentDetailList> datas) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        if (datas != null && datas.size() > 0) {
            mDatas.addAll(datas);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==1){
            return new TimeViewHolder(mInflater.inflate(R.layout.item_conscomdetail_time, parent, false));
        }
        return new ViewHolder(mInflater.inflate(R.layout.item_conscomdetail, parent, false));
    }

    public void setData(List<AgentDetailList> datas) {
        mDatas.clear();
        if (datas != null && datas.size() > 0) {
            mDatas.addAll(datas);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mDatas.get(position).getViewType()==1){
            return 1;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AgentDetailList info = mDatas.get(position);
        if(holder instanceof  TimeViewHolder){
            TimeViewHolder viewHolder = (TimeViewHolder) holder;
            MyLog.i("test","info.getRecordTime(): " +info.getYearMonthDay());
            viewHolder.time.setText(info.getYearMonthDay());
        } else {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.record.setText(info.getRemark());
            if (info.getType() == 1) {  //支出
                viewHolder.account.setText(mContext.getString(R.string.commission_expend, info.getChangeAmount()));
                viewHolder.account.setTextColor(ContextCompat.getColor(mContext, R.color.color_DB493E));
              //  viewHolder.icon_type.setImageResource(R.drawable.icon_tixian);
            } else if (info.getType() == 2) {  //收入
                viewHolder.account.setTextColor(ContextCompat.getColor(mContext, R.color.color_DB493E));
               // viewHolder.icon_type.setImageResource(R.drawable.icon_yong);
                viewHolder.account.setText(mContext.getString(R.string.commission_income, info.getChangeAmount()));
            }
            viewHolder.record_time.setText(DateTimeUtils.getTheMonth(info.getYearMonthDay(),DateTimeUtils.FORMAT_PATTERN_YEAR_MONTH_DAY_WITHOUT_TEXT,DateTimeUtils.FORMAT_PATTERN_MONTH_DAY_WITHOUT_TEXT)+" "+info.getHourBranchSeconds());
           viewHolder.tv_hint.setText(info.getReason());

               if (mDatas.size()==0||position==mDatas.size()-1){
                   viewHolder.view.setVisibility(View.GONE);
               }


        }

    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView record_time, account, record,tv_hint;
        ImageView icon_type;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            record_time = (TextView) itemView.findViewById(R.id.record_time);
            account = (TextView) itemView.findViewById(R.id.getComMoney);
            record = (TextView) itemView.findViewById(R.id.name);
            tv_hint = (TextView) itemView.findViewById(R.id.tv_hint);
            icon_type = itemView.findViewById(R.id.icon_type);
            view=itemView.findViewById(R.id.view);
        }
    }

    private class TimeViewHolder extends RecyclerView.ViewHolder {
        TextView time;


        public TimeViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.tv_time);
        }
    }
    private OnAdapterClickListener onAdapterClickListener;

    public interface OnAdapterClickListener {
        public void onItem(int position);
    }

    public void setOnAdapterClickListener(OnAdapterClickListener listener) {
        onAdapterClickListener = listener;
    }
}