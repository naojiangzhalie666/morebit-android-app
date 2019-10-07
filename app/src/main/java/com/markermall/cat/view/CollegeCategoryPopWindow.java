package com.markermall.cat.view;

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
import android.widget.TextView;

import com.markermall.cat.R;
import com.markermall.cat.utils.action.MyAction;
import com.markermall.cat.view.flowlayout.FlowLayout;
import com.markermall.cat.view.flowlayout.TagAdapter;
import com.markermall.cat.view.flowlayout.TagFlowLayout;

import java.util.List;


/**
 * @author YangBoTian
 * @date 2019/8/30
 * @des
 */

public class CollegeCategoryPopWindow extends PopupWindow {
    private View contentView;
    private TagFlowLayout flowLayout;
    private Context context;

    private List<String> mHomeColumns;
    private MyAction.One<Integer> action;
    private RelativeLayout rl_root;

    public CollegeCategoryPopWindow(final Activity context, List<String> homeColumns, MyAction.One<Integer> action) {
        this.context = context;
        this.mHomeColumns = homeColumns;
        this.action = action;
        //获得 LayoutInflater 的实例
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.college_more_category, null);

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

        flowLayout = (TagFlowLayout) contentView.findViewById(R.id.flowlayout);
        rl_root = contentView.findViewById(R.id.rl_root);
        final LayoutInflater mInflater = LayoutInflater.from(context);
        flowLayout.setMaxSelectCount(1);
        flowLayout.setAdapter(new TagAdapter<String>(mHomeColumns) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
//                Log.i("test","this2 " +parent.isMore());
                View flowView = (View) mInflater.inflate(R.layout.item_college_categroy_tv,
                        flowLayout, false);
                TextView tv = (TextView) flowView.findViewById(R.id.flow_text);
                tv.setText(s);
                return flowView;
            }
        });


        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                action.invoke(position);
                return true;
            }
        });
        rl_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CollegeCategoryPopWindow.this.isShowing()) {
                    action.invoke(-1);
                    dismiss();
                }
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                action.invoke(-1);
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
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }


    public void setSelected(int position){
        if(flowLayout!=null){
            flowLayout.getAdapter().setSelectedList(position);
        }

    }
}
