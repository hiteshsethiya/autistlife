package com.diemen.easelife.util;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.GridView;

import com.diemen.easelife.easelife.R;

/**
 * Created by tfs-hitesh on 11/1/15.
 */
public class Util {

    private static int tapdone = 0;
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

    public static void animateIngridView(GridView gridView,Animation animation)
    {
        int length = gridView.getCount();

        if(gridView == null || animation == null)
        {
            return;
        }

        tapdone++;
        if (tapdone == 1) {
            startAnimations(length,gridView,animation);

        } else if(tapdone == 2){
            stopAnimations(length,gridView,null);
            tapdone = 0;

        }
    }

    public static void changebackground(GridView gridView, int ontap,int ontaprelease)
    {
        if(gridView == null)
        {
            return;
        }

        tapdone++;
        if (tapdone == 1) {
            gridView.setBackgroundColor(ontap);
        } else if(tapdone == 2){
            gridView.setBackgroundColor(ontaprelease);
            tapdone = 0;
        }
    }

    private static void startAnimations(int length,GridView gridView,Animation animation)
    {
        for(int i = 0; i < length ; ++i) {
            gridView.getChildAt(i).setAnimation(animation);
            gridView.getChildAt(i).startAnimation(animation);
        }
    }
    private static void stopAnimations(int length,GridView gridView,Animation animation)
    {
        for(int i = 0; i < length ; ++i) {
            gridView.getChildAt(i).setAnimation(animation);
        }
    }
}