package com.markermall.cat.Module.common.View;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.markermall.cat.R;
import com.markermall.cat.pojo.UI.BaseTitleTabBean;
import com.markermall.cat.utils.DensityUtil;

import java.util.ArrayList;

/**
 * Created by fengrs on 2018/9/11.
 * 备注:
 */

public class TabStickyHeaderAdapter implements StickyHeaderAdapter<TabStickyHeaderAdapter.HeaderHolder> {
    private Context mContext;
    private int mSelectedPos;
    private ArrayList<BaseTitleTabBean> mTabData;


    private String descParms = "desc"; //倒序
    private String ascParms = "asc"; //升序


    public TabStickyHeaderAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * Returns the header id for the item at the given position.
     *
     * @param position the item position
     * @return the header id
     */
    @Override
    public long getHeaderId(int position) {
        return 3;
    }

    /**
     * Creates a new header ViewHolder.
     *
     * @param parent the header's view parent
     * @return a view holder for the created view
     */
    @Override
    public TabStickyHeaderAdapter.HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_test, parent, false);
        return new HeaderHolder(view);
    }

    /**
     * Updates the header view to reflect the header data for the given position
     *
     * @param viewholder the header view holder
     * @param position   the header's item position
     */
    @Override
    public void onBindHeaderViewHolder(TabStickyHeaderAdapter.HeaderHolder viewholder, int position) {
        initTab(viewholder.header);
    }

    public void setTabData(ArrayList<BaseTitleTabBean> tabData) {
        mTabData = tabData;
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {
        public TabLayout header;

        public HeaderHolder(View itemView) {
            super(itemView);

            header = (TabLayout) itemView;
        }
    }


    /**
     * 初始化TabLayout
     */
    private void initTab(final TabLayout tabLayout) {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabTextColors(mContext.getResources().getColor(R.color.color_666666), mContext.getResources().getColor(R.color.color_666666));
        tabLayout.setSelectedTabIndicatorColor(mContext.getResources().getColor(R.color.top_head));
        tabLayout.setSelectedTabIndicatorHeight(DensityUtil.dip2px(mContext, 0));

        //填充数据
        for (int i = 0; i < mTabData.size(); i++) {
            BaseTitleTabBean bean = mTabData.get(i);
            TabLayout.Tab tab = tabLayout.newTab();
            if (bean.isSwitchItme) {
                tab.setCustomView(R.layout.tablayout_switch_itme);
                tab.setTag(i);
                tabLayout.addTab(tab);
                continue;
            }
            bean.order = descParms;
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

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            /**
             * 新选
             * @param tab
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mSelectedPos = (int) tab.getTag();
                BaseTitleTabBean bean = mTabData.get(mSelectedPos);
                if (bean.isSwitchItme) {
                    switchItmeStye(mSelectedPos);
                    return;
                }
                switchTab(tabLayout, mSelectedPos, bean, false);
                if (mListener!=null)
                    mListener.onReload(mSelectedPos);
            }

            /**
             * 当tab从 选择 ->未选择
             * @param tab
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = (int) tab.getTag();
                BaseTitleTabBean bean = mTabData.get(pos);
                if (bean.isSwitchItme) return;
                bean.order = descParms;// 清空未选择的状态
                switchTab(tabLayout, pos, bean, true);
            }

            /**
             * 复选
             * @param tab
             */
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int getPos = (int) tab.getTag();
                BaseTitleTabBean bean = mTabData.get(getPos);
                if (bean.isSwitchItme) {
                    switchItmeStye(getPos);
                    return;
                }
                if (bean.isSelect) {
                    bean.order = ascParms.equals(bean.order) ? descParms : ascParms;
                    switchTab(tabLayout, getPos, bean, false);
                    if (mListener!=null)
                        mListener.onReload(getPos);
                }
            }

            private void switchItmeStye(int getPos) {
                if (mListener!=null) {
                    TabLayout.Tab tabAt = tabLayout.getTabAt(getPos);
                    mListener.switchItmeStye(tabAt,getPos);
                }

            }
        });
    }


    private void switchTab(TabLayout tabLayout, int i, BaseTitleTabBean switchTop, boolean isInit) {
        TextView textIcon1 = (TextView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.class_icon_up);
        TextView textIcon2 = (TextView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.class_icon_down);
        if (isInit) {
            textIcon1.setBackgroundResource(R.drawable.icon_jiageshangxuanzhong);
            textIcon2.setBackgroundResource(R.drawable.icon_jiagexia);
        } else {
            if (ascParms.equals(switchTop.order)) {
                textIcon1.setBackgroundResource(R.drawable.icon_jiageshangxuanzhong);
                textIcon2.setBackgroundResource(R.drawable.icon_jiagexiaxuanzhong);
            } else {
                textIcon1.setBackgroundResource(R.drawable.icon_jiageshang);
                textIcon2.setBackgroundResource(R.drawable.icon_jiagexia);
            }
        }
    }

    private OnTabListener mListener;
    public void setOnTabListener(OnTabListener listener) {
        mListener = listener;
    }

    private OnTabListener onReLoadListener;

    public interface OnTabListener {
        void onReload(int selectedPos);

        void switchItmeStye(TabLayout.Tab tabAt, int getPos);
    }
}
