package com.skoumal.teanity.example.model

import io.reactivex.Completable

interface Model {

    // return true if provided email is valid, false otherwise
    fun verifyEmail(email: String): Boolean

    fun login(email: String): Completable

    fun logout(): Completable
}