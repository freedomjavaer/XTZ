package com.ypwl.xiaotouzi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HotPlatformLayout extends ViewGroup {
	private List<Line> mLines = new ArrayList<Line>();// 用来记录布局有多少行
	private Line mCurrentLine;// 记录当前是哪一行
	private int mHorizontalSpace = 10;
	private int mVerticalSpace = 10;

	public HotPlatformLayout(Context context) {
		this(context, null);
	}

	public HotPlatformLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setSpace(int horizontalSpace, int verticalSpace) {
		this.mHorizontalSpace = horizontalSpace;
		this.mVerticalSpace = verticalSpace;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		mLines.clear();
		mCurrentLine = null;
		// 获得layout的宽度
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int childMaxWidth = widthSize - getPaddingLeft() - getPaddingRight();
		int count = getChildCount();
		// 测量孩子
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);

			if (child.getVisibility() == View.GONE) {
				continue;
			}
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			// 记录孩子到行中
			if (mCurrentLine == null) {
				mCurrentLine = new Line(childMaxWidth, mHorizontalSpace);
				mLines.add(mCurrentLine);
			}
			if (mCurrentLine.canAdd(child)) {
				mCurrentLine.addView(child);
			} else {
				mCurrentLine = new Line(childMaxWidth, mHorizontalSpace);
				mLines.add(mCurrentLine);
				mCurrentLine.addView(child);
			}
		}
		// setMeasuredDimension:设置自己宽度和高度, 行的高度的累加
		int heightSize = getPaddingTop() + getPaddingBottom();
		for (int i = 0; i < mLines.size(); i++) {
			Line line = mLines.get(i);
			heightSize += line.mHeight;
			if (i != 0) {
				heightSize += mVerticalSpace;
			}
		}
		setMeasuredDimension(widthSize, heightSize);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		// 给孩子布局,让行给孩子布局
		int top = getPaddingTop();
		for (int i = 0; i < mLines.size(); i++) {
			Line line = mLines.get(i);
			line.layout(getPaddingLeft(), top);
			top += line.mHeight;
			if (i != mLines.size() - 1) {
				top += mVerticalSpace;
			}
		}
	}

	// 用来记录描述 layout中的行的信息
	class Line {
		private List<View> mViews = new ArrayList<View>();
		private int mCurrentWidth;
		private int mHeight;
		private int mMaxWidth;// 最大宽度
		private int mSpace;// 空隙

		// 构造
		public Line(int maxWidth, int space) {
			this.mMaxWidth = maxWidth;
			this.mSpace = space;
		}

		public void layout(int pLeft, int pTop) {
			// 给View布局
			for (int i = 0; i < mViews.size(); i++) {
				View view = mViews.get(i);
				int	width = view.getMeasuredWidth();
				int	height = view.getMeasuredHeight();
				int extraHeight = mHeight - height;
				int l = pLeft;
				int t = (int) (pTop + extraHeight / 2f + 0.5);
				int r = l + width;
				int b = t + height;

				view.layout(l, t, r, b);

				pLeft += width + mSpace;
			}
		}

		public void addView(View view) {
			int width = view.getMeasuredWidth();
			int height = view.getMeasuredHeight();
			mHeight = mHeight < height ? height : mHeight;

			if (mViews.size() == 0) {
				if (width > mMaxWidth) {
					mCurrentWidth = mMaxWidth;
				} else {
					mCurrentWidth = width;
				}
			} else {
				mCurrentWidth += mSpace + width;
			}
			mViews.add(view);
		}

		public boolean canAdd(View view) {
			int width = view.getMeasuredWidth();
			if (mViews.size() == 0) {
				return true;
			} else {
				if (mCurrentWidth + mSpace + width > mMaxWidth) {
					return false;
				} else {
					return true;
				}
			}
		}

	}
}
