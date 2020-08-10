package com.zjzy.morebit.Module.common.Dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.FansInfo;
import com.zjzy.morebit.pojo.TeamInfo;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;


/**
 * 我的团队中二维码弹窗
 */

public class QrcodeDialog extends AlertDialog {


    private Context mContext;

    private TextView tv_level;
    private RoundedImageView iv_head;
    private TextView tv_name;
    private TextView invitation_code;
    private TextView wx_code;
    private TextView fans;
    private TextView register_time;
    private TextView last_month_jiesuan;
    private TextView total_earnings;
    private TextView copy_wx_code;
    private TextView copy_invitation_code;
    private String mInvitationCode = "";
    private String mWxCode = "";
    private LinearLayout ll_vip;
    private TextView userLevel;
    private ImageView vip_img;

    public QrcodeDialog(Context context) {
        super(context, R.style.dialog);
        this.mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_dialog);
        setCanceledOnTouchOutside(true);
        initView();
    }

    private void initView() {
        vip_img = findViewById(R.id.vip_img);
        userLevel = findViewById(R.id.userLevel);
        ll_vip = findViewById(R.id.ll_vip);
        iv_head = findViewById(R.id.iv_head);
        tv_name = findViewById(R.id.name);
        wx_code = findViewById(R.id.wx_code);
        copy_wx_code = findViewById(R.id.copy_wx_code);
        copy_invitation_code = findViewById(R.id.copy_invitation_code);
        fans = findViewById(R.id.fans);
        total_earnings = findViewById(R.id.total_earnings);
        last_month_jiesuan = findViewById(R.id.last_month_jiesuan);
        register_time = findViewById(R.id.register_time);
        invitation_code = findViewById(R.id.invitation_code);
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        copy_wx_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUtil.coayText((Activity) mContext, mWxCode);
                Toast.makeText(mContext, "已复制到粘贴版", Toast.LENGTH_SHORT).show();
            }
        });
        copy_invitation_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.coayText((Activity) mContext, mInvitationCode);
                Toast.makeText(mContext, "已复制到粘贴版", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refreshData(TeamInfo teamIfo, FansInfo fansInfo) {
        if (teamIfo != null) {
            if (teamIfo.getUserType() == 0) {//vip
                vip_img.setImageResource(R.mipmap.vip_icon_right2);
                userLevel.setText("VIP");
                ll_vip.setBackgroundResource(R.drawable.bg_e9c8a7_round_9dp);
                userLevel.setTextColor(Color.parseColor("#A8947A"));
            } else {//掌柜黄金
                vip_img.setImageResource(R.mipmap.vip_bg_icon);
                userLevel.setText("掌柜(黄金)");
                ll_vip.setBackgroundResource(R.drawable.bg_vip_round_9dp);
                userLevel.setTextColor(Color.parseColor("#FCAF00"));
            }


            LoadImgUtils.setImgCircle(mContext, iv_head, teamIfo.getHeadImg(),R.drawable.head_icon);
            if (TextUtils.isEmpty(teamIfo.getNickName())) {
                tv_name.setText("暂未填写");
            } else {
                tv_name.setText(teamIfo.getNickName());
            }

            fans.setText(teamIfo.getChildCount() + "");
            register_time.setText("注册时间：" + teamIfo.getCreateTime());
            if (TextUtils.isEmpty(teamIfo.getWxNumber())) {
                wx_code.setText("微信号：未填写");
                copy_wx_code.setVisibility(View.INVISIBLE);
            } else {
                mWxCode = teamIfo.getWxNumber();
                wx_code.setText("微信号：" + teamIfo.getWxNumber());
                copy_wx_code.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(teamIfo.getInviteCode())) {
                invitation_code.setText("邀请码：未填写");
                copy_invitation_code.setVisibility(View.INVISIBLE);
            } else {
                mInvitationCode = teamIfo.getInviteCode();
                invitation_code.setText("邀请码：" + teamIfo.getInviteCode());
                copy_invitation_code.setVisibility(View.VISIBLE);
            }
        }
        if (fansInfo != null) {
            last_month_jiesuan.setText(MathUtils.getMoney(fansInfo.getPrevMonthEstimateMoney()) + "元");
            total_earnings.setText(MathUtils.getMoney(fansInfo.getAccumulatedAmount()) + "元");
        }
    }


}