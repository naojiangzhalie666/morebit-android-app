package com.zjzy.morebit.utils;


import android.text.TextUtils;

import com.blankj.utilcode.util.TimeUtils;
import com.zjzy.morebit.App;
import com.zjzy.morebit.LocalData.UserLocalData;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Administrator on 2017/10/16.
 */

public class DateTimeUtils {
    public static final String FORMAT_PATTERN_YEAR_MONTH = "yyyy年MM月";
    public static final String FORMAT_PATTERN_YEAR_MONTH_DAY_WITHOUT_TEXT = "yyyy-MM-dd";
    public static final String FORMAT_PATTERN_MONTH_DAY_WITHOUT_TEXT = "MM-dd";
    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();

    /**
     * 转换PHP返回的时间戳
     * yyyy-MM-dd
     *
     * @return
     */
    public static String getYmd1(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = new Date(Long.parseLong(time));
            String t1 = format.format(d1);
            return t1;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 转换PHP返回的时间戳
     * yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getYmdhhmmss(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = new Date(Long.parseLong(time));
            String t1 = format.format(d1);
            return t1;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 转换PHP返回的时间戳
     * yyyy-MM-dd
     *
     * @return
     */
    public static String getMdhms(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
            Date d1 = new Date(Long.parseLong(time + "000"));
            String t1 = format.format(d1);
            return t1;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 转换PHP返回的时间戳
     * yyyy-MM-dd
     *
     * @return
     */
    public static String getYMdhm(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date d1 = new Date(Long.parseLong(time + "000"));
            String t1 = format.format(d1);
            return t1;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 转换PHP返回的时间戳
     * yyyy-MM-dd
     *
     * @return
     */
    public static String getHms(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date d1 = new Date(Long.parseLong(time + "000"));
            String t1 = format.format(d1);
            return t1;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 转换PHP返回的时间戳
     * yyyy-MM-dd
     *
     * @return
     */
    public static String getMdhm(String time) {
        try {

            SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
            Date d1 = new Date(Long.parseLong(time + "000"));
            String t1 = format.format(d1);
            return t1;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 转换PHP返回的时间戳
     * HH
     *
     * @return
     */
    public static String geth(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("mm");
            Date d1 = new Date(Long.parseLong(time + "000"));
            String t1 = format.format(d1);
            return t1;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getTimeNow() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getTimePath() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "1/1/1";
        }

    }

    /**
     * 获取当前时间-传入格式 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static int getTimeNowToSs() {
        int nowTimeSS = 0;
        try {
            String getTime = getTimeNow();
            String gethmsStr = getTime.substring(11, getTime.length());
            String[] spliteStr = gethmsStr.split(":");
            nowTimeSS = (Integer.parseInt(spliteStr[0])) * 3600 + (Integer.parseInt(spliteStr[1])) * 60 + Integer.parseInt(spliteStr[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowTimeSS;
    }

    //    public static void getTiem(){
//        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
//        t.setToNow(); // 取得系统时间。
//        int year = t.year;
//        int month = t.month;
//        int date = t.monthDay;
//        int hour = t.hour; // 0-23
//        int minute = t.minute;
//        int second = t.second;
//    }
//毫秒换成00:00:00
    public static String getCountTimeByLong(long finishTime) {
        try {

            int totalTime = (int) (finishTime / 1000);//秒
            int hour = 0, minute = 0, second = 0;

            if (3600 <= totalTime) {
                hour = totalTime / 3600;
                totalTime = totalTime - 3600 * hour;
            }
            if (60 <= totalTime) {
                minute = totalTime / 60;
                totalTime = totalTime - 60 * minute;
            }
            if (0 <= totalTime) {
                second = totalTime;
            }
            StringBuilder sb = new StringBuilder();

            if (hour < 10) {
                sb.append("0").append(hour).append(":");
            } else {
                sb.append(hour).append(":");
            }
            if (minute < 10) {
                sb.append("0").append(minute).append(":");
            } else {
                sb.append(minute).append(":");
            }
            if (second < 10) {
                sb.append("0").append(second);
            } else {
                sb.append(second);
            }
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 根据秒数转化为时分秒   00:00:00
     *
     * @return
     */
    public static String getTime(long second) {
        String hourStr = "00";
        String minStr = "00";
        String secondeStr = "00";
        long hour = second / 3600;
        long minute = (second - hour * 3600) / 60;
        second = second - hour * 3600 - minute * 60;
        if (hour < 10) {
            hourStr = "0" + hour;
        } else {
            hourStr = "" + hour;
        }
        if (minute < 10) {
            minStr = "0" + minute;
        } else {
            minStr = "" + minute;
        }
        if (second < 10) {
            secondeStr = "0" + second;
        } else {
            secondeStr = "" + second;
        }
        return hourStr + ":" + minStr + ":" + secondeStr;
    }

    /**
     * 把videoView的进度转换成时分秒
     *
     * @param timeMs
     * @return
     */
    public static String getVideoTime(int timeMs) {
        StringBuilder mFormatBuilder;
        Formatter mFormatter;
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    /**
     * 获取当天的几时的 long
     *
     * @param hour
     * @return
     */
    public static long getFirstCreateIndexDate(int hour) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime().getTime();

    }

    public static String getYMDTime(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        try {
            Timestamp ts = Timestamp.valueOf(time);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(ts);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getHMSTime(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        try {
            Timestamp ts = Timestamp.valueOf(time);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            return sdf.format(ts);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 转换为hh：mm：ss展示
     * @param ms
     * @return
     */
    public static String getHHmmss(long ms){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hms = formatter.format(ms);
        return hms;


    }

    public static String getDatetoString(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        try {
            Timestamp ts = Timestamp.valueOf(time);
            return getDateToString(ts.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDateToString(long milSecond) {
        Date date = new Date(milSecond);
        SimpleDateFormat format;
        String hintDate = "";
        //当前时间
        Calendar currentTime = Calendar.getInstance();
        //要转换的时间
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(milSecond);

        //先获取年份
        int year = time.get(Calendar.YEAR);
        //获取一年中的第几天
        int day = time.get(Calendar.DAY_OF_YEAR);
        //获取当前年份 和 一年中的第几天
        Date currentDate = new Date(System.currentTimeMillis());
        int currentYear = currentTime.get(Calendar.YEAR);
        int currentDay = currentTime.get(Calendar.DAY_OF_YEAR);
        //计算今年
        if (currentYear == year) {
            //今年
            if (currentDay - day == 1) {
                hintDate = "昨日";
            }
            if (currentDay - day == 0) {
                hintDate = "今天";
            }
        }
        if (TextUtils.isEmpty(hintDate)) {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.format(date);
        } else {
            format = new SimpleDateFormat("HH:mm:ss");
            return hintDate;
        }

    }


    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     */
    public static boolean IsToday(String day) {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = getDateFormat().parse(day);
            cal.setTime(date);

            if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
                int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                        - pre.get(Calendar.DAY_OF_YEAR);

                if (diffDay == 0) {
                    return true;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     */
    public static boolean IsYesterday(String day) {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = getDateFormat().parse(day);
            cal.setTime(date);

            if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
                int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                        - pre.get(Calendar.DAY_OF_YEAR);

                if (diffDay == -1) {
                    return true;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

    public static long getNext30MinuteTime() {
        String geth = DateTimeUtils.geth(System.currentTimeMillis() / 1000 + "");
        if (!TextUtils.isEmpty(geth)) {
            int abs = 1;
            try {
                Integer currentTimeH = Integer.valueOf(geth);
                if (currentTimeH < 30) {
                    abs = 30 - currentTimeH;
                } else if (currentTimeH > 30) {
                    abs = 60 - currentTimeH;
                } else {
                    abs = 30;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return abs * 60 * 1000;
        }
        return 0;
    }

    /**
     * 转换PHP返回的时间戳
     * yyyy-MM-dd
     *
     * @return
     */
    public static String getYmd(Date date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String t1 = format.format(date);
            return t1;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 转换PHP返回的时间戳
     * yyyy-MM-dd
     *
     * @return
     */
    public static String getmd(Date date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM.dd");
            String t1 = format.format(date);
            return t1;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static boolean getPastDate(long past) {
        Date date1 = new Date(past);
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String t1 = format.format(date1);
            return IsToday(t1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    /**
//     * yyyy-MM-dd HH:mm:ss 转 yyyy-MM-dd
//     * @param ymdhs   yyyy-MM-dd HH:mm:ss
//     * @return
//     */
//    public static String ymdhsToYmd(String ymdhs) {
//        TimeUnit.string2Date
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
//        String str = formatter.format(curDate);
//        return str;
//    }

    public static String getTheMonth(String time, String formatPattern, String toPattern) {
        DateFormat format = new SimpleDateFormat(formatPattern);
        try {
            Date date = format.parse(time);
            return getPatternTime(date.getTime(), toPattern);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String getPatternTime(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);//参数时间
        return param;
    }


    public static Date getDateByString(String time) {
        Date date = null;
        if (time == null) {
            return date;
        }

        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getShortTime(String time) {
        String shortString = null;
        long now = Calendar.getInstance().getTimeInMillis();
        Date date = getDateByString(time);
        if (date == null) {
            return shortString;
        }
        long delTime = (now - date.getTime()) / 1000;
        if (delTime > 365 * 24 * 60 * 60) {
            shortString = (int) (delTime / (365 * 24 * 60 * 60)) + "年前";
        } else if (delTime > 24 * 60 * 60) {
            shortString = (int) (delTime / (24 * 60 * 60)) + "天前";
        } else if (delTime > 60 * 60) {
            shortString = (int) (delTime / (60 * 60)) + "小时前";
        } else if (delTime > 60) {
            shortString = (int) (delTime / (60)) + "分钟前";
        } else if (delTime > 1) {
            shortString = delTime + "秒前";
        } else {
            shortString = "1秒前";
        }
        return shortString;
    }

    /**
     * yyyy-MM-dd HH:mm:ss转
     *
     * @param ymdhms
     * @return
     */
    public static String ymdhmsToymd(String ymdhms) {
        ;
        if ("今天".equals(ymdhms)) {
            return "今天";
        } else if ("昨日".equals(ymdhms)) {
            return "昨天";
        } else {
            String time = "";
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date d1 = new Date(TimeUtils.string2Millis(ymdhms));
                time = format.format(d1);
                return time;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

    }

    /**
     * 判断是否是当天，例如当天23.55分操作了，第二天0.01分再操作
     *
     * @return
     */
    public static boolean isToday() {
        int day = (int) SharedPreferencesUtils.get(App.getAppContext(), C.sp.DIALOG_USER_IS_UPGRADE_TIME+ UserLocalData.getUser().getPhone(), -1);
        int curDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        if ((day == -1 || day != curDay)) {
            SharedPreferencesUtils.put(App.getAppContext(), C.sp.DIALOG_USER_IS_UPGRADE_TIME+ UserLocalData.getUser().getPhone(), curDay);
            return true;
        } else {
            return false;
        }
    }


    public static Long toLongTime(String time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
          return    simpleDateFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * yyyy-MM-dd HH:mm:ss 数据转换为MM.dd
     * @param time
     * @return
     */
    public static String toMMdd(String time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat mmdd = new SimpleDateFormat("MM.dd");
        String format = null;
        try {
            format = mmdd.format(simpleDateFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format;
    }

    /**
     * yyyy-MM-dd HH:mm:ss 数据转换为MM-dd
     * @param time
     * @return
     */
    public static String formatMMdd(String time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat mmdd = new SimpleDateFormat("MM-dd");
        String format = null;
        try {
            format = mmdd.format(simpleDateFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format;
    }



    public static void main(String[] args) {

    }
}
