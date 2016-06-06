package com.vilyever.popupcontroller.animation;

import android.animation.Animator;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.vilyever.popupcontroller.R;

/**
 * FadeInAnimationPerformer
 * AndroidPopupController <com.vilyever.popupcontroller.animation>
 * Created by vilyever on 2016/3/2.
 * Feature:
 */
public class DialogEnterAnimationPerformer extends AnimationPerformer {
    final DialogEnterAnimationPerformer self = this;


    /* Constructors */
    
    
    /* Public Methods */
    
    
    /* Properties */

    
    /* Overrides */
    @Override
    public void onAnimation(final View view, final OnAnimationStateChangeListener listener) {
        onAnimationCancel(view);

        view.setAlpha(0.0f);
        view.setScaleX(0.8f);
        view.setScaleY(0.8f);

        view.animate().setDuration(DefaultAnimateDuration)
            .setInterpolator(AnimationUtils.loadInterpolator(view.getContext(), R.anim.popup_controller_decelerate_quint))
            .alpha(1.0f)
            .scaleX(1.0f)
            .scaleY(1.0f)
            .setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    self.setAnimating(true);

                    switch (self.getAnimationDirection()) {
                        case Center:
                            view.setPivotX(view.getWidth() / 2.0f);
                            view.setPivotY(view.getHeight() / 2.0f);
                            break;
                        case Left:
                            view.setPivotX(view.getWidth());
                            view.setPivotY(view.getHeight() / 2.0f);
                            break;
                        case Top:
                            view.setPivotX(view.getWidth() / 2.0f);
                            view.setPivotY(view.getHeight());
                            break;
                        case Right:
                            view.setPivotX(0.0f);
                            view.setPivotY(view.getHeight() / 2.0f);
                            break;
                        case Bottom:
                            view.setPivotX(view.getWidth() / 2.0f);
                            view.setPivotY(0.0f);
                            break;
                        case LeftTop:
                            view.setPivotX(view.getWidth());
                            view.setPivotY(view.getHeight());
                            break;
                        case RightTop:
                            view.setPivotX(0.0f);
                            view.setPivotY(view.getHeight());
                            break;
                        case RightBottom:
                            view.setPivotX(0.0f);
                            view.setPivotY(0.0f);
                            break;
                        case LeftBottom:
                            view.setPivotX(view.getWidth());
                            view.setPivotY(0.0f);
                            break;
                    }
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
                    view.setScaleX(1.0f);
                    view.setScaleY(1.0f);

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