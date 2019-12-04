package com.zjzy.morebit.network;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

/**
 * Created by YangBoTian on 2018/6/6 21:01
 */

final class FastJsonResponseBodyConvert<T> implements Converter<ResponseBody, T> {
    private final Type type;

    public FastJsonResponseBodyConvert(Type type) {
        this.type = type;
    }

    /*
    * 转换方法
    */
    @Override
    public T convert(ResponseBody value) throws IOException {

        BufferedSource bufferedSource = Okio.buffer(value.source());
        String tempStr = bufferedSource.readUtf8();
        Log.i("test","value: " +tempStr.toString());
        Log.i("test","type: " +type );
        bufferedSource.close();
        Log.i("test","JSON: " + JSON.parseObject(tempStr, type));
        return JSON.parseObject(tempStr, type);

    }
}

