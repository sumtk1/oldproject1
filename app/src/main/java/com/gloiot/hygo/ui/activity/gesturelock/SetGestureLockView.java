package com.gloiot.hygo.ui.activity.gesturelock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.gloiot.hygo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 整体包含n*n个GestureLockView,每个GestureLockView间间隔mMarginBetweenLockView，
 * 最外层的GestureLockView与容器存在mMarginBetweenLockView的外边距
 * 
 * 关于GestureLockView的边长（n*n）： n * mGestureLockViewWidth + ( n + 1 ) *
 * mMarginBetweenLockView = mWidth ; 得：mGestureLockViewWidth = 4 * mWidth / ( 5
 * * mCount + 1 ) 注：mMarginBetweenLockView = mGestureLockViewWidth * 0.25 ;
 * 
 * @author zhy
 * 
 */
public class SetGestureLockView extends RelativeLayout {

	private static final String TAG = "SetGestureLockView";
	private Handler mHandler = new Handler();
	/**
	 * 保存所有的GestureLockView
	 */
	private GestureLockView[] mGestureLockViews;
	/**
	 * 每个边上的GestureLockView的个数
	 */
	private int mCount = 4;

	/**
	 * 保存用户选中的GestureLockView的id
	 */
	private List<Integer> firstChoose = new ArrayList<Integer>();//第一次绘制
	private List<Integer> secondChoose = new ArrayList<Integer>();//第二次绘制
	/**
	 * 是否是第一次绘制
	 */
	private boolean isfirst = false;

	private Paint mPaint;
	/**
	 * 每个GestureLockView中间的间距 设置为：mGestureLockViewWidth * 25%
	 */
	private int mMarginBetweenLockView = 30;
	/**
	 * GestureLockView的边长 4 * mWidth / ( 5 * mCount + 1 )
	 */
	private int mGestureLockViewWidth;

	/**
	 * GestureLockView无手指触摸的状态下内圆的颜色
	 */
	private int mNoFingerInnerCircleColor = 0xFF939090;
	/**
	 * GestureLockView无手指触摸的状态下外圆的颜色
	 */
	private int mNoFingerOuterCircleColor = 0xFF999999;
	/**
	 * GestureLockView手指触摸的状态下内圆和外圆的颜色
	 */
	private int mFingerOnColor = 0xFF52B4FB;
	/**
	 * GestureLockView手指抬起的状态下内圆和外圆的颜色
	 */
	private int mFingerUpColor = 0xFFFF0000;

	/**
	 * 宽度
	 */
	private int mWidth;
	/**
	 * 高度
	 */
	private int mHeight;

	private Path mPath;
	/**
	 * 指引线的开始位置x
	 */
	private int mLastPathX;
	/**
	 * 指引线的开始位置y
	 */
	private int mLastPathY;
	/**
	 * 指引下的结束位置
	 */
	private Point mTmpTarget = new Point();

	/**
	 * 最大尝试次数
	 */
	private int mTryTimes = 4;
	/**
	 * 回调接口
	 */
	private OnGestureLockViewListener mOnGestureLockViewListener;

