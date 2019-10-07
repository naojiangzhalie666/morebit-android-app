package com.markermall.cat.pojo.requestbodybean;

import com.markermall.cat.pojo.request.RequestBaseBean;
import com.markermall.cat.utils.C;

/**
 * Created by JerryHo on 2019/1/7
 * Description: 请求版本升级bean
 */
public class RequestOsBean  extends RequestBaseBean {

    private int os = C.Setting.os;  //系统  1:android
}
