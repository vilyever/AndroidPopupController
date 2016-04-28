package com.vilyever.popupcontroller.hud;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vilyever.activityhelper.ActivityHelper;
import com.vilyever.activityhelper.ActivityStateDelegate;
import com.vilyever.contextholder.ContextHolder;
import com.vilyever.popupcontroller.popup.PopupController;
import com.vilyever.popupcontroller.popup.PopupDirection;
import com.vilyever.unitconversion.DimenConverter;

/**
 * HudController
 * AndroidPopupController <com.vilyever.popupcontroller.hud>
 * Created by vilyever on 2016/4/15.
 * Feature:
 */
public class HudController extends PopupController {
    final HudController self = this;


    /* Constructors */
    public HudController() {
        super(ContextHolder.getContext());

        setView(getRootFrameLayout());
        init();
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
            ActivityHelper.registerActivityStateDelegate(new ActivityStateDelegate.SimpleActivityStateDelegate() {
                @Override
                public void onActivityResumed(Activity activity) {
                    super.onActivityResumed(activity);

                    self.show(activity);

                    ActivityHelper.removeActivityStateDelegate(this);
                }
            });
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
    public <T extends HudController> T show(Activity activity) {
        popupInView(activity.getWindow().getDecorView(), PopupDirection.Center);

        return (T) this;
    }
    
    /* Properties */
    /**
     * root视图，套上一层FrameLayout以便child能确定LayoutParams的类型
     */
    private FrameLayout rootFrameLayout;
    private FrameLayout getRootFrameLayout() {
        if (this.rootFrameLayout == null) {
            this.rootFrameLayout = new FrameLayout(getContext());
            this.rootFrameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return this.rootFrameLayout;
    }

    /**
     * 包含alert所有内容的视图
     */
    private LinearLayout contentLinearLayout;
    public LinearLayout getContentLinearLayout() {
        if (this.contentLinearLayout == null) {
            this.contentLinearLayout = new LinearLayout(getContext());
            this.contentLinearLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            this.contentLinearLayout.setMinimumWidth(DimenConverter.dpToPixel(100));
            this.contentLinearLayout.setOrientation(LinearLayout.VERTICAL);
        }
        return this.contentLinearLayout;
    }

    /**
     * 标题栏
     */
    private TextView titleLabel;
    public TextView getTitleLabel() {
        if (this.titleLabel == null) {
            this.titleLabel = new TextView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            int margin = DimenConverter.dpToPixel(8);
            layoutParams.setMargins(margin, margin, margin, margin);
            this.titleLabel.setLayoutParams(layoutParams);
            this.titleLabel.setGravity(Gravity.CENTER);
            this.titleLabel.setLines(1);
            this.titleLabel.setTypeface(Typeface.DEFAULT_BOLD);
            this.titleLabel.setTextSize(20);
            this.titleLabel.setTextColor(Color.WHITE);
        }
        return this.titleLabel;
    }

    private FrameLayout customViewLayout;
    protected FrameLayout getCustomViewLayout() {
        if (this.customViewLayout == null) {
            this.customViewLayout = new FrameLayout(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            int margin = DimenConverter.dpToPixel(8);
            layoutParams.setMargins(margin, 0, margin, margin);
            this.customViewLayout.setLayoutParams(layoutParams);
        }
        return this.customViewLayout;
    }

    private View customView;
    protected HudController setCustomView(View customView) {
        this.customView = customView;
        return this;
    }
    public View getCustomView() {
        return this.customView;
    }

    /**
     * 分割标题信息与button的分割线
     */
    private View splitView;
    public View getSplitView() {
        if (this.splitView == null) {
            this.splitView = new View(getContext());
            this.splitView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DimenConverter.dpToPixel(1)));
            this.splitView.setBackgroundColor(Color.parseColor("#DBDBDF"));
        }
        return this.splitView;
    }

    private RecyclerView buttonRecyclerView;
    protected RecyclerView getButtonRecyclerView() {
        if (this.buttonRecyclerView == null) {
            this.buttonRecyclerView = new RecyclerView(getContext());

        }
        return this.buttonRecyclerView;
    }


    /* Overrides */
    
    
    /* Delegates */
    
    
    /* Private Methods */
    private void init() {
        getContentLinearLayout().addView(getTitleLabel());
        getContentLinearLayout().addView(getCustomViewLayout());
        getContentLinearLayout().addView(getSplitView());
//        getContentLinearLayout().addView(getButtonRecyclerView());

        getRootFrameLayout().addView(getContentLinearLayout());

        setDismissOnTouchOutside(false);
        setPopupBackgroundColor(Color.parseColor("#88000000"));
        setEdgeRoundedRadius(DimenConverter.dpToPixel(8));
    }
}