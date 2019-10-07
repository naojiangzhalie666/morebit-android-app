package com.jf.my.LocalData;

import android.app.Activity;
import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.jf.my.pojo.PushMsgInfo;
import com.jf.my.pojo.UserInfo;
import com.jf.my.utils.SharedPreferencesUtils;

import java.util.List;

/**
 * 推送消息本地保存.
 */

public class PushMsgLocalData {
    /**
     * 获取推送消息
     * @param activity
     * @return
     */
    public static List<PushMsgInfo> getPushList(Context activity){
        String msgJson = (String) SharedPreferencesUtils.get(activity,"PushMsgList","");
        if(msgJson!=null && !"".equals(msgJson)){
            try {
                List<PushMsgInfo> arrList = JSON.parseArray(msgJson, PushMsgInfo.class);
                return arrList;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 保存推送消息
     * @param activity
     * @param msgJson
     */
    public static void setPushList(Context activity,String msgJson){
        SharedPreferencesUtils.put(activity,"PushMsgList",msgJson);
    }
}
