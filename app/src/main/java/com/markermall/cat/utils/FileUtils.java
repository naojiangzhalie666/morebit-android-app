package com.markermall.cat.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.WindowManager;

import com.markermall.cat.App;
import com.markermall.cat.LocalData.UserLocalData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {
    public static final String IMAGE_SUFFIX = ".png";

    /**
     * Save image to the SD card
     *
     * @param photoBitmap
     * @param photoName
     * @param path
     */
    public static String savePhoto(Bitmap photoBitmap, String path,
                                   String photoName) {
        String localPath = null;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            File photoFile = null;
            FileOutputStream fileOutputStream = null;
            try {
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                photoFile = new File(path, photoName + ".jpg");
                if (photoFile.exists()) {
                    photoFile.delete();
                }

                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                            fileOutputStream)) {
                        localPath = photoFile.getPath();
                        fileOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                localPath = null;
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                localPath = null;
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                        fileOutputStream = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return localPath;
    }

    /**
     * ת��ͼƬ��Բ��
     *
     * @param bitmap  ����Bitmap����
     * @param tempUri
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap, Uri tempUri) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            left = 0;
            top = 0;
            right = width;
            bottom = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// ���û����޾��

        canvas.drawARGB(0, 0, 0, 0); // �������Canvas
        paint.setColor(color);

        // ���������ַ�����Բ,drawRounRect��drawCircle
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);//
        // ��Բ�Ǿ��Σ���һ������Ϊͼ����ʾ���򣬵ڶ��������͵����������ֱ���ˮƽԲ�ǰ뾶�ʹ�ֱԲ�ǰ뾶��
        canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// ��������ͼƬ�ཻʱ��ģʽ,�ο�http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); // ��Mode.SRC_INģʽ�ϲ�bitmap���Ѿ�draw�˵�Circle

        return output;
    }

    /**
     * 从路径字符串中获取文件名称 不带后续格式
     *
     * @param pathStr
     * @return
     */
    public static String getPathStringForFileName(String pathStr) {
        int start = pathStr.lastIndexOf("/");
        int end = pathStr.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathStr.substring(start + 1, end);
        } else {
            return null;
        }
    }

    /**
     * 把本地文件转成byte数组
     *
     * @param path
     * @return
     */
    public static byte[] getBytesFromFile(String path) {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(path);
            byte[] b = new byte[1024];
            int num = -1;
            bos = new ByteArrayOutputStream();
            while ((num = fis.read(b)) != -1) {
                bos.write(b, 0, num);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 保存字节数组到文件
     *
     * @param b
     * @param path
     */
    public static void saveBytesToFile(byte[] b, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(b);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 获取网络图片名称
//     *
//     * @param picture
//     * @return
//     */
//    public static String getPictureName(String picture) {
//        if (picture == null) {
//            return System.currentTimeMillis() + "";
//        }
//        try {
//            if (TextUtils.isEmpty(picture.substring(picture.lastIndexOf("/") + 1, picture.lastIndexOf(".") - 1))) {
//                return System.currentTimeMillis() + "";
//            } else {
//                String userHead = "";
//                if (!TextUtils.isEmpty(UserLocalData.mUserhead)) {
//                    userHead = UserLocalData.mUserhead.substring(UserLocalData.mUserhead.lastIndexOf("/") + 1, UserLocalData.mUserhead.lastIndexOf(".") - 1);
////                    userHead = "http://zhituapi.oss-cn-beijing.aliyuncs.com/business/zhitu_rBMFGb1VmpIb/1.png".substring(picture.lastIndexOf("/") + 1, picture.lastIndexOf(".") - 1);
//                }
//                String phone = UserLocalData.getUser(App.getAppContext()).getPhone();
//
//                String s = picture.substring(picture.lastIndexOf("/") + 1, picture.lastIndexOf(".") - 1) + userHead + phone;
//                if (s.contains(".")) {
//                    return System.currentTimeMillis() + "";
//                }
//                if (!s.contains("/")) {
//                    return s;
//                }
//
//                //  om/business/zh
//                String[] split = s.split("/");
//                if (split == null) {
//                    return s;
//                }
//                if (!TextUtils.isEmpty(split[0])) {
//                    return System.currentTimeMillis() + split[0];
//                } else {
//                    return System.currentTimeMillis() + "";
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return System.currentTimeMillis() + "";
//        }
//
//    }

    /**
     * 获取网络图片名称
     *
     * @param picture
     * @return
     */
    public static String getPictureName(String picture) {
        String name = "img_";// 必须要字母开头才能系统分享
        if (picture == null) {
            name = name + System.currentTimeMillis();
        }
        try {
            if (TextUtils.isEmpty(picture.substring(picture.lastIndexOf("/") + 1, picture.lastIndexOf(".")))) {
                name = name + System.currentTimeMillis();
            } else {
                String userHead = "";
                if (!TextUtils.isEmpty(UserLocalData.mUserhead) && UserLocalData.mUserhead.contains(".")) {
                    try {
                        userHead = UserLocalData.mUserhead.substring(UserLocalData.mUserhead.lastIndexOf("/") + 1, UserLocalData.mUserhead.lastIndexOf("."));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                String phone = UserLocalData.getUser(App.getAppContext()).getPhone();
                userHead = userHead + phone;
                String s = picture.substring(picture.lastIndexOf("/") + 1, picture.lastIndexOf(".")) + userHead;
                String regEx = "[^a-z0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(s);
                s = m.replaceAll("").trim();
                if (TextUtils.isEmpty(s)) {
                    name = name + System.currentTimeMillis();
                } else {
                    name = name + s;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            name = name + System.currentTimeMillis();
        }
        return name;
    }

    /**
     * 截取图片的名称
     *
     * @param picture
     * @return
     */
    public static String interceptingPictureName(String picture) {
        if (TextUtils.isEmpty(picture)) return "";
         String name ="";
        try {
            name = picture.substring(picture.lastIndexOf("/") + 1, picture.lastIndexOf("."));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 更新图片到图库
     */
    public static void updaImgToMedia(Context context, String path, String bitName) {
        try {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path + "/" + bitName)));
            MediaStore.Images.Media.insertImage(context.getContentResolver(), new File(path + "/" + bitName).getAbsolutePath() + ".jpg", bitName + ".jpg", null);
            // 发送广播，通知刷新图库的显示

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getRealFilePath(Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }

                }
                cursor.close();
            }
            if (data == null) {
                data = getImageAbsolutePath(context, uri);
            }

        }
        return data;
    }

    public static Uri getUri(final String filePath) {
        return Uri.fromFile(new File(filePath));
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param context
     * @param imageUri
     * @author yaoxing
     * @date 2014-10-12
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Context context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 判断文件是否存在
     * @param path
     * @return
     */
    public static boolean isFileExit(String path){
        if(path == null){
            return false;
        }
        try{
            File f = new File(path);
            if(!f.exists()){
                return false;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     *  回去 适配路径
     * @return
     */
    @NonNull
    public static String getSDDataVideoPath(Context context) {
        //sd卡是否存在
        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
            try {
                path = context.getExternalCacheDir().getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(path)) {
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            }
        } else {
            path = context.getCacheDir().getAbsolutePath();
        }


        File appDir = new File(path);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        return appDir + File.separator + "video";
    }


    /**
     * 计算出图片初次显示需要放大倍数
     * @param imagePath 图片的绝对路径
     */
    public static float getImageScale(Context context, String imagePath){
        if(TextUtils.isEmpty(imagePath)) {
            return 2.0f;
        }

        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeFile(imagePath);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }

        if(bitmap == null) {
            return 2.0f;
        }

        // 拿到图片的宽和高
        int dw = bitmap.getWidth();
        int dh = bitmap.getHeight();

        WindowManager wm = ((Activity)context).getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        float scale = 1.0f;
        //图片宽度大于屏幕，但高度小于屏幕，则缩小图片至填满屏幕宽
        if (dw > width && dh <= height) {
            scale = width * 1.0f / dw;
        }
        //图片宽度小于屏幕，但高度大于屏幕，则放大图片至填满屏幕宽
        if (dw <= width && dh > height) {
            scale = width * 1.0f / dw;
        }
        //图片高度和宽度都小于屏幕，则放大图片至填满屏幕宽
        if (dw < width && dh < height) {
            scale = width * 1.0f / dw;
        }
        //图片高度和宽度都大于屏幕，则缩小图片至填满屏幕宽
        if (dw > width && dh > height) {
            scale = width * 1.0f / dw;
        }
        bitmap.recycle();
        return scale;
    }
}
