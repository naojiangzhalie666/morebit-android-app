package com.jf.my.Module.common.Utils;

import android.content.Context;

import com.jf.my.Module.common.Dialog.NetLoadingDialog;
import com.jf.my.R;

import java.lang.ref.SoftReference;

/**
 * Created by Administrator on 2017/11/4.
 */

public class LoadingView {

    public static NetLoadingDialog loadingDialog;

    /**
     * 显示等待圈
     */
    public static void showDialog(Context context ){
        LoadingView.showDialog(context,"请求中...");
    }
    public static void showDialog(Context context, String text){

        try {
            if (loadingDialog!=null&&loadingDialog.isShowing()){
                loadingDialog.dismiss();
                loadingDialog = null;
            }
            SoftReference<Context> softReference = new SoftReference<Context>(context);
            loadingDialog = new NetLoadingDialog(softReference.get(), R.style.dialog,text);
            loadingDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 关闭等待圈
     */
    public static void dismissDialog(){
        try {
             if(loadingDialog!=null){
                 loadingDialog.dismiss();
             }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 清除对象
     */
    public static void closeDialog() {
        dismissDialog();
        loadingDialog = null;
    }

    public static void setCancelable(boolean cancelable){
        if(loadingDialog!=null){
            loadingDialog.setCancelable(cancelable);
        }
    }

}
