package com.vilyever.popupcontroller.animation;

import android.view.View;

import com.vilyever.popupcontroller.R;
import com.vilyever.resource.Resource;

/**
 * AnimationPerformer
 * AndroidPopupController <com.vilyever.popupcontroller.animation>
 * Created by vilyever on 2016/3/2.
 * Feature:
 */
public abstract class AnimationPerformer {
    final AnimationPerformer self = this;

    public final static int DefaultDirectionMoveDistance = Resource.getDimensionPixelSize(R.dimen.controllerAnimationDirectionMoveDistance);
    public final static int DefaultAnimateDuration = Resource.getInteger(R.integer.controllerAnimateDuration);
    
    /* Constructors */
    
    
    /* Public Methods */
    /**
     * 实现动画效果
     * 注意：此时view已经添加到window上，但还未计算宽高等参数
     * @param view 当前controller根视图
     */
    public abstract void onAnimation(View view, OnAnimationStateChangeListener listener);

    /**
     * 撤销当前动画
     * @param view 当前controller根视图
     */
    public abstract void onAnimationCancel(View view);

    
    /* Properties */
    private AnimationDirection animationDirection;
    public <T extends AnimationPerformer> T setAnimationDirection(AnimationDirection animationDirection) {
        this.animationDirection = animationDirection;
        return (T) this;
    }
    public AnimationDirection getAnimationDirection() {
        if (animationDirection == null) {
            animationDirection = AnimationDirection.Center;
        }
        return animationDirection;
    }
    
    
    /* Overrides */
     
     
    /* Delegates */
     
     
    /* Private Methods */
    
}