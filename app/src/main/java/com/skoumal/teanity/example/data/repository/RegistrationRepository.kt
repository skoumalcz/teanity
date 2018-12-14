package com.skoumal.teanity.example.data.repository

import android.net.ConnectivityManager
import com.skoumal.teanity.api.ApiXEvaluator
import com.skoumal.teanity.example.Config
import com.skoumal.teanity.example.data.network.ApiServices
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RegistrationRepository(
    private val api: ApiServices,
    private val cm: ConnectivityManager
) {

    fun login(evaluatorHelper: Login.() -> Unit): Completable {
        val evaluator = Login().apply(evaluatorHelper)

        return if (evaluator.evaluate() && (cm.activeNetworkInfo?.isConnected == true)) {
            Completable.complete()
                .delay(1000, TimeUnit.MILLISECONDS, Schedulers.computation(), true)
                .doOnComplete { Config.token = "token" }
        } else {
            Completable.error(IllegalStateException())
        }

    }

    fun logout(): Completable {
        return Completable.complete()
            .delay(1000, TimeUnit.MILLISECONDS)
            .doOnComplete { Config.token = "" }
    }

    class Login : ApiXEvaluator<Login>() {
        var email: String = ""
        var password: String = ""
    }

}