package com.markermall.cat.utils.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangBoTian on 2018/11/26.
 */

public class StringConverter implements PropertyConverter<List<String>,String> {

    private final Gson mGson;

    public StringConverter() {
        mGson = new Gson();

    }

    @Override
    public List<String> convertToEntityProperty(String databaseValue) {

        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> itemList= mGson.fromJson(databaseValue, type);
        return itemList;
    }

    @Override
    public String convertToDatabaseValue(List<String> entityProperty) {
        String dbString = mGson.toJson(entityProperty);
        return dbString;
    }


}
