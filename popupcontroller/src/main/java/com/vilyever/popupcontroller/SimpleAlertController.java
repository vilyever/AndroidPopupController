package com.vilyever.popupcontroller;

import android.content.Context;
import android.graphics.Typeface;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vilyever.resource.VDResource;

/**
 * SimpleAlertController
 * AndroidPopupController <com.vilyever.popupcontroller>
 * Created by vilyever on 2016/2/24.
 * Feature:
 */
public class SimpleAlertController extends PopupController {
    final SimpleAlertController self = this;
    
    
    /* Constructors */
    public SimpleAlertController(View view) {
        super(view);

        self.initial();
    }
    
    
    /* Public Methods */
    public static SimpleAlertController create(Context context) {
        return new SimpleAlertController(genereteRootView(context));
    }

    /**
     * 显示alert窗口
     * @param anchorView 锚view，此view仅用于查找window上的decorView，故此view需在decorView的子view中
     */
    public <T extends SimpleAlertController> T show(View anchorView) {
        if (self.getNegativeButtonTitle() == null && self.getPositiveButtonTitle() == null) {
            self.setPositiveButtonTitle(VDResource.getString(R.string.defaultAlertPositiveButtonTitle));
        }

        if (self.getTitle() == null && self.getMessage() == null) {
            self.setTitle("");
        }

        if (self.getNegativeButtonTitle() == null || self.getPositiveButtonTitle() == null) {
            self.getSplitButtonView().setVisibility(View.GONE);
        }
        else {
            self.getSplitButtonView().setVisibility(View.VISIBLE);
        }

        View view = anchorView;
        while (view.getParent() instanceof View) {
            view = (View) view.getParent();
        }
        self.popupInView(view, PopupDirection.Center);

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
    private FrameLayout getRootFrameLayout(Context context) {
        if (rootFrameLayout == null) {

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
            alertLinearLayout = new LinearLayout(self.getContext());
            alertLinearLayout.setLayoutParams(new FrameLayout.LayoutParams(VDResource.getDimensionPixelSize(R.dimen.simpleAlertWidth), ViewGroup.LayoutParams.WRAP_CONTENT));
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
            titleLabel = new TextView(self.getContext());
            titleLabel.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, VDResource.getDimensionPixelSize(R.dimen.simpleAlertTitleHeight)));
            titleLabel.setGravity(Gravity.CENTER);
            titleLabel.setLines(1);
            titleLabel.setTypeface(Typeface.DEFAULT_BOLD);
            titleLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, VDResource.getDimensionPixelSize(R.dimen.simpleAlertTitleSize));
            titleLabel.setTextColor(VDResource.getColor(R.color.simpleAlertTitle));
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
            messageLabel = new TextView(self.getContext());
            messageLabel.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int margin = VDResource.getDimensionPixelSize(R.dimen.simpleAlertMargin);
            ((LinearLayout.LayoutParams) messageLabel.getLayoutParams()).setMargins(margin, margin, margin, margin);
            messageLabel.setMaxHeight(VDResource.getDimensionPixelSize(R.dimen.simpleAlertMessageMaxHeight));
            messageLabel.setGravity(Gravity.CENTER);
            messageLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, VDResource.getDimensionPixelSize(R.dimen.simpleAlertMessageSize));
            messageLabel.setTextColor(VDResource.getColor(R.color.simpleAlertMessage));
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
            splitVerticalView = new View(self.getContext());
            splitVerticalView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, VDResource.getDimensionPixelSize(R.dimen.simpleAlertSplitLine)));
            splitVerticalView.setBackgroundColor(VDResource.getColor(R.color.simpleAlertSplitLine));
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
            buttonLayout = new LinearLayout(self.getContext());
            buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, VDResource.getDimensionPixelSize(R.dimen.simpleAlertButtonHeight)));
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
            splitButtonView = new View(self.getContext());
            splitButtonView.setLayoutParams(new LinearLayout.LayoutParams(VDResource.getDimensionPixelSize(R.dimen.simpleAlertSplitLine), ViewGroup.LayoutParams.MATCH_PARENT));
            splitButtonView.setBackgroundColor(VDResource.getColor(R.color.simpleAlertSplitLine));
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
            negativeButton = new Button(self.getContext());
            negativeButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));
            ((LinearLayout.LayoutParams) negativeButton.getLayoutParams()).weight = 1;
            negativeButton.setGravity(Gravity.CENTER);
            negativeButton.setLines(1);
            negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, VDResource.getDimensionPixelSize(R.dimen.simpleAlertButtonTitleSize));
            negativeButton.setTextColor(VDResource.getColor(R.color.simpleAlertButtonTitle));
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
            positiveButton = new Button(self.getContext());
            positiveButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));
            ((LinearLayout.LayoutParams) positiveButton.getLayoutParams()).weight = 1;
            positiveButton.setGravity(Gravity.CENTER);
            positiveButton.setLines(1);
            positiveButton.setTypeface(Typeface.DEFAULT_BOLD);
            positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, VDResource.getDimensionPixelSize(R.dimen.simpleAlertButtonTitleSize));
            positiveButton.setTextColor(VDResource.getColor(R.color.simpleAlertButtonTitle));
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
        self.getTitleLabel().setText(title);
        self.getTitleLabel().setVisibility(title != null ? View.VISIBLE : View.GONE);
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
        self.getMessageLabel().setText(message);
        self.getMessageLabel().setVisibility(message != null ? View.VISIBLE : View.GONE);
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
        self.getNegativeButton().setText(negativeButtonTitle);
        self.getNegativeButton().setVisibility(negativeButtonTitle != null ? View.VISIBLE : View.GONE);
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
        self.getPositiveButton().setText(positiveButtonTitle);
        self.getPositiveButton().setVisibility(positiveButtonTitle != null ? View.VISIBLE : View.GONE);
        return (T) this;
    }
    public String getPositiveButtonTitle() {
        return positiveButtonTitle;
    }

    /**
     * button点击回调
     */
    public interface ActionDelegate {
        void onButtonClick(SimpleAlertController controller, Button button);
    }
    private ActionDelegate negativeDelegate;
    public <T extends SimpleAlertController> T setNegativeDelegate(ActionDelegate negativeDelegate) {
        this.negativeDelegate = negativeDelegate;
        return (T) this;
    }
    public ActionDelegate getNegativeDelegate() {
        if (negativeDelegate == null) {
            negativeDelegate = new ActionDelegate() {
                @Override
                public void onButtonClick(SimpleAlertController controller, Button button) {
                }
            };
        }
        return negativeDelegate;
    }

    private ActionDelegate positiveDelegate;
    public <T extends SimpleAlertController> T setPositiveDelegate(ActionDelegate positiveDelegate) {
        this.positiveDelegate = positiveDelegate;
        return (T) this;
    }
    public ActionDelegate getPositiveDelegate() {
        if (positiveDelegate == null) {
            positiveDelegate = new ActionDelegate() {
                @Override
                public void onButtonClick(SimpleAlertController controller, Button button) {
                }
            };
        }
        return positiveDelegate;
    }


    /* Overrides */
     
     
    /* Delegates */
     
     
    /* Private Methods */
    private static View genereteRootView(Context context) {
        return new FrameLayout(context);
    }

    private void initial() {
        FrameLayout rootFrameLayout = (FrameLayout) self.getView();
        rootFrameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        self.getAlertLinearLayout().addView(self.getTitleLabel());
        self.getAlertLinearLayout().addView(self.getMessageLabel());
        self.getAlertLinearLayout().addView(self.getSplitVerticalView());
        self.getButtonLayout().addView(self.getNegativeButton());
        self.getButtonLayout().addView(self.getSplitButtonView());
        self.getButtonLayout().addView(self.getPositiveButton());
        self.getAlertLinearLayout().addView(self.getButtonLayout());

        rootFrameLayout.addView(self.getAlertLinearLayout());

        self.getMessageLabel().setMovementMethod(new ScrollingMovementMethod());
        self.getNegativeButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.dismissPopup();
                self.getNegativeDelegate().onButtonClick(self, (Button) v);
            }
        });
        self.getPositiveButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.dismissPopup();
                self.getPositiveDelegate().onButtonClick(self, (Button) v);
            }
        });

        self.setDismissOnTouchOutside(false);
        self.setPopupBackgroundColor(VDResource.getColor(R.color.simpleAlertBackground));
        self.setEdgeRoundedRadius(VDResource.getDimensionPixelSize(R.dimen.simpleAlertRoundedRadius));
    }
    
}