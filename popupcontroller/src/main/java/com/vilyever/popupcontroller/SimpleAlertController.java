package com.vilyever.popupcontroller;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * SimpleAlertController
 * AndroidPopupController <com.vilyever.popupcontroller>
 * Created by vilyever on 2016/2/24.
 * Feature:
 */
public class SimpleAlertController extends PopupController {
    final SimpleAlertController self = this;
    
    
    /* Constructors */
    public SimpleAlertController(Context context) {
        super(context, R.layout.simple_alert_controller);

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
        self.setPopupBackgroundColor(ContextCompat.getColor(self.getContext(), R.color.simpleAlertBackground));
        self.setEdgeRoundedRadius(self.getContext().getResources().getDimensionPixelSize(R.dimen.simpleAlertRoundedRadius));
    }
    
    
    /* Public Methods */
    public static SimpleAlertController create(Context context) {
        return new SimpleAlertController(context);
    }

    /**
     * 显示alert窗口
     * @param anchorView 锚view，此view仅用于查找window上的decorView，故此view需在decorView的子view中
     */
    public void show(View anchorView) {
        if (self.getNegativeButtonTitle() == null && self.getPositiveButtonTitle() == null) {
            self.setPositiveButtonTitle(self.getContext().getString(R.string.defaultAlertPositiveButtonTitle));
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
    }

    
    /* Properties */
    /**
     * 标题栏
     */
    private TextView titleLabel;
    public TextView getTitleLabel() { if (titleLabel == null) { titleLabel = (TextView) self.getView().findViewById(R.id.titleLabel); } return titleLabel; }

    /**
     * 信息栏
     */
    private TextView messageLabel;
    public TextView getMessageLabel() { if (messageLabel == null) { messageLabel = (TextView) self.getView().findViewById(R.id.messageLabel); } return messageLabel; }

    /**
     * 按钮分割线
     */
    private View splitButtonView;
    public View getSplitButtonView() { if (splitButtonView == null) { splitButtonView = (View) self.getView().findViewById(R.id.splitButtonView); } return splitButtonView; }

    /**
     * 左侧取消button
     */
    private Button negativeButton;
    public Button getNegativeButton() { if (negativeButton == null) { negativeButton = (Button) self.getView().findViewById(R.id.negativeButton); } return negativeButton; }

    /**
     * 右侧确定button
     */
    private Button positiveButton;
    public Button getPositiveButton() { if (positiveButton == null) { positiveButton = (Button) self.getView().findViewById(R.id.positiveButton); } return positiveButton; }

    /**
     * 标题
     * {@link #titleLabel}
     */
    private String title;
    public SimpleAlertController setTitle(String title) {
        this.title = title;
        self.getTitleLabel().setText(title);
        self.getTitleLabel().setVisibility(View.VISIBLE);
        return this; 
    }
    public String getTitle() {
        return title;
    }

    /**
     * 信息
     * {@link #messageLabel}
     */
    private String message;
    public SimpleAlertController setMessage(String message) {
        this.message = message;
        self.getMessageLabel().setText(message);
        self.getMessageLabel().setVisibility(View.VISIBLE);
        return this; 
    }
    public String getMessage() {
        return message;
    }

    /**
     * 取消button标题
     * {@link #negativeButton}
     */
    private String negativeButtonTitle;
    public SimpleAlertController setNegativeButtonTitle(String negativeButtonTitle) {
        this.negativeButtonTitle = negativeButtonTitle;
        self.getNegativeButton().setText(negativeButtonTitle);
        self.getNegativeButton().setVisibility(View.VISIBLE);
        return this; 
    }
    public String getNegativeButtonTitle() {
        return negativeButtonTitle;
    }

    /**
     * 确定button标题
     * {@link #positiveButton}
     */
    private String positiveButtonTitle;
    public SimpleAlertController setPositiveButtonTitle(String positiveButtonTitle) {
        this.positiveButtonTitle = positiveButtonTitle;
        self.getPositiveButton().setText(positiveButtonTitle);
        self.getPositiveButton().setVisibility(View.VISIBLE);
        return this;
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
    public SimpleAlertController setNegativeDelegate(ActionDelegate negativeDelegate) {
        this.negativeDelegate = negativeDelegate;
        return this;
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
    private SimpleAlertController setPositiveDelegate(ActionDelegate positiveDelegate) {
        this.positiveDelegate = positiveDelegate;
        return this;
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
    
}