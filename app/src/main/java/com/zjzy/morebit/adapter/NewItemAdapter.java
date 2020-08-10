package com.zjzy.morebit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.UserZeroInfoBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;

import java.util.List;

public class NewItemAdapter extends RecyclerView.Adapter<NewItemAdapter.ViewHolder> {
    private Context context;
    private List<UserZeroInfoBean.ItemListBean> list;

    public NewItemAdapter(Context context ,List<UserZeroInfoBean.ItemListBean> itemList ) {
        this.context = context;
        this.list = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.itme_newgoods, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        LoadImgUtils.loadingCornerTop(context, holder.iv_head, list.get(position).getItemPicture(), 8);

        if (!TextUtils.isEmpty(list.get(position).getSlogan())){
                holder.tv_content.setText(list.get(position).getSlogan()+"");
        }

        if (!TextUtils.isEmpty(list.get(position).getItemVoucherPrice())){
            holder.tv_price.setText("¥ "+MathUtils.getnum(list.get(position).getItemVoucherPrice()));
        }

    }

    @Override
    public int getItemCount() {
        if (list.size() < 3) {
            return list.size();
        } else {
            return 3;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_head;
        private TextView  tv_content, tv_price;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_head = itemView.findViewById(R.id.iv_head);//主图
            tv_price = itemView.findViewById(R.id.tv_price);//价格
            tv_content = itemView.findViewById(R.id.tv_content);//标题


        }
    }
}
