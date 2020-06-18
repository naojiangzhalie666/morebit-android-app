package com.zjzy.morebit.view.helper;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.UI.BaseTitleTabBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.MyLog;

import java.util.AbstractList;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fengrs on 2019/4/16.
 * 备注: 商品滴tab栏
 */

public class ShoppingTabView extends RelativeLayout {
    private final int mtype;
    @BindView(R.id.tl_tab)
    TabLayout tl_tab;
    @BindView(R.id.iv_switch)
    ImageView mIvSwitch;
    @BindView(R.id.rl_switch)
    RelativeLayout rl_switch;
    private Context mContext;
    private int mSelectedPos;
    AbstractList<BaseTitleTabBean> tabList = new ArrayList();
    private OnTabListner mListener;

    public ShoppingTabView(Context context, int type) {
        this(context, null, type);

    }

    public ShoppingTabView(Context context, AttributeSet attrs, int type) {
        super(context, attrs);
        mtype = type;
        View view = LayoutInflater.from(context).inflate(R.layout.view_shopping_tab, null);
        this.addView(view);
        ButterKnife.bind(this, view);
        this.mContext = context;
        initView();
    }


    private void initView() {
        tabList.add(new BaseTitleTabBean("综合", false, C.Setting.itemIndex));
        tabList.add(new BaseTitleTabBean("券后价", true, C.Setting.itemVoucherPrice));
        tabList.add(new BaseTitleTabBean("销量", true, C.Setting.saleMonth));
        if (mtype == C.GoodsListType.ForeShow_1 || mtype == C.GoodsListType.ForeShow_2 || mtype == C.GoodsListType.ForeShow_3 || mtype == C.GoodsListType.RANKING_NEWS) {
            rl_switch.setVisibility(GONE);
        }
        initTab(tl_tab);
    }


    /**
     * 初始化TabLayout
     */
    private void initTab(final TabLayout tabLayout) {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabTextColors(getResources().getColor(R.color.color_666666), getResources().getColor(R.color.color_666666));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.top_head));
        tabLayout.setSelectedTabIndicatorHeight(DensityUtil.dip2px(mContext, 0));

        //填充数据
        for (int i = 0; i < tabList.size(); i++) {
            BaseTitleTabBean bean = tabList.get(i);
            TabLayout.Tab tab = tabLayout.newTab();

            bean.order = C.Setting.descParms;
            tab.setCustomView(R.layout.tablayout_donw_up_item_tv);
            TextView textView = (TextView) tab.getCustomView().findViewById(R.id.class_tv);

            textView.setText(bean.name);//设置tab上的文字
            tab.setTag(i);
            tabLayout.addTab(tab);
            //初始化上下按钮
            LinearLayout linearLayout = (LinearLayout) tab.getCustomView().findViewById(R.id.class_icon_ly);

            if (bean.isSelect) {
                linearLayout.setVisibility(View.VISIBLE);
                switchTab(tabLayout, i, bean, true);
            } else {
                linearLayout.setVisibility(View.INVISIBLE);
            }

        }
        switchTab(tabLayout, mSelectedPos, tabList.get(0), false);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            /**
             * 新选
             * @param tab
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                BaseTitleTabBean bean = tabList.get((int) tab.getTag());
                mSelectedPos = (int) tab.getTag();
                switchTab(tabLayout, mSelectedPos, bean, false);
                MyLog.i("test", "onTabSelected: " + bean + "pos: " + mSelectedPos + tabLayout);
                if (AppUtil.isFastClick(500)) return;
                if (mListener != null) ;
                mListener.onReload();
            }

            /**
             * 当tab从 选择 ->未选择
             * @param tab
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                int pos = (int) tab.getTag();
                BaseTitleTabBean bean = tabList.get(pos);
                bean.order = C.Setting.descParms;// 清空未选择的状态
                switchTab(tabLayout, pos, bean, true);
                MyLog.i("test", "onTabSelected: " + bean + "pos: " + pos + tabLayout);
            }

            /**
             * 复选
             * @param tab
             */
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                MyLog.i("test", "onTabSelected");
                int getPos = (int) tab.getTag();
                BaseTitleTabBean bean = tabList.get(getPos);

                if (bean.isSelect) {
                    bean.order = C.Setting.ascParms.equals(bean.order) ? C.Setting.descParms : C.Setting.ascParms;
                    switchTab(tabLayout, getPos, bean, false);
                    if (AppUtil.isFastClick(500)) return;
                    if (mListener != null) ;
                    mListener.onReload();
                }
            }


        });
    }

    private void switchTab(TabLayout tabLayout, int i, BaseTitleTabBean switchTop, boolean isInit) {
        ImageView textIcon1 = (ImageView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.class_icon_up);
      //  ImageView textIcon2 = (ImageView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.class_icon_down);
        TextView text = (TextView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.class_tv);
        if (isInit) {
            textIcon1.setImageResource(R.drawable.icon_jiage_no);
        //    textIcon2.setImageResource(R.drawable.icon_jiagexia);
            TextPaint tp = text.getPaint();
            tp.setFakeBoldText(false);
            text.setTextColor(ContextCompat.getColor(mContext, R.color.color_999999));
        } else {
            text.setTextColor(ContextCompat.getColor(mContext, R.color.top_head));
            TextPaint tp = text.getPaint();
            tp.setFakeBoldText(true);
            if (C.Setting.ascParms.equals(switchTop.order)) {
                textIcon1.setImageResource(R.drawable.icon_jiage_down);
              //  textIcon2.setImageResource(R.drawable.icon_jiagexiaxuanzhong);
            } else {
                textIcon1.setImageResource(R.drawable.icon_jiage_up);
               // textIcon2.setImageResource(R.drawable.icon_jiagexia);
            }
        }
    }

    @OnClick(R.id.rl_switch)
    public void onViewClicked() {
        if (mListener != null) ;
        mListener.switchItmeStye();
    }

    public void setSwitchSelected(boolean switchSelected) {
        mIvSwitch.setSelected(switchSelected);
    }

    public BaseTitleTabBean getTabBean() {
        return tabList.get(mSelectedPos);
    }

    public interface OnTabListner {
        void switchItmeStye();

        void onReload();
    }

    public void setmOkListener(OnTabListner listener) {
        this.mListener = listener;
    }

}
