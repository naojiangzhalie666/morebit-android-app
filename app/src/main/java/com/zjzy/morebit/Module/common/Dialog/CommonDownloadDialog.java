package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.zjzy.morebit.R;
import com.zjzy.morebit.utils.DownloadManage;
import com.zjzy.morebit.utils.MyLog;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.util.List;


/**
 * Created by YangBoTian on 2019/3/18.
 */

public class CommonDownloadDialog extends Dialog {
    public static  final int DOWNLOADING = 1; //正在下载中
    public static  final int DOWNLOADING_COMPLETE= 2; //下载完成
    public static  final int DOWNLOADING_ERROR= 3; //下载错误
    private Context mContext;
    private DonutProgress progress;
    private List<String> mUrls;
    private FileDownloadListener downloadListener;
    private int mDownloadCount;
    public CommonDownloadDialog(Context context, int themeResId, List<String> imgUrls ) {
        super(context, themeResId);
        mContext = context;
        mUrls = imgUrls;
    }
    public CommonDownloadDialog(Context context,List<String> imgUrls ) {
        super(context,  R.style.dialog);
        mContext = context;
        mUrls = imgUrls;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_common_download);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        progress = findViewById(R.id.progress);
        download();
    }

    private void download() {
        downloadListener =createLis();
//        for(int i=0;i<mUrls.size();i++){
//            String fileName = mUrls.get(i).substring(mUrls.get(i).lastIndexOf("/")+1,mUrls.get(i).lastIndexOf("."));
//            String suffix = mUrls.get(i).substring(mUrls.get(i).lastIndexOf(".")+1,mUrls.get(i).length());
//            DownloadManage.getInstance().start(mUrls.get(i),SdDirPath.IMAGE_CACHE_PATH+fileName+"."+suffix,downloadListener);
//        }
      DownloadManage.getInstance().multitaskStart(mUrls,downloadListener);
    }



    public interface OnOkListener {
        void onClick(View view);
    }


    private FileDownloadListener createLis() {
        return new FileDownloadListener() {

            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                // 之所以加这句判断，是因为有些异步任务在pause以后，会持续回调pause回来，而有些任务在pause之前已经完成，
                // 但是通知消息还在线程池中还未回调回来，这里可以优化
                // 后面所有在回调中加这句都是这个原因
                MyLog.i("test","soFarBytes: " +soFarBytes+" totalBytes: " +totalBytes);
                if (task.getListener() != downloadListener) {
                    return;
                }
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (task.getListener() != downloadListener) {
                    return;
                }
                float percent = soFarBytes*100/totalBytes;
                progress.setProgress(percent);
            }



            @Override
            protected void completed(BaseDownloadTask task) {
                MyLog.i("test","completed: " +mDownloadCount);
                if (task.getListener() != downloadListener) {
                    return;
                }
                mDownloadCount++;
                float percent = mDownloadCount*100/ mUrls.size();
                MyLog.i("test","percent: " +percent);
//                progress.setProgress(percent);
                  if(mDownloadCount== mUrls.size()){
                     dismiss();
                  }

            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (task.getListener() != downloadListener) {
                    return;
                }
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                MyLog.i("test","error: "+e.toString());
                if (task.getListener() != downloadListener) {
                    return;
                }
                dismiss();
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                MyLog.i("test","warn: ");
                if (task.getListener() != downloadListener) {
                    return;
                }

            }
        };
    }

    @Override
    public void dismiss() {
        super.dismiss();
        sotpDownload();
    }
    private  void sotpDownload(){
        FileDownloader.getImpl().pauseAll();
    }
}
