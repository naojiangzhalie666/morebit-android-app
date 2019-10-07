package com.jf.my.utils.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jf.my.pojo.goods.GoodsImgDetailBean;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangBoTian on 2018/11/26.
 */

public class GoodsImgDetailBeanConverter implements PropertyConverter<GoodsImgDetailBean,String> {

    private final Gson mGson;

    public GoodsImgDetailBeanConverter() {
        mGson = new Gson();

    }

    @Override
    public GoodsImgDetailBean convertToEntityProperty(String databaseValue) {

        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        GoodsImgDetailBean item= mGson.fromJson(databaseValue, type);
        return item;
    }

    @Override
    public String convertToDatabaseValue(GoodsImgDetailBean entityProperty) {
        String dbString = mGson.toJson(entityProperty);
        return dbString;
    }


}
