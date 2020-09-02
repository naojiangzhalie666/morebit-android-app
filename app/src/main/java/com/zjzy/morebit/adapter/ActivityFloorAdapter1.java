package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.jdsjlzx.ItemDecoration.SpaceItemDecoration;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.FloorBean2;
import com.zjzy.morebit.pojo.FloorChildInfo;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description: 二级模块
 **/
public class ActivityFloorAdapter1 extends RecyclerView.Adapter{
    public static final int DISPLAY_HORIZONTAL = 0; //横向
    private LayoutInflater mInflater;
    private List<FloorBean2.ListDataBean> mDatas = new ArrayList<>();
    private Context mContext;
    private static final int FristType = 7;
    private static final int TwoType = 5;
    private static final int ThreeType = 6;
    private String ext;
    private boolean iszhu=false;

    public ActivityFloorAdapter1(Context context, List<FloorBean2.ListDataBean> listData, String ext) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mDatas=listData;
        this.ext=ext;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = this.mDatas.get(position).getShowType();
        switch (viewType) {
            case FristType:
                viewType = FristType;
                break;
            case TwoType:
                viewType = TwoType;
                break;
            case ThreeType:
                viewType = ThreeType;
                break;
        }
        return viewType;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;
        if (viewType == FristType) {
        view = mInflater.inflate(R.layout.item_vipfloor_tou1, viewGroup, false);
        return new  ViewHolder1(view);
        } else if (viewType==TwoType){
              view = mInflater.inflate(R.layout.item_vipfloor_horizontal, viewGroup, false);
            return new ViewHolder2(view);
        } else if (viewType==ThreeType){
            view = mInflater.inflate(R.layout.item_vipfloor_verlayout, viewGroup, false);
            return new ViewHolder3(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (mDatas==null)return;
        List<FloorBean2.ListDataBean.ChildBean> child = mDatas.get(position).getChild();
        int viewType = getItemViewType(position);


       if (viewType==FristType){
           final FloorChildInfo floorChildInfo=new FloorChildInfo();
           Log.e("sfsdf",floorChildInfo+"");
           ViewHolder1 holder1= (ViewHolder1) holder;

           if (child==null ){
               holder1.imageView.setVisibility(View.GONE);
               iszhu=true;
           }else{
               iszhu=false;
               holder1.imageView.setVisibility(View.VISIBLE);
               floorChildInfo.setId(child.get(position).getId());
               floorChildInfo.setOpen(child.get(position).getOpen());
               floorChildInfo.setMainTitle(child.get(position).getMainTitle());
               floorChildInfo.setClassId(child.get(position).getClassId());
               floorChildInfo.setUrl(child.get(position).getUrl());
               floorChildInfo.setSubTitle(child.get(position).getSubTitle());

               LoadImgUtils.setImg(mContext,holder1.imageView,child.get(position).getPicture());
               holder1.imageView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       SensorsDataUtil.getInstance().advClickTrack(floorChildInfo.getId()+"",floorChildInfo.getOpen()+"", "楼层管理"+position, position,floorChildInfo.getMainTitle(), floorChildInfo.getClassId()+"", floorChildInfo.getUrl(), floorChildInfo.getSubTitle());
                       BannerInitiateUtils.gotoAction((Activity) mContext, MyGsonUtils.toImageInfo(floorChildInfo));
                   }
               });

           }

       }else if (viewType==TwoType){
           if (mDatas.get(position).getChild()==null)return;
           ViewHolder2 holder2= (ViewHolder2) holder;
           if (iszhu){
               holder2.ll1.setVisibility(View.GONE);
           }else{
               holder2.ll1.setVisibility(View.VISIBLE);
               GridLayoutManager manager3 = new GridLayoutManager(mContext, 2);
               holder2.rcy1.setLayoutManager(manager3);
               if (holder2.rcy1.getItemDecorationCount() == 0) {//防止每一次刷新recyclerview都会使间隔增大一倍 重复调用addItemDecoration方法
                   holder2.rcy1.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(mContext, 3)));
               }

               if (ext.length()<=7){
                   holder2.ll1.setBackgroundColor(Color.parseColor(ext));
               }
               FloorAdapter2 adapter2=new FloorAdapter2(mContext,mDatas.get(position).getChild());
               holder2.rcy1.setAdapter(adapter2);
           }


       }else if (viewType==ThreeType){
           if (mDatas.get(position).getChild()==null)return;
           ViewHolder3 holder3= (ViewHolder3) holder;
           if (iszhu){
               holder3.ll2.setVisibility(View.GONE);
           }else {
               holder3.ll2.setVisibility(View.VISIBLE);
               GridLayoutManager manager3 = new GridLayoutManager(mContext, 4);
               holder3.rcy2.setLayoutManager(manager3);
               if (holder3.rcy2.getItemDecorationCount() == 0) {//防止每一次刷新recyclerview都会使间隔增大一倍 重复调用addItemDecoration方法
                   holder3.rcy2.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(mContext, 3)));
               }
               if (ext.length()<=7){
                   holder3.ll2.setBackgroundColor(Color.parseColor(ext));
               }
               FloorAdapter3 adapter3 = new FloorAdapter3(mContext, mDatas.get(position).getChild());
               holder3.rcy2.setAdapter(adapter3);
           }

       }
    }

//    @Override
//    public void onBindViewHolder(ActivityFloorAdapter1.ViewHolder holder, final int position) {
//        final ImageInfo imageInfo = mDatas.get(position);
//        ActivityFloorAdapter1.ViewHolder viewHolder = (ActivityFloorAdapter1.ViewHolder) holder;
//        if (!TextUtils.isEmpty(imageInfo.getPicture()) && imageInfo.getId()!=-1) {
//            viewHolder.imageView.setVisibility(View.VISIBLE);
//            LoadImgUtils.loadingCornerBitmap(mContext, viewHolder.imageView, imageInfo.getPicture(),8);
//        }else{
//            viewHolder.imageView.setVisibility(View.GONE);
//        }
//        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(-1 != imageInfo.getId()){
//                    SensorsDataUtil.getInstance().advClickTrack(imageInfo.getId()+"",imageInfo.getOpen()+"", "", position,imageInfo.getTitle(), imageInfo.getClassId()+"", imageInfo.getUrl(), imageInfo.getSubTitle());
//                    BannerInitiateUtils.gotoAction((Activity) mContext,imageInfo);
//                }
//            }
//        });
//    }

    @Override
    public int getItemCount() {

        Log.e("sfsf",mDatas.size()+"");
        return mDatas==null ? 0:mDatas.size();

    }


    public class ViewHolder1 extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public ViewHolder1(View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.image);
        }
    }



    public class ViewHolder2 extends RecyclerView.ViewHolder {
        private RecyclerView rcy1;
        private LinearLayout ll1;
        public ViewHolder2(View itemView) {
            super(itemView);
            rcy1= itemView.findViewById(R.id.rcy1);
            ll1=itemView.findViewById(R.id.ll1);
        }
    }
    public class ViewHolder3 extends RecyclerView.ViewHolder {
        private RecyclerView rcy2;
        private LinearLayout ll2;
        public ViewHolder3(View itemView) {
            super(itemView);
            rcy2= itemView.findViewById(R.id.rcy2);
            ll2=itemView.findViewById(R.id.ll2);
        }
    }
}
