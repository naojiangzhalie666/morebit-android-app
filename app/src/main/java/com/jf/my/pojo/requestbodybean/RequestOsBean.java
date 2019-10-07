package com.jf.my.pojo.requestbodybean;

import com.jf.my.pojo.request.RequestBaseBean;
import com.jf.my.utils.C;

/**
 * Created by JerryHo on 2019/1/7
 * Description: 请求版本升级bean
 */
public class RequestOsBean  extends RequestBaseBean {

    private int os = C.Setting.os;  //系统  1:android
}
