package com.zjzy.morebit.utils;

import android.app.ProgressDialog;

public class ProgressDialogUniversal {
	public static ProgressDialog jdtdialog; 
	 public static void jdtProgressDialoga (String message){
	    	//设置进度条风格，风格为圆形，旋转的  
	    			//jdtdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    			//设置ProgressDialog 标题  
	    			//jdtdialog.setTitle("进度对话框");  
	    			//设置ProgressDialog 提示信息  
	    			jdtdialog.setMessage(message);  
	    			//设置ProgressDialog 标题图标  
	    			jdtdialog.setIcon(android.R.drawable.ic_dialog_alert);
	    			//设置ProgressDialog的最大进度  
	    			jdtdialog.setMax(100);  
	    			//设置ProgressDialog 的一个Button  
	    			//设置ProgressDialog 是否可以按退回按键取消  
	    			jdtdialog.setCancelable(true);  
	    			//显示  
	    			jdtdialog.show();    			  
	    			//设置ProgressDialog的当前进度  
	    			jdtdialog.setProgress(66); 
	    }


}
