package com.vilyever.popupcontroller.dragging;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
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
        getRootFrameLayout().setMotionEventSplittingEnabled(false);
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
    /** {@link #addDraggingChild(View, DraggingChildOptions)} */
    public DraggingContainerController addDraggingChild(View child) {
        return addDraggingChild(child, new DraggingChildOptions());
    }

    /**
     * 向此容器添加child
     *
     * child的OnChildTouchListener将会被替换
     * 若child原先持有OnChildTouchListener
     * 可在{@link DraggingChildOptions#setOriginalOnTouchListener(View.OnTouchListener)}设置
     *
     * 当child处于拖动状态时，所有其他接收touch event的事件都将被取消
     *
     * @param child
     * @param options 作用于child的拖动选项
     * @return
     */
    public DraggingContainerController addDraggingChild(View child, @NonNull DraggingChildOptions options) {

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

//            if (options.getOriginalOnTouchListener() == null) {
//                try {
//                    Field listenerInfoField = View.class.getDeclaredField("mListenerInfo");
//                    listenerInfoField.setAccessible(true);
//                    Object listenerInfo = listenerInfoField.get(child);
//
//                    if (listenerInfo != null) {
//                        Field onTouchListenerField = Class.forName("android.view.View$ListenerInfo").getDeclaredField("mOnTouchListener");
//                        onTouchListenerField.setAccessible(true);
//
//                        View.OnTouchListener onTouchListener = (View.OnTouchListener) onTouchListenerField.get(listenerInfo);
//                        options.setOriginalOnTouchListener(onTouchListener);
//                    }
//                }
//                catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//                catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//                catch (NoSuchFieldException e) {
//                    e.printStackTrace();
//                }
//            }

            getChildOptionsHashMap().put(child, options);

            internalCalibrateChildToOriginalWindowCoordinate(child, false);

            child.setOnTouchListener(getOnChildTouchListener());
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

    /**
     * 获取对应child的拖动选项
     * 可直接修改
     * @param child
     * @return
     */
    public DraggingChildOptions getChildOptions(View child) {
        if (getChildOptionsHashMap().containsKey(child)) {
            return getChildOptionsHashMap().get(child);
        }

        return null;
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
     * 每个child的OnChildTouchListener
     * 若child原先持有OnChildTouchListener将会被替换
     */
    private View.OnTouchListener onChildTouchListener;
    protected View.OnTouchListener getOnChildTouchListener() { 
        if (this.onChildTouchListener == null) {
            this.onChildTouchListener = new View.OnTouchListener() {
                boolean dragging = false;

                boolean initialTouchSet = false;
                int initialTouchX;
                int initialTouchY;

                int lastTouchX;
                int lastTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent e) {
                    if (!this.dragging) {
                        boolean handled = false;
                        View.OnTouchListener originalOnTouchListener = self.getChildOptions(v).getOriginalOnTouchListener();
                        if (originalOnTouchListener != null) {
                            handled = originalOnTouchListener.onTouch(v, e);
                        }

                        if (!handled) {
                            v.onTouchEvent(e);
                        }
                    }

                    final int action = MotionEventCompat.getActionMasked(e);

                    if (!this.initialTouchSet) {
                        this.initialTouchX = this.lastTouchX = (int) (e.getRawX() + 0.5f);
                        this.initialTouchY = this.lastTouchY = (int) (e.getRawY() + 0.5f);
                        this.initialTouchSet = true;
                    }

                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            break;
                        case MotionEvent.ACTION_MOVE: {
                            final int x = (int) (e.getRawX() + 0.5f);
                            final int y = (int) (e.getRawY() + 0.5f);

                            if (!this.dragging) {
                                final int dx = x - this.initialTouchX;
                                final int dy = y - this.initialTouchY;

                                int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

                                if (Math.abs(dx) > touchSlop) {
                                    this.lastTouchX = this.initialTouchX + touchSlop * (dx < 0 ? -1 : 1);
                                    this.dragging = true;
                                }
                                else if (Math.abs(dy) > touchSlop) {
                                    this.lastTouchY = this.initialTouchY + touchSlop * (dy < 0 ? -1 : 1);
                                    this.dragging = true;
                                }

                                if (this.dragging) {
                                    final int oldAction = e.getAction();
                                    e.setAction(MotionEvent.ACTION_CANCEL);
                                    v.onTouchEvent(e);
                                    e.setAction(oldAction);

                                    self.onChildBeginDragging(v);
                                }
                            }
                            else {
                                int dx = x - this.lastTouchX;
                                int dy = y - this.lastTouchY;

                                this.lastTouchX = x;
                                this.lastTouchY = y;

                                DraggingCoordinate coordinate = self.internalMoveChild(v, dx, dy);
                                self.onChildDragging(v, coordinate.x, coordinate.y);
                            }
                        }
                        break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL: {
                            if (this.dragging) {
                                self.internalOnChildDraggingRelease(v);
                                self.onChildEndDragging(v);

                                this.initialTouchSet = false;
                                this.dragging = false;
                            }
                        }
                    }

                    return true;
                }
            };
        }
        return this.onChildTouchListener; 
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
        void onChildBeginDragging(DraggingContainerController controller, View child);
        void onChildDragging(DraggingContainerController controller, View child, int x, int y);
        void onChildEndDragging(DraggingContainerController controller, View child);

        class SimpleDelegate implements DraggingDelegate {
            @Override
            public void onChildBeginDragging(DraggingContainerController controller, View child) {

            }

            @Override
            public void onChildDragging(DraggingContainerController controller, View child, int x, int y) {
        
            }

            @Override
            public void onChildEndDragging(DraggingContainerController controller, View child) {

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

        if (getChildOptions(child).shouldDraggingInsideBounds()) {
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

        if (getChildOptions(child).shouldDraggingInsideBounds()) {
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
        if (getChildOptions(child).shouldAutoReturnOriginalWindowCoordinate()) {
            internalCalibrateChildToOriginalWindowCoordinate(child, true);
        }
        else if (getChildOptions(child).shouldAutoAttachNearestEdge()) {
            float distanceX = 0;
            float distanceY = 0;

            int edges = getChildOptions(child).getAutoAttachNearestEdgesSide();
            float left = DraggingChildOptions.checkAttachEdge(edges, DraggingChildOptions.EdgeLeft) ? child.getX() : Float.MAX_VALUE;
            float top = DraggingChildOptions.checkAttachEdge(edges, DraggingChildOptions.EdgeTop)  ? child.getY() : Float.MAX_VALUE;
            float right = DraggingChildOptions.checkAttachEdge(edges, DraggingChildOptions.EdgeRight)  ? (getRootFrameLayout().getWidth() - (child.getX() + child.getWidth())) : Float.MAX_VALUE;
            float bottom = DraggingChildOptions.checkAttachEdge(edges, DraggingChildOptions.EdgeBottom)  ? (getRootFrameLayout().getHeight() - (child.getY() + child.getHeight())) : Float.MAX_VALUE;

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

            if (!getChildOptions(child).shouldAutoAttachNearestEdgeAnimated()) {
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

        DraggingCoordinate coordinate = getChildOptions(child).getOriginalWindowCoordinate();

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
    protected void onChildBeginDragging(View child) {

        ArrayList<DraggingDelegate> copyList =
                (ArrayList<DraggingDelegate>)getDraggingDelegates().clone();
        int size = copyList.size();
        for (int i = 0; i < size; ++i) {
            copyList.get(i).onChildBeginDragging(this, child);
        }
    }

    @CallSuper
    /**
     * 拖动完成后的回调
     */
    protected void onChildDragging(View child, int x, int y) {

        ArrayList<DraggingDelegate> copyList =
                (ArrayList<DraggingDelegate>)getDraggingDelegates().clone();
        int size = copyList.size();
        for (int i = 0; i < size; ++i) {
            copyList.get(i).onChildDragging(this, child, x, y);
        }
    }

    @CallSuper
    /**
     * 拖动完成后的回调
     */
    protected void onChildEndDragging(View child) {

        ArrayList<DraggingDelegate> copyList =
                (ArrayList<DraggingDelegate>)getDraggingDelegates().clone();
        int size = copyList.size();
        for (int i = 0; i < size; ++i) {
            copyList.get(i).onChildEndDragging(this, child);
        }
    }

     
    /* Private Methods */

//    /**
//     * 检测touch event的坐标是否在某个view内
//     * @param view
//     * @param event
//     * @return
//     */
//    private boolean checkMotionEventLocateInView(View view, MotionEvent event)
//    {
//        if (view.getVisibility() != View.VISIBLE) {
//            return false;
//        }
//
//        int[] location = new int[2];
//        view.getLocationInWindow(location);
//
//        boolean isLocateInView = false;
//
//        if (event.getRawX() >= location[0]
//            && event.getRawX() <= location[0] + view.getWidth()
//            && event.getRawY() >= location[1]
//            && event.getRawY() <= location[1] + view.getHeight() )
//        {
//            isLocateInView = true;
//        }
//
//        return isLocateInView;
//    }

}