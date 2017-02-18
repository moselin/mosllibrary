package com.moselin.rmlib.mvp.model;

import com.moselin.rmlib.request.ABSSubscriber;

import rx.Subscription;

/**
 * @Description
 * @Author MoseLin
 * @Date 2016/10/24.
 */

public class GetModelImpl<E> extends BaseModelImpl implements IGetModel<E>,IGetUrlModel<E>
{
    @Override
    public Subscription get(String action, ABSSubscriber<E> subscriber)
    {
        return getGetSubscription(action,subscriber);
    }

    @Override
    public Subscription get(String url, String action, ABSSubscriber<E> subscriber)
    {
        return getGetSubscription(url,action,subscriber);
    }
}
