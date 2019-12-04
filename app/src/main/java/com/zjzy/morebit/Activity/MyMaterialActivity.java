package com.zjzy.morebit.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.CircleDayHotFragment;
import com.zjzy.morebit.utils.UI.ActivityUtils;
import com.zjzy.morebit.view.ToolbarHelper;

public class MyMaterialActivity extends BaseActivity {

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, MyMaterialActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImmersionBar
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .init();// 默认白底黑字
        setContentView(R.layout.activity_my_material);


        initView();
    }


    private void initView() {
        new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(getString(R.string.my_material));
        CircleDayHotFragment fragment = new CircleDayHotFragment();
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(), fragment, R.id.fl_content);
    }
    @Override
    protected boolean isImmersionBarEnabled() {
        return true;
    }

}
