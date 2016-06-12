package com.vilyever.popupcontroller.animation;

import android.animation.Animator;
import android.view.View;

import com.vilyever.logger.Logger;

/**
 * FadeOutAnimationPerformer
 * AndroidPopupController <com.vilyever.popupcontroller.animation>
 * Created by vilyever on 2016/3/2.
 * Feature:
 */
public class FadeOutAnimationPerformer extends AnimationPerformer {
    final FadeOutAnimationPerformer self = this;


    /* Constructors */
    
    
    /* Public Methods */
    
    
    /* Properties */

    
    /* Overrides */
    @Override
    public void onAnimation(final View view, final OnAnimationStateChangeListener listener) {
        onAnimationCancel(view);

        int toX = 0;
        int toY = 0;

        switch (self.getAnimationDirection()) {
            case Left:
                toX = -DefaultDirectionMoveDistance;
                break;
            case Top:
                toY = -DefaultDirectionMoveDistance;
                break;
            case Right:
                toX = DefaultDirectionMoveDistance;
                break;
            case Bottom:
                toY = DefaultDirectionMoveDistance;
                break;
            case LeftTop:
                toX = -DefaultDirectionMoveDistance;
                toY = -DefaultDirectionMoveDistance;
                break;
            case RightTop:
                toX = DefaultDirectionMoveDistance;
                toY = -DefaultDirectionMoveDistance;
                break;
            case RightBottom:
                toX = DefaultDirectionMoveDistance;
                toY = DefaultDirectionMoveDistance;
                break;
            case LeftBottom:
                toX = -DefaultDirectionMoveDistance;
                toY = DefaultDirectionMoveDistance;
                break;
        }

        view.setAlpha(1.0f);
        view.setTranslationX(0.0f);
        view.setTranslationY(0.0f);

        view.animate().setDuration(DefaultAnimateDuration)
            .alpha(0.0f)
            .translationX(toX)
            .translationY(toY)
            .setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    self.setAnimating(true);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    self.setAnimating(false);

                    view.setAlpha(1.0f);
                    view.setTranslationX(0.0f);
                    view.setTranslationY(0.0f);

                    if (listener != null) {
                        listener.onAnimationEnd();
                        view.animate().setListener(null);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    self.setAnimating(false);

                    view.setAlpha(1.0f);
                    view.setTranslationX(0.0f);
                    view.setTranslationY(0.0f);

                    if (listener != null) {
                        listener.onAnimationCancel();
                        view.animate().setListener(null);
                    }
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
    }

    @Override
    public void onAnimationCancel(View view) {
        setAnimating(false);
        view.animate().cancel();
    }

    /* Delegates */
     
     
    /* Private Methods */
    
}