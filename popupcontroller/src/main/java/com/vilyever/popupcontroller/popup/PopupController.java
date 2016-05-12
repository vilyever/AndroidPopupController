package com.vilyever.popupcontroller.popup;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.vilyever.popupcontroller.ViewController;
import com.vilyever.resource.Resource;

/**
 * PopupController
 * AndroidPopupController <com.vilyever.popupcontroller>
 * Created by vilyever on 2016/2/24.
 * Feature:
 * popup视图控制器
 * popupWindow封装
 */
public class PopupController extends ViewController {
    final PopupController self = this;

    /* Constructors */
    public PopupController(Context context) {
        super(context);
    }

    public PopupController(Context context, int layout) {
        super(context, layout);
    }

    public PopupController(View view) {
        super(view);
    }
    
    /* Public Methods */
    public boolean isShowing() {
        return getPopupWindow().isShowing();
    }

    /** @see #popupFromView(View, PopupDirection, boolean, int, int)  */
    public <T extends PopupController> T popupFromView(@NonNull View anchorView, @NonNull PopupDirection popupDirection) {
        return popupFromView(anchorView, popupDirection, false, 0, 0);
    }

    /** @see #popupFromView(View, PopupDirection, boolean, int, int)  */
    public <T extends PopupController> T popupFromView(@NonNull View anchorView, @NonNull PopupDirection popupDirection, boolean withArrow) {
        return popupFromView(anchorView, popupDirection, withArrow, 0, 0);
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
        if (!isShowing()) {

            attachToParent(getPopupBackgroundView());

            if (withArrow) {
                getPopupBackgroundView().setPopupDirection(popupDirection);
            }
            else {
                getPopupBackgroundView().setPopupDirection(PopupDirection.Center);
            }

            getPopupWindow().setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            getPopupWindow().setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//            getPopupWindow().getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            getPopupWindow().getContentView().measure(View.MeasureSpec.makeMeasureSpec(Resource.getDisplayMetrics().widthPixels, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(Resource.getDisplayMetrics().heightPixels, View.MeasureSpec.AT_MOST));
            int popupWidth = getPopupWindow().getContentView().getMeasuredWidth();
            int popupHeight = getPopupWindow().getContentView().getMeasuredHeight();

            getPopupWindow().setWidth(popupWidth);
            getPopupWindow().setHeight(popupHeight);

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
                    popupX = originX - popupWidth + getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                    popupY = originY - (popupHeight / 2 - anchorView.getHeight() / 2) + offsetY;
                    break;
                case Up:
                    popupX = originX - (popupWidth / 2 - anchorView.getWidth() / 2) + offsetX;
                    popupY = originY - popupHeight + getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                    break;
                case Right:
                    popupX = originX + anchorView.getWidth() - getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                    popupY = originY - (popupHeight / 2 - anchorView.getHeight() / 2) + offsetY;
                    break;
                case Down:
                    popupX = originX - (popupWidth / 2 - anchorView.getWidth() / 2) + offsetX;
                    popupY = originY + anchorView.getHeight() - getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                    break;
                case LeftUp:
                    popupX = originX - popupWidth + getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                    popupY = originY - popupHeight + getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                    break;
                case RightUp:
                    popupX = originX + anchorView.getWidth() - getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                    popupY = originY - popupHeight + getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                    break;
                case RightDown:
                    popupX = originX + anchorView.getWidth() - getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                    popupY = originY + anchorView.getHeight() - getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                    break;
                case LeftDown:
                    popupX = originX - popupWidth + getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                    popupY = originY + anchorView.getHeight() - getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                    break;
            }

            getPopupWindow().showAtLocation(anchorView, Gravity.START | Gravity.TOP, popupX, popupY);
        }

