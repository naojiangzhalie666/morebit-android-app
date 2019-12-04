package com.zjzy.morebit.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.zjzy.morebit.R;

/**
 * 自定义popupWindow--分享
 */

public class SharePopwindow extends PopupWindow {
    private View mView;

    public SharePopwindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        initView(context, itemsOnClick);
    }

    private void initView(final Activity context, View.OnClickListener itemsOnClick) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.popupwindow_share, null);
//        RelativeLayout weiXFriend = (RelativeLayout) mView.findViewById(R.id.rl_weix);
//        RelativeLayout friendster = (RelativeLayout) mView.findViewById(R.id.rl_weixpyq);
//        TextView canaleTv = (TextView) mView.findViewById(R.id.share_cancle);
//        canaleTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //销毁弹出框
//                dismiss();
//                backgroundAlpha(context, 1f);
//            }
//        });
        //设置按钮监听
        mView.findViewById(R.id.weixinFriend).setOnClickListener(itemsOnClick);
        mView.findViewById(R.id.weixinCircle).setOnClickListener(itemsOnClick);
        mView.findViewById(R.id.sinaWeibo).setOnClickListener(itemsOnClick);
        mView.findViewById(R.id.qqFriend).setOnClickListener(itemsOnClick);
        mView.findViewById(R.id.qqRoom).setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置PopupWindow可触摸
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        //设置非PopupWindow区域是否可触摸
//        this.setOutsideTouchable(false);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.select_anim);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        backgroundAlpha(context, 0.5f);//0.0-1.0
        this.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha(context, 1f);
            }
        });
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePopwindow.this.dismiss();
            }
        });
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

}
