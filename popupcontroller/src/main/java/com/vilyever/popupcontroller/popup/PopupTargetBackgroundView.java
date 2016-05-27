package com.vilyever.popupcontroller.popup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.vilyever.popupcontroller.R;

/**
 * PopupBackgroundView
 * AndroidPopupController <com.vilyever.popupcontroller>
 * Created by vilyever on 2016/2/24.
 * Feature:
 * {@link PopupController}弹出时的背景视图
 * 实现箭头 边框圆角 阴影等效果
 */
public class PopupTargetBackgroundView extends FrameLayout {
    final PopupTargetBackgroundView self = this;

    public static final int WindowLocationCenter = -1;

    /* Constructors */
    public PopupTargetBackgroundView(Context context) {
        super(context);
        internalInit();
    }
    
    public PopupTargetBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        internalInit();
    }
    
    public PopupTargetBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        internalInit();
    }
    
    
    /* Public Methods */
    
    
    /* Properties */
    /**
     * 背景色
     * 此视图的默认background不起作用
     */
    private int popupBackgroundColor;
    public PopupTargetBackgroundView setPopupBackgroundColor(int popupBackgroundColor) {
        this.popupBackgroundColor = popupBackgroundColor;
        invalidate();
        return this;
    }
    public int getPopupBackgroundColor() {
        return this.popupBackgroundColor;
    }

    /**
     * 边缘圆角半径
     */
    private int edgeRoundedRadius;
    public PopupTargetBackgroundView setEdgeRoundedRadius(int edgeRoundedRadius) {
        this.edgeRoundedRadius = edgeRoundedRadius;
        invalidate();
        return this;
    }
    public int getEdgeRoundedRadius() {
        return this.edgeRoundedRadius;
    }

    /**
     * popup方向，与箭头方向相反
     */
    private PopupDirection popupDirection;
    public PopupTargetBackgroundView setPopupDirection(PopupDirection popupDirection) {
        this.popupDirection = popupDirection;
        internalUpdatePadding();
        return this;
    }
    public PopupDirection getPopupDirection() {
        if (this.popupDirection == null) {
            this.popupDirection = PopupDirection.Center;
        }
        return this.popupDirection;
    }

    /**
     * 三角形箭头高度
     */
    private int directionArrowHeight;
    public PopupTargetBackgroundView setDirectionArrowHeight(int directionArrowHeight) {
        this.directionArrowHeight = directionArrowHeight;
        invalidate();
        return this;
    }
    public int getDirectionArrowHeight() {
        return this.directionArrowHeight;
    }

    /**
     * 阴影半径
     */
    private int popupShadowRadius;
    public PopupTargetBackgroundView setPopupShadowRadius(int popupShadowRadius) {
        this.popupShadowRadius = popupShadowRadius;
        internalUpdatePadding();
        return this;
    }
    public int getPopupShadowRadius() {
        return this.popupShadowRadius;
    }

    /**
     * 边缘内距
     */
    private Rect edgePadding;
    public PopupTargetBackgroundView setEdgePadding(Rect edgePadding) {
        this.edgePadding = edgePadding;
        internalUpdatePadding();
        return this;
    }
    public Rect getEdgePadding() {
        if (this.edgePadding == null) {
            this.edgePadding = new Rect();
        }
        return this.edgePadding;
    }

    private int windowX = WindowLocationCenter;
    public PopupTargetBackgroundView setWindowX(int windowX) {
        this.windowX = windowX;
        return this;
    }
    public int getWindowX() {
        return this.windowX;
    }

    private int windowY = WindowLocationCenter;
    public PopupTargetBackgroundView setWindowY(int windowY) {
        this.windowY = windowY;
        return this;
    }
    public int getWindowY() {
        return this.windowY;
    }

    /**
     * 去除阴影面积后的容器大小
     */
    private RectF contentRect;
    private RectF getContentRect() {
        if (this.contentRect == null) {
            this.contentRect = new RectF();
        }
        return this.contentRect;
    }

    /**
     * 边缘圆角绘制帮助矩形
     */
    private RectF edgeRoundedArcRect;
    private RectF getEdgeRoundedArcRect() {
        if (this.edgeRoundedArcRect == null) {
            this.edgeRoundedArcRect = new RectF();
        }
        return this.edgeRoundedArcRect;
    }

    private Path popupBackgroundPath;
    private Path getPopupBackgroundPath() {
        if (this.popupBackgroundPath == null) {
            this.popupBackgroundPath = new Path();
        }
        return this.popupBackgroundPath;
    }

    private Paint popupBackgroundPaint;
    private Paint getPopupBackgroundPaint() {
        if (this.popupBackgroundPaint == null) {
            this.popupBackgroundPaint = new Paint();
            this.popupBackgroundPaint.setAntiAlias(true);
            this.popupBackgroundPaint.setDither(true);
            this.popupBackgroundPaint.setStyle(Paint.Style.FILL);
        }
        return this.popupBackgroundPaint;
    }

    /* Overrides */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 等边三角形箭头边长
        int triangleLength = (int) (getDirectionArrowHeight() * Math.tan(Math.PI / 6) * 2);
    
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
    
        int left = getPopupShadowRadius();
        int top = getPopupShadowRadius();
        int right = getWidth() - getPopupShadowRadius();
        int bottom = getHeight() - getPopupShadowRadius();
    
        getContentRect().set(left, top, right, bottom);
    
        switch (getPopupDirection()) {
            case Center:
                break;
            case Left:
                getContentRect().right -= getDirectionArrowHeight();
                break;
            case Top:
                getContentRect().bottom -= getDirectionArrowHeight();
                break;
            case Right:
                getContentRect().left += getDirectionArrowHeight();
                break;
            case Bottom:
                getContentRect().top += getDirectionArrowHeight();
                break;
        }

