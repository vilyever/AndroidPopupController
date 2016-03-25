package com.vilyever.popupcontroller.dragging;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * DraggingChildOptions
 * AndroidPopupController <com.vilyever.popupcontroller.dragging>
 * Created by vilyever on 2016/3/25.
 * Feature:
 */
public class DraggingChildOptions {
    final DraggingChildOptions self = this;

    /**
     * 边缘粘附
     * 可任意设置多边组合
    */
    public static final int EdgeLeft = 1;
    public static final int EdgeTop = 1 << 1;
    public static final int EdgeRight = 1 << 2;
    public static final int EdgeBottom = 1 << 3;
    public static final int EdgeAll = EdgeLeft | EdgeTop | EdgeRight | EdgeBottom;
    @IntDef({
                    EdgeLeft,
                    EdgeTop,
                    EdgeRight,
                    EdgeBottom,
                    EdgeAll,
            })
    @Retention(RetentionPolicy.SOURCE)
    public @interface AttachEdge {}
    
    /* Constructors */
    
    
    /* Public Methods */
    
    
    /* Properties */
    /**
     * 是否限定在容器内部拖动
     * 即child不能超出容器
     */
    private boolean draggingInsideBounds;
    public DraggingChildOptions setDraggingInsideBounds(boolean draggingInsideBounds) {
        this.draggingInsideBounds = draggingInsideBounds;
        return this; 
    }
    public boolean shouldDraggingInsideBounds() {
        return this.draggingInsideBounds;
    }
    
//    private boolean draggingAvoidCrossOtherChild;
//    public DraggingChildOptions setDraggingAvoidCrossOtherChild(boolean draggingAvoidCrossOtherChild) {
//        this.draggingAvoidCrossOtherChild = draggingAvoidCrossOtherChild;
//        return this;
//    }
//    public boolean shouldDraggingAvoidCrossOtherChild() {
//        return this.draggingAvoidCrossOtherChild;
//    }

    /**
     * 是否自动粘附最近的边缘
     */
    private boolean autoAttachNearestEdge;
    public DraggingChildOptions setAutoAttachNearestEdge(boolean autoAttachNearestEdge) {
        this.autoAttachNearestEdge = autoAttachNearestEdge;
        return this; 
    }
    public boolean shouldAutoAttachNearestEdge() {
        return this.autoAttachNearestEdge;
    }

    /**
     * 粘附边缘时是否显示动画
     */
    private boolean autoAttachNearestEdgeAnimated;
    public DraggingChildOptions setAutoAttachNearestEdgeAnimated(boolean autoAttachNearestEdgeAnimated) {
        this.autoAttachNearestEdgeAnimated = autoAttachNearestEdgeAnimated;
        return this; 
    }
    public boolean shouldAutoAttachNearestEdgeAnimated() {
        return this.autoAttachNearestEdgeAnimated;
    }

    /**
     * 可粘附的边缘
     * 若某个edge没有被设置，即使它离child最近也不粘附它
     * 默认全边缘
     */
    private @AttachEdge int autoAttachNearestEdgesSide = EdgeAll;
    public DraggingChildOptions setAutoAttachNearestEdgesSide(@AttachEdge int... autoAttachNearestEdgesSide) {
        if (autoAttachNearestEdgesSide.length > 0) {
            this.autoAttachNearestEdgesSide = autoAttachNearestEdgesSide[0];
            for (int attachEdge : autoAttachNearestEdgesSide) {
                this.autoAttachNearestEdgesSide |= attachEdge;
            }
        }
        else {
            this.autoAttachNearestEdgesSide = EdgeAll;
        }
        return this; 
    }
    public @AttachEdge int getAutoAttachNearestEdgesSide() {
        return this.autoAttachNearestEdgesSide;
    }

    /**
     * 是否在每次拖动完都返回预设坐标
     */
    private boolean autoReturnOriginalWindowCoordinate;
    public DraggingChildOptions setAutoReturnOriginalWindowCoordinate(boolean autoReturnOriginalWindowCoordinate) {
        this.autoReturnOriginalWindowCoordinate = autoReturnOriginalWindowCoordinate;
        return this;
    }
    public boolean shouldAutoReturnOriginalWindowCoordinate() {
        return this.autoReturnOriginalWindowCoordinate;
    }

    /**
     * 预设坐标
     */
    private DraggingCoordinate originalWindowCoordinate;
    public DraggingChildOptions setOriginalWindowCoordinate(DraggingCoordinate originalWindowCoordinate) {
        this.originalWindowCoordinate = originalWindowCoordinate;
        return this;
    }
    public DraggingCoordinate getOriginalWindowCoordinate() {
        return this.originalWindowCoordinate;
    }
    
    /* Overrides */
     
     
    /* Delegates */
     
     
    /* Private Methods */


}