package com.jf.my.main.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jf.my.R;
import com.jf.my.fragment.base.BaseMainFragmeng;
import com.jf.my.pojo.SystemConfigBean;
import com.jf.my.utils.C;
import com.jf.my.utils.ConfigListUtlis;
import com.jf.my.utils.UI.ActivityUtils;
import com.jf.my.utils.action.MyAction;

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
