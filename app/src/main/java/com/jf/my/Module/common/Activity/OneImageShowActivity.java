package com.jf.my.Module.common.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jf.my.Module.common.Fragment.ImageDetailFragment;
import com.jf.my.R;
import com.jf.my.utils.ActivityStyleUtil;


/**
 * 图片显示-可放大缩小、可删除
 */

public class OneImageShowActivity extends FragmentActivity implements View.OnClickListener {
    private TextView txt_head_title;
    private View status_bar;
    private ImageDetailFragment imageDetailFragment;
    private LinearLayout btn_back;
    private Bundle bundle;
    private String url = "";
    private String title = "";
    private boolean isDel = false; //是否显示删除按钮
    private LinearLayout del_ly;
    private int position;
    private RelativeLayout rel_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneimageshow);
        initBundle();
        inview();
    }

    private void initBundle() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("title");
            url = bundle.getString("url");
            isDel = bundle.getBoolean("isDel", false);
            position = bundle.getInt("position", -1);
        }
    }

    public void inview() {
        ActivityStyleUtil.initSystemBar(this, R.color.color_757575); //设置标题栏颜色值

        txt_head_title = (TextView) findViewById(R.id.txt_head_title);
        txt_head_title.setText(title);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_back.setVisibility(View.VISIBLE);
        del_ly = (LinearLayout) findViewById(R.id.del_ly);
        del_ly.setOnClickListener(this);
        rel_top = (RelativeLayout) findViewById(R.id.rel_top);
        if (isDel) {
            del_ly.setVisibility(View.VISIBLE);
            rel_top.setVisibility(View.VISIBLE);
        } else {
            del_ly.setVisibility(View.GONE);
            rel_top.setVisibility(View.GONE);
        }
        showFragment(0);
    }

    int currentFlagment = 0;

    public void showFragment(int index) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        // 想要显示一个fragment,先隐藏所有fragment，防止重叠
        hideFragments(ft);
        switch (index) {
            case 0:
                // 如果fragment1已经存在则将其显示出来
                if (imageDetailFragment != null) {
                    ft.show(imageDetailFragment);
                    // 否则添加fragment1，注意添加后是会显示出来的，replace方法也是先remove后add
                } else {
                    imageDetailFragment = ImageDetailFragment.newInstance(url);
                    ft.add(R.id.con_page, imageDetailFragment, "imageDetailFragment");
                }
                break;
        }
        ft.commit();
        currentFlagment = index;
    }


    // 当fragment已被实例化，相当于发生过切换，就隐藏起来
    public void hideFragments(FragmentTransaction ft) {
        if (imageDetailFragment != null)
            ft.hide(imageDetailFragment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.del_ly: //删除
                delImg();
                break;
            default:
                break;
        }
    }

    /**
     * 删除图片
     */
    private void delImg() {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        Intent getIt = getIntent();
        getIt.putExtras(bundle);
        setResult(114, getIt);
        finish();
    }

}
