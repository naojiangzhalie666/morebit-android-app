package com.zjzy.morebit.pojo.requestbodybean;

import com.zjzy.morebit.pojo.request.RequestBaseBean;
import com.zjzy.morebit.utils.C;

/**
 * Created by JerryHo on 2019/1/7
 * Description: 请求版本升级bean
 */
public class RequestOsBean  extends RequestBaseBean {

    private int os = C.Setting.os;  //系统  1:android
}
