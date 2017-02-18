package com.moselin.rmlib.mvp.model;

import com.moselin.rmlib.request.ABSSubscriber;

import java.util.Map;

import rx.Subscription;

/**
 * @Description
 * @Author MoseLin
 * @Date 2016/10/24.
 */

public class PostModelImpl<E> extends BaseModelImpl implements IPostModel<E>,IPostUrlModel<E>
{
    @Override
    public Subscription post(String action, Map<String,Object> parameters, ABSSubscriber<E> subscriber)
    {
         return getPostSubscription(action,parameters,subscriber);
    }

    @Override
    public Subscription post(String url, String action, Map<String, Object> parameters, ABSSubscriber<E> subscriber)
    {
        return getPostSubscription(url,action,parameters,subscriber);
    }
}