        return (T) this;
    }

    /** @see #popupInView(View, PopupDirection, int, int)  */
    public <T extends PopupController> T popupInView(@NonNull View anchorView, @NonNull PopupDirection popupDirection) {
        return popupInView(anchorView, popupDirection, 0, 0);
    }

    /**
     * 在某个view内弹出popupWindow
     * @param anchorView 锚view
     * @param popupDirection popupWindow方向
     * @param offsetX X轴偏移量
     * @param offsetY Y轴偏移量
     */
    public <T extends PopupController> T popupInView(@NonNull View anchorView, @NonNull PopupDirection popupDirection, int offsetX, int offsetY) {
        if (!isShowing()) {

            attachToParent(getPopupBackgroundView());

            getPopupBackgroundView().setPopupDirection(PopupDirection.Center);

            getPopupWindow().setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            getPopupWindow().setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//            getPopupWindow().getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            getPopupWindow().getContentView().measure(View.MeasureSpec.makeMeasureSpec(Resource.getDisplayMetrics().widthPixels, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(Resource.getDisplayMetrics().heightPixels, View.MeasureSpec.AT_MOST));
            int popupWidth = getPopupWindow().getContentView().getMeasuredWidth();
            int popupHeight = getPopupWindow().getContentView().getMeasuredHeight();

            getPopupWindow().setWidth(popupWidth);
            getPopupWindow().setHeight(popupHeight);

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
                    popupX = originX - getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                    popupY = originY - (popupHeight / 2 - anchorView.getHeight() / 2) + offsetY;
                    break;
                case Up:
                    popupX = originX - (popupWidth / 2 - anchorView.getWidth() / 2) + offsetX;
                    popupY = originY - getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                    break;
                case Right:
                    popupX = originX + anchorView.getWidth() - popupWidth + getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                    popupY = originY - (popupHeight / 2 - anchorView.getHeight() / 2) + offsetY;
                    break;
                case Down:
                    popupX = originX - (popupWidth / 2 - anchorView.getWidth() / 2) + offsetX;
                    popupY = originY + anchorView.getHeight() - popupHeight + getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                    break;
                case LeftUp:
                    popupX = originX - getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                    popupY = originY - getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                    break;
                case RightUp:
                    popupX = originX + anchorView.getWidth() - popupWidth + getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                    popupY = originY - getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                    break;
                case RightDown:
                    popupX = originX + anchorView.getWidth() - popupWidth + getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                    popupY = originY + anchorView.getHeight() - popupHeight + getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                    break;
                case LeftDown:
                    popupX = originX - getPopupBackgroundView().getPopupShadowRadius() + offsetX;
                    popupY = originY + anchorView.getHeight() - popupHeight + getPopupBackgroundView().getPopupShadowRadius() + offsetY;
                    break;
            }

            getPopupWindow().showAtLocation(anchorView, Gravity.START | Gravity.TOP, popupX, popupY);
        }

        return (T) this;
    }

    /**
     * 消除popupWindow
     */
    public <T extends PopupController> T dismissPopup() {
        if (isShowing()) {
            getPopupWindow().close();
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
        getPopupBackgroundView().setPopupBackgroundColor(popupBackgroundColor);
        return (T) this;
    }

    /**
     * 为popupWindow的背景容器设置边缘圆角半径
     * 仅在{@link PopupBackgroundView#edgePadding}不为0时可见{@link #setEdgePadding(int, int, int, int)}
     * @param edgeRoundedRadius 边缘圆角半径
     * @return this
     */
    public <T extends PopupController> T setEdgeRoundedRadius(int edgeRoundedRadius) {
        getPopupBackgroundView().setEdgeRoundedRadius(edgeRoundedRadius);
        return (T) this;
    }

    /**
     * 为popupWindow的背景容器设置三角形箭头的高
     * 仅在arrow显示时可见
     * @param directionArrowHeight 三角形箭头的高
     * @return this
     */
    public <T extends PopupController> T setDirectionArrowHeight(int directionArrowHeight) {
        getPopupBackgroundView().setDirectionArrowHeight(directionArrowHeight);
        return (T) this;
    }

    /**
     * 为popupWindow的背景容器设置阴影半径
     * 与{@link #setPopupBackgroundColor(int)}的透明度相关，若color为Transparent，则shadow无法显示
     * @param popupShadowRadius 阴影半径
     * @return this
     */
    public <T extends PopupController> T setPopupShadowRadius(int popupShadowRadius) {
        getPopupBackgroundView().setPopupShadowRadius(popupShadowRadius);
        return (T) this;
    }

    /**
     * 为popupWindow的背景容器设置边缘padding
     * @return this
     */
    public <T extends PopupController> T setEdgePadding(int left, int top, int right, int bottom) {
        getPopupBackgroundView().setEdgePadding(new Rect(left, top, right, bottom));
        return (T) this;
    }
    
    
    /* Properties */
    private CustomPopupWindow popupWindow;
    protected CustomPopupWindow getPopupWindow() {
        if (this.popupWindow == null) {
            this.popupWindow = new CustomPopupWindow(getPopupBackgroundView());
            this.popupWindow.setFocusable(true);
            this.popupWindow.setClippingEnabled(false);
            this.popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            this.popupWindow.setBackgroundDrawable(new ColorDrawable());

            this.popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!isDismissOnTouchOutside()) {
                        final int x = (int) event.getX();
                        final int y = (int) event.getY();
                        if ((event.getAction() == MotionEvent.ACTION_DOWN)
                            && ((x < 0) || (x >= getPopupWindow().getContentView().getWidth()) || (y < 0) || (y >= getPopupWindow().getContentView().getHeight()))) {
                            return true;
                        }
                        else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                            return true;
                        }
                    }
                    return false;
                }
            });

            this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    onPopupDismiss();
                }
            });
        }
        return this.popupWindow;
    }

    private PopupBackgroundView popupBackgroundView;
    protected PopupBackgroundView getPopupBackgroundView() {
        if (this.popupBackgroundView == null) {
            this.popupBackgroundView = new PopupBackgroundView(getContext());
        }
        return this.popupBackgroundView;
    }

    public interface OnPopupDismissListener {
        void onPopupWindowDismiss(PopupController controller);
    }
    private OnPopupDismissListener onPopupDismissListener;
    public <T extends PopupController> T setOnPopupDismissListener(OnPopupDismissListener onPopupDismissListener) {
        this.onPopupDismissListener = onPopupDismissListener;
        return (T) this;
    }
    public OnPopupDismissListener getOnPopupDismissListener() {
        if (this.onPopupDismissListener == null) {
            this.onPopupDismissListener = new OnPopupDismissListener() {
                @Override
                public void onPopupWindowDismiss(PopupController controller) {
                }
            };
        }
        return this.onPopupDismissListener;
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
        return this.dismissOnTouchOutside;
    }

    /* Overrides */

    /* Delegates */

    /* Protected Methods */
    @CallSuper
    protected void onPopupDismiss() {
//        detachFromParent(false);
        getOnPopupDismissListener().onPopupWindowDismiss(this);
    }
     
    /* Private Methods */
    
}