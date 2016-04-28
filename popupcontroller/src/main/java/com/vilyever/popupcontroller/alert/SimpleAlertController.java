package com.vilyever.popupcontroller.alert;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.method.ScrollingMovementMethod;
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
import com.vilyever.activityhelper.ActivityStateDelegate;
import com.vilyever.contextholder.ContextHolder;
import com.vilyever.popupcontroller.R;
import com.vilyever.popupcontroller.popup.PopupController;
import com.vilyever.popupcontroller.popup.PopupDirection;
import com.vilyever.popupcontroller.util.RippleFrameLayout;
import com.vilyever.resource.Resource;

/**
 * SimpleAlertController
 * AndroidPopupController <com.vilyever.popupcontroller>
 * Created by vilyever on 2016/2/24.
 * Feature:
 * 提示弹窗
 * 注意：此弹窗不和Activity关联，即弹窗期间切换activity仍然存在
 */
public class SimpleAlertController extends PopupController {
    final SimpleAlertController self = this;


    /* Constructors */
    public SimpleAlertController() {
        super(ContextHolder.getContext());

        setView(getRootFrameLayout());
        init();
    }


    /* Public Methods */
    /**
     * 在当前状态为resumed的Activity显示alert窗口
     * 调用此方法时，若app处于后台，则没有状态为resumed的Activity
     * （或其它没有状态为resumed的Activity时的情况下），
     * alert窗口将在下一次任意Activity状态变为resumed时显示
     * @deprecated try to call {@link #show(View)} or {@link #show(Activity)} immediately
     */
    @Deprecated
    public <T extends SimpleAlertController> T show() {
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
    public <T extends SimpleAlertController> T show(View view) {
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
    public <T extends SimpleAlertController> T show(Activity activity) {
        if (getNegativeButtonTitle() == null && getPositiveButtonTitle() == null) {
            setPositiveButtonTitle(Resource.getString(R.string.defaultAlertPositiveButtonTitle));
        }

        if (getTitle() == null && getMessage() == null) {
            setTitle("");
        }

        if (getNegativeButtonTitle() == null || getPositiveButtonTitle() == null) {
            getSplitButtonView().setVisibility(View.GONE);
        }
        else {
            getSplitButtonView().setVisibility(View.VISIBLE);
        }

        popupInView(activity.getWindow().getDecorView(), PopupDirection.Center);

        return (T) this;
    }


    /* Properties */
    /**
     * root视图，套上一层FrameLayout以便{@link #alertLinearLayout}能确定LayoutParams的类型
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
    private LinearLayout alertLinearLayout;
    public LinearLayout getAlertLinearLayout() {
        if (this.alertLinearLayout == null) {
            this.alertLinearLayout = new LinearLayout(getContext());
            this.alertLinearLayout.setLayoutParams(new FrameLayout.LayoutParams(Resource.getDimensionPixelSize(R.dimen.simpleAlertWidth), ViewGroup.LayoutParams.WRAP_CONTENT));
            this.alertLinearLayout.setOrientation(LinearLayout.VERTICAL);
        }
        return this.alertLinearLayout;
    }

    /**
     * 标题栏
     */
    private TextView titleLabel;
    public TextView getTitleLabel() {
        if (this.titleLabel == null) {
            this.titleLabel = new TextView(getContext());
            this.titleLabel.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Resource.getDimensionPixelSize(R.dimen.simpleAlertTitleHeight)));
            this.titleLabel.setGravity(Gravity.CENTER);
            this.titleLabel.setLines(1);
            this.titleLabel.setTypeface(Typeface.DEFAULT_BOLD);
            this.titleLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, Resource.getDimensionPixelSize(R.dimen.simpleAlertTitleSize));
            this.titleLabel.setTextColor(Resource.getColor(R.color.simpleAlertTitle));
            this.titleLabel.setVisibility(View.GONE);
        }
        return this.titleLabel;
    }

    /**
     * 信息栏
     */
    private TextView messageLabel;
    public TextView getMessageLabel() {
        if (this.messageLabel == null) {
            this.messageLabel = new TextView(getContext());
            this.messageLabel.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int margin = Resource.getDimensionPixelSize(R.dimen.simpleAlertMargin);
            ((LinearLayout.LayoutParams) this.messageLabel.getLayoutParams()).setMargins(margin, margin, margin, margin);
            this.messageLabel.setMaxHeight(Resource.getDimensionPixelSize(R.dimen.simpleAlertMessageMaxHeight));
            this.messageLabel.setGravity(Gravity.CENTER);
            this.messageLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, Resource.getDimensionPixelSize(R.dimen.simpleAlertMessageSize));
            this.messageLabel.setTextColor(Resource.getColor(R.color.simpleAlertMessage));
            this.messageLabel.setVisibility(View.GONE);
        }
        return this.messageLabel;
    }

    /**
     * 分割标题信息与button的分割线
     */
    private View splitVerticalView;
    public View getSplitVerticalView() {
        if (this.splitVerticalView == null) {
            this.splitVerticalView = new View(getContext());
            this.splitVerticalView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Resource.getDimensionPixelSize(R.dimen.simpleAlertSplitLine)));
            this.splitVerticalView.setBackgroundColor(Resource.getColor(R.color.simpleAlertSplitLine));
        }
        return this.splitVerticalView;
    }

    private LinearLayout buttonLayout;
    public LinearLayout getButtonLayout() {
        if (this.buttonLayout == null) {
            this.buttonLayout = new LinearLayout(getContext());
            this.buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Resource.getDimensionPixelSize(R.dimen.simpleAlertButtonHeight)));
            this.buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        }
        return this.buttonLayout;
    }

    /**
     * 按钮分割线
     */
    private View splitButtonView;
    public View getSplitButtonView() {
        if (this.splitButtonView == null) {
            this.splitButtonView = new View(getContext());
            this.splitButtonView.setLayoutParams(new LinearLayout.LayoutParams(Resource.getDimensionPixelSize(R.dimen.simpleAlertSplitLine), ViewGroup.LayoutParams.MATCH_PARENT));
            this.splitButtonView.setBackgroundColor(Resource.getColor(R.color.simpleAlertSplitLine));
        }
        return this.splitButtonView;
    }

    /**
     * 左侧取消button
     */
    private Button negativeButton;
    public Button getNegativeButton() {
        if (this.negativeButton == null) {
            this.negativeButton = new Button(getContext());
            this.negativeButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));
            ((LinearLayout.LayoutParams) this.negativeButton.getLayoutParams()).weight = 1;
            this.negativeButton.setGravity(Gravity.CENTER);
            this.negativeButton.setLines(1);
            this.negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, Resource.getDimensionPixelSize(R.dimen.simpleAlertButtonTitleSize));
            this.negativeButton.setTextColor(Resource.getColor(R.color.simpleAlertButtonTitle));
            this.negativeButton.setBackground(null);
            this.negativeButton.setVisibility(View.GONE);
        }
        return this.negativeButton;
    }

    /**
     * 右侧确定button
     */
    private Button positiveButton;
    public Button getPositiveButton() {
        if (this.positiveButton == null) {
            this.positiveButton = new Button(getContext());
            this.positiveButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            this.positiveButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));
