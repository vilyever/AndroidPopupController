package com.vilyever.popupcontroller;

/**
 * OnViewAppearStateChangeListener
 * AndroidPopupController <com.vilyever.popupcontroller>
 * Created by vilyever on 2016/3/2.
 * Feature:
 * {@link ViewController}根视图的在window上显示的状态监听
 */
public interface OnViewAppearStateChangeListener {
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
