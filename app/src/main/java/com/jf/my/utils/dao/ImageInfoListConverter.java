package com.jf.my.utils.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jf.my.pojo.ImageInfo;
import com.jf.my.utils.MyGsonUtils;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangBoTian on 2018/11/26.
 */

public class ImageInfoListConverter implements PropertyConverter<List<ImageInfo>,String> {

    private final Gson mGson;

    public ImageInfoListConverter() {
        mGson = new Gson();

    }

    @Override
    public List<ImageInfo> convertToEntityProperty(String databaseValue) {


        ArrayList<ImageInfo> itemList= (ArrayList<ImageInfo>) MyGsonUtils.getListBeanWithResult(databaseValue,ImageInfo.class);
        return itemList;
    }

    @Override
    public String convertToDatabaseValue(List<ImageInfo> entityProperty) {
        String dbString = mGson.toJson(entityProperty);
        return dbString;
    }




}
