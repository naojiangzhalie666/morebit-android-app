package com.zjzy.morebit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.utils.StringsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description: 二级模块
 **/
public class SelectGoodsAdapter extends RecyclerView.Adapter{
    public static final int DISPLAY_HORIZONTAL = 0; //横向
    private LayoutInflater mInflater;
    private List<ImageInfo> mDatas = new ArrayList<>();
    private Context mContext;
    private static final int FristType = 0;
    private static final int TwoType = 1;

    public SelectGoodsAdapter(Context context/*, List<ImageInfo> data*/) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
       // this.mDatas=data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == FristType) {
              view = mInflater.inflate(R.layout.item_selectgoods, parent, false);
             return new ViewHolder1(view);
        } else if (viewType==TwoType){
              view = mInflater.inflate(R.layout.item_selectgoods2, parent, false);
            return new ViewHolder2(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder1){
            ViewHolder1 holder1= (ViewHolder1) holder;
            StringsUtils.retractTitle(holder1.tv_icon, holder1.tv_title, "特仑苏牛奶大减价了 快来买啊实践活动放松放松房间里刷卡缴费卡洛斯");
        }else{
            ViewHolder2 holder2= (ViewHolder2) holder;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if("0".equals(FristType)){
            return FristType;
        }else {
            return FristType;
        }
    }





    @Override
    public int getItemCount() {
//        if (mDatas.size()<2){
//            return mDatas.size();
//        }else{
//            return 2;
//        }
        return 20;

    }


    public class ViewHolder1 extends RecyclerView.ViewHolder {
        private TextView tv_icon,tv_title;
        private ImageView img;
        public ViewHolder1(View itemView) {
            super(itemView);
            tv_icon= itemView.findViewById(R.id.tv_icon);
            img= itemView.findViewById(R.id.img);
            tv_title= itemView.findViewById(R.id.tv_title);
        }
    }



    public class ViewHolder2 extends RecyclerView.ViewHolder {
        private ImageView img2;
        public ViewHolder2(View itemView) {
            super(itemView);

            img2= itemView.findViewById(R.id.img2);

        }
    }
}
