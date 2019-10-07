package com.markermall.cat.Module.service;

import com.markermall.cat.network.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {
    /**
     * http://image.ideayapai.com/upload?defectType=0&perunit=1
     * @param defectType
     * @param perunit
     * @return
     */
    @Multipart

    @POST("upload")
    Observable<BaseResponse<String>> getPictureCheck(@Query("defectType") int defectType, @Query("perunit") double perunit, @Part("file\"; filename=\"image.png\"") RequestBody file);
}