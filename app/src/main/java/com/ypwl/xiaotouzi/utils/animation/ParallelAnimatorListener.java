package com.ypwl.xiaotouzi.utils.animation;

/**
 * This method is called when the parallel animation ends.
 */
public interface ParallelAnimatorListener {

    /**
     * This method is called when the parallel animation ends.
     *
     * @param parallelAnimator The ParallelAnimator object.
     */
    void onAnimationEnd(ParallelAnimator parallelAnimator);
}