//        float width = right - left;
//        float height = baseLineBottom - top;
//        double atan = Math.atan(width / height);
//        double aa = Math.sqrt(2);
//        double bb = (2 - aa) * Math.PI / 4;
//        int roundRectRadius = (int) (Math.sqrt(width + height) * Math.sin(aa * atan + bb));

        getEdgeRoundedArcRect().set(0, 0, getEdgeRoundedRadius() * 2, getEdgeRoundedRadius() * 2);
    
        getPopupBackgroundPath().reset();
    
        getPopupBackgroundPath().moveTo(getContentRect().left + getEdgeRoundedRadius(), getContentRect().top);
    
        if (getPopupDirection() == PopupDirection.Bottom) {
            getPopupBackgroundPath().lineTo(centerX - triangleLength / 2, getContentRect().top);
            getPopupBackgroundPath().lineTo(centerX, top);
            getPopupBackgroundPath().lineTo(centerX + triangleLength / 2, getContentRect().top);
        }
        getPopupBackgroundPath().lineTo(getContentRect().right - getEdgeRoundedRadius(), getContentRect().top);
        getEdgeRoundedArcRect().offsetTo(getContentRect().right - getEdgeRoundedRadius() * 2, getContentRect().top);
        getPopupBackgroundPath().arcTo(getEdgeRoundedArcRect(), 270.0f, 90.0f);
    
        if (getPopupDirection() == PopupDirection.Left) {
            getPopupBackgroundPath().lineTo(getContentRect().right, centerY - triangleLength / 2);
            getPopupBackgroundPath().lineTo(right, centerY);
            getPopupBackgroundPath().lineTo(getContentRect().right, centerY + triangleLength / 2);
        }
        getPopupBackgroundPath().lineTo(getContentRect().right, getContentRect().bottom - getEdgeRoundedRadius());
        getEdgeRoundedArcRect().offsetTo(getContentRect().right - getEdgeRoundedRadius() * 2, getContentRect().bottom - getEdgeRoundedRadius() * 2);
        getPopupBackgroundPath().arcTo(getEdgeRoundedArcRect(), 0.0f, 90.0f);
    
        if (getPopupDirection() == PopupDirection.Top) {
            getPopupBackgroundPath().lineTo(centerX - triangleLength / 2, getContentRect().bottom);
            getPopupBackgroundPath().lineTo(centerX, bottom);
            getPopupBackgroundPath().lineTo(centerX + triangleLength / 2, getContentRect().bottom);
        }
        getPopupBackgroundPath().lineTo(getContentRect().left + getEdgeRoundedRadius(), getContentRect().bottom);
        getEdgeRoundedArcRect().offsetTo(getContentRect().left, getContentRect().bottom - getEdgeRoundedRadius() * 2);
        getPopupBackgroundPath().arcTo(getEdgeRoundedArcRect(), 90.0f, 90.0f);
    
        if (getPopupDirection() == PopupDirection.Right) {
            getPopupBackgroundPath().lineTo(getContentRect().left, centerY - triangleLength / 2);
            getPopupBackgroundPath().lineTo(left, centerY);
            getPopupBackgroundPath().lineTo(getContentRect().left, centerY + triangleLength / 2);
        }
        getPopupBackgroundPath().lineTo(getContentRect().left, getContentRect().top + getEdgeRoundedRadius());
        getEdgeRoundedArcRect().offsetTo(getContentRect().left, getContentRect().top);
        getPopupBackgroundPath().arcTo(getEdgeRoundedArcRect(), 180.0f, 90.0f);
    
        getPopupBackgroundPaint().setColor(getPopupBackgroundColor());
        getPopupBackgroundPaint().setShadowLayer(getPopupShadowRadius(), 0, 0, Color.DKGRAY);
    
        canvas.drawPath(getPopupBackgroundPath(), getPopupBackgroundPaint());
    }

    @Override
    public void setBackground(Drawable background) {
    }

    /* Delegates */
    
    
    /* Private Methods */
    private void internalInit() {
        this.directionArrowHeight = getContext().getResources().getDimensionPixelSize(R.dimen.popupBackgroundArrowHeight);
        this.popupShadowRadius =  getContext().getResources().getDimensionPixelSize(R.dimen.popupBackgroundShadowRadius);
        this.popupBackgroundColor = Color.WHITE;

        setWillNotDraw(false);

        setClipChildren(false);
        setClipToPadding(false);
    }

    /**
     * 更新内距padding
     */
    private void internalUpdatePadding() {
        int left, top, right, bottom;
        left = getEdgePadding().left + getPopupShadowRadius();
        top = getEdgePadding().top + getPopupShadowRadius();;
        right = getEdgePadding().right + getPopupShadowRadius();;
        bottom = getEdgePadding().bottom + getPopupShadowRadius();;
        switch (getPopupDirection()) {
            case Left:
                right += getDirectionArrowHeight();
                break;
            case Top:
                bottom += getDirectionArrowHeight();
                break;
            case Right:
                left += getDirectionArrowHeight();
                break;
            case Bottom:
                top += getDirectionArrowHeight();
                break;
        }
        setPadding(left, top, right, bottom);
    }
    
}