package com.zjzy.morebit.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.HomeTopFenleiAdapter;
import com.zjzy.morebit.adapter.OrderPopAdapter;
import com.zjzy.morebit.pojo.goods.GoodCategoryInfo;
import com.zjzy.morebit.utils.action.MyAction;

import java.util.List;

/**
 * Created by YangBoTian on 2018/7/5.
 */

public class HomeCategoryPopWindow extends PopupWindow {
    private View contentView;
    private FixGridView fl_gView;
    private Context context;
    private OrderPopAdapter adapter;
    private HomeTopFenleiAdapter htflAdapter;
    private List<GoodCategoryInfo> mHomeColumns ;
    private  MyAction.One<Integer> action ;
    private RelativeLayout rl_root;
    public HomeCategoryPopWindow(final Activity context, List<GoodCategoryInfo> homeColumns, MyAction.One<Integer> action) {
        this.context = context;
      this.mHomeColumns = homeColumns;
        this.action = action;
        //获得 LayoutInflater 的实例
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.home_more_category, null);

        this.setContentView(contentView);
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

        initData();
    }

    private void initData() {

        fl_gView = (FixGridView) contentView.findViewById(R.id.fl_gView);
        rl_root = contentView.findViewById(R.id.rl_root);
        fl_gView.setAdapter(adapter);
        htflAdapter = new HomeTopFenleiAdapter(context, mHomeColumns,action);
        fl_gView.setAdapter(htflAdapter);
        rl_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HomeCategoryPopWindow.this.isShowing()){
                     dismiss();
                }
            }
        });
    }

    /**
     * 显示popupWindow
     *
     * @param anchor
     */
    @Override
    public void showAsDropDown(View anchor) {
        if(Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }



}
