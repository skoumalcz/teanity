package com.skoumal.teanity.example.data.repository

import android.net.ConnectivityManager
import com.skoumal.teanity.api.Result
import com.skoumal.teanity.example.Config
import com.skoumal.teanity.example.data.network.ApiServices
import com.skoumal.teanity.example.model.entity.outbound.Login
import com.skoumal.teanity.example.util.retrofit.awaitResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class RegistrationRepository(
    private val api: ApiServices,
    private val cm: ConnectivityManager
) {

    suspend fun login(login: Login) = withContext(Dispatchers.IO) {
        val result = if (cm.activeNetworkInfo?.isConnected == true) {
            delay(1000) // pretend to do some work, don't use in prod, lol
            api.login().awaitResult().let { Result.Success() } // login doesn't work so we default to success
        } else {
            Result.Error(IllegalStateException())
        }

        if (result.isSuccess) {
            Config.token = "token"
        }

        result
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        delay(1000) // pretend to do some work, don't use in prod, lol

        val result = Result.Success()

        if (result.isSuccess) {
            Config.token = ""
        }

        result
    }

}