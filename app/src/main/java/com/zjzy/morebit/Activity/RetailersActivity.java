package com.zjzy.morebit.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.fragment.NumberSubFragment;
import com.zjzy.morebit.home.fragment.AdvancedClassFragment;
import com.zjzy.morebit.home.fragment.IconFragment;
import com.zjzy.morebit.home.fragment.MembershipFragment;
import com.zjzy.morebit.home.fragment.OrderRetailersFragment;
import com.zjzy.morebit.home.fragment.ShoppingMallFragment;
import com.zjzy.morebit.info.ui.fragment.SearchOrderFragment;
import com.zjzy.morebit.main.ui.myview.xtablayout.XTabLayout;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.SwipeDirectionDetector;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/*
 *
 * 电商平台订单
 *
 * */
public class RetailersActivity extends BaseActivity implements View.OnClickListener {
    private TextView txt_head_title;
    private LinearLayout btn_back;
    private XTabLayout xTablayout;
    private ViewPager viewPager;
    private List<OrderRetailersFragment> mFragments = new ArrayList<>();
    private List<String> list = new ArrayList<>();
    private int type;
    private ImageView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailers);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .init();
        initView();
    }

    public static void start(Activity activity, int type) {
        Intent it = new Intent(activity, RetailersActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(C.Extras.ORDERTYPE, type);
        it.putExtras(bundle);

        activity.startActivity(it);
    }

    private void initView() {
        type = getIntent().getIntExtra(C.Extras.ORDERTYPE, 0);
        txt_head_title = (TextView) findViewById(R.id.txt_head_title);
        switch (type) {
            case 0:
                txt_head_title.setText("全部订单");
                break;
            case 1:
                txt_head_title.setText("淘宝订单");
                break;
            case 2:
                txt_head_title.setText("京东订单");
                break;
            case 4:
                txt_head_title.setText("拼多多订单");
                break;
            case 5:
                txt_head_title.setText("考拉订单");
                break;
            case 6:
                txt_head_title.setText("唯品会订单");
                break;
        }

        txt_head_title.getPaint().setFakeBoldText(true);
        txt_head_title.setTextSize(18);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        txt_head_title.setOnClickListener(this);
        search= (ImageView) findViewById(R.id.search);
        search.setOnClickListener(this);
        xTablayout = (XTabLayout) findViewById(R.id.xTablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        list.add("全部");
        list.add("待返佣");
        list.add("已到账");

        initViewpager();

    }

    private void initViewpager() {
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        xTablayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);

        }


        @Override
        public CharSequence getPageTitle(int position) {
            return list == null ? "" + position : list.get(position);

        }

        @Override
        public Fragment getItem(int position) {
            if (position==0){
                Log.e("hjhjhj",type+"01");
                return OrderRetailersFragment.newInstance(5,type);//全部
            }else if (position==1){
                Log.e("hjhjhj",type+"02");
                return OrderRetailersFragment.newInstance(1,type);//待返佣
            }else{
                Log.e("hjhjhj",type+"03");
                return OrderRetailersFragment.newInstance(3,type);//已到账
            }

        }

        @Override
        public int getCount() {
            return list != null ? list.size() : 0;
        }




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.txt_head_title:
                showPopupWindow(txt_head_title);
                break;
            case R.id.search:
                Bundle bundle = new Bundle();
                bundle.putInt(C.Extras.order_type,type);
                OpenFragmentUtils.goToSimpleFragment(RetailersActivity.this, SearchOrderFragment.class.getName(), bundle);
                break;
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private void showPopupWindow(View view) {
        //加载布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.pop_order, null);
        //更改背景颜色
//        inflate.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        final PopupWindow mPopupWindow = new PopupWindow(inflate);
        //设置SelectPicPopupWindow弹出窗体的宽
        mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //点击其他地方隐藏,false为无反应
        mPopupWindow.setFocusable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //对他进行编译
            mPopupWindow.showAsDropDown(view, 0, 0, Gravity.BOTTOM);
        }
        backgroundAlpha(0.2f);
        //对popupWindow进行显示
        mPopupWindow.update();


        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });


        TextView tv_all = inflate.findViewById(R.id.tv_all);
        TextView tv_tao = inflate.findViewById(R.id.tv_tao);
        TextView tv_jd = inflate.findViewById(R.id.tv_jd);
        TextView tv_pdd = inflate.findViewById(R.id.tv_pdd);
        TextView tv_wph = inflate.findViewById(R.id.tv_wph);
        TextView tv_kl = inflate.findViewById(R.id.tv_kl);
        initColor(tv_all,tv_tao,tv_jd,tv_pdd,tv_wph,tv_kl);

        tv_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 0;
                EventBus.getDefault().post(new MessageEvent(EventBusAction.ORDERTYPE_ALL));
                txt_head_title.setText("全部订单");
                mPopupWindow.dismiss();
            }
        });
        tv_tao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                EventBus.getDefault().post(new MessageEvent(EventBusAction.ORDERTYPE_TAO));
                txt_head_title.setText("淘宝订单");
                mPopupWindow.dismiss();
            }
        });
        tv_jd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                EventBus.getDefault().post(new MessageEvent(EventBusAction.ORDERTYPE_JD));
                txt_head_title.setText("京东订单");
                mPopupWindow.dismiss();
            }
        });
        tv_pdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 4;
                EventBus.getDefault().post(new MessageEvent(EventBusAction.ORDERTYPE_PDD));
                txt_head_title.setText("拼多多订单");
                mPopupWindow.dismiss();
            }
        });
        tv_wph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 6;
                EventBus.getDefault().post(new MessageEvent(EventBusAction.ORDERTYPE_WPH));
                txt_head_title.setText("唯品会订单");
                mPopupWindow.dismiss();
            }
        });
        tv_kl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 5;
                EventBus.getDefault().post(new MessageEvent(EventBusAction.ORDERTYPE_KAOLA));
                txt_head_title.setText("考拉订单");
                mPopupWindow.dismiss();
            }
        });

    }

    private void initColor(TextView tv_all, TextView tv_tao, TextView tv_jd, TextView tv_pdd, TextView tv_wph, TextView tv_kl) {
        if (type==0){
            tv_all.setTextColor(Color.parseColor("#F05557"));
            tv_tao.setTextColor(Color.parseColor("#333333"));
            tv_jd.setTextColor(Color.parseColor("#333333"));
            tv_pdd.setTextColor(Color.parseColor("#333333"));
            tv_wph.setTextColor(Color.parseColor("#333333"));
            tv_kl.setTextColor(Color.parseColor("#333333"));

        }else if (type==1){
            tv_all.setTextColor(Color.parseColor("#333333"));
            tv_tao.setTextColor(Color.parseColor("#F05557"));
            tv_jd.setTextColor(Color.parseColor("#333333"));
            tv_pdd.setTextColor(Color.parseColor("#333333"));
            tv_wph.setTextColor(Color.parseColor("#333333"));
            tv_kl.setTextColor(Color.parseColor("#333333"));
        }else if (type==2){
            tv_all.setTextColor(Color.parseColor("#333333"));
            tv_tao.setTextColor(Color.parseColor("#333333"));
            tv_jd.setTextColor(Color.parseColor("#F05557"));
            tv_pdd.setTextColor(Color.parseColor("#333333"));
            tv_wph.setTextColor(Color.parseColor("#333333"));
            tv_kl.setTextColor(Color.parseColor("#333333"));
        }else if (type==4){
            tv_all.setTextColor(Color.parseColor("#333333"));
            tv_tao.setTextColor(Color.parseColor("#333333"));
            tv_jd.setTextColor(Color.parseColor("#333333"));
            tv_pdd.setTextColor(Color.parseColor("#F05557"));
            tv_wph.setTextColor(Color.parseColor("#333333"));
            tv_kl.setTextColor(Color.parseColor("#333333"));
        }else if (type==6){
            tv_all.setTextColor(Color.parseColor("#333333"));
            tv_tao.setTextColor(Color.parseColor("#333333"));
            tv_jd.setTextColor(Color.parseColor("#333333"));
            tv_pdd.setTextColor(Color.parseColor("#333333"));
            tv_wph.setTextColor(Color.parseColor("#F05557"));
            tv_kl.setTextColor(Color.parseColor("#333333"));
        }else if (type==5){
            tv_all.setTextColor(Color.parseColor("#333333"));
            tv_tao.setTextColor(Color.parseColor("#333333"));
            tv_jd.setTextColor(Color.parseColor("#333333"));
            tv_pdd.setTextColor(Color.parseColor("#333333"));
            tv_wph.setTextColor(Color.parseColor("#333333"));
            tv_kl.setTextColor(Color.parseColor("#F05557"));
        }


    }
}
