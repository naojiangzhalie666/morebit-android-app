package com.markermall.cat.info.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.markermall.cat.Activity.ShowWebActivity;
import com.markermall.cat.Module.common.Activity.BaseActivity;
import com.markermall.cat.R;
import com.markermall.cat.fragment.AppFeedBackFragment;
import com.markermall.cat.fragment.CircleFeedBackFragment;
import com.markermall.cat.fragment.GoodsFeedBackFragment;
import com.markermall.cat.main.model.ConfigModel;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.utils.ActivityStyleUtil;
import com.markermall.cat.utils.OpenFragmentUtils;
import com.markermall.cat.utils.UI.ActivityUtils;
import com.markermall.cat.view.ToolbarHelper;


/**
 * 通用单fragment-Activity
 * Created by Administrator on 2017/9/11 0011.
 */

public class AppFeedActivity extends BaseActivity {
    private Bundle bundle;
    private String title = "";
    private String fragmentName = "";
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_feed);
        initBundle();
        inview();
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    private void initBundle() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("title", "");
            fragmentName = bundle.getString("fragmentName", "");
        }
    }

    public void inview() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityStyleUtil.initSystemBar(this, R.color.white); //设置标题栏颜色值
        } else {
            ActivityStyleUtil.initSystemBar(this, R.color.color_757575); //设置标题栏颜色值
        }
        ToolbarHelper toolbarHelper = new ToolbarHelper(this).setToolbarAsUp().setCustomTitle(title);
        if ("AppFeedBackFragment".equals(fragmentName)) {
            toolbarHelper.setCustomRightTitle("投诉", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConfigModel.getInstance().getSystemCustomService(AppFeedActivity.this)
                            .subscribe(new DataObserver<String>() {
                                @Override
                                protected void onSuccess(String data) {

                                    if (!TextUtils.isEmpty(data)) {
                                        if (data.contains("?")) {
                                           data = data+"&qiyu=true";
                                        } else {
                                            data = data+"?qiyu=true";
                                        }
                                        ShowWebActivity.start(AppFeedActivity.this, data, getString(R.string.service));
                                    }
                                }
                            });
                }
            });
        }
        showFragment();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void showFragment() {
        mFragment = OpenFragmentUtils.getFragment(fragmentName);
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(), mFragment, R.id.con_page);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if ("GoodsFeedBackFragment".equals(fragmentName)) {
                ((GoodsFeedBackFragment) mFragment).onActivityResult(requestCode, resultCode, data);
            } else if ("AppFeedBackFragment".equals(fragmentName)) {
                ((AppFeedBackFragment) mFragment).onActivityResult(requestCode, resultCode, data);

            } else if("CircleFeedBackFragment".equals(fragmentName)){
                ((CircleFeedBackFragment) mFragment).onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
