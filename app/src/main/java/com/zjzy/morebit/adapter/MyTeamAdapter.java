package com.zjzy.morebit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjzy.morebit.Module.common.Dialog.QrcodeDialog;
import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.MyTeamListFragment;
import com.zjzy.morebit.pojo.TeamInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class MyTeamAdapter extends RecyclerView.Adapter {
    public final static int teamOne = 0;
    public final static int teamTwo = 1;
    public final static int teamThree = 2;

    private LayoutInflater mInflater;
    private Context mContext;
    private List<TeamInfo> mDatas = new ArrayList<>();
//    DisplayImageOptions options;
    private String mType;
    private UserInfo userInfo;
    private QrcodeDialog mQrcodeDialog;


    public MyTeamAdapter(Context context, List<TeamInfo> datas) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        if (datas != null && datas.size() > 0) {
            mDatas.addAll(datas);
        }
//        //设置图片视图图片资源
//        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.logo)
//                .showImageForEmptyUri(R.drawable.logo)
//                .showImageOnFail(R.drawable.logo)
//                .cacheInMemory(true).cacheOnDisc(true)
//                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_myteam, parent, false));
    }

    public void setData(List<TeamInfo> data) {
        if (data != null) {
            mDatas.clear();
            mDatas.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final TeamInfo info = mDatas.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
//        ImageLoader.getInstance().displayImage(info.getHeadImg(), viewHolder.userIcon, options);

        LoadImgUtils.setImgHead(mContext,viewHolder.userIcon,info.getHeadImg());
        if(MyTeamListFragment.TYPE_DYNAMIC_RANK_ACTIVITY.equals(mType)){
            if(!TextUtils.isEmpty(info.getLastActiveTime())){
                viewHolder.createTime.setText("最后登录时间："+DateTimeUtils.ymdhmsToymd((DateTimeUtils.getDatetoString(info.getLastActiveTime()))));
            } else {
                viewHolder.createTime.setText("");
            }

        } else if(MyTeamListFragment.TYPE_DYNAMIC_RANK_COMMISSION.equals(mType)) {
            viewHolder.createTime.setText("近七日预估佣金：" +info.getSevenCommission()+"元");
        } else if(MyTeamListFragment.TYPE_DYNAMIC_RANK_NEW.equals(mType)){
            viewHolder.createTime.setText("近七日拉新：" +info.getSevenNewUsers()+"人");
        }else {
            viewHolder.createTime.setText(info.getCreateTime());
        }

        viewHolder.name.setText(info.getNickName());
        if (TextUtils.isEmpty(info.getInvitationUserName())){
            viewHolder.tv_yao.setVisibility(View.GONE);
        }else{
            viewHolder.tv_yao.setVisibility(View.VISIBLE);
        }
        viewHolder.tv_yao.setText("邀请人："+info.getInvitationUserName());
        if(C.UserType.member.equals(info.getUserType()+"")){
            viewHolder.vip_img.setImageResource(R.mipmap.vip_icon_right2);
            viewHolder.userLevel.setText("VIP");
        } else {
            viewHolder.vip_img.setImageResource(R.mipmap.vip_bg_icon);
            viewHolder.userLevel.setText("掌柜");

        }
        viewHolder.people_count.setText("粉丝数"+info.getChildCount());
        viewHolder.item_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onAdapterClickListener != null) {
                    onAdapterClickListener.onItem(position);
                }
            }
        });
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




    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView userIcon;
        TextView  userLevel, createTime, people_count,tv_remark,input_remark,name,tv_yao;
        View bottomLine;
        RelativeLayout item_ly;
        ImageView vip_img;
        LinearLayout ll_vip;

        public ViewHolder(View itemView) {
            super(itemView);
            item_ly = (RelativeLayout) itemView.findViewById(R.id.item_ly);
            userIcon = (RoundedImageView) itemView.findViewById(R.id.userIcon);

            bottomLine = (View) itemView.findViewById(R.id.bottomLine);
            userLevel = (TextView) itemView.findViewById(R.id.userLevel);
            createTime = (TextView) itemView.findViewById(R.id.createTime);
            tv_remark = (TextView) itemView.findViewById(R.id.tv_remark);
            input_remark = (TextView) itemView.findViewById(R.id.input_remark);
            people_count = (TextView) itemView.findViewById(R.id.people_count);

            name = (TextView) itemView.findViewById(R.id.name);
            vip_img=itemView.findViewById(R.id.vip_img);
            tv_yao=itemView.findViewById(R.id.tv_yao);
            ll_vip=itemView.findViewById(R.id.ll_vip);
        }
    }

    private OnAdapterClickListener onAdapterClickListener;

    public interface OnAdapterClickListener {
        public void onItem(int position);
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

    public void setType(String mType) {
        this.mType = mType;
    }
}