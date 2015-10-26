package com.example.sergbek.cpb_4;


import android.graphics.Canvas;
import android.graphics.Paint;

public final class Line {

    private int xStart;
    private int yStart;
    private int xEnd;
    private int yEnd;

    private Paint mPaint;

    public Line() {
        mPaint = new Paint();
        mPaint.setColor(0xFFB6DE32);
        mPaint.setStrokeWidth(4);
    }

    public void onDraw(Canvas canvas) {
        canvas.drawLine(xStart, yStart, xEnd, yEnd, mPaint);
    }

    public void set(int xStart, int yStart, int xEnd, int yEnd) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
    }
}
