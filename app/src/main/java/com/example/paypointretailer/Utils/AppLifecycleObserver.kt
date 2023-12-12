package com.example.paypointretailer.Utils

import android.app.Activity
import android.app.Application
import android.os.Bundle

class AppLifecycleObserver : Application.ActivityLifecycleCallbacks {

    private var appInForeground = false

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {
        appInForeground = true
    }

    override fun onActivityPaused(activity: Activity) {
        appInForeground = false
    }

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

    fun isAppInForeground(): Boolean {
        return appInForeground
    }
}