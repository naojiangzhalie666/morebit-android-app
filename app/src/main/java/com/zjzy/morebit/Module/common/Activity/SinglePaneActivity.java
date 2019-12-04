package com.zjzy.morebit.Module.common.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by YangBoTian on 2018/7/6.
 */

public class SinglePaneActivity extends SimpleSinglePaneActivity{

    private String mFragment;
    private Fragment fragment;
    public static void start(Context context, String fragmentClass, Bundle extras) {
        Intent intent = new Intent(context, SinglePaneActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.putExtra("fragment", fragmentClass);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏icon和文字颜色为亮色模式 避免浅色状态栏造成iocn文字看不清
     //   StatusBar.StatusBarLightMode(this);
    }

    @Override
    protected Fragment onCreatePane() {
        Intent intent = getIntent();
        mFragment = intent.getStringExtra("fragment");
        fragment =Fragment.instantiate(getApplicationContext(), mFragment);
        return fragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(fragment!=null){
            fragment.onActivityResult(requestCode,resultCode,data);
        }

    }
}
