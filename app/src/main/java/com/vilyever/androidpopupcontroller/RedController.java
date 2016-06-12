package com.vilyever.androidpopupcontroller;

import android.content.Context;
import android.widget.FrameLayout;

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
public class RedController extends ViewController {
    final RedController self = this;
    
    
    /* Constructors */
    public RedController(Context context) {
        super(context, R.layout.color_controller);

        setAttachAnimationPerformer(new FadeInAnimationPerformer().setAnimationDirection(AnimationDirection.Right));
        setDetachAnimationPerformer(new FadeOutAnimationPerformer().setAnimationDirection(AnimationDirection.Left));

        getView().setBackgroundColor(Colour.Red);
    }
    
    
    /* Public Methods */
    
    
    /* Properties */
    private FrameLayout contentLayout;
    protected FrameLayout getContentLayout() { if (this.contentLayout == null) {this.contentLayout = findViewById(R.id.contentLayout); } return this.contentLayout; }

    private BlueController blueController;
    protected BlueController getBlueController() {
        if (this.blueController == null) {
            this.blueController = new BlueController(getContext());
        }
        return this.blueController;
    }

    /* Overrides */
    @Override
    protected void onViewAppeared() {
        super.onViewAppeared();

//        getBlueController().attachToParent(getContentLayout());
    }

    /* Delegates */
    
    
    /* Private Methods */
    
}