package com.skoumal.teanity.di.module

import android.app.*
import android.app.job.JobScheduler
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.location.LocationManager
import android.media.AudioManager
import android.media.MediaRouter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Vibrator
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import org.koin.dsl.module

internal val genericModule = module {
    /* Resources */
    single { get<Context>().resources }
    single { get<Context>().assets }

    /* All available system services */
    single { get<Context>().getSystemService<WindowManager>() }
    single { get<Context>().getSystemService<LayoutInflater>() }
    single { get<Context>().getSystemService<ActivityManager>() }
    single { get<Context>().getSystemService<NotificationManager>() }
    single { get<Context>().getSystemService<KeyguardManager>() }
    single { get<Context>().getSystemService<LocationManager>() }
    single { get<Context>().getSystemService<SearchManager>() }
    single { get<Context>().getSystemService<Vibrator>() }
    single { get<Context>().getSystemService<ConnectivityManager>() }
    single { get<Context>().getSystemService<WifiManager>() }
    single { get<Context>().getSystemService<AudioManager>() }
    single { get<Context>().getSystemService<MediaRouter>() }
    single { get<Context>().getSystemService<TelephonyManager>() }
    single { get<Context>().getSystemService<SubscriptionManager>() }
    single { get<Context>().getSystemService<InputMethodManager>() }
    single { get<Context>().getSystemService<UiModeManager>() }
    single { get<Context>().getSystemService<DownloadManager>() }
    single { get<Context>().getSystemService<BatteryManager>() }
    single { get<Context>().getSystemService<JobScheduler>() }
    single { get<Context>().getSystemService<NetworkStatsManager>() }
}