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
import android.view.View;
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
public class PopupBackgroundView extends FrameLayout {
    final PopupBackgroundView self = this;

    /* Constructors */
    public PopupBackgroundView(Context context) {
        super(context);
        init();
    }
    
    public PopupBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public PopupBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    
    /* Public Methods */
    
    
    /* Properties */
    /**
     * 背景色
     * 此视图的默认background不起作用
     */
    private int popupBackgroundColor;
    public PopupBackgroundView setPopupBackgroundColor(int popupBackgroundColor) {
        this.popupBackgroundColor = popupBackgroundColor;
        invalidate();
        return this;
    }
    public int getPopupBackgroundColor() {
        return popupBackgroundColor;
    }

    /**
     * 边缘圆角半径
     */
    private int edgeRoundedRadius;
    public PopupBackgroundView setEdgeRoundedRadius(int edgeRoundedRadius) {
        this.edgeRoundedRadius = edgeRoundedRadius;
        invalidate();
        return this;
    }
    public int getEdgeRoundedRadius() {
        return edgeRoundedRadius;
    }

    /**
     * popup方向，与箭头方向相反
     */
    private PopupDirection popupDirection;
    public PopupBackgroundView setPopupDirection(PopupDirection popupDirection) {
        this.popupDirection = popupDirection;
        updatePadding();
        return this;
    }
    public PopupDirection getPopupDirection() {
        if (popupDirection == null) {
            popupDirection = PopupDirection.Center;
        }
        return popupDirection;
    }

    /**
     * 三角形箭头高度
     */
    private int directionArrowHeight;
    public PopupBackgroundView setDirectionArrowHeight(int directionArrowHeight) {
        this.directionArrowHeight = directionArrowHeight;
        invalidate();
        return this;
    }
    public int getDirectionArrowHeight() {
        return directionArrowHeight;
    }

    /**
     * 阴影半径
     */
    private int popupShadowRadius;
    public PopupBackgroundView setPopupShadowRadius(int popupShadowRadius) {
        this.popupShadowRadius = popupShadowRadius;
        updatePadding();
        return this;
    }
    public int getPopupShadowRadius() {
        return popupShadowRadius;
    }

    /**
     * 边缘内距
     */
    private Rect edgePadding;
    public PopupBackgroundView setEdgePadding(Rect edgePadding) {
        this.edgePadding = edgePadding;
        updatePadding();
        return this;
    }
    public Rect getEdgePadding() {
        if (edgePadding == null) {
            edgePadding = new Rect();
        }
        return edgePadding;
    }

    /**
     * 去除阴影面积后的容器大小
     */
    private RectF contentRect;
    private RectF getContentRect() {
        if (contentRect == null) {
            contentRect = new RectF();
        }
        return contentRect;
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

    private Path popupBackgroundPath;
    private Path getPopupBackgroundPath() {
        if (popupBackgroundPath == null) {
            popupBackgroundPath = new Path();
        }
        return popupBackgroundPath;
    }

    private Paint popupBackgroundPaint;
    private Paint getPopupBackgroundPaint() {
        if (popupBackgroundPaint == null) {
            popupBackgroundPaint = new Paint();
            popupBackgroundPaint.setAntiAlias(true);
            popupBackgroundPaint.setDither(true);
            popupBackgroundPaint.setStyle(Paint.Style.FILL);
        }
        return popupBackgroundPaint;
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
            case Up:
                getContentRect().bottom -= getDirectionArrowHeight();
                break;
            case Right:
                getContentRect().left += getDirectionArrowHeight();
                break;
            case Down:
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
    
        if (getPopupDirection() == PopupDirection.Down) {
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
    
        if (getPopupDirection() == PopupDirection.Up) {
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
    private void init() {
        directionArrowHeight = getContext().getResources().getDimensionPixelSize(R.dimen.popupBackgroundArrowHeight);
        popupShadowRadius =  getContext().getResources().getDimensionPixelSize(R.dimen.popupBackgroundShadowRadius);
        popupBackgroundColor = Color.WHITE;

        setWillNotDraw(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    /**
     * 更新内距padding
     */
    private void updatePadding() {
        int left, top, right, bottom;
        left = getEdgePadding().left + getPopupShadowRadius();
        top = getEdgePadding().top + getPopupShadowRadius();;
        right = getEdgePadding().right + getPopupShadowRadius();;
        bottom = getEdgePadding().bottom + getPopupShadowRadius();;
        switch (getPopupDirection()) {
            case Left:
                right += getDirectionArrowHeight();
                break;
            case Up:
                bottom += getDirectionArrowHeight();
                break;
            case Right:
                left += getDirectionArrowHeight();
                break;
            case Down:
                top += getDirectionArrowHeight();
                break;
        }
        setPadding(left, top, right, bottom);
    }
    
}