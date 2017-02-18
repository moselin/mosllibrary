package com.moselin.rmlib.widget.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.moselin.rmlib.R;

import butterknife.ButterKnife;

/**
 * @Description 自定义PopupWindow基类
 * @Author MoseLin
 * @Date 2016/8/16.
 */

public abstract class BasePopupwindow
{
    private View view;//自定义dialog的View
    private final PopupWindow dialog;
    private Context context;
    private PopupWindow.OnDismissListener onDismissListener;

    public BasePopupwindow(Context context){
        this.context = context;

        view = LayoutInflater.from(context).inflate(getLayoutId(),null);
        ButterKnife.bind(this,view);//注册注解
        dialog = new PopupWindow(view,getWidth(),getHeight());
        dialog.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
            }
        });
        dialog.setOutsideTouchable(true);
        dialog.setFocusable(true);
        dialog.setTouchable(true);
        dialog.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        dialog.setAnimationStyle(getWindowAnimations());
    }

    /**
     *
     * @return dialog height
     */
    protected  int getHeight(){
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    /**
     *
     * @return dialog width
     */
    protected abstract int getWidth();

    /**
     *
     * @return dialog layout id
     */
    protected abstract int getLayoutId();

    /**
     *
     * @return dialog default style
     */
    protected int getStyle(){
        return R.style.default_dialog;
    }

    /**
     * 默认不加入出现的动画
     * @return int
     */
    protected int getWindowAnimations(){
        return 0;
    }

    public View getView()
    {
        return view;
    }

    /**
     * dialog show
     */
    public void show(View anchor){
        if(!dialog.isShowing()) {
            dialog.showAsDropDown(anchor);
            //相对某个控件的位置（正左下方），无偏移
        }
    }
    public void show(View anchor, int xoff, int yoff){
        if (!dialog.isShowing())
            dialog.showAsDropDown(anchor,xoff,yoff);
        //相对某个控件的位置，有偏移;xoff表示x轴的偏移，正值表示向左，负值表示向右；yoff表示相对y轴的偏移，正值是向下，负值是向上；
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void show(View anchor, int xoff, int yoff, int gravity){
        if (!dialog.isShowing())
            dialog.showAsDropDown(anchor,xoff,yoff,gravity);
    }
    public void showAtLocation(View anchor,int gravity, int xoff, int yoff){
        if (!dialog.isShowing())
            dialog.showAtLocation(anchor,gravity,xoff,yoff);
        //相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移
    }

    /**
     * dismiss dialog
     */
    public void dismiss(){
        if (dialog.isShowing())
            dialog.dismiss();
        if (onDismissListener != null)
            onDismissListener.onDismiss();
    }
    public Context getContext()
    {
        return context;
    }

    public int getH()
    {
        return dialog.getHeight();
    }
    public int getW()
    {
        return dialog.getWidth();
    }

    /**
     * 设置PopupWindow的高
     * @param height int
     */
    public void setHeight(int height)
    {
        dialog.setHeight(height);
    }
    public void setWidth(int width)
    {
        dialog.setWidth(width);
    }


    public boolean isShow()
    {
        return dialog.isShowing();
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener)
    {
        this.onDismissListener = onDismissListener;
        dialog.setOnDismissListener(onDismissListener);
    }
}
