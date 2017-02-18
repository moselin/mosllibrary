package com.moselin.rmlib.request;


import android.support.annotation.NonNull;

import com.moselin.rmlib.Mosl;
import com.moselin.rmlib.util.StringUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Description
 * @Author MoseLin
 * @Date 2016/6/12.
 */
public class RequestClient
{
    private static RequestClient client;
    public static synchronized RequestClient getClient(){
        if (client == null)
            client = new RequestClient();
        return client;
    }

    /**
     * 平常的接口请求
     * @return retrofit
     */
    public Retrofit getRetrofit() {
        return getRetrofit(null,null);
    }

    /**
     * 下载文件的请求
     * @param listener 下载进度监听
     * @return retrofit
     */
    public Retrofit getRetrofit(String apiUrl,ProgressListener listener) {
        String url = Mosl.httpUrl;
        if (!StringUtils.isBlank(apiUrl))
            url = apiUrl;
        OkHttpClient client = Mosl.getClient();
        if (listener != null){
            client = client.newBuilder().addNetworkInterceptor(new ProcessInterceptor(listener)).build();
        }
        Retrofit.Builder builder = getBuilder(url, client);
        return builder.build();
    }

    /**
     *
     * @param url 域名
     * @param client okhttp请求客户端
     * @return retrofit的构建器
     */
    @NonNull
    private Retrofit.Builder getBuilder(String url, OkHttpClient client)
    {
        return new Retrofit.Builder()
                    //baseUrl
                    .baseUrl(url)
                    //gson转化器
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client);
    }

    /**
     *
     * @param url 域名
     * @return retrofit的构建器
     */
    public Retrofit getRetrofit(String url)
    {
        return getRetrofit(url,null);
    }
}
