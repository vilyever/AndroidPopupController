package com.vilyever.popupcontroller.popup;

import android.view.View;
import android.widget.PopupWindow;

/**
 * CustomPopupWindow
 * Created by vilyever on 2016/5/12.
 * Feature:
 */
public class CustomPopupWindow extends PopupWindow {
    final CustomPopupWindow self = this;
    
    
    /* Constructors */
    public CustomPopupWindow(View contentView) {
        super(contentView);
    }
    
    /* Public Methods */
    public void close() {
        super.dismiss();
    }
    
    /* Properties */
    private boolean dismissDisabled;
    public CustomPopupWindow setDismissDisabled(boolean dismissDisabled) {
        this.dismissDisabled = dismissDisabled;
        return this;
    }
    public boolean isDismissDisabled() {
        return this.dismissDisabled;
    }
    
    /* Overrides */
    @Override
    public void dismiss() {
        if (!isDismissDisabled()) {
            super.dismiss();
        }
    }

    /* Delegates */
    
    
    /* Private Methods */
    
}