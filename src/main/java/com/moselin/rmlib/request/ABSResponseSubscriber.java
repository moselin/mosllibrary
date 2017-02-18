package com.moselin.rmlib.request;

import com.google.gson.Gson;
import com.moselin.rmlib.Mosl;
import com.moselin.rmlib.mvp.presenter.IPresenterCallback;

import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * @Description
 * @Author MoseLin
 * @Date 2017/2/9.
 */

public abstract class ABSResponseSubscriber<E> extends Subscriber<retrofit2.Response<ResponseBody>>
{
    protected IPresenterCallback<E> callback;
    protected Gson gson;
    protected Class<E> eClass;
    public ABSResponseSubscriber(Class<E> eClass, IPresenterCallback<E> callback){
        this.callback = callback;
        Mosl.requestCount++;
        gson = new Gson();
        this.eClass = eClass;
    }
    @Override
    public void onCompleted()
    {
        dismissLoading();
    }

    public abstract void dismissLoading();

    @Override
    public void onError(Throwable e)
    {
        dismissLoading();

    }

    @Override
    public abstract void onNext(retrofit2.Response<ResponseBody> o);
}
