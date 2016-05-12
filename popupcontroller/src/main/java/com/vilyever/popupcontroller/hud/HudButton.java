package com.vilyever.popupcontroller.hud;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * HudButton
 * Created by vilyever on 2016/5/12.
 * Feature:
 */
public class HudButton extends Button {
    final HudButton self = this;

    /* Constructors */
    public HudButton(Context context) {
        super(context);
    }

    public HudButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HudButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    /* Public Methods */
    
    
    /* Properties */
    private int normalTextColor;
    public HudButton setNormalTextColor(int normalTextColor) {
        this.normalTextColor = normalTextColor;
        setTextColor(this.normalTextColor);
        return this;
    }
    public @ColorInt int getNormalTextColor() {
        return this.normalTextColor;
    }

    private int highlightTextColor;
    public HudButton setHighlightTextColor(int highlightTextColor) {
        this.highlightTextColor = highlightTextColor;
        return this;
    }
    public @ColorInt int getHighlightTextColor() {
        return this.highlightTextColor;
    }
    
    /* Overrides */
    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);

        setTextColor(pressed ? getHighlightTextColor() : getNormalTextColor());
    }

    /* Delegates */
    
    
    /* Private Methods */
    
}