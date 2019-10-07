package com.markermall.cat.utils.appDownload;

import android.os.Environment;
import android.support.annotation.NonNull;

import com.markermall.cat.utils.LogUtils;
import com.markermall.cat.utils.appDownload.update_app.HttpManager;
import com.markermall.cat.utils.appDownload.update_app.UpdateAppBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Vector
 * on 2017/6/19 0019.
 */

public class UpdateAppHttpUtil implements HttpManager {
    private UpdateAppBean response;

    public UpdateAppHttpUtil(UpdateAppBean response) {
        this.response = response;
    }

    /**
     * 异步get
     *
     * @param url      get请求地址
     * @param params   get参数
     * @param callBack 回调
     */
    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
//        OkHttpUtils.get()
//                .url(url)
//                .params(params)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Response response, Exception e, int id) {
//                        callBack.onError(validateError(e, response));
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        callBack.onResponse(response);
//                    }
//                });


//        {
//            "update": "Yes",
//                "new_version": "0.8.3",
//                "apk_file_url": "https://raw.githubusercontent.com/WVector/AppUpdateDemo/master/apk/sample-debug.apk",
//                "update_log": "1，添加删除信用卡接口。\r\n2，添加vip认证。\r\n3，区分自定义消费，一个小时不限制。\r\n4，添加放弃任务接口，小时内不生成。\r\n5，消费任务手动生成。",
//                "target_size": "5M",
//                "new_md5":"b97bea014531123f94c3ba7b7afbaad2",
//                "constraint": false
//        }
        if (response != null) {
            callBack.onResponse(response);
        }

    }

    /**
     * 异步post
     *
     * @param url      post请求地址
     * @param params   post请求参数
     * @param callBack 回调
     */
    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
//        OkHttpUtils.post()
//                .url(url)
//                .params(params)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Response response, Exception e, int id) {
//                        callBack.onError(validateError(e, response));
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        callBack.onResponse(response);
//                    }
//                });
        if (response != null) {
            callBack.onResponse(response);
        }
    }

    /**
     * 下载
     *
     * @param url      下载地址
     * @param path     文件保存路径
     * @param fileName 文件名称
     * @param callback 回调
     */
    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final FileCallback callback) {
//        callback.onBefore();
//        DownloadUtil.get().download(url, path,fileName, new DownloadUtil.OnDownloadListener() {
//            @Override
//            public void onDownloadSuccess(final File file) {
////                Utils.showToast(MainActivity.this, "下载完成");
//
//                        callback.onResponse(file);
//
//            }
//
//            @Override
//            public void onDownloading(float progress, long total) {
//                callback.onProgress(progress, total);
//            }
//
//
//            @Override
//            public void onDownloadFailed(Exception e) {
//                        callback.onError("下载失败");
////                Utils.showToast(MainActivity.this, "下载失败");
//            }
//
//        });

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(path, fileName) {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        callback.onProgress(progress, total);
                        LogUtils.Log("OkHttpUtils", "progress  " + progress + "  total  " + total);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e, int id) {
                        callback.onError(validateError(e, response));

                    }

                    @Override
                    public void onResponse(File response, int id) {
                        callback.onResponse(response);

                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        callback.onBefore();
                    }
                });

    }

    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

}