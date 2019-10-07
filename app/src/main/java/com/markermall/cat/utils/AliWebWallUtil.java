package com.markermall.cat.utils;

import com.alibaba.wireless.security.jaq.JAQException;
import com.alibaba.wireless.security.jaq.avmp.IJAQAVMPSignComponent;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.markermall.cat.App;

import java.io.UnsupportedEncodingException;

/**
 * 阿里云WebApp防火墙
 */

public class AliWebWallUtil {

    private static int VMP_SIGN_WITH_GENERAL_WUA2 = 3;
    public static synchronized String avmpSign(String inputData) {
        // Perform action on click
        String input = inputData;  // : 待签名的数据
        try {
            if (initAVMP() == false) {
                return null;
            }
            byte[] result = jaqVMPComp.avmpSign(VMP_SIGN_WITH_GENERAL_WUA2, input.getBytes("UTF-8"));
            String signResult = new String(result, "UTF-8");
            return signResult;
        } catch (JAQException e) {
            LogUtils.Log("WAF", "avmp sign failed with errorCode=" + e.getErrorCode());
        }catch (UnsupportedEncodingException e){
            LogUtils.Log("WAF", "UnsupportedEncodingException exception error !!!");
        }
        return null;
    }

    private static IJAQAVMPSignComponent jaqVMPComp = null;
    // : 保证线程同步，调用AVMP的接口全局请只初始化一次即可，避免反复初始化造成不必要的开销。
    private static synchronized boolean initAVMP() {
        boolean result = false;
        try {
            if (jaqVMPComp != null) {
                LogUtils.Log("WAF", "AVMP instance has been initialized");
                return true;
            }
            jaqVMPComp = SecurityGuardManager.getInstance(App.getAppContext()).getInterface(IJAQAVMPSignComponent.class);
            result = jaqVMPComp.initialize();
        } catch (JAQException e) {
            LogUtils.Log("WAF", "init failed with errorCode " + e.getErrorCode());
        } catch (Exception e) {
            LogUtils.Log("WAF", "unkown exception has occured");
            e.printStackTrace();
        }
        return result;
    }
}
