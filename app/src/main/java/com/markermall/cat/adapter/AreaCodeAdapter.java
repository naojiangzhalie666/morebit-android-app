package com.markermall.cat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.markermall.cat.R;
import com.markermall.cat.pojo.AreaCodeBean;
import com.markermall.cat.pojo.AreaCodeItem;

import java.util.ArrayList;
import java.util.List;


public class AreaCodeAdapter  extends RecyclerView.Adapter<AreaCodeAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<AreaCodeItem> mDatas = new ArrayList<>();


    public AreaCodeAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_area_code, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final AreaCodeBean areaCodeBean = mDatas.get(position).data;
         if(position == mDatas.size()-1){
             holder.line.setVisibility(View.GONE);
         }else{
             AreaCodeBean nextAreaCodeBean = mDatas.get(position+1).data;
             if(nextAreaCodeBean.getPrefix().equals(areaCodeBean.getPrefix())){
                 holder.line.setVisibility(View.VISIBLE);
             }else{
                 holder.line.setVisibility(View.GONE);
             }
         }



        holder.itemView.setTag(mDatas.get(position));
        holder.textArea.setText(areaCodeBean.getCountry());
        holder.textCode.setText("+"+areaCodeBean.getAreaCode());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != onAdapterClickListener){
                    onAdapterClickListener.onItem(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textArea;
        TextView textCode;
        View line;

        ViewHolder(View itemView) {
            super(itemView);
            textArea = itemView.findViewById(R.id.text_area);
            textCode = itemView.findViewById(R.id.text_code);
            line = itemView.findViewById(R.id.line);
        }
    }


    public void updateData(List<AreaCodeItem> data) {
        if (null == data) {
            return;
        }

        mDatas.clear();
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    public List<AreaCodeItem> getDatas(){
        return mDatas;
    }


    private OnAdapterClickListener onAdapterClickListener;

    public interface OnAdapterClickListener {
        public void onItem(int position);
    }

    public void setOnAdapterClickListener(OnAdapterClickListener listener) {
        onAdapterClickListener = listener;
    }
}
