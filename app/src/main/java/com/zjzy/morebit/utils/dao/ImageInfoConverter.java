package com.zjzy.morebit.utils.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zjzy.morebit.pojo.ImageInfo;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;

/**
 * Created by YangBoTian on 2018/11/26.
 */

public class ImageInfoConverter implements PropertyConverter<ImageInfo,String> {

    private final Gson mGson;

    public ImageInfoConverter() {
        mGson = new Gson();

    }

    @Override
    public ImageInfo convertToEntityProperty(String databaseValue) {

        Type type = new TypeToken<ImageInfo>() {
        }.getType();
        ImageInfo itemList= mGson.fromJson(databaseValue, type);
        return itemList;
    }

    @Override
    public String convertToDatabaseValue(ImageInfo entityProperty) {
        String dbString = mGson.toJson(entityProperty);
        return dbString;
    }




}
