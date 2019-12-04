package com.zjzy.morebit.utils.sys;

import android.app.Activity;

import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.fire.BuglyUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * Created by fengrs on 2018/7/7.
 * 申请权限
 */

public class RequestPermissionUtlis {
    /**
     * 申请权限
     *
     * @param activity
     * @param action
     * @param permissions
     */
    public static void requestOne(final Activity activity, final MyAction.OnResult<String> action, String... permissions) {
        requestOne(activity, action, true, permissions);
    }

    public static void requestOne(final Activity activity, final MyAction.OnResult<String> action, final boolean isgoSetting, String... permissions) {

        RxPermissions rxPermission = new RxPermissions(activity);
        rxPermission
                .requestEach(permissions)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            BuglyUtils.e("requestOne","permission state  == granted");
                            // 用户已经同意该权限
                            if (action != null) {
                                action.invoke("");
                            }
                        } else if (permission.shouldShowRequestPermissionRationale) {

                            BuglyUtils.e("requestOne","permission state  == shouldShowRequestPermissionRationale");
                            ViewShowUtils.showShortToast(activity, "请开启权限,才能使用此功能哦");
                        } else {
                            BuglyUtils.e("requestOne","permission state  == goSetting");
                            if (action != null) {
                                action.onError();
                            }
                            if (isgoSetting) {
                                AppUtil.goSetting(activity);
                            }
                        }
                    }
                });
    }
}
