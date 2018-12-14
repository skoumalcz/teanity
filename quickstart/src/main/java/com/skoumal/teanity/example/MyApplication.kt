package com.skoumal.teanity.example

import android.app.Application
import android.util.Log
import com.chibatching.kotpref.Kotpref
import com.facebook.stetho.Stetho
import com.skoumal.teanity.example.di.koinModules
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Kotpref.init(this)

        startKoin(this, koinModules)

        /*val core = CrashlyticsCore.Builder().disabled(Constants.DEBUG).build()
        Fabric.with(this, Crashlytics.Builder().core(core).build())*/

        if (Constants.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        if (Constants.DEBUG) {
            Timber.plant(debugTree)
        } else {
            Timber.plant(crashReportingTree)
        }
    }

    private val debugTree = Timber.DebugTree()

    private val crashReportingTree = object : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }

            //Crashlytics.log(priority, tag, message)

            if (t != null) {
                //Crashlytics.logException(t)
            }
        }
    }
}