package com.vilyever.popupcontroller;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * PopupController
 * AndroidPopupController <com.vilyever.popupcontroller>
 * Created by vilyever on 2016/2/24.
 * Feature:
 * popupWindow封装
 */
public class PopupController {
    final PopupController self = this;


    /* Constructors */
    public PopupController(Context context, int layout) {
        this(View.inflate(context, layout, null));
    }

    public PopupController(View view) {
        self.setView(view);
    }
    
    /* Public Methods */
    /**
     * 添加当前controller的view到一个父view上
     * @param parent 父view
     */
    public void attachToParent(ViewGroup parent) {
        if (self.getView().getParent() == parent) {
            return;
        }

        self.detachFromParent();
        parent.addView(self.getView());
    }
    
    /**
     * 将当前controller的view从父view移除
     */
    public void detachFromParent() {
        if (self.getView().getParent() != null) {
            ((ViewGroup) self.getView().getParent()).removeView(self.getView());
        }
    }

    /** @see #popupFromView(View, PopupDirection, boolean, int, int)  */
    public void popupFromView(@NonNull View anchorView, @NonNull PopupDirection popupDirection) {
        self.popupFromView(anchorView, popupDirection, false, 0, 0);
    }

    /** @see #popupFromView(View, PopupDirection, boolean, int, int)  */
    public void popupFromView(@NonNull View anchorView, @NonNull PopupDirection popupDirection, boolean withArrow) {
        self.popupFromView(anchorView, popupDirection, withArrow, 0, 0);
    }
    
    /**
     * 从某个view上弹出popupWindow
     * @param anchorView 锚view
     * @param popupDirection popupWindow方向
     * @param withArrow 是否显示箭头，仅对左上右下四个正方向有效。{@link PopupDirection#Left}{@link PopupDirection#Up}{@link PopupDirection#Right}{@link PopupDirection#Down}
     * @param offsetX X轴偏移量
     * @param offsetY Y轴偏移量
     */
    public void popupFromView(@NonNull View anchorView, @NonNull PopupDirection popupDirection, boolean withArrow, int offsetX, int offsetY) {
        if (self.getPopupWindow().isShowing()) {
            return;
        }

        self.attachToParent(self.getPopupBackgroundView());

        if (withArrow) {
            self.getPopupBackgroundView().setPopupDirection(popupDirection);
        }
        else {
            self.getPopupBackgroundView().setPopupDirection(PopupDirection.Center);
        }

        self.getPopupWindow().setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        self.getPopupWindow().setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        self.getPopupWindow().getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = self.getPopupWindow().getContentView().getMeasuredWidth();
        int popupHeight = self.getPopupWindow().getContentView().getMeasuredHeight();

        int[] location = new int[2];
        anchorView.getLocationInWindow(location);
        int originX = location[0];
        int originY = location[1];

        int popupX = 0, popupY = 0;

        switch (popupDirection) {
            case Center:
                popupX = originX - (popupWidth / 2 - anchorView.getWidth() / 2) + offsetX;
                popupY = originY - (popupHeight / 2 - anchorView.getHeight() / 2) + offsetY;
                break;
            case Left:
                popupX = originX - popupWidth + self.getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                popupY = originY - (popupHeight / 2 - anchorView.getHeight() / 2) + offsetY;
                break;
            case Up:
                popupX = originX - (popupWidth / 2 - anchorView.getWidth() / 2) + offsetX;
                popupY = originY - popupHeight + self.getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                break;
            case Right:
                popupX = originX + anchorView.getWidth() - self.getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                popupY = originY - (popupHeight / 2 - anchorView.getHeight() / 2) + offsetY;
                break;
            case Down:
                popupX = originX - (popupWidth / 2 - anchorView.getWidth() / 2) + offsetX;
                popupY = originY + anchorView.getHeight() - self.getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                break;
            case LeftUp:
                popupX = originX - popupWidth + self.getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                popupY = originY - popupHeight + self.getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                break;
            case RightUp:
                popupX = originX + anchorView.getWidth() - self.getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                popupY = originY - popupHeight + self.getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                break;
            case RightDown:
                popupX = originX + anchorView.getWidth() - self.getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                popupY = originY + anchorView.getHeight() - self.getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                break;
            case LeftDown:
                popupX = originX - popupWidth + self.getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                popupY = originY + anchorView.getHeight() - self.getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                break;
        }

        self.getPopupWindow().showAtLocation(anchorView, Gravity.START | Gravity.TOP, popupX, popupY);
    }

    /** @see #popupInView(View, PopupDirection, int, int)  */
    public void popupInView(@NonNull View anchorView, @NonNull PopupDirection popupDirection) {
        self.popupInView(anchorView, popupDirection, 0, 0);
    }

