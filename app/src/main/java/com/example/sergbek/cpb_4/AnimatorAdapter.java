package com.example.sergbek.cpb_4;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;


public class AnimatorAdapter extends AnimatorListenerAdapter {


    @Override
    public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        animation.start();
    }

}
