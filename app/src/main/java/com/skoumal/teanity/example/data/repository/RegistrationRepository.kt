package com.skoumal.teanity.example.data.repository

import android.net.ConnectivityManager
import com.skoumal.teanity.example.Config
import com.skoumal.teanity.example.data.network.ApiServices
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RegistrationRepository(
    private val api: ApiServices,
    private val cm: ConnectivityManager
) {

    fun login(email: String, password: String): Completable {
        val result = if (cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected) {
            Completable.complete()
        } else {
            Completable.error(IllegalStateException())
        }
        return result
            .delay(1000, TimeUnit.MILLISECONDS, Schedulers.computation(), true)
            .doOnComplete { Config.token = "token" }
    }

    fun logout(): Completable {
        return Completable.complete()
            .delay(1000, TimeUnit.MILLISECONDS)
            .doOnComplete { Config.token = "" }
    }

}