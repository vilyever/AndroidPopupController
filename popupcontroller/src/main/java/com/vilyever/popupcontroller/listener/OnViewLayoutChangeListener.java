package com.vilyever.popupcontroller.listener;

import android.graphics.Rect;

import com.vilyever.popupcontroller.ViewController;

/**
 * OnViewLayoutChangeListener
 * AndroidPopupController <com.vilyever.popupcontroller.listener>
 * Created by vilyever on 2016/3/2.
 * Feature:
 * {@link ViewController}根视图的Layout改变监听
 */
public interface OnViewLayoutChangeListener {
    /**
     * {@link ViewController#onViewLayoutChange(Rect, Rect)}
     * @param controller controller
     * @param frame 当前frame
     * @param oldFrame 变动前frame
     */
    void onViewLayoutChange(ViewController controller, Rect frame, Rect oldFrame);
}
