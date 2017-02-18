package com.moselin.rmlib.request.helper;

import com.moselin.rmlib.request.RequestClient;
import com.moselin.rmlib.request.api.IRequestService;

/**
 * @Description 网络请求帮助工具类
 * @Author MoseLin
 * @Date 2016/8/10.
 */

public class RequestHelper
{
    public static IRequestService getRequestService(){

        return RequestClient.getClient().getRetrofit().create(IRequestService.class);
    }
    public static IRequestService getRequestService(String url){

        return RequestClient.getClient().getRetrofit(url).create(IRequestService.class);
    }
}
