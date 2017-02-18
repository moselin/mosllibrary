package com.moselin.rmlib.mvp.model;

import com.moselin.rmlib.request.ABSResponseSubscriber;

import java.util.Map;

import rx.Subscription;

/**
 * @Description
 * @Author MoseLin
 * @Date 2017/2/9.
 */

public class PostResponseModelImpl<E> extends BaseModelImpl implements IPostResponseModel<E>
{
    @Override
    public Subscription post(String action, Map<String,Object> parameters, ABSResponseSubscriber<E> subscriber)
    {
        return getResponsePostSubscription(action,parameters,subscriber);
    }
}
