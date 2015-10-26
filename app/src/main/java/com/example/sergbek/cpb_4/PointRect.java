package com.example.sergbek.cpb_4;


import android.util.Property;

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



    public int getPointX() {
        return mPointX;
    }

    public int getPointY() {
        return mPointY;
    }

    public final Property<PointRect, Integer> POINT_X = new Property<PointRect, Integer>(Integer.TYPE,"setPointX") {
        @Override
        public Integer get(PointRect object) {
            return mPointX;
        }

        @Override
        public void set(PointRect object, Integer value) {
            mPointX = value;
        }
    };

    public final Property<PointRect, Integer> POINT_Y = new Property<PointRect, Integer>(Integer.TYPE,"setPointY") {
        @Override
        public Integer get(PointRect object) {
            return mPointY;
        }

        @Override
        public void set(PointRect object, Integer value) {
            mPointY = value;
        }
    };
}
