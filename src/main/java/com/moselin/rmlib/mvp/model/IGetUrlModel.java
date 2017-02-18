package com.moselin.rmlib.mvp.model;

import com.moselin.rmlib.request.ABSSubscriber;

import rx.Subscription;

/**
 * Created by Moselin on 2016/12/1.
 */

public interface IGetUrlModel<E> extends IBaseModel
{
    /**
     *
     * @param url 请求API地址的ip或域名
     * @param action 请求动作路径
     * @param subscriber subscriber
     * @return Subscription
     */
    Subscription get(String url,String action, ABSSubscriber<E> subscriber);
}
