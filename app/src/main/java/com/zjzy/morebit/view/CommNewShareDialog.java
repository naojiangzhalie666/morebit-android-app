package com.zjzy.morebit.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzy.morebit.R;

/**
 * Created by YangBoTian on 2019/5/13.
 */

public class CommNewShareDialog extends Dialog {
    public static  final  int NORMAL =0;
    public static  final  int BLACK =1;
    private View mView;
    private View.OnClickListener mOnClick;
    private Context mContext;
    private LinearLayout ll_dialog;
    private  int mType;
    ImageView iv_weChat,iv_weChat_circle,iv_qq,iv_qq_room;
    TextView tv_weChat,tv_weChat_circle,tv_qq,tv_qq_room;
    TextView iv_close;
    TextView title;
    public CommNewShareDialog(Context context, View.OnClickListener onClick) {
        super(context, R.style.dialog);
        mOnClick = onClick;
        mContext = context;
    }
    public CommNewShareDialog(Context context, int type, View.OnClickListener onClick) {
        super(context, R.style.dialog);
        mType= type;
        mOnClick = onClick;
        mContext = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_commnew);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        ll_dialog = findViewById(R.id.ll_dialog);
        iv_weChat = findViewById(R.id.iv_weChat);
        iv_weChat_circle = findViewById(R.id.iv_weChat_circle);
        iv_qq = findViewById(R.id.iv_qq);
        iv_qq_room = findViewById(R.id.iv_qq_room);
        iv_close = findViewById(R.id.iv_close);
        tv_weChat = findViewById(R.id.tv_weChat);
        tv_weChat_circle = findViewById(R.id.tv_weChat_circle);
        tv_qq = findViewById(R.id.tv_qq);
        tv_qq_room = findViewById(R.id.tv_qq_room);
        iv_close = findViewById(R.id.iv_close);
        title = findViewById(R.id.title);
        if(mType==1){
            GradientDrawable background = (GradientDrawable) ll_dialog.getBackground();
            background.setColor(ContextCompat.getColor(mContext,R.color.colcor_B300000));
            iv_weChat.setImageResource(R.drawable.icon_video_wechat);
            iv_weChat_circle.setImageResource(R.drawable.icon_video_weichat_circle);
            iv_qq.setImageResource(R.drawable.icon_video_qq);
            iv_qq_room.setImageResource(R.drawable.icon_video_qq_room);
            iv_close.setVisibility(View.GONE);
            title.setTextColor(ContextCompat.getColor(mContext,R.color.colcor_999999));
            tv_weChat.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            tv_weChat_circle.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            tv_qq.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            tv_qq_room.setTextColor(ContextCompat.getColor(mContext,R.color.white));
        }
        //设置按钮监听
        findViewById(R.id.weixinFriend).setOnClickListener(mOnClick);
        findViewById(R.id.weixinCircle).setOnClickListener(mOnClick);
        findViewById(R.id.qqFriend).setOnClickListener(mOnClick);
        findViewById(R.id.qqRoom).setOnClickListener(mOnClick);
        findViewById(R.id.rl_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        WindowManager windowManager = ((Activity)mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int)(display.getWidth()); //设置宽度
        getWindow().setAttributes(lp);
    }

}
