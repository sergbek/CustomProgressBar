package com.example.sergbek.cpb_4;


public final class PointRect {

    private int mPointX;
    private int mPointY;

    public PointRect() {
    }

    public PointRect(int pointX, int pointY) {
        mPointX = pointX;
        mPointY = pointY;
    }

    public void set(int pointX, int pointY){
        this.mPointX = pointX;
        this.mPointY = pointY;
    }

    public void setPointX(int pointX) {
        mPointX = pointX;
    }

    public void setPointY(int pointY) {
        mPointY = pointY;
    }

    public int getPointX() {
        return mPointX;
    }

    public int getPointY() {
        return mPointY;
    }
}
