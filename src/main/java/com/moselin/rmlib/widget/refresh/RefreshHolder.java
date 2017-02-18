package com.moselin.rmlib.widget.refresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.ButterKnife;


/**
 * @Description 上下拉视图基类
 * @Author MoseLin
 * @Date 2016/7/21.
 */
public abstract class RefreshHolder extends LinearLayout
{
    protected View view;
    private int height;
    private int width;
    public RefreshHolder(Context context){
        super(context);
        view = LayoutInflater.from(context).inflate(getLayout(),this);
        int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(w,h);
        height = view.getMeasuredHeight();
        width = view.getMeasuredWidth();
        ButterKnife.bind(this,view);
    }

    /**
     *
     * @return mxl布局的ID
     */
    protected abstract int getLayout();

    /**
     *
     * @return 视图的高度
     */
    int getViewHeight()
    {
        return height;
    }


    /**
     *
     * @return 视图的大小
     */
    public int getViewWidth()
    {
        return width;
    }

    /**
     * 正常状态，如提示下拉可刷新或加载
     */
    public abstract void normal();

    /**
     * 下拉或上拉到松开手指可刷新的状态或加载
     */
    public abstract void canRefresh();

    /**
     * 开始刷新或加载的状态
     */
    public abstract void beginRefresh();

    /**
     * 刷新或加载完成的状态
     */
    public abstract void refreshComplete();

    /**
     * 清除动画的操作
     */
    public abstract void clearAnimat();

    /**
     * 刷新或加载失败
     */
    public abstract void refreshFail();
}
