package com.vilyever.popupcontroller.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.vilyever.popupcontroller.R;
import com.vilyever.popupcontroller.ViewController;
import com.vilyever.popupcontroller.animation.AnimationDirection;
import com.vilyever.popupcontroller.animation.AnimationPerformer;
import com.vilyever.popupcontroller.animation.DialogEnterAnimationPerformer;
import com.vilyever.popupcontroller.animation.DialogExitAnimationPerformer;
import com.vilyever.resource.Colour;
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
     * @param withArrow 是否显示箭头，仅对左上右下四个正方向有效。{@link PopupDirection#Left}{@link PopupDirection#Top}{@link PopupDirection#Right}{@link PopupDirection#Bottom}
     * @param offsetX X轴偏移量
     * @param offsetY Y轴偏移量
     */
    public <T extends PopupController> T popupFromView(@NonNull View anchorView, @NonNull PopupDirection popupDirection, boolean withArrow, int offsetX, int offsetY) {
        return internalPopup(anchorView, popupDirection, withArrow, offsetX, offsetY, false);
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
        return internalPopup(anchorView, popupDirection, false, offsetX, offsetY, true);
    }

    /**
     * 消除popupWindow
     */
    public <T extends PopupController> T dismissPopup() {
        if (isShowing()) {
            getContentFrameLayout().removePopupView(getPopupTargetBackgroundView(), new PopupContentFrameLayout.RemoveDelegate() {
                @Override
                public void onRemoveAnimationEnd(PopupContentFrameLayout frameLayout) {
                    self.getPopupWindow().close();
                }
            });
        }

        return (T) this;
    }

    /**
     * 为popupWindow的背景容器设置背景色
     * 仅在arrow显示或{@link PopupTargetBackgroundView#edgePadding}不为0时可见{@link #setEdgePadding(int, int, int, int)}
     * @param popupBackgroundColor 背景色
     * @return this
     */
    public <T extends PopupController> T setPopupBackgroundColor(int popupBackgroundColor) {
        getPopupTargetBackgroundView().setPopupBackgroundColor(popupBackgroundColor);
        return (T) this;
    }

    /**
     * 为popupWindow的背景容器设置边缘圆角半径
     * 仅在{@link PopupTargetBackgroundView#edgePadding}不为0时可见{@link #setEdgePadding(int, int, int, int)}
     * @param edgeRoundedRadius 边缘圆角半径
     * @return this
     */
    public <T extends PopupController> T setEdgeRoundedRadius(int edgeRoundedRadius) {
        getPopupTargetBackgroundView().setEdgeRoundedRadius(edgeRoundedRadius);
        return (T) this;
    }

    /**
     * 为popupWindow的背景容器设置三角形箭头的高
     * 仅在arrow显示时可见
     * @param directionArrowHeight 三角形箭头的高
     * @return this
     */
    public <T extends PopupController> T setDirectionArrowHeight(int directionArrowHeight) {
        getPopupTargetBackgroundView().setDirectionArrowHeight(directionArrowHeight);
        return (T) this;
    }

    /**
     * 为popupWindow的背景容器设置阴影半径
     * 与{@link #setPopupBackgroundColor(int)}的透明度相关，若color为Transparent，则shadow无法显示
     * @param popupShadowRadius 阴影半径
     * @return this
     */
    public <T extends PopupController> T setPopupShadowRadius(int popupShadowRadius) {
        getPopupTargetBackgroundView().setPopupShadowRadius(popupShadowRadius);
        return (T) this;
    }

    /**
     * 为popupWindow的背景容器设置边缘padding
     * @return this
     */
    public <T extends PopupController> T setEdgePadding(int left, int top, int right, int bottom) {
        getPopupTargetBackgroundView().setEdgePadding(new Rect(left, top, right, bottom));
        return (T) this;
    }
    
    
    /* Properties */
    private CustomPopupWindow popupWindow;
    public CustomPopupWindow getPopupWindow() {
        if (this.popupWindow == null) {
            this.popupWindow = new CustomPopupWindow(getContentFrameLayout());
            this.popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            this.popupWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
            this.popupWindow.setFocusable(true);
            this.popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//            this.popupWindow.setClippingEnabled(false); // set false to enable show outside of the window(srceen), set true(default) enable input keyboard adjust resize.
            this.popupWindow.setAnimationStyle(R.style.AnimationPopupController);
            this.popupWindow.setBackgroundDrawable(new ColorDrawable());

            this.popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int x = (int) event.getRawX();
                    final int y = (int) event.getRawY();

                    int[] popupContentViewLocation = new int[2];
                    self.getPopupWindow().getContentView().getLocationOnScreen(popupContentViewLocation);
                    int popupContentViewX = popupContentViewLocation[0];
                    int popupContentViewY = popupContentViewLocation[1];

                    int[] showingViewLocation = new int[2];
                    self.getPopupTargetBackgroundView().getLocationOnScreen(showingViewLocation);
                    int showingViewX = showingViewLocation[0];
                    int showingViewY = showingViewLocation[1];

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if ((x < popupContentViewX) || (x >= popupContentViewX + self.getPopupWindow().getContentView().getWidth())
                            || (y < popupContentViewY) || (y >= popupContentViewY + self.getPopupWindow().getContentView().getHeight())) {
                            // touch outside of the fullscreen content layout, not possible in common
                            // FIXME: 2016/5/27 sth wrong when keyboard popup will enter here
                            if (self.isDismissOnTouchOutside()) {
//                                self.dismissPopup();
                            }
                            return true;
                        }
                        else {
                            if ((x < showingViewX) || (x >= showingViewX + self.getPopupTargetBackgroundView().getWidth())
                                || (y < showingViewY) || (y >= showingViewY + self.getPopupTargetBackgroundView().getHeight())) {
                                // touch outside of the showing content(include the backgroundView)
                                if (self.isDismissOnTouchOutside()) {
                                    self.dismissPopup();
                                }
                            }
                        }
                    }
                    else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        if (self.isDismissOnTouchOutside()) {
                            self.dismissPopup();
                        }
                        return true;
                    }

                    return false;
                }
            });

            this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    internalOnPopupDismiss();
                }
            });
        }
        return this.popupWindow;
    }

    private PopupContentFrameLayout contentFrameLayout;
    protected PopupContentFrameLayout getContentFrameLayout() {
        if (this.contentFrameLayout == null) {
            this.contentFrameLayout = new PopupContentFrameLayout(getContext());
            this.contentFrameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        return this.contentFrameLayout;
    }

    private PopupTargetBackgroundView popupTargetBackgroundView;
    public PopupTargetBackgroundView getPopupTargetBackgroundView() {
        if (this.popupTargetBackgroundView == null) {
            this.popupTargetBackgroundView = new PopupTargetBackgroundView(getContext());
            this.popupTargetBackgroundView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return this.popupTargetBackgroundView;
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

    private boolean dismissOnBackPressed = true;
    public PopupController setDismissOnBackPressed(boolean dismissOnBackPressed) {
        this.dismissOnBackPressed = dismissOnBackPressed;
        getPopupWindow().setDismissDisabled(!this.dismissOnBackPressed);
        return this;
    }
    public boolean isDismissOnBackPressed() {
        return this.dismissOnBackPressed;
    }
    
    private boolean dimBackground = true;
    public PopupController setDimBackground(boolean dimBackground) {
        this.dimBackground = dimBackground;
        return this;
    }
    public boolean isDimBackground() {
        return this.dimBackground;
    }

    /* Overrides */

    /* Delegates */

    /* Protected Methods */
    /**
     * 从某个view上弹出popupWindow
     * @param anchorView 锚view
     * @param popupDirection popupWindow方向
     * @param withArrow 是否显示箭头，仅对左上右下四个正方向有效。{@link PopupDirection#Left}{@link PopupDirection#Top}{@link PopupDirection#Right}{@link PopupDirection#Bottom}
     * @param offsetX X轴偏移量
     * @param offsetY Y轴偏移量
     */
    @CallSuper
    protected  <T extends PopupController> T internalPopup(@NonNull View anchorView, @NonNull PopupDirection popupDirection, boolean withArrow, int offsetX, int offsetY, boolean isInView) {
        if (!isShowing()) {

            attachToParent(getPopupTargetBackgroundView());

            if (withArrow) {
                getPopupTargetBackgroundView().setPopupDirection(popupDirection);
            }
            else {
                getPopupTargetBackgroundView().setPopupDirection(PopupDirection.Center);
            }

            getPopupTargetBackgroundView().measure(View.MeasureSpec.makeMeasureSpec(Resource.getDisplayMetrics().widthPixels, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(Resource.getDisplayMetrics().heightPixels, View.MeasureSpec.AT_MOST));
            int contentWidth = getPopupTargetBackgroundView().getMeasuredWidth();
            int contentHeight = getPopupTargetBackgroundView().getMeasuredHeight();

            int[] location = new int[2];
            anchorView.getLocationOnScreen(location);
            int originX = location[0];
            int originY = location[1];

            int windowX = 0, windowY = 0;

            AnimationPerformer showPopupAnimationPerformer = getContentFrameLayout().getShowPopupAnimationPerformer();
            AnimationPerformer dismissPopupAnimationPerformer = getContentFrameLayout().getDismissPopupAnimationPerformer();
            if (showPopupAnimationPerformer == null) {
                showPopupAnimationPerformer = new DialogEnterAnimationPerformer();
            }
            if (dismissPopupAnimationPerformer == null) {
                dismissPopupAnimationPerformer = new DialogExitAnimationPerformer();
            }

            switch (popupDirection) {
                case Center:
                    if (isInView) {
                        windowX = originX + (anchorView.getWidth() / 2 - contentWidth / 2) + offsetX;
                        windowY = originY + (anchorView.getHeight() / 2 - contentHeight / 2) + offsetY;
                    }
                    else {
                        windowX = originX - (contentWidth / 2 - anchorView.getWidth() / 2) + offsetX;
                        windowY = originY - (contentHeight / 2 - anchorView.getHeight() / 2) + offsetY;
                    }
                    showPopupAnimationPerformer.setAnimationDirection(AnimationDirection.Center);
                    dismissPopupAnimationPerformer.setAnimationDirection(AnimationDirection.Center);
                    break;
                case Left:
                    if (isInView) {
                        windowX = originX - getPopupTargetBackgroundView().getPopupShadowRadius() + offsetX;
                        windowY = originY - (contentHeight / 2 - anchorView.getHeight() / 2) + offsetY;
                    }
                    else {
                        windowX = originX - contentWidth + getPopupTargetBackgroundView().getPopupShadowRadius() + offsetX;
                        windowY = originY - (contentHeight / 2 - anchorView.getHeight() / 2) + offsetY;
                    }
                    showPopupAnimationPerformer.setAnimationDirection(AnimationDirection.Left);
                    dismissPopupAnimationPerformer.setAnimationDirection(AnimationDirection.Left);
                    break;
                case Top:
                    if (isInView) {
                        windowX = originX - (contentWidth / 2 - anchorView.getWidth() / 2) + offsetX;
                        windowY = originY - getPopupTargetBackgroundView().getPopupShadowRadius() + offsetY;
                    }
                    else {
                        windowX = originX - (contentWidth / 2 - anchorView.getWidth() / 2) + offsetX;
                        windowY = originY - contentHeight + getPopupTargetBackgroundView().getPopupShadowRadius() + offsetY;
                    }
                    showPopupAnimationPerformer.setAnimationDirection(AnimationDirection.Top);
                    dismissPopupAnimationPerformer.setAnimationDirection(AnimationDirection.Top);
                    break;
                case Right:
                    if (isInView) {
                        windowX = originX + anchorView.getWidth() - contentWidth + getPopupTargetBackgroundView().getPopupShadowRadius() + offsetX;
                        windowY = originY - (contentHeight / 2 - anchorView.getHeight() / 2) + offsetY;
                    }
                    else {
                        windowX = originX + anchorView.getWidth() - getPopupTargetBackgroundView().getPopupShadowRadius() + offsetX;
                        windowY = originY - (contentHeight / 2 - anchorView.getHeight() / 2) + offsetY;
                    }
                    showPopupAnimationPerformer.setAnimationDirection(AnimationDirection.Right);
                    dismissPopupAnimationPerformer.setAnimationDirection(AnimationDirection.Right);
                    break;
                case Bottom:
                    if (isInView) {
                        windowX = originX - (contentWidth / 2 - anchorView.getWidth() / 2) + offsetX;
                        windowY = originY + anchorView.getHeight() - contentHeight + getPopupTargetBackgroundView().getPopupShadowRadius() + offsetY;
                    }
                    else {
                        windowX = originX - (contentWidth / 2 - anchorView.getWidth() / 2) + offsetX;
                        windowY = originY + anchorView.getHeight() - getPopupTargetBackgroundView().getPopupShadowRadius() + offsetY;
                    }
                    showPopupAnimationPerformer.setAnimationDirection(AnimationDirection.Bottom);
                    dismissPopupAnimationPerformer.setAnimationDirection(AnimationDirection.Bottom);
                    break;
                case LeftTop:
                    if (isInView) {
                        windowX = originX - getPopupTargetBackgroundView().getPopupShadowRadius() + offsetX;
                        windowY = originY - getPopupTargetBackgroundView().getPopupShadowRadius() + offsetY;
                    }
                    else {
                        windowX = originX - contentWidth + getPopupTargetBackgroundView().getPopupShadowRadius() + offsetX;
                        windowY = originY - contentHeight + getPopupTargetBackgroundView().getPopupShadowRadius() + offsetY;
                    }
                    showPopupAnimationPerformer.setAnimationDirection(AnimationDirection.LeftTop);
                    dismissPopupAnimationPerformer.setAnimationDirection(AnimationDirection.LeftTop);
                    break;
                case RightTop:
                    if (isInView) {
                        windowX = originX + anchorView.getWidth() - contentWidth + getPopupTargetBackgroundView().getPopupShadowRadius() + offsetX;
                        windowY = originY - getPopupTargetBackgroundView().getPopupShadowRadius() + offsetY;
                    }
                    else {
                        windowX = originX + anchorView.getWidth() - getPopupTargetBackgroundView().getPopupShadowRadius() + offsetX;
                        windowY = originY - contentHeight + getPopupTargetBackgroundView().getPopupShadowRadius() + offsetY;
                    }
                    showPopupAnimationPerformer.setAnimationDirection(AnimationDirection.RightTop);
                    dismissPopupAnimationPerformer.setAnimationDirection(AnimationDirection.RightTop);
                    break;
                case RightBottom:
                    if (isInView) {
                        windowX = originX + anchorView.getWidth() - contentWidth + getPopupTargetBackgroundView().getPopupShadowRadius() + offsetX;
                        windowY = originY + anchorView.getHeight() - contentHeight + getPopupTargetBackgroundView().getPopupShadowRadius() + offsetY;
                    }
                    else {
                        windowX = originX + anchorView.getWidth() - getPopupTargetBackgroundView().getPopupShadowRadius() + offsetX;
                        windowY = originY + anchorView.getHeight() - getPopupTargetBackgroundView().getPopupShadowRadius() + offsetY;
                    }
                    showPopupAnimationPerformer.setAnimationDirection(AnimationDirection.RightBottom);
                    dismissPopupAnimationPerformer.setAnimationDirection(AnimationDirection.RightBottom);
                    break;
                case LeftBottom:
                    if (isInView) {
                        windowX = originX - getPopupTargetBackgroundView().getPopupShadowRadius() + offsetX;
                        windowY = originY + anchorView.getHeight() - contentHeight + getPopupTargetBackgroundView().getPopupShadowRadius() + offsetY;
                    }
                    else {
                        windowX = originX - contentWidth + getPopupTargetBackgroundView().getPopupShadowRadius() + offsetX;
                        windowY = originY + anchorView.getHeight() - getPopupTargetBackgroundView().getPopupShadowRadius() + offsetY;
                    }
                    showPopupAnimationPerformer.setAnimationDirection(AnimationDirection.LeftBottom);
                    dismissPopupAnimationPerformer.setAnimationDirection(AnimationDirection.LeftBottom);
                    break;
            }

            getContentFrameLayout().setShowPopupAnimationPerformer(showPopupAnimationPerformer);
            getContentFrameLayout().setDismissPopupAnimationPerformer(dismissPopupAnimationPerformer);

            if (getPopupTargetBackgroundView().getParent() != null) {
                ((ViewGroup) getPopupTargetBackgroundView().getParent()).removeView(getPopupTargetBackgroundView());
            }
            getContentFrameLayout().addPopupView(getPopupTargetBackgroundView());

            getPopupTargetBackgroundView().setWindowX(windowX);
            getPopupTargetBackgroundView().setWindowY(windowY);

            internalShowPopupWindow(anchorView);
        }

        return (T) this;
    }

    @CallSuper
    protected void internalShowPopupWindow(View anchorView) {
        if (isDimBackground()) {
            getContentFrameLayout().setBackgroundColor(Colour.changeAlpha(Color.BLACK, 130));
        }
        getPopupWindow().showAtLocation(anchorView, Gravity.START | Gravity.TOP, 0, 0);
    }

    @CallSuper
    protected void internalOnPopupDismiss() {
//        detachFromParent(false);
        getOnPopupDismissListener().onPopupWindowDismiss(this);
    }
     
    /* Private Methods */

}