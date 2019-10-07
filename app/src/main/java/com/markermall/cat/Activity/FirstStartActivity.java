package com.markermall.cat.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.markermall.cat.MainActivity;
import com.markermall.cat.R;
import com.markermall.cat.utils.ActivityStyleUtil;
import com.markermall.cat.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/9/11 0011.
 */

public class FirstStartActivity extends FragmentActivity implements  ViewPager.OnPageChangeListener {

    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    // 记录当前选中位置
    private int currentIndex;
    Boolean isFirst;
    private boolean isLastPage = false;
    private boolean isDragPage = false;
    private boolean canJumpPage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_firststart);
        // 初始化页面
        initViews();
        // 初始化底部小点
    }

    private void initViews() {
        SharedPreferencesUtils.put(FirstStartActivity.this,"isFirstStart","1");
        ActivityStyleUtil.initSystemBar(FirstStartActivity.this,R.color.transparent); //设置标题栏颜色值
        LayoutInflater inflater = LayoutInflater.from(this);
//        RelativeLayout guideFour = (RelativeLayout) inflater.inflate(R.layout.viewpage_firststart_four, null);
//        guideFour.findViewById(R.id.toMain).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FirstStartActivity.this,StartAdActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
        views = new ArrayList<View>();
        // 初始化引导图片列表
        views.add(inflater.inflate(R.layout.viewpage_firststart_one, null));
        views.add(inflater.inflate(R.layout.viewpage_firststart_two, null));
        views.add(inflater.inflate(R.layout.viewpage_firststart_three, null));
        View inflate = inflater.inflate(R.layout.viewpage_firststart_4, null);
        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpToNext();
            }
        });
        views.add(inflate);
        // 初始化Adapter
        vpAdapter = new ViewPagerAdapter(views, this);

        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);

    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int state) {
        isDragPage = state == 1;
    }

    /**
     * 在屏幕滚动过程中不断被调用
     * @param position
     * @param positionOffset   是当前页面滑动比例，如果页面向右翻动，这个值不断变大，最后在趋近1的情况后突变为0。如果页面向左翻动，这个值不断变小，最后变为0
     * @param positionOffsetPixels   是当前页面滑动像素，变化情况和positionOffset一致
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (isLastPage && isDragPage && positionOffsetPixels == 0){   //当前页是最后一页，并且是拖动状态，并且像素偏移量为0
            if (canJumpPage){
                canJumpPage = false;
                JumpToNext();
            }
        }
    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int position) {
        // 设置底部小点选中状态

        isLastPage = position == views.size()-1;
    }

    public class ViewPagerAdapter extends PagerAdapter {

        // 界面列表
        private List<View> views;
        private Activity activity;


        public ViewPagerAdapter(List<View> views, Activity activity) {
            this.views = views;
            this.activity = activity;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position),0);
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // super.destroyItem(container, position, object);
            container.removeView(views.get(position));
        }

        // 获得当前界面数
        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }

    /**
     * viewpager滑动到最后一页做跳转逻辑
     */
    private void JumpToNext() {
        Intent intent = new Intent(FirstStartActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }




}
