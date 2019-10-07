package com.markermall.cat.contact;

import android.os.Environment;

/**
 * Created by fengrs on 2017/10/23.
 */

public class SdDirPath {
    public static String SaveName = "MARKERMALL5/";
    public static String BasePath = Environment.getExternalStorageDirectory().getPath();

    public static String IMAGE_CACHE_PATH = BasePath + "/MARKERMALL5/";

    public static String APK_DOWNLOAD_CACHE_PATH = BasePath + "/MARKERMALL5/";

    public static String APK_CACHE_PATH = BasePath + "/MARKERMALL5/";

}
