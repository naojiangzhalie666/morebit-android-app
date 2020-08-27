
package com.zjzy.morebit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.WithdrawDetailBean;
import com.zjzy.morebit.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

public class BillDetailAdapter extends RecyclerView.Adapter<BillDetailAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private Context mContext;
    private List<WithdrawDetailBean> mDatas = new ArrayList<>();


    public BillDetailAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public BillDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = mInflater.inflate(R.layout.item_billdetail, parent, false);
        BillDetailAdapter.ViewHolder viewHolder = new BillDetailAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        WithdrawDetailBean info = mDatas.get(position);

        viewHolder.record.setText(info.getRemark());
        if ("1".equals(info.getType())) {  //支出
            viewHolder.account.setText(mContext.getString(R.string.commission_expend, info.getChangeAmount()));
            viewHolder.account.setTextColor(ContextCompat.getColor(mContext, R.color.color_DB493E));
            //  viewHolder.icon_type.setImageResource(R.drawable.icon_tixian);
        } else if ("2".equals(info.getType())) {  //收入
            viewHolder.account.setTextColor(ContextCompat.getColor(mContext, R.color.color_DB493E));
            // viewHolder.icon_type.setImageResource(R.drawable.icon_yong);
            viewHolder.account.setText(mContext.getString(R.string.commission_income, info.getChangeAmount()));
        }
        viewHolder.record_time.setText(DateTimeUtils.getTheMonth(info.getYearMonthDay(),DateTimeUtils.FORMAT_PATTERN_YEAR_MONTH_DAY_WITHOUT_TEXT,DateTimeUtils.FORMAT_PATTERN_MONTH_DAY_WITHOUT_TEXT)+" "+info.getHourBranchSeconds());
        viewHolder.tv_hint.setText(info.getReason());



        viewHolder.view1.setVisibility(position==0 ? View.VISIBLE :View.GONE);
        viewHolder.view.setVisibility(position==mDatas.size()-1 ? View.GONE :View.VISIBLE);
        viewHolder.view2.setVisibility(position==mDatas.size()-1  ? View.VISIBLE :View.GONE);

    }

    public void setData(List<WithdrawDetailBean> datas) {
        mDatas.clear();
        if (datas != null && datas.size() > 0) {
            mDatas.addAll(datas);

            notifyDataSetChanged();
        }
    }

    public void addData(List<WithdrawDetailBean> datas) {
        if (datas != null && datas.size() > 0) {
            mDatas.addAll(datas);
            notifyItemChanged(0,datas.size());
        }
    }




    @Override
    public int getItemCount() {

        return mDatas == null ? 0 : mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView record_time, account, record,tv_hint;

        View view,view1,view2;

        public ViewHolder(View itemView) {
            super(itemView);
            record_time = (TextView) itemView.findViewById(R.id.record_time);
            account = (TextView) itemView.findViewById(R.id.getComMoney);
            record = (TextView) itemView.findViewById(R.id.name);
            tv_hint = (TextView) itemView.findViewById(R.id.tv_hint);
            view=itemView.findViewById(R.id.view);
            view1=itemView.findViewById(R.id.view1);
            view2=itemView.findViewById(R.id.view2);
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