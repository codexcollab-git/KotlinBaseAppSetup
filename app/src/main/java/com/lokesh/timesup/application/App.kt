package com.lokesh.timesup.application

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.lokesh.timesup.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(), Application.ActivityLifecycleCallbacks {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    companion object {
        private var appContext: Context? = null
        private lateinit var app: Application
        fun getAppContext() = app
        fun getCurrentActivity() = appContext
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        if (!BuildConfig.DEBUG) {
            FirebaseApp.initializeApp(this)
            firebaseAnalytics = Firebase.analytics
        }
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        appContext = activity
    }

    override fun onActivityStarted(activity: Activity) {
        appContext = activity
    }

    override fun onActivityResumed(activity: Activity) {
        appContext = activity
    }

    override fun onActivityPaused(activity: Activity) {
        appContext = null
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}
