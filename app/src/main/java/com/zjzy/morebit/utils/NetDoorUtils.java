package com.zjzy.morebit.utils;

import java.util.UUID;

/**
 * 接口密钥加工类
 */
public class NetDoorUtils {

    /**
     * 获取时分秒的系统时间戳
     *
     * @return
     */
    public static String getSysTime() {
        String getTime = "" + System.currentTimeMillis() / 1000;
        return getTime;
    }

    /**
     * 获取32位随机数
     *
     * @return
     */
    public static String getNonceStr() {
        String nonceStr = UUID.randomUUID().toString().trim();
        nonceStr = nonceStr.replaceAll("-", "");
        return nonceStr;
    }


    /**
     * 获取sha1旧的加密串加密字符串
     * @param timeStr
     * @param nstr
     * @return
     */
    public static String getOldSha1Str(String timeStr,String nstr){
        Sha1Utils sha1Utils = new Sha1Utils();
        return sha1Utils.Digest(JniUtils.myOldEncrypt(timeStr,nstr));
    }

}
