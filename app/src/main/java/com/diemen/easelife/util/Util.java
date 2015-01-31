package com.diemen.easelife.util;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.widget.EditText;

/**
 * Created by tfs-hitesh on 11/1/15.
 */
public class Util {

    public static boolean isInteger(String value)
    {
        boolean status = false;

        try
        {
            Integer.parseInt(value);
            status = true;
        }
        catch(NumberFormatException e)
        {
            status = false;
        }
        return status;
    }

    public static void animateBackgroundColor(final EditText editText, Integer colorFrom,Integer colorTo)
    {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(1000);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                editText.setBackgroundColor((Integer)animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }
}