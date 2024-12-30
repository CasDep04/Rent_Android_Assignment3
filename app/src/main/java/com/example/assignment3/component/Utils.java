package com.example.assignment3.component;

import android.animation.ObjectAnimator;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;

public class Utils {

    public static void animateViewOut(View view, float translationX) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", translationX);
        animator.setDuration(500);
        animator.start();
    }
    public static void animateViewIn(View view, float translationX) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", translationX);
        animator.setDuration(500);
        animator.start();
    }


}
