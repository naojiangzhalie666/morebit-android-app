package com.zjzy.morebit.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.OrderPopAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangBoTian on 2018/7/5.
 */

public class OrderPopupWindow extends PopupWindow {
    private View contentView;
    private ListView lv_pop;
    private List<String> paths;
    private Context context;
    private OrderPopAdapter adapter;
    private SelectClickListener selectClickListener;

    public OrderPopupWindow(final Activity context) {
        this.context = context;

        //获得 LayoutInflater 的实例
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.item_pop, null);

        //获取屏幕的宽高
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
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

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams parms = ((Activity) context).getWindow().getAttributes();
                parms.alpha = 1.0f;
                ((Activity) context).getWindow().setAttributes(parms);
                if(selectClickListener!=null){
                    selectClickListener.dismiss();
                }
            }
        });
        initData();
    }

    private void initData() {

        paths = new ArrayList<>();
        paths.add("全部");
        paths.add("已结算");
        paths.add("已付款");
        paths.add("已失效");
        adapter = new OrderPopAdapter(paths, context);
        lv_pop = (ListView) contentView.findViewById(R.id.list_lv);
        lv_pop.setAdapter(adapter);
        lv_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectClickListener != null) {
                    adapter.setCurPosition(position);
                    adapter.notifyDataSetChanged();
                    selectClickListener.onClick(view, position, paths.get(position));
                    OrderPopupWindow.this.dismiss();
                }
            }
        });
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            // 弹出PopupWindow时让后面的界面变暗
            WindowManager.LayoutParams parms = ((Activity) context).getWindow().getAttributes();
            parms.alpha = 0.5f;
            ((Activity) context).getWindow().setAttributes(parms);
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }

    public void setDefaultSelect() {
        if (adapter != null) {
            adapter.setCurPosition(-1);
        }
    }
    public void setSelect(int position) {
        if (adapter != null) {
            adapter.setCurPosition(position);
        }
    }
    public interface SelectClickListener {
        void onClick(View view, int position, String curType);
        void dismiss();
    }

    public void setSelectClickListener(SelectClickListener selectClickListener) {
        this.selectClickListener = selectClickListener;
    }
}
