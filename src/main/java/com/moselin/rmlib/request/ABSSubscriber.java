package com.moselin.rmlib.request;

import com.google.gson.Gson;
import com.moselin.rmlib.Mosl;
import com.moselin.rmlib.mvp.presenter.IPresenterCallback;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Subscriber;

/**
 * @Description 重写RxJava的订阅者
 * @Author MoseLin
 * @Date 2016/6/24.
 */
public abstract class ABSSubscriber<E> extends Subscriber<Response<ResponseBody>>
{

    protected IPresenterCallback<E> callback;
    protected Gson gson;
    protected Class<E> eClass;
    public ABSSubscriber(Class<E> eClass, IPresenterCallback<E> callback){
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
    public abstract void onNext(Response<ResponseBody> o);
}
