package com.vilyever.popupcontroller;

import android.graphics.Rect;

/**
 * OnViewLayoutChangeListener
 * AndroidPopupController <com.vilyever.popupcontroller>
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
