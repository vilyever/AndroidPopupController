package com.vilyever.popupcontroller.listener;

import android.view.ViewGroup;

import com.vilyever.popupcontroller.ViewController;

/**
 * OnViewStateChangeListener
 * AndroidPopupController <com.vilyever.popupcontroller>
 * Created by vilyever on 2016/3/2.
 * Feature:
 * {@link ViewController}根视图的parent变动监听
 */
public interface OnViewStateChangeListener {
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

    /**
     * {@link ViewController#onViewWillAppear()}
     * @param controller controller
     */
    void onViewWillAppear(ViewController controller);

    /**
     * {@link ViewController#onViewAppeared()}
     * @param controller controller
     */
    void onViewAppeared(ViewController controller);

    /**
     * {@link ViewController#onViewWillDisappear()} ()}
     * @param controller controller
     */
    void onViewWillDisappear(ViewController controller);

    /**
     * {@link ViewController#onViewDisappeared()}
     * @param controller controller
     */
    void onViewDisappeared(ViewController controller);
}
