package com.moselin.rmlib.mvp.model;

import com.moselin.rmlib.request.ABSResponseSubscriber;

import java.util.Map;

import rx.Subscription;

/**
 * @Description
 * @Author MoseLin
 * @Date 2017/2/9.
 */

public interface IPostResponseModel<E> extends IBaseModel
{
    /**
     * 发送post请求
     * @param action action 路径
     * @param parameters post参数
     * @param subscriber 订阅者
     * @return Subscription
     */
    Subscription post(String action, Map<String, Object> parameters, ABSResponseSubscriber<E> subscriber);
}
