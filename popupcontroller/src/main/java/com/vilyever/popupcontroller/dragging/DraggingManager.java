package com.vilyever.popupcontroller.dragging;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.vilyever.popupcontroller.R;
import com.vilyever.resource.Resource;

/**
 * DraggingManager
 * AndroidPopupController <com.vilyever.popupcontroller.dragging>
 * Created by vilyever on 2016/3/28.
 * Feature:
 * 无缝衔接拖动管理
 * 用于拖动已经在某个layout中的view
 * 通过复制copy来实现拖动效果
 */
public class DraggingManager {
    final DraggingManager self = this;

    
    /* Constructors */
    public DraggingManager(@NonNull View targetView) {
        if (!(targetView.getContext() instanceof Activity)) {
            throw new IllegalStateException("DraggingManager: target view is not in activity");
        }

        this.targetView = targetView;
        this.targetView.setOnTouchListener(getTargetViewOnTouchListener());
    }

    /* Public Methods */
    /**
     * 动画返回拖动前坐标
     * 动画结束后回收拖动容器和视图
     */
    public void animateBackToOriginalCoordinateBeforeEndDragging() {
        int[] targetLocation = new int[2];
        getTargetView().getLocationInWindow(targetLocation);

        int targetViewX = targetLocation[0];
        int targetViewY = targetLocation[1];

        getDraggingView().animate().x(targetViewX).y(targetViewY).start();
        getDraggingView().animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                self.endDragging();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                self.endDragging();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 结束拖动
     * 回收拖动容器和视图
     * 请确保每次{@link #onDraggingEnd(View)}都会调用此方法，可同步调用，或执行动画或某些后台操作后调用
     */
    public void endDragging() {
        self.getDraggingContainerController().detachFromParent();
        self.getDraggingContainerController().removeDraggingChild(self.getDraggingView());
    }

    
    /* Properties */
    /**
     * 原始目标view
     */
    private final View targetView;
    public View getTargetView() {
        return this.targetView;
    }

    /**
     * 配合{@link DraggingContainerController}的触摸监听实现无缝衔接拖动
     */
    private View.OnTouchListener targetViewOnTouchListener;
    protected View.OnTouchListener getTargetViewOnTouchListener() {
        if (this.targetViewOnTouchListener == null) {
            this.targetViewOnTouchListener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!self.isEnabled()) {
                        return false;
                    }

                    self.getGestureDetector().onTouchEvent(event);

                    if (self.isDragging()) {
                        self.getDraggingView().dispatchTouchEvent(event);
                    }

                    if (event.getAction() == MotionEvent.ACTION_UP
                            || event.getAction() == MotionEvent.ACTION_CANCEL) {
                        self.setDragging(false);
                        self.onDraggingEnd(self.getDraggingView());
                    }

                    return true;
                }
            };

        }
        return this.targetViewOnTouchListener;
    }

    /**
     * 拖动容器
     * 默认附着于Activity的decorView上
     */
    private DraggingContainerController draggingContainerController;
    public DraggingContainerController getDraggingContainerController() {
        if (this.draggingContainerController == null) {
            this.draggingContainerController = new DraggingContainerController(getTargetView().getContext());
            this.draggingContainerController.registerDraggingDelegate(new DraggingContainerController.DraggingDelegate() {
                @Override
                public void onChildBeginDragging(DraggingContainerController controller, View child) {
                }

                @Override
                public void onChildDragging(DraggingContainerController controller, View child, int x, int y) {
                    if (self.isDragging() && child == self.getDraggingView()) {
                        self.onDragging(self.getDraggingView(), x, y);
                    }
                }

                @Override
                public void onChildEndDragging(DraggingContainerController controller, View child) {
                }
            });
        }
        return this.draggingContainerController;
    }

    /**
     * 手势监测
     * 实现触摸即拖拽
     * 或长按后拖拽
     */
    private GestureDetector gestureDetector;
    protected GestureDetector getGestureDetector() {
        if (this.gestureDetector == null) {
            this.gestureDetector = new GestureDetector(getTargetView().getContext(), new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    if (self.shouldStartDraggingOnTouchDown()) {
                        self.startDragging();
                    }
                    return false;
                }

                @Override
                public void onShowPress(MotionEvent e) {

                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return false;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    if (!self.shouldStartDraggingOnTouchDown() && self.shouldStartDraggingOnLongPress()) {
                        self.startDragging();
                    }
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }
            });
        }
        return this.gestureDetector;
    }

    /**
     * 用于拖动的copy视图
     */
    private CopyView draggingView;
    protected final CopyView getDraggingView() {
        if (this.draggingView == null) {
            this.draggingView = new CopyView(getViewForCopy());
        }
        return this.draggingView;
    }

    /**
     * 是否正在拖动状态
     */
    private boolean dragging;
    protected DraggingManager setDragging(boolean dragging) {
        this.dragging = dragging;
        return this;
    }
    public boolean isDragging() {
        return this.dragging;
    }

    /**
     * 启用或禁用此Manager
     */
    private boolean enabled = true;
    public DraggingManager setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
    public boolean isEnabled() {
        return this.enabled;
    }

    /* Overrides */
     
     
    /* Delegates */


    /* Protected Methods */
    /**
     * 是否在touch时间开始时就进入拖动
     * 此优先级高于{@link #shouldStartDraggingOnLongPress()}
     * @return
     */
    protected boolean shouldStartDraggingOnTouchDown() {
        return false;
    }

    /**
     * 是否在长按后才进入拖动
     * @return
     */
    protected boolean shouldStartDraggingOnLongPress() {
        return true;
    }

    /**
     * copy哪个视图来显示拖动
     * 默认为{@link #targetView}
     * 考虑到可能{@link #targetView}有不必要的边框装饰等
     * 可返回其child来显示拖动
     * @return
     */
    protected View getViewForCopy() {
        return getTargetView();
    }

    /**
     * 拖动开始回调
     * 主要用于配置初始参数
     * 默认配置了透明度和x，y偏移
     * @param draggingView
     */
    protected void onDraggingBegin(View draggingView) {
        draggingView.setAlpha(0.8f);
        draggingView.setX(draggingView.getX() + Resource.getDimensionPixelSize(R.dimen.draggingManagerStartDraggingOffset));
        draggingView.setY(draggingView.getY() + Resource.getDimensionPixelSize(R.dimen.draggingManagerStartDraggingOffset));
    }

    /**
     * 拖动变化回调
     * @param draggingView
     * @param x
     * @param y
     */
    protected void onDragging(View draggingView, int x, int y) {

    }

    /**
     * 拖动结束回调
     * 请确保每次回调都调用{@link #endDragging()}来回收拖动容器，可同步调用，或执行动画或某些后台操作后调用
     * @param draggingView
     */
    protected void onDraggingEnd(View draggingView) {
        endDragging();
    }

    /* Private Methods */

    /**
     * 进入拖动模式
     */
    private void startDragging() {
        int[] targetLocation = new int[2];
        getTargetView().getLocationInWindow(targetLocation);

        int targetViewX = targetLocation[0];
        int targetViewY = targetLocation[1];

        setDragging(true);
        Activity activity = (Activity) getTargetView().getContext();
        getDraggingContainerController().attachToParent((ViewGroup) activity.getWindow().getDecorView());
        getDraggingContainerController().addDraggingChild(getDraggingView());

        getDraggingView().setX(targetViewX);
        getDraggingView().setY(targetViewY);

        onDraggingBegin(getDraggingView());
    }

    /* Inner Classes */
    public static class CopyView extends View {
        public CopyView(View originalView) {
            super(originalView.getContext());
            this.originalView = originalView;
        }

        private final View originalView;
        protected View getOriginalView() {
            return this.originalView;
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            if (getOriginalView() != null) {
                getOriginalView().draw(canvas);
            }
        }
    }

}