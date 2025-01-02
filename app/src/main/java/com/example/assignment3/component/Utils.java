package com.example.assignment3.component;

import android.animation.ObjectAnimator;
import android.view.View;

public class Utils {

    public static ObjectAnimator animateViewOut(View view, float translationX) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", translationX);
        animator.setDuration(500);
        animator.start();
        return animator;
    }

    public static ObjectAnimator animateViewIn(View view, float translationX) {
        view.setTranslationX(translationX); // Start position (off-screen)
        view.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0f); // Slide into view
        animator.setDuration(500);
        animator.start();
        return animator;
    }
}
