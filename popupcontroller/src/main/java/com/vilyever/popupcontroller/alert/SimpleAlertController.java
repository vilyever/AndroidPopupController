package com.vilyever.popupcontroller.alert;

import android.app.Activity;
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
            self.show(resumedActivity);
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
    private SimpleAlertController setRootFrameLayout(FrameLayout rootFrameLayout) {
        this.rootFrameLayout = rootFrameLayout;
        return this;
    }
    private FrameLayout getRootFrameLayout() {
        if (rootFrameLayout == null) {
            rootFrameLayout = new FrameLayout(getContext());
            rootFrameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return rootFrameLayout;
    }

    /**
     * 包含alert所有内容的视图
     */
    private LinearLayout alertLinearLayout;
    private SimpleAlertController setAlertLinearLayout(LinearLayout alertLinearLayout) {
        this.alertLinearLayout = alertLinearLayout;
        return this;
    }
    public LinearLayout getAlertLinearLayout() {
        if (alertLinearLayout == null) {
            alertLinearLayout = new LinearLayout(getContext());
            alertLinearLayout.setLayoutParams(new FrameLayout.LayoutParams(Resource.getDimensionPixelSize(R.dimen.simpleAlertWidth), ViewGroup.LayoutParams.WRAP_CONTENT));
            alertLinearLayout.setOrientation(LinearLayout.VERTICAL);
        }
        return alertLinearLayout;
    }

    /**
     * 标题栏
     */
    private TextView titleLabel;
    private SimpleAlertController setTitleLabel(TextView titleLabel) {
        this.titleLabel = titleLabel;
        return this;
    }
    public TextView getTitleLabel() {
        if (titleLabel == null) {
            titleLabel = new TextView(getContext());
            titleLabel.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Resource.getDimensionPixelSize(R.dimen.simpleAlertTitleHeight)));
            titleLabel.setGravity(Gravity.CENTER);
            titleLabel.setLines(1);
            titleLabel.setTypeface(Typeface.DEFAULT_BOLD);
            titleLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, Resource.getDimensionPixelSize(R.dimen.simpleAlertTitleSize));
            titleLabel.setTextColor(Resource.getColor(R.color.simpleAlertTitle));
            titleLabel.setVisibility(View.GONE);
        }
        return titleLabel;
    }

    /**
     * 信息栏
     */
    private TextView messageLabel;
    private SimpleAlertController setMessageLabel(TextView messageLabel) {
        this.messageLabel = messageLabel;
        return this;
    }
    public TextView getMessageLabel() {
        if (messageLabel == null) {
            messageLabel = new TextView(getContext());
            messageLabel.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int margin = Resource.getDimensionPixelSize(R.dimen.simpleAlertMargin);
            ((LinearLayout.LayoutParams) messageLabel.getLayoutParams()).setMargins(margin, margin, margin, margin);
            messageLabel.setMaxHeight(Resource.getDimensionPixelSize(R.dimen.simpleAlertMessageMaxHeight));
            messageLabel.setGravity(Gravity.CENTER);
            messageLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, Resource.getDimensionPixelSize(R.dimen.simpleAlertMessageSize));
            messageLabel.setTextColor(Resource.getColor(R.color.simpleAlertMessage));
            messageLabel.setVisibility(View.GONE);
        }
        return messageLabel;
    }

    /**
     * 分割标题信息与button的分割线
     */
    private View splitVerticalView;
    private SimpleAlertController setSplitVerticalView(View splitVerticalView) {
        this.splitVerticalView = splitVerticalView;
        return this;
    }
    public View getSplitVerticalView() {
        if (splitVerticalView == null) {
            splitVerticalView = new View(getContext());
            splitVerticalView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Resource.getDimensionPixelSize(R.dimen.simpleAlertSplitLine)));
            splitVerticalView.setBackgroundColor(Resource.getColor(R.color.simpleAlertSplitLine));
        }
        return splitVerticalView;
    }

    private LinearLayout buttonLayout;
    private SimpleAlertController setButtonLayout(LinearLayout buttonLayout) {
        this.buttonLayout = buttonLayout;
        return this;
    }
    public LinearLayout getButtonLayout() {
        if (buttonLayout == null) {
            buttonLayout = new LinearLayout(getContext());
            buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Resource.getDimensionPixelSize(R.dimen.simpleAlertButtonHeight)));
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        }
        return buttonLayout;
    }

    /**
     * 按钮分割线
     */
    private View splitButtonView;
    private SimpleAlertController setSplitButtonView(View splitButtonView) {
        this.splitButtonView = splitButtonView;
        return this;
    }
    public View getSplitButtonView() {
        if (splitButtonView == null) {
            splitButtonView = new View(getContext());
            splitButtonView.setLayoutParams(new LinearLayout.LayoutParams(Resource.getDimensionPixelSize(R.dimen.simpleAlertSplitLine), ViewGroup.LayoutParams.MATCH_PARENT));
            splitButtonView.setBackgroundColor(Resource.getColor(R.color.simpleAlertSplitLine));
        }
        return splitButtonView;
    }

    /**
     * 左侧取消button
     */
    private Button negativeButton;
    private SimpleAlertController setNegativeButton(Button negativeButton) {
        this.negativeButton = negativeButton;
        return this;
    }
    public Button getNegativeButton() {
        if (negativeButton == null) {
            negativeButton = new Button(getContext());
            negativeButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));
            ((LinearLayout.LayoutParams) negativeButton.getLayoutParams()).weight = 1;
            negativeButton.setGravity(Gravity.CENTER);
            negativeButton.setLines(1);
            negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, Resource.getDimensionPixelSize(R.dimen.simpleAlertButtonTitleSize));
            negativeButton.setTextColor(Resource.getColor(R.color.simpleAlertButtonTitle));
            negativeButton.setBackground(null);
            negativeButton.setVisibility(View.GONE);
        }
        return negativeButton;
    }

    /**
     * 右侧确定button
     */
    private Button positiveButton;
    private SimpleAlertController setPositiveButton(Button positiveButton) {
        this.positiveButton = positiveButton;
        return this;
    }
    public Button getPositiveButton() {
        if (positiveButton == null) {
            positiveButton = new Button(getContext());
            positiveButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));
            ((LinearLayout.LayoutParams) positiveButton.getLayoutParams()).weight = 1;
            positiveButton.setGravity(Gravity.CENTER);
            positiveButton.setLines(1);
            positiveButton.setTypeface(Typeface.DEFAULT_BOLD);
            positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, Resource.getDimensionPixelSize(R.dimen.simpleAlertButtonTitleSize));
            positiveButton.setTextColor(Resource.getColor(R.color.simpleAlertButtonTitle));
            positiveButton.setBackground(null);
            positiveButton.setVisibility(View.GONE);
        }
        return positiveButton;
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
        return title;
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
        return message;
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
        return negativeButtonTitle;
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
        return positiveButtonTitle;
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
        if (onNegativeButtonClickListener == null) {
            onNegativeButtonClickListener = new OnClickListener() {
                @Override
                public void onButtonClick(SimpleAlertController controller, Button button) {
                }
            };
        }
        return onNegativeButtonClickListener;
    }

    private OnClickListener onPositiveButtonClickListener;
    public <T extends SimpleAlertController> T setOnPositiveButtonClickListener(OnClickListener onPositiveButtonClickListener) {
        this.onPositiveButtonClickListener = onPositiveButtonClickListener;
        return (T) this;
    }
    public OnClickListener getOnPositiveButtonClickListener() {
        if (onPositiveButtonClickListener == null) {
            onPositiveButtonClickListener = new OnClickListener() {
                @Override
                public void onButtonClick(SimpleAlertController controller, Button button) {
                }
            };
        }
        return onPositiveButtonClickListener;
    }


    /* Overrides */
    @Override
    protected void onPopupDismiss() {
        super.onPopupDismiss();
    }

    /* Delegates */


    /* Private Methods */
    private void init() {
        getAlertLinearLayout().addView(getTitleLabel());
        getAlertLinearLayout().addView(getMessageLabel());
        getAlertLinearLayout().addView(getSplitVerticalView());
        getButtonLayout().addView(getNegativeButton());
        getButtonLayout().addView(getSplitButtonView());
        getButtonLayout().addView(getPositiveButton());
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
    }
}