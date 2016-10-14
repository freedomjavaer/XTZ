package com.ypwl.xiaotouzi.utils.animation;

import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;

/**
 * This interface is implemented only by animation classes that can be combined  to animate together.
 */
public interface Combinable {

    void animate();

    AnimatorSet getAnimatorSet();

    Animation setInterpolator(TimeInterpolator interpolator);

    long getDuration();

    Animation setDuration(long duration);

    Animation setListener(AnimationListener listener);

}
