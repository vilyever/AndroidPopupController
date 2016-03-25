package com.vilyever.popupcontroller.dragging;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.vilyever.popupcontroller.ViewController;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * DraggingContainerController
 * AndroidPopupController <com.vilyever.popupcontroller.dragging>
 * Created by vilyever on 2016/3/25.
 * Feature:
 * 拖动视图容器
 * 此容器内所有视图都可以拖动
 */
public class DraggingContainerController extends ViewController {
    final DraggingContainerController self = this;


    /* Constructors */
    public DraggingContainerController(Context context) {
        super(context);

        setView(getRootFrameLayout());
        init();
    }

    private void init() {
        getRootFrameLayout().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return getDraggingGestureDetector().onTouchEvent(event);
            }
        });

        getRootFrameLayout().setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {

            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
                if (parent == self.getRootFrameLayout()) {
                    getChildOptionsHashMap().remove(child);
                }
            }
        });
    }
    
    /* Public Methods */
    public DraggingContainerController addDraggingChild(View child) {
        return addDraggingChild(child, new DraggingChildOptions());
    }

    /**
     * 向此容器添加child
     * @param child
     * @param options 作用于child的拖动选项
     * @return
     */
    public DraggingContainerController addDraggingChild(View child, DraggingChildOptions options) {

        if (child.getParent() != getRootFrameLayout()) {


            if (child.getParent() != null) {
                if (options.getOriginalWindowCoordinate() == null) {
                    int[] location = new int[2];
                    child.getLocationInWindow(location);
                    options.setOriginalWindowCoordinate(new DraggingCoordinate(location[0], location[1]));
                }

                ViewGroup parent = (ViewGroup) child.getParent();
                parent.removeView(child);
            }

            getRootFrameLayout().addView(child);
            child.setRight(child.getRight() - child.getLeft());
            child.setLeft(0);
            child.setBottom(child.getBottom() - child.getTop());
            child.setTop(0);

            getChildOptionsHashMap().put(child, options);

            internalCalibrateChildToOriginalWindowCoordinate(child, false);
        }

        return this;
    }

    /**
     * 从当前容器移除child
     * @param child
     * @return
     */
    public DraggingContainerController removeDraggingChild(View child) {
        getChildOptionsHashMap().remove(child);
        getRootFrameLayout().removeView(child);

        return this;
    }

    /**
     * 注册child拖动监听
     * @param delegate
     * @return
     */
    public DraggingContainerController registerDraggingDelegate(DraggingDelegate delegate) {
        if (!getDraggingDelegates().contains(delegate)) {
            getDraggingDelegates().add(delegate);
        }
        return this;
    }

    /**
     * 移除child拖动监听
     * @param delegate
     * @return
     */
    public DraggingContainerController removeDraggingDelegate(DraggingDelegate delegate) {
        getDraggingDelegates().remove(delegate);
        return this;
    }
    
    
    /* Properties */
    /**
     * root视图
     */
    private FrameLayout rootFrameLayout;
    private FrameLayout getRootFrameLayout() {
        if (rootFrameLayout == null) {
            rootFrameLayout = new FrameLayout(getContext());
            rootFrameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        return rootFrameLayout;
    }

    /**
     * 手势检测
     */
    private DraggingGestureDetector draggingGestureDetector;
    private DraggingGestureDetector getDraggingGestureDetector() {
        if (this.draggingGestureDetector == null) {
            this.draggingGestureDetector = new DraggingGestureDetector(getContext(), new DraggingGestureListener());
        }
        return this.draggingGestureDetector;
    }


    private class DraggingGestureDetector extends GestureDetector {
        private final DraggingGestureListener draggingGestureListener;

        public DraggingGestureDetector(Context context, DraggingGestureListener listener) {
            super(context, listener);
            this.draggingGestureListener = listener;
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            if (!super.onTouchEvent(ev)) {
                return this.draggingGestureListener.onTouchEvent(ev);
            }
            return true;
        }
    }

    private class DraggingGestureListener implements GestureDetector.OnGestureListener {
        View handlingView;

        @Override
        public boolean onDown(MotionEvent e) {
            this.handlingView = null;
            for (int i = self.getRootFrameLayout().getChildCount() - 1; i >= 0; i--) {
                View child = self.getRootFrameLayout().getChildAt(i);
                if (self.checkMotionEventLocateInView(child, e)) {
                    this.handlingView = child;
                    this.handlingView.animate().cancel();
                    break;
                }
            }

            return this.handlingView != null;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if (this.handlingView != null) {

                DraggingCoordinate coordinate = self.internalMoveChild(this.handlingView, -distanceX, -distanceY);
                self.onChildDragging(this.handlingView, coordinate.x, coordinate.y);

                return true;
            }

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        public boolean onTouchEvent(MotionEvent e) {
            if (e.getAction() == MotionEvent.ACTION_UP
                || e.getAction() == MotionEvent.ACTION_CANCEL) {

                self.internalOnChildDraggingRelease(this.handlingView);

                this.handlingView = null;
                return true;
            }

            return false;
        }
    }

    /**
     * 存储每个child的拖动选项
     */
    private HashMap<View, DraggingChildOptions> childOptionsHashMap;
    protected HashMap<View, DraggingChildOptions> getChildOptionsHashMap() {
        if (this.childOptionsHashMap == null) {
            this.childOptionsHashMap = new HashMap<View, DraggingChildOptions>();
        }
        return this.childOptionsHashMap;
    }

    /**
     * 拖动回调
     */
    private ArrayList<DraggingDelegate> draggingDelegates;
    protected ArrayList<DraggingDelegate> getDraggingDelegates() {
        if (this.draggingDelegates == null) {
            this.draggingDelegates = new ArrayList<DraggingDelegate>();
        }
        return this.draggingDelegates;
    }
    public interface DraggingDelegate {
        void onChildDragging(DraggingContainerController controller, View child, int x, int y);

        class SimpleDelegate implements DraggingDelegate {
            @Override
            public void onChildDragging(DraggingContainerController controller, View child, int x, int y) {
        
            }
        }
    }

    /* Overrides */
     
     
    /* Delegates */


    /* Protected Methods */

    /**
     * 移动child
     * @param child
     * @param distanceX x轴距离，左负右正
     * @param distanceY y轴距离，上负下正
     * @return
     */
    @NonNull
    protected DraggingCoordinate internalMoveChild(View child, float distanceX, float distanceY) {
        child.animate().cancel();

        int x = (int) Math.floor(child.getX() + distanceX);
        int y = (int) Math.floor(child.getY() + distanceY);

        if (getChildOptionsHashMap().get(child).shouldDraggingInsideBounds()) {
            x = Math.max(0, x);
            x = Math.min(getRootFrameLayout().getWidth() - child.getWidth(), x);
            y = Math.max(0, y);
            y = Math.min(getRootFrameLayout().getHeight() - child.getHeight(), y);
        }

        child.setX(x);
        child.setY(y);

        return new DraggingCoordinate(x, y);
    }

    /**
     * 移动child，显示移动动画
     * @param child
     * @param distanceX x轴距离，左负右正
     * @param distanceY y轴距离，上负下正
     */
    protected void internalMoveChildAnimated(View child, float distanceX, float distanceY) {
        child.animate().cancel();

        int x = (int) Math.floor(child.getX() + distanceX);
        int y = (int) Math.floor(child.getY() + distanceY);

        if (getChildOptionsHashMap().get(child).shouldDraggingInsideBounds()) {
            x = Math.max(0, x);
            x = Math.min(getRootFrameLayout().getWidth() - child.getWidth(), x);
            y = Math.max(0, y);
            y = Math.min(getRootFrameLayout().getHeight() - child.getHeight(), y);
        }

        child.animate().x(x).y(y).start();
    }

    /**
     * 处理拖动手指放开时的事件
     * 是否返回原点
     * 是否粘附边缘
     * @param child
     */
    protected void internalOnChildDraggingRelease(View child) {
        if (getChildOptionsHashMap().get(child).shouldAutoReturnOriginalWindowCoordinate()) {
            internalCalibrateChildToOriginalWindowCoordinate(child, true);
        }
        else if (getChildOptionsHashMap().get(child).shouldAutoAttachNearestEdge()) {
            float distanceX = 0;
            float distanceY = 0;

            int edges = getChildOptionsHashMap().get(child).getAutoAttachNearestEdgesSide();
            float left = (edges & DraggingChildOptions.EdgeLeft) == DraggingChildOptions.EdgeLeft  ? child.getX() : Float.MAX_VALUE;
            float top = (edges & DraggingChildOptions.EdgeTop) == DraggingChildOptions.EdgeTop  ? child.getY() : Float.MAX_VALUE;
            float right = (edges & DraggingChildOptions.EdgeRight) == DraggingChildOptions.EdgeRight  ? (getRootFrameLayout().getWidth() - (child.getX() + child.getWidth())) : Float.MAX_VALUE;
            float bottom = (edges & DraggingChildOptions.EdgeBottom) == DraggingChildOptions.EdgeBottom  ? (getRootFrameLayout().getHeight() - (child.getY() + child.getHeight())) : Float.MAX_VALUE;

            if (left <= top && left <= right && left <= bottom) {
                distanceX = -left;
            }
            else if (top <= left && top <= right && top <= bottom) {
                distanceY = -top;
            }
            else if (right <= left && right <= top && right <= bottom) {
                distanceX = right;
            }
            else if (bottom <= left && bottom <= top && bottom <= right) {
                distanceY = bottom;
            }

            if (!getChildOptionsHashMap().get(child).shouldAutoAttachNearestEdgeAnimated()) {
                internalMoveChild(child, distanceX, distanceY);
            }
            else {
                internalMoveChildAnimated(child, distanceX, distanceY);
            }
        }
    }

    /**
     * 将child返回加入此容器前在window上的位置
     * @param child
     * @param animated 是否显示动画
     */
    protected void internalCalibrateChildToOriginalWindowCoordinate(View child, boolean animated) {
        child.animate().cancel();

        DraggingCoordinate coordinate = getChildOptionsHashMap().get(child).getOriginalWindowCoordinate();

        if (coordinate == null) {
            return;
        }

        int[] rootLocation = new int[2];
        getRootFrameLayout().getLocationInWindow(rootLocation);

        int x = coordinate.x - rootLocation[0];
        int y = coordinate.y - rootLocation[1];

        if (!animated) {
            child.setX(x);
            child.setY(y);
        }
        else {
            child.animate().x(x).y(y).start();
        }
    }

    @CallSuper
    /**
     * 拖动完成后的回调
     */
    protected void onChildDragging(View child, int x, int y) {


        ArrayList<DraggingDelegate> copyList =
                (ArrayList<DraggingDelegate>)getOnViewAttachStateChangeListeners().clone();
        int size = copyList.size();
        for (int i = 0; i < size; ++i) {
            copyList.get(i).onChildDragging(this, child, x, y);
        }
    }
     
     
    /* Private Methods */

    /**
     * 检测touch event的坐标是否在某个view内
     * @param view
     * @param event
     * @return
     */
    private boolean checkMotionEventLocateInView(View view, MotionEvent event)
    {
        if (view.getVisibility() != View.VISIBLE) {
            return false;
        }

        int[] location = new int[2];
        view.getLocationInWindow(location);

        boolean isLocateInView = false;

        if (event.getRawX() >= location[0]
            && event.getRawX() <= location[0] + view.getWidth()
            && event.getRawY() >= location[1]
            && event.getRawY() <= location[1] + view.getHeight() )
        {
            isLocateInView = true;
        }

        return isLocateInView;
    }

}