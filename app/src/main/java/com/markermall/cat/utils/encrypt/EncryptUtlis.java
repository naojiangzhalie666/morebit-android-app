package com.markermall.cat.utils.encrypt;

import android.text.TextUtils;

import com.markermall.cat.utils.MyGsonUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by fengrs on 2018/12/20.
 * 备注: 加密滴Utils
 */

public class EncryptUtlis {

    public static String createSign(String characterEncoding, TreeMap<Object, Object> parameters, String key) {
        StringBuffer sb = new StringBuffer();
        StringBuffer sbKey = new StringBuffer();
//  所有参与传参的参数按照ASCII排序（升序）
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object obj = entry.getValue();
            if (!"sign".equals(k)) {
//  空值不传递，不参与签名组串
                if (null != obj && !"".equals(obj)) {
                    sb.append(k).append("=").append(obj).append("&");
                    sbKey.append(k).append("=").append(obj).append("&");
                }
            }
        }
        sbKey = sbKey.append("key=" + key);
//  MD5加密,结果转换为大写字符
        return MD5Utils.MD5Encode(sbKey.toString(), characterEncoding).toUpperCase();
    }


    public static String getSign(TreeMap<Object, Object> map) {
        String createSign = createSign("UTF-8", map, null);

        return createSign;

    }

    /**
     *  获取Sign 值
     * @param object bean
     * @return
     */
    public static String getSign2(Object object) {
        try {
            String s = MyGsonUtils.beanToJson(object);
            if (TextUtils.isEmpty(s)) {
                return "";
            }
            TreeMap<Object, Object> map = MyGsonUtils.json2TreeMap(s);
            if (map == null || map.size() == 0) {
                return "";
            }
            return EncryptUtlis.getSign(map);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 将map转换成url
     *
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(Map<String, Integer> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
