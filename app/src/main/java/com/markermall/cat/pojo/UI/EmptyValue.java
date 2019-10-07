package com.markermall.cat.pojo.UI;

/**
 * Created by YangBoTian on 2018/8/14.
 *  占一整行而无数据的类型，用于分割线、写死的标题等等。
 */

public class EmptyValue {
    public static final int TYPE_ALL_GOODS = 1;
    //public static final int TYPE_LINE = 2;

    public int type;

    public EmptyValue(int type) {
        this.type = type;
    }
}
