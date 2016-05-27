package com.vilyever.popupcontroller.hud;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vilyever.activityhelper.ActivityHelper;
import com.vilyever.contextholder.ContextHolder;
import com.vilyever.popupcontroller.popup.PopupController;
import com.vilyever.popupcontroller.popup.PopupDirection;
import com.vilyever.resource.Colour;
import com.vilyever.unitconversion.DimenConverter;

/**
 * HudController
 * AndroidPopupController <com.vilyever.popupcontroller.hud>+
 * Created by vilyever on 2016/4/15.
 * Feature:
 */
public class HudController extends PopupController {
    final HudController self = this;


    /* Constructors */
    public HudController() {
        super(ContextHolder.getContext());

        setView(getRootFrameLayout());
        internalInit();
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
        internalUpdateLayout();

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

    public HudController showTitle(boolean show) {
        getTitleLabel().setVisibility(show ? View.VISIBLE : View.GONE);
        return this;
    }

    public HudController showCustom(boolean show) {
        getCustomViewLayout().setVisibility(show ? View.VISIBLE : View.GONE);
        return this;
    }

    public HudController showLeftButton(boolean show) {
        getLeftButton().setVisibility(show ? View.VISIBLE : View.GONE);
        internalUpdateLayout();
        return this;
    }

    public HudController showCenterButton(boolean show) {
        getCenterButton().setVisibility(show ? View.VISIBLE : View.GONE);
        internalUpdateLayout();
        return this;
    }

    public HudController showRightButton(boolean show) {
        getRightButton().setVisibility(show ? View.VISIBLE : View.GONE);
        internalUpdateLayout();
        return this;
    }

    public HudController setTitle(String title) {
        getTitleLabel().setText(title);
        showTitle(title != null);
        return this;
    }

    public HudController setLeftButtonTitle(String title) {
        getLeftButton().setText(title);
        showLeftButton(title != null);
        return this;
    }

    public HudController setCenterButtonTitle(String title) {
        getCenterButton().setText(title);
        showCenterButton(title != null);
        return this;
    }

    public HudController setRightButtonTitle(String title) {
        getRightButton().setText(title);
        showRightButton(title != null);
        return this;
    }

    public HudController dismissOnLeftButton() {
        getLeftButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.dismissPopup();
            }
        });
        return this;
    }

    public HudController dismissOnCenterButton() {
        getCenterButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.dismissPopup();
            }
        });
        return this;
    }

    public HudController dismissOnRightButton() {
        getRightButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.dismissPopup();
            }
        });
        return this;
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
            this.contentLinearLayout.setMinimumWidth(DimenConverter.dpToPixel(300));
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
            this.titleLabel.setVisibility(View.GONE);
        }
        return this.titleLabel;
    }

    private FrameLayout customViewLayout;
    public FrameLayout getCustomViewLayout() {
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
    public HudController setCustomView(View customView) {
        if (this.customView != customView) {
            if (this.customView != null) {
                getCustomViewLayout().removeView(this.customView);
            }
            this.customView = customView;
            if (this.customView != null) {
                getCustomViewLayout().addView(this.customView);
                getCustomViewLayout().setVisibility(View.VISIBLE);
            }
            else {
                getCustomViewLayout().setVisibility(View.GONE);
            }
        }
        return this;
    }
    public View getCustomView() {
        return this.customView;
    }

    /**
     * 分割标题信息与button的分割线
     */
    private View verticalSplitView;
    public View getVerticalSplitView() {
        if (this.verticalSplitView == null) {
            this.verticalSplitView = internalGenerateSplitView();
            this.verticalSplitView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DimenConverter.dpToPixel(1)));
        }
        return this.verticalSplitView;
    }

    private LinearLayout buttonLayout;
    public LinearLayout getButtonLayout() {
        if (this.buttonLayout == null) {
            this.buttonLayout = new LinearLayout(getContext());
            this.buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DimenConverter.dpToPixel(44)));
            this.buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        }
        return this.buttonLayout;
    }

    private View leftButtonSplitView;
    public View getLeftButtonSplitView() {
        if (this.leftButtonSplitView == null) {
            this.leftButtonSplitView = internalGenerateSplitView();
            this.leftButtonSplitView.setLayoutParams(new LinearLayout.LayoutParams(DimenConverter.dpToPixel(1), ViewGroup.LayoutParams.MATCH_PARENT));
        }
        return this.leftButtonSplitView;
    }

    private View rightButtonSplitView;
    public View getRightButtonSplitView() {
        if (this.rightButtonSplitView == null) {
            this.rightButtonSplitView = internalGenerateSplitView();
            this.rightButtonSplitView.setLayoutParams(new LinearLayout.LayoutParams(DimenConverter.dpToPixel(1), ViewGroup.LayoutParams.MATCH_PARENT));

        }
        return this.rightButtonSplitView;
    }

    private Button leftButton;
    public Button getLeftButton() {
        if (this.leftButton == null) {
            this.leftButton = internalGenerateButton();
            this.leftButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));
            ((LinearLayout.LayoutParams) this.leftButton.getLayoutParams()).weight = 1;
        }
        return this.leftButton;
    }

    private Button centerButton;
    public Button getCenterButton() {
        if (this.centerButton == null) {
            this.centerButton = internalGenerateButton();
            this.centerButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));
            ((LinearLayout.LayoutParams) this.centerButton.getLayoutParams()).weight = 1;
        }
        return this.centerButton;
    }

    private Button rightButton;
    public Button getRightButton() {
        if (this.rightButton == null) {
            this.rightButton = internalGenerateButton();
            this.rightButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));
            ((LinearLayout.LayoutParams) this.rightButton.getLayoutParams()).weight = 1;
        }
        return this.rightButton;
    }
    
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
    private void internalInit() {
        getContentLinearLayout().addView(getTitleLabel());
        getContentLinearLayout().addView(getCustomViewLayout());
        getContentLinearLayout().addView(getVerticalSplitView());

        getButtonLayout().addView(getLeftButton());
        getButtonLayout().addView(getLeftButtonSplitView());
        getButtonLayout().addView(getCenterButton());
        getButtonLayout().addView(getRightButtonSplitView());
        getButtonLayout().addView(getRightButton());

        getContentLinearLayout().addView(getButtonLayout());

        getRootFrameLayout().addView(getContentLinearLayout());

        setPopupBackgroundColor(Color.parseColor("#88000000"));
        setEdgeRoundedRadius(DimenConverter.dpToPixel(8));
        setDismissOnTouchOutside(false);
        setDismissOnBackPressed(false);
    }

    private View internalGenerateSplitView() {
        View view = new View(getContext());
        
        view.setBackgroundColor(Color.parseColor("#DBDBDF"));
        view.setVisibility(View.GONE);
        
        return view;
    }

    private Button internalGenerateButton() {
        HudButton button = new HudButton(getContext());
        
        button.setGravity(Gravity.CENTER);
        button.setLines(1);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        button.setNormalTextColor(Color.parseColor("#007AFF"));
        button.setHighlightTextColor(Colour.LightGray);
        button.setBackground(null);
        button.setVisibility(View.GONE);
        
        return button;
    }

    private void internalUpdateLayout() {
        boolean isLeftButtonShow = getLeftButton().getVisibility() == View.VISIBLE;
        boolean isCenterButtonShow = getCenterButton().getVisibility() == View.VISIBLE;
        boolean isRightButtonShow = getRightButton().getVisibility() == View.VISIBLE;

        if (!isLeftButtonShow && !isCenterButtonShow && !isRightButtonShow) {
            getVerticalSplitView().setVisibility(View.GONE);
            getButtonLayout().setVisibility(View.GONE);
        }
        else {
            getVerticalSplitView().setVisibility(View.VISIBLE);
            getButtonLayout().setVisibility(View.VISIBLE);
        }

        getLeftButtonSplitView().setVisibility(View.GONE);
        getRightButtonSplitView().setVisibility(View.GONE);
        if (isLeftButtonShow && isCenterButtonShow && isRightButtonShow) {
            getLeftButtonSplitView().setVisibility(View.VISIBLE);
            getRightButtonSplitView().setVisibility(View.VISIBLE);
        }
        else if ((isLeftButtonShow && isCenterButtonShow)
                || (isLeftButtonShow && isRightButtonShow)) {
            getLeftButtonSplitView().setVisibility(View.VISIBLE);
        }
        else if ((isCenterButtonShow && isRightButtonShow)) {
            getRightButtonSplitView().setVisibility(View.VISIBLE);
        }

    }

    private void internalShow(View view) {
        popupInView(view, PopupDirection.Center);
    }
}