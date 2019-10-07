package com.markermall.cat.utils;

/**
 * Created by Administrator on 2018/2/23.
 */

public class JniUtils{
    public static native String getStringC();

    public static native String getToken();

    public static native String myEncrypt(String key1,String key2);

    public static native String myOldEncrypt(String key1,String key2);

    public static native String myDecrypt(String content);

//    static {
//        System.loadLibrary("MyEncrypt");//之前在build.gradle里面设置的so名字，必须一致
//    }


}
