package com.zjzy.morebit.Module.common.Dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private ImageView iv_head;
    private TextView tv_name;
    private TextView invitation_code;
    private TextView wx_code;
    private TextView fans;
    private TextView register_time;
    private TextView last_month_jiesuan;
    private TextView total_earnings;
    private TextView copy_wx_code;
    private TextView copy_invitation_code;
    private String  mInvitationCode="";
    private String  mWxCode="";
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
        tv_level = findViewById(R.id.tv_level);
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
               
                AppUtil.coayText((Activity) mContext,mWxCode);
                Toast.makeText(mContext, "已复制到粘贴版", Toast.LENGTH_SHORT).show();
            }
        });
        copy_invitation_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.coayText((Activity) mContext,mInvitationCode);
                Toast.makeText(mContext, "已复制到粘贴版", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refreshData(TeamInfo teamIfo, FansInfo fansInfo){
        if(teamIfo!=null){
           tv_level.setText(teamIfo.getGrade());
            LoadImgUtils.setImgHead(mContext,iv_head,teamIfo.getHeadImg());
            if(TextUtils.isEmpty(teamIfo.getNickName())){
                tv_name.setText("暂未填写");
            } else {
                tv_name.setText(teamIfo.getNickName());
            }

            fans.setText(teamIfo.getChildCount()+"");
            register_time.setText("注册时间："+teamIfo.getCreateTime());
           if(TextUtils.isEmpty(teamIfo.getWxNumber())){
               wx_code.setText("微信号：未填写");
               copy_wx_code.setVisibility(View.INVISIBLE);
           } else {
               mWxCode = teamIfo.getWxNumber();
               wx_code.setText("微信号："+teamIfo.getWxNumber());
               copy_wx_code.setVisibility(View.VISIBLE);
           }
            if(TextUtils.isEmpty(teamIfo.getInviteCode())){
                invitation_code.setText("邀请码：未填写");
                copy_invitation_code.setVisibility(View.INVISIBLE);
            } else {
                mInvitationCode = teamIfo.getInviteCode();
                invitation_code.setText("邀请码："+teamIfo.getInviteCode());
                copy_invitation_code.setVisibility(View.VISIBLE);
            }
        }
        if(fansInfo!=null){
            last_month_jiesuan.setText(MathUtils.getMoney(fansInfo.getPrevMonthEstimateMoney())+"元");
            total_earnings.setText( MathUtils.getMoney(fansInfo.getAccumulatedAmount())+"元");
        }
    }


}