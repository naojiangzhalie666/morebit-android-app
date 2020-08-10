package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.Module.common.Dialog.QrcodeDialog;
import com.zjzy.morebit.R;

import com.zjzy.morebit.pojo.TeamInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;

import java.util.ArrayList;
import java.util.List;

public class MyFansAdapter extends RecyclerView.Adapter<MyFansAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private Context mContext;
    private List<TeamInfo> mDatas = new ArrayList<>();
//    DisplayImageOptions options;
    private int  mType;
    private UserInfo userInfo;
    private QrcodeDialog mQrcodeDialog;



    public MyFansAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;

//        //设置图片视图图片资源
//        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.logo)
//                .showImageForEmptyUri(R.drawable.logo)
//                .showImageOnFail(R.drawable.logo)
//                .cacheInMemory(true).cacheOnDisc(true)
//                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_myfans, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final TeamInfo info = mDatas.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        LoadImgUtils.setImgHead(mContext,viewHolder.userIcon,info.getHeadImg());
        viewHolder.name.setText(info.getNickName());
        if (mType==4){//总预估
            viewHolder.people_count.setVisibility(View.GONE);
            viewHolder.ll_la.setVisibility(View.VISIBLE);
            viewHolder.createTime.setVisibility(View.GONE);
            viewHolder.tv_title.setText("总预估佣金：");
            viewHolder.tv_content.setText("¥"+info.getSevenCommission());
        }else if (mType==1){//活跃
            viewHolder.people_count.setVisibility(View.VISIBLE);
            viewHolder.ll_la.setVisibility(View.GONE);
            viewHolder.createTime.setVisibility(View.VISIBLE);
            String time = DateTimeUtils.ymdhmsToymd(info.getCreateTime());
            Log.e("sfsfdsf",time+"");
            viewHolder.createTime.setText(DateTimeUtils.getShortTime3(info.getLastActiveTime())+ "");
        }else if (mType==3){//七日
            viewHolder.people_count.setVisibility(View.GONE);
            viewHolder.ll_la.setVisibility(View.VISIBLE);
            viewHolder.createTime.setVisibility(View.GONE);
            viewHolder.tv_title.setText("近七日拉新：");
            viewHolder.tv_content.setText(info.getSevenNewUsers()+"人");
        }else if (mType==2){//近30日
            viewHolder.people_count.setVisibility(View.GONE);
            viewHolder.ll_la.setVisibility(View.VISIBLE);
            viewHolder.createTime.setVisibility(View.GONE);
            viewHolder.tv_title.setText("近30日预估佣金：");
            viewHolder.tv_content.setText("¥"+info.getSevenCommission());
        }

        viewHolder.input_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAdapterClickListener != null) {
                    onAdapterClickListener.onRemark(position);
                }
            }
        });
        if(TextUtils.isEmpty(info.getRemark())){
            viewHolder.input_remark.setText("填写备注");
            viewHolder.input_remark.setTextColor(Color.WHITE);
            viewHolder.input_remark.setBackgroundResource(R.drawable.item_fans_bg);
            viewHolder.tv_remark.setText(mContext.getString(R.string.fans_remark,"未填写"));
        } else {
            viewHolder.input_remark.setText("修改备注");
            viewHolder.input_remark.setTextColor(Color.parseColor("#F05557"));
            viewHolder.input_remark.setBackgroundResource(R.mipmap.bg_item_fans_remark);
            viewHolder.tv_remark.setText(mContext.getString(R.string.fans_remark,info.getRemark()));
        }

        if ("0".equals(info.getFsType())){
            viewHolder.tv_zhuan.setText("专属");
            viewHolder.tv_zhuan.setBackgroundResource(R.drawable.bg_fans_ff5b4d_4dp);
        }else{
            viewHolder.tv_zhuan.setText("普通");
            viewHolder.tv_zhuan.setBackgroundResource(R.drawable.bg_fans_d8d8d8_4dp);
        }

        int userType = info.getUserType();
        if (userType==0){
            viewHolder.vip_img2.setImageResource(R.mipmap.vip_icon_right2);
        }else{
            viewHolder.vip_img2.setImageResource(R.mipmap.vip_bg_icon);
        }
        viewHolder.people_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (!TextUtils.isEmpty(info.getUserH5Url())){
                ShowWebActivity.start((Activity) mContext,info.getUserH5Url(),"");
            }
            }
        });

    }

    public void setData(List<TeamInfo> data,int type) {
        if (data != null) {
            mDatas.clear();
            mDatas.addAll(data);
            mType=type;

           notifyDataSetChanged();
        }
    }


    public void addData(List<TeamInfo> data) {
        if (data != null) {
            mDatas.addAll(data);
            notifyItemRangeChanged(0, data.size());
        }
    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

//        ImageLoader.getInstance().displayImage(info.getHeadImg(), viewHolder.userIcon, options);
//        viewHolder.phone.setText(LoginUtil.hideNumber(info.getPhone()));

//        if(MyTeamListFragment.TYPE_DYNAMIC_RANK_ACTIVITY.equals(mType)){
//            if(!TextUtils.isEmpty(info.getLastActiveTime())){
//                viewHolder.createTime.setText("最后登录时间："+DateTimeUtils.getTheMonth((DateTimeUtils.getDatetoString(info.getLastActiveTime()))));
//            } else {
//                viewHolder.createTime.setText("");
//            }
//
//        } else if(MyTeamListFragment.TYPE_DYNAMIC_RANK_COMMISSION.equals(mType)) {
//            viewHolder.createTime.setText("近七日预估佣金：" +info.getSevenCommission()+"元");
//        } else if(MyTeamListFragment.TYPE_DYNAMIC_RANK_NEW.equals(mType)){
//            viewHolder.createTime.setText("近七日拉新：" +info.getSevenNewUsers()+"人");
//        }else {
//            viewHolder.createTime.setText(info.getCreateTime());
//        }


//        if (TextUtils.isEmpty(info.getInvitationUserName())){
//            viewHolder.tv_yao.setVisibility(View.GONE);
//        }else{
//            viewHolder.tv_yao.setVisibility(View.VISIBLE);
//        }
//        viewHolder.tv_yao.setText("邀请人："+info.getInvitationUserName());
//        if(C.UserType.member.equals(info.getUserType()+"")){
//            viewHolder.vip_img.setImageResource(R.mipmap.vip_icon_right);
//            viewHolder.userLevel.setText("普通会员");
//        } else {
//            viewHolder.vip_img.setImageResource(R.mipmap.vip_bg_icon2);
//            viewHolder.userLevel.setText("VIP");
//        }
//        viewHolder.people_count.setText("粉丝数"+info.getChildCount());
//        viewHolder.item_ly.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (onAdapterClickListener != null) {
//                    onAdapterClickListener.onItem(position);
//                }
//            }
//        });
//        viewHolder.input_remark.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onAdapterClickListener != null) {
//                    onAdapterClickListener.onRemark(position);
//                }
//            }
//        });
//        if(TextUtils.isEmpty(info.getRemark())){
//            viewHolder.input_remark.setText("填写备注");
//            viewHolder.input_remark.setTextColor(Color.WHITE);
//            viewHolder.input_remark.setBackgroundResource(R.drawable.item_fans_bg);
//            viewHolder.tv_remark.setText(mContext.getString(R.string.fans_remark,"未填写"));
//        } else {
//            viewHolder.input_remark.setText("修改备注");
//            viewHolder.input_remark.setTextColor(Color.parseColor("#F05557"));
//            viewHolder.input_remark.setBackgroundResource(R.mipmap.bg_item_fans_remark);
//            viewHolder.tv_remark.setText(mContext.getString(R.string.fans_remark,info.getRemark()));
//        }
//
//        if (TextUtils.isEmpty(info.getSpecialId())){
//            viewHolder.authorization.setVisibility(View.GONE);
//        }else{
//            viewHolder.authorization.setVisibility(View.VISIBLE);
//        }


//    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView userIcon;
        TextView people_count, userLevel, createTime, tv_content,tv_remark,input_remark,tv_title,name,tv_yao,tv_zhuan;
        View bottomLine;
        RelativeLayout item_ly;
        ImageView vip_img2;
        private LinearLayout ll_la;

        public ViewHolder(View itemView) {
            super(itemView);
            item_ly = (RelativeLayout) itemView.findViewById(R.id.item_ly);
            userIcon = (RoundedImageView) itemView.findViewById(R.id.userIcon);
//
            people_count = (TextView) itemView.findViewById(R.id.people_count);
//            bottomLine = (View) itemView.findViewById(R.id.bottomLine);
//            userLevel = (TextView) itemView.findViewById(R.id.userLevel);
            createTime = (TextView) itemView.findViewById(R.id.createTime);
             tv_remark = (TextView) itemView.findViewById(R.id.tv_remark);
             input_remark = (TextView) itemView.findViewById(R.id.input_remark);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
           tv_title=itemView.findViewById(R.id.tv_title);
            name = (TextView) itemView.findViewById(R.id.name);
            tv_zhuan=itemView.findViewById(R.id.tv_zhuan);
             vip_img2=itemView.findViewById(R.id.vip_img2);
            ll_la=itemView.findViewById(R.id.ll_la);
        }
    }

    private OnAdapterClickListener onAdapterClickListener;

    public interface OnAdapterClickListener {
        public void onRemark(int position);
    }

    public void setOnAdapterClickListener(OnAdapterClickListener listener) {
        onAdapterClickListener = listener;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

   // public void setType(String mType) {
//        this.mType = mType;
//    }
}