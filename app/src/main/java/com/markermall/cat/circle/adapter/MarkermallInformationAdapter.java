package com.markermall.cat.circle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.markermall.cat.R;
import com.markermall.cat.adapter.SimpleAdapter;
import com.markermall.cat.adapter.holder.SimpleViewHolder;
import com.markermall.cat.pojo.MarkermallInformation;

import java.util.List;

/**
 * @author YangBoTian
 * @date 2019/9/4
 * @des
 */
public class MarkermallInformationAdapter extends SimpleAdapter<MarkermallInformation, SimpleViewHolder> {
    private Context mContext;
    OnClickListenter onClickListenter;
    List<MarkermallInformation> mList;
    public MarkermallInformationAdapter(Context context, List<MarkermallInformation> list) {
        super(context,list);
        mContext = context;
        mList = list;
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, final int position) {
        final MarkermallInformation item = getItem(position);
        TextView tv_title = holder.viewFinder().view(R.id.tv_title);
        TextView tv_time = holder.viewFinder().view(R.id.tv_time);
        final TextView tv_read = holder.viewFinder().view(R.id.tv_read);
        TextView tv_share_sum = holder.viewFinder().view(R.id.tv_share_sum);
        ImageView iv_new = holder.viewFinder().view(R.id.iv_new);
        int read = item.getVirtualRead()+item.getReadNum();
        tv_read.setText(getString(R.string.mi_yuan_information_read,read));
        tv_title.setText(item.getTitle());
        tv_time.setText(item.getCreateTime());
        int share = item.getVirtualShare()+item.getShareNum();
        tv_share_sum.setText(share+"");
        if(position<2){
            iv_new.setVisibility(View.VISIBLE);
        } else {
            iv_new.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListenter!=null){
                    onClickListenter.onClick(v,position);
                }

            }
        });
    }

    @Override
    protected View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        return layoutInflater.inflate(R.layout.item_mi_yuan_information_recycle, parent, false);
    }


    public interface OnClickListenter{
        void onClick(View v,int position);
    }

    public void setOnClickListenter(OnClickListenter onClickListenter) {
        this.onClickListenter = onClickListenter;
    }
}
