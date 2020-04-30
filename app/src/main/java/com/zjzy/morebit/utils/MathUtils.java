package com.zjzy.morebit.utils;

import android.text.TextUtils;

import com.zjzy.morebit.Module.common.Dialog.ProductDialog;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.pddjd.JdPddProgramItem;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.umeng.analytics.pro.ca.f;

/**
 * Created by fengrs on 2018/7/6.
 */

public class MathUtils {
    /**
     * 成长值比例
     */
    public static final int CORN_RATION = 1;

    /**
     * 转换两位数小时
     *
     * @param rateStr
     * @return
     */
    public static String formatTo2Decimals(String rateStr) {
        try {
            if (rateStr.indexOf(".") != -1) {
                //获取小数点的位置
                int num = 0;
                //找到小数点在字符串中的位置,找到返回一个int类型的数字,不存在的话返回 -1
                num = rateStr.indexOf(".");

                String dianAfter = rateStr.substring(0, num + 1);//输入100.30,dianAfter = 100.
                String afterData = rateStr.replace(dianAfter, "");//把原字符(rateStr)串包括小数点和小数点前的字符替换成"",最后得到小数点后的字符(不包括小数点)

                //判断小数点后字符的长度并做不同的操作,得到小数点后两位的字符串
                if (afterData.length() < 2) {
                    afterData = afterData + "0";
                } else {
                    afterData = afterData;
                }
                //返回元字符串开始到小数点的位置 + "." + 小数点后两位字符
                return rateStr.substring(0, num) + "." + afterData.substring(0, 2);
            } else {
                if (rateStr == "1") {
                    return "1";
                } else {
                    return rateStr;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取自己的分拥金额
     *
     * @param calculation      比例
     * @param commission_price 总价格
     * @return
     */
    public static String getMuRatioComPrice(String calculation, String commission_price) {
        if (TextUtils.isEmpty(calculation) || TextUtils.isEmpty(commission_price)) {
            return "";
        }
        double v1 = getMuratiocom(calculation, commission_price);
        return formatTo2Decimals(v1 + "");
    }

    /**
     * 获取自己的补贴佣金金额
     *
     * @param calculation    比例
     * @param subsidiesPrice 总补贴
     * @return
     */
    public static String getMuRatioSubSidiesPrice(String calculation, String subsidiesPrice) {
        if (TextUtils.isEmpty(calculation) || TextUtils.isEmpty(subsidiesPrice)) {
            return "";
        }
        double v1 = getMuratiocomByBigDecimal(calculation, subsidiesPrice);
        return formatTo2Decimals(v1 + "");
    }


    /**
     * 计算平台补贴+佣金
     *
     * @param ratioComPrice  佣金
     * @param subsidiesPrice 补贴金额
     * @return
     */
    public static String getTotleSubSidies(String ratioComPrice, String subsidiesPrice) {
        double calSubsidiesPrice = 0;
        double calRatioComPrice = 0;
        if (!TextUtils.isEmpty(subsidiesPrice)) {
            calSubsidiesPrice = Double.parseDouble(subsidiesPrice);
        }
        if (!TextUtils.isEmpty(ratioComPrice)) {
            calRatioComPrice = Double.parseDouble(ratioComPrice);
        }

        double v1 = sum(calSubsidiesPrice, calRatioComPrice);
        return formatTo2Decimals(v1 + "");
    }


    private static double getMuratiocom(String calculation, String commission_price) {
        double dRatio = 0;
        double getdRatioComPrice = 0;
        try {
            dRatio = Double.parseDouble(calculation);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("MathUtils", "自己的比例 == 0");
        }
        try {
            getdRatioComPrice = Double.parseDouble(commission_price);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("MathUtils", "商品价格 == 0");
        }


//        BigDecimal bigdRatio = new BigDecimal(dRatio);
//        BigDecimal bigdComPrice = new BigDecimal(getdRatioComPrice);
//        BigDecimal multiply = bigdComPrice.multiply(bigdRatio);
        double v = dRatio * getdRatioComPrice;
        return v / 100;
    }

    private static double getMuratiocomByBigDecimal(String calculation, String commission_price) {
        double dRatio = 0;
        double getdRatioComPrice = 0;
        try {
            dRatio = Double.parseDouble(calculation);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("MathUtils", "自己的比例 == 0");
        }
        try {
            getdRatioComPrice = Double.parseDouble(commission_price);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("MathUtils", "商品价格 == 0");
        }


        double v = mul(dRatio, getdRatioComPrice);
        return v / 100;
    }

    /**
     * 会员商品展示的多豆金额
     *
     * @param price
     * @return
     */
    public static String getMorebitCorn(String price) {
        if (TextUtils.isEmpty(price)) {
            return "0";
        }
        long corn = 0;
        try {
            corn = (long) (Double.parseDouble(price) * CORN_RATION);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("MathUtils", "价格转换非法");
        }

        return String.valueOf(corn);

    }

    /**
     * 获取券后价格
     *
     * @param voucherPrice 比例
     * @return
     */
    public static String getVoucherPrice(String voucherPrice) {
        if (TextUtils.isEmpty(voucherPrice)) {
            return "";
        }
        return voucherPrice;
//        double voucher = 0;
//        try {
//            voucher = Double.parseDouble(voucherPrice);
//            voucher = voucher / 100;
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtils.e("MathUtils", "自己的比例 == 0");
//        }
//        return formatTo2Decimals(voucher + "");
    }

    /**
     * 优惠券金额
     *
     * @param couponPrice
     * @return
     */
    public static String getCouponPrice(String couponPrice) {
        if (TextUtils.isEmpty(couponPrice)) {
            return "";
        }
        return couponPrice;
//        double voucher = 0;
//        try {
//            voucher = Double.parseDouble(couponPrice);
//            voucher = voucher / 100;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return formatTo2Decimals(voucher + "");
    }

    /**
     * 获取收益
     *
     * @param money
     * @return
     */
    public static String getMoney(String money) {
        if (TextUtils.isEmpty(money)) {
            return "0";
        }
        return money;
//        double voucher = 0;
//        try {
//            voucher = Double.parseDouble(money);
//            voucher = voucher / 100;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return formatTo2Decimals(voucher + "");
    }

    /**
     * 获取 价格
     *
     * @param
     * @return
     */
    public static String getPrice(String price) {
        if (TextUtils.isEmpty(price)) {
            return "";
        }
        return price;
//
//        double voucher = 0;
//        try {
//            voucher = Double.parseDouble(price);
//            voucher = voucher / 100;
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtils.e("MathUtils", "自己的比例 == 0");
//        }
//        return formatTo2Decimals(voucher + "");

    }

    /**
     * 获取 价格
     *
     * @param
     * @return
     */
    public static String getnum(String rmb) {
        if (TextUtils.isEmpty(rmb)) {
            return "";
        }
        if(rmb.contains(".")){
            if (rmb.contains(".00")){
                return rmb.replace(".00","");
            }else if (rmb.contains(".0")){
                return rmb.replace(".0","");
            }else if (rmb.substring(rmb.length()-1).equals("0")){
                return rmb.replace("0","");
            }
        }else{
            return rmb;
        }
        return rmb;

    }

    /**
     * 将double格式化为指定小数位的String，不足小数位用0补全
     *
     * @param v     需要格式化的数字
     * @param scale 小数点后保留几位
     * @return
     */
    public static String roundByScale(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        if(scale == 0){
            return new DecimalFormat("0").format(v);
        }
        String formatStr = "0.";
        for(int i=0;i<scale;i++){
            formatStr = formatStr + "0";
        }
        return new DecimalFormat(formatStr).format(v);
    }

    /**
     * 转换为万  小数
     *
     * @param
     * @return
     */
    public static String getSalesPrice(String getSaless) {
        String salesStr = "";
        if (TextUtils.isEmpty(getSaless)) return "0";
        try {
            double getSalesNum = Double.valueOf(getSaless);
            if (getSalesNum > 10000) {

                double saleW = getSalesNum * 1.0 / 10000;
                salesStr = String.format("%.2f", saleW) + "万";
            } else {
                salesStr = "" + getSalesNum;
            }
        } catch (Exception e) {
            e.printStackTrace();
            salesStr = getSaless;
        }
        return salesStr;
    }

    /**
     * 转换为万  整数
     *
     * @param
     * @return
     */
    public static String getSales(String getSaless) {
        String salesStr = "";
        if (TextUtils.isEmpty(getSaless)) return "0";
        try {
            int getSalesNum = Integer.valueOf(getSaless);
            if (getSalesNum > 10000) {
                double saleW = getSalesNum * 1.0 / 10000;
                salesStr = String.format("%.1f", saleW) + "万";
            } else {
                salesStr = "" + getSalesNum;
            }
        } catch (Exception e) {
            e.printStackTrace();
            salesStr = getSaless;
        }
        return salesStr;
    }

    /**
     * 校验无效滴今儿
     *
     * @param
     * @return
     */
    public static boolean checkoutInvalidMoney(String str) {
        return !(TextUtils.isEmpty(str) || "0".equals(str) || "0.0".equals(str));
    }

    /**
     * 校验无效滴今儿
     *
     * @param
     * @return
     */
    public static double allDiscountsMoney(String calculation, String commission_price, String couponPrice) {
        double muratiocom = getMuratiocom(calculation, commission_price);
        double couponPriceDouble = 0;
        if (checkoutInvalidMoney(couponPrice)) {
            try {
                couponPriceDouble = Double.parseDouble(couponPrice);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        double allMultiple = muratiocom * 100 + couponPriceDouble * 100;
        return allMultiple / 100;

    }

    public static String getTitle(ShopGoodInfo item) {
        if (item == null) return "";
        String itemSubhead = item.getItemSubhead();
        if (!TextUtils.isEmpty(itemSubhead)
                && (itemSubhead != null && !itemSubhead.equals("NULL"))) {
            return item.getItemSubhead();
        } else {
            if (!TextUtils.isEmpty(item.getTitle())) {
                return item.getTitle();
            }
        }
        return "";
    }

    //拼多多标题
    public static String getTitleForPdd(JdPddProgramItem item) {
        if (item == null) return "";
        String itemSubhead = item.getItemTitle();
        if (!TextUtils.isEmpty(itemSubhead)
                && (itemSubhead != null && !itemSubhead.equals("NULL"))) {
            return item.getItemTitle();
        } else {
            if (!TextUtils.isEmpty(item.getItemTitle())) {
                return item.getItemTitle();
            }
        }
        return "";
    }

    public static String getPicture(ShopGoodInfo item) {
        if (item == null) return "";
        if (!TextUtils.isEmpty(item.getPicture())) {
            return item.getPicture();
        } else {
            return "";
        }
    }

    /**
     * 获取商品主图
     *
     * @param item
     * @return
     */
    public static String getImageUrl(ShopGoodInfo item) {
        if (item == null) return "";
        String imageUrl = item.getImageUrl();
        if (!TextUtils.isEmpty(imageUrl)) {
            return imageUrl;
        } else {
            return "";
        }
    }


    /**
     * double相加
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double sum(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2).doubleValue();
    }

    /**
     * double相乘
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double mul(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.multiply(bd2).doubleValue();
    }

    /**
     * 两位小数点格式化
     *
     * @param price
     * @return
     */
    public static String formatMoney(double price) {
        NumberFormat format = new DecimalFormat("######0.00");
        return format.format(price);

    }
}
