package com.zjzy.morebit.utils.UI;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/11/8 19:20
 * Summary:
 */
public class GsonUtil {
    private static Gson mGson;
    static {
        if (mGson == null) {
            mGson = new Gson();
        }
    }

    private GsonUtil() {
    }

    /**
     * 将object对象转成json字符串
     *
     * @param object
     * @return
     */
    public static String gsonString(Object object) {
        String gsonString = null;
        if (mGson != null) {
            gsonString = mGson.toJson(object);
        }
        return gsonString;
    }


    /**
     * 将gsonString转成泛型bean
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T gsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (mGson != null) {
            try {
                t = mGson.fromJson(gsonString, cls);
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        return t;
    }

    public static <T> T gsonToBean(String gsonString, Type typeOfT) {
        T t = null;
        if (mGson != null) {
            try {
                t = mGson.fromJson(gsonString, typeOfT);
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        return t;
    }


}
