package com.vilyever.androidpopupcontroller;

import android.app.Application;
import android.content.ComponentCallbacks;

import com.vilyever.contextholder.VDContextHolder;

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
        VDContextHolder.initial(self);

//        ACRA.init(this);
//        HttpSender httpSender = new HttpSender(HttpSender.Method.PUT,
//                HttpSender.Type.JSON, "http://192.168.2.233:5984/acra-vilyever/_design/acra-storage/_update/report", null);
//        httpSender.setBasicAuth("vilyever", "vilyever");
//        ACRA.getErrorReporter().addReportSender(httpSender);

//        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
//            @Override
//            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//                System.out.println("onActivityCreated " + activity);
//            }
//
//            @Override
//            public void onActivityStarted(Activity activity) {
//                System.out.println("onActivityStarted " + activity);
//            }
//
//            @Override
//            public void onActivityResumed(Activity activity) {
//                System.out.println("onActivityResumed " + activity);
//            }
//
//            @Override
//            public void onActivityPaused(Activity activity) {
//                System.out.println("onActivityPaused " + activity);
//            }
//
//            @Override
//            public void onActivityStopped(Activity activity) {
//                System.out.println("onActivityStopped " + activity);
//            }
//
//            @Override
//            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//
//            }
//
//            @Override
//            public void onActivityDestroyed(Activity activity) {
//                System.out.println("onActivityDestroyed " + activity);
//            }
//        });
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