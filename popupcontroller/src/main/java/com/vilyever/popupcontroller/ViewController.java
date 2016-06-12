package com.vilyever.popupcontroller;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.vilyever.activityhelper.ActivityHelper;
import com.vilyever.activityhelper.ActivityStateDelegate;
import com.vilyever.popupcontroller.animation.AnimationPerformer;
import com.vilyever.popupcontroller.animation.OnAnimationStateChangeListener;
import com.vilyever.popupcontroller.listener.OnViewLayoutChangeListener;
import com.vilyever.popupcontroller.listener.OnViewStateChangeListener;

import java.util.ArrayList;

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
        ViewControllerManager.checkInitialized();

        setContext(context);

        ActivityHelper.registerActivityStateDelegate(getActivityStateDelegate());
    }

    public ViewController(Context context, int layout) {
        this(context);

        // For generate LayoutParams
        FrameLayout wrapperFrameLayout = new FrameLayout(context);

        View view = LayoutInflater.from(wrapperFrameLayout.getContext())
                                  .inflate(layout, wrapperFrameLayout, false);
        setView(view);
        setContext(context);
    }

    public ViewController(View view) {
        this(view.getContext());

        setView(view);
    }

    public static ViewController fromActivity(Activity activity) {
        return new ViewController(activity.findViewById(android.R.id.content));
    }

    public static ViewController fromFragment(Fragment fragment) {
        if (fragment.getView() == null) {
            throw new IllegalStateException("onCreateView is not call in fragment");
        }
        return new ViewController(fragment.getView());
    }

    /* Public Methods */
    /**
     * 当前{@link #view}的context是否是Activity
     * @return
     */
    public boolean checkContextIsActivity() {
        return getContext() instanceof Activity;
    }

    /**
     * 若当前{@link #view}的context是Activity，返回此Activity，否则返回null
     * @return
     */
    public Activity getActivity() {
        if (checkContextIsActivity()) {
            return (Activity) getContext();
        }

        return null;
    }

    /**
     * 添加当前controller的view到一个activity的根视图上
     * @param activity
     * @return
     */
    public <T extends ViewController> T attachToActivity(Activity activity) {
        return attachToParent((ViewGroup) activity.getWindow().getDecorView());
    }

    public <T extends ViewController> T attachToParent(Activity activity, int parentLayoutID) {
        return attachToParent((ViewGroup) activity.findViewById(parentLayoutID));
    }
    
    /**
     * 添加当前controller的view到一个父view上
     * @param parent 父view
     */
    public <T extends ViewController> T attachToParent(ViewGroup parent) {
        if (getDetachAnimationPerformer() != null) {
            getDetachAnimationPerformer().onAnimationCancel(getView());
        }

        if (getView().getParent() != parent) {
            ViewGroup.MarginLayoutParams preLayoutParams = null;
            if (getView().getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                preLayoutParams = (ViewGroup.MarginLayoutParams) getView().getLayoutParams();
            }

            detachFromParent(false);

            internalAttemptToChangeStateFlow(StateFlow.State.WillAttach, true, parent);

            parent.addView(getView());

            if (isKeepMargin()) {
                if (preLayoutParams != null && getView().getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getView().getLayoutParams();
                    layoutParams.setMargins(preLayoutParams.leftMargin, preLayoutParams.topMargin, preLayoutParams.rightMargin, preLayoutParams.bottomMargin);
                }
            }

            internalAttemptToChangeStateFlow(StateFlow.State.Attached, true, parent);
        }

        return (T) this;
    }

    /** {@link #detachFromParent(boolean)} */
    public <T extends ViewController> T detachFromParent() {
        return detachFromParent(true);
    }

    /**
     * 将当前controller的view从父view移除
     * 注意：如果detach动画{@link #detachAnimationPerformer}不为空且animated为true，即detach动画可用，在动画结束前，根视图的parent不会移除当前根视图
     * 注意：如果parent是{@link android.widget.LinearLayout}时，在detach动画未完成前向该parent添加视图会导致显示问题
     * @param animated 是否启用detach动画
     */
    public <T extends ViewController> T detachFromParent(boolean animated) {
        if (animated && isDisappearing()) {
            return (T) this;
        }

        if (getAttachAnimationPerformer() != null) {
            getAttachAnimationPerformer().onAnimationCancel(getView());
        }

        if (getView().getParent() != null) {
            ViewGroup parent = (ViewGroup) getView().getParent();

            internalAttemptToChangeStateFlow(StateFlow.State.WillDetach, false, parent);

            if (animated && getDetachAnimationPerformer() != null) {
                getDetachAnimationPerformer().onAnimation(getView(), getOnDetachAnimationStateChangeListener());
            }
            else {
                if (getDetachAnimationPerformer() != null) {
                    getDetachAnimationPerformer().onAnimationCancel(getView());
                }

                parent.removeView(getView());

                internalAttemptToChangeStateFlow(StateFlow.State.Detached, false, parent);
            }
        }
        return (T) this;
    }

    /**
     * 替代controller进入parent显示
     * @param controller 被替代的controller
     */
    public void replaceController(@NonNull ViewController controller) {
        if (controller.isAttachedToParent() && controller.getView().getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) controller.getView().getParent();
            parent.removeView(controller.getView());
            attachToParent(parent);
        }
    }

    /**
     * 是否添加到了某个parent上
     */
    public boolean isAttachedToParent() {
        return getView() != null && getView().getParent() != null;
    }

    /**
     * 是否显示在window上
     * 注意：true表明此时根视图{@link #view}的宽高等参数已经计算完成
     */
    public boolean isAppeared() {
        return getStateFlow().getState() == StateFlow.State.Appeared;
    }

    /**
     * 添加根视图{@link #view}的layout change监听
     * @param listener listener
     * @return this
     */
    public ViewController registerOnViewLayoutChangeListener(OnViewLayoutChangeListener listener) {
        if (!getOnViewLayoutChangeListeners().contains(listener)) {
            getOnViewLayoutChangeListeners().add(listener);
        }
        return this;
    }

    /**
     * 移除根视图{@link #view}的layout change监听
     * @param listener listener
     * @return this
     */
    public ViewController removeOnViewLayoutChangeListener(OnViewLayoutChangeListener listener) {
        getOnViewLayoutChangeListeners().remove(listener);
        return this;
    }

    /**
     * 添加根视图{@link #view}的attach state监听
     * @param listener listener
     * @return this
     */
    public ViewController registerOnViewAttachStateChangeListener(OnViewStateChangeListener listener) {
        if (!getOnViewStateChangeListeners().contains(listener)) {
            getOnViewStateChangeListeners().add(listener);
        }
        return this;
    }

    /**
     * 移除根视图{@link #view}的attach state监听
     * @param listener listener
     * @return this
     */
    public ViewController removeOnViewAttachStateChangeListener(OnViewStateChangeListener listener) {
        getOnViewStateChangeListeners().remove(listener);
        return this;
    }

    /* Properties */
    private Context context;
    protected <T extends ViewController> T setContext(Context context) {
        this.context = context;
        return (T) this;
    }
    public Context getContext() {
        return this.context;
    }

    /**
     * controller的根视图
     * 注意：如果controller是由{@link #ViewController(Context, int)}生成的，此时的根视图view存在一个包裹它的FrameLayout
     * 这是由于如果没有一个view用于初始化layout，layout的根视图将无法生成LayoutParams
     * 如果要对LayoutParams，请注意其类型
     */
    private View view;
    protected <T extends ViewController> T setView(View view) {
        if (this.view != null) {
            this.view.removeOnLayoutChangeListener(getInternalOnLayoutChangeListener());
            this.view.removeOnAttachStateChangeListener(getInternalOnAttachStateChangeListener());
        }

        this.view = view;
        setContext(this.view.getContext());

        this.view.addOnLayoutChangeListener(getInternalOnLayoutChangeListener());
        this.view.addOnAttachStateChangeListener(getInternalOnAttachStateChangeListener());

        return (T) this;
    }
    public View getView() {
        return this.view;
    }

    private StateFlow stateFlow;
    protected StateFlow getStateFlow() {
        if (this.stateFlow == null) {
            this.stateFlow = new StateFlow();
        }
        return this.stateFlow;
    }
    
    private boolean keepMargin;
    public ViewController setKeepMargin(boolean keepMargin) {
        this.keepMargin = keepMargin;
        return this;
    }
    public boolean isKeepMargin() {
        return this.keepMargin;
    }

    /**
     * attach动画提供
     */
    private AnimationPerformer attachAnimationPerformer;
    public ViewController setAttachAnimationPerformer(AnimationPerformer attachAnimationPerformer) {
        this.attachAnimationPerformer = attachAnimationPerformer;
        return this;
    }
    public AnimationPerformer getAttachAnimationPerformer() {
        return this.attachAnimationPerformer;
    }

    /**
     * detach动画提供
     */
    private AnimationPerformer detachAnimationPerformer;
    public ViewController setDetachAnimationPerformer(AnimationPerformer detachAnimationPerformer) {
        this.detachAnimationPerformer = detachAnimationPerformer;
        return this;
    }
    public AnimationPerformer getDetachAnimationPerformer() {
        return this.detachAnimationPerformer;
    }

    private ActivityStateDelegate activityStateDelegate;
    protected ActivityStateDelegate getActivityStateDelegate() {
        if (this.activityStateDelegate == null) {
            this.activityStateDelegate = new ActivityStateDelegate() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    if (self.getContext().equals(activity)
                            || !(self.getContext() instanceof Activity)) {
                        self.onActivityCreated(activity, savedInstanceState);
                    }
                }

                @Override
                public void onActivityStarted(Activity activity) {
                    if (self.getContext().equals(activity)
                        || !(self.getContext() instanceof Activity)) {
                        self.onActivityStarted(activity);
                    }
                }

                @Override
                public void onActivityResumed(Activity activity) {
                    if (self.getContext().equals(activity)
                        || !(self.getContext() instanceof Activity)) {
                        self.onActivityResumed(activity);
                    }
                }

                @Override
                public void onActivityPaused(Activity activity) {
                    if (self.getContext().equals(activity)
                        || !(self.getContext() instanceof Activity)) {
                        self.onActivityPaused(activity);
                    }
                }

                @Override
                public void onActivityStopped(Activity activity) {
                    if (self.getContext().equals(activity)
                        || !(self.getContext() instanceof Activity)) {
                        self.onActivityStopped(activity);
                    }
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                    if (self.getContext().equals(activity)
                        || !(self.getContext() instanceof Activity)) {
                        self.onActivitySaveInstanceState(activity, outState);
                    }
                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    if (self.getContext().equals(activity)
                        || !(self.getContext() instanceof Activity)) {
                        self.onActivityDestroyed(activity);
                    }
                }
            };
        }
        return this.activityStateDelegate;
    }

    /**
     * OnLayoutChangeListener
     * 用于当前根视图{@link #view}
     */
    private View.OnLayoutChangeListener internalOnLayoutChangeListener;
    private View.OnLayoutChangeListener getInternalOnLayoutChangeListener() {
        if (this.internalOnLayoutChangeListener == null) {
            this.internalOnLayoutChangeListener = new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    Rect frame = new Rect(left, top, right, bottom);
                    Rect oldFrame = new Rect(oldLeft, oldTop, oldRight, oldBottom);

                    self.onViewLayoutChange(frame, oldFrame);
                }
            };
        }
        return this.internalOnLayoutChangeListener;
    }

    /**
     * OnAttachStateChangeListener
     * 用于当前根视图{@link #view}
     */
    private View.OnAttachStateChangeListener internalOnAttachStateChangeListener;
    protected View.OnAttachStateChangeListener getInternalOnAttachStateChangeListener() {
        if (this.internalOnAttachStateChangeListener == null) {
            this.internalOnAttachStateChangeListener = new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    self.internalAttemptToChangeStateFlow(StateFlow.State.WillAppear, true, (ViewGroup) self.getView().getParent());

                    if (self.getAttachAnimationPerformer() != null) {
                        self.getAttachAnimationPerformer().onAnimation(self.getView(), self.getOnAttachAnimationStateChangeListener());
                    }
                    else {
                        self.getView().post(self.getAppearedRunnable());
                    }
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    self.internalAttemptToChangeStateFlow(StateFlow.State.Detached, false, (ViewGroup) self.getView().getParent());
                }
            };
        }
        return this.internalOnAttachStateChangeListener;
    }

    /**
     * OnViewLayoutChangeListener合集
     */
    private ArrayList<OnViewLayoutChangeListener> onViewLayoutChangeListeners;
    protected ArrayList<OnViewLayoutChangeListener> getOnViewLayoutChangeListeners() {
        if (this.onViewLayoutChangeListeners == null) {
            this.onViewLayoutChangeListeners = new ArrayList<OnViewLayoutChangeListener>();
        }
        return this.onViewLayoutChangeListeners;
    }

    /**
     * OnViewAttachStateChangeListener合集
     */
    private ArrayList<OnViewStateChangeListener> onViewStateChangeListeners;
    protected ArrayList<OnViewStateChangeListener> getOnViewStateChangeListeners() {
        if (this.onViewStateChangeListeners == null) {
            this.onViewStateChangeListeners = new ArrayList<OnViewStateChangeListener>();
        }
        return this.onViewStateChangeListeners;
    }

    /**
     * 是否正在进行attach动画
     */
    private boolean appearing;
    protected ViewController setAppearing(boolean appearing) {
        this.appearing = appearing;
        return this;
    }
    public boolean isAppearing() {
        return this.appearing;
    }

    /**
     * 是否正在进行detach动画
     */
    private boolean disappearing;
    protected ViewController setDisappearing(boolean disappearing) {
        this.disappearing = disappearing;
        return this;
    }
    public boolean isDisappearing() {
        return this.disappearing;
    }

    /**
     * 是否显示到window上过
     */
    private boolean alreadyAppearedOnce = false;
    protected ViewController setAlreadyAppearedOnce(boolean alreadyAppearedOnce) {
        this.alreadyAppearedOnce = alreadyAppearedOnce;
        return this;
    }
    protected boolean isAlreadyAppearedOnce() {
        return this.alreadyAppearedOnce;
    }

    private OnAnimationStateChangeListener onAttachAnimationStateChangeListener;
    protected OnAnimationStateChangeListener getOnAttachAnimationStateChangeListener() {
        if (this.onAttachAnimationStateChangeListener == null) {
            this.onAttachAnimationStateChangeListener = new OnAnimationStateChangeListener() {
                @Override
                public void onAnimationStart() {
                    self.setAppearing(true);
                    self.onAttachAnimationStart();
                }

                @Override
                public void onAnimationEnd() {
                    self.setAppearing(false);
                    self.onAttachAnimationEnd();

                    self.internalAttemptToChangeStateFlow(StateFlow.State.Appeared, true, (ViewGroup) self.getView().getParent());
                }

                @Override
                public void onAnimationCancel() {
                    self.setAppearing(false);
                    self.onAttachAnimationCancel();
                }
            };
        }
        return this.onAttachAnimationStateChangeListener;
    }

    private OnAnimationStateChangeListener onDetachAnimationStateChangeListener;
    protected OnAnimationStateChangeListener getOnDetachAnimationStateChangeListener() {
        if (this.onDetachAnimationStateChangeListener == null) {
            this.onDetachAnimationStateChangeListener = new OnAnimationStateChangeListener() {
                @Override
                public void onAnimationStart() {
                    self.setDisappearing(true);
                    self.onDetachAnimationStart();
                    self.internalAttemptToChangeStateFlow(StateFlow.State.WillDisappear, false, null);
                }

                @Override
                public void onAnimationEnd() {
                    self.setDisappearing(false);
                    self.onDetachAnimationEnd();

                    ViewGroup parent = (ViewGroup) getView().getParent();
                    parent.removeView(getView());

                    self.internalAttemptToChangeStateFlow(StateFlow.State.Detached, false, parent);
                }

                @Override
                public void onAnimationCancel() {
                    self.setDisappearing(false);
                    self.onDetachAnimationCancel();
                }
            };
        }
        return this.onDetachAnimationStateChangeListener;
    }

    private Runnable appearedRunnable;
    protected Runnable getAppearedRunnable() {
        if (this.appearedRunnable == null) {
            this.appearedRunnable = new Runnable() {
                @Override
                public void run() {
                    self.internalAttemptToChangeStateFlow(StateFlow.State.Appeared, true, (ViewGroup) self.getView().getParent());
                }
            };
        }
        return this.appearedRunnable;
    }
    
    /* Overrides */
     
     
    /* Delegates */


    /* Protected Methods */

    /**
     * 根视图{@link #view}的Layout发生改变
     * @param frame 当前frame
     * @param oldFrame 改变前的frame
     */
    @CallSuper
    protected void onViewLayoutChange(Rect frame, Rect oldFrame) {

        ArrayList<OnViewLayoutChangeListener> copyList =
                (ArrayList<OnViewLayoutChangeListener>)getOnViewLayoutChangeListeners().clone();
        int size = copyList.size();
        for (int i = 0; i < size; ++i) {
            copyList.get(i).onViewLayoutChange(this, frame, oldFrame);
        }
    }

    /**
     * 根视图{@link #view}将要添加到parent上
     * 注意：【Attach、Detach】与【Appear、Disappear】无特定顺序关系
     * @param parent 将要添加到的parent
     */
    @CallSuper
    protected void onViewWillAttachToParent(ViewGroup parent) {
    }

    /**
     * 根视图{@link #view}已经添加到parent上
     * 注意：【Attach、Detach】与【Appear、Disappear】无特定顺序关系
     * @param parent 已经添加到的parent，即根视图{@link #view}当前的parent
     */
    @CallSuper
    protected void onViewAttachedToParent(ViewGroup parent) {
    }

    /**
     * 根视图{@link #view}将要从当前parent上移除
     * 注意：【Attach、Detach】与【Appear、Disappear】无特定顺序关系
     * @param parent 根视图{@link #view}当前的parent
     */
    @CallSuper
    protected void onViewWillDetachFromParent(ViewGroup parent) {

        // 在根视图{@link #view}将要脱离window时，若子view持有焦点可能导致键盘不隐藏，故强制隐藏键盘
        if (getView().hasFocus()) {
            getView().clearFocus();
            InputMethodManager imm = (InputMethodManager) getView().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    /**
     * 根视图{@link #view}已经从先前的parent上移除
     * 注意：【Attach、Detach】与【Appear、Disappear】无特定顺序关系
     * @param parent 根视图{@link #view}先前的parent
     */
    @CallSuper
    protected void onViewDetachedFromParent(ViewGroup parent) {
    }

    /**
     * 根视图{@link #view}将要显示在window上
     * 注意：【Attach、Detach】与【Appear、Disappear】无特定顺序关系
     * 注意：此时根视图{@link #view}已经添加在了window下，只是还未计算宽高等参数
     * 注意：Appear与根视图{@link #view}的{@link View#setVisibility(int)}无关
     */
    @CallSuper
    protected void onViewWillAppear() {
    }

    /**
     * 根视图{@link #view}已经显示在window上
     * 注意：【Attach、Detach】与【Appear、Disappear】无特定顺序关系
     * 注意：此时可以对根视图{@link #view}直接获取getWidth()等
     * 注意：Appear与根视图{@link #view}的{@link View#setVisibility(int)}无关
     */
    @CallSuper
    protected void onViewAppeared() {

        if (!isAlreadyAppearedOnce()) {
            setAlreadyAppearedOnce(true);
            onViewFirstAppeared();
        }
    }

    /**
     * 第一次显示到window上时的回调
     */
    @CallSuper
    protected void onViewFirstAppeared() {
    }

    /**
     * 根视图{@link #view}将从window上被移除
     * 注意：【Attach、Detach】与【Appear、Disappear】无特定顺序关系
     * 注意：Disappear与根视图{@link #view}的{@link View#setVisibility(int)}无关
     */
    @CallSuper
    protected void onViewWillDisappear() {
    }

    /**
     * 根视图{@link #view}已经从window上被移除
     * 注意：【Attach、Detach】与【Appear、Disappear】无特定顺序关系
     * 若要处理类似WillDisappear时的事件，通常可以在{@link #onViewWillDetachFromParent(ViewGroup)}中完成
     * 注意：Disappear与根视图{@link #view}的{@link View#setVisibility(int)}无关
     */
    @CallSuper
    protected void onViewDisappeared() {
    }

    /**
     * 根视图{@link #view}attach显示动画开始
     */
    @CallSuper
    protected void onAttachAnimationStart() {
    }

    /**
     * 根视图{@link #view}attach显示动画结束
     */
    @CallSuper
    protected void onAttachAnimationEnd() {
    }

    /**
     * 根视图{@link #view}attach显示动画取消
     */
    @CallSuper
    protected void onAttachAnimationCancel() {
    }

    /**
     * 根视图{@link #view}detach显示动画开始
     */
    @CallSuper
    protected void onDetachAnimationStart() {
    }

    /**
     * 根视图{@link #view}detach显示动画结束
     */
    @CallSuper
    protected void onDetachAnimationEnd() {
    }

    /**
     * 根视图{@link #view}detach显示动画取消
     */
    @CallSuper
    protected void onDetachAnimationCancel() {
    }

    @CallSuper
    protected void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @CallSuper
    protected void onActivityStarted(Activity activity) {

    }

    @CallSuper
    protected void onActivityResumed(Activity activity) {

    }

    @CallSuper
    protected void onActivityPaused(Activity activity) {

    }

    @CallSuper
    protected void onActivityStopped(Activity activity) {

    }

    @CallSuper
    protected void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @CallSuper
    protected void onActivityDestroyed(Activity activity) {

    }

    /**
     * findViewById便捷封装
     * @param id 控件id
     * @param <T> 控件类型
     * @return 控件实例
     */
    protected  <T extends View> T findViewById(int id) {
        return (T) getView().findViewById(id);
    }

    /* Private Methods */
    private void internalAttemptToChangeStateFlow(@NonNull StateFlow.State state, boolean onAttach, @Nullable ViewGroup parent) {

        while (state != getStateFlow().getState()) {
            StateFlow.State nextState = StateFlow.State.Detached;
            switch (getStateFlow().getState()) {
                case WillAttach:
                    nextState = onAttach ? StateFlow.State.Attached : StateFlow.State.WillDetach;
                    break;
                case Attached:
                    nextState = onAttach ? StateFlow.State.WillAppear : StateFlow.State.WillDetach;
                    break;
                case WillAppear:
                    nextState = onAttach ? StateFlow.State.Appeared : StateFlow.State.WillDetach;
                    break;
                case Appeared:
                    nextState = onAttach ? null : StateFlow.State.WillDisappear;
                    break;
                case WillDisappear:
                    nextState = onAttach ? StateFlow.State.WillAppear : StateFlow.State.Disappeared;
                    break;
                case Disappeared:
                    nextState = onAttach ? StateFlow.State.WillAppear : StateFlow.State.WillDetach;
                    break;
                case WillDetach:
                    nextState = onAttach ? StateFlow.State.WillAttach : StateFlow.State.Detached;
                    break;
                case Detached:
                    nextState = onAttach ? StateFlow.State.WillAttach : null;
                    break;
            }

            if (nextState == null) {
                return;
            }

            getStateFlow().setState(nextState);

            switch (nextState) {
                case WillAttach:
                    onViewWillAttachToParent(parent);
                    break;
                case Attached:
                    onViewAttachedToParent(parent);
                    break;
                case WillAppear:
                    onViewWillAppear();
                    break;
                case Appeared:
                    onViewAppeared();
                    break;
                case WillDisappear:
                    onViewWillDisappear();
                    break;
                case Disappeared:
                    onViewDisappeared();
                    break;
                case WillDetach:
                    onViewWillDetachFromParent(parent);
                    break;
                case Detached:
                    onViewDetachedFromParent(parent);
                    break;
            }
            internalNotifyStateChangeListeners(nextState, parent);
        }
    }

    private void internalNotifyStateChangeListeners(StateFlow.State state, ViewGroup parent) {
        ArrayList<OnViewStateChangeListener> copyList =
                (ArrayList<OnViewStateChangeListener>) getOnViewStateChangeListeners().clone();
        int size = copyList.size();
        for (int i = 0; i < size; ++i) {
            switch (state) {
                case WillAttach:
                    copyList.get(i).onViewWillAttachToParent(this, parent);
                    break;
                case Attached:
                    copyList.get(i).onViewAttachedToParent(this, parent);
                    break;
                case WillAppear:
                    copyList.get(i).onViewWillAppear(this);
                    break;
                case Appeared:
                    copyList.get(i).onViewAppeared(this);
                    break;
                case WillDisappear:
                    copyList.get(i).onViewWillDisappear(this);
                    break;
                case Disappeared:
                    copyList.get(i).onViewDisappeared(this);
                    break;
                case WillDetach:
                    copyList.get(i).onViewWillDetachFromParent(this, parent);
                    break;
                case Detached:
                    copyList.get(i).onViewDetachedFromParent(this, parent);
                    break;
            }
        }
    }
}