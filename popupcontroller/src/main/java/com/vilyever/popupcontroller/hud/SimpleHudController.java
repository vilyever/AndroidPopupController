package com.vilyever.popupcontroller.hud;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vilyever.resource.Colour;
import com.vilyever.unitconversion.DimenConverter;

/**
 * HudController
 * AndroidPopupController <com.vilyever.popupcontroller.hud>+
 * Created by vilyever on 2016/4/15.
 * Feature:
 */
public class SimpleHudController extends HudController {
    final SimpleHudController self = this;


    /* Constructors */
    public SimpleHudController() {
        super();

        setView(getRootFrameLayout());
        internalInit();
    }

    
    /* Public Methods */
    public SimpleHudController showTitle(boolean show) {
        getTitleLabel().setVisibility(show ? View.VISIBLE : View.GONE);
        return this;
    }

    public SimpleHudController showCustom(boolean show) {
        getCustomViewLayout().setVisibility(show ? View.VISIBLE : View.GONE);
        return this;
    }

    public SimpleHudController showLeftButton(boolean show) {
        getLeftButton().setVisibility(show ? View.VISIBLE : View.GONE);
        internalUpdateLayout();
        return this;
    }

    public SimpleHudController showCenterButton(boolean show) {
        getCenterButton().setVisibility(show ? View.VISIBLE : View.GONE);
        internalUpdateLayout();
        return this;
    }

    public SimpleHudController showRightButton(boolean show) {
        getRightButton().setVisibility(show ? View.VISIBLE : View.GONE);
        internalUpdateLayout();
        return this;
    }

    public SimpleHudController setTitle(String title) {
        getTitleLabel().setText(title);
        showTitle(title != null);
        return this;
    }

    public SimpleHudController setLeftButtonTitle(String title) {
        getLeftButton().setText(title);
        showLeftButton(title != null);
        return this;
    }

    public SimpleHudController setCenterButtonTitle(String title) {
        getCenterButton().setText(title);
        showCenterButton(title != null);
        return this;
    }

    public SimpleHudController setRightButtonTitle(String title) {
        getRightButton().setText(title);
        showRightButton(title != null);
        return this;
    }

    public SimpleHudController dismissOnLeftButton() {
        getLeftButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.dismissPopup();
            }
        });
        return this;
    }

    public SimpleHudController dismissOnCenterButton() {
        getCenterButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.dismissPopup();
            }
        });
        return this;
    }

    public SimpleHudController dismissOnRightButton() {
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
    public SimpleHudController setCustomView(View customView) {
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


    /* Overrides */

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
        SimpleHudButton button = new SimpleHudButton(getContext());
        
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
}