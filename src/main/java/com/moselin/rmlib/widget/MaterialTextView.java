/**
 * <p>Title: MaterialButton.java</p>
 * @author mosl
 * @date 2015年11月17日
 * @since JDK 1.7
 * 
 * 注意：背景只能设置为颜色
 */
package com.moselin.rmlib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.moselin.rmlib.R;


public class MaterialTextView extends TextView implements MaterialBackgroundDetector.Callback
{

	private int cornerRadius = 0;// 圆角
	private int shadowHight = 0;// 阴影面积
	private Drawable bgDrawable;
	private int paddingTop;
	private int paddingBottom;
	private int paddingLeft;
	private int paddingRight;
	private int defualtPadding = 0;
	private MaterialBackgroundDetector mDetector;
	private MaterialBackgroundDetector.Callback callback;
	private Paint paint;
	private RectF rectf;
	private int strokeLine = 0;
	private int strokeColor = 0;
	public MaterialTextView(Context context) {
		super(context);
		init(null, 0);
	}

	public MaterialTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public MaterialTextView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs, defStyleAttr);
	}

	@SuppressLint("Recycle")
	private void init(AttributeSet attrs, int defStyleAttr) {
		paint = new Paint();
		rectf = new RectF();
		if (attrs != null) {
			final TypedArray array = getContext().obtainStyledAttributes(attrs,
					R.styleable.MaterialButton, defStyleAttr, 0);
			for (int i = 0; i < array.getIndexCount(); i++) {
				int attr = array.getIndex(i);
				if (attr == R.styleable.MaterialButton_shadowHeight) {
					shadowHight = array.getInt(i, 0);
				} else if (attr == R.styleable.MaterialButton_cornerRadius) {
					cornerRadius = array.getInt(i, 0);
				} else if (attr == R.styleable.MaterialButton_strokeLine)
				{
					strokeLine = array.getInteger(attr, 0);
				} else if (attr == R.styleable.MaterialButton_strokeColor)
				{
					strokeColor = array.getColor(attr, 0);
				}
			}
		}
		bgDrawable = getDrawable();
		int color = MaterialBackgroundDetector.DEFAULT_COLOR;
		mDetector = new MaterialBackgroundDetector(getContext(), this, this,
				color);
		mDetector.setCornerRadius(cornerRadius);
		paddingTop = getPaddingTop() == 0 ? defualtPadding : getPaddingTop();
		paddingBottom = getPaddingBottom() == 0 ? defualtPadding
				: getPaddingBottom();
		paddingLeft = getPaddingLeft() == 0 ? defualtPadding : getPaddingLeft();
		paddingRight = getPaddingRight() == 0 ? defualtPadding
				: getPaddingRight();
	}

	@SuppressWarnings("deprecation")
	private Drawable getDrawable() {
		Drawable drawable = getBackground();
		int color = Color.WHITE;
		if (drawable != null && drawable instanceof ColorDrawable) {
			ColorDrawable cDrawable = (ColorDrawable) drawable;
			color = cDrawable.getColor();
		}
		// TODO Auto-generated method stub
		float[] outerRadius = new float[] { cornerRadius, cornerRadius,
				cornerRadius, cornerRadius, cornerRadius, cornerRadius,
				cornerRadius, cornerRadius };

		// Top
		RoundRectShape topRoundRect = new RoundRectShape(outerRadius, null,
				null);
		ShapeDrawable topShapeDrawable = new ShapeDrawable(topRoundRect);
		topShapeDrawable.getPaint().setColor(color);
		// Bottom
		RoundRectShape roundRectShape = new RoundRectShape(outerRadius, null,
				null);
		ShapeDrawable bottomShapeDrawable = new ShapeDrawable(roundRectShape);
		bottomShapeDrawable.getPaint().setColor(
				getResources().getColor(R.color.grayHalf));
		// Create array
		Drawable[] drawArray = { bottomShapeDrawable, topShapeDrawable };
		LayerDrawable layerDrawable = new LayerDrawable(drawArray);

		layerDrawable.setLayerInset(0, 0, shadowHight, 0, 0); /*
															 * index, left, top,
															 * right, bottom
															 */
		layerDrawable.setLayerInset(1, 0, 0, 0, shadowHight); /*
															 * index, left, top,
															 * right, bottom
															 */

		return layerDrawable;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mDetector.onSizeChanged(w, h);
	}

	@SuppressLint({ "ClickableViewAccessibility" })
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean superResult = super.onTouchEvent(event);
		return mDetector.onTouchEvent(event, superResult);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isInEditMode()) {
			return;
		}
		mDetector.draw(canvas);
		if (strokeLine > 0)
		{

			paint.setStrokeWidth(strokeLine);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(strokeColor);
			paint.setAntiAlias(true);
			// 根据控件类型的属性去绘制圆形或者矩形
			// 当ShapeType = 1 时 图片为圆角矩形

			rectf.set(strokeLine / 2, strokeLine / 2, getWidth() - strokeLine / 2,
					getHeight() - strokeLine / 2);
			canvas.drawRoundRect(rectf, cornerRadius, cornerRadius, paint);
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		updateBackground(bgDrawable);
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void updateBackground(Drawable background) {
		if (background == null)
			return;
		// Set button background
		if (Build.VERSION.SDK_INT >= 16) {
			this.setBackground(background);
		} else {
			this.setBackgroundDrawable(background);
		}
		setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
	}
	@Override
	public void performClickAfterAnimation()
	{
		if (callback != null)
			callback.performClickAfterAnimation();
	}

	@Override
	public void performLongClickAfterAnimation()
	{
		if (callback != null)
			callback.performLongClickAfterAnimation();
	}

	public void setCallback(MaterialBackgroundDetector.Callback callback)
	{
		this.callback = callback;
	}

	public void handlePerformClick()
	{
		mDetector.handlePerformClick();
	}

	public int getCornerRadius()
	{
		return cornerRadius;
	}
}
