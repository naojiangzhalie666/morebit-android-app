package com.markermall.cat.view.helper;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.markermall.cat.R;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.PanicBuyTiemBean;
import com.markermall.cat.pojo.request.RequestPanicBuyTabBean;
import com.markermall.cat.utils.DateTimeUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * Created by fengrs on 2019/4/16.
 * 备注:
 */

public class PanicBuyTabView extends RelativeLayout {
    @BindView(R.id.tl_tab)
    TabLayout mTabLayout;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_flash_sale)
    TextView tv_flash_sale;
    @BindView(R.id.ll_time)
    LinearLayout ll_time;
    @BindView(R.id.rl_flash_sale)
    RelativeLayout rl_flash_sale;
    private View mView;

    private CountDownTimer mCountDownTiemr;
    private int mRushPos;
    private List<PanicBuyTiemBean> mPanicBuyTiemBean;
    private PanicBuyTiemBean mTiemPosbean;
    private OnTabListner mListener;

    public PanicBuyTabView(Context context) {
        this(context, null);
    }

    public PanicBuyTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mView = LayoutInflater.from(context).inflate(R.layout.view_panic_buy_tab, null);
        this.addView(mView);
        ButterKnife.bind(this, mView);
    }

    /**
     * 初始化TabLayout
     *
     * @param List
     */
    private void initTab(List<PanicBuyTiemBean> List) {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabTextColors(getResources().getColor(R.color.mmhuisezi), getResources().getColor(R.color.top_head));
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.top_head));
        mTabLayout.setSelectedTabIndicatorHeight(0);
        //填充数据
        for (int i = 0; i < List.size(); i++) {

            PanicBuyTiemBean bean = List.get(i);
            TabLayout.Tab tab = mTabLayout.newTab();
            tab.setCustomView(R.layout.tablayout_item_twotv);
            if (i == 0) {
                // 设置第一个tab的TextView是被选择的样式
                tab.getCustomView().findViewById(R.id.tab_box).setSelected(true);//第一个tab被选中
            }
            TextView textView1 = (TextView) tab.getCustomView().findViewById(R.id.tab_text1);
            textView1.setText(bean.getTitle());//设置tab上的文字
            TextView textView2 = (TextView) tab.getCustomView().findViewById(R.id.tab_text2);
            textView2.setText(bean.getSubtitle());//设置tab下的文字
            tab.setTag(i);
            if (mTabLayout.getChildCount() > 4) {
                continue;
            }
            mTabLayout.addTab(tab);
        }
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            /**
             * 新选
             * @param tab
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int getPos = (int) tab.getTag();
                tab.getCustomView().findViewById(R.id.tab_box).setSelected(true);
                reLoadData(getPos);
            }

            /**
             * 当tab从 选择 ->未选择
             * @param tab
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tab_box).setSelected(false);
            }

            /**
             * 复选
             * @param tab
             */
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    /**
     * 第一次获取数据
     */
    public void getTabDeta(final RxAppCompatActivity activity) {

        getGet_taoqianggou_time(activity)
                .subscribe(new DataObserver<List<PanicBuyTiemBean>>() {
                    @Override
                    protected void onSuccess(List<PanicBuyTiemBean> bean) {
                        mPanicBuyTiemBean = bean;
                        initTab(bean);
                        for (int i = 0; i < bean.size(); i++) {
                            PanicBuyTiemBean itme = bean.get(i);
                            if ("1".equals(itme.getType())) {
                                mRushPos = i;
                                mTabLayout.getTabAt(i).select();
                                reLoadData(i);
                                long l = System.currentTimeMillis();
                                int endTiem = 0;
                                try {
                                    endTiem = Integer.valueOf(itme.getEndTime());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                final long firstCreateIndexDate = DateTimeUtils.getFirstCreateIndexDate(endTiem);
                                long downTiem = firstCreateIndexDate - l;
                                if (mCountDownTiemr != null) {
                                    mCountDownTiemr.cancel();
                                }
                                mCountDownTiemr = new CountDownTimer(downTiem, 1000) {
                                    public void onTick(long millisUntilFinished) {

                                        String hms = DateTimeUtils.getCountTimeByLong(millisUntilFinished);
                                        if (!TextUtils.isEmpty(hms))
                                            tv_time.setText(hms);
                                    }

                                    public void onFinish() {
                                        getTabDeta(activity);
                                    }
                                }.start();
                            }
                        }
                    }
                });

    }

    /**
     * 重新加载数据
     *
     * @param getPos
     */
    private void reLoadData(int getPos) {
        if (mRushPos != getPos) {
            ll_time.setVisibility(View.GONE);
            rl_flash_sale.setVisibility(View.VISIBLE);
        } else {
            ll_time.setVisibility(View.VISIBLE);
            rl_flash_sale.setVisibility(View.GONE);
        }

        if (mPanicBuyTiemBean != null && mPanicBuyTiemBean.size() != 0) {
            mTiemPosbean = mPanicBuyTiemBean.get(getPos);
        }

        mListener.getFirstData();


    }


    private Observable<BaseResponse<List<PanicBuyTiemBean>>> getGet_taoqianggou_time(RxAppCompatActivity activity) {
        RequestPanicBuyTabBean requestBean = new RequestPanicBuyTabBean();
        requestBean.setType(0);

        return RxHttp.getInstance().getSysteService()
                .getPanicBuyTabData(requestBean)
                .compose(RxUtils.<BaseResponse<List<PanicBuyTiemBean>>>switchSchedulers())
                .compose(activity.<BaseResponse<List<PanicBuyTiemBean>>>bindToLifecycle());
    }


    public PanicBuyTiemBean getTiemPosBean() {
        return mTiemPosbean;
    }

    public void destroyView() {
        if (mCountDownTiemr != null) mCountDownTiemr.cancel();
    }

    public interface OnTabListner {
        void getFirstData();
    }

    public void setTabListener(OnTabListner listener) {
        this.mListener = listener;
    }

}
