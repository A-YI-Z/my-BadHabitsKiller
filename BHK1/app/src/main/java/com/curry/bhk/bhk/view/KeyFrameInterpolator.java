package com.curry.bhk.bhk.view;

import android.animation.TimeInterpolator;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.view.animation.Interpolator;

/**
 * Created by ybq.
 */
public class KeyFrameInterpolator implements Interpolator {

    private TimeInterpolator interpolator;
    private float[] fractions;


    public static KeyFrameInterpolator easeInOut(float... fractions) {
        KeyFrameInterpolator interpolator = new KeyFrameInterpolator(PathInterpolatorCompat.create(0.42f, 0f, 0.58f, 1f));
        interpolator.setFractions(fractions);
        return interpolator;
    }

    public static KeyFrameInterpolator pathInterpolator(float controlX1, float controlY1,
                                                        float controlX2, float controlY2,
                                                        float... fractions) {
        KeyFrameInterpolator interpolator = new KeyFrameInterpolator(PathInterpolatorCompat.create(controlX1, controlY1, controlX2, controlY2));
        interpolator.setFractions(fractions);
        return interpolator;
    }

    public KeyFrameInterpolator(TimeInterpolator interpolator, float... fractions) {
        this.interpolator = interpolator;
        this.fractions = fractions;
    }

    public void setFractions(float... fractions) {
        this.fractions = fractions;
    }

    @Override
    public synchronized float getInterpolation(float input) {
        if (fractions.length > 1) {
            for (int i = 0; i < fractions.length - 1; i++) {
                float start = fractions[i];
                float end = fractions[i + 1];
                float duration = end - start;
                if (input >= start && input <= end) {
                    input = (input - start) / duration;
                    return start + (interpolator.getInterpolation(input)
                            * duration);
                }
            }
        }
        return interpolator.getInterpolation(input);
    }
}