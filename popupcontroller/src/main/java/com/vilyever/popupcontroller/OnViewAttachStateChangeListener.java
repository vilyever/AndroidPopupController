package com.vilyever.popupcontroller;

import android.view.ViewGroup;

/**
 * OnViewAttachStateChangeListener
 * AndroidPopupController <com.vilyever.popupcontroller>
 * Created by vilyever on 2016/3/2.
 * Feature:
 * {@link ViewController}根视图的parent变动监听
 */
public interface OnViewAttachStateChangeListener {
    /**
     * {@link ViewController#onViewWillAttachToParent(ViewGroup)}
     * @param controller controller
     * @param parent 将要添加到的parent
     */
    void onViewWillAttachToParent(ViewController controller, ViewGroup parent);

    /**
     * {@link ViewController#onViewAttachedToParent(ViewGroup)}
     * @param controller controller
     * @param parent 已经添加到的parent，即controller根视图当前的parent
     */
    void onViewAttachedToParent(ViewController controller, ViewGroup parent);

    /**
     * {@link ViewController#onViewWillDetachFromParent(ViewGroup)}
     * @param controller controller
     * @param parent controller根视图当前的parent
     */
    void onViewWillDetachFromParent(ViewController controller, ViewGroup parent);

    /**
     * {@link ViewController#onViewDetachedFromParent(ViewGroup)}
     * @param controller controller
     * @param parent 将controller根视图移除的parent
     */
    void onViewDetachedFromParent(ViewController controller, ViewGroup parent);
}
