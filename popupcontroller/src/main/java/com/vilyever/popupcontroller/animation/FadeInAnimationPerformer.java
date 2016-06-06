package com.vilyever.popupcontroller.animation;

import android.animation.Animator;
import android.view.View;

/**
 * FadeInAnimationPerformer
 * AndroidPopupController <com.vilyever.popupcontroller.animation>
 * Created by vilyever on 2016/3/2.
 * Feature:
 */
public class FadeInAnimationPerformer extends AnimationPerformer {
    final FadeInAnimationPerformer self = this;


    /* Constructors */
    
    
    /* Public Methods */
    
    
    /* Properties */

    
    /* Overrides */
    @Override
    public void onAnimation(final View view, final OnAnimationStateChangeListener listener) {
        onAnimationCancel(view);

        int fromX = 0;
        int fromY = 0;

        switch (self.getAnimationDirection()) {
            case Left:
                fromX = -DefaultDirectionMoveDistance;
                break;
            case Top:
                fromY = -DefaultDirectionMoveDistance;
                break;
            case Right:
                fromX = DefaultDirectionMoveDistance;
                break;
            case Bottom:
                fromY = DefaultDirectionMoveDistance;
                break;
            case LeftTop:
                fromX = -DefaultDirectionMoveDistance;
                fromY = -DefaultDirectionMoveDistance;
                break;
            case RightTop:
                fromX = DefaultDirectionMoveDistance;
                fromY = -DefaultDirectionMoveDistance;
                break;
            case RightBottom:
                fromX = DefaultDirectionMoveDistance;
                fromY = DefaultDirectionMoveDistance;
                break;
            case LeftBottom:
                fromX = -DefaultDirectionMoveDistance;
                fromY = DefaultDirectionMoveDistance;
                break;
        }

        view.setAlpha(0.0f);
        view.setTranslationX(fromX);
        view.setTranslationY(fromY);

        view.animate().setDuration(DefaultAnimateDuration)
                        .alpha(1.0f)
                        .translationX(0.0f)
                        .translationY(0.0f)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                self.setAnimating(true);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                self.setAnimating(false);

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
        if (isAnimating()) {
            setAnimating(false);
            view.animate().cancel();
        }
    }

    /* Delegates */
     
     
    /* Private Methods */
    
}