    /**
     * 在某个view内弹出popupWindow
     * @param anchorView 锚view
     * @param popupDirection popupWindow方向
     * @param offsetX X轴偏移量
     * @param offsetY Y轴偏移量
     */
    public void popupInView(@NonNull View anchorView, @NonNull PopupDirection popupDirection, int offsetX, int offsetY) {
        if (self.getPopupWindow().isShowing()) {
            return;
        }

        self.attachToParent(self.getPopupBackgroundView());

        self.getPopupBackgroundView().setPopupDirection(PopupDirection.Center);

        self.getPopupWindow().setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        self.getPopupWindow().setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        self.getPopupWindow().getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = self.getPopupWindow().getContentView().getMeasuredWidth();
        int popupHeight = self.getPopupWindow().getContentView().getMeasuredHeight();

        int[] location = new int[2];
        anchorView.getLocationInWindow(location);
        int originX = location[0];
        int originY = location[1];

        int popupX = 0, popupY = 0;

        switch (popupDirection) {
            case Center:
                popupX = originX + (anchorView.getWidth() / 2 - popupWidth / 2) + offsetX;
                popupY = originY + (anchorView.getHeight() / 2 - popupHeight / 2) + offsetY;
                break;
            case Left:
                popupX = originX - self.getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                popupY = originY - (popupHeight / 2 - anchorView.getHeight() / 2) + offsetY;
                break;
            case Up:
                popupX = originX - (popupWidth / 2 - anchorView.getWidth() / 2) + offsetX;
                popupY = originY - self.getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                break;
            case Right:
                popupX = originX + anchorView.getWidth() - popupWidth + self.getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                popupY = originY - (popupHeight / 2 - anchorView.getHeight() / 2) + offsetY;
                break;
            case Down:
                popupX = originX - (popupWidth / 2 - anchorView.getWidth() / 2) + offsetX;
                popupY = originY + anchorView.getHeight() - popupHeight + self.getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                break;
            case LeftUp:
                popupX = originX - self.getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                popupY = originY - self.getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                break;
            case RightUp:
                popupX = originX + anchorView.getWidth() - popupWidth + self.getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                popupY = originY - self.getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                break;
            case RightDown:
                popupX = originX + anchorView.getWidth() - popupWidth + self.getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                popupY = originY + anchorView.getHeight() - popupHeight + self.getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                break;
            case LeftDown:
                popupX = originX - self.getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                popupY = originY + anchorView.getHeight() - popupHeight + self.getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                break;
        }

        self.getPopupWindow().showAtLocation(anchorView, Gravity.START | Gravity.TOP, popupX, popupY);
    }

    /**
     * 消除popupWindow
     */
    public void dismissPopup() {
        if (!self.getPopupWindow().isShowing()) {
            return;
        }
        self.getPopupWindow().dismiss();
    }

    /**
     * 为popupWindow的背景容器设置背景色
     * 仅在arrow显示或{@link PopupBackgroundView#edgePadding}不为0时可见{@link #setEdgePadding(int, int, int, int)}
     * @param popupBackgroundColor 背景色
     * @return this
     */
    public PopupController setPopupBackgroundColor(int popupBackgroundColor) {
        self.getPopupBackgroundView().setPopupBackgroundColor(popupBackgroundColor);
        return this;
    }

    /**
     * 为popupWindow的背景容器设置边缘圆角半径
     * 仅在{@link PopupBackgroundView#edgePadding}不为0时可见{@link #setEdgePadding(int, int, int, int)}
     * @param edgeRoundedRadius 边缘圆角半径
     * @return this
     */
    public PopupController setEdgeRoundedRadius(int edgeRoundedRadius) {
        self.getPopupBackgroundView().setEdgeRoundedRadius(edgeRoundedRadius);
        return this;
    }

    /**
     * 为popupWindow的背景容器设置三角形箭头的高
     * 仅在arrow显示时可见
     * @param directionArrowHeight 三角形箭头的高
     * @return this
     */
    public PopupController setDirectionArrowHeight(int directionArrowHeight) {
        self.getPopupBackgroundView().setDirectionArrowHeight(directionArrowHeight);
        return this;
    }

    /**
     * 为popupWindow的背景容器设置阴影半径
     * @param popupShadowRadius 阴影半径
     * @return this
     */
    public PopupController setPopupShadowRadius(int popupShadowRadius) {
        self.getPopupBackgroundView().setPopupShadowRadius(popupShadowRadius);
        return this;
    }

    /**
     * 为popupWindow的背景容器设置边缘padding
     * @return this
     */
    public PopupController setEdgePadding(int left, int top, int right, int bottom) {
        self.getPopupBackgroundView().setEdgePadding(new Rect(left, top, right, bottom));
        return this;
    }
    
    
    /* Properties */
    private Context context;
    private PopupController setContext(Context context) {
        this.context = context;
        return this;
    }
    public Context getContext() {
        return context;
    }

    private View view;
    private PopupController setView(View view) {
        this.view = view;
        self.setContext(view.getContext());
        return this;
    }
    public View getView() {
        return view;
    }

    private PopupWindow popupWindow;
    private PopupWindow getPopupWindow() {
        if (popupWindow == null) {
            self.popupWindow = new PopupWindow(self.getPopupBackgroundView());
            self.popupWindow.setFocusable(true);
            self.popupWindow.setClippingEnabled(false);
            self.popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            self.popupWindow.setBackgroundDrawable(new ColorDrawable());

            self.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    self.getPopupBackgroundView().removeAllViews();

                    if (self.getDelegate() != null) {
                        self.getDelegate().onPopupWindowDismiss(self);
                    }
                }
            });
        }
        return popupWindow;
    }

    private PopupBackgroundView popupBackgroundView;
    private PopupBackgroundView getPopupBackgroundView() {
        if (popupBackgroundView == null) {
            popupBackgroundView = new PopupBackgroundView(self.getContext());
        }
        return popupBackgroundView;
    }

    public interface Delegate {
        void onPopupWindowDismiss(PopupController controller);
    }
    private Delegate delegate;
    private PopupController setDelegate(Delegate delegate) {
        this.delegate = delegate;
        return this;
    }
    public Delegate getDelegate() {
        if (delegate == null) {
            delegate = new Delegate() {
                @Override
                public void onPopupWindowDismiss(PopupController controller) {
                }
            };
        }
        return delegate;
    }

    
    /* Overrides */
     
     
    /* Delegates */
     
     
    /* Private Methods */
    
}