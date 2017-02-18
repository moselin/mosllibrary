package com.moselin.rmlib.mvp.model;

import com.moselin.rmlib.request.ABSSubscriber;

import java.util.Map;

import rx.Subscription;

/**
 * Created by Moselin on 2016/12/1.
 */

public interface IPostUrlModel<E> extends IBaseModel
{
    /**
     *
     * @param url 请求API的地址
     * @param action 请求的动作路径
     * @param parameters 提交的参数
     * @param subscriber subscriber
     * @return Subscription
     */
    Subscription post(String url,String action, Map<String, Object> parameters, ABSSubscriber<E> subscriber);
}
