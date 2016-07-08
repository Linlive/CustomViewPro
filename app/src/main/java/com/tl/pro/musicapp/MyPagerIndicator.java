package com.tl.pro.musicapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * 自定义ViewPagerIndicator
 * Created by Tl on 2016/7/8.
 */
public class MyPagerIndicator extends LinearLayout {

	private Paint mPaint;
	private Path mPath;

	private int mTriangleWidth;
	private int mTriangleHeight = 0;
	private int initIndex = 0;

	private float RADIO_TRIANGLE = 1 / 6F;

	private final int TRIANGLE_WIDTH_MAX = (int) (getScreenWidth() / 3 * RADIO_TRIANGLE);

	private int mInitTranslateX;
	private int mTranslateX = 0;

	private final int DEFAULT_TAB_COUNT = 4;
	private int mVisibleCount = DEFAULT_TAB_COUNT;

	private PageChangeListener mPageChangeListener;

	private ViewPager mViewPager;

	//是否是初始化设置显示页卡
	boolean first = true;

	/**
	 * 标题正常时的颜色
	 */
	private static final int COLOR_TEXT_NORMAL = 0x80FFFFFF;
	/**
	 * 标题选中时的颜色
	 */
	private static final int COLOR_TEXT_HIGHLIGHT_COLOR = 0xFFFFFFFF;

	/**
	 * 提供给使用者调用原始的pagerChangeListener接口
	 */
	public interface PageChangeListener {

		void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

		void onPageSelected(int position);

		void onPageScrollStateChanged(int state);
	}

	public MyPagerIndicator(Context context) {
		this(context, null);
	}

