package com.vilyever.popupcontroller.animation;

import android.animation.Animator;
import android.view.View;

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
        view.animate().cancel();

        view.animate().setDuration(DefaultAnimateDuration);

        switch (self.getAnimationDirection()) {
            case Left:
                view.animate().translationX(-DefaultDirectionMoveDistance);
                break;
            case Top:
                view.animate().translationY(-DefaultDirectionMoveDistance);
                break;
            case Right:
                view.animate().translationX(DefaultDirectionMoveDistance);
                break;
            case Bottom:
                view.animate().translationY(DefaultDirectionMoveDistance);
                break;
            case LeftTop:
                view.animate().translationX(-DefaultDirectionMoveDistance);
                view.animate().translationY(-DefaultDirectionMoveDistance);
                break;
            case RightTop:
                view.animate().translationX(DefaultDirectionMoveDistance);
                view.animate().translationY(-DefaultDirectionMoveDistance);
                break;
            case RightBottom:
                view.animate().translationX(DefaultDirectionMoveDistance);
                view.animate().translationY(DefaultDirectionMoveDistance);
                break;
            case LeftBottom:
                view.animate().translationX(-DefaultDirectionMoveDistance);
                view.animate().translationY(DefaultDirectionMoveDistance);
                break;
        }

        view.animate().alpha(0.0f);
        view.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (listener != null) {
                    listener.onAnimationStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setAlpha(1.0f);
                view.setTranslationX(0.0f);
                view.setTranslationY(0.0f);

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