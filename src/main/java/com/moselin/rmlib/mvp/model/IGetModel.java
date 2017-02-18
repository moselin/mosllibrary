package com.moselin.rmlib.mvp.model;

import com.moselin.rmlib.request.ABSSubscriber;

import rx.Subscription;

/**
 * @Description
 * @Author MoseLin
 * @Date 2016/10/24.
 */

public interface IGetModel<E> extends IBaseModel
{
    /**
     * 发送get请求
     * @param action action路径
     * @param subscriber 订阅者
     * @return Subscription
     */
    Subscription get(String action, ABSSubscriber<E> subscriber);
}
