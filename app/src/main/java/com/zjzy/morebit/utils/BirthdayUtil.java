package com.zjzy.morebit.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.TimeBean;


import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * data:2019/11/4
 * author:jyx
 * function:时间选择器
 */
public class BirthdayUtil {
    private volatile static BirthdayUtil sBirthdayUtil;
    private Context context;
    private boolean[] type={true,true,false,false,false,false};
    public BirthdayUtil(Context context) {
        this.context=context;
    }
    public static BirthdayUtil getInstance(Context context){
        if (sBirthdayUtil==null){
            sBirthdayUtil=new BirthdayUtil(context);
        }
        return sBirthdayUtil;
    }

    /**
     * 时间选择器
     * @param
     * @param title 选择器弹窗的标题
     * @param flag 返回时间的格式
     */
    public void showBirthdayDate(Context context,String title, final int flag){


        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH,+100);// 选择时间的间隔
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2020,3,1);//设置起始年份
        Calendar endDate = Calendar.getInstance();
       new TimePickerBuilder(context, new OnTimeSelectListener() {
           @Override
           public void onTimeSelect(Date date, View v) {
               String time = getTime(date,flag);// 返回的时间
               long stringToDate = getStringToDate(time);
               requestBirthday(stringToDate);
           }
       })

                .setType(type)
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("完成")//确认按钮文字
                .setTitleSize(16)//标题文字大小
                .setContentTextSize(20)//内容位子大小
                .setTitleText(title)//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#F05557"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#999999"))//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate,endDate)//起始终止年月日设定
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build()
                .show();

    }

    private void requestBirthday(long textView) {

        EventBus.getDefault().post(new TimeBean(textView+""));


    }


    /**
     * 时间选择器
     * @param
     * @param title 选择器弹窗的标题
     * @param flag 返回时间的格式
     */
    public void showBirthdayDate2(Context context, String title, final TextView textView, final int flag){


        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH,+100);// 选择时间的间隔
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2020,3,1);//设置起始年份
        Calendar endDate = Calendar.getInstance();
        new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String time = getTime(date,flag);// 返回的时间
                textView.setText(time);
                String time2 = getTime(date,0);// 返回的时间
                long stringToDate = getStringToDate(time2);
                requestBirthday2(stringToDate);
            }
        })

                .setType(type)
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("完成")//确认按钮文字
                .setTitleSize(16)//标题文字大小
                .setContentTextSize(20)//内容位子大小
                .setTitleText(title)//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#F05557"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#999999"))//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate,endDate)//起始终止年月日设定
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build()
                .show();

    }

    private void requestBirthday2(long stringToDate) {
        EventBus.getDefault().post(new TimeBean(stringToDate+""));
    }

    /**
     * 格式化时间
     * @param date 时间数据源
     * @param type 时间返回格式
     * @return
     */
    public String getTime(Date date , int type) {//可根据需要自行截取数据显示
        SimpleDateFormat format = null;
        if (type==1){
            format = new SimpleDateFormat("yyyy年MM月"+" 月报");
        }else if (type==0){
            format = new SimpleDateFormat("yyyy-MM");
        }else {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }

        return format.format(date);
    }
    //字符串转化为时间戳
    public static long getStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

}
