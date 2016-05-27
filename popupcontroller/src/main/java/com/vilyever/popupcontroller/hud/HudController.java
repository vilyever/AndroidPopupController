package com.vilyever.popupcontroller.hud;

import android.app.Activity;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;

import com.vilyever.activityhelper.ActivityHelper;
import com.vilyever.contextholder.ContextHolder;
import com.vilyever.popupcontroller.popup.PopupController;
import com.vilyever.popupcontroller.popup.PopupDirection;

/**
 * HudController
 * AndroidPopupController <com.vilyever.popupcontroller.hud>+
 * Created by vilyever on 2016/4/15.
 * Feature:
 */
public abstract class HudController extends PopupController {
    final HudController self = this;


    /* Constructors */
    public HudController() {
        super(ContextHolder.getContext());
    }

    public HudController(int layout) {
        super(ContextHolder.getContext(), layout);
    }

    public HudController(View view) {
        super(view);
    }

    
    /* Public Methods */
    /**
     * 在当前状态为resumed的Activity显示hud
     * 调用此方法时，若app处于后台，则没有状态为resumed的Activity
     * （或其它没有状态为resumed的Activity时的情况下），
     * hud将在下一次任意Activity状态变为resumed时显示
     * @deprecated try to call {@link #show(View)} or {@link #show(Activity)} immediately
     */
    @Deprecated
    public <T extends HudController> T show() {
        Activity resumedActivity = ActivityHelper.findResumedActivity();
        if (resumedActivity != null) {
            show(resumedActivity);
        }
        else {
            /**
             * wait for activity resume
             */
            setPrepareToShow(true);
        }

        return (T) this;
    }

    /**
     * 在view所处的activity显示alert窗口
     */
    public <T extends HudController> T show(View view) {
        if (!(view.getContext() instanceof Activity)) {
            Log.w(this.getClass().getSimpleName(), "the view is not attach to an activity");
        }
        else {
            show((Activity) view.getContext());
        }

        return (T) this;
    }

    /**
     * 在activity显示alert窗口
     */
    public <T extends HudController> T show(final Activity activity) {
        if (ViewCompat.isAttachedToWindow(activity.getWindow().getDecorView())) {
            internalShow(activity.getWindow().getDecorView());
        }
        else {
            setPrepareToShow(true);
            activity.getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    if (self.isPrepareToShow()) {
                        self.setPrepareToShow(false);
                        if (ViewCompat.isAttachedToWindow(activity.getWindow().getDecorView())) {
                            self.internalShow(activity.getWindow().getDecorView());
                        }
                    }
                }
            });
        }

        return (T) this;
    }

    /* Properties */
    private boolean prepareToShow;
    protected HudController setPrepareToShow(boolean prepareToShow) {
        this.prepareToShow = prepareToShow;
        return this;
    }
    public boolean isPrepareToShow() {
        return this.prepareToShow;
    }
    

    /* Overrides */
    @Override
    public <T extends PopupController> T dismissPopup() {
        setPrepareToShow(false);
        return super.dismissPopup();
    }

    @Override
    protected void onActivityResumed(Activity activity) {
        super.onActivityResumed(activity);

        if (isPrepareToShow()) {
            setPrepareToShow(false);
            show(activity);
        }
    }

    /* Delegates */
    
    
    /* Private Methods */
    private void internalShow(View view) {
        popupInView(view, PopupDirection.Center);
    }
}