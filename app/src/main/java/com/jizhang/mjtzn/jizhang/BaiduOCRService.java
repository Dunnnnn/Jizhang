package com.jizhang.mjtzn.jizhang;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface BaiduOCRService {
    @POST("rest/2.0/ocr/v1/receipt")
    @FormUrlEncoded
    Observable<RecognitionResultBean> getRecognitionResultByImage(@Field("access_token") String accessToken, @Field("image") String image);
}
