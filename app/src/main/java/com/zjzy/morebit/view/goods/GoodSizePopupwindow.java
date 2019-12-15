package com.zjzy.morebit.view.goods;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.zjzy.morebit.R;

/**
 * 选定商品
 * Created by haiping.liu on 2019-12-10.
 */
public class GoodSizePopupwindow extends PopupWindow {

    private final View minusView;
    private final View addView;
    private Context mContext;
    private View view;

    public GoodSizePopupwindow(Context mContext,View.OnClickListener itemsOnClick) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_number_goods_rule, null);
        minusView = view.findViewById(R.id.goodsRule_minusRelative);
        addView = view.findViewById(R.id.goodsRule_addRelative);
        // 设置按钮监听
        minusView.setOnClickListener(itemsOnClick);
        addView.setOnClickListener(itemsOnClick);
        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_photo_anim);


    }


}
