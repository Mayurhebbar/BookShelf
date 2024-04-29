package com.example.bookshelf.ui

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LifecycleObserver

class BookShelfApplication : Application() ,LifecycleObserver{
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(ApplicationLifeCycleTracker())
    }
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }



}

class ApplicationLifeCycleTracker : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        //do nothing
    }

    override fun onActivityStarted(p0: Activity) {
        //do nothing
    }

    override fun onActivityResumed(p0: Activity) {
        //do nothing
    }

    override fun onActivityPaused(p0: Activity) {
    //do nothing
    }

    override fun onActivityStopped(p0: Activity) {
        //do nothing
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        //do nothing
    }


    override fun onActivityDestroyed(p0: Activity) {
        //do nothing
    }
}