package com.skoumal.teanity.extensions

import android.os.Handler
import android.os.Looper
import com.skoumal.teanity.tools.annotation.RemoveOnDeprecation

@Deprecated("Use coroutines with context = Dispatchers.Main")
@RemoveOnDeprecation("1.2")
fun ui(body: () -> Unit) = Handler(Looper.getMainLooper()).post(body)