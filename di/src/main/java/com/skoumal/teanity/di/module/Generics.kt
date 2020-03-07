package com.skoumal.teanity.di.module

import android.app.*
import android.app.job.JobScheduler
import android.app.usage.NetworkStatsManager
import android.location.LocationManager
import android.media.AudioManager
import android.media.MediaRouter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.Vibrator
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val genericModule = module {
    /* Resources */
    single { androidContext().resources }
    single { androidContext().assets }

    /* All available system services */
    single { androidContext().getSystemService<WindowManager>() }
    single { androidContext().getSystemService<LayoutInflater>() }
    single { androidContext().getSystemService<ActivityManager>() }
    single { androidContext().getSystemService<NotificationManager>() }
    single { androidContext().getSystemService<KeyguardManager>() }
    single { androidContext().getSystemService<LocationManager>() }
    single { androidContext().getSystemService<SearchManager>() }
    single { androidContext().getSystemService<Vibrator>() }
    single { androidContext().getSystemService<ConnectivityManager>() }
    single { androidContext().getSystemService<WifiManager>() }
    single { androidContext().getSystemService<AudioManager>() }
    single { androidContext().getSystemService<MediaRouter>() }
    single { androidContext().getSystemService<TelephonyManager>() }
    single { androidContext().getSystemService<InputMethodManager>() }
    single { androidContext().getSystemService<UiModeManager>() }
    single { androidContext().getSystemService<DownloadManager>() }
    single { androidContext().getSystemService<BatteryManager>() }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        single { androidContext().getSystemService<JobScheduler>() }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        single { androidContext().getSystemService<SubscriptionManager>() }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        single { androidContext().getSystemService<NetworkStatsManager>() }
    }
}