package com.skoumal.teanity.network.exception

import okhttp3.ResponseBody
import java.io.IOException

class NetworkException @JvmOverloads constructor(message: String, cause: Throwable? = null) :
    IOException(message, cause) {

    private var errorBody: String? = null

    constructor(errorBody: ResponseBody?) : this("Encountered error whilst fetching from remote") {
        this.errorBody = errorBody?.string()
    }

}