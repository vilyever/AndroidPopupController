package com.vilyever.popupcontroller;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
    public PopupController(Context context) {
        self.setContext(context);
    }

    public PopupController(Context context, int layout) {
        // For generate LayoutParams
        FrameLayout wrapperFrameLayout = new FrameLayout(context);
        wrapperFrameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        View.inflate(context, layout, wrapperFrameLayout);

        self.setView(wrapperFrameLayout.getChildAt(0));
    }

    public PopupController(View view) {
        self.setView(view);
    }
    
    /* Public Methods */
    /**
     * 添加当前controller的view到一个父view上
     * @param parent 父view
     */
    public <T extends PopupController> T attachToParent(ViewGroup parent) {
        if (self.getView().getParent() != parent) {
            self.detachFromParent();
            parent.addView(self.getView());
        }

        return (T) this;
    }
    
    /**
     * 将当前controller的view从父view移除
     */
    public <T extends PopupController> T detachFromParent() {
        if (self.getView().getParent() != null) {
            ((ViewGroup) self.getView().getParent()).removeView(self.getView());
        }
        return (T) this;
    }

    /** @see #popupFromView(View, PopupDirection, boolean, int, int)  */
    public <T extends PopupController> T popupFromView(@NonNull View anchorView, @NonNull PopupDirection popupDirection) {
        return self.popupFromView(anchorView, popupDirection, false, 0, 0);
    }

    /** @see #popupFromView(View, PopupDirection, boolean, int, int)  */
    public <T extends PopupController> T popupFromView(@NonNull View anchorView, @NonNull PopupDirection popupDirection, boolean withArrow) {
        return self.popupFromView(anchorView, popupDirection, withArrow, 0, 0);
    }
    
    /**
     * 从某个view上弹出popupWindow
     * @param anchorView 锚view
     * @param popupDirection popupWindow方向
     * @param withArrow 是否显示箭头，仅对左上右下四个正方向有效。{@link PopupDirection#Left}{@link PopupDirection#Up}{@link PopupDirection#Right}{@link PopupDirection#Down}
     * @param offsetX X轴偏移量
     * @param offsetY Y轴偏移量
     */
    public <T extends PopupController> T popupFromView(@NonNull View anchorView, @NonNull PopupDirection popupDirection, boolean withArrow, int offsetX, int offsetY) {
        if (!self.getPopupWindow().isShowing()) {

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

        return (T) this;
    }

    /** @see #popupInView(View, PopupDirection, int, int)  */
    public <T extends PopupController> T popupInView(@NonNull View anchorView, @NonNull PopupDirection popupDirection) {
        return self.popupInView(anchorView, popupDirection, 0, 0);
    }

    /**
     * 在某个view内弹出popupWindow
     * @param anchorView 锚view
     * @param popupDirection popupWindow方向
     * @param offsetX X轴偏移量
     * @param offsetY Y轴偏移量
     */
    public <T extends PopupController> T popupInView(@NonNull View anchorView, @NonNull PopupDirection popupDirection, int offsetX, int offsetY) {
        if (!self.getPopupWindow().isShowing()) {

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

        return (T) this;
    }

    /**
     * 消除popupWindow
     */
    public <T extends PopupController> T dismissPopup() {
        if (self.getPopupWindow().isShowing()) {
            self.getPopupWindow().dismiss();
        }

        return (T) this;
    }

    /**
     * 为popupWindow的背景容器设置背景色
     * 仅在arrow显示或{@link PopupBackgroundView#edgePadding}不为0时可见{@link #setEdgePadding(int, int, int, int)}
     * @param popupBackgroundColor 背景色
     * @return this
     */
    public <T extends PopupController> T setPopupBackgroundColor(int popupBackgroundColor) {
        self.getPopupBackgroundView().setPopupBackgroundColor(popupBackgroundColor);
        return (T) this;
    }

    /**
     * 为popupWindow的背景容器设置边缘圆角半径
     * 仅在{@link PopupBackgroundView#edgePadding}不为0时可见{@link #setEdgePadding(int, int, int, int)}
     * @param edgeRoundedRadius 边缘圆角半径
     * @return this
     */
    public <T extends PopupController> T setEdgeRoundedRadius(int edgeRoundedRadius) {
        self.getPopupBackgroundView().setEdgeRoundedRadius(edgeRoundedRadius);
        return (T) this;
    }

    /**
     * 为popupWindow的背景容器设置三角形箭头的高
     * 仅在arrow显示时可见
     * @param directionArrowHeight 三角形箭头的高
     * @return this
     */
    public <T extends PopupController> T setDirectionArrowHeight(int directionArrowHeight) {
        self.getPopupBackgroundView().setDirectionArrowHeight(directionArrowHeight);
        return (T) this;
    }

    /**
     * 为popupWindow的背景容器设置阴影半径
     * 与{@link #setPopupBackgroundColor(int)}的透明度相关，若color为Transparent，则shadow无法显示
     * @param popupShadowRadius 阴影半径
     * @return this
     */
    public <T extends PopupController> T setPopupShadowRadius(int popupShadowRadius) {
        self.getPopupBackgroundView().setPopupShadowRadius(popupShadowRadius);
        return (T) this;
    }

    /**
     * 为popupWindow的背景容器设置边缘padding
     * @return this
     */
    public <T extends PopupController> T setEdgePadding(int left, int top, int right, int bottom) {
        self.getPopupBackgroundView().setEdgePadding(new Rect(left, top, right, bottom));
        return (T) this;
    }
    
    
    /* Properties */
    private Context context;
    protected <T extends PopupController> T setContext(Context context) {
        this.context = context;
        return (T) this;
    }
    public Context getContext() {
        return context;
    }

    /**
     * controller的根视图
     * 注意：如果controller是由{@link #PopupController(Context, int)}生成的，此时的根视图view存在一个包裹它的FrameLayout
     * 这是由于如果没有一个view用于初始化layout，layout的根视图将无法生成LayoutParams
     * 如果要对LayoutParams，请注意其类型
     */
    private View view;
    protected <T extends PopupController> T setView(View view) {
        this.view = view;
        self.setContext(view.getContext());
        return (T) this;
    }
    public View getView() {
        return view;
    }

    private PopupWindow popupWindow;
    protected PopupWindow getPopupWindow() {
        if (popupWindow == null) {
            self.popupWindow = new PopupWindow(self.getPopupBackgroundView());
            self.popupWindow.setFocusable(true);
            self.popupWindow.setClippingEnabled(false);
            self.popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            self.popupWindow.setBackgroundDrawable(new ColorDrawable());

            self.popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!self.isDismissOnTouchOutside()) {
                        final int x = (int) event.getX();
                        final int y = (int) event.getY();
                        if ((event.getAction() == MotionEvent.ACTION_DOWN)
                            && ((x < 0) || (x >= self.getPopupWindow().getContentView().getWidth()) || (y < 0) || (y >= self.getPopupWindow().getContentView().getHeight()))) {
                            return true;
                        }
                        else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                            return true;
                        }
                    }
                    return false;
                }
            });

            self.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    self.onPopupDismiss();
                }
            });
        }
        return popupWindow;
    }

    private PopupBackgroundView popupBackgroundView;
    protected PopupBackgroundView getPopupBackgroundView() {
        if (popupBackgroundView == null) {
            popupBackgroundView = new PopupBackgroundView(self.getContext());
        }
        return popupBackgroundView;
    }

    public interface PopupDelegate {
        void onPopupWindowDismiss(PopupController controller);
    }
    private PopupDelegate popupDelegate;
    public <T extends PopupController> T setPopupDelegate(PopupDelegate popupDelegate) {
        this.popupDelegate = popupDelegate;
        return (T) this;
    }
    public PopupDelegate getPopupDelegate() {
        if (popupDelegate == null) {
            popupDelegate = new PopupDelegate() {
                @Override
                public void onPopupWindowDismiss(PopupController controller) {
                }
            };
        }
        return popupDelegate;
    }

    /**
     * 是否点击window外部时消除popupWindow
     */
    private boolean dismissOnTouchOutside = true;
    public <T extends PopupController> T setDismissOnTouchOutside(boolean dismissOnTouchOutside) {
        this.dismissOnTouchOutside = dismissOnTouchOutside;
        return (T) this;
    }
    public boolean isDismissOnTouchOutside() {
        return dismissOnTouchOutside;
    }

    /* Overrides */
     
     
    /* Delegates */

    /* Protected Methods */
    @CallSuper
    protected void onPopupDismiss() {
        self.getPopupBackgroundView().removeAllViews();
        self.getPopupDelegate().onPopupWindowDismiss(self);
    }
     
    /* Private Methods */
    
}