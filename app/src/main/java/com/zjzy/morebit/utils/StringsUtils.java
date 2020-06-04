package com.zjzy.morebit.utils;

import android.content.Context;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.LeadingMarginSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.zjzy.morebit.App;
import com.zjzy.morebit.R;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mtopsdk.common.util.StringUtils;

/**
 * Created by Administrator on 2018/1/19.
 */

public class StringsUtils {
     public static  int sWidth =0;
    /**
     * 隐藏手机号码个别字符
     *
     * @param oldPhone
     * @return
     */
    public static String encryPhone(String oldPhone) {
        String newPhone = "";
        if (!TextUtils.isEmpty(oldPhone) && oldPhone.length() > 6) {
            int changeSize = oldPhone.length() - 6;
            String changeFu = "";
            for (int i = 0; i < changeSize; i++) {
                changeFu = changeFu + "*";
            }
            String changeStr = oldPhone.substring(0, 3);
            changeStr = changeStr + changeFu;
            changeStr = changeStr + oldPhone.substring(3 + changeSize, oldPhone.length());
            newPhone = changeStr;
        }
        return newPhone;
    }

    public static boolean isShowVideo(String videoid) {
        if (TextUtils.isEmpty(videoid) || "0".equals(videoid) || "1".equals(videoid)) {
            return false;
        }
        return true;
    }

