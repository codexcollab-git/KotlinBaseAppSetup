package com.lokesh.appsetup.application

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.lokesh.appsetup.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

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
    }
}
