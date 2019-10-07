package com.markermall.cat.info.ui.PopWindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.markermall.cat.R;
import com.markermall.cat.utils.DensityUtil;
import com.markermall.cat.utils.action.MyAction;
import com.markermall.cat.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;


/**
 * @author YangBoTian
 * @date 2019/8/30
 * @des
 */

public class SwitchOrderTypePopWindow extends PopupWindow {
    private LinearLayout contentView;
    private TagFlowLayout flowLayout;
    private Context mContext;

    private MyAction.One<String> mAction;
    private RelativeLayout rl_root;
    private final int mTextHeight;
    private TextView mTextView;

    public SwitchOrderTypePopWindow(final Activity context, MyAction.One<String> action) {
        this.mContext = context;

        this.mAction = action;
        mTextHeight = DensityUtil.dip2px(context, 45);

        View view =  LayoutInflater.from(mContext).inflate(R.layout.popwindow_switch_order_type, null);
        contentView = view.findViewById(R.id.ll_content);
        view.findViewById(R.id.view_clser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchOrderTypePopWindow.this.dismiss();
            }
        });
        initView();
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(ContextCompat.getColor(context, R.color.transparent));
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //   this.setAnimationStyle(R.style.AnimationPreview);

    }

    ArrayList<TextView> arrayList = new ArrayList<>();

    private void initView() {
        String[] titles = new String[]{mContext.getString(R.string.order_team_tb), mContext.getString(R.string.order_team_sl), mContext.getString(R.string.order_team_other)};
        for (int i = 0; i < titles.length; i++) {
            String title = titles[i];
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mTextHeight);

            final TextView textView = new TextView(mContext);
            textView.setGravity(Gravity.CENTER);
            arrayList.add(textView);
            if (i == 0) {
                textView.setBackgroundResource(R.drawable.bg_pop_window_switch_order_type_itme_line);
                textView.setSelected(true);
            } else if (i == titles.length - 1) {
                textView.setBackgroundResource(R.drawable.bg_pop_window_switch_order_type_itme);
            } else {
                textView.setBackgroundResource(R.drawable.bg_pop_window_switch_order_type_itme_line);
            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAction != null) {
                        mAction.invoke(textView.getText().toString());
                    }
                    mTextView = textView;
                    setBg(textView);
                    SwitchOrderTypePopWindow.this.dismiss();
                }
            });
            textView.setText(title);
            contentView.addView(textView, layoutParams);
        }
    }

    public void setBg(TextView textView) {
        for (int j = 0; j < arrayList.size(); j++) {
            TextView textView1 = arrayList.get(j);
            if (textView1.hashCode() == textView.hashCode()) {
                textView.setSelected(true);
            } else {
                textView1.setSelected(false);
            }
        }
    }

    /**
     * 显示popupWindow
     *
     * @param anchor
     */
    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        if (mTextView != null) {
            setBg(mTextView);
        }
        super.showAsDropDown(anchor);
    }


}
