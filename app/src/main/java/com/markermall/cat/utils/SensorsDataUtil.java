package com.markermall.cat.utils;

import android.webkit.WebView;

import com.markermall.cat.App;
import com.markermall.cat.LocalData.UserLocalData;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.util.JsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description: 大数据埋点
 **/
public class SensorsDataUtil {
    private static final String TAG = "SensorsDataAPI";
    private static SensorsDataAPI sensorsDataAPI = null;
    private static SensorsDataUtil sensorsDataUtil = null;
    private static boolean initSDk = false;

    public static SensorsDataUtil getInstance() {
        if (sensorsDataUtil == null) {
            synchronized (SensorsDataUtil.class) {
                if (sensorsDataUtil == null) {
                    sensorsDataUtil = new SensorsDataUtil();
                }
                if (sensorsDataAPI == null) {
                    sensorsDataAPI = SensorsDataAPI.sharedInstance();
                }

            }
        }
        return sensorsDataUtil;
    }

    /**
     * 埋点sdk初始化
     */
    public static void initBidDataAPI() {
        initSDk = true;
        try {
            SensorsDataAPI.sharedInstance(App.getAppContext(), "");
            List<SensorsDataAPI.AutoTrackEventType> eventTypeList = new ArrayList<>();
            eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_START);
            eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_END);
            eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_VIEW_SCREEN);
            eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_CLICK);
            SensorsDataAPI.sharedInstance(App.getAppContext()).enableAutoTrack(eventTypeList);
            SensorsDataAPI.sharedInstance(App.getAppContext()).enableLog(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void catalogClickTrack(String userId, String catalogId, String catalogName) {
        if (isPust()) {
            try {
                sensorsDataAPI.catalogClickTrack("", catalogId, catalogName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 搜索弹窗
     *
     * @param searchKey
     */
    public void dialogSearch(String searchKey) {
        if (isPust()) {
            try {
                sensorsDataAPI.dialogSearch(UserLocalData.getUser().getId(), searchKey, UserLocalData.getUser().getPhone());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 点击搜索按钮
     *
     * @param searchKey
     * @param type
     */
    public void searchKeyTrack(String searchKey, int type) {
        if (isPust()) {
            try {
                sensorsDataAPI.searchKeyTrack(UserLocalData.getUser().getId(), searchKey, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 首页-广告位,首页-闪屏页，首页活动弹窗点击
     */
    public void advClickTrack(String title, String clickId, String type, String model, int position, String classId, String url) {
        if (isPust()) {
            try {
                sensorsDataAPI.advClickTrack(UserLocalData.getUser().getId(), title, UserLocalData.getUser().getPhone(),
                        clickId, type, model, position + 1, "", url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 首页曝光Item
     */
    public void exposureViewTrack( String model) {
        if (isPust()) {
            try {

                sensorsDataAPI.exposureViewTrack(UserLocalData.getUser().getId(), UserLocalData.getUser().getPhone(),
                        UserLocalData.getUser().getToken(),model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * 蜜粉圈广告位
     */
    public void advClickTrack(String clickId, String level_1, String level_2, String type, String model, int position, String title, String classId, String url, String subTitle) {
        if (isPust()) {
            try {
                sensorsDataAPI.advClickTrack(UserLocalData.getUser().getId(), UserLocalData.getUser().getPhone(),
                        UserLocalData.getUser().getToken(),clickId, level_1,level_2,type, model, position + 1, title, classId,url,subTitle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * 首页-楼层
     */
    public void advClickTrack(String clickId, String type, String model, int position, String title, String classId, String url, String subTitle) {

        if (isPust()) {
            try {
                sensorsDataAPI.advClickTrack(UserLocalData.getUser().getId(), UserLocalData.getUser().getPhone(),
                        clickId, type, model, position + 1, title,classId, url,subTitle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * 首页-马克猫热门
     *
     * @param title
     */
    public void hotClick(String title) {
        if (isPust()) {
            try {
                sensorsDataAPI.hotClick(UserLocalData.getUser().getId(), title, UserLocalData.getUser().getPhone());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 商学院-为你推荐
     *
     * @param title
     */
    public void mifenClickTrack(String level_1, String level_2, String model, int position, String productId, String productName, String type, String classId, String url, String title, String clickId) {
        if (isPust()) {
            try {

                sensorsDataAPI.mifenClickTrack(UserLocalData.getUser().getId(), UserLocalData.getUser().getPhone(),UserLocalData.getUser().getToken(),level_1,level_2,model,position+1,productId,productName,type,classId,url,title,clickId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 蜜粉圈-item/分享点击
     *
     * @param title
     */
    public void mifenClickTrack( String level_1, String level_2, String model, int position, String productId, String productName, String type, String classId, String url, String title, String subTitle, int shareClick, String clickId) {
        if (isPust()) {
            try {
                sensorsDataAPI.mifenClickTrack(UserLocalData.getUser().getId(), UserLocalData.getUser().getPhone(),UserLocalData.getUser().getToken(),
                        level_1,level_2,model,position,productId,productName,type,classId,url,title,subTitle,shareClick,clickId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public void setUserInfo() {
        if (isPust()) {
            try {
                sensorsDataAPI.setUserInfo(UserLocalData.getUser().getId(), UserLocalData.getUser().getPhone(), UserLocalData.getToken());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 意见反馈
     *
     * @param userId
     * @param suggestion
     */
    public void setSuggestionTrack(String userId, String suggestion) {
        if (isPust()) {
            try {
                sensorsDataAPI.setSuggestionTrack(userId, suggestion);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 浏览商品
     */
    public void browseProductTrack(String userId, String productId) {
        if (isPust()) {
            try {
                sensorsDataAPI.browseProductTrack(userId, productId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 首页蜜粉推荐 浏览商品
     */
    public void browseProductTrack(String model, int position, String productId, String productName, String productPrice, String type, String classId, String url, String title){
        if (isPust()) {
            try {
                sensorsDataAPI.browseProductTrack(UserLocalData.getUser().getId(), UserLocalData.getUser().getPhone(),UserLocalData.getUser().getToken(),
                        model,position,productId,productName,productPrice,type,classId,url,title);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 收藏商品
     */
    public void collectProductTrack(String userId, String productId, String productName, String productPrice) {
        if (isPust()) {
            try {
                sensorsDataAPI.collectProductTrack(userId, productId, productName, productPrice);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 立即购买
     */
    public void buy(String userId, String couponsId, String productId, String productName, String productPrice) {
        if (isPust()) {
            try {
                sensorsDataAPI.buy(userId, couponsId, productId, productName, productPrice);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * webview埋点
     */
    public void showUpWebView(WebView webView, boolean isSupportJellyBean) {
        if (isPust()) {
            try {
                sensorsDataAPI.showUpWebView(webView, isSupportJellyBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 业务活动
     */
    public void setAcitivityClickTrack(String userId, String activityId) {
        if (isPust()) {
            try {
                sensorsDataAPI.setAcitivityClickTrack(userId, activityId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * web界面上传
     */
    public void injectJsSdk(WebView view) {
        if (isPust()&&view!=null) {
            try {
                JsUtil.injectJsSdk(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 首页-弹窗
     */
    public void exposureViewTrack(String model, String type, String classId, String url, String title,String clickId) {
        if (isPust()) {
            try {
                sensorsDataAPI.exposureViewTrack(UserLocalData.getUser().getId(), UserLocalData.getUser().getPhone(), UserLocalData.getUser().getToken(),
                        model,type,classId,url,title,clickId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * 是否上传
     */
    public boolean isPust() {
        return initSDk && sensorsDataAPI != null;
    }

}
