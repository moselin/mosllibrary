package com.moselin.rmlib.widget.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @Description
 * @Author MoseLin
 * @Date 2016/10/18.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private final boolean isLeft;
    private int space;
    private int row;

    public GridItemDecoration(int space, int row) {
        this(space, row, true);
    }

    public GridItemDecoration(int space, int row, boolean isLeft) {
        this.space = space;
        this.row = row;
        this.isLeft = isLeft;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        if (isLeft)
            outRect.left = space;
            //由于每行都只有row个，所以第一个都是row的倍数，把左边距设为0
        if (parent.getChildLayoutPosition(view) % row == 0)
            outRect.left = 0;

        outRect.bottom = space;

    }

}
