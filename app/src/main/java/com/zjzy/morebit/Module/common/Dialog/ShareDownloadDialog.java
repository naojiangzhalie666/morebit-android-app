package com.zjzy.morebit.Module.common.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.zjzy.morebit.Activity.ShareMoneyActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.utils.DownloadManage;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.PageToUtil;

import java.io.FileNotFoundException;
import java.util.List;


/**
 * Created by YangBoTian on 2019/3/18.
 */

public class ShareDownloadDialog extends Dialog {
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
    public ShareDownloadDialog(Context context, int themeResId ) {
        super(context, themeResId);
        mContext = context;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sharedownload);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        btn_cancel = findViewById(R.id.btn_cancel);

        btn_setting = findViewById(R.id.btn_setting);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转微信
                PageToUtil.goToWeixin(mContext);
                dismiss();
            }
        });

    }

    private void download() {

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



    @Override
    public void dismiss() {
        super.dismiss();

    }

}