	public MyPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);

		//获取tab数量
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyPagerIndicator);

		mVisibleCount = array.getInt(R.styleable.MyPagerIndicator_visible_count, DEFAULT_TAB_COUNT);
		mTriangleHeight = array.getInt(R.styleable.MyPagerIndicator_triangleHeight, 0);
		if (mVisibleCount <= 0) {
			mVisibleCount = DEFAULT_TAB_COUNT;
		}
		array.recycle();

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.parseColor("#ccffff"));
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));
	}

	/**
	 * 设置tab的标题内容 可选，可以自己在布局文件中写死
	 *
	 * @param datas tab的文字
	 */
	public void setTabItemTitles(List<String> datas) {
		// 如果传入的list有值，则移除布局文件中设置的view
		if (datas != null && datas.size() > 0) {
			this.removeAllViews();
			for (String title : datas) {
				// 添加view
				addView(generateTextView(title));
			}
			// 设置item的click事件
			setItemClickEvent();
		}
	}

	/**
	 * 设置tab显示的数量
	 *
	 * note：应该在使用之前先设置，因为后续的操作使用到了该值
	 * @param count 界面上能显示几个tab
	 */
	public void setVisibleCount(int count) {
		mVisibleCount = count;
	}

	/**
	 * 根据标题生成我们的TextView
	 *
	 * @param text 设置的文本
	 * @return 该 textView
	 */
	private TextView generateTextView(String text) {
		TextView tv = new TextView(getContext());
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.width = getScreenWidth() / mVisibleCount;
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(COLOR_TEXT_NORMAL);
		tv.setText(text);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv.setLayoutParams(lp);
		return tv;
	}

	/**
	 * 初始化viewpager
	 *
	 * @param pager page容器
	 * @param position 游标：从0开始，当大于页数是，指定为显示最后一页
	 */
	public void setViewPager(ViewPager pager, int position) {
		initIndex = position;
		if (initIndex >= getChildCount() - 1) {
			initIndex = getChildCount() - 1;
		}
		mViewPager = pager;

		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// 滚动
				scroll(position, positionOffset);
				// 回调
				if (mPageChangeListener != null) {
					mPageChangeListener.onPageScrolled(position,
							positionOffset, positionOffsetPixels);
				}
			}

			@Override
			public void onPageSelected(int position) {
				// 设置字体颜色高亮
				resetTextViewColor();
				highLightTextView(position);
				// 回调
				if (mPageChangeListener != null) {
					mPageChangeListener.onPageSelected(position);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// 回调
				if (mPageChangeListener != null) {
					mPageChangeListener.onPageScrollStateChanged(state);
				}
			}
		});

		// 设置当前页
		mViewPager.setCurrentItem(initIndex, true);
		// 高亮
		highLightTextView(position);
	}

	public void addPageChangeListener(PageChangeListener listener) {
		this.mPageChangeListener = listener;
	}

	/**
	 * 高亮文本
	 *
	 * @param position 哪一个tab需要设置
	 */
	protected void highLightTextView(int position) {
		View view = getChildAt(position);
		if (view instanceof TextView) {
			((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHT_COLOR);
		}
	}

	/**
	 * 重置文本颜色
	 */
	private void resetTextViewColor() {
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			if (view instanceof TextView) {
				((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mTriangleWidth = (int) (w / mVisibleCount * RADIO_TRIANGLE);
		mTriangleWidth = Math.min(mTriangleWidth, TRIANGLE_WIDTH_MAX);
		int tmpW = w / mVisibleCount / 2 - mTriangleWidth / 2;
		mInitTranslateX = tmpW + (initIndex * (w / mVisibleCount));

		initTriangle();
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.save();

		canvas.translate(mInitTranslateX + mTranslateX, getHeight() + 2);
		canvas.drawPath(mPath, mPaint);

		canvas.restore();
		super.dispatchDraw(canvas);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		int childCount = getChildCount();
		if (childCount <= 0) {
			return;
		}
		for (int i = 0; i < childCount; i++) {
			View view = getChildAt(i);
			LinearLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
			lp.width = 0;
			lp.height = getScreenWidth() / mVisibleCount;
			view.setLayoutParams(lp);
		}
	}

	/**
	 * 获取屏幕的宽度
	 *
	 * @return 屏幕的宽度像素值
	 */
	private int getScreenWidth() {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);

		return dm.widthPixels;
	}

	private void initTriangle() {
		if(mTriangleHeight == 0){
			mTriangleHeight = mTriangleWidth / 2;
		}
		mPath = new Path();
		mPath.moveTo(0, 0);
		mPath.lineTo(mTriangleWidth, 0);
		mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
	}

	/**
	 * 设置点击事件
	 */
	public void setItemClickEvent() {
		int cCount = getChildCount();
		for (int i = 0; i < cCount; i++) {
			final int j = i;
			View view = getChildAt(i);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mViewPager.setCurrentItem(j);
				}
			});
		}
	}

	/**
	 * 指示器跟随 手指滚动
	 *
	 * @param position       当前选项卡所在位置（从0开始计算）
	 * @param positionOffset 当前手指滑动的偏移量（0.0 - 1.0）
	 */
	public void scroll(int position, float positionOffset) {
		int tabWidth = getWidth() / mVisibleCount;
		mTranslateX = (int) (tabWidth * (position - initIndex + positionOffset));

		//初始化的位置坐标
		if (first && getChildCount() > mVisibleCount) {
			//最后一页
			if(position >= getChildCount() - 1){
				this.scrollTo(
						(position - mVisibleCount + 1) * tabWidth + (int) (positionOffset * tabWidth), 0);
			} else if (position >= mVisibleCount - 2 ) {
				this.scrollTo(
						(position - mVisibleCount + 2) * tabWidth + (int) (positionOffset * tabWidth), 0);
			}
			first = false;
			invalidate();
			return;
		}
		//已经滑到 倒数第二个tab的时候，容器不再向左滑动，改为三角形向右滑动
		if (position == getChildCount() - 2) {
			invalidate();
			return;
		}
		//除以上两种情况外，容器向左滑动（当前的位置为最后一个tab）
		if (position >= mVisibleCount - 2 && positionOffset > 0 && getChildCount() > mVisibleCount) {

			if (mVisibleCount != 1) {
				this.scrollTo(
						(position - (mVisibleCount - 2)) * tabWidth + (int) (positionOffset * tabWidth), 0);
			} else {
				this.scrollTo(
						position * tabWidth + (int) (positionOffset * tabWidth), 0);
			}
			first = false;
		}
		invalidate();
	}
}
