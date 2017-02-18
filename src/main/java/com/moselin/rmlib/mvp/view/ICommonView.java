package com.moselin.rmlib.mvp.view;


import android.content.Context;

import java.util.Map;

/**
 * @Description 公共的view接口，一般的请求一般都可以直接继承或实现此接口
 * @Author MoseLin
 * @Date 2016/6/24.
 */
public interface ICommonView<E> extends ILoadingView
{


    /**
     * 请求的参数
     * @return map
     */
    Map<String,Object> getParameters();


    /**
     * 请求成功返回的实体
     * @param entity 实体
     */
    void onCompleted(E entity);


    /**
     * 获取action路径
     * @return String
     */
    String getUrlAction();

    /**
     * 获得实体类的class
     * @return class
     */
    Class<E> getEClass();
}
