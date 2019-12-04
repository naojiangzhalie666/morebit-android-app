package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.zjzy.morebit.R;
import com.zjzy.morebit.contact.SdDirPath;
import com.zjzy.morebit.utils.DownloadManage;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.MyLog;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.util.List;


/**
 * Created by YangBoTian on 2019/3/18.
 */

public class DownloadDialog extends Dialog {
    public static  final int DOWNLOADING = 1; //正在下载中
    public static  final int DOWNLOADING_COMPLETE= 2; //下载完成
    public static  final int DOWNLOADING_ERROR= 3; //下载错误
    private TextView mBtnConfirm;
    private OnOkListener mOkListener;
    private OnCancelListner mCancelListener;
    private ClearSDdataDialog.OnOkListener okListener;
    private Context mContext;
    private TextView mBtnTitle,tv_image_total,tv_download_count,btn_cancel,btn_setting,tv_download_failure_count;
    private boolean gravity ;//是否设置控件居中
    private DonutProgress progress;
    private List<String> mImgUrls;
    private FileDownloadListener downloadListener;
    private int mDownloadCount;
    private int type = DOWNLOADING;
    private LinearLayout ll_download_failure;
    private LinearLayout ll_download;
    public DownloadDialog(Context context, int themeResId, List<String> imgUrls ) {
        super(context, themeResId);
        mContext = context;
        mImgUrls = imgUrls;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mBtnConfirm = findViewById(R.id.btn_confirm);
        mBtnTitle = findViewById(R.id.tv_title);
        progress = findViewById(R.id.progress);
        btn_cancel = findViewById(R.id.btn_cancel);
        tv_image_total = findViewById(R.id.tv_image_total);
        tv_download_failure_count = findViewById(R.id.tv_download_failure_count);
        tv_download_count = findViewById(R.id.tv_download_count);
        ll_download_failure = findViewById(R.id.ll_download_failure);
        ll_download = findViewById(R.id.ll_download);
        btn_setting = findViewById(R.id.btn_setting);
        tv_image_total.setText(mContext.getString(R.string.image_total,mImgUrls.size()+""));

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == DOWNLOADING_COMPLETE||type == DOWNLOADING_ERROR){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("image/*");
                    mContext.startActivity(intent);
                }
                    dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                mContext.startActivity(intent);
                dismiss();
            }
        });
        download();
    }

    private void download() {
//        FileDownloader.setup(mContext);
        downloadListener =createLis();
//        final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(downloadListener);
//        final List<BaseDownloadTask> tasks = new ArrayList<>();
//        MyLog.i("test","cache: " + PathUtils.getExternalAppCachePath());
        for(int i=0;i<mImgUrls.size();i++){
            String fileName = mImgUrls.get(i).substring(mImgUrls.get(i).lastIndexOf("/")+1,mImgUrls.get(i).lastIndexOf("."));
            String suffix = mImgUrls.get(i).substring(mImgUrls.get(i).lastIndexOf(".")+1,mImgUrls.get(i).length());
//            MyLog.i("test","suffix: " +suffix+"  fileName: " +fileName);
//            MyLog.i("test","paht: " +SdDirPath.IMAGE_CACHE_PATH+fileName+"."+suffix);
            DownloadManage.getInstance().start(mImgUrls.get(i),SdDirPath.IMAGE_CACHE_PATH+fileName+"."+suffix,downloadListener);
//          tasks.add(FileDownloader.getImpl().create(mImgUrls.get(i)).setPath( SdDirPath.IMAGE_CACHE_PATH+fileName+".jpg").setTag(i + 1));
        }
//        queueSet.disableCallbackProgressTimes();
//        queueSet.downloadSequentially(tasks);
//        queueSet.setAutoRetryTimes(1);
//        queueSet.start();
    }



    public interface OnOkListener {
        void onClick(View view);
    }

    public interface OnCancelListner {
        void onClick(View view);
    }

    public void setmOkListener(OnOkListener mOkListener) {
        this.mOkListener = mOkListener;
    }

    public void setmCancelListener(OnCancelListner mCancelListener) {
        this.mCancelListener = mCancelListener;
    }

    private FileDownloadListener createLis() {
        return new FileDownloadListener() {

            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                // 之所以加这句判断，是因为有些异步任务在pause以后，会持续回调pause回来，而有些任务在pause之前已经完成，
                // 但是通知消息还在线程池中还未回调回来，这里可以优化
                // 后面所有在回调中加这句都是这个原因
                if (task.getListener() != downloadListener) {
                    return;
                }
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (task.getListener() != downloadListener) {
                    return;
                }
                type =DOWNLOADING ;
                btn_setting.setVisibility(View.GONE);
                MyLog.i("test","progress.getProgress()+1: " +progress.getProgress()+1);
//                progressPb.setProgress(progressPb.getProgress() + 1);
//                progressTv.setText("progress: " + progressPb.getProgress());
//                progressInfoTv.append((int)task.getTag() + " | ");


            }



            @Override
            protected void completed(BaseDownloadTask task) {
                MyLog.i("test","completed: " +mDownloadCount);
                if (task.getListener() != downloadListener) {
                    return;
                }
                mDownloadCount++;
                btn_setting.setVisibility(View.GONE);
                float percent = mDownloadCount*100/mImgUrls.size();
                MyLog.i("test","percent: " +percent);
                progress.setProgress(percent);
                tv_download_count.setText(mContext.getString(R.string.image_download_count,mDownloadCount+""));
                ll_download_failure.setVisibility(View.GONE);
                ll_download.setVisibility(View.VISIBLE);
                  if(mDownloadCount==mImgUrls.size()){
                      type = DOWNLOADING_COMPLETE;
                      mBtnConfirm.setText("去相册查看");
                      btn_cancel.setBackgroundResource(R.drawable.submit_buttom_feed);
                      mBtnConfirm.setBackgroundResource(R.drawable.bg_solid_ececec_30dp);
                      btn_cancel.setText("完成");
                      mBtnConfirm.setVisibility(View.VISIBLE);
                      progress.setVisibility(View.GONE);
                  } else {
                      type = DOWNLOADING;
                      btn_cancel.setText("取消");
                      progress.setVisibility(View.VISIBLE);
                      btn_cancel.setBackgroundResource(R.drawable.bg_solid_ececec_30dp);
                      mBtnConfirm.setVisibility(View.GONE);
                  }
                GoodsUtil.updataImgToTK(mContext,new File(task.getPath()),task.getFilename());
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
                MyLog.i("test","downloadListener: "+downloadListener);
                type =DOWNLOADING_ERROR ;
                btn_setting.setVisibility(View.VISIBLE);
                if(mDownloadCount>0){
                    mBtnConfirm.setVisibility(View.VISIBLE);
                    mBtnConfirm.setBackgroundResource(R.drawable.submit_buttom_feed);
                }
                ll_download.setVisibility(View.GONE);
                ll_download_failure.setVisibility(View.VISIBLE);
                btn_cancel.setText("取消");
                tv_download_failure_count.setText(mDownloadCount+"");
                btn_cancel.setBackgroundResource(R.drawable.bg_solid_ececec_30dp);
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
