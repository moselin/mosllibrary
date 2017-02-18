package com.moselin.rmlib;

import android.annotation.SuppressLint;
import android.content.Context;

import com.moselin.rmlib.request.HeaderInterceptor;
import com.moselin.rmlib.request.RequestCacheInterceptor;
import com.moselin.rmlib.util.L;
import com.moselin.rmlib.util.SPUtils;
import com.moselin.rmlib.util.StringUtils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * @Description
 * @Author MoseLin
 * @Date 2016/6/24.
 */
public class Mosl
{
    private static final String TOKEN = "token";
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    public static String httpUrl;// 请求地址域名
    private static OkHttpClient client;
    public static int requestCount;
    public static String APP_PATH;

    /**
     * 请求无header初始化
     * @param mContext Application context
     * @param url look httpUrl
     */
    public static void init(Context mContext, String url)
    {
        init(mContext, url, null);

    }

    /**
     * 给请求添加header
     * @param mContext Application context
     * @param url look httpUrl
     * @param header request header
     */
    public static void init(Context mContext, String url, final Map<String, String> header)
    {
        context = mContext;
        httpUrl = url;
        //http cache path
        File cacheFile = new File(mContext.getExternalCacheDir(), "responses");
        APP_PATH = mContext.getExternalCacheDir().getPath();
        if (!APP_PATH.endsWith("/"))
            APP_PATH = APP_PATH+File.separator;
        String preferenceName = mContext.getPackageName();
        preferenceName = preferenceName.substring(preferenceName.lastIndexOf(".")+1,preferenceName.length());
        SPUtils.PREFERENCE_NAME = preferenceName+"_Preference";
        //cache size
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);
        //init OkhttpClient
        client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new RequestCacheInterceptor(context))
                .connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(cache)
                .build();
        if (header != null)
        {
            client = client.newBuilder().addInterceptor(new HeaderInterceptor(header)).build();
        }
    }

    public static OkHttpClient getClient()
    {
        return client;
    }

    public static void addHeader(final Map<String, String> header){
        client = client.newBuilder().addInterceptor(new HeaderInterceptor(header)).build();
    }
    public static void saveT(String head)
    {
        if (!StringUtils.isBlank(head))
        if (!StringUtils.isBlank(SPUtils.getString(context,TOKEN,""))&&!head.equals(SPUtils.getString(context,TOKEN,"")))
        {
            SPUtils.putString(context, TOKEN, head);
        }else SPUtils.putString(context, TOKEN, head);
    }

    public static String getT(){
        return SPUtils.getString(context,TOKEN,"");
    }
}
