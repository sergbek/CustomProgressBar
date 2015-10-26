package com.example.sergbek.cpb_4;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public final class CustomProgressBar extends View
        implements ValueAnimator.AnimatorUpdateListener {

    private Line mLeftLine;
    private Line mTopLine;
    private Line mRightLine;
    private Line mBottomLine;
    private Line mFirstDiagonal;
    private Line mSecondDiagonal;

    private Rect mLeftTopRect;
    private Rect mRightTopRect;
    private Rect mLeftBottomRect;
    private Rect mRightBottomRect;
    private Rect mCentralRect;

    private Paint mPCornerSquares;
    private Paint mPCenterSquare;

    private int mSideRect;
    private int mSizeSmallRect;
    private int mColorRect;

    private PointRect mRightBottomPoint;
    private PointRect mLeftBottomPoint;
    private PointRect mLeftTopPoint;
    private PointRect mRightTopPoint;

    private AnimatorSet mSet;
    private List<Animator> mAnimatorList;

    private static final int TIME_ALPHA = 3000;

    private static final int DESIRED_WIDTH = 285;
    private static final int DESIRED_HEIGHT = 285;

    public CustomProgressBar(Context context) {
        super(context);
        init();
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mSet = new AnimatorSet();
        mAnimatorList = new ArrayList<>();

        mLeftTopRect = new Rect();
        mRightTopRect = new Rect();
        mLeftBottomRect = new Rect();
        mRightBottomRect = new Rect();
        mCentralRect = new Rect();

        mLeftLine = new Line();
        mTopLine = new Line();
        mRightLine = new Line();
        mBottomLine = new Line();
        mFirstDiagonal = new Line();
        mSecondDiagonal = new Line();

        mPCornerSquares = new Paint();
        mPCenterSquare = new Paint();

        mColorRect = 0xFFB6DE32;
        mPCornerSquares.setColor(mColorRect);
        mPCenterSquare.setColor(mColorRect);

        mRightBottomPoint = new PointRect();
        mLeftBottomPoint = new PointRect();
        mLeftTopPoint = new PointRect();
        mRightTopPoint = new PointRect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width;
        int height;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
            mSideRect = width;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(DESIRED_WIDTH, widthSize);
        } else {
            width = DESIRED_WIDTH;
        }

        mSideRect = width;

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(DESIRED_HEIGHT, heightSize);
        } else {
            height = DESIRED_HEIGHT;
        }

        if (height < width)
            mSideRect = height;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mSizeSmallRect = mSideRect / 16;

        mRightBottomPoint.set(mSideRect - mSizeSmallRect, mSideRect - mSizeSmallRect);
        mLeftBottomPoint.set(0, mSideRect - mSizeSmallRect);
        mRightTopPoint.set(mSideRect - mSizeSmallRect, 0);
        mLeftTopPoint.set(0, 0);

        mLeftTopRect.set(mLeftTopPoint.getPointX(), mLeftTopPoint.getPointY(),
                mSizeSmallRect, mSizeSmallRect);

        mRightTopRect.set(mRightTopPoint.getPointX(), mRightTopPoint.getPointY(),
                mSideRect, mSizeSmallRect);

        mLeftBottomRect.set(mLeftBottomPoint.getPointX(), mLeftBottomPoint.getPointY(),
                mSizeSmallRect, mSideRect);

        mRightBottomRect.set(mRightBottomPoint.getPointX(), mRightBottomPoint.getPointY(),
                mSideRect, mSideRect);

        int percent = mSideRect * 20 / 100;
        mCentralRect.set(percent, percent, mSideRect - percent, mSideRect - percent);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mLeftLine.set(mLeftTopRect.centerX(), mLeftTopRect.centerY(),
                mLeftBottomRect.centerX(), mLeftBottomRect.centerY());
        mTopLine.set(mLeftTopRect.centerX(), mLeftTopRect.centerY(),
                mRightTopRect.centerX(), mRightTopRect.centerY());
        mRightLine.set(mRightTopRect.centerX(), mRightTopRect.centerY(),
                mRightBottomRect.centerX(), mRightBottomRect.centerY());
        mBottomLine.set(mRightBottomRect.centerX(), mRightBottomRect.centerY(),
                mLeftBottomRect.centerX(), mLeftBottomRect.centerY());
        mFirstDiagonal.set(mLeftTopRect.centerX(), mLeftTopRect.centerY(),
                mRightBottomRect.centerX(), mRightBottomRect.centerY());
        mSecondDiagonal.set(mRightTopRect.centerX(), mRightTopRect.centerY(),
                mLeftBottomRect.centerX(), mLeftBottomRect.centerY());

        mLeftLine.onDraw(canvas);
        mTopLine.onDraw(canvas);
        mRightLine.onDraw(canvas);
        mBottomLine.onDraw(canvas);
        mFirstDiagonal.onDraw(canvas);
        mSecondDiagonal.onDraw(canvas);

        canvas.drawRect(mLeftTopRect, mPCornerSquares);
        canvas.drawRect(mRightTopRect, mPCornerSquares);
        canvas.drawRect(mLeftBottomRect, mPCornerSquares);
        canvas.drawRect(mRightBottomRect, mPCornerSquares);
        canvas.drawRect(mCentralRect, mPCenterSquare);
    }

    public void start() {

        if (!mSet.isRunning()) {
            initAnimationList();
            setListeners();
            mSet.playTogether(mAnimatorList);
            mSet.addListener(new AnimatorAdapter());
            mSet.start();
        }
    }

    private void initAnimationList() {
        int center = mSideRect / 2;
        int halfRect = mSideRect - mSizeSmallRect;

        mAnimatorList.clear();
        animationAlphaRect();
        animationPoint(center);
        reversAnimationPoint(center, halfRect);
    }

    private void animationAlphaRect() {
        ObjectAnimator obAlpha = ObjectAnimator.ofInt(this, ALPHA, 255, 0, 255, 0, 255, 0);
        obAlpha.setDuration(TIME_ALPHA);
        mAnimatorList.add(obAlpha);

        ObjectAnimator obAlphaCorner = ObjectAnimator.ofInt(this, ALPHA_CORNER,
                255, 0, 255, 0, 255, 0, 255);
        obAlphaCorner.setStartDelay(TIME_ALPHA + 900);
        obAlphaCorner.setDuration(TIME_ALPHA);
        mAnimatorList.add(obAlphaCorner);
    }


    public final Property<CustomProgressBar, Integer> ALPHA = new Property<CustomProgressBar, Integer>(Integer.TYPE,"alpha") {
        @Override
        public Integer get(CustomProgressBar object) {
            return null;
        }

        @Override
        public void set(CustomProgressBar object, Integer value) {
            mPCenterSquare.setAlpha(value);
        }
    };

    public final Property<CustomProgressBar, Integer> ALPHA_CORNER = new Property<CustomProgressBar, Integer>(Integer.TYPE,"alphaCorner") {
        @Override
        public Integer get(CustomProgressBar object) {
            return null;
        }

        @Override
        public void set(CustomProgressBar object, Integer value) {
            mPCornerSquares.setAlpha(value);
        }
    };

    private void animationPoint(int center) {
        ObjectAnimator obRightBottomY = ObjectAnimator.ofInt(mRightBottomPoint, mRightBottomPoint.POINT_Y,
                mRightBottomPoint.getPointY(), center);
        obRightBottomY.setStartDelay(TIME_ALPHA);
        mAnimatorList.add(obRightBottomY);

        ObjectAnimator obRightBottomX = ObjectAnimator.ofInt(mRightBottomPoint, mRightBottomPoint.POINT_X,
                mRightBottomPoint.getPointX(), center);
        obRightBottomX.setStartDelay(TIME_ALPHA + 300);
        mAnimatorList.add(obRightBottomX);

        ObjectAnimator obLeftBottomX = ObjectAnimator.ofInt(mLeftBottomPoint, mLeftBottomPoint.POINT_X,
                mLeftBottomPoint.getPointX(), center);
        obLeftBottomX.setStartDelay(TIME_ALPHA + 100);
        mAnimatorList.add(obLeftBottomX);

        ObjectAnimator obLeftBottomY = ObjectAnimator.ofInt(mLeftBottomPoint, mLeftBottomPoint.POINT_Y,
                mLeftBottomPoint.getPointY(), center);
        obLeftBottomY.setStartDelay(TIME_ALPHA + 400);
        mAnimatorList.add(obLeftBottomY);

        ObjectAnimator obLeftTopY = ObjectAnimator.ofInt(mLeftTopPoint, mLeftTopPoint.POINT_Y,
                mLeftTopPoint.getPointY(), center);
        obLeftTopY.setStartDelay(TIME_ALPHA + 200);
        mAnimatorList.add(obLeftTopY);

        ObjectAnimator obLeftTopX = ObjectAnimator.ofInt(mLeftTopPoint, mLeftTopPoint.POINT_X,
                mLeftTopPoint.getPointX(), center);
        obLeftTopX.setStartDelay(TIME_ALPHA + 500);
        mAnimatorList.add(obLeftTopX);

        ObjectAnimator obRightTopX = ObjectAnimator.ofInt(mRightTopPoint, mRightTopPoint.POINT_X,
                mRightTopPoint.getPointX(), center);
        obRightTopX.setStartDelay(TIME_ALPHA + 300);
        mAnimatorList.add(obRightTopX);

        ObjectAnimator obRightTopY = ObjectAnimator.ofInt(mRightTopPoint, mRightTopPoint.POINT_Y,
                mRightTopPoint.getPointY(), center);
        obRightTopY.setStartDelay(TIME_ALPHA + 600);
        mAnimatorList.add(obRightTopY);
    }

    private void reversAnimationPoint(int center, int halfRect) {
        ObjectAnimator obReversRightBottomX = ObjectAnimator.ofInt(mRightBottomPoint, mRightBottomPoint.POINT_X,
                center, halfRect);
        obReversRightBottomX.setStartDelay(TIME_ALPHA * 2 + 900);
        mAnimatorList.add(obReversRightBottomX);

        ObjectAnimator obReversRightBottomY = ObjectAnimator.ofInt(mRightBottomPoint, mRightBottomPoint.POINT_Y,
                center, halfRect);
        obReversRightBottomY.setStartDelay(TIME_ALPHA * 2 + 1200);
        mAnimatorList.add(obReversRightBottomY);

        ObjectAnimator obReversLeftBottomY = ObjectAnimator.ofInt(mLeftBottomPoint, mLeftBottomPoint.POINT_Y,
                center, halfRect);
        obReversLeftBottomY.setStartDelay(TIME_ALPHA * 2 + 1000);
        mAnimatorList.add(obReversLeftBottomY);

        ObjectAnimator obReversLeftBottomX = ObjectAnimator.ofInt(mLeftBottomPoint, mLeftBottomPoint.POINT_X,
                center, 0);
        obReversLeftBottomX.setStartDelay(TIME_ALPHA * 2 + 1300);
        mAnimatorList.add(obReversLeftBottomX);

        ObjectAnimator obReversLeftTopX = ObjectAnimator.ofInt(mLeftTopPoint, mLeftTopPoint.POINT_X,
                center, 0);
        obReversLeftTopX.setStartDelay(TIME_ALPHA * 2 + 1100);
        mAnimatorList.add(obReversLeftTopX);

        ObjectAnimator obReversLeftTopY = ObjectAnimator.ofInt(mLeftTopPoint, mLeftTopPoint.POINT_Y,
                center, 0);
        obReversLeftTopY.setStartDelay(TIME_ALPHA * 2 + 1400);
        mAnimatorList.add(obReversLeftTopY);

        ObjectAnimator obReversRightTopY = ObjectAnimator.ofInt(mRightTopPoint, mRightTopPoint.POINT_Y,
                center, 0);
        obReversRightTopY.setStartDelay(TIME_ALPHA * 2 + 1300);
        mAnimatorList.add(obReversRightTopY);

        ObjectAnimator obReversRightTopX = ObjectAnimator.ofInt(mRightTopPoint, mRightTopPoint.POINT_X,
                center, halfRect);
        obReversRightTopX.setStartDelay(TIME_ALPHA * 2 + 1600);
        mAnimatorList.add(obReversRightTopX);
    }

    private void setListeners() {
        int size = mAnimatorList.size();

        for (int i = 0; i < size; i++) {
            Animator animator = mAnimatorList.get(i);
            ((ObjectAnimator) animator).addUpdateListener(this);
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {

        mLeftTopRect.set(mLeftTopPoint.getPointX(),
                mLeftTopPoint.getPointY(),
                mLeftTopPoint.getPointX() + mSizeSmallRect,
                mLeftTopPoint.getPointY() + mSizeSmallRect);

        mRightTopRect.set(mRightTopPoint.getPointX(),
                mRightTopPoint.getPointY(),
                mRightTopPoint.getPointX() + mSizeSmallRect,
                mRightTopPoint.getPointY() + mSizeSmallRect);

        mLeftBottomRect.set(mLeftBottomPoint.getPointX(),
                mLeftBottomPoint.getPointY(),
                mLeftBottomPoint.getPointX() + mSizeSmallRect,
                mLeftBottomPoint.getPointY() + mSizeSmallRect);

        mRightBottomRect.set(mRightBottomPoint.getPointX(),
                mRightBottomPoint.getPointY(),
                mRightBottomPoint.getPointX() + mSizeSmallRect,
                mRightBottomPoint.getPointY() + mSizeSmallRect);

        invalidate();
    }

    public void setColorRect(int colorRect) {
        mColorRect = colorRect;
    }

}
