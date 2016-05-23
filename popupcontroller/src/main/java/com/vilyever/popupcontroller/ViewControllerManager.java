package com.vilyever.popupcontroller;

import android.app.Application;

import com.vilyever.activityhelper.ActivityHelper;

/**
 * ViewControllerManager
 * Created by vilyever on 2016/5/23.
 * Feature:
 */
public class ViewControllerManager {
    final ViewControllerManager self = this;
    
    
    /* Constructors */
    private static ViewControllerManager instance;
    private synchronized static ViewControllerManager getInstance() {
        if (instance == null) {
            instance = new ViewControllerManager();
        }

        return instance;
    }
    
    /* Public Methods */
    /**
     * 初始化，必须在Application中调用
     * @param application 当前应用application
     */
    public static void initialize(Application application) {
        if (application == null) {
            throw new IllegalStateException("ViewControllerManager cannot initialize with null.");
        }

        if (!getInstance().isInitialized()) {
            ActivityHelper.initialize(application);
            getInstance().setInitialized(true);
        }
    }

    public static void checkInitialized() {
        getInstance().internalCheckInitialized();
    }
    
    /* Properties */
    private boolean initialized;
    protected ViewControllerManager setInitialized(boolean initialized) {
        this.initialized = initialized;
        return this;
    }
    protected boolean isInitialized() {
        return initialized;
    }
    
    /* Overrides */
    
    
    /* Delegates */
    
    
    /* Private Methods */
    /**
     * 检查是否初始化
     */
    private void internalCheckInitialized() {
        if (!isInitialized()) {
            throw new IllegalStateException("ViewControllerManager is not initialized, which must initialize with application.");
        }
    }
}