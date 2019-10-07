package com.markermall.cat.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

import com.markermall.cat.MainActivity;
import com.markermall.cat.R;

/**
 * App每次打开显示的页面
 * Created by Administrator on 2017/9/11 0011.
 */

public class StartAdActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_startad);
        // 初始化页面
        initViews();
    }

    private void initViews() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartAdActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
//
            }
//        }, 2000);//2秒后执行Runnable中的run方法
        }, 0);//2秒后执行Runnable中的run方法

    }



}
