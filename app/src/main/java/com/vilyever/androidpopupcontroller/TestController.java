package com.vilyever.androidpopupcontroller;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.vilyever.popupcontroller.ViewController;
import com.vilyever.popupcontroller.animation.FadeInAnimationPerformer;
import com.vilyever.popupcontroller.animation.FadeOutAnimationPerformer;

/**
 * TestController
 * AndroidPopupController <com.vilyever.androidpopupcontroller>
 * Created by vilyever on 2016/4/28.
 * Feature:
 */
public class TestController extends ViewController {
    final TestController self = this;

    public TestController(Context context) {
        super(context, R.layout.test_view);

        setAttachAnimationPerformer(new FadeInAnimationPerformer());
        setDetachAnimationPerformer(new FadeOutAnimationPerformer());
    }
    
    
    /* Constructors */
    
    
    /* Public Methods */
    
    
    /* Properties */
    
    
    /* Overrides */

    @Override
    protected void onViewWillAttachToParent(ViewGroup parent) {
        super.onViewWillAttachToParent(parent);

        Log.d("tttest", "onViewWillAttachToParent " + parent);
    }

    @Override
    protected void onViewAttachedToParent(ViewGroup parent) {
        super.onViewAttachedToParent(parent);

        Log.d("tttest", "onViewAttachedToParent " + parent);
    }

    @Override
    protected void onViewWillAppear() {
        super.onViewWillAppear();

        Log.d("tttest", "onViewWillAppear");
    }

    @Override
    protected void onViewAppeared() {
        super.onViewAppeared();

        Log.d("tttest", "onViewAppeared");
    }

    @Override
    protected void onViewWillDisappear() {
        super.onViewWillDisappear();

        Log.d("tttest", "onViewWillDisappear");
    }

    @Override
    protected void onViewDisappeared() {
        super.onViewDisappeared();

        Log.d("tttest", "onViewDisappeared");
    }

    @Override
    protected void onViewWillDetachFromParent(ViewGroup parent) {
        super.onViewWillDetachFromParent(parent);

        Log.d("tttest", "onViewWillDetachFromParent " + parent);
    }

    @Override
    protected void onViewDetachedFromParent(ViewGroup parent) {
        super.onViewDetachedFromParent(parent);

        Log.d("tttest", "onViewDetachedFromParent " + parent);
    }

    /* Delegates */
    
    
    /* Private Methods */
    
}