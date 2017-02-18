package com.moselin.rmlib.widget.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;

/**
 * @Description 可变颜色的分割线
 * @Author MoseLin
 * @Date 2016/10/14.
 */

public class ColorDividerDecoration extends DividerItemDecoration
{


    private ColorDividerDecoration(Context context, int orientation, int dividerHeight)
    {
        super(context, orientation, dividerHeight);

    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */

    private ColorDividerDecoration(Context context, int orientation, int dividerHeight, int dividerColor)
    {
        this(context, orientation, dividerHeight);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public static ColorDividerDecoration createVerticalDivider(Context context, int mDividerHeight, int dividerColor){
        return new ColorDividerDecoration(context, LinearLayoutManager.VERTICAL,mDividerHeight,dividerColor);
    }
}
