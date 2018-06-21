package com.skoumal.teanity.model

import android.content.Context
import android.net.ConnectivityManager
import android.util.Patterns
import com.skoumal.teanity.Config
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ModelImpl(
    private val context: Context
) : Model {

    override fun verifyEmail(email: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false
        }

        return true
    }

    override fun login(email: String): Completable {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected) {
            Completable.complete()
        } else {
            Completable.error(IllegalStateException())
        }
            .delay(1000, TimeUnit.MILLISECONDS, Schedulers.computation(), true)
            .doOnComplete { Config.token = "token" }
    }

    override fun logout(): Completable {
        return Completable.complete()
            .delay(1000, TimeUnit.MILLISECONDS)
            .doOnComplete { Config.token = "" }
    }
}