package com.vilyever.popupcontroller.hud;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * HudFrameLayout
 * AndroidPopupController <com.vilyever.popupcontroller.hud>
 * Created by vilyever on 2016/4/15.
 * Feature:
 */
public class HudFrameLayout extends FrameLayout {
    final HudFrameLayout self = this;

    /* Constructors */
    public HudFrameLayout(Context context) {
        super(context);
    }

    public HudFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HudFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    /* Public Methods */
    
    
    /* Properties */
    private int minWidth = 0;
    protected HudFrameLayout setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        return this;
    }
    public int getMinWidth() {
        return this.minWidth;
    }

    private int maxWidth = Integer.MAX_VALUE;
    protected HudFrameLayout setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }
    public int getMaxWidth() {
        return this.maxWidth;
    }

    private int minHeight = 0;
    protected HudFrameLayout setMinHeight(int minHeight) {
        this.minHeight = minHeight;
        return this;
    }
    public int getMinHeight() {
        return this.minHeight;
    }

    private int maxHeight = Integer.MAX_VALUE;
    protected HudFrameLayout setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }
    public int getMaxHeight() {
        return this.maxHeight;
    }
    
    /* Overrides */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        width = Math.max(width, getMinWidth());
        width = Math.min(width, getMaxWidth());
        height = Math.max(height, getMinHeight());
        height = Math.min(height, getMaxHeight());

        setMeasuredDimension(width, height);

//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//
//        int width = widthSize;
//        int height = heightSize;
//
//        if (widthMode != MeasureSpec.EXACTLY) {
//            width = getMinWidth();
//            if (widthMode == MeasureSpec.AT_MOST) {
//                width = Math.min(getMaxWidth(), widthSize);
//            }
//        }
//        if (heightMode != MeasureSpec.EXACTLY) {
//            height = getMinHeight();
//            if (heightMode == MeasureSpec.AT_MOST) {
//                height = Math.min(getMaxHeight(), heightSize);
//            }
//        }
//
//        setMeasuredDimension(width, height);
    }

    /* Delegates */
    
    
    /* Private Methods */
    
}