package com.vilyever.popupcontroller;

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
import android.widget.RelativeLayout;

/**
 * PopupBackgroundView
 * AndroidPopupController <com.vilyever.popupcontroller>
 * Created by vilyever on 2016/2/24.
 * Feature:
 */
public class PopupBackgroundView extends RelativeLayout {
    final PopupBackgroundView self = this;

    /* Constructors */
    public PopupBackgroundView(Context context) {
        super(context);
        self.initial();
    }
    
    public PopupBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        self.initial();
    }
    
    public PopupBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        self.initial();
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
        self.invalidate();
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
        self.invalidate();
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
        self.updatePadding();
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
        self.invalidate();
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
        self.updatePadding();
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
        self.updatePadding();
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
        int triangleLength = (int) (self.getDirectionArrowHeight() * Math.tan(Math.PI / 6) * 2);
    
        int centerX = self.getWidth() / 2;
        int centerY = self.getHeight() / 2;
    
        int left = self.getPopupShadowRadius();
        int top = self.getPopupShadowRadius();
        int right = self.getWidth() - self.getPopupShadowRadius();
        int bottom = self.getHeight() - self.getPopupShadowRadius();
    
        self.getContentRect().set(left, top, right, bottom);
    
        switch (self.getPopupDirection()) {
            case Center:
                break;
            case Left:
                self.getContentRect().right -= self.getDirectionArrowHeight();
                break;
            case Up:
                self.getContentRect().bottom -= self.getDirectionArrowHeight();
                break;
            case Right:
                self.getContentRect().left += self.getDirectionArrowHeight();
                break;
            case Down:
                self.getContentRect().top += self.getDirectionArrowHeight();
                break;
        }

//        float width = right - left;
//        float height = baseLineBottom - top;
//        double atan = Math.atan(width / height);
//        double aa = Math.sqrt(2);
//        double bb = (2 - aa) * Math.PI / 4;
//        int roundRectRadius = (int) (Math.sqrt(width + height) * Math.sin(aa * atan + bb));

        self.getEdgeRoundedArcRect().set(0, 0, self.self.getEdgeRoundedRadius() * 2, self.self.getEdgeRoundedRadius() * 2);
    
        self.getPopupBackgroundPath().reset();
    
        self.getPopupBackgroundPath().moveTo(self.getContentRect().left + self.self.getEdgeRoundedRadius(), self.getContentRect().top);
    
        if (self.getPopupDirection() == PopupDirection.Down) {
            self.getPopupBackgroundPath().lineTo(centerX - triangleLength / 2, self.getContentRect().top);
            self.getPopupBackgroundPath().lineTo(centerX, top);
            self.getPopupBackgroundPath().lineTo(centerX + triangleLength / 2, self.getContentRect().top);
        }
        self.getPopupBackgroundPath().lineTo(self.getContentRect().right - self.self.getEdgeRoundedRadius(), self.getContentRect().top);
        self.getEdgeRoundedArcRect().offsetTo(self.getContentRect().right - self.self.getEdgeRoundedRadius() * 2, self.getContentRect().top);
        self.getPopupBackgroundPath().arcTo(self.getEdgeRoundedArcRect(), 270.0f, 90.0f);
    
        if (self.getPopupDirection() == PopupDirection.Left) {
            self.getPopupBackgroundPath().lineTo(self.getContentRect().right, centerY - triangleLength / 2);
            self.getPopupBackgroundPath().lineTo(right, centerY);
            self.getPopupBackgroundPath().lineTo(self.getContentRect().right, centerY + triangleLength / 2);
        }
        self.getPopupBackgroundPath().lineTo(self.getContentRect().right, self.getContentRect().bottom - self.self.getEdgeRoundedRadius());
        self.getEdgeRoundedArcRect().offsetTo(self.getContentRect().right - self.self.getEdgeRoundedRadius() * 2, self.getContentRect().bottom - self.self.getEdgeRoundedRadius() * 2);
        self.getPopupBackgroundPath().arcTo(self.getEdgeRoundedArcRect(), 0.0f, 90.0f);
    
        if (self.getPopupDirection() == PopupDirection.Up) {
            self.getPopupBackgroundPath().lineTo(centerX - triangleLength / 2, self.getContentRect().bottom);
            self.getPopupBackgroundPath().lineTo(centerX, bottom);
            self.getPopupBackgroundPath().lineTo(centerX + triangleLength / 2, self.getContentRect().bottom);
        }
        self.getPopupBackgroundPath().lineTo(self.getContentRect().left + self.self.getEdgeRoundedRadius(), self.getContentRect().bottom);
        self.getEdgeRoundedArcRect().offsetTo(self.getContentRect().left, self.getContentRect().bottom - self.self.getEdgeRoundedRadius() * 2);
        self.getPopupBackgroundPath().arcTo(self.getEdgeRoundedArcRect(), 90.0f, 90.0f);
    
        if (self.getPopupDirection() == PopupDirection.Right) {
            self.getPopupBackgroundPath().lineTo(self.getContentRect().left, centerY - triangleLength / 2);
            self.getPopupBackgroundPath().lineTo(left, centerY);
            self.getPopupBackgroundPath().lineTo(self.getContentRect().left, centerY + triangleLength / 2);
        }
        self.getPopupBackgroundPath().lineTo(self.getContentRect().left, self.getContentRect().top + self.self.getEdgeRoundedRadius());
        self.getEdgeRoundedArcRect().offsetTo(self.getContentRect().left, self.getContentRect().top);
        self.getPopupBackgroundPath().arcTo(self.getEdgeRoundedArcRect(), 180.0f, 90.0f);
    
        self.getPopupBackgroundPaint().setColor(self.getPopupBackgroundColor());
        self.getPopupBackgroundPaint().setShadowLayer(self.getPopupShadowRadius(), 0, 0, Color.DKGRAY);
    
        canvas.drawPath(self.getPopupBackgroundPath(), self.getPopupBackgroundPaint());
    }

    @Override
    public void setBackground(Drawable background) {
    }
     
    /* Delegates */
    
    
    /* Private Methods */
    private void initial() {
        self.directionArrowHeight = self.getContext().getResources().getDimensionPixelSize(R.dimen.PopupBackgroundArrowHeight);
        self.popupShadowRadius =  self.getContext().getResources().getDimensionPixelSize(R.dimen.PopupBackgroundShadowRadius);
        self.popupBackgroundColor = Color.TRANSPARENT;

        self.setWillNotDraw(false);
        self.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    /**
     * 更新内距padding
     */
    private void updatePadding() {
        int left, top, right, bottom;
        left = self.getEdgePadding().left + self.getPopupShadowRadius();
        top = self.getEdgePadding().top + self.getPopupShadowRadius();;
        right = self.getEdgePadding().right + self.getPopupShadowRadius();;
        bottom = self.getEdgePadding().bottom + self.getPopupShadowRadius();;
        switch (self.getPopupDirection()) {
            case Left:
                right += self.getDirectionArrowHeight();
                break;
            case Up:
                bottom += self.getDirectionArrowHeight();
                break;
            case Right:
                left += self.getDirectionArrowHeight();
                break;
            case Down:
                top += self.getDirectionArrowHeight();
                break;
        }
        self.setPadding(left, top, right, bottom);
    }
    
}