    public static boolean equals(Object a, Object b) {
        boolean istr = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            istr = Objects.equals(a, b);
        }
        return istr;
    }


    public static boolean isError404(String errMsg) {
        boolean istr = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            istr = Objects.equals("404", errMsg);
        }

        return istr;
    }


    /**
     * 优惠券是否失效
     *
     * @param coupon
     * @return
     */
    public static boolean isEmpty(String coupon) {
        return TextUtils.isEmpty(coupon) || "0".equals(coupon) || "0.0".equals(coupon)|| "0.00".equals(coupon);
    }

    /**
     * 是否已注册(正常注册)
     *
     * @param errCode
     * @return
     */
    public static boolean isRegister(String errCode) {
        boolean istr = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            istr = Objects.equals(C.requestCode.B10005, errCode);
        } else {
            istr = C.requestCode.B10005.equals(errCode);
        }

        return istr;
    }

    /**
     * 判断前缀有没有http 没有加加 http
     *
     * @param
     * @return
     */
    public static String checkHttp(String urlStr) {
        if (urlStr == null) return "";
        if (urlStr.startsWith("http")) {
            return urlStr;
        } else {
            return "http" + urlStr;
        }
    }

    /**
     * 检查颜色是否合法
     * @param color
     * @return
     */
    public static boolean checkColor(String color){
        Pattern pattern = Pattern.compile("^#([0-9a-fA-F]{6}|[0-9a-fA-F]{8})$");
        Matcher matcher = pattern.matcher(color.trim());
        return  matcher.matches();
    }





    /**
     * 向url链接追加参数
     * @param url
     * @param params
     * @return
     */
    public static String appendUrlParams(String url, Map<String, String> params){
        if(TextUtils.isEmpty(url)){
            return "";
        }else if(null == params || params.size() < 1 ){
            return url.trim();
        }else{
            StringBuffer sb = new StringBuffer();
            Set<String> keys = params.keySet();
            for (String key : keys) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
            //删除最后一个&
            sb.deleteCharAt(sb.length() - 1);

            url = url.trim();
            int length = url.length();
            int index = url.indexOf("?");
            if(index > -1){
                //url有?
                if((length - 1) == index){
                    //url最后一个符号为？，如：http://wwww.baidu.com?
                    url += sb.toString();
                }else{
                    //如：http://wwww.baidu.com?aa=11 已经有拼接参数
                    url += "&" + sb.toString();
                }
            }else{
                //url后面没有问号，如：http://wwww.baidu.com
                url += "?" + sb.toString();
            }
            return url;
        }
    }


    public static String appendUrlParams(String url, String appendUrl){
        if(TextUtils.isEmpty(url)){
            return "";
        }else if(TextUtils.isEmpty(appendUrl)){
            return url.trim();
        }else{


            url = url.trim();
            int length = url.length();
            int index = url.indexOf("?");
            if(index > -1){
                //url有?
                if((length - 1) == index){
                    //url最后一个符号为？，如：http://wwww.baidu.com?
                    url += appendUrl;
                }else{
                    //如：http://wwww.baidu.com?aa=11 已经有拼接参数
                    url += "&" + appendUrl;
                }
            }else{
                //url后面没有问号，如：http://wwww.baidu.com
                url += "?" + appendUrl;
            }
            return url;
        }
    }


    /**
     * 将url参数转换成map
     * @param param
     * @return
     */
    public static Map<String, String> getUrlParams(String param) {
        Map<String, String> map = new LinkedHashMap<String, String>(0);
        if (StringUtils.isBlank(param)) {
            return map;
        }
        String[] params = param.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    /**
     *     左边图片右边文字换行不错位
     * @param title      标题
     * @return
     */
    public static void retractTitleForPdd(final View icon, final TextView tv, final String title){
        if(TextUtils.isEmpty(title)){
            return;
        }
        if(sWidth == 0){
            icon.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    sWidth = icon.getWidth() +App.getAppContext().getResources().getDimensionPixelSize(R.dimen.margin_pdd);
                    retractTitle(tv,title);
                    icon.getViewTreeObserver().removeOnPreDrawListener(
                            this);
                    return false;
                }
            });
        }else{
            retractTitle(tv,title);
        }

    }


    /**
     *     左边图片右边文字换行不错位
     * @param title      标题
     * @return
     */
    public static void retractTitle(final View icon, final TextView tv, final String title){
        if(TextUtils.isEmpty(title)){
            return;
        }
        if(sWidth == 0){
            icon.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    sWidth = icon.getWidth() +App.getAppContext().getResources().getDimensionPixelSize(R.dimen.margin_small);
                    retractTitle(tv,title);
                    icon.getViewTreeObserver().removeOnPreDrawListener(
                            this);
                    return false;
                }
            });
        }else{
            retractTitle(tv,title);
        }
    }

    /**
     *     左边图片右边文字换行不错位
     * @param title      标题
     * @return
     */
    public static void retractKaoLaTitle(final View icon, final TextView tv, final String title){
        if(TextUtils.isEmpty(title)){
            return;
        }
        if(sWidth == 0){
            icon.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    sWidth = icon.getWidth() +App.getAppContext().getResources().getDimensionPixelSize(R.dimen.margin_small);
                    retractTitle(tv,title);
                    icon.getViewTreeObserver().removeOnPreDrawListener(
                            this);
                    return false;
                }
            });
        }else{
            retractTitle(tv,title);
        }
    }

    /**
     *     左边图片右边文字换行不错位
     * @param title      标题
     * @return
     */
    public static void retractGuessTitle(final View icon, final TextView tv, final String title){
        if(TextUtils.isEmpty(title)){
            return;
        }
        if(sWidth == 0){
            icon.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    sWidth = icon.getWidth() +App.getAppContext().getResources().getDimensionPixelSize(R.dimen.margin_guess);
                    retractTitle(tv,title);
                    icon.getViewTreeObserver().removeOnPreDrawListener(
                            this);
                    return false;
                }
            });
        }else{
            retractTitle(tv,title);
        }
    }
    private static void retractTitle(  final TextView tv, final String title){
        if(TextUtils.isEmpty(title)){
            return;
        }

        SpannableString spannableString = new SpannableString(title);
        LeadingMarginSpan.Standard what = new LeadingMarginSpan.Standard(sWidth  , 0);
        spannableString.setSpan(what, 0, title.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        tv.setText(spannableString);

    }


    /**
     * 接口数据列表返回null或者size为0
     *
     * @param coupon
     * @return
     */
    public static boolean isDataEmpty(String errCode) {
        return C.requestCode.dataListEmpty.equals(errCode) || C.requestCode.dataNull.equals(errCode);
    }
}
