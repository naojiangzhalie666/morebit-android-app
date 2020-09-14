package com.zjzy.morebit.info.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.fragment.AppFeedBackFragment;
import com.zjzy.morebit.fragment.CircleFeedBackFragment;
import com.zjzy.morebit.fragment.GoodsFeedBackFragment;
import com.zjzy.morebit.main.model.ConfigModel;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.UI.ActivityUtils;
import com.zjzy.morebit.view.ToolbarHelper;

import org.greenrobot.eventbus.EventBus;


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
        Toolbar toolbar = toolbarHelper.getToolbar();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            EventBus.getDefault().post(new MessageEvent(EventBusAction.DISS_RUANJIANPAN));
        }
        return super.onOptionsItemSelected(item);
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
