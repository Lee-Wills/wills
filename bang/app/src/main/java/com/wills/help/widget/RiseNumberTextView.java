package com.wills.help.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.nineoldandroids.animation.ValueAnimator;

import java.text.DecimalFormat;


/**
 * 数字增长 TextView ，实现接口RiseNumberBase
 * @author user
 *
 */
public class RiseNumberTextView extends TextView{


	private static final int STOPPED = 0;

	private static final int RUNNING = 1;

	private int mPlayingState = STOPPED;

	private float number;

	private float fromNumber;

	/**
	 * 动画播放时长
	 */
	private long duration = 1000;
	/**
	 * 1.int 2.float
	 */
	private int numberType = 2;

	private DecimalFormat fnum;

	private EndListener mEndListener = null;

	final static int[] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999,
		99999999, 999999999, Integer.MAX_VALUE };


	public RiseNumberTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/** 
	 * 使用xml布局文件默认的被调用的构造方法 
	 *  
	 * @param context 
	 * @param attrs
	 */  
	public RiseNumberTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 构造方法
	 * @param context
	 */
	public RiseNumberTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}



	/**
	 * 判断动画是否正在播放
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return (mPlayingState == RUNNING);
	}

	/**
	 * 跑小数动画
	 */
	private void runFloat() {
		ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.00f, number);
		valueAnimator.setDuration(duration);

		valueAnimator
		.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {

				setText(fnum.format(Float.parseFloat(valueAnimator.getAnimatedValue().toString())));
				if (valueAnimator.getAnimatedFraction() >= 1) {
					mPlayingState = STOPPED;
					if (mEndListener != null)
						mEndListener.onEndFinish();
				}
			}


		});

		valueAnimator.start();
	}

	/**
	 * 跑整数动画 ,fromNumber
	 */
	private void runInt() {

		ValueAnimator valueAnimator = ValueAnimator.ofInt((int) fromNumber,
				(int) number);
		valueAnimator.setDuration(duration);

		valueAnimator
		.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				//设置瞬时的数据值到界面上
				setText(valueAnimator.getAnimatedValue().toString());
				if (valueAnimator.getAnimatedFraction() >= 1) {
					//设置状态为停止
					mPlayingState = STOPPED;
					if (mEndListener != null)
						//通知监听器，动画结束事件
						mEndListener.onEndFinish();
				}
			}
		});
		valueAnimator.start();
	}

	static int sizeOfInt(int x) {
		for (int i = 0;; i++){
			if (x <= sizeTable[i])
				return i + 1;
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		fnum = new DecimalFormat("##0.00");
	}

	/**
	 * 开始播放动画
	 */
	public void start() {

		if (!isRunning()) {
			mPlayingState = RUNNING;
			if (numberType == 1)
				runInt();
			else
				runFloat();
		}
	}

	/**
	 * 设置一个小数进来
	 */
	public void withNumber(float number) {
		this.number = number;
		numberType = 2;
		if (number > 1000) {
			fromNumber = number
					- (float) Math.pow(10, sizeOfInt((int) number) - 1);
		} else {
			fromNumber = number / 2;
		}

	}

	/**
	 * 设置一个整数进来
	 */
	public void withNumber(int number) {
		this.number = number;
		numberType = 1;
		if (number > 1000) {
			fromNumber = number
					- (float) Math.pow(10, sizeOfInt((int) number) - 2);
		} else {
			fromNumber = number / 2;
		}
	}

	/**
	 * 设置动画播放时间
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * 设置动画结束监听器
	 */
	public void setOnEndListener(EndListener callback) {
		mEndListener = callback;
	}

	/**
	 * 定义动画结束接口
	 * 
	 * 
	 */
	public interface EndListener {
		/**
		 * 当动画播放结束时的回调方法
		 */
		public void onEndFinish();
	}
}
