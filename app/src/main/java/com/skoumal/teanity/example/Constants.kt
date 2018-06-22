package com.skoumal.teanity.example

object Constants {

    const val DEBUG = BuildConfig.BUILD_TYPE == "debug"
    const val ALPHA = BuildConfig.BUILD_TYPE == "alpha"
    const val BETA = BuildConfig.BUILD_TYPE == "beta"
    const val RELEASE = BuildConfig.BUILD_TYPE == "release"

    const val API_ACCESS_KEY = "b2aca767ea814ee517645716525ccb49bfdfbe05a8bf1f2dfea7830e6e303483"

    private const val API_URL_ALPHA = "https://api.unsplash.com"
    private const val API_URL_BETA = "https://api.unsplash.com"
    private const val API_URL_RELEASE = "https://api.unsplash.com"

    val API_URL = when {
        RELEASE -> API_URL_RELEASE
        BETA -> API_URL_BETA
        else -> API_URL_ALPHA
    }
}