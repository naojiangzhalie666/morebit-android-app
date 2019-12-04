package com.zjzy.morebit.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.zjzy.morebit.Module.push.Logger;
import com.zjzy.morebit.pojo.CircleBrand;
import com.zjzy.morebit.pojo.FloorChildInfo;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MarkermallCircleInfo;
import com.zjzy.morebit.pojo.SearchHotKeyBean;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by kenmy on 16/3/10.
 */
public class MyGsonUtils {

    private static Gson gson = null;

    //判断gson对象是否存在了,不存在则创建对象
    static {
        if (gson == null) {
            //gson = new Gson();
            //使用GsonBuilder方式时属性为空的时候输出来的json字符串是有键值key的,显示形式是"key":null，而直接new出来的就没有"key":null的
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        }
    }

    public static void clearShare(Context con) {//删除
        PreferencesHelper preferencesHelper = new PreferencesHelper(con, null);
        preferencesHelper.setString("historyData", "");
    }

    public static void writeToShare(Context con, List<Map<String, Object>> beans) {//写入historyData中
        PreferencesHelper preferencesHelper = new PreferencesHelper(con, null);
        String str = new Gson().toJson(beans);
        preferencesHelper.setString("historyData", str);
    }


    public static void writeToSeekShare(Context con, List<String> beans) {//写入historyData中
        PreferencesHelper preferencesHelper = new PreferencesHelper(con, null);
        String str = new Gson().toJson(beans);
        preferencesHelper.setString("seekData", str);
    }

    public static List<String> readToSeekShare(Context con) {//获取
        try {
            PreferencesHelper preferencesHelper = new PreferencesHelper(con, null);
            String str = preferencesHelper.getString("seekData", null);
            if (TextUtils.isEmpty(str))
                return null;
            Type type = new TypeToken<List<String>>() {
            }.getType();
            List<String> infos = new Gson().fromJson(str, type);
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Map<String, Object>> readToShare(Context con) {//获取
        try {
            PreferencesHelper preferencesHelper = new PreferencesHelper(con, null);
            String str = preferencesHelper.getString("historyData", null);
            if (TextUtils.isEmpty(str))
                return null;
            Type type = new TypeToken<List<Map<String, Object>>>() {
            }.getType();
            List<Map<String, Object>> infos = new Gson().fromJson(str, type);
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getListBeanWithResult(String resultStr, Class<?> rspClass) {

        try {
            ArrayList<Object> listRspBean = new ArrayList<>();
            Type type = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            ArrayList<JsonObject> jsonObjs = new Gson().fromJson(resultStr, type);
            for (JsonObject jsonObj : jsonObjs) {
                Object responseBean = jsonObjectToBean(jsonObj, rspClass);
                listRspBean.add(responseBean);
            }
            return listRspBean;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Object getListBeanWithString(String resultStr) {
        if (TextUtils.isEmpty(resultStr)) {
            return null;
        }
        try {
            Gson gson = new Gson();
            // json转为带泛型的list
            List<String> retList = gson.fromJson(resultStr,
                    new TypeToken<List<String>>() {
                    }.getType());
            return retList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object jsonObjectToBean(JsonObject jsonObject, Class<?> rspClass) {
        Object obj = null;
        try {
            obj = rspClass.newInstance();
            obj = new Gson().fromJson(jsonObject, rspClass);

        } catch (InstantiationException e) {
            //   Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            //   Auto-generated catch block
            e.printStackTrace();
        }

        return obj;
    }

    /**
     * 将bean转换成json
     *
     * @param object
     * @return
     */
    public static String beanToJson(Object object) {
        try {
            String jsonStr = null;
            if (gson != null) {
                jsonStr = gson.toJson(object);
            }
            return jsonStr;
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 将json转成特定的cls的对象
     *
     * @param json
     * @param cls
     * @return
     */
    public static <T> T jsonToBean(String json, Class<T> cls) {
        try {

            T t = null;
            if (gson != null) {
                t = gson.fromJson(json, cls);
            }
            return t;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * json字符串转成list
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> List<T> json2List(String gsonString, Class<T> cls) {
        try {
            List<T> list = null;
            if (gson != null) {
                list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
                }.getType());
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * json字符串转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> json2ListMap(String gsonString) {
        try {
            List<Map<String, T>> list = null;
            if (gson != null) {
                list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {
                }.getType());
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * json字符串转成map的
     *
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> json2Map(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }

    public static <T> TreeMap<Object, T> json2TreeMap(String gsonString) {
        TreeMap<Object, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<TreeMap<Object, T>>() {
            }.getType());
        }
        return map;
    }

    /**
     * 泛型参数
     */
    public static Type getSuperclassTypeParameter(Class<?> subclass) {
        try {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                Logger.e("------------解析异常 Missing type parameter.");
                return null;
                //                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        } catch (Exception e) {
            Logger.e("-------泛型解析异常");
            return null;
        }

    }

    public static ImageInfo toImageInfo(FloorChildInfo  childInfo) {
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setTitle(childInfo.getMainTitle());
        imageInfo.setId(childInfo.getId());
        imageInfo.setClassId(childInfo.getClassId());
        imageInfo.setBackgroundImage(childInfo.getBackgroundImage());
        imageInfo.setPicture(childInfo.getPicture());
        imageInfo.setUrl(childInfo.getUrl());
        imageInfo.setOpen(childInfo.getOpen());
        imageInfo.setSplicePid(childInfo.getSplicePid());
        return  imageInfo;
    }

    public static ImageInfo toImageInfo(SearchHotKeyBean childInfo) {
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setTitle(childInfo.getKeyWord());
        imageInfo.setId(childInfo.getId());
        imageInfo.setClassId(childInfo.getClassId());
        imageInfo.setUrl(childInfo.getUrl());
        imageInfo.setOpen(childInfo.getOpen());
        imageInfo.setSplicePid(childInfo.getSplicePid());
        return  imageInfo;
    }


    public static List<MarkermallCircleInfo> toMarkermallCircleInfo(List<CircleBrand> datas) {
        List<MarkermallCircleInfo> markermallCircleInfos = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            CircleBrand circleBrand =   datas.get(i);
            MarkermallCircleInfo markermallCircleInfo = new MarkermallCircleInfo();
            markermallCircleInfo.setId(circleBrand.getId());
            List<String> pics = new ArrayList<>();
            pics.add(circleBrand.getPicture());
            markermallCircleInfo.setPicture(pics);
            markermallCircleInfo.setPictureList(circleBrand.getPictureList());
            markermallCircleInfo.setIcon(circleBrand.getIcon());
            markermallCircleInfo.setName(circleBrand.getName());
            markermallCircleInfo.setIsCollection(circleBrand.getIsCollection());
            markermallCircleInfo.setCollectionId(circleBrand.getCollectionId());
            markermallCircleInfos.add(markermallCircleInfo);
        }

        return markermallCircleInfos;
    }




}
