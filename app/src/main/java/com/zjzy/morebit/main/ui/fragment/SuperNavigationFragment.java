package com.zjzy.morebit.main.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.pojo.SystemConfigBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.ConfigListUtlis;
import com.zjzy.morebit.utils.UI.ActivityUtils;
import com.zjzy.morebit.utils.action.MyAction;

import java.util.List;

/**
 * Created by YangBoTian on 2019/7/18.
 * 超级导航
 */

public class SuperNavigationFragment extends BaseMainFragmeng {

    private View mView;
    private String mUrl;
    ShowWebFragment mShowWebFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_super_navigation, container, false);
        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && mView != null) {
            if (ConfigListUtlis.sSystemConfigBeenList == null) {
                getUrl();
            } else {
                SystemConfigBean bean = ConfigListUtlis.getSystemConfigBean(C.ConfigKey.SUPER_NAVIGATION_URL);
                if (bean != null) {
                    mUrl = bean.getSysValue();
                    addFragment(mUrl);
                }
            }

        }
    }

    private void addFragment(String url) {
        if(mShowWebFragment !=null){
            return;
        }
        mShowWebFragment = ShowWebFragment.newInstance(url);
        ActivityUtils.replaceFragmentToActivity(
                getChildFragmentManager(), mShowWebFragment, R.id.fl_webview);
    }


    public void getUrl() {
        ConfigListUtlis.getConfigList(this, C.ConfigKey.SUPER_NAVIGATION_URL, new MyAction.One<List<SystemConfigBean>>() {
            @Override
            public void invoke(List<SystemConfigBean> arg) {
                SystemConfigBean bean = ConfigListUtlis.getSystemConfigBean(C.ConfigKey.SUPER_NAVIGATION_URL);
                if (bean != null) {
                    mUrl = bean.getSysValue();
                    addFragment(mUrl);
                }
            }
        });

    }
}
