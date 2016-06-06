package com.vilyever.androidpopupcontroller;

import android.content.Context;

import com.vilyever.popupcontroller.ViewController;
import com.vilyever.popupcontroller.animation.AnimationDirection;
import com.vilyever.popupcontroller.animation.FadeInAnimationPerformer;
import com.vilyever.popupcontroller.animation.FadeOutAnimationPerformer;
import com.vilyever.resource.Colour;

/**
 * RedController
 * Created by vilyever on 2016/6/3.
 * Feature:
 */
public class BlueController extends ViewController {
    final BlueController self = this;


    /* Constructors */
    public BlueController(Context context) {
        super(context, R.layout.color_controller);

        setAttachAnimationPerformer(new FadeInAnimationPerformer().setAnimationDirection(AnimationDirection.Right));
        setDetachAnimationPerformer(new FadeOutAnimationPerformer().setAnimationDirection(AnimationDirection.Left));

        getView().setBackgroundColor(Colour.Blue);
    }
    
    
    /* Public Methods */
    
    
    /* Properties */
    
    
    /* Overrides */
    
    
    /* Delegates */
    
    
    /* Private Methods */
    
}