package com.vilyever.popupcontroller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * ViewController
 * AndroidPopupController <com.vilyever.popupcontroller>
 * Created by vilyever on 2016/3/1.
 * Feature:
 * 视图控制器
 */
public class ViewController {
    final ViewController self = this;


    /* Constructors */
    public ViewController(Context context) {
        self.setContext(context);
    }

    public ViewController(Context context, int layout) {
        // For generate LayoutParams
        FrameLayout wrapperFrameLayout = new FrameLayout(context);
        wrapperFrameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        View.inflate(context, layout, wrapperFrameLayout);

        self.setView(wrapperFrameLayout.getChildAt(0));
    }

    public ViewController(View view) {
        self.setView(view);
    }


    /* Public Methods */
    /**
     * 添加当前controller的view到一个父view上
     * @param parent 父view
     */
    public <T extends ViewController> T attachToParent(ViewGroup parent) {
        if (self.getView().getParent() != parent) {
            self.detachFromParent();
            parent.addView(self.getView());
        }

        return (T) this;
    }

    /**
     * 将当前controller的view从父view移除
     */
    public <T extends ViewController> T detachFromParent() {
        if (self.getView().getParent() != null) {
            ((ViewGroup) self.getView().getParent()).removeView(self.getView());
        }
        return (T) this;
    }


    /* Properties */
    private Context context;
    protected <T extends ViewController> T setContext(Context context) {
        this.context = context;
        return (T) this;
    }
    public Context getContext() {
        return context;
    }

    /**
     * controller的根视图
     * 注意：如果controller是由{@link #ViewController(Context, int)}生成的，此时的根视图view存在一个包裹它的FrameLayout
     * 这是由于如果没有一个view用于初始化layout，layout的根视图将无法生成LayoutParams
     * 如果要对LayoutParams，请注意其类型
     */
    private View view;
    protected <T extends ViewController> T setView(View view) {
        this.view = view;
        self.setContext(view.getContext());
        return (T) this;
    }
    public View getView() {
        return view;
    }
    
    
    /* Overrides */
     
     
    /* Delegates */
     
     
    /* Private Methods */
    
}