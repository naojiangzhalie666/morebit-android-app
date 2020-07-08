package com.zjzy.morebit.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import com.zjzy.morebit.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author : LiuZhenGuo Android Developer
 * create by : Administrator -- WorkSpace
 * create date : 2019/7/23
 * create time : 11:25
 * function : 时间格式化工具类
 */
public class TimeUtil {
    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * 获取当前格式化时间
     *
     * @param pattern 时间格式
     * @return 返回对应格式的时间
     */
    public static String getTimeString(String pattern) {
        if (TextUtils.isEmpty(pattern)) {
            throw new NullPointerException("pattern不允许为null");
        } else {
            return getTimeString(pattern, new Date().getTime());
        }
    }

    /**
     * 获取对应时间戳的格式化时间
     *
     * @param pattern 时间格式
     * @param time    时间戳 当为0的时候获取当前时间
     * @return 返回对应格式的时间
     */
    public static String getTimeString(String pattern, long time) {
        if (TextUtils.isEmpty(pattern)) {
            throw new NullPointerException("pattern不允许为null");
        } else {
            if (time == 0) {
                time = new Date().getTime();
            }
            Date date = new Date(time);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sd = new SimpleDateFormat(pattern);
            return sd.format(date);
        }

    }

    /**
     * 返回文字描述的日期
     */
    public static String getTimeFormatText(Context context, Date date) {
        String sAgo = context.getString(R.string.ago);
//        String sYears = context.getString(R.string.years) + sAgo;
//        String sMonth = context.getString(R.string.month) + sAgo;
//        String sWeek = context.getString(R.string.week) + sAgo;
        String sDay = context.getString(R.string.day) + sAgo;
        String sHours = context.getString(R.string.hours) + sAgo;
        String sMinutes = context.getString(R.string.minutes) + sAgo;
        String sSecknds = context.getString(R.string.secknds) + sAgo;
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
//        if (diff > year) {
//            r = (diff / year);
//            return r + sYears;
//        }
//        if (diff > month) {
//            r = (diff / month);
//            return r + sMonth;
//        }
        if (diff > day) {
            r = (diff / day);
            return r + sDay;
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + sHours;
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + sMinutes;
        }
        return context.getString(R.string.just_now);
    }


    /**
     *
     * @param
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + " 天 " + hours + ":" + minutes + ":"
                + seconds;
    }
    /**
     *
     * @param begin 时间段的开始
     * @param end   时间段的结束
     * @return  输入的两个Date类型数据之间的时间间格用* days * hours * minutes * seconds的格式展示
     * @author fy.zhang
     */
    public static String formatDuring(Date begin, Date end) {
        return formatDuring(end.getTime() - begin.getTime());
    }
}