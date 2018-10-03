package com.skoumal.teanity.example.util

import android.util.Patterns
import com.skoumal.teanity.example.Config
import com.skoumal.teanity.example.R
import com.skoumal.teanity.util.KObservableField

fun String.isEmail(errorField: KObservableField<String>): Boolean =
    Patterns.EMAIL_ADDRESS.matcher(this).matches().apply {
        errorField.value = if (this) "" else R.string.login_email_error.string
    }

fun String.isPassword(errorField: KObservableField<String>): Boolean =
    this.isPassword(-1).apply {
        errorField.value = if (this) "" else R.string.login_password_error.string
    }

private val Int.string get() = Config.context.getString(this)

private fun String.isPassword(length: Int = 0): Boolean =
    if (length == -1) isNotBlank()
    else "^(?=.*?[a-z])(?=.*?[0-9]).{$length,}\$".let { Regex(it) }.matches(this)