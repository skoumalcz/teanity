package com.skoumal.teanity.example.data.repository

import android.net.ConnectivityManager
import com.skoumal.teanity.api.ApiXEvaluator
import com.skoumal.teanity.example.model.entity.Result
import com.skoumal.teanity.example.Config
import com.skoumal.teanity.example.data.network.ApiServices
import kotlinx.coroutines.delay

class RegistrationRepository(
    private val api: ApiServices,
    private val cm: ConnectivityManager
) {

    suspend fun login(evaluatorHelper: Login.() -> Unit): Result<Unit> {
        val evaluator = Login().apply(evaluatorHelper)

        val result = if (evaluator.evaluate() && (cm.activeNetworkInfo?.isConnected == true)) {
            delay(1000)
            Result.Success()
        } else {
            Result.Error(IllegalStateException())
        }

        if (result.isSuccess) {
            Config.token = "token"
        }

        return result
    }

    suspend fun logout(): Result<Unit> {
        delay(1000)

        val result = Result.Success()

        if (result.isSuccess) {
            Config.token = ""
        }

        return result
    }

    class Login : ApiXEvaluator<Login>() {
        var email: String = ""
        var password: String = ""
    }

}