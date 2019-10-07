package com.jf.my.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * sharePreference工具类                适合用于保存软件配置参数
 */
public class SharePreferenceUtil {

	private static final String SHARE_PRE_NAME = "share_pre_data";//xml文件,指定该文件的名称

	static SharedPreferences preferences;

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	private synchronized static SharedPreferences getInstance(Context context) {
		if (null == preferences) {
			preferences = context.getSharedPreferences(SHARE_PRE_NAME,
					Context.MODE_PRIVATE);
		}
		return preferences;
	}

	/***
	 * 保存
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveStringDataToSharePreference(Context context,
			String key, String value) {

		preferences = getInstance(context);

		SharedPreferences.Editor editor = preferences.edit();

		editor.putString(key, value);

		editor.commit();
	}

	/***
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveLongDataToSharePreference(Context context,
			String key, long value) {

		preferences = getInstance(context);

		SharedPreferences.Editor editor = preferences.edit();

		editor.putLong(key, value);

		editor.commit();
	}

	/***
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveBooleanDataToSharePreference(Context context,
			String key, boolean value) {

		preferences = getInstance(context);

		SharedPreferences.Editor editor = preferences.edit();

		editor.putBoolean(key, value);

		editor.commit();
	}

	/***
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveIntDataToSharePreference(Context context,
			String key, int value) {

		preferences = getInstance(context);

		SharedPreferences.Editor editor = preferences.edit();

		editor.putInt(key, value);

		editor.commit();
	}

	/***
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getStringDataByKe(Context context, String key,
			String defaultValue) {
		preferences = getInstance(context);
		return preferences.getString(key, defaultValue);
	}

	/***
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static int getIntDataByKey(Context context, String key,
			int defaultValue) {

		preferences = getInstance(context);

		return preferences.getInt(key, defaultValue);

	}

	/***
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static boolean getBooleanDataByKey(Context context, String key,
			boolean defaultValue) {

		preferences = getInstance(context);

		return preferences.getBoolean(key, defaultValue);

	}

	/***
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static long getLongDataByKey(Context context, String key,
			long defaultValue) {

		preferences = getInstance(context);

		return preferences.getLong(key, defaultValue);

	}

    public static void saveLoginInfoToPreferenceInfo(Context context, String gardenId, String gardenName, String userId, String jiaId){
        preferences = getInstance(context);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("gardenId", gardenId);
        editor.putString("gardenName", gardenName);
        editor.putString("userId", userId);
        editor.putString("jiaId", jiaId);
        editor.commit();
    }


    /**
     * 是否更新版本信息
     * @param context
     * @param isUpdate 为true更新，为false不更新
     */
    public static void saveIsUpdateVersionInfo(Context context, Boolean isUpdate){
        preferences = getInstance(context);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isUpdateVersion", isUpdate);
        editor.commit();
    }


    /**
     * 得到是否更新版本信息
     * @param context
     */
    public static Boolean getIsUpdateVersionInfo(Context context){
        preferences = getInstance(context);
        return preferences.getBoolean("isUpdateVersion", true);
    }



    /**
     * 清除sharedPreferences
     * @param context
     */
    public static void clearSharedPreferences(Context context) {

        preferences = getInstance(context);

        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();

        editor.commit();

    }
}