//            ((LinearLayout.LayoutParams) this.positiveButton.getLayoutParams()).weight = 1;
            this.positiveButton.setGravity(Gravity.CENTER);
            this.positiveButton.setLines(1);
            this.positiveButton.setTypeface(Typeface.DEFAULT_BOLD);
            this.positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, Resource.getDimensionPixelSize(R.dimen.simpleAlertButtonTitleSize));
            this.positiveButton.setTextColor(Resource.getColor(R.color.simpleAlertButtonTitle));
            this.positiveButton.setBackground(null);
            this.positiveButton.setVisibility(View.GONE);
        }
        return this.positiveButton;
    }

    /**
     * 标题
     * {@link #titleLabel}
     */
    private String title;
    public <T extends SimpleAlertController> T setTitle(String title) {
        this.title = title;
        getTitleLabel().setText(title);
        getTitleLabel().setVisibility(title != null ? View.VISIBLE : View.GONE);
        return (T) this;
    }
    public String getTitle() {
        return this.title;
    }

    /**
     * 信息
     * {@link #messageLabel}
     */
    private String message;
    public <T extends SimpleAlertController> T setMessage(String message) {
        this.message = message;
        getMessageLabel().setText(message);
        getMessageLabel().setVisibility(message != null ? View.VISIBLE : View.GONE);
        return (T) this;
    }
    public String getMessage() {
        return this.message;
    }

    /**
     * 取消button标题
     * {@link #negativeButton}
     */
    private String negativeButtonTitle;
    public <T extends SimpleAlertController> T setNegativeButtonTitle(String negativeButtonTitle) {
        this.negativeButtonTitle = negativeButtonTitle;
        getNegativeButton().setText(negativeButtonTitle);
        getNegativeButton().setVisibility(negativeButtonTitle != null ? View.VISIBLE : View.GONE);
        return (T) this;
    }
    public String getNegativeButtonTitle() {
        return this.negativeButtonTitle;
    }

    /**
     * 确定button标题
     * {@link #positiveButton}
     */
    private String positiveButtonTitle;
    public <T extends SimpleAlertController> T setPositiveButtonTitle(String positiveButtonTitle) {
        this.positiveButtonTitle = positiveButtonTitle;
        getPositiveButton().setText(positiveButtonTitle);
        getPositiveButton().setVisibility(positiveButtonTitle != null ? View.VISIBLE : View.GONE);
        return (T) this;
    }
    public String getPositiveButtonTitle() {
        return this.positiveButtonTitle;
    }

    /**
     * button点击回调
     */
    public interface OnClickListener {
        void onButtonClick(SimpleAlertController controller, Button button);
    }
    private OnClickListener onNegativeButtonClickListener;
    public <T extends SimpleAlertController> T setOnNegativeButtonClickListener(OnClickListener onNegativeButtonClickListener) {
        this.onNegativeButtonClickListener = onNegativeButtonClickListener;
        return (T) this;
    }
    public OnClickListener getOnNegativeButtonClickListener() {
        if (this.onNegativeButtonClickListener == null) {
            this.onNegativeButtonClickListener = new OnClickListener() {
                @Override
                public void onButtonClick(SimpleAlertController controller, Button button) {
                }
            };
        }
        return this.onNegativeButtonClickListener;
    }

    private OnClickListener onPositiveButtonClickListener;
    public <T extends SimpleAlertController> T setOnPositiveButtonClickListener(OnClickListener onPositiveButtonClickListener) {
        this.onPositiveButtonClickListener = onPositiveButtonClickListener;
        return (T) this;
    }
    public OnClickListener getOnPositiveButtonClickListener() {
        if (this.onPositiveButtonClickListener == null) {
            this.onPositiveButtonClickListener = new OnClickListener() {
                @Override
                public void onButtonClick(SimpleAlertController controller, Button button) {
                }
            };
        }
        return this.onPositiveButtonClickListener;
    }


    /* Overrides */
    @Override
    protected void onPopupDismiss() {
        super.onPopupDismiss();
    }

    /* Delegates */

    private RippleFrameLayout rippleFrameLayout;
    protected RippleFrameLayout getRippleFrameLayout() {
        if (this.rippleFrameLayout == null) {
            this.rippleFrameLayout = new RippleFrameLayout(getContext());
            this.rippleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));
            ((LinearLayout.LayoutParams) this.rippleFrameLayout.getLayoutParams()).weight = 1;
            this.rippleFrameLayout.setHighlightColor(Color.RED);
        }
        return this.rippleFrameLayout;
    }

    /* Private Methods */
    private void init() {
        getAlertLinearLayout().addView(getTitleLabel());
        getAlertLinearLayout().addView(getMessageLabel());
        getAlertLinearLayout().addView(getSplitVerticalView());
        getButtonLayout().addView(getNegativeButton());
        getButtonLayout().addView(getSplitButtonView());

        getRippleFrameLayout().addView(getPositiveButton());
        getButtonLayout().addView(getRippleFrameLayout());

//        getButtonLayout().addView(getPositiveButton());
        getAlertLinearLayout().addView(getButtonLayout());

        getRootFrameLayout().addView(getAlertLinearLayout());

        getMessageLabel().setMovementMethod(new ScrollingMovementMethod());
        getNegativeButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopup();
                getOnNegativeButtonClickListener().onButtonClick(self, (Button) v);
            }
        });
        getPositiveButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopup();
                getOnPositiveButtonClickListener().onButtonClick(self, (Button) v);
            }
        });

        setDismissOnTouchOutside(false);
        setPopupBackgroundColor(Resource.getColor(R.color.simpleAlertBackground));
        setEdgeRoundedRadius(Resource.getDimensionPixelSize(R.dimen.simpleAlertRoundedRadius));
        getRippleFrameLayout().setEdgeRoundedRadiusRightBottom(getPopupBackgroundView().getEdgeRoundedRadius());
    }
}