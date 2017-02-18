package com.moselin.rmlib.mvp.presenter;

import com.moselin.rmlib.mvp.model.IBaseModel;
import com.moselin.rmlib.mvp.view.IBaseView;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @Description P层基类
 * @Author MoseLin
 * @Date 2016/7/6.
 */
public abstract class BasePresenter<M extends IBaseModel,V extends IBaseView>
{
    protected M model;
    protected V view;
    private CompositeSubscription sub = new CompositeSubscription();
    public void setModel(M m){
        model = m;
    }
    public void setView(V v){
        view = v;
    }
    public void setModelView(M m,V v){
        model = m;
        view = v;
    }
    /**
     * 取消注册RxJava 防止内存溢出
     */
    public void unRegistRx(){
        if (sub != null && sub.hasSubscriptions())
        {
            sub.unsubscribe();
            view = null;
        }
    }
    protected void addSub(Subscription subscription)
    {
        if (sub != null)
        sub.add(subscription);
    }
}
