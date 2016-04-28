package com.vilyever.popupcontroller.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.CallSuper;
import android.support.v4.view.ViewCompat;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * RippleDrawable
 * AndroidPopupController <com.vilyever.popupcontroller.util>
 * Created by vilyever on 2016/4/15.
 * Feature:
 */
public class RippleDrawable extends Drawable implements Animatable, Animation.AnimationListener {
    final RippleDrawable self = this;


    /* Constructors */
    
    
    /* Public Methods */
    
    
    /* Properties */
    private RippleFrameLayout holderView;
    public RippleDrawable setHolderView(RippleFrameLayout holderView) {
        this.holderView = holderView;
        return this;
    }
    public RippleFrameLayout getHolderView() {
        return this.holderView;
    }

    private boolean animated;
    protected RippleDrawable setAnimated(boolean animated) {
        this.animated = animated;
        return this;
    }
    protected boolean isAnimated() {
        return this.animated;
    }

    private Animation progressAnimation;
    protected final Animation getProgressAnimation() {
        if (this.progressAnimation == null) {
            this.progressAnimation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    self.setProgress(interpolatedTime);
                }
            };
            this.progressAnimation.setAnimationListener(this);

            this.progressAnimation.setDuration(500);
            this.progressAnimation.setInterpolator(new LinearInterpolator());
        }
        return this.progressAnimation;
    }

    private float progress;
    @CallSuper
    protected RippleDrawable setProgress(float progress) {
        this.progress = progress;
        ViewCompat.postInvalidateOnAnimation(getHolderView());
        return this;
    }
    protected float getProgress() {
        return this.progress;
    }

    private int animationRepeatTimes;
    protected RippleDrawable setAnimationRepeatTimes(int animationRepeatTimes) {
        this.animationRepeatTimes = animationRepeatTimes;
        return this;
    }
    protected int getAnimationRepeatTimes() {
        return this.animationRepeatTimes;
    }

    private long animationDuration;
    public RippleDrawable setAnimationDuration(long animationDuration) {
        this.animationDuration = animationDuration;
        getProgressAnimation().setDuration(animationDuration);
        return this;
    }
    public long getAnimationDuration() {
        return this.animationDuration;
    }

    private boolean pressing;
    public RippleDrawable setPressing(boolean pressing) {
        this.pressing = pressing;
        ViewCompat.postInvalidateOnAnimation(getHolderView());
        return this;
    }
    public boolean isPressing() {
        return this.pressing;
    }

    private int highlightColor = Color.TRANSPARENT;
    public RippleDrawable setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
        return this;
    }
    public int getHighlightColor() {
        return this.highlightColor;
    }

    private int rippleColor;
    public RippleDrawable setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
        return this;
    }
    public int getRippleColor() {
        return this.rippleColor;
    }

    private int edgeRoundedRadiusLeftTop;
    public RippleDrawable setEdgeRoundedRadiusLeftTop(int edgeRoundedRadiusLeftTop) {
        this.edgeRoundedRadiusLeftTop = edgeRoundedRadiusLeftTop;
        return this;
    }
    public int getEdgeRoundedRadiusLeftTop() {
        return this.edgeRoundedRadiusLeftTop;
    }

    private int edgeRoundedRadiusRightTop;
    public RippleDrawable setEdgeRoundedRadiusRightTop(int edgeRoundedRadiusRightTop) {
        this.edgeRoundedRadiusRightTop = edgeRoundedRadiusRightTop;
        return this;
    }
    public int getEdgeRoundedRadiusRightTop() {
        return this.edgeRoundedRadiusRightTop;
    }

    private int edgeRoundedRadiusRightBottom;
    public RippleDrawable setEdgeRoundedRadiusRightBottom(int edgeRoundedRadiusRightBottom) {
        this.edgeRoundedRadiusRightBottom = edgeRoundedRadiusRightBottom;
        return this;
    }
    public int getEdgeRoundedRadiusRightBottom() {
        return this.edgeRoundedRadiusRightBottom;
    }

    private int edgeRoundedRadiusLeftBottom;
    public RippleDrawable setEdgeRoundedRadiusLeftBottom(int edgeRoundedRadiusLeftBottom) {
        this.edgeRoundedRadiusLeftBottom = edgeRoundedRadiusLeftBottom;
        return this;
    }
    public int getEdgeRoundedRadiusLeftBottom() {
        return this.edgeRoundedRadiusLeftBottom;
    }

    private RectF animationRect;
    private RectF getAnimationRect() {
        if (this.animationRect == null) {
            this.animationRect = new RectF();
        }
        return this.animationRect;
    }

    /**
     * 边缘圆角绘制帮助矩形
     */
    private RectF edgeRoundedArcRect;
    private RectF getEdgeRoundedArcRect() {
        if (edgeRoundedArcRect == null) {
            edgeRoundedArcRect = new RectF();
        }
        return edgeRoundedArcRect;
    }

    private Path animationPath;
    private Path getAnimationPath() {
        if (this.animationPath == null) {
            this.animationPath = new Path();
        }
        return this.animationPath;
    }

    private Paint animationPaint;
    private Paint getAnimationPaint() {
        if (this.animationPaint == null) {
            this.animationPaint = new Paint();
            this.animationPaint.setAntiAlias(true);
            this.animationPaint.setDither(true);
            this.animationPaint.setStyle(Paint.Style.FILL);
            this.animationPaint.setStrokeJoin(Paint.Join.ROUND);
            this.animationPaint.setStrokeCap(Paint.Cap.ROUND);
        }
        return this.animationPaint;
    }
    
    /* Overrides */
    @Override
    public void draw(Canvas canvas) {
        if (isPressing()) {
            if (getHighlightColor() != Color.TRANSPARENT) {
                int saveCount = canvas.save();

                getAnimationPaint().setColor(getHighlightColor());
                getAnimationPath().reset();

                getAnimationRect().set(0, 0, getHolderView().getWidth(), getHolderView().getHeight());

                getAnimationPath().moveTo(getEdgeRoundedRadiusLeftTop(), getAnimationRect().top);

                getAnimationPath().lineTo(getAnimationRect().right - getEdgeRoundedRadiusRightTop(), getAnimationRect().top);
                if (getEdgeRoundedRadiusRightTop() > 0) {
                    getEdgeRoundedArcRect().set(0, 0, getEdgeRoundedRadiusRightTop() * 2, getEdgeRoundedRadiusRightTop() * 2);
                    getEdgeRoundedArcRect().offsetTo(getAnimationRect().right - getEdgeRoundedRadiusRightTop() * 2, getAnimationRect().top);
                    getAnimationPath().arcTo(getEdgeRoundedArcRect(), 270.0f, 90.0f);
                }

                getAnimationPath().lineTo(getAnimationRect().right, getAnimationRect().bottom - getEdgeRoundedRadiusRightBottom());
                if (getEdgeRoundedRadiusRightBottom() > 0) {
                    getEdgeRoundedArcRect().set(0, 0, getEdgeRoundedRadiusRightBottom() * 2, getEdgeRoundedRadiusRightBottom() * 2);
                    getEdgeRoundedArcRect().offsetTo(getAnimationRect().right - getEdgeRoundedRadiusRightBottom() * 2, getAnimationRect().bottom - getEdgeRoundedRadiusRightBottom() * 2);
                    getAnimationPath().arcTo(getEdgeRoundedArcRect(), 0.0f, 90.0f);
                }

                getAnimationPath().lineTo(getAnimationRect().left + getEdgeRoundedRadiusLeftBottom(), getAnimationRect().bottom);
                if (getEdgeRoundedRadiusLeftBottom() > 0) {
                    getEdgeRoundedArcRect().set(0, 0, getEdgeRoundedRadiusLeftBottom() * 2, getEdgeRoundedRadiusLeftBottom() * 2);
                    getEdgeRoundedArcRect().offsetTo(getAnimationRect().left, getAnimationRect().bottom - getEdgeRoundedRadiusLeftBottom() * 2);
                    getAnimationPath().arcTo(getEdgeRoundedArcRect(), 90.0f, 90.0f);
                }

                getAnimationPath().lineTo(getAnimationRect().left, getAnimationRect().top + getEdgeRoundedRadiusLeftTop());
                if (getEdgeRoundedRadiusLeftTop() > 0) {
                    getEdgeRoundedArcRect().set(0, 0, getEdgeRoundedRadiusLeftTop() * 2, getEdgeRoundedRadiusLeftTop() * 2);
                    getEdgeRoundedArcRect().offsetTo(getAnimationRect().left, getAnimationRect().top);
                    getAnimationPath().arcTo(getEdgeRoundedArcRect(), 180.0f, 90.0f);
                }

                canvas.drawPath(getAnimationPath(), getAnimationPaint());

                canvas.restoreToCount(saveCount);
            }
        }
    }
    
    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        if (visible && isAnimated()) {
            start();
        }
        return super.setVisible(visible, restart);
    }
    
    @Override
    public void setAlpha(int alpha) {
        
    }
    
    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        
    }
    
    @Override
    public int getOpacity() {
        return 0;
    }
    
    /* Delegates */
    /** {@link Animatable} */
    @Override
    @CallSuper
    public void start() {
        getProgressAnimation().reset();
        setAnimationRepeatTimes(0);
        setAnimated(true);
        getHolderView().startAnimation(getProgressAnimation());
    }
    
    @Override
    @CallSuper
    public void stop() {
        getProgressAnimation().cancel();
        getHolderView().clearAnimation();
        setAnimated(false);
    }
    
    @Override
    public boolean isRunning() {
        return isAnimated();
    }
    
    /** {@link Animation.AnimationListener} */
    @Override
    public void onAnimationStart(Animation animation) {
        
    }
    
    @Override
    public void onAnimationEnd(Animation animation) {
        
    }
    
    @Override
    @CallSuper
    public void onAnimationRepeat(Animation animation) {
        setAnimationRepeatTimes(getAnimationRepeatTimes() + 1);
    }
    
    /* Protected Methods */

    /* Private Methods */
    
}