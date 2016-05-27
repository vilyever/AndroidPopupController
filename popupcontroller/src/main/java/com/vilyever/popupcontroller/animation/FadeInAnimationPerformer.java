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
        view.animate().cancel();

        view.setAlpha(0.0f);

        switch (self.getAnimationDirection()) {
            case Left:
                view.setTranslationX(-DefaultDirectionMoveDistance);
                break;
            case Top:
                view.setTranslationY(-DefaultDirectionMoveDistance);
                break;
            case Right:
                view.setTranslationX(DefaultDirectionMoveDistance);
                break;
            case Bottom:
                view.setTranslationY(DefaultDirectionMoveDistance);
                break;
            case LeftTop:
                view.setTranslationX(-DefaultDirectionMoveDistance);
                view.setTranslationY(-DefaultDirectionMoveDistance);
                break;
            case RightTop:
                view.setTranslationX(DefaultDirectionMoveDistance);
                view.setTranslationY(-DefaultDirectionMoveDistance);
                break;
            case RightBottom:
                view.setTranslationX(DefaultDirectionMoveDistance);
                view.setTranslationY(DefaultDirectionMoveDistance);
                break;
            case LeftBottom:
                view.setTranslationX(-DefaultDirectionMoveDistance);
                view.setTranslationY(DefaultDirectionMoveDistance);
                break;
        }

        view.animate().setDuration(DefaultAnimateDuration);
        view.animate().alpha(1.0f);
        view.animate().translationX(0.0f).translationY(0.0f);
        view.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (listener != null) {
                    listener.onAnimationStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                view.setAlpha(1.0f);
                view.setTranslationX(0.0f);
                view.setTranslationY(0.0f);

                if (listener != null) {
                    listener.onAnimationCancel();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onAnimationCancel(View view) {
        view.animate().cancel();
    }

    /* Delegates */
     
     
    /* Private Methods */
    
}