package com.skoumal.teanity.example.data.repository

import android.net.ConnectivityManager
import com.skoumal.teanity.api.ApiXBuilder
import com.skoumal.teanity.api.build
import com.skoumal.teanity.example.Config
import com.skoumal.teanity.example.data.network.ApiServices
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RegistrationRepository(
    private val api: ApiServices,
    private val cm: ConnectivityManager
) {

    fun login(helper: Login.() -> Unit): Completable = Login(helper)
        .build()
        .afterEvaluate(Completable.complete())
        .delay(1000, TimeUnit.MILLISECONDS, Schedulers.computation(), true)
        .doOnComplete { Config.token = "token" }

    fun logout(): Completable = Completable.complete()
        .delay(1000, TimeUnit.MILLISECONDS)
        .doOnComplete { Config.token = "" }

    class Login : ApiXBuilder<Login>() {
        var email: String = ""
        var password: String = ""

        companion object : ApiXBuilder.Creator<Login>(Login::class)
    }

}