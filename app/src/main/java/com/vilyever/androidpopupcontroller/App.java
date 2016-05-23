package com.vilyever.androidpopupcontroller;

import android.app.Application;
import android.content.ComponentCallbacks;

import com.vilyever.popupcontroller.ViewControllerManager;

/**
 * App
 * AndroidPopupController <com.vilyever.androidpopupcontroller>
 * Created by vilyever on 2016/2/26.
 * Feature:
 */
public class App extends Application {
    final App self = this;

    /* Public Methods */

    /* Overrides */
    @Override
    public void onCreate() {
        super.onCreate();

//        ACRA.init(this);
//        HttpSender httpSender = new HttpSender(HttpSender.Method.PUT,
//                HttpSender.Type.JSON, "http://192.168.2.233:5984/acra-vilyever/_design/acra-storage/_update/report", null);
//        httpSender.setBasicAuth("vilyever", "vilyever");
//        ACRA.getErrorReporter().addReportSender(httpSender);

        ViewControllerManager.initialize(this);
    }

    @Override
    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.registerActivityLifecycleCallbacks(callback);
        System.out.println("registerActivityLifecycleCallbacks " + callback);
    }

    @Override
    public void registerComponentCallbacks(ComponentCallbacks callback) {
        super.registerComponentCallbacks(callback);
        System.out.println("registerComponentCallbacks " + callback);
    }

    @Override
    public void registerOnProvideAssistDataListener(OnProvideAssistDataListener callback) {
        super.registerOnProvideAssistDataListener(callback);
        System.out.println("registerOnProvideAssistDataListener " + callback);
    }
}