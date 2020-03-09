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
    factory { androidContext().resources }
    factory { androidContext().assets }

    /* All available system services */
    factory { androidContext().getSystemService<WindowManager>() }
    factory { androidContext().getSystemService<LayoutInflater>() }
    factory { androidContext().getSystemService<ActivityManager>() }
    factory { androidContext().getSystemService<NotificationManager>() }
    factory { androidContext().getSystemService<KeyguardManager>() }
    factory { androidContext().getSystemService<LocationManager>() }
    factory { androidContext().getSystemService<SearchManager>() }
    factory { androidContext().getSystemService<Vibrator>() }
    factory { androidContext().getSystemService<ConnectivityManager>() }
    factory { androidContext().getSystemService<WifiManager>() }
    factory { androidContext().getSystemService<AudioManager>() }
    factory { androidContext().getSystemService<MediaRouter>() }
    factory { androidContext().getSystemService<TelephonyManager>() }
    factory { androidContext().getSystemService<InputMethodManager>() }
    factory { androidContext().getSystemService<UiModeManager>() }
    factory { androidContext().getSystemService<DownloadManager>() }
    factory { androidContext().getSystemService<BatteryManager>() }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        factory { androidContext().getSystemService<JobScheduler>() }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        factory { androidContext().getSystemService<SubscriptionManager>() }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        factory { androidContext().getSystemService<NetworkStatsManager>() }
    }
}