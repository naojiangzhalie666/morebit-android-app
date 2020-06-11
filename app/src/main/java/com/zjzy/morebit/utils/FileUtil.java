package com.zjzy.morebit.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;


public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

    public static File getFilesDir(Context context) {
        return context.getFilesDir();
    }

    /**
     * sd卡是否挂载
     *
     * @return
     */
    public static boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取sd卡路径
     *
     * @return
     */
    public static File getExternalStorageDirectory() {
        return Environment.getExternalStorageDirectory();
    }

    public static File getExternalFile(Context context, String fileName) {
        return new File(FileUtil.getExternalFilesDir(context, ""), fileName);
    }
    public static File getExternalVideoFile(Context context) {
        return  FileUtil.getExternalFilesDir(context, "video") ;
    }

    /**
     * 获取app在sd卡上的程序目录创建的新目录，其中程序目录为xxx/android/data/pkg/files/
     * 为避免空指针，系统方法返回null时，自己拼接路径
     *
     * @param context
     * @param type    用户指定的子目录
     * @return
     */
    public static File getExternalFilesDir(Context context, String type) {
        File file = null;

        try {
            file = context.getExternalFilesDir(type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (file == null) {
            if (type == null) {
                file = new File(getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + context.getPackageName() + "/files");
            } else {
                file = new File(getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + context.getPackageName() + "/files/" + type);
            }
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getGuidFile() {
        File file = null;

        if (file == null) {
            file = new File(getExternalStorageDirectory().getAbsolutePath() + "/lzyp/files");
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取app在sd卡上的程序目录创建的新目录，其中程序目录为xxx/android/data/pkg/cache/
     * 为避免空指针，系统方法返回null时，自己拼接路径
     *
     * @param context
     * @return
     */
    public static File getExternalCacheDir(Context context) {
        File file = context.getExternalCacheDir();
        if (file == null) {
            file = new File(getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + context.getPackageName() + "/cache");
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return file;
    }

    /**
     * 获取app在sd卡上的程序目录创建的新目录，其中程序目录为xxx/android/data/pkg/cache/
     * 为避免空指针，系统方法返回null时，自己拼接路径
     *
     * @param context
     * @param path    用户指定的子目录
     * @return
     */
    public static File getExternalPhotoFile(Context context, String dir, String path) {
        File file;
        file = new File(getExternalStorageDirectory().getAbsolutePath() +"/"+ context.getPackageName() + "/cache/" + dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File(getExternalStorageDirectory().getAbsolutePath() +"/"+ context.getPackageName() + "/cache/" + dir, path);
        return file1;
    }

    /**
     * 保存文件
     *
     * @param file
     * @param data
     * @return
     */
    public static boolean save(File file, byte[] data) {
        OutputStream os = null;
        try {
            os = openOutputStream(file);
            os.write(data, 0, data.length);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 生成海报路径
     * @return
     */
    public static String getScreenPosterUrl(String name,Context context){
       return FileUtil.getExternalFilesDir(context, "screen_shot").getAbsolutePath() + "/"+name+".jpg";
    }

    public static byte[] getByteArrayFromInputStream(InputStream in) {
        final byte[] buffer = new byte[1024];
        int byteread = 0;
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            out.flush();
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(in, out);
        }
        return null;
    }

    public static byte[] getByteArrayFromFile(File file) {
        if (file != null && file.isFile()) {
            FileInputStream in = null;
            try {
                return getByteArrayFromInputStream(new FileInputStream(file));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean mkParentDir(final File file) {
        final File parentDir = file.getParentFile();
        if (!parentDir.exists() || !parentDir.isDirectory()) {
            return parentDir.mkdirs();
        }
        return true;
    }

    public static boolean saveByteArrayToFile(byte[] data, File file) {
        if (file != null && data != null) {
            if (!mkParentDir(file)) {
                return false;
            }

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(data);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeStream(fos);
            }
        }
        return false;
    }

    public static boolean saveObjectToFile(Object obj, File file) {
        if (obj == null || file == null) {
            return false;
        }

        file.deleteOnExit();

        if (!mkParentDir(file)) {
            return false;
        }

        ObjectOutputStream out = null;
        final long start = System.currentTimeMillis();
        try {
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            out.writeObject(obj);
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            if (file.isFile()) {
                file.delete();
            }
        } finally {
            closeStream(out);
           // LZLog.d(TAG, "saveObjectToFile spend " + (System.currentTimeMillis() - start) + " " + file.getAbsolutePath());
        }
        return false;
    }

    public static boolean saveInputStreamToFile(InputStream in, File file) {
        if (in == null || file == null) {
            return false;
        }

        if (!mkParentDir(file)) {
            return false;
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            copyStream(in, out);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            if (file.isFile()) {
                file.delete();
            }
        } finally {
            closeStream(out);
        }
        return false;
    }

    public static Object getObjectFromFile(File file) {
        if (file != null && file.isFile() && file.exists()) {
            ObjectInputStream in = null;
            final long start = System.currentTimeMillis();
            try {
                in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
                return in.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeStream(in);
               // LZLog.d(TAG, "getObjectFromFile spend " + (System.currentTimeMillis() - start) + " " + file.getAbsolutePath());
            }
        }
        return null;
    }

    public static void closeStream(InputStream in) {
        closeStream(in, null);
    }

    public static void closeStream(OutputStream out) {
        closeStream(null, out);
    }

    public static void closeStream(Reader reader) {
        closeStream(reader, null);
    }

    public static void closeStream(Writer writer) {
        closeStream(null, writer);
    }

    /**
     * 关闭流
     *
     * @param in
     * @param out
     */
    public static void closeStream(InputStream in, OutputStream out) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭流
     */
    public static void closeStream(Reader reader, Writer writer) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 合并文件(把assets目录下的多个小文件合并成一个大文件，因其中的文件大小不允许超过1M)
     *
     * @param context
     * @param partFileList 小文件名集合
     * @param dst          目标文件路径
     * @throws IOException
     * @author zuolongsnail
     */
    public static void mergeAssetsFiles(Context context, ArrayList<String> partFileList, String dst) throws IOException {
        File dstFile = new File(dst);
        if (dstFile.exists()) {
            dstFile.delete();
        }

        OutputStream out = new FileOutputStream(dst);
        byte[] buffer = new byte[1024];
        InputStream in;
        int readLen = 0;
        for (int i = 0; i < partFileList.size(); i++) {
            // 获得输入流
            in = openAssetsInput(context, partFileList.get(i));
            while ((readLen = in.read(buffer)) != -1) {
                out.write(buffer, 0, readLen);
            }
            out.flush();
            in.close();
        }
        // 把所有小文件都进行写操作后才关闭输出流，这样就会合并为一个文件了
        out.close();
    }

    public static InputStream openAssetsInput(Context context, String assetsName)
            throws IOException {
        return context.getAssets().open(assetsName, AssetManager.ACCESS_STREAMING);
    }

    public static boolean copyFile(InputStream oldFileStream, String newPath) {
        if (oldFileStream == null) {
            return false;
        }

        FileOutputStream fs = null;

        try {
            int byteread = 0;
            // 文件存在时
            fs = new FileOutputStream(new File(newPath));
            byte[] buffer = new byte[1444];
            while ((byteread = oldFileStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
            fs.flush();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (oldFileStream != null) {
                    oldFileStream.close();
                }
                if (fs != null) {
                    fs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean copyAssertFile(Context context, String assetFilePath, File destFile) {
        mkParentDir(destFile);
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = context.getAssets().open(assetFilePath);
            out = new FileOutputStream(destFile);
            copyStream(in, out);
            return true;
        } catch (IOException e) {
           // LZLog.w(TAG, e.getMessage());
        } finally {
            closeStream(in, out);
        }
        return false;
    }



    public static void copyStream(final InputStream in, final OutputStream out) throws IOException {
        copyStream(in, out, 1024 * 8);
    }

    public static void copyStream(final InputStream in, final OutputStream out, final int bufferSize) throws IOException {
        final byte[] buffer = new byte[bufferSize];
        int readBytes = 0;
        while ((readBytes = in.read(buffer)) != -1) {
            out.write(buffer, 0, readBytes);
        }
        out.flush();
    }

    public static void cleanDirectory(File directory) {
        if (!directory.exists()) {
           // LZLog.w(TAG, directory + " does not exist");
            return;
        }

        if (!directory.isDirectory()) {
           // LZLog.w(TAG, directory + " is not a directory");
            return;
        }

        final File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            try {
                forceDelete(file);
            } catch (Exception e) {
              //  LZLog.w(TAG, e);
            }
        }
    }

    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent) {
                    throw new FileNotFoundException("File does not exist: "
                            + file);
                }
                String message = "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }

    public static void deleteDirectory(File directory) throws IOException {
        cleanDirectory(directory);

        if (!directory.delete()) {
            String message = "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file
                        + "' exists but is a directory");
            }
            if (!file.canRead()) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file
                    + "' does not exist");
        }
        return new FileInputStream(file);
    }



    public static FileOutputStream openOutputStream(String filePath)
            throws IOException {
        return openOutputStream(new File(filePath));
    }

    public static FileOutputStream openOutputStream(File file)
            throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file
                        + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw new IOException("File '" + file
                        + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                if (!parent.mkdirs()) {
                    throw new IOException("File '" + file
                            + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file);
    }
    public static String getExternalCacheDirPath(Context context) {
        File externalCacheDir = FileUtil.getExternalCacheDir(context);
        return externalCacheDir.getAbsolutePath();
    }

    public static String getCreateFileReturnAllName(String url){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        final long startTime = System.currentTimeMillis();
        Log.i(TAG, "startTime=" + startTime);
        //下载函数
        String filename;
        if (url.contains("?")) {
            String ustart = url.split("\\?")[0];
            if (ustart != null) {
                filename = ustart.substring(url.lastIndexOf("/") + 1);
            } else {
                filename = "lzyp_" + SystemClock.currentThreadTimeMillis();
            }
        } else {
            filename = url.substring(url.lastIndexOf("/") + 1);
        }
        File pathFile=new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        String filePath=path + "/lz_" + filename;
        File file1 = new File(filePath);
        if (!file1.exists()) {
            boolean newFile = false;
            try {
                newFile = file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
           // LZLog.i(TAG,"newFile="+newFile);
        }
        return filePath;
    }
}
