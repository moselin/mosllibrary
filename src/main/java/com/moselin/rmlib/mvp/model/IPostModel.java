package com.moselin.rmlib.mvp.model;

import com.moselin.rmlib.request.ABSSubscriber;

import java.util.Map;

import rx.Subscription;

/**
 * @Description
 * @Author MoseLin
 * @Date 2016/10/24.
 */

public interface IPostModel<E> extends IBaseModel
{
    /**
     * 发送post请求
     * @param action action 路径
     * @param parameters post参数
     * @param subscriber 订阅者
     * @return Subscription
     */
    Subscription post(String action, Map<String, Object> parameters, ABSSubscriber<E> subscriber);
}