	public SetGestureLockView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public SetGestureLockView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		/**
		 * 获得所有自定义的参数的值
		 */
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GestureLockViewGroup, defStyle, 0);
		int n = a.getIndexCount();

		for (int i = 0; i < n; i++)
		{
			int attr = a.getIndex(i);
			switch (attr)
			{
			case R.styleable.GestureLockViewGroup_color_no_finger_inner_circle:
				mNoFingerInnerCircleColor = a.getColor(attr,
						mNoFingerInnerCircleColor);
				break;
			case R.styleable.GestureLockViewGroup_color_no_finger_outer_circle:
				mNoFingerOuterCircleColor = a.getColor(attr,
						mNoFingerOuterCircleColor);
				break;
			case R.styleable.GestureLockViewGroup_color_finger_on:
				mFingerOnColor = a.getColor(attr, mFingerOnColor);
				break;
			case R.styleable.GestureLockViewGroup_color_finger_up:
				mFingerUpColor = a.getColor(attr, mFingerUpColor);
				break;
			case R.styleable.GestureLockViewGroup_count:
				mCount = a.getInt(attr, 3);
				break;
			case R.styleable.GestureLockViewGroup_tryTimes:
				mTryTimes = a.getInt(attr, 5);
			default:
				break;
			}
		}

		a.recycle();

		// 初始化画笔
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.STROKE);
		// mPaint.setStrokeWidth(20);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		// mPaint.setColor(Color.parseColor("#aaffffff"));
		mPath = new Path();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		mWidth = MeasureSpec.getSize(widthMeasureSpec);
		mHeight = MeasureSpec.getSize(heightMeasureSpec);

		// Log.e(TAG, mWidth + "");
		// Log.e(TAG, mHeight + "");

		mHeight = mWidth = mWidth < mHeight ? mWidth : mHeight;

		// setMeasuredDimension(mWidth, mHeight);

		// 初始化mGestureLockViews
		if (mGestureLockViews == null)
		{
			mGestureLockViews = new GestureLockView[mCount * mCount];
			// 计算每个GestureLockView的宽度
//			mGestureLockViewWidth = (int) (4 * mWidth * 1.0f / (5 * mCount + 1));
			mGestureLockViewWidth = (int) (2 * mWidth * 1.0f / (2 * mCount + 4));
			//计算每个GestureLockView的间距
//			mMarginBetweenLockView = (int) (mGestureLockViewWidth * 0.25);
			mMarginBetweenLockView = (int) (mGestureLockViewWidth / 2);
			// 设置画笔的宽度为GestureLockView的内圆直径稍微小点（不喜欢的话，随便设）
			mPaint.setStrokeWidth(mGestureLockViewWidth * 0.29f);

			for (int i = 0; i < mGestureLockViews.length; i++)
			{
				//初始化每个GestureLockView
				mGestureLockViews[i] = new GestureLockView(getContext(),
						mNoFingerInnerCircleColor, mNoFingerOuterCircleColor,
						mFingerOnColor, mFingerUpColor);
				mGestureLockViews[i].setId(i + 1);
				//设置参数，主要是定位GestureLockView间的位置
				LayoutParams lockerParams = new LayoutParams(
						mGestureLockViewWidth, mGestureLockViewWidth);

				// 不是每行的第一个，则设置位置为前一个的右边
				if (i % mCount != 0)
				{
					lockerParams.addRule(RelativeLayout.RIGHT_OF,
							mGestureLockViews[i - 1].getId());
				}
				// 从第二行开始，设置为上一行同一位置View的下面
				if (i > mCount - 1)
				{
					lockerParams.addRule(RelativeLayout.BELOW,
							mGestureLockViews[i - mCount].getId());
				}
				//设置右下左上的边距
				int rightMargin = mMarginBetweenLockView;
				int bottomMargin = mMarginBetweenLockView;
				int leftMagin = 0;
				int topMargin = 0;
				/**
				 * 每个View都有右外边距和底外边距 第一行的有上外边距 第一列的有左外边距
				 */
				if (i >= 0 && i < mCount)// 第一行
				{
					topMargin = mMarginBetweenLockView;
				}
				if (i % mCount == 0)// 第一列
				{
					leftMagin = mMarginBetweenLockView;
				}

				lockerParams.setMargins(leftMagin, topMargin, rightMargin,
						bottomMargin);
				mGestureLockViews[i].setMode(GestureLockView.Mode.STATUS_NO_FINGER);
				addView(mGestureLockViews[i], lockerParams);
			}

			Log.e(TAG, "mWidth = " + mWidth + " ,  mGestureViewWidth = "
					+ mGestureLockViewWidth + " , mMarginBetweenLockView = "
					+ mMarginBetweenLockView);

		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();

		Runnable resetRunnable = new Runnable() {
			@Override
			public void run() {
				// 重置
				reset();
				postInvalidate();//线程执行完后刷新界面
			}
		};

		switch (action)
		{
		case MotionEvent.ACTION_DOWN:
			mHandler.removeCallbacksAndMessages(null);
			// 重置
			reset();
			break;
		case MotionEvent.ACTION_MOVE:
			mPaint.setColor(mFingerOnColor);
			mPaint.setAlpha(50);
			GestureLockView child = getChildIdByPos(x, y);
			if (child != null) {
				int cId = child.getId();
				if (isfirst) {
					if (!secondChoose.contains(cId)) {
						secondChoose.add(cId);
						child.setMode(GestureLockView.Mode.STATUS_FINGER_ON);// 设置指引线的起点
						mLastPathX = child.getLeft() / 2 + child.getRight() / 2;
						mLastPathY = child.getTop() / 2 + child.getBottom() / 2;

						if (secondChoose.size() == 1) {// 当前添加为第一个
							mPath.moveTo(mLastPathX, mLastPathY);
						} else {// 非第一个，将两者使用线连上
							mPath.lineTo(mLastPathX, mLastPathY);
						}
					}
				} else {
					if (!firstChoose.contains(cId)) {
						firstChoose.add(cId);
						child.setMode(GestureLockView.Mode.STATUS_FINGER_ON);// 设置指引线的起点
						mLastPathX = child.getLeft() / 2 + child.getRight() / 2;
						mLastPathY = child.getTop() / 2 + child.getBottom() / 2;

						if (firstChoose.size() == 1) {// 当前添加为第一个
							mPath.moveTo(mLastPathX, mLastPathY);
						} else {// 非第一个，将两者使用线连上
							mPath.lineTo(mLastPathX, mLastPathY);
						}
					}
				}
			}
			// 指引线的终点
			mTmpTarget.x = x;
			mTmpTarget.y = y;
			break;
		case MotionEvent.ACTION_UP:

//			mPaint.setColor(mFingerUpColor);
//			mPaint.setAlpha(50);

			if (!isfirst) {//第一次绘制结束
				if (firstChoose.size() < 3) {
					if (firstChoose.size() > 0) {
						mOnGestureLockViewListener.onFirstSelected(firstChoose, false);
					}
				} else {
					mOnGestureLockViewListener.onFirstSelected(firstChoose, true);
					isfirst = true;
				}
			} else {
				if (secondChoose.size() > 0) {
					if (!checkChoose()) {
						// 改变指引线的状态为UP
						mPaint.setColor(mFingerUpColor);
						mPaint.setAlpha(50);
						// 改变子元素的状态为UP
						changeItemMode();
					}
					mOnGestureLockViewListener.onGestureEvent(checkChoose());
				}
			}

			Log.e(TAG, "firstChoose = " + firstChoose);

			// 将终点设置位置为起点，即取消指引线
			mTmpTarget.x = mLastPathX;
			mTmpTarget.y = mLastPathY;

//			new Handler().postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					// 重置
//					reset();
//					postInvalidate();//线程执行完后刷新界面
//				}
//			}, 1000);

			mHandler.postDelayed(resetRunnable, 1000);

			break;
		}
		invalidate();
		return true;
	}

	/**
	 * 将绘制好的图案变成红色
	 */
	private void changeItemMode() {
		for (GestureLockView gestureLockView : mGestureLockViews)
		{
			if (secondChoose.contains(gestureLockView.getId()))
			{
				gestureLockView.setMode(GestureLockView.Mode.STATUS_FINGER_UP);
			}
		}
	}

	/**
	 * 
	 * 做一些必要的重置
	 */
	private void reset() {
		if (!isfirst) {
			firstChoose.clear();
		}
		secondChoose.clear();
		mPath.reset();
		for (GestureLockView gestureLockView : mGestureLockViews)
		{
			gestureLockView.setMode(GestureLockView.Mode.STATUS_NO_FINGER);
			gestureLockView.setArrowDegree(-1);
		}
	}

	/**
	 * 检查用户绘制的手势是否可以超过3个点
	 * @return
	 */
	private boolean checkChoose() {
		if (firstChoose.size() != secondChoose.size())
			return false;

		for (int i = 0; i < firstChoose.size(); i++) {
			if (firstChoose.get(i) != secondChoose.get(i))
				return false;
		}

		return true;
	}

	/**
	 * 检查当前左边是否在child中
	 * @param child
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean checkPositionInChild(View child, int x, int y)
	{

		//设置了内边距，即x,y必须落入下GestureLockView的内部中间的小区域中，可以通过调整padding使得x,y落入范围不变大，或者不设置padding
		int padding = (int) (mGestureLockViewWidth * 0.15);

		if (x >= child.getLeft() + padding && x <= child.getRight() - padding
				&& y >= child.getTop() + padding
				&& y <= child.getBottom() - padding)
		{
			return true;
		}
		return false;
	}

	/**
	 * 通过x,y获得落入的GestureLockView
	 * @param x
	 * @param y
	 * @return
	 */
	private GestureLockView getChildIdByPos(int x, int y)
	{
		for (GestureLockView gestureLockView : mGestureLockViews)
		{
			if (checkPositionInChild(gestureLockView, x, y))
			{
				return gestureLockView;
			}
		}

		return null;

	}

	/**
	 * 设置回调接口
	 * 
	 * @param listener
	 */
	public void setOnGestureLockViewListener(OnGestureLockViewListener listener)
	{
		this.mOnGestureLockViewListener = listener;
	}

	@Override
	public void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		//绘制GestureLockView间的连线
		if (mPath != null)
		{
			canvas.drawPath(mPath, mPaint);
		}
		//绘制指引线
		if (!isfirst) {
			if (firstChoose.size() > 0) {
				if (mLastPathX != 0 && mLastPathY != 0)
					canvas.drawLine(mLastPathX, mLastPathY, mTmpTarget.x, mTmpTarget.y, mPaint);
			}
		} else {
			if (secondChoose.size() > 0) {
				if (mLastPathX != 0 && mLastPathY != 0)
					canvas.drawLine(mLastPathX, mLastPathY, mTmpTarget.x, mTmpTarget.y, mPaint);
			}
		}

	}

	public interface OnGestureLockViewListener
	{
//		/**
//		 * 第一次绘制完成的结果是否合格
//		 *
//		 * @param firstresult
//		 */
//		public void onFirstResult(boolean firstresult);

		/**
		 * 第一次绘制完成的结果集合
		 *
		 * @param firstChoose
		 * @param firstresult
		 */
		public void onFirstSelected(List<Integer> firstChoose, boolean firstresult);

		/**
		 * 两次绘制结果比较
		 *
		 * @param result
		 */
		public void onGestureEvent(boolean result);

	}
}
