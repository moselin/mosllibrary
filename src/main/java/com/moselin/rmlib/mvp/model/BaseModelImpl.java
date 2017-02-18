package com.moselin.rmlib.mvp.model;

import com.moselin.rmlib.Mosl;
import com.moselin.rmlib.request.ABSResponseSubscriber;
import com.moselin.rmlib.request.ABSSubscriber;
import com.moselin.rmlib.request.helper.RequestHelper;
import com.moselin.rmlib.util.L;

import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Description
 * @Author MoseLin
 * @Date 2016/10/21.
 */

public class BaseModelImpl
{
    /**
     * get请求
     * @param action 动作路径地址
     * @param subscriber 订阅者回调
     * @param <T> 泛型实体
     * @return 订阅者
     */
    public <T> Subscription getGetSubscription(String action, ABSSubscriber<T> subscriber)
    {

         return RequestHelper.getRequestService().get(Mosl.getT(),action).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);
    }

    /**
     * post请求
     * @param action 动作路径地址
     * @param parameters post参数
     * @param subscriber 订阅者回调
     * @param <T> 泛型实体
     * @return 订阅者
     */
    public  <T> Subscription getPostSubscription(String action, Map<String, Object> parameters, ABSSubscriber<T>
            subscriber)
    {
        if (parameters == null)
            return RequestHelper.getRequestService().post(Mosl.getT(),action).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        return RequestHelper.getRequestService().post(Mosl.getT(),action,parameters).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * get请求带api服务器地址或域名
     * @param url 服务器地址或域名
     * @param action 动作路径地址
     * @param subscriber 订阅者回调
     * @param <T> 泛型实体
     * @return 订阅者
     */
    public <T> Subscription getGetSubscription(String url,String action, ABSSubscriber<T> subscriber)
    {

         return RequestHelper.getRequestService(url).get(Mosl.getT(),action).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);
    }

    /**
     * post请求带api服务器地址或域名
     * @param url 服务器地址或域名
     * @param action 动作路径地址
     * @param parameters post参数
     * @param subscriber 订阅者回调
     * @param <T> 泛型实体
     * @return 订阅者
     */
    public <T> Subscription getPostSubscription(String url,String action, Map<String, Object> parameters, ABSSubscriber<T>
            subscriber)
    {
        if (parameters == null)
            return RequestHelper.getRequestService(url).post(Mosl.getT(),action).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        return RequestHelper.getRequestService(url).post(Mosl.getT(),action,parameters).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public <T> Subscription getResponsePostSubscription(String action, Map<String, Object> parameters, ABSResponseSubscriber<T>
            subscriber)
    {
        if (parameters == null)
            return RequestHelper.getRequestService().postResponse(Mosl.getT(),action).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        return RequestHelper.getRequestService().postResponse(Mosl.getT(),action,parameters).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
