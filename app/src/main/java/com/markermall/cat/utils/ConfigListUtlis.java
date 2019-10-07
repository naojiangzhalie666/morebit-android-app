package com.markermall.cat.utils;

import android.text.TextUtils;

import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.SystemConfigBean;
import com.markermall.cat.pojo.request.RequestConfigKeyBean;
import com.markermall.cat.utils.action.MyAction;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangBoTian on 2019/7/18.
 */

public class ConfigListUtlis {
    public static List<SystemConfigBean> sSystemConfigBeenList=null;
    public static void getConfigList(RxAppCompatActivity activity, String  key, final MyAction.One<List<SystemConfigBean>> action) {
       if(sSystemConfigBeenList == null){
           RequestConfigKeyBean requestConfigKeyBean = new RequestConfigKeyBean();
           requestConfigKeyBean.setKeys(key);
           RxHttp.getInstance().getSysteService().getConfigList(requestConfigKeyBean)
                   .compose(RxUtils.<BaseResponse<List<SystemConfigBean>>>switchSchedulers())
                   .compose(activity.<BaseResponse<List<SystemConfigBean>>>bindToLifecycle())
                   .subscribe(new DataObserver<List<SystemConfigBean>>() {
                       @Override
                       protected void onSuccess(List<SystemConfigBean> data) {
                           sSystemConfigBeenList = data;
                           if(action!=null){
                               action.invoke(data);
                           }

                       }
                   });
       } else {
           action.invoke(sSystemConfigBeenList);
       }

    }
    public static void getConfigList(RxFragment fragment, String  key, final MyAction.One<List<SystemConfigBean>> action) {
        if(sSystemConfigBeenList == null){
            RequestConfigKeyBean requestConfigKeyBean = new RequestConfigKeyBean();
            requestConfigKeyBean.setKeys(key);
            RxHttp.getInstance().getSysteService().getConfigList(requestConfigKeyBean)
                    .compose(RxUtils.<BaseResponse<List<SystemConfigBean>>>switchSchedulers())
                    .compose(fragment.<BaseResponse<List<SystemConfigBean>>>bindToLifecycle())
                    .subscribe(new DataObserver<List<SystemConfigBean>>() {
                        @Override
                        protected void onSuccess(List<SystemConfigBean> data) {
                            sSystemConfigBeenList = data;
                            if(action!=null){
                                action.invoke(data);
                            }

                        }
                    });
        } else {
            action.invoke(sSystemConfigBeenList);
        }

    }

    public static void getConfigListCacheNet(RxAppCompatActivity activity, String  key, final MyAction.One<List<SystemConfigBean>> action) {
        if(sSystemConfigBeenList != null){
            action.invoke(sSystemConfigBeenList);
        }
        RequestConfigKeyBean requestConfigKeyBean = new RequestConfigKeyBean();
        requestConfigKeyBean.setKeys(key);
        RxHttp.getInstance().getSysteService().getConfigList(requestConfigKeyBean)
                .compose(RxUtils.<BaseResponse<List<SystemConfigBean>>>switchSchedulers())
                .compose(activity.<BaseResponse<List<SystemConfigBean>>>bindToLifecycle())
                .subscribe(new DataObserver<List<SystemConfigBean>>() {
                    @Override
                    protected void onSuccess(List<SystemConfigBean> data) {
                        for (int i = 0; i < data.size(); i++) {
                            putSystemConfigBean(data.get(i));
                        }
                        if(action!=null){
                            action.invoke(sSystemConfigBeenList);
                        }

                    }
                });
    }

    public static void getConfigList(String  key, final MyAction.One<List<SystemConfigBean>> action) {
        if(sSystemConfigBeenList == null){
            RequestConfigKeyBean requestConfigKeyBean = new RequestConfigKeyBean();
            requestConfigKeyBean.setKeys(key);
            RxHttp.getInstance().getSysteService().getConfigList(requestConfigKeyBean)
                    .compose(RxUtils.<BaseResponse<List<SystemConfigBean>>>switchSchedulers())
                    .subscribe(new DataObserver<List<SystemConfigBean>>() {
                        @Override
                        protected void onError(String errorMsg, String errCode) {
                            super.onError(errorMsg, errCode);
                            if(action!=null){
                                action.invoke(null);
                            }
                        }

                        @Override
                        protected void onSuccess(List<SystemConfigBean> data) {
                            sSystemConfigBeenList = data;
                            if(action!=null){
                                action.invoke(data);
                            }

                        }
                    });
        } else {
            action.invoke(sSystemConfigBeenList);
        }
    }

    public static String getConfigAllKey(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(C.ConfigKey.SUPER_NAVIGATION_URL);
        stringBuffer.append(","+C.ConfigKey.CATEGORY_URL);
        stringBuffer.append(","+C.ConfigKey.NOTICE);
        stringBuffer.append(","+C.ConfigKey.HOME_RECOMMENDED_TITLE);
        stringBuffer.append(","+C.ConfigKey.TODAY_RECOMMENDED_TITLE);
        stringBuffer.append(","+C.ConfigKey.ITEM_DETAILS_RECOMMENDED_TITLE);
        stringBuffer.append(","+C.ConfigKey.ORDER_TRACKING);
        stringBuffer.append(","+C.ConfigKey.ORDER_BIG_DATA_PUSH_SYSTEM);
        stringBuffer.append(","+ C.ConfigKey.WEB_WITHDRAW_TIME);

        return stringBuffer.toString();
    }

    public static SystemConfigBean getSystemConfigBean(String configKey){
        SystemConfigBean systemConfigBean=null;
        if(sSystemConfigBeenList==null|| TextUtils.isEmpty(configKey)){
            return null;
        }
        for (int i=0;i<sSystemConfigBeenList.size();i++){
            if(configKey.equals(sSystemConfigBeenList.get(i).getSysKey())){
                systemConfigBean = sSystemConfigBeenList.get(i);
            }
        }
        return systemConfigBean;
    }

    public static void putSystemConfigBean(SystemConfigBean data){
        if(sSystemConfigBeenList==null){
            sSystemConfigBeenList = new ArrayList<>();
        }
        boolean isFind = false;
        for (int i=0;i<sSystemConfigBeenList.size();i++){
            String key = data.getSysKey();
            if(key.equals(sSystemConfigBeenList.get(i).getSysKey())){
                isFind = true;
                sSystemConfigBeenList.set(i,data);
            }
            if(!isFind){
                sSystemConfigBeenList.add(data);
            }
        }
    }

}
