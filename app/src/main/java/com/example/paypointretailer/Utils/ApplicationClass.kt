package com.example.paypointretailer.Utils

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass : Application() {
    val appLifecycleObserver = AppLifecycleObserver()
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(AppLifecycleObserver())
    }
}