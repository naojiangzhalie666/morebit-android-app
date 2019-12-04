package com.zjzy.morebit.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.zjzy.morebit.App;
import com.zjzy.morebit.contact.SdDirPath;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangBoTian on 2019/8/2.
 */

public class DownloadManage {
    private static DownloadManage sDownloadManage = null;

    public static DownloadManage getInstance() {
        if (sDownloadManage == null) {
            synchronized (DownloadManage.class) {
                if (sDownloadManage == null) {
                    FileDownloader.setup(App.getAppContext());
                    sDownloadManage = new DownloadManage();
                }
            }
        }
        return sDownloadManage;
    }

    public void start(String url,String savePath, FileDownloadListener downloadListener) {
        MyLog.i("test","url: " +url+"  savePath: " +savePath);
        if(url==null||savePath==null){
            return;
        }
            FileDownloader.getImpl().create(url)
                    .setCallbackProgressTimes(300)
                    .setListener(downloadListener)
                    .setPath(savePath)
                    .asInQueueTask()
                    .enqueue();
        FileDownloader.getImpl().start(downloadListener, true);
    }
    public void multitaskStart(List<String> urls, FileDownloadListener downloadListener) {
        if(urls==null||urls.size()==0){
            return;
        }
        final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(downloadListener);

        final List<BaseDownloadTask> tasks = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            String fileName = urls.get(i).substring(urls.get(i).lastIndexOf("/")+1,urls.get(i).lastIndexOf("."));
            String suffix = urls.get(i).substring(urls.get(i).lastIndexOf(".")+1,urls.get(i).length());
            tasks.add(FileDownloader.getImpl().create(urls.get(i)).setPath(SdDirPath.IMAGE_CACHE_PATH+fileName+"."+suffix).setTag(i + 1));
        }
        queueSet.setAutoRetryTimes(1);
        queueSet.downloadTogether(tasks);
        queueSet.start();
    }
    /**
     * 刷新图库
     * @param context
     * @param path
     */

    public void refreshGallery(Context context, String path){
        if(!FileUtils.isFileExit(path)){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(new File(path)));
        context.sendBroadcast(intent);
    }
}
