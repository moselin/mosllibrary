package com.moselin.rmlib.request.api;


import android.support.annotation.Nullable;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @Description 请求服务
 * @Author MoseLin
 * @Date 2016/6/6.
 */
public interface IRequestService
{

    /**
     * 发起get请求
     * @param url action路径
     * @return Observable
     */
    @GET()
    Observable<Response<ResponseBody>> get(@Header("t") String token,@Url String url);

    /**
     * 发起post请求
     * @param url action路径
     * @param parameters post参数
     * @return Observable
     */
    @FormUrlEncoded
    @POST()
    Observable<Response<ResponseBody>> post(@Header("t") String token,@Url String url, @Nullable @FieldMap(encoded = true) Map<String, Object> parameters);
    /**
     * 发起post请求
     * @param url action路径
     * @return Observable
     */
    @POST()
    Observable<Response<ResponseBody>> post(@Header("t") String token,@Url String url);
    @POST()
    Observable<Response<ResponseBody>> postResponse(@Header("t") String token,@Url String url);
    @FormUrlEncoded
    @POST()
    Observable<Response<ResponseBody>> postResponse(@Header("t") String token,@Url String url, @Nullable @FieldMap(encoded = true) Map<String, Object> parameters);

}
