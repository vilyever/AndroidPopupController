package com.vilyever.popupcontroller.util;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * RippleFrameLayout
 * AndroidPopupController <com.vilyever.popupcontroller.util>
 * Created by vilyever on 2016/4/15.
 * Feature:
 */
public class RippleFrameLayout extends FrameLayout {
    final RippleFrameLayout self = this;


    /* Constructors */
    public RippleFrameLayout(Context context) {
        super(context);
        init();
    }

    public RippleFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RippleFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    /* Public Methods */
    public RippleFrameLayout setHighlightColor(int color) {
        getRippleDrawable().setHighlightColor(color);
        return this;
    }

    public RippleFrameLayout setEdgeRoundedRadiusLeftTop(int edgeRoundedRadiusLeftTop) {
        getRippleDrawable().setEdgeRoundedRadiusLeftTop(edgeRoundedRadiusLeftTop);
        return this;
    }

    public RippleFrameLayout setEdgeRoundedRadiusRightTop(int edgeRoundedRadiusRightTop) {
        getRippleDrawable().setEdgeRoundedRadiusRightTop(edgeRoundedRadiusRightTop);
        return this;
    }

    public RippleFrameLayout setEdgeRoundedRadiusRightBottom(int edgeRoundedRadiusRightBottom) {
        getRippleDrawable().setEdgeRoundedRadiusRightBottom(edgeRoundedRadiusRightBottom);
        return this;
    }

    public RippleFrameLayout setEdgeRoundedRadiusLeftBottom(int edgeRoundedRadiusLeftBottom) {
        getRippleDrawable().setEdgeRoundedRadiusLeftBottom(edgeRoundedRadiusLeftBottom);
        return this;
    }

    /* Properties */
    private RippleDrawable rippleDrawable;
    protected RippleDrawable getRippleDrawable() {
        if (this.rippleDrawable == null) {
            this.rippleDrawable = new RippleDrawable();
            this.rippleDrawable.setHolderView(this);
        }
        return this.rippleDrawable;
    }
    
    /* Overrides */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getRippleDrawable().draw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                getRippleDrawable().setPressing(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                getRippleDrawable().setPressing(false);
                break;

        }

        return super.dispatchTouchEvent(ev);
    }

    /* Delegates */
    
    
    /* Private Methods */
    private void init() {
        setWillNotDraw(false);
    }
}