package com.moselin.rmlib.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.moselin.rmlib.mvp.presenter.BasePresenter;
import com.moselin.rmlib.util.TUitl;

import butterknife.ButterKnife;

/**
 * @Description 要用Mvp模式activity须继承此类
 * @Author MoseLin
 * @Date 2016/7/6.
 */
public abstract class PresenterActivity<P extends BasePresenter> extends AppCompatActivity
{
    protected P presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        presenter = getPresenter();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (presenter != null) presenter.unRegistRx();
    }

    /**
     * 获取控制器
     * @return <？ extends BasePresenter> 如果不设置泛型，请return null
     */
    protected abstract  P getPresenter();